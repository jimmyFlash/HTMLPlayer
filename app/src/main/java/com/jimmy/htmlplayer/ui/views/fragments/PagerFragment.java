package com.jimmy.htmlplayer.ui.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jimmy.htmlplayer.R;
import com.jimmy.htmlplayer.ui.UIConstants;
import com.jimmy.htmlplayer.ui.views.activities.PagerComponent;
import com.jimmy.htmlplayer.ui.views.adapters.MyPageChangeListener;
import com.jimmy.htmlplayer.ui.views.adapters.ScreenSlidePagerAdapter;


public class PagerFragment extends Fragment implements View.OnClickListener{

	public static ViewPager mViewPager;  // viewpager in layout ref
	private ScreenSlidePagerAdapter mPagerAdapter; // the adapter ref
	private MyPageChangeListener mPagerListener; // viewpager swipe listener

	public static int pagerPos = 0;// holder for pages position
	private ImageButton btnNext, btnFinish;
	private PagerComponent pager_indicator;

//	private int dotsCount;
//	private ImageView[] dots;

	private Context act;

	public static Fragment newInstance() {
		return new PagerFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.pager_main, container, false);

		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
		// mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		// mPager.setPageTransformer(true, new DepthPageTransformer());
		// mPager.setOffscreenPageLimit(0);

		btnNext = (ImageButton) rootView.findViewById(R.id.btn_next);
		btnFinish = (ImageButton) rootView.findViewById(R.id.btn_finish);
		pager_indicator = (PagerComponent) rootView.findViewById(R.id.viewPagerIndicatorComp);


/*
		btnNext.setOnClickListener(this);
		btnFinish.setOnClickListener(this);
*/
		act= getActivity();


		// get the page portion to scroll to
		if (savedInstanceState != null) pagerPos = savedInstanceState.getInt(UIConstants.KEY_CURRENT_PAGE);

		mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(pagerPos);



		mPagerListener = new MyPageChangeListener(mViewPager, mPagerAdapter, new MyPageChangeListener.EventsDelegate() {
			@Override
			public void pageSelected(int position) {
			/*
				for (int i = 0; i < dotsCount; i++) {
					dots[i].setImageDrawable(ContextCompat.getDrawable(act, R.drawable.nonselecteditem_dot));
				}

				dots[position].setImageDrawable(ContextCompat.getDrawable(act, R.drawable.selecteditem_dot));

				if (position + 1 == dotsCount) {
					btnNext.setVisibility(View.GONE);
					btnFinish.setVisibility(View.VISIBLE);
				} else {
					btnNext.setVisibility(View.VISIBLE);
					btnFinish.setVisibility(View.GONE);
				}*/

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

//		setUiPageViewController();

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

/*

	private void setUiPageViewController() {

		dotsCount = mPagerAdapter.getCount();
		dots = new ImageView[dotsCount];

		for (int i = 0; i < dotsCount; i++) {
			dots[i] = new ImageView(act);

			dots[i].setImageDrawable(ContextCompat.getDrawable(act, R.drawable.nonselecteditem_dot ));


			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT
			);

			params.setMargins(4, 0, 4, 0);

			pager_indicator.addView(dots[i], params);
		}

		dots[mViewPager.getCurrentItem()].setImageDrawable(ContextCompat.getDrawable(act, R.drawable.selecteditem_dot));

		if (mViewPager.getCurrentItem() + 1 == dotsCount) {
			btnNext.setVisibility(View.GONE);
			btnFinish.setVisibility(View.VISIBLE);
		} else {
			btnNext.setVisibility(View.VISIBLE);
			btnFinish.setVisibility(View.GONE);
		}
	}
*/


}
