package com.ft4sua.sutdapp1d.DatabasePackage;

import android.app.Activity;
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
import android.util.Log;
import android.widget.Toast;

import com.ft4sua.sutdapp1d.R;
import com.ft4sua.sutdapp1d.Globals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private SharedPreferences SP;
    private SharedPreferences.Editor SPE;

    //TODO: Handle add/edit/delete flags (synchronization)
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
    }

    public static synchronized EventsHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new EventsHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," +
                COLUMN_EventType + " TEXT," +
                COLUMN_Event + " TEXT," +
                COLUMN_Details + " TEXT," +
                COLUMN_StartDate + " TEXT," +
                COLUMN_EndDate + " TEXT," +
                COLUMN_EventTag + " TEXT," +
                COLUMN_EventDate + " TEXT" +
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
    //TODO: Depending on event type, send changes to server
    public void addEvent(final Event event, final Context con, Activity act){
        db = getWritableDatabase();
        db.beginTransaction();
        final ProgressDialog pd = new ProgressDialog(con);
        pd.setTitle("Please Wait");
        pd.setMessage("Adding Event");
        pd.show();

        HashMap<String,String> data=event.get();

        Boolean status=true;
        try {
            ContentValues values = new ContentValues();
            for (int m = 0; ALL_COLUMNS_USER_ENTER.length > m; m++) {
                if (data.get(ALL_COLUMNS_USER_ENTER[m]) != null) {
                    values.put(ALL_COLUMNS_USER_ENTER[m], data.get(ALL_COLUMNS_USER_ENTER[m]));
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

    public void addEvent(final Bundle data, final Context con, Activity act) { // Add event into database /true = success /false = error

        // Create and/or open the database for writing
        db = getWritableDatabase();
        db.beginTransaction();
        final ProgressDialog pd = new ProgressDialog(con);
        pd.setTitle("Please Wait");
        pd.setMessage("Adding Event");
        pd.show();

        Boolean status=true;
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
            status=false;
        } finally {
            db.endTransaction();
            pd.dismiss();
            if (status) Toast.makeText(con, "Event successfully added", Toast.LENGTH_SHORT).show();
            else Toast.makeText(con, "Failed to add event", Toast.LENGTH_SHORT).show();
        }
    }


    //-------------------------EDIT FUNCTIONS-----------------------------
    //TODO: Depending on event type, send changes to server
    public void editEvent(final Bundle data, final Context con, Activity act) { //update event details /true = success /false = error

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
    //TODO: Depending on event type, send changes to server
    public void deleteEvent(final Integer clickPosition, Activity act, final Context con) { //delete Event // clickPosition = pass in click position of listview or null to delete with currentGWOID

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
                if (clickPosition == null) {
                    ID[0] = Long.toString(Globals.currentEventID);
                } else {
//                    if (clickPosition + 1 > EventList.size()) {
//                        Log.v("EH(GroundWaterObDelete)", "Error Delete - exceeded groundwaterIDList range");
//                        Toast.makeText(con, "Failed to delete Ground Water Observation", Toast.LENGTH_SHORT).show();
//                        EHL.onFinishDelete(false);
//                    } else {
//                        gwoID[0] = Long.toString(EventList.get(clickPosition));
//                    }
                }
            }

            @Override
            protected Boolean doInBackground(Bundle... bundles) {
                if (ID != null) {
                    ContentValues values = new ContentValues();
                    //TODO: Check EventType; Update status for server sync and User permissions to delete (if admin)
                    //values.put(COLUMN_Status, 0);

                    rEvent[0] = db.update(TABLE_NAME, values, COLUMN_ID + "='" +
                            ID[0] + "'", null);
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

    //-------------------------GET FUNCTIONS-----------------------------
    //TODO: Change to db cursor base
    public Bundle getProject(int id) { //return project details of the current event ID /null - error
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
    public List<Bundle> getEventList() { //Returns project list or null if database is empty

        db = getReadableDatabase();
        List<Bundle> eventList = new ArrayList<Bundle>();
        Cursor eventC;
        
        eventC = db.query(TABLE_NAME, null, null, null, null, null, null);
                
        if (eventC != null) {
            for (eventC.moveToFirst(); !eventC.isAfterLast(); eventC.moveToNext()) {
                Bundle temp2 = new Bundle();
                temp2.putInt(COLUMN_ID, eventC.getInt(eventC.getColumnIndex(COLUMN_ID)));   //reference to position in database
                temp2.putInt(COLUMN_EventType, eventC.getInt(eventC.getColumnIndex(COLUMN_EventType)));
                temp2.putString(COLUMN_Event, eventC.getString(eventC.getColumnIndex(COLUMN_Event)));
                temp2.putString(COLUMN_StartDate, eventC.getString(eventC.getColumnIndex(COLUMN_StartDate)));
                temp2.putString(COLUMN_EndDate, eventC.getString(eventC.getColumnIndex(COLUMN_EndDate)));
                eventList.add(temp2);
            }
            eventC.close();
        }
        return eventList;
    }
}
