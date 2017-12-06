package com.ft4sua.sutdapp1d.EventPackage;

import com.ft4sua.sutdapp1d.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by swonlek on 5/12/2017.
 * this is a demo data for events
 */

public class Event {
    String event_name, event_date, event_type, event_start, event_end, event_venue;

    public Event(String event_name, String event_date, String event_type, String event_start, String event_end, String event_venue) {
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_type = event_type;
        this.event_start = event_start;
        this.event_end = event_end;
        this.event_venue = event_venue;
    }
}
