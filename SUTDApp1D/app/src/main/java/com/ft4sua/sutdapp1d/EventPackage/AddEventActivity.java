package com.ft4sua.sutdapp1d.EventPackage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.ft4sua.sutdapp1d.DatabasePackage.Event;
import com.ft4sua.sutdapp1d.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;

public class AddEventActivity extends AppCompatActivity {

    // firebase
    private FirebaseDatabase database;
    private DatabaseReference allEvents;
    //private F
    // local
    private Set<Event> myEvents;
    private List<String> mySubscriptions;
    private String id;

    // user inputs and buttons
    private Event newEvent;
    private EditText nameInput;
    private EditText dateInput;
    private EditText startTimeInput;
    private EditText endTimeInput;
    private EditText venueInput;
    private EditText idInput;
    private EditText tagInput;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        database = FirebaseDatabase.getInstance();
        allEvents = database.getReference("events");
        mySubscriptions = new LinkedList<String>();
        myEvents = new TreeSet<Event>();
        id = "1002169";

        // assign inputs
        nameInput = (EditText) findViewById(R.id.nameInput);
        dateInput = (EditText) findViewById(R.id.dateInput);
        startTimeInput = (EditText) findViewById(R.id.startTimeInput);
        endTimeInput = (EditText) findViewById(R.id.endTimeInput);
        venueInput = (EditText) findViewById(R.id.venueInput);
        idInput = (EditText) findViewById(R.id.idInput);
        tagInput = (EditText) findViewById(R.id.tagInput);

        createButton = (Button) findViewById(R.id.fab_edit);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                String date = dateInput.getText().toString();
                String start = startTimeInput.getText().toString();
                String end = endTimeInput.getText().toString();
                String venue = venueInput.getText().toString();
                String id = idInput.getText().toString();
                String tag = tagInput.getText().toString();

                newEvent = new Event(name, date, start, end, venue, id, tag);
                createEvent(newEvent);

                showToast("Your event has been created.");
            }
        });

        // test firebase upload
        List<Event> test = new LinkedList<Event>();
        test.add(new Event("Sing Song", "Mon, 30 Oct 2017", "16:00",
                "18:00", "MPH", "1002169", "Choir"));
        test.add(new Event("Ping Pong", "Wed, 01 Nov 2017", "16:00",
                "18:00", "MPH", "1002169", "Choir"));
        test.add(new Event("Shake Hand", "Mon, 30 Oct 2017", "20:00",
                "22:00", "Dance Studio 1", "1002169", "Dance"));
        test.add(new Event("Shake Leg", "Wed, 01 Nov 2017", "20:00",
                "22:00", "Dance Studio 4", "1002169", "Dance"));

        for (Event i : test) {
            DatabaseReference newEvent = allEvents.push();
            i.setUid(newEvent.getKey());
            newEvent.setValue(i);
            myEvents.add(i);
        }
    }

    public void showToast(String text) {
        Toast.makeText(AddEventActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * ADMINISTRATOR METHODS
     */
    // create a new event
    public void createEvent(Event e) {
        DatabaseReference newEvent = allEvents.push();  // unique id assigned to node
        e.setUid(newEvent.getKey());                    // assign uid to event instance
        newEvent.setValue(e);                           // set node value to event instance
        myEvents.add(e);                                // add event instance to local database
    }

    // edit existing event
    public void editEvent(Event e, String s) {
        allEvents.child(s).setValue(e);                 // update firebase
        for (Event i : myEvents) {                      // update local database
            if (s.equals(i.getUid())) {
                myEvents.remove(i);
                myEvents.add(e);
            }
        }
    }

    // remove event
    public void deleteEvent(Event e) {
        allEvents.child(e.getUid()).removeValue();
        myEvents.remove(e);
    }
}
