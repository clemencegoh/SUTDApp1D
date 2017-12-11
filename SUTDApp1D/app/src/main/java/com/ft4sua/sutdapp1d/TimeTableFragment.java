package com.ft4sua.sutdapp1d;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ft4sua.sutdapp1d.Connections.myPortal;
import com.ft4sua.sutdapp1d.DatabasePackage.DatabaseTester;
import com.ft4sua.sutdapp1d.DatabasePackage.Event;
import com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeTableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
//    EventsHelper eventsHelper = EventsHelper.getInstance(getActivity());
//    List<Bundle> eventList = eventsHelper.getEventList();

    //private RecyclerView mRecyclerView;
    private TimeTableAdapter timeTableAdapter;

    // TODO: Rename and change types of parameters
    private String dateTracker;
    private String events;
    static TimeTableFragment fragment;

    private OnFragmentInteractionListener mListener;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    public void showCalendarNavigatorDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CalendarNavigatorDialogFragment();

        dialog.show(getFragmentManager(), "CalendarNavigatorDialogFragment");
    }



    // TODO: Rename and change types and number of parameters
    public static TimeTableFragment newInstance(String dateTracker) {
        fragment = new TimeTableFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, dateTracker);
        fragment.setArguments(args);
        return fragment;
    }
    public static TimeTableFragment getInstance() {
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dateTracker = getArguments().getString(ARG_PARAM1);
//            for (Bundle ii: eventList) {
//                events = getArguments().get(eventList.);
//            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_time_table, container, false);

        ImageView background = (ImageView) v.findViewById(R.id.background);

        background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarNavigatorDialog();
            }
        });

        TextView dateView = (TextView) v.findViewById(R.id.date);

        Log.i("Kenjyi","Reaches Timetable createView");
        EventsHelper EH= EventsHelper.getInstance(getActivity());
        List<Event> test=EH.getDayEventList(dateTracker);

        if(dateTracker.substring(0,3).equalsIgnoreCase("Mon")){
            background.setBackgroundResource(R.drawable.schoolbackground1);
        }
        else if(dateTracker.substring(0,3).equalsIgnoreCase("Tue")){
            background.setBackgroundResource(R.drawable.schoolbackground2);
        }
        else if(dateTracker.substring(0,3).equalsIgnoreCase("Wed")){
            background.setBackgroundResource(R.drawable.schoolbackground3);
        }
        else if(dateTracker.substring(0,3).equalsIgnoreCase("Thu")){
            background.setBackgroundResource(R.drawable.schoolbackground4);
        }
        else if(dateTracker.substring(0,3).equalsIgnoreCase("Fri")){
            background.setBackgroundResource(R.drawable.schoolbackground5);
        }
        else{
            background.setBackgroundResource(R.drawable.restbackground1);
        }

        dateView.setText(dateTracker);

        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        timeTableAdapter = new TimeTableAdapter(getActivity(), test);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(timeTableAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
