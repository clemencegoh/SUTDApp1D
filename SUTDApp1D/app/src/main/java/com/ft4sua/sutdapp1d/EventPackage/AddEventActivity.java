package com.ft4sua.sutdapp1d.EventPackage;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ft4sua.sutdapp1d.DatabasePackage.Event;
import com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper;
import com.ft4sua.sutdapp1d.R;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import java.util.LinkedList;
//import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    private EventsHelper EH;

    // user inputs and buttons
    private Event newEvent;
    private EditText nameInput;
    private TextView dateInput;
    private TextView startTimeInput;
    private TextView endTimeInput;
    private EditText venueInput;
    private EditText idInput;
    private EditText eventType;
    private FloatingActionButton doneButton;
    private FloatingActionButton editButton;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog timePickerDialog;
    private TimePickerDialog.OnTimeSetListener startTimeSetListener;
    private TimePickerDialog.OnTimeSetListener endTimeSetListener;
    private CheckBox pushCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EH=EventsHelper.getInstance(this);

        // assign inputs
        nameInput = (EditText) findViewById(R.id.eventNameInput);
        dateInput = (TextView) findViewById(R.id.dateInput);
        startTimeInput = (TextView) findViewById(R.id.startTimeInput);
        endTimeInput = (TextView) findViewById(R.id.endTimeInput);
        venueInput = (EditText) findViewById(R.id.venueInput);
        idInput = (EditText) findViewById(R.id.idInput);
        eventType = (EditText) findViewById(R.id.event_type_dropdown);
        pushCheck = (CheckBox) findViewById(R.id.check_firebase);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int id = prefs.getInt(getString(R.string.login_key),0);
        idInput.setText(Integer.toString(id));
        idInput.setFocusable(false);
        idInput.setEnabled(false);

        // Pick out Date
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month ++;
//                String date = day + "/" + month + "/" + year;
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day);
                dateInput.setText(DateFormat.format("EEE, dd MMM yyyy", cal));
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
                        AddEventActivity.this,
                        mDateSetListener, year, month, day);
                datePickerDialog.getWindow();
                datePickerDialog.show();
            }
        });


        // Pick out Time
        startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {
                String time = String.format(Locale.ENGLISH,"%02d:%02d",hr,min);
                startTimeInput.setText(time);
            }
        };
        endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hr, int min) {
                String time = String.format(Locale.ENGLISH,"%02d:%02d",hr,min);
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
                        AddEventActivity.this,
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
                        AddEventActivity.this,
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
                String tag = eventType.getText().toString();

                newEvent = new Event(name, date, start, end, venue, id, tag);
                if (pushCheck.isChecked()) newEvent.setUid(getString(R.string.firebase_flag));
                EH.addEvent(newEvent,AddEventActivity.this);
                //EH.getAllEventsList();    For debugging
                setResult(RESULT_OK, null);
                finish();
            }
        });
//        }
    }
}
