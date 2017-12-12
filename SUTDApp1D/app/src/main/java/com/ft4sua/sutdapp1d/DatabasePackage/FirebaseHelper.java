package com.ft4sua.sutdapp1d.DatabasePackage;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.ft4sua.sutdapp1d.EventManagerFragment;
import com.ft4sua.sutdapp1d.MainActivity;
import com.ft4sua.sutdapp1d.R;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class FirebaseHelper {

    private Context context;

    FirebaseDatabase database;
    DatabaseReference allEvents;
    ChildEventListener myListener;

    public static FirebaseHelper sInstance;

    public FirebaseHelper() {}

    public FirebaseHelper(Context context) {
        this.context = context;

        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);            // offline support, i.e. check and update changes upon reconnection
        allEvents = database.getReference("events");
        allEvents.keepSynced(true);
//        this.setListener();
    }

    public void setListener() {
        // LISTENER
        myListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("Firebase Helper", "An event was added to Firebase, checking subscriptions...");
                Set<String> mySubscriptions = PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getStringSet(context.getString(R.string.subscriptions_key),
                                new HashSet<>(Arrays.asList("")));
                Event e = dataSnapshot.getValue(Event.class);
                if (mySubscriptions.contains(e.getTag())&&!EventsHelper.getInstance(context).CheckIsFidIn(e)) {     // user is subscribed
                    EventsHelper.getInstance(context).addLocalEvent(e,context);
                    sendNotification(e, "added");
                    scheduleNotification(e);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.i("Firebase Helper", "An event was changed in Firebase, checking local events...");
                Event e = dataSnapshot.getValue(Event.class);
//                EventsHelper.getInstance(context).updateFromFirebase(s, e, context);
                EventsHelper.getInstance(context).removedFromFirebase(s,context);
                sendNotification(e, "edited");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("Firebase Helper", "An event was removed from Firebase, checking local events...");
                Event e = dataSnapshot.getValue(Event.class);
                EventsHelper.getInstance(context)
                        .removedFromFirebase(e.getUid(),context);
                sendNotification(e, "removed");
                unscheduleNotification(e);
            }

            @Override
            // NOT REQUIRED
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            // NOT REQUIRED
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        allEvents.addChildEventListener(myListener);
    }

    public void sendNotification(Event e, String type) {
        String title = "";
        switch (type) {
            case "added":
                title = "New Subscription Event";
                break;
            case "edited":
                title = "Subscription Event Changed";
                break;
            case "removed":
                title = "Subscription Event Removed";
                break;
        }

        NotificationCompat.InboxStyle nStyle = new NotificationCompat.InboxStyle();
        String[] info = {e.getName(), e.getDate() + ", " + e.getStart() + " to " + e.getEnd(), e.getVenue(), e.getTag()};
        nStyle.setBigContentTitle(title);

        for (int i = 0; i < info.length; i++) {
            nStyle.addLine(info[i]);
        }

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_logo_b)
                .setContentTitle(title)
                .setContentText(e.getName())
                .setStyle(nStyle);
        int notificationId = e.getId();

        Intent mIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(mIntent);

        PendingIntent nPendingIntent = stackBuilder.getPendingIntent(notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(nPendingIntent);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(notificationId, nBuilder.build());
    }

    public void scheduleNotification(Event e) {
        NotificationCompat.InboxStyle nStyle = new NotificationCompat.InboxStyle();
        String[] info = {e.getName(), e.getDate() + ", " + e.getStart() + " to " + e.getEnd(), e.getVenue(), e.getTag()};
        nStyle.setBigContentTitle("Upcoming Event");

        for (int i = 0; i < info.length; i++) {
            nStyle.addLine(info[i]);
        }

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.app_logo_b)
                .setContentTitle("Upcoming Event")
                .setContentText(e.getName())
                .setStyle(nStyle);
        int notificationId = e.getId();
        Notification reminderNotification = nBuilder.build();

        Intent mIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(mIntent);

        PendingIntent nPendingIntent = stackBuilder.getPendingIntent(notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(nPendingIntent);

        Intent nIntent = new Intent(context, ReminderReceiver.class);
        nIntent.putExtra("notification_id", notificationId);
        nIntent.putExtra("notification", reminderNotification);
        PendingIntent bIntent = PendingIntent.getBroadcast(context, notificationId,
                nIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        String[] date = e.getDate().split(" ");
        String[] time = e.getStart().split(":");
        int year = Integer.parseInt(date[3]);
        int month = Arrays.asList(Event.MONTHS)
                .indexOf(date[2]);
        int dayOfMonth = Integer.parseInt(date[1]);
        int hourOfDay = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        Log.i("Firebase Helper", "Alarm Time: " + calendar.getTime());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 900000, bIntent);
    }

    public void unscheduleNotification(Event e) {
        int notificationId = e.getId();
        Intent uIntent = new Intent(context, ReminderReceiver.class);
        PendingIntent bIntent = PendingIntent.getBroadcast(context, notificationId, uIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(bIntent);
    }

    /*public int createNotificationId(String uid) {
        return Integer.parseInt(new String(uid.toCharArray()));
    }*/

    public DatabaseReference getFirebaseRef() { return this.allEvents; }
    public static synchronized FirebaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FirebaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }
}