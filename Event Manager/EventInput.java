package com.example.tingyu.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EventInput extends AppCompatActivity {

    // firebase
    private FirebaseDatabase database;
    private DatabaseReference allEvents;

    // local
    private List<Event> myEvents;
    private List<String> mySubscriptions;
    private String id;

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
        setContentView(R.layout.activity_event_input);

        database = FirebaseDatabase.getInstance();
        allEvents = database.getReference("events");
        mySubscriptions = new ArrayList<String>();
        myEvents = new ArrayList<Event>();
        id = "1002169";

        nameInput = (EditText) findViewById(R.id.nameInput);
        dateInput = (EditText) findViewById(R.id.dateInput);
        startTimeInput = (EditText) findViewById(R.id.startTimeInput);
        endTimeInput = (EditText) findViewById(R.id.endTimeInput);
        venueInput = (EditText) findViewById(R.id.venueInput);
        idInput = (EditText) findViewById(R.id.idInput);
        tagInput = (EditText) findViewById(R.id.tagInput);

        createButton = (Button) findViewById(R.id.createButton);
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

        Collections.sort(myEvents);
        System.out.print("Your Events: ");
        for (int i = 0; i < test.size(); i++) {
            System.out.print(test.get(i).getName() + " ");
        }
    }

    public void showToast(String text) {
        Toast.makeText(EventInput.this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * ADMINISTRATOR METHODS
     */
    // create a new event, add to local database and upload to firebase
    public void createEvent(Event e) {
        DatabaseReference newEvent = allEvents.push();
        e.setUid(newEvent.getKey());
        newEvent.setValue(e);
        myEvents.add(e);
    }

    // edit existing event, update local database and firebase
    public void editEvent(Event e, String s) {
        allEvents.child(s).setValue(e);
        for (Event i : myEvents) {
            if (s.equals(i.getUid())) {
                int index = myEvents.indexOf(i);
                myEvents.remove(index);
                myEvents.add(e);
            }
        }
    }

    // remove event from local database and firebase
    public void deleteEvent(Event e) {
        allEvents.child(e.getUid()).removeValue();
        myEvents.remove(e);
    }
}
