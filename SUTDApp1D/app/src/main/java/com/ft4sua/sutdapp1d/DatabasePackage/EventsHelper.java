package com.ft4sua.sutdapp1d.DatabasePackage;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.ft4sua.sutdapp1d.Globals;
import com.ft4sua.sutdapp1d.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Choco〜bourbon on 22-Oct-17.
 */

public class EventsHelper extends SQLiteOpenHelper {

    //-----Variables------
    public static final int DB_VERSION = 1;
    //Paths
    private static final String DB_PATH = "/data/data/ft4sua.sutdapp1d/databases/";
    private static final String DB_NAME = "saved_events";                        //packaged db
    private static final String L_DB_NAME = "events_scheduler";                 //locally saved db

    private final Context context;
    protected static SQLiteDatabase db;
    private static EventsHelper sInstance;

    private DatabaseReference fref;

    private SharedPreferences prefs;

    //<---Start of DB fields-->
    private static final String TABLE_NAME = "events";
    public final static String COLUMN_ID = "ID";
    public final static String COLUMN_EventType = "EventType";
    public final static String COLUMN_Event = "Event";  //if ==100xxxx admin else unique event/timetable
    public final static String COLUMN_Details = "Details";
    public final static String COLUMN_StartDate = "StartDate";
    public final static String COLUMN_EndDate = "EndDate";
    public final static String COLUMN_EventTag = "EventTag";  //can combine w/ eventType field
    public final static String COLUMN_EventDate = "EventDate";
    public final static String[] ALL_COLUMNS_USER_ENTER = new String[]{COLUMN_EventType,
            COLUMN_Event, COLUMN_Details, COLUMN_EventDate, COLUMN_StartDate, COLUMN_EndDate, COLUMN_EventTag};
    public final static String[] ALL_COLUMNS = new String[]{COLUMN_ID, COLUMN_EventType,
            COLUMN_Event, COLUMN_Details, COLUMN_StartDate, COLUMN_EndDate, COLUMN_EventTag,
            COLUMN_EventDate};
    //<---End of DB fields-->

    //Use getInstance to initialize instead of this
    public EventsHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

