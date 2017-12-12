package com.ft4sua.sutdapp1d.DatabasePackage;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by tingyu on 12/12/2017.
 */

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Firebase Helper", "Receiving broadcast, sending notification...");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra("notification");
        int id = intent.getIntExtra("notification_id", 0);
        notificationManager.notify(id, notification);
    }
}
