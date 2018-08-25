package com.jimmy.htmlplayer.ui.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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


public class PagerFragment extends Fragment implements View.OnClickListener{

	public static ViewPager mViewPager;  // viewpager in layout ref
	private ScreenSlidePagerAdapter mPagerAdapter; // the adapter ref
	private MyPageChangeListener mPagerListener; // viewpager swipe listener
	private RecyclerView rv;

	private static int pagerPos = 0;// holder for pages position
	private ImageButton btnNext, btnFinish;
	private ImageView strImg;
	private PagerComponent pager_indicator;

//	private int dotsCount;
//	private ImageView[] dots;

	private Context act;


	private ArrayList list;
	private ConstraintLayout constrain1;
	private boolean changed  = false;


	public static Fragment newInstance() {
		pagerPos = 0;
		return new PagerFragment();
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

		//todo enable / add drawable compat lib and chnage icon up/down based on press
		strImg.setOnClickListener(v -> {

            TransitionManager.beginDelayedTransition(constrain1, transition);
            ConstraintSet constraint =  (changed)? constraintSet1 : constraintSet2;
            constraint.applyTo(constrain1);

            changed = !changed;
        });

		btnNext = (ImageButton) rootView.findViewById(R.id.btn_next);
		btnFinish = (ImageButton) rootView.findViewById(R.id.btn_finish);
		pager_indicator = (PagerComponent) rootView.findViewById(R.id.viewPagerIndicatorComp);
		
		rv = (RecyclerView) rootView.findViewById(R.id.header);
		rv.addItemDecoration(new DividerItemDecoration(rv.getContext(), DividerItemDecoration.HORIZONTAL));
		rv.setLayoutManager(new LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false));


		int arrObjLen = ((ArrayList) ((HashMap) setsList.get(selectedSet)).get(chapTitlesArr[selectedSet - 1])).size();
		Log.e("Length of Obj arr", arrObjLen + "");
		 list = (ArrayList) ((HashMap) setsList.get(selectedSet)).get(chapTitlesArr[selectedSet - 1]);
		ThumbsRvAdapter thumbsAdapter = new ThumbsRvAdapter(getActivity(), list, (htmlObject, pos) ->
				MyPageChangeListener.updateView(pos));
		rv.setAdapter(thumbsAdapter);

//		Log.e("getCount", ( (ArrayList) ( (HashMap) setsList.get(selectedSet)).get( chapTitlesArr[selectedSet - 1])).size() + "");


		// get the page portion to scroll to
		if (savedInstanceState != null) pagerPos = savedInstanceState.getInt(UIConstants.KEY_CURRENT_PAGE);

		mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(pagerPos);



		mPagerListener = new MyPageChangeListener(mViewPager, mPagerAdapter, new MyPageChangeListener.EventsDelegate() {
			@Override
			public void pageSelected(int position) {
				pager_indicator.pageSelected( position);

			}

			@Override
			public void pageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void pageStateChnage(int state) {

			}
		});
		mViewPager.addOnPageChangeListener(mPagerListener);


		pager_indicator.setUiPageViewController(mPagerAdapter, mViewPager);

		return rootView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		Log.d("current page", "val: " + mViewPager.getCurrentItem());
		outState.putInt(UIConstants.KEY_CURRENT_PAGE, mViewPager.getCurrentItem());
	}

	@Override
	public void onClick(View v) {

		/*switch (v.getId()) {
			case R.id.btn_next:
				mViewPager.setCurrentItem((mViewPager.getCurrentItem() < dotsCount) ? mViewPager.getCurrentItem() + 1 : 0);
				break;

			case R.id.btn_finish:
				// getActivity().finish();
				break;
		}*/

	}

}
