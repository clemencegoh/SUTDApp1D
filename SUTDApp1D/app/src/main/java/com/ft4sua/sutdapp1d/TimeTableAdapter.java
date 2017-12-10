package com.ft4sua.sutdapp1d;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ft4sua.sutdapp1d.DatabasePackage.Event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Lim Ken Jyi on 5/12/2017.
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.EventHolder> implements TimeSlotDialogFragment.TimeSlotDialogListener {

    private final List<Event> mTimeTableList;
    private final LayoutInflater mInflater;


    class EventHolder extends RecyclerView.ViewHolder{

        public final TextView locationView;
        public final TextView timeView;
        //public final ImageView typeView;
        public final LinearLayout layoutBgView;
        public final View tmrEventView;
        public final TextView eventNameView;
        final TimeTableAdapter mAdapter;


        /**
         * Creates a new custom view holder to hold the view to display in the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views for the RecyclerView.
         */
        public EventHolder(View itemView, TimeTableAdapter adapter) {
            super(itemView);
            locationView = (TextView) itemView.findViewById(R.id.location);
            timeView = (TextView) itemView.findViewById(R.id.time);
            //typeView = (ImageView) itemView.findViewById(R.id.eventtype);
            layoutBgView = (LinearLayout) itemView.findViewById(R.id.recyclerbg);
            tmrEventView = (View) itemView.findViewById(R.id.nextdaytype);
            eventNameView = (TextView) itemView.findViewById(R.id.eventname);
            this.mAdapter = adapter;
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }


    public TimeTableAdapter(Context context, List<Event> eventList) {
        mInflater = LayoutInflater.from(context);
        this.mTimeTableList = eventList;
        List<Integer> numOfEvents = new ArrayList<Integer>();
        for(int ii=6; ii<24; ii++){
            for(Event jj: eventList){
                if(!(jj.getStart()+":00").equals(ii + ":00") && !numOfEvents.contains(ii)){
                    numOfEvents.add(ii);
                    Log.i("Kenjyi", ""+ii);
                }
            }
        }
        Log.i("Kenjyi", ""+numOfEvents.size());
        for(Integer ii: numOfEvents){
            eventList.add(new Event("","",ii+":00",(ii+1)+":00","",""));
        }
    }

    /**
     * Inflates an item view and returns a new view holder that contains it.
     * Called when the RecyclerView needs a new view holder to represent an item.
     *
     * @param parent The view group that holds the item views.
     * @param viewType Used to distinguish views, if more than one type of item view is used.
     * @return a view holder.
     */
    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.eventlist_item, parent, false);
        return new EventHolder(mItemView, this);
    }

    public final void showTimeSlotDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new TimeSlotDialogFragment();
        dialog.show(TimeTableFragment.getInstance().getFragmentManager(), "TimeSlotDialogFragment");
    }

    /**
     * Sets the contents of an item at a given position in the RecyclerView.
     * Called by RecyclerView to display the data at a specificed position.
     *
     * @param holder The view holder for that position in the RecyclerView.
     * @param position The position of the item in the RecycerView.
     */
    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        // Retrieve the data for that position.
        Event mCurrent = mTimeTableList.get(position);
        if(position!=mTimeTableList.size()-1) {
            Event mNext = mTimeTableList.get(position + 1);
            String tomorrowEvent = mNext.getAdmin();
            if(tomorrowEvent.equalsIgnoreCase("0")){
                //holder.vehicleView.setImageResource(R.drawable.walking);
                holder.tmrEventView.setBackgroundColor(Color.GREEN);
            }
            else if(tomorrowEvent.equalsIgnoreCase("1")){
                //holder.vehicleView.setImageResource(R.drawable.bus);
                holder.tmrEventView.setBackgroundColor(Color.RED);
            }
            else if(tomorrowEvent.equalsIgnoreCase("2")){
                //holder.vehicleView.setImageResource(R.drawable.cab);
                holder.tmrEventView.setBackgroundColor(Color.parseColor("purple"));
            }
        }
        String eventName = mCurrent.getName();
        String date = mCurrent.getDate();
        String location = mCurrent.getVenue();
        String timeStart = mCurrent.getStart();
        String timeEnd = mCurrent.getEnd();
        String timePeriod = "  " + timeStart + "\n- " + timeEnd;

        // Add the data to the view holder.
        holder.eventNameView.setText(eventName);
        holder.locationView.setText(location);
        Log.i("Kenjyi", date);

        holder.timeView.setText(timePeriod);
        String eventType = mCurrent.getAdmin();
        if(eventType.equalsIgnoreCase("0")){
            //holder.vehicleView.setImageResource(R.drawable.walking);
            holder.layoutBgView.setBackgroundColor(Color.GREEN);
        }
        else if(eventType.equalsIgnoreCase("1")){
            //holder.vehicleView.setImageResource(R.drawable.bus);
            holder.layoutBgView.setBackgroundColor(Color.RED);
        }
        else if(eventType.equalsIgnoreCase("2")){
            //holder.vehicleView.setImageResource(R.drawable.cab);
            holder.layoutBgView.setBackgroundColor(Color.parseColor("purple"));
        }

        holder.layoutBgView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTimeSlotDialog();
            }
        });
    }

    /**
     * Returns the size of the container that holds the data.
     *
     * @return Size of the list of data.
     */
    @Override
    public int getItemCount() {
        return mTimeTableList.size();
    }
}