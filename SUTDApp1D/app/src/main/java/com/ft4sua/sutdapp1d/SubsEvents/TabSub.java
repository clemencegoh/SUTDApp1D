package com.ft4sua.sutdapp1d.SubsEvents;

/**
 * Created by swonlek on 30/11/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ft4sua.sutdapp1d.R;

/**
     * Created by Belal on 2/3/2016.
     */

//Our class extending fragment
    public class TabSub extends Fragment {

        //Overriden method onCreateView
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            return inflater.inflate(R.layout.tab_subs, container, false);
        }
    }
