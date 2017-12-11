package com.ft4sua.sutdapp1d.DatabasePackage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ft4sua.sutdapp1d.MainActivity;
import com.ft4sua.sutdapp1d.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Choco〜bourbon on 25-Oct-17.
 */

public class DatabaseTester {

    List<Event> l;

    public static void test(Context con){
        EventsHelper EH= EventsHelper.getInstance(con);

        Event testet=new Event("Ping Pong", "Wed, 01 Nov 2017", "16:00",
                "18:00", "MPH", "1");
        testet.setUid(con.getString(R.string.firebase_flag));
        EH.addEvent(testet,con);

        //---adding---
        List<Event> test=new ArrayList<>();
        test.add(new Event("Sing Song", "Mon, 30 Oct 2017", "16:00",
                "18:00", "MPH", "1"));
        test.add(new Event("Ping Pong", "Wed, 01 Nov 2017", "16:00",
                "18:00", "MPH", "1"));
        test.add(new Event("Shake Hand", "Mon, 30 Oct 2017", "20:00",
                "22:00", "Dance Studio 1", "1"));
        test.add(new Event("Shake Leg", "Wed, 01 Nov 2017", "20:00",
                "22:00", "Dance Studio 4", "1"));
//        EH.addLocalEvents(test,con);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse("SUN, 29 Oct 2017"));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DATE, 1);  // number of days to add
        String dt = sdf.format(cal.getTime());  // dt is now the new date

        //---getList---
        List<Event> l= EH.getDayEventList(dt);
        Log.i("Event List", String.valueOf(l));

        //Call this to delete table
        //EH.clearDataBase();
    }
}
