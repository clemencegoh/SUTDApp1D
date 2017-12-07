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
import com.ft4sua.sutdapp1d.DatabasePackage.Event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeTableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeTableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeTableFragment extends Fragment implements TimeSlotDialogFragment.TimeSlotDialogListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
//    EventsHelper eventsHelper = EventsHelper.getInstance(getActivity());
//    List<Bundle> eventList = eventsHelper.getEventList();

    //private RecyclerView mRecyclerView;
    private TimeTableAdapter timeTableAdapter;

    // TODO: Rename and change types of parameters
    private int dayTracker;
    private String events;
    static TimeTableFragment fragment;

    private OnFragmentInteractionListener mListener;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new TimeSlotDialogFragment();
        dialog.show(getFragmentManager(), "TimeSlotDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }


    // TODO: Rename and change types and number of parameters
    public static TimeTableFragment newInstance(int dayTracker) {
        fragment = new TimeTableFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_PARAM1, dayTracker);
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
            dayTracker = getArguments().getInt(ARG_PARAM1);
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
        background.setImageResource(R.drawable.schoolbackground1);

        TextView dateView= (TextView) v.findViewById(R.id.date);
        dateView.setText("30 Oct 2017");

        Log.i("Kenjyi","Reaches Timetable createView");
        List<Event> test=new ArrayList<>();
//        EventsHelper eventsHelper = EventsHelper.getInstance(getContext());
//        for(Bundle ii: eventsHelper.getInstance(getContext()).getEventList()){
//            test.add(new Event().bundleToEvent(ii));
//            Log.i("kenjyi", new Event().bundleToEvent(ii).getName());
//        }
//
//        myPortal timeTable = new myPortal();
//
//        class DownloadThread extends Thread {
//            List<Event> innerEventList = new ArrayList<>();
//            public void run(){
//                try{
//                    myPortal profile = new myPortal();
//                    Event[] events = profile.timeTable("1002208", "1hcoatBs");
//                    Log.i("Login","Login successful, events initialized");
//                    innerEventList = Arrays.asList(events);
//                }catch(Exception e){
//                    Log.d("Login","User or password may be wrong");
//                }
//            }
//        }
//        DownloadThread downloadThread = new DownloadThread();
//        Thread downloadThread = new Thread(){
//            public void run(){
//                try{
//                    myPortal profile = new myPortal();
//                    Event[] events = profile.timeTable("1002208", "1hcoatBs");
//                    Log.i("Login","Login successful, events initialized");
//                    List<Event> innerEventList = Arrays.asList(events);
//                    test.addAll(innerEventList);
//                }catch(Exception e){
//                    Log.d("Login","User or password may be wrong");
//                }
//            }
//        };
//        downloadThread.start();
//        test = downloadThread.innerEventList;
//        Log.d("running", String.valueOf(test.size()));
//        Log.d("running2", String.valueOf(downloadThread.innerEventList.size()));



        if(dayTracker == 0){
            test.add(new Event("Sing Song", "Mon, 30 Oct 2017", "16:00",
                    "18:00", "MPH", "2"));
            test.add(new Event("Ping Pong", "Wed, 01 Nov 2017", "16:00",
                    "18:00", "MPH", "2"));
        }
        else if (dayTracker == 1) {
            test.add(new Event("Shake Hand", "Mon, 30 Oct 2017", "20:00",
                    "22:00", "Dance Studio 1", "1"));
            test.add(new Event("Shake Leg", "Wed, 01 Nov 2017", "20:00",
                    "22:00", "Dance Studio 4", "1"));
        }
        else{
        }

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
