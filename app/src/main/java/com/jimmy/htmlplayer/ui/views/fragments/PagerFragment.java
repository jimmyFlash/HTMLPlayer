package com.jimmy.htmlplayer.ui.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jimmy.htmlplayer.R;
import com.jimmy.htmlplayer.datahandlers.pojo.HTMLObject;
import com.jimmy.htmlplayer.ui.UIConstants;
import com.jimmy.htmlplayer.ui.views.activities.PagerComponent;
import com.jimmy.htmlplayer.ui.views.adapters.MyPageChangeListener;
import com.jimmy.htmlplayer.ui.views.adapters.ScreenSlidePagerAdapter;
import com.jimmy.htmlplayer.ui.views.adapters.ThumbsRvAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import static com.jimmy.htmlplayer.ui.UIConfig.chapTitlesArr;
import static com.jimmy.htmlplayer.ui.UIConfig.selectedSet;
import static com.jimmy.htmlplayer.ui.UIConfig.setsList;


public class PagerFragment extends Fragment{

	public static ViewPager mViewPager;  // viewpager in layout ref
	private static PagerFragment frgIns;
	private ScreenSlidePagerAdapter mPagerAdapter; // the adapter ref
	private MyPageChangeListener mPagerListener; // viewpager swipe listener
	private RecyclerView rv;

	private ImageButton btnNext, btnFinish;
	private ImageView strImg;
	private PagerComponent pager_indicator;

//	private int dotsCount;
//	private ImageView[] dots;

	private Context act;


	private ArrayList list;
	private ConstraintLayout constrain1;
	private boolean changed  = false;
	private static final String KEY_CURRENT_SLIDE_NO = "currentSlideNo";
	private int currentPos = 0;


	public static Fragment newInstance(int slideNo) {
		frgIns = new PagerFragment();
		if(slideNo > 0){
			Bundle args = new Bundle();
			args.putInt(KEY_CURRENT_SLIDE_NO, slideNo);
			frgIns.setArguments(args);
		}
		return frgIns;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(getArguments() != null)currentPos = getArguments().getInt(KEY_CURRENT_SLIDE_NO);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pager_main, container, false);
		act= getActivity();
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		// mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		// mPager.setPageTransformer(true, new DepthPageTransformer());
		// mPager.setOffscreenPageLimit(0);

		strImg = (ImageView) rootView.findViewById(R.id.sub_set);

		constrain1 = (ConstraintLayout) rootView.findViewById(R.id.constrain_set1);

		ConstraintSet constraintSet1 = new ConstraintSet();
		constraintSet1.clone(constrain1);
		ConstraintSet constraintSet2 = new ConstraintSet();
		constraintSet2.clone(constrain1);
		constraintSet2.setVerticalBias(R.id.sub_set, (float) 1.0);

		AutoTransition transition = new AutoTransition();
		transition.setDuration(500);

		// set VectorDrawable for pre-Lollipop devices
		Resources resources = getActivity().getResources();
		Resources.Theme theme = getActivity().getTheme();
		VectorDrawableCompat downVec = VectorDrawableCompat.create(
				resources, R.drawable.ic_arrow_drop_down_black_24dp, theme);
		VectorDrawableCompat upVec = VectorDrawableCompat.create(
				resources, R.drawable.ic_arrow_drop_up_black_24dp, theme);

		strImg.setOnClickListener(v -> {

            TransitionManager.beginDelayedTransition(constrain1, transition);
            ConstraintSet constraint =  (changed)? constraintSet1 : constraintSet2;
            constraint.applyTo(constrain1);

            changed = !changed;

			strImg.setImageDrawable((changed)? upVec : downVec);
        });

		btnNext = (ImageButton) rootView.findViewById(R.id.btn_next);
		btnFinish = (ImageButton) rootView.findViewById(R.id.btn_finish);
		pager_indicator = (PagerComponent) rootView.findViewById(R.id.viewPagerIndicatorComp);

		
		rv = (RecyclerView) rootView.findViewById(R.id.header);
		rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.HORIZONTAL));
		rv.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false));

		int arrObjLen = ((ArrayList) ((HashMap) setsList.get(selectedSet)).get(chapTitlesArr[selectedSet - 1])).size();
//		Log.e("Length of Obj arr", arrObjLen + "");

		list = (ArrayList) ((HashMap) setsList.get(selectedSet)).get(chapTitlesArr[selectedSet - 1]);
		ThumbsRvAdapter thumbsAdapter = new ThumbsRvAdapter(getActivity(), list, (htmlObject, pos) ->
				MyPageChangeListener.updateView(pos));
		rv.setAdapter(thumbsAdapter);

//		Log.e("getCount", ( (ArrayList) ( (HashMap) setsList.get(selectedSet)).get( chapTitlesArr[selectedSet - 1])).size() + "");

		mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		if(currentPos > 0 )mViewPager.setCurrentItem(currentPos);



		mPagerListener = new MyPageChangeListener(mViewPager, mPagerAdapter, new MyPageChangeListener.EventsDelegate() {
			@Override
			public void pageSelected(int position) {
				pager_indicator.pageSelected( position);
			}

			@Override
			public void pageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

			@Override
			public void pageStateChnage(int state) {}
		});
		mViewPager.addOnPageChangeListener(mPagerListener);
		pager_indicator.setUiPageViewController(mPagerAdapter, mViewPager);

		return rootView;
	}

}
