package com.jimmy.htmlplayer.ui.views.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.jimmy.htmlplayer.ui.views.fragments.ScreenSlidePageFragment;

import java.util.ArrayList;
import java.util.HashMap;

import static com.jimmy.htmlplayer.ui.UIConfig.chapTitlesArr;
import static com.jimmy.htmlplayer.ui.UIConfig.selectedSet;
import static com.jimmy.htmlplayer.ui.UIConfig.setsList;


public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

	public SparseArray<ScreenSlidePageFragment> registeredFragments;

	public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);

		registeredFragments = new SparseArray<>();
		// firstRun = true;
	}

	@Override
	public ScreenSlidePageFragment getItem(int position) {
		return ScreenSlidePageFragment.newInstance(position);
	}

	@Override
	public int getCount() {
//		Log.e("getCount", ( (ArrayList) ( (HashMap) setsList.get(selectedSet)).get( chapTitlesArr[selectedSet - 1])).size() + "");
		return ( (ArrayList) ( (HashMap) setsList.get(selectedSet)).get( chapTitlesArr[selectedSet - 1])).size();

	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public ScreenSlidePageFragment instantiateItem(ViewGroup container, int position) {

		ScreenSlidePageFragment frg = (ScreenSlidePageFragment) super.instantiateItem(container, position);
		registeredFragments.put(position, frg);

		return frg;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);

	}

	public ScreenSlidePageFragment getRegisteredFrgament(int position) {
		return (ScreenSlidePageFragment) registeredFragments.get(position);
	}

	/*
	 * public void clearAll(){ for(int i=0; i<registeredFragments.size();
	 * i++){ ScreenSlidePageFragment frag = registeredFragments.get(i);
	 * fmg.beginTransaction().remove(frag).commit(); }
	 * registeredFragments.clear(); }
	 */

}
