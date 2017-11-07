package com.ft4sua.sutdapp1d;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Choco〜bourbon on 07-Nov-17.
 */

public class Launcher extends AppCompatActivity {

    SharedPreferences prefs;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        id = prefs.getInt(getString(R.string.login_key),0);
        Log.v("id= ", String.valueOf(id));
        if (id==0) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

    }
}
