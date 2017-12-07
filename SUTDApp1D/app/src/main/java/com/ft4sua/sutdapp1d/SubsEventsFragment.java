package com.ft4sua.sutdapp1d;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ft4sua.sutdapp1d.SubsEvents.ViewPagerAdapter;

public class SubsEventsFragment extends Fragment {

    public SubsEventsFragment(){
        //Required empty public constructor
    }

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private Toolbar mToolbar;
    private ViewPagerAdapter mViewPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.nav_subs_events);
        View rootView = inflater.inflate(R.layout.fragment_subs_events, container, false);
        // refer to whatever things you need in the layout
        setViewPager(rootView);
        return rootView;
    }


    private void setViewPager(View v) {
        mViewPager = (ViewPager) v.findViewById(R.id.container);
        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);

        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }


}

