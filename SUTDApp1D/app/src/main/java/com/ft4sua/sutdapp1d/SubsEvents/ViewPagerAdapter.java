package com.ft4sua.sutdapp1d.SubsEvents;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ft4sua.sutdapp1d.R;

/**
 * Created by swonlek on 30/11/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private static int TAB_COUNT = 2;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TabSub sub = new TabSub();
                return sub;
            case 1:
                TabEvents event = new TabEvents();
                return event;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Subscriptions";

            case 1:
                return "Events";
        }
        return super.getPageTitle(position);
    }
}
