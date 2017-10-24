package com.ft4sua.sutdapp1d.DatabasePackage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ft4sua.sutdapp1d.MainActivity;

import java.util.List;

/**
 * Created by Choco〜bourbon on 25-Oct-17.
 */

public class DatabaseTester {
    public void test(Context con, Activity act){
        EventsHelper EH= EventsHelper.getInstance(con);

        //---adding---
        Bundle input=new Bundle();
        input.putString(EventsHelper.COLUMN_EventType, "0");        //0-User def, 1-myportal timetable, 2-unique event
        input.putString(EventsHelper.COLUMN_Event, "Test Day");
        input.putString(EventsHelper.COLUMN_Details, "thisisnoahophappyssmekfadboard");
        input.putString(EventsHelper.COLUMN_StartDate, "000");
        input.putString(EventsHelper.COLUMN_EndDate, "111");
        input.putString(EventsHelper.COLUMN_Admin, "1000040");      //set based on SharedPreferences if user is admin
        Log.i("Bundle", String.valueOf(input));
        EH.addEvent(input,con,act);

        //---getList---
        List<Bundle> l= EH.getEventList();
        Log.i("Bundle List", String.valueOf(l));

        //Call this to delete table
        //EH.clearDataBase();
    }
}
