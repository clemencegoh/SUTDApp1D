package com.example.tingyu.scheduler;

import java.util.Arrays;

public class Event implements Comparable<Event> {

    private String uid;
    private String name;
    private String date;        // DAY, DD MMM YYYY (e.g. Sun, 29 Oct 2017)
    private String start;
    private String end;
    private String venue;
    private String admin;
    private String tag;

    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public Event(String name, String date, String start, String end,
                 String venue, String admin, String tag) {
        this.name = name;
        this.date = date;
        this.start = start;
        this.end = end;
        this.venue = venue;
        this.admin = admin;
        this.tag = tag;
    }

    // getters and setters
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

    public String getAdmin() { return admin; }
    public void setAdmin(String id) { this.admin = id; }

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