        //Firebase interface
        FirebaseDatabase firebase = FirebaseDatabase.getInstance();
        firebase.setPersistenceEnabled(true);            // offline support, i.e. check and update changes upon reconnection
        fref = firebase.getReference("events");

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized EventsHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new EventsHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Unique auto-increment id possibly needed
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_ID + " TEXT," +
                COLUMN_EventType + " TEXT," +
                COLUMN_Event + " TEXT," +
                COLUMN_Details + " TEXT," +
                COLUMN_StartDate + " TEXT," +
                COLUMN_EndDate + " TEXT," +
                COLUMN_EventTag + " TEXT," +
                COLUMN_EventDate + " TEXT," +
                "PRIMARY KEY("+COLUMN_ID+")"+
                ")";

        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * This method will create database in application package /databases
     * directory when first time application launched
     **/
    public void create() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException mIOException) {
                mIOException.printStackTrace();
                throw new Error("Error copying database");
            } finally {
                this.close();
            }
        }
    }

    /** This method checks whether database is exists or not **/
    private boolean checkDataBase() {
        try {
            final String mPath = DB_PATH + DB_NAME;
            final File file = new File(mPath);
            if (file.exists())
                return true;
            else
                return false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method will copy database from /assets directory to application
     * package /databases directory
     **/
    private void copyDataBase() throws IOException {
        try {

            InputStream mInputStream = context.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream mOutputStream = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = mInputStream.read(buffer)) > 0) {
                mOutputStream.write(buffer, 0, length);
            }
            mOutputStream.flush();
            mOutputStream.close();
            mInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** This method open database for operations **/
    public static synchronized boolean open() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        return db.isOpen();
    }

    /** This method close database connection and released occupied memory **/
    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }

    public void clearDataBase() {
        db.delete(TABLE_NAME, null, null);
    }



    //-------------------------ADD FUNCTIONS-----------------------------
    public void addEvent(final Event event, final Context con){
        db = getWritableDatabase();
        db.beginTransaction();
        final ProgressDialog pd = new ProgressDialog(con);
        pd.setTitle("Please Wait");
        pd.setMessage("Adding Event");
        pd.show();

        //push to firebase
        if (!event.getUid().equals("")) {
            DatabaseReference newEvent = fref.push();      // unique id assigned to node
            event.setUid(newEvent.getKey());                     // assign uid to event instance
            newEvent.setValue(event);                           // set node value to event instance
            Log.v("Event: ", event.toString());
        }

        //add to local
        Bundle data=event.getBundle();
        Boolean status=true;
        try {
            ContentValues values = new ContentValues();
            for (int m = 0; ALL_COLUMNS.length > m; m++) {
                if (data.get(ALL_COLUMNS[m]) != null) {
                    values.put(ALL_COLUMNS[m], data.getString(ALL_COLUMNS[m]));
                }
            }
            db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            status=false;
        } finally {
            db.endTransaction();
            pd.dismiss();
            if (status) Toast.makeText(con, "Event successfully added", Toast.LENGTH_SHORT).show();
            else Toast.makeText(con, "Failed to add event", Toast.LENGTH_SHORT).show();
        }
    }

    public void addLocalEvents(final List<Event> events, final Context con) { // Add event into database /true = success /false = error

        // Create and/or open the database for writing
        db = getWritableDatabase();
        db.beginTransaction();
        final ProgressDialog pd = new ProgressDialog(con);
        pd.setTitle("Please Wait");
        pd.setMessage("Adding Event(s)");
        pd.show();

        Boolean status=true;
        try {
            for (Event e: events) {
                Bundle data=e.getBundle();
                ContentValues values = new ContentValues();
                for (int m = 0; ALL_COLUMNS_USER_ENTER.length > m; m++) {
                    if (data.getString(ALL_COLUMNS_USER_ENTER[m]) != null) {
                        values.put(ALL_COLUMNS_USER_ENTER[m], data.getString(ALL_COLUMNS_USER_ENTER[m]));
                    }
                }
                db.insertOrThrow(TABLE_NAME, null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            status=false;
        } finally {
            db.endTransaction();
            pd.dismiss();
            if (status) Toast.makeText(con, "Event successfully added", Toast.LENGTH_SHORT).show();
            else Toast.makeText(con, "Failed to add event", Toast.LENGTH_SHORT).show();
        }
    }


    //-------------------------EDIT FUNCTIONS (Only for user-def events)-----------------------------
    public void editEvent(final Event event, final Context con) { //update event details /true = success /false = error

        final ProgressDialog pd = new ProgressDialog(con);
        pd.setTitle("Please Wait");
        pd.setMessage("Editing Event");
        pd.show();

        final String uid = event.getUid();
        fref.child(uid).setValue(event);                 // update firebase

        Bundle data=event.getBundle();
        Boolean status=true;
        try {
            ContentValues values = new ContentValues();
            for (String COL : ALL_COLUMNS) {
                if (data.getString(COL) != null) {
                    values.put(COL, data.getString(COL));
                }
            }
            db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(uid) });
                    //new String[] { String.valueOf(Globals.currentEventID) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            status=false;
        } finally {
            db.endTransaction();
            pd.dismiss();
            if (status) Toast.makeText(con, "Event successfully edited", Toast.LENGTH_SHORT).show();
            else Toast.makeText(con, "Failed to edit event", Toast.LENGTH_SHORT).show();
        }
    }

    public void editEvent(final Bundle data, final Context con) { //update event details /true = success /false = error

        final ProgressDialog pd = new ProgressDialog(con);
        pd.setTitle("Please Wait");
        pd.setMessage("Editing Event");
        pd.show();

        Boolean status=true;
        try {
            ContentValues values = new ContentValues();
            for (int m = 0; ALL_COLUMNS_USER_ENTER.length > m; m++) {
                if (data.getString(ALL_COLUMNS_USER_ENTER[m]) != null) {
                    values.put(ALL_COLUMNS_USER_ENTER[m], data.getString(ALL_COLUMNS_USER_ENTER[m]));
                }
            }
            db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(Globals.currentEventID) });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            status=false;
        } finally {
            db.endTransaction();
            pd.dismiss();
            if (status) Toast.makeText(con, "Event successfully edited", Toast.LENGTH_SHORT).show();
            else Toast.makeText(con, "Failed to edit event", Toast.LENGTH_SHORT).show();
        }
    }

    //-------------------------DELETE FUNCTIONS-----------------------------
    public void deleteEvent(final Event event, final Context con) { //delete Event // clickPosition = pass in click position of listview or null to delete with currentGWOID

        final ProgressDialog pd = new ProgressDialog(con);
        pd.setTitle("Please Wait");
        pd.setMessage("Deleting Event");

        final int[] rEvent = {0};
        final String[] ID = new String[1];

        new AsyncTask<Bundle, Integer, Boolean>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd.show();
                ID[0]=event.getUid();
            }

            @Override
            protected Boolean doInBackground(Bundle... bundles) {
                if (prefs.getString(con.getString(R.string.login_key), "").equals(ID[0]))
                    fref.child(event.getUid()).removeValue();
                if (ID != null) {
                    db.delete(TABLE_NAME, COLUMN_ID + "='" +
                            ID[0] + "'",null);
//                    rEvent[0] = db.update(TABLE_NAME, values, COLUMN_ID + "='" +
//                            ID[0] + "'", null);
                    return true;
                } else {
                    return false;
                }
            }
            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                pd.dismiss();
                if (aBoolean) {
                    Toast.makeText(con, "Event successfully deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(con, "Failed to delete event", Toast.LENGTH_SHORT).show();
                }
                Log.v("EH(EventDelete)", "Number of Deletes:\nEvent = " + String.valueOf(rEvent[0]));
            }
        }.execute();
    }

    //------------------------FIREBASE FUNCTIONS------------------------
    //When calling, be sure to delete all first
    public void addFromSubs(final Context con) { // Add event into database /true = success /false = error
        // Create and/or open the database for writing
        db = getWritableDatabase();
        db.beginTransaction();
        final ProgressDialog pd = new ProgressDialog(con);
        pd.setTitle("Please Wait");
        pd.setMessage("Adding Event(s)");
        pd.show();

        fref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Event singleEvent = postSnapshot.getValue(Event.class);
                    if (prefs.getStringSet(con.getString(R.string.subscriptions_key), new HashSet<>(Arrays.asList(""))).contains(singleEvent.getUid())) {
                        Boolean status = true;
                        Bundle data=singleEvent.getBundle();
                        try {
                            ContentValues values = new ContentValues();
                            for (int m = 0; ALL_COLUMNS_USER_ENTER.length > m; m++) {
                                if (data.getString(ALL_COLUMNS_USER_ENTER[m]) != null) {
                                    values.put(ALL_COLUMNS_USER_ENTER[m], data.getString(ALL_COLUMNS_USER_ENTER[m]));
                                }
                            }
                            db.insertOrThrow(TABLE_NAME, null, values);
                            db.setTransactionSuccessful();
                        } catch (Exception e) {
                            status = false;
                        } finally {
                            if (status) Log.v("added event from tag: ",singleEvent.getTag());
                            else Log.e("failed to add an event","");
                        }
                    }
                }
                db.endTransaction();
                pd.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void deleteAllFromSubs(final Context con){
        Set<String> subs = prefs.getStringSet(con.getString(R.string.subscriptions_key), new HashSet<>(Arrays.asList("")));
        for (String s:subs) {

        }
    }

    //-------------------------GET FUNCTIONS-----------------------------
    //TODO: Change to db cursor base
    public Bundle getEvent(int id) { //return project details of the current event ID /null - error
        Cursor pC = db.query(TABLE_NAME, null, COLUMN_ID + "='" + String.valueOf(Globals.currentEventID) + "'", null, null, null, null);
        Bundle data = new Bundle();
        if (pC.getCount() == 1) {
            pC.moveToFirst();
            for (int m = 0; ALL_COLUMNS_USER_ENTER.length > m; m++) {
                data.putString(ALL_COLUMNS_USER_ENTER[m], pC.getString(pC.getColumnIndex(ALL_COLUMNS_USER_ENTER[m])));
            }

            //TODO: Handle locally deleted entries
//            if (pC.getInt(pC.getColumnIndex(COLUMN_Flag)) == 1) {
//                data.putBoolean(COLUMN_Flag, true);
//            } else {
//                data.putBoolean(COLUMN_Flag, false);
//            }
            return data;
        } else {
            return null;
        }
    }

    //-------------------------GET LIST FUNCTIONS-----------------------------
    public List<Bundle> getEventList() { //Returns all events as a list or null if database is empty

        db = getReadableDatabase();
        List<Bundle> eventList = new ArrayList<Bundle>();
        Cursor eventC;

        eventC = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (eventC != null) {
            for (eventC.moveToFirst(); !eventC.isAfterLast(); eventC.moveToNext()) {
                Bundle temp2 = new Bundle();
                for (String COL:ALL_COLUMNS_USER_ENTER) {
                    temp2.putString(COL,eventC.getString(eventC.getColumnIndex(COL)));
                }
                eventList.add(temp2);
            }
            eventC.close();
        }
        return eventList;
    }
}
