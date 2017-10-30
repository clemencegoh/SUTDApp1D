package com.example.tingyu.scheduler;

public class Event implements Comparable<Event> {

    private String name;
    private String date;        // DAY, DD MMM YYYY (e.g. Sun, 29 Oct 2017)
    private String start;
    private String end;
    private String venue;
    private String admin;
    private String tag;

    private static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY",
            "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
    private static final String[] DAYS = {"SUN", "MON", "TUES", "WED", "THU",
            "FRI", "SAT"};

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
    // to sort events according to date and time and differentiate
    // repeated events which have the same name
    public int compareTo(Event other) {
        return 0;
    }
}
