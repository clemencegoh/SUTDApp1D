package com.example.tingyu.scheduler;

import org.junit.Test;

import java.util.TreeSet;
import java.util.Set;

import static org.junit.Assert.*;

public class EventTest {
    @Test
    public void sortEvents() {
        Set<Event> test = new TreeSet<Event>();
        Event e1 = new Event("Sing Song", "Mon, 30 Oct 2017", "16:00",
                "18:00", "MPH", "1002169", "Choir");
        Event e2 = new Event("Ping Pong", "Wed, 01 Nov 2017", "16:00",
                "18:00", "MPH", "1002169", "Choir");
        Event e3 = new Event("Shake Hand", "Mon, 30 Oct 2017", "20:00",
                "22:00", "Dance Studio 1", "1002169", "Dance");
        Event e4 = new Event("Shake Leg", "Wed, 01 Nov 2017", "20:00",
                "22:00", "Dance Studio 4", "1002169", "Dance");
        test.add(e1);
        test.add(e2);
        test.add(e3);
        test.add(e4);

        System.out.println("Your Events: ");
        for (Event i : test) {
            System.out.println(i.toString());
        }
    }
}
