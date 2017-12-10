package com.ft4sua.sutdapp1d.DatabasePackage;

import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_Details;
import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_EndTime;
import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_Event;
import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_EventDate;
import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_EventTag;
import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_EventType;
//import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_ID;
import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_FID;
import static com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper.COLUMN_StartTime;

public class Event implements Comparable<Event> {

    private int id;
    private String uid;         // generated by firebase; if 0, does not push
    private String name;
    private String date;        // DAY, DD MMM YYYY (e.g. SUN, 29 Oct 2017)
    private String start;       // HH:MM
    private String end;
    private String venue;
    private String type;        // if user generated: student ID    else: 0-timetable 1-root events
    private String tag;         // subscription name

    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public Event(){
        id=-1;
        uid = "";
        name = "";
        date = "";
        start = "";
        end = "";
        venue = "";
        type = "";
        tag = "";
    }

    public Event(String name, String date, String start, String end,
                 String venue, String type){
        id=-1;
        uid = "";
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
        this.venue = venue;
        this.type = type;
        tag = "";
    }

    public Event(String name, String date, String start, String end,
                 String venue, String type, String tag) {
        id=-1;
        uid="";
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
        this.venue = venue;
        this.type = type;
        this.tag = tag;
    }

    // getters and setters
    public Bundle getBundle(){
        Bundle data=new Bundle(8);
        data.putString(COLUMN_FID,uid);
        data.putString(COLUMN_Event,name);
        data.putString(COLUMN_EventDate,date);
        data.putString(COLUMN_StartTime,start);
        data.putString(COLUMN_EndTime,end);
        data.putString(COLUMN_Details,venue);
        data.putString(COLUMN_EventType,type);
        data.putString(COLUMN_EventTag,tag);
        Log.v("!!!!Event bundle: ",data.toString());
        return data;
    }

    public Event bundleToEvent(Bundle bundle){
        Event data = new Event((String)bundle.get(COLUMN_Event),(String)bundle.get(COLUMN_EventDate),
                (String)bundle.get(COLUMN_StartTime), (String)bundle.get(COLUMN_EndTime),
                (String)bundle.get(COLUMN_Details), (String)bundle.get(COLUMN_EventType));
        return data;
    }

    public int getId() {return id; }
    public void setId(int id) {this.id = id; }

    public String getUid() { return uid; }
    public void setUid(String uid) {this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStart() { return start; }
    public void setStart(String time) { this.start = time; }

    public String getEnd() { return end; }
    public void setEnd(String time) { this.end = time; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) {this.venue = venue; }

    public String getAdmin() { return type; }
    public void setAdmin(String id) { this.type = id; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String toString() {
        return this.date + ", from " + this.start + " to " + this.end
                + ": " + this.name + " @ " + this.venue;
    }

    @Override
    // to sort events according to date and time
    public int compareTo(Event o) {
        int thisMonth = Arrays.asList(MONTHS).indexOf(this.date.substring(8, 11));
        int otherMonth = Arrays.asList(MONTHS).indexOf(o.date.substring(8, 11));
        int thisDay = Integer.parseInt(this.date.substring(5, 7));
        int otherDay = Integer.parseInt(o.date.substring(5, 7));
        int thisTime = Integer.parseInt(this.start.replace(":", ""));
        int otherTime = Integer.parseInt(o.start.replace(":", ""));

        // first compare month
        if (thisMonth < otherMonth) {
            return -1;
        } else if (thisMonth > otherMonth) {
            return 1;
        } else {
            // if in the same month, compare day
            if (thisDay < otherDay) {
                return -1;
            } else if (thisDay > otherDay) {
                return 1;
            } else {
                // if on the same day, compare time
                if (thisTime < otherTime) {
                    return -1;
                } else if (thisTime > otherTime) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }
}
