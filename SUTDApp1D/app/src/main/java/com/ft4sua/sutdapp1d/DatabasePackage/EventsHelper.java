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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Choco〜bourbon on 22-Oct-17.
 */

public class EventsHelper extends SQLiteOpenHelper {

    //-----Variables------
    public static final int DB_VERSION = 1;
    //Paths
    private static final String DB_PATH = "data/data/sp.bolog/databases/";
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
    public final static String COLUMN_Event = "Event";
    public final static String COLUMN_StartDate = "StartDate";
    public final static String COLUMN_EndDate = "EndDate";
    public final static String COLUMN_Admin = "Admin";
    public final static String COLUMN_LastSync = "LastSync";
    public final static String[] ALL_COLUMNS_USER_ENTER = new String[]{COLUMN_EventType,
            COLUMN_Event, COLUMN_StartDate, COLUMN_EndDate};
    public final static String[] ALL_COLUMNS = new String[]{COLUMN_ID, COLUMN_EventType,
            COLUMN_Event, COLUMN_StartDate, COLUMN_EndDate, COLUMN_Admin, COLUMN_LastSync};
    //<---End of DB fields-->


    private EventsHelper(Context context) {
        super(context, L_DB_NAME, null, DB_VERSION);
        this.context = context;
        SP = context.getSharedPreferences(context.getString(R.string.sharedPreference_key), Context.MODE_PRIVATE);
        SPE = SP.edit();
    }

    public static synchronized EventsHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new EventsHelper(context.getApplicationContext());
        }
        open();
        return sInstance;
    }

    public interface EventsHelperListener {
        public void onFinishAddEdit(Boolean SuccessOrFailed);

        public void onDBupdate();

        public void onFinishDelete(Boolean SuccessOrFailed);

    }

    public boolean create() throws IOException { // create database /true = no error /false = error
        if (checkDataBase()) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying Events database");
            }
        }
        return checkDataBase();
    }

    private boolean checkDataBase() { // check if database already exist /true = exist /false = don't exist
        SQLiteDatabase checkDB = null;
        try {
            String path = DB_PATH + L_DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException { // copy included database into local app
        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + L_DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
        SPE.putInt(context.getString(R.string.eventsVer_key), DB_VERSION).commit();
    }

    public void clearDataBase() {
        db.delete(TABLE_NAME, null, null);
    }

    public static synchronized boolean open() {
        try {
            String myPath = DB_PATH + L_DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            return true;
        } catch (SQLException sqle) {
            db = null;
            return false;
        }
    }

    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //-------------------------ADD FUNCTIONS-----------------------------
    public void addEvent(final Bundle data, final Context con, Activity act) { // Add event into database /true = success /false = error

        final ProgressDialog pd = new ProgressDialog(con);
        final EventsHelperListener EHL;
        pd.setTitle("Please Wait");
        pd.setMessage("Adding Event");

        try {
            EHL = (EventsHelperListener) act;
        } catch (Exception e) {
            throw new ClassCastException(act.toString()
                    + " must implement EventsHelperListener");
        }
        new AsyncTask<Bundle, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd.show();
            }

            @Override
            protected Boolean doInBackground(Bundle... bundles) {
                ContentValues values = new ContentValues();
                for (int m = 0; ALL_COLUMNS_USER_ENTER.length > m; m++) {
                    if (data.getString(ALL_COLUMNS_USER_ENTER[m]) != null) {
                        values.put(ALL_COLUMNS_USER_ENTER[m], data.getString(ALL_COLUMNS_USER_ENTER[m]));
                    }
                }

                //TODO: Change to current date here
                //values.put(COLUMN_Flag, 0);
                //values.put(COLUMN_Status, 2);
                Long pID = null;
                try {
                    pID = db.insert(TABLE_NAME, null, values);
                    //GlobalVar.currentProjectID = pID;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                if (pID != null) {
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
                    Toast.makeText(con, "Event successfully added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(con, "Failed to add event", Toast.LENGTH_SHORT).show();
                }
                EHL.onFinishAddEdit(aBoolean);
            }
        }.execute();
    }


    //-------------------------EDIT FUNCTIONS-----------------------------
    public void editEvent(final Bundle data, final Context con, Activity act) { //update event details /true = success /false = error

        final ProgressDialog pd = new ProgressDialog(con);
        final EventsHelperListener EHL;
        pd.setTitle("Please Wait");
        pd.setMessage("Editing Event");

        try {
            EHL = (EventsHelperListener) act;
        } catch (Exception e) {
            throw new ClassCastException(act.toString()
                    + " must implement EventsHelperListener");
        }

        new AsyncTask<Bundle, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd.show();
            }

            @Override
            protected Boolean doInBackground(Bundle... bundles) {
                ContentValues values = new ContentValues();
                for (int m = 0; ALL_COLUMNS_USER_ENTER.length > m; m++) {
                    if (data.getString(ALL_COLUMNS_USER_ENTER[m]) != null) {
                        values.put(ALL_COLUMNS_USER_ENTER[m], data.getString(ALL_COLUMNS_USER_ENTER[m]));
                    }
                }

                Cursor cProject = db.query(TABLE_NAME, null, COLUMN_ID + "='" + Long.toString(Globals.currentEventID) +
                        "'", null, null, null, null);

                if (cProject.getCount() != 1) {
                    return false;
                } else {
                    cProject.moveToFirst();
                }

                //TODO: Change Date here/Handle synch
//                if (cProject.getInt(cProject.getColumnIndex(COLUMN_Status)) == 1) {
//                    values.put(COLUMN_Status, 3);
//                }

                int lProject;
                try {
                    lProject = db.update(TABLE_NAME, values, COLUMN_ID + "='" + Long.toString(Globals.currentEventID) +
                            "'", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                if (lProject == 1) {
                    return true;
                } else {
                    //TODO = handle error in redundancy
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                pd.dismiss();
                if (aBoolean) {
                    Toast.makeText(con, "Event successfully edited", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(con, "Failed to edit event", Toast.LENGTH_SHORT).show();
                }
                EHL.onFinishAddEdit(aBoolean);
            }
        }.execute();
    }

    //-------------------------DELETE FUNCTIONS-----------------------------
    public void deleteEvent(final Integer clickPosition, Activity act, final Context con) { //delete Event // clickPosition = pass in click position of listview or null to delete with currentGWOID

        final ProgressDialog pd = new ProgressDialog(con);
        final EventsHelperListener EHL;
        pd.setTitle("Please Wait");
        pd.setMessage("Deleting Event");

        try {
            EHL = (EventsHelperListener) act;
        } catch (Exception e) {
            throw new ClassCastException(act.toString()
                    + " must implement EventsHelperListener");
        }

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
                EHL.onFinishDelete(aBoolean);
                Log.v("EH(EventDelete)", "Number of Deletes:\nEvent = " + String.valueOf(rEvent[0]));
            }
        }.execute();
    }

    //-------------------------GET FUNCTIONS-----------------------------
    public Bundle getProject() { //return project details of the current event ID /null - error
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
}
