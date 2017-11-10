package com.ft4sua.sutdapp1d;

import android.app.Application;

import com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper;

/**
 * Created by Choco〜bourbon on 24-Oct-17.
 */

public class Globals extends Application {
    public static String currentEventID;
    private EventsHelper EH;

    @Override
    public void onCreate() {
        super.onCreate();
//        EH = EventsHelper.getInstance(getApplicationContext());
//        EH.open();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
//        EH.close();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
//        EH.close();
    }
}
