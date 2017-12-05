package com.ft4sua.sutdapp1d;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ft4sua.sutdapp1d.DatabasePackage.Event;

/**
 * Created by Lim Ken Jyi on 5/12/2017.
 */

//public class SectionPagerAdapter extends FragmentPagerAdapter {
//    final int PAGE_COUNT = 3;
//    private String tabTitles[] = new String[]{getString(R.string.main_tab), getString(R.string.locator_tab)};
//
//    public SectionPagerAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        switch (position) {
//            case 0:
//            default:
//                return TimeTableFragment.newInstance(new Event());
//            case 1:
//                return TimeTableFragment.newInstance();
//            case 2:
//                return TimeTableFragment.newInstance();
//        }
//    }
//    @Override
//    public int getCount() {
//        return PAGE_COUNT;
//    }
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTitles[position];
//    }
//}
