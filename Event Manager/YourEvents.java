package com.example.tingyu.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class YourEvents extends AppCompatActivity {

    // firebase
    private FirebaseDatabase database;
    private DatabaseReference allEvents;
    private ChildEventListener myListener;

    // local
    protected List<Event> myEvents;
    protected List<String> mySubscriptions;
    protected String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_events);

        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);           // offline support, i.e. check and update changes upon reconnection
        allEvents = database.getReference("events");
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
                    if (s.equals(i.getUid())) {
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
    }

    public void addOnPress(Event e) {
        myEvents.add(e);
    }

    public void removeOnPress(Event e) {
        myEvents.remove(e);
    }

    public void addSubscription(String tag) {
        mySubscriptions.add(tag);
    }

    public void cancelSubscription(String tag) {
        mySubscriptions.remove(tag);
    }
}
