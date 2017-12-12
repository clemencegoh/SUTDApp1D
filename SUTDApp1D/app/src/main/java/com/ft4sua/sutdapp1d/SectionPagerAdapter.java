package com.ft4sua.sutdapp1d;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lim Ken Jyi on 5/12/2017.
 */

public class SectionPagerAdapter extends FragmentStatePagerAdapter {
    final int NUM_ITEMS = 10;
    Date today = new Date();
    public SectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    public void setItem(Date date){
        today = date;
        getItem(4);
    }

    @Override
    public Fragment getItem(int position) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
        String dt;
        switch (position) {
            case 0:
                cal.add(Calendar.DATE, -4);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 1:
                cal.add(Calendar.DATE, -3);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 2:
                cal.add(Calendar.DATE, -2);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 3:
                cal.add(Calendar.DATE, -1);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 4:
            default:
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 5:
                cal.add(Calendar.DATE, 1);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 6:
                cal.add(Calendar.DATE, 2);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 7:
                cal.add(Calendar.DATE, 3);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 8:
                cal.add(Calendar.DATE, 4);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
            case 9:
                cal.add(Calendar.DATE, 5);
                dt = sdf.format(cal.getTime());  // dt is now the new date
                return TimeTableFragment.newInstance(dt);
        }

        //return ArrayListFragment.newInstance(position);
    }

    //public static class ArrayListFragment extends ListFragment {
//        int mNum;
//
//        /**
//         *  * Create a new instance of CountingFragment, providing "num"
//         *  * as an argument.
//         *  
//         */
//        static ArrayListFragment newInstance(int num) {
//            ArrayListFragment f = new ArrayListFragment();
//
//            // Supply num input as an argument.
//            Bundle args = new Bundle();
//            args.putInt("num", num);
//            f.setArguments(args);
//
//            return f;
//        }
//
//        /**
//         *  * When creating, retrieve this instance's number from its arguments.
//         *  
//         */
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
//        }
//
//        /**
//         *  * The Fragment's UI is just a simple text view showing its
//         *  * instance number.
//         *  
//         */
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View v = inflater.inflate(R.layout.fragment_pager_list, container, false);
//
////            View tv = v.findViewById(R.id.text);
////            ((TextView) tv).setText("Fragment #" + mNum);
//            return v;
//        }
//
////        @Override
////        public void onActivityCreated(Bundle savedInstanceState) {
////            super.onActivityCreated(savedInstanceState);
////            setListAdapter(new ArrayAdapter<String>(getActivity(),
////                    android.R.layout.simple_list_item_1, Cheeses.sCheeseStrings));
////        }
//
//        @Override
//        public void onListItemClick(ListView l, View v, int position, long id) {
//            Log.i("FragmentList", "Item clicked: " + id);
//        }
//    }

}
