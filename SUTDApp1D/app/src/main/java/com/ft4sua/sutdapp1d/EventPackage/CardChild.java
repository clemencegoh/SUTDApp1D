package com.ft4sua.sutdapp1d.EventPackage;

/**
 * Created by swonlek on 6/12/2017.
 */

public class CardChild {

    private String event_type, event_start, event_end, event_venue;

    public CardChild(String event_type, String event_start, String event_end, String event_venue) {
        this.event_type = event_type;
        this.event_start = event_start;
        this.event_end = event_end;
        this.event_venue = event_venue;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_start() {
        return event_start;
    }

    public void setEvent_start(String event_start) {
        this.event_start = event_start;
    }

    public String getEvent_end() {
        return event_end;
    }

    public void setEvent_end(String event_end) {
        this.event_end = event_end;
    }

    public String getEvent_venue() {
        return event_venue;
    }

    public void setEvent_venue(String event_venue) {
        this.event_venue = event_venue;
    }
}
