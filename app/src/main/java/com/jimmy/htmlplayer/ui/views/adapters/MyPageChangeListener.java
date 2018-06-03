package com.jimmy.htmlplayer.ui.views.adapters;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.jimmy.htmlplayer.datahandlers.pojo.HTMLObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.media.CamcorderProfile.get;
import static com.jimmy.htmlplayer.ui.UIConfig.chapTitlesArr;
import static com.jimmy.htmlplayer.ui.UIConfig.selectedSet;
import static com.jimmy.htmlplayer.ui.UIConfig.setsList;

public class MyPageChangeListener extends SimpleOnPageChangeListener {

	private final EventsDelegate evDel;

	private boolean updatedSlideSelection = false;


	private int currentPos = 0;


	private static ViewPager mViewPager;
	private static ScreenSlidePagerAdapter mPagerAdapter;



	public interface EventsDelegate{

		 void pageSelected(int pos);
		 void pageScrolled(int position, float positionOffset, int positionOffsetPixels);
		 void pageStateChnage(int state);
	}

	public MyPageChangeListener(ViewPager viewPager, ScreenSlidePagerAdapter pagerAdapter,EventsDelegate evDel_ ) {

		mViewPager = viewPager;
		mPagerAdapter = pagerAdapter;
		evDel = evDel_;
	}

	@Override
	public void onPageSelected(int position) {
		// Log.d("ON PAGE SELECTED", " page selected: " + position +
		// ", pager positon " + mPager.getCurrentItem());

		updatedSlideSelection = true;

		currentPos = mViewPager.getCurrentItem();

		evDel.pageSelected(position);

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		// Log.d("ONPAGESCROLLED", " page scrolled: " + position);

		super.onPageScrolled(position, positionOffset, positionOffsetPixels);
		evDel.pageScrolled(position, positionOffset, positionOffsetPixels);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

		WebView web = ((ScreenSlidePagerAdapter) mPagerAdapter)
				.getRegisteredFrgament(mViewPager.getCurrentItem()).webView;

		// Log.d("ON PAGE SCROLL STATE CHNAGED", " page state: " + state +
		// ", pager position: " + mPager.getCurrentItem() + ", total pages "
		// + NUM_PAGES + "{"+ "currentPos : " +currentPos +
		// ", previousState : " + previousPos + "--------- " +
		// frg.urlLoadedOnToch + "/" + web.getUrl() +"}");
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			if (updatedSlideSelection == true) {
				updatedSlideSelection = false;
				if (mPagerAdapter.registeredFragments.get(mViewPager.getCurrentItem() - 1) != null) {

					mPagerAdapter.registeredFragments.get(mViewPager.getCurrentItem() - 1).webView.setVisibility(View.INVISIBLE);
					mPagerAdapter.registeredFragments.get(mViewPager.getCurrentItem() - 1).webView.reload();
					mPagerAdapter.registeredFragments.get(mViewPager.getCurrentItem() - 1).getDecView().setVisibility(View.VISIBLE);
				}

				((ScreenSlidePagerAdapter) mPagerAdapter).getRegisteredFrgament(mViewPager.getCurrentItem()).getDecView().setVisibility(View.GONE);
				((ScreenSlidePagerAdapter) mPagerAdapter).getRegisteredFrgament(mViewPager.getCurrentItem()).getwebView().setVisibility(View.VISIBLE);

				((ScreenSlidePagerAdapter) mPagerAdapter).getRegisteredFrgament(mViewPager.getCurrentItem()).getwebView().loadUrl("javascript:testFunc('Java android');");
			}

//			previousPos = currentPos;
			// Log.d("MyPageChangeListener - onPageScrollStateChanged - idle",
			// " the current url " + web.getUrl() + ", the position: " +
			// mPager.getCurrentItem());

		} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
			if (mViewPager.getCurrentItem() == 0) {
				HashMap selectedSetMap = ((HashMap) setsList.get(selectedSet));
				ArrayList htmlpagesArr = ( (ArrayList) ( (HashMap) setsList.get(selectedSet )).get(chapTitlesArr[selectedSet - 1]));
				web.loadUrl(((HTMLObject) htmlpagesArr.get(0)).getHtml());
			}

			currentPos = mViewPager.getCurrentItem();

		} else if (state == ViewPager.SCROLL_STATE_DRAGGING) {

		}

//		previousState = state;

		evDel.pageStateChnage(state);

		super.onPageScrollStateChanged(state);
	}

	public static void updateView(int i) {
		/*
		 * No swipe animation, and no onPageChanged for the page in the middle
		 */
		mViewPager.setCurrentItem(i, false);

		WebView web = ((ScreenSlidePagerAdapter) mPagerAdapter).getRegisteredFrgament(mViewPager.getCurrentItem()).webView;

		((ScreenSlidePagerAdapter) mPagerAdapter)
				.getRegisteredFrgament(mViewPager.getCurrentItem())
				.getDecView().setVisibility(View.GONE);
		web.setVisibility(View.VISIBLE);
		web.loadUrl("javascript:testFunc('Hello from Java android');");

	}

}
