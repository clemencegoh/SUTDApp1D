package com.ft4sua.sutdapp1d.SubsEvents;

/**
 * Created by swonlek on 30/11/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ft4sua.sutdapp1d.EventPackage.Event;
import com.ft4sua.sutdapp1d.R;

import java.util.ArrayList;

/**
     * Created by Belal on 2/3/2016.
     */

//Our class extending fragment
public class TabEvents extends Fragment {

    private ArrayList<Tabbed> t;
    private RecyclerView recyclerView;

    public TabEvents() {
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_frag_subs_events, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        recyclerView = (RecyclerView)rootView.findViewById(R.id.rv_events_tab);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        initialiseData();
        TabRVAdapter adapter = new TabRVAdapter(t);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private void initialiseData(){
        t = new ArrayList<>();
        t.add(new Tabbed("hello","6 dec, 7pm, ds 5"));
        t.add(new Tabbed("jhjhjh","6 jan, 7pm, ds 5"));
        t.add(new Tabbed("iyfyvhk","6 dec, 9pm, ds 5"));
        t.add(new Tabbed("mnjb","6 dec, 7pm, ds 100"));
        t.add(new Tabbed("gk","6 dec, 7pm, library 5"));
        t.add(new Tabbed("xjyr","6 dec, 7am, ds 5"));
        t.add(new Tabbed("hhvk","60 dec, 7pm, ds 5"));
        t.add(new Tabbed("atej","6 december 7pm, ds 5"));
        t.add(new Tabbed("atej","6 december 7pm, ds 5"));
        t.add(new Tabbed("atej","6 december 7pm, ds 5"));
        t.add(new Tabbed("atej","6 december 7pm, ds 5"));
    }
}
