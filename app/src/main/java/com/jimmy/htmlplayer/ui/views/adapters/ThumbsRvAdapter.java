package com.jimmy.htmlplayer.ui.views.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimmy.htmlplayer.R;
import com.jimmy.htmlplayer.databinding.RecyclerItemBinding;
import com.jimmy.htmlplayer.datahandlers.pojo.HTMLObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ThumbsRvAdapter extends RecyclerView.Adapter<ThumbsRvAdapter.ViewHolder> {

    private Context ctx;
    private List<HTMLObject> lstObj;
    private PostsAdapterListener postsAdapterListener;
    private LayoutInflater layoutInflater;
    


    public interface PostsAdapterListener {
        void onPostClicked(HTMLObject htmlObject, int pos);
    }

    public ThumbsRvAdapter(List<HTMLObject> lstObj, PostsAdapterListener postsAdapterListener) {
        this.lstObj = lstObj;
        this.postsAdapterListener = postsAdapterListener;
    }

    public ThumbsRvAdapter(Context ctx, List<HTMLObject> lstObj, PostsAdapterListener postsAdapterListener) {
        this.ctx = ctx;
        this.lstObj = lstObj;
//        Log.e("V", lstObj.size()+"");
        this.postsAdapterListener = postsAdapterListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        RecyclerItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.recycler_item,
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HTMLObject htmlObj = lstObj.get(position);
        holder.recyclerItemBinding.setHtmlObj(htmlObj);
        Bitmap b = getBitmapFromAsset(ctx, htmlObj.getThumb());
        holder.recyclerItemBinding.imageView.setImageBitmap(b);
        holder.recyclerItemBinding.imageView.setOnClickListener(v -> {
            postsAdapterListener.onPostClicked(htmlObj, position);

        });

    }

    @Override
    public int getItemCount() {
        return lstObj.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RecyclerItemBinding recyclerItemBinding;

        public ViewHolder(RecyclerItemBinding recyclerItemBinding) {
            super(recyclerItemBinding.getRoot());
            this.recyclerItemBinding = recyclerItemBinding;

        }
    }


    public void updateAdapter(List<HTMLObject> lstObj){
        this.lstObj = lstObj;
        notifyDataSetChanged();
    }


    private static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
            istr.close();
        } catch (IOException e) {
            Log.e("FUCK", e.getMessage());
        }

        return bitmap;
    }
}
