package com.example.tingyu.scheduler;

import org.junit.Test;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class EventTest {
    @Test
    public void sortEvents() {
        List<Event> test = new LinkedList<Event>();
        test.add(new Event("Sing Song", "Mon, 30 Oct 2017", "16:00",
                "18:00", "MPH", "1002169", "Choir"));
        test.add(new Event("Ping Pong", "Wed, 01 Nov 2017", "16:00",
                "18:00", "MPH", "1002169", "Choir"));
        test.add(new Event("Shake Hand", "Mon, 30 Oct 2017", "20:00",
                "22:00", "Dance Studio 1", "1002169", "Dance"));
        test.add(new Event("Shake Leg", "Wed, 01 Nov 2017", "20:00",
                "22:00", "Dance Studio 4", "1002169", "Dance"));

        Collections.sort(test);
        System.out.print("Your Events: {");
        for (int i = 0; i < test.size() - 1; i++) {
            System.out.print(test.get(i).getName() + ", ");
        }
        System.out.print(test.get(test.size() - 1).getName() + "}");
    }
}
