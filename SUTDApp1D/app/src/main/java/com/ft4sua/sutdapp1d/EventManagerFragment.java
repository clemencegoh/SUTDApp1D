package com.ft4sua.sutdapp1d;

import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.ft4sua.sutdapp1d.EventPackage.CardChild;
import com.ft4sua.sutdapp1d.EventPackage.CardCreator;
import com.ft4sua.sutdapp1d.EventPackage.CardParent;
import com.ft4sua.sutdapp1d.EventPackage.Event;
import com.ft4sua.sutdapp1d.EventPackage.RVAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventManagerFragment extends android.support.v4.app.Fragment {


    Context context = getActivity();
    private List<Event> events;
    private RecyclerView eventRecycler;

    public EventManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(R.string.nav_event_manager);
        View rootView = inflater.inflate(R.layout.fragment_event_manager, container, false);

        eventRecycler = (RecyclerView)rootView.findViewById(R.id.rv_event_manager);
        eventRecycler.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        eventRecycler.setLayoutManager(llm);

        initialiseData();
        initialiseAdapter();
        return rootView;
    }
    private void initialiseAdapter() {
        RVAdapter adapter = new RVAdapter(getActivity(), initialiseData());
        adapter.setCustomParentAnimationViewId(R.id.expand_arrow);
        adapter.setParentClickableViewAnimationDefaultDuration();
        adapter.setParentAndIconExpandOnClick(true);
        eventRecycler.setAdapter(adapter);
    }
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        ((RVAdapter)eventRecycler.getAdapter()).onSaveInstanceState(outState);
//    }

    // details: Date, Start Time, Location
    // this is where JSON comes in? or firebase
    private List<ParentObject> initialiseData(){
        CardCreator cardCreator = CardCreator.get(getActivity());
        List<CardParent> collapsed = cardCreator.getAll();
        List<ParentObject> parentObjects = new ArrayList<>();
        for (CardParent cardParent:collapsed){
            List<Object> expandList = new ArrayList<>();
            expandList.add(new CardChild("Fifth row", "2pm","5pm","ISH level 1"));
            cardParent.setChildObjectList(expandList);
            parentObjects.add(cardParent);
        }
        return parentObjects;
//        events = new ArrayList<>();
//        events.add(new Event("Dance Prac", "6 Dec 2017, 2 pm, DS 5"));
//        events.add(new Event("Guest Lecture", "7 Dec 2017, 3 pm, LT 4"));
//        events.add(new Event("Hostel Event", "7 Dec 2017, 8 pm, BLK 55"));
//        events.add(new Event("Dance Prac", "8 Dec 2017, 2 pm, DS 5"));
//        events.add(new Event("Guest Lecture", "9 Dec 2017, 3 pm, LT 4"));
//        events.add(new Event("Hostel Event", "9 Dec 2017, 8 pm, BLK 55"));

    }


}