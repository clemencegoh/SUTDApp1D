package com.ft4sua.sutdapp1d.DatabasePackage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.ft4sua.sutdapp1d.MainActivity;
import com.ft4sua.sutdapp1d.R;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FirebaseHelper {

    private Context context;

    FirebaseDatabase database;
    DatabaseReference allEvents;
    ChildEventListener myListener;

    public static FirebaseHelper sInstance;

    public static synchronized FirebaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new FirebaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public FirebaseHelper() {}

    public FirebaseHelper(Context context) {
        this.context = context;

        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);            // offline support, i.e. check and update changes upon reconnection
        allEvents = database.getReference("events");
    }

    public void setListener() {
        // LISTENER
        myListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Set<String> mySubscriptions = PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getStringSet(context.getString(R.string.subscriptions_key),
                                new HashSet<>(Arrays.asList("")));
                Event e = dataSnapshot.getValue(Event.class);
                if (mySubscriptions.contains(e.getTag())) {     // user is subscribed
                    EventsHelper.getInstance(context).addLocalEvent(e);
                    sendNotification(e, "added");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Event e = dataSnapshot.getValue(Event.class);
                EventsHelper.getInstance(context).updateFromFirebase(s, e);
                sendNotification(e, "edited");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                EventsHelper.getInstance(context)
                        .removedFromFirebase(dataSnapshot.getKey());
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

        Intent mIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(mIntent);

        PendingIntent nPendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(nPendingIntent);

        NotificationManager nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nManager.notify(0, nBuilder.build());
    }
}
