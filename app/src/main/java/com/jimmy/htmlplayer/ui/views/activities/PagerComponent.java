package com.jimmy.htmlplayer.ui.views.activities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jimmy.htmlplayer.R;



/**
 * Created by jamal on 8/25/2017.
 */

public class PagerComponent extends RelativeLayout implements View.OnClickListener{

    private LinearLayout pager_indicator;
    private  ImageButton btnFinish;
    private  ImageButton btnNext;
    private RelativeLayout rl;
    private  Context cntxt;

    public int getDotsCount() {
        return dotsCount;
    }

    private int dotsCount = 0;
    private ImageView[] dots;

    public ViewPager getmViewPager() {
        return mViewPager;
    }


    private ViewPager mViewPager;

    private PagerCallBacks callbacks;
    public interface PagerCallBacks{

        public void nextBtnclick();

        public void tickBtnClicked(int pos);
    }

    public PagerComponent(Context context) {
        super(context);


    }

    public PagerComponent(Context context, AttributeSet attrs) {
        super(context, attrs);

        cntxt = context;

    /*    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Options, 0, 0);
        String titleText = a.getString(R.styleable.Options_titleText);
        int valueColor = a.getColor(R.styleable.Options_valueColor, Color.BLACK);
        a.recycle();
*/
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = inflater.inflate(R.layout.pager_component_layout, this, true);


        btnNext = (ImageButton) findViewById(R.id.btn_next);
        btnFinish = (ImageButton) findViewById(R.id.btn_finish);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        btnNext.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_next:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem((mViewPager.getCurrentItem() < dotsCount) ? mViewPager.getCurrentItem() + 1 : 0);
                }
                break;

            case R.id.btn_finish:
                if (mViewPager != null) {
                    mViewPager.setCurrentItem((mViewPager.getCurrentItem() > 0) ? mViewPager.getCurrentItem() - 1 : 0);
                }
                break;
        }
    }


    public void setUiPageViewController(FragmentStatePagerAdapter mPagerAdapter, ViewPager vPager) {

        dotsCount = mPagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        mViewPager = vPager;

      //  if (mViewPager != null) {
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(cntxt);

                dots[i].setImageDrawable(ContextCompat.getDrawable(cntxt, R.drawable.nonselecteditem_dot));


                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                pager_indicator.addView(dots[i], params);
            }

            dots[mViewPager.getCurrentItem()].setImageDrawable(ContextCompat.getDrawable(cntxt, R.drawable.selecteditem_dot));

            if (mViewPager.getCurrentItem() + 1 == dotsCount) {
                btnNext.setVisibility(View.GONE);

                btnFinish.setVisibility(View.VISIBLE);
            } else if(mViewPager.getCurrentItem() > 0){
               btnFinish.setVisibility(View.VISIBLE);

            } else {
                btnNext.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.GONE);

            }
       // }
    }


    public void pageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(cntxt, R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(ContextCompat.getDrawable(cntxt, R.drawable.selecteditem_dot));

        if (position + 1 == dotsCount) {
            btnNext.setVisibility(View.GONE);

            btnFinish.setVisibility(View.VISIBLE);

        } else if(position > 0){
            btnFinish.setVisibility(View.VISIBLE);

        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);

        }

    }
}
