package com.ft4sua.sutdapp1d;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ft4sua.sutdapp1d.DatabasePackage.DatabaseTester;
import com.ft4sua.sutdapp1d.DatabasePackage.Event;
import com.ft4sua.sutdapp1d.EventPackage.RVAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventManagerFragment extends android.support.v4.app.Fragment {
    ArrayList<Event> events;
    private RecyclerView eventRecycler;

    public EventManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.nav_event_manager);
        View rootView = inflater.inflate(R.layout.fragment_event_manager, container, false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());

        eventRecycler = (RecyclerView)rootView.findViewById(R.id.rv_event_manager);
        eventRecycler.setLayoutManager(llm);
        eventRecycler.setHasFixedSize(true);
        //TODO: add FAB


        initialiseData();
        initialiseAdapter();
        return rootView;
    }


    private void initialiseAdapter() {
        RVAdapter adapter = new RVAdapter(events);
        eventRecycler.setAdapter(adapter);
    }


    // details: Date, Start Time, Location
    // this is where JSON comes in? or firebase
    private void initialiseData(){
        events = new ArrayList<>();
        events.add(new Event("Dance Prac", "6 Dec 2017","fifth row","2 pm","5pm","DS 5"));
        events.add(new Event("Guest Lecture", "7 Dec 2017","root","2 pm","3pm","LT 5"));
        events.add(new Event("Hostel Event", "7 Dec 2017", "house guardian","7 pm","8pm","BLK 55"));
        events.add(new Event("Dance Prac", "8 Dec 2017", "fifth row","7 pm","9pm","DS 5"));
        events.add(new Event("Guest Lecture", "9 Dec 2017", "root","12 pm","2pm","LT 3"));
        events.add(new Event("Hostel Event", "9 Dec 2017", "house guardian","8 pm","9pm","BLK 55"));

    }


}