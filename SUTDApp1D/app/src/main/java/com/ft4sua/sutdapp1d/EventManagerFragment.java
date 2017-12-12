package com.ft4sua.sutdapp1d;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ft4sua.sutdapp1d.DatabasePackage.DatabaseTester;
import com.ft4sua.sutdapp1d.DatabasePackage.Event;
import com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper;
import com.ft4sua.sutdapp1d.EventPackage.AddEventActivity;
import com.ft4sua.sutdapp1d.EventPackage.RVAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventManagerFragment extends AppCompatActivity {
    List<Event> events;
    private RecyclerView eventRecycler;

    public EventManagerFragment() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_manager);


        LinearLayoutManager llm = new LinearLayoutManager(this);

        eventRecycler = (RecyclerView)findViewById(R.id.rv_event_manager);
        eventRecycler.setLayoutManager(llm);
        eventRecycler.setHasFixedSize(true);

        //Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEventManager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(EventManagerFragment.this, AddEventActivity.class),1);
            }
        });

        initialiseData();
        initialiseAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, EventManagerFragment.class);
            startActivity(refresh);
            setResult(RESULT_OK, null);
            finish();
        }
    }


    private void initialiseAdapter() {
        RVAdapter adapter = new RVAdapter(events);
        eventRecycler.setAdapter(adapter);
    }


    // details: Date, Start Time, Location
    // this is where JSON comes in? or firebase
    private void initialiseData(){
        events = EventsHelper.getInstance(EventManagerFragment.this).getTaggedEventList();

    }


}