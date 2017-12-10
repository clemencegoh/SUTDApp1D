package com.ft4sua.sutdapp1d.EventPackage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ft4sua.sutdapp1d.DatabasePackage.Event;
import com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper;
import com.ft4sua.sutdapp1d.R;

import java.util.Calendar;

/**
 * Created by Choco〜bourbon on 10-Dec-17.
 */

public class EditEventActivity extends AppCompatActivity {


    //private F
    // local
    //private Set<Event> myEvents;
    private EventsHelper EH;
    //private List<String> mySubscriptions; //Use Shared preferences
//    private String id;

    // user inputs and buttons
    private Event newEvent;
    private EditText nameInput;
    private TextView dateInput;
    private TextView startTimeInput;
    private TextView endTimeInput;
    private EditText venueInput;
    private EditText idInput;
    private Spinner eventType;
    private FloatingActionButton doneButton;
    private FloatingActionButton editButton;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog timePickerDialog;
    private TimePickerDialog.OnTimeSetListener startTimeSetListener;
    private TimePickerDialog.OnTimeSetListener endTimeSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EH=EventsHelper.getInstance(this);
//        mySubscriptions = new LinkedList<String>();
//        myEvents = new TreeSet<Event>();
        //id = "1002169";

        // assign inputs
        nameInput = (EditText) findViewById(R.id.eventNameInput);
        dateInput = (TextView) findViewById(R.id.dateInput);
        startTimeInput = (TextView) findViewById(R.id.startTimeInput);
        endTimeInput = (TextView) findViewById(R.id.endTimeInput);
        venueInput = (EditText) findViewById(R.id.venueInput);
        idInput = (EditText) findViewById(R.id.idInput);
        eventType = (Spinner) findViewById(R.id.event_type_dropdown);

        // Pick out Date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month ++;
                String date = day + "/" + month + "/" + year;
                dateInput.setText(date);
            }
        };
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(
                        EditEventActivity.this,
                        mDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });


        // Pick out Time
        startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {
                String time = hr + ":" + min;
                startTimeInput.setText(time);
            }
        };
        endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {
                String time = hr + ":" + min;
                endTimeInput.setText(time);
            }
        };
        startTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hr = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(
                        EditEventActivity.this,
                        startTimeSetListener, hr, min, true);
                timePickerDialog.show();
            }
        });
        endTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int hr = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);
                timePickerDialog = new TimePickerDialog(
                        EditEventActivity.this,
                        endTimeSetListener, hr, min, true);
                timePickerDialog.show();
            }
        });

        editButton = (FloatingActionButton) findViewById(R.id.fab_edit);
        editButton.setVisibility(View.GONE);
        doneButton = (FloatingActionButton) findViewById(R.id.fab_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                String date = dateInput.getText().toString();
                String start = startTimeInput.getText().toString();
                String end = endTimeInput.getText().toString();
                String venue = venueInput.getText().toString();
                String id = idInput.getText().toString();
                String tag = eventType.getSelectedItem().toString();

                newEvent = new Event(name, date, start, end, venue, id, tag);
                //createEvent(newEvent);
                EH.addEvent(newEvent);
                finish();
            }
        });

        // test firebase upload
        //List<Event> test = new LinkedList<Event>();
//        test.add(new Event("Sing Song", "Mon, 30 Oct 2017", "16:00",
//                "18:00", "MPH", "1002169", "Choir"));
//        test.add(new Event("Ping Pong", "Wed, 01 Nov 2017", "16:00",
//                "18:00", "MPH", "1002169", "Choir"));
//        test.add(new Event("Shake Hand", "Mon, 30 Oct 2017", "20:00",
//                "22:00", "Dance Studio 1", "1002169", "Dance"));
//        test.add(new Event("Shake Leg", "Wed, 01 Nov 2017", "20:00",
//                "22:00", "Dance Studio 4", "1002169", "Dance"));

//        for (Event i : test) {
//            DatabaseReference newEvent = allEvents.push();
//            i.setUid(newEvent.getKey());
//            newEvent.setValue(i);
//            EH.addEvent(i,this,this);
//        }
    }
    public void setDateTimeField() {

    }

    public void showToast(String text) {
        Toast.makeText(EditEventActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * ADMINISTRATOR METHODS
     * TODO: Move to local database helper
     */
    // create a new event
    public void createEvent(Event e) {
//        DatabaseReference newEvent = allEvents.push();  // unique id assigned to node
//        e.setUid(newEvent.getKey());                    // assign uid to event instance
//        newEvent.setValue(e);                           // set node value to event instance
        EH.addEvent(e);                     // add event instance to local database
    }

    // edit existing event
    public void editEvent(Event e, String s) {
//        allEvents.child(s).setValue(e);                 // update firebase
        EH.editEvent(e);
//        for (Event i : myEvents) {                      // update local database
//            if (s.equals(i.getUid())) {
//                myEvents.remove(i);
//                myEvents.add(e);
//            }
//        }
    }

    // remove event
    public void deleteEvent(Event e) {
//        allEvents.child(e.getUid()).removeValue();

//        myEvents.remove(e);
    }


}

