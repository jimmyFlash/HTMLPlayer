package com.jimmy.htmlplayer.datahandlers.adapters;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by jamal.safwat on 9/19/2016.
 */
public class SectionPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{

    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
