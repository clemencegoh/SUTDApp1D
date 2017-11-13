package com.example.tingyu.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.Collections;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // firebase
    private FirebaseDatabase db;
    private DatabaseReference allEvents;
    private ChildEventListener myListener;

    // local
    private List<Event> myEvents;
    private List<String> mySubscriptions;
    private String id;

    private static final String TAG = "YourEvents";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        db.setPersistenceEnabled(true);
        allEvents = db.getReference("events");

        mySubscriptions = new ArrayList<String>();
        myEvents = new ArrayList<Event>();
        id = "1002169";

        myListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event e = dataSnapshot.getValue(Event.class);
                if (mySubscriptions.contains(e.getTag())) {
                    myEvents.add(e);
                    // send notification
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Event e = dataSnapshot.getValue(Event.class);
                for (Event i : myEvents) {
                    // event is in local database
                    if (s.equals(i.getName())) {
                        // update local database
                        int index = myEvents.indexOf(i);
                        myEvents.remove(index);
                        myEvents.add(e);
                        // send notification
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Event e = dataSnapshot.getValue(Event.class);
                // event is in local database
                if (myEvents.contains(e)) {
                    // remove from local database
                    int index = myEvents.indexOf(e);
                    myEvents.remove(index);
                }
            }

            @Override
            // not required
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            // error case
            public void onCancelled(DatabaseError databaseError) {}
        };

        setContentView(R.layout.main_activity);
    }

    /**
     * COMMON METHODS
     */
    public void addEvent(Event e) {
        myEvents.add(e);
    }

    public void removeEvent(Event e) {
        myEvents.remove(e);
    }

    public void addSubscription(String tag) {
        mySubscriptions.add(tag);
    }

    public void cancelSubscription(String tag) {
        mySubscriptions.remove(tag);
    }

    /**
     * ADMINISTRATOR METHODS
     */
    // create a new event, add to local database and upload to firebase
    public void createEvent(String name, String date, String start, String end,
                            String venue, String admin, String tag) {
        Event e = new Event(name, date, start, end, venue, admin, tag);
        allEvents.child(name).setValue(e);
        myEvents.add(e);
    }

    // edit existing event, update local database and firebase
    public void editEvent(Event e, String s) {}

    // remove event from local database and firebase
    public void deleteEvent(Event e) {
        allEvents.child(e.getName()).removeValue();
        myEvents.remove(e);
    }
}
