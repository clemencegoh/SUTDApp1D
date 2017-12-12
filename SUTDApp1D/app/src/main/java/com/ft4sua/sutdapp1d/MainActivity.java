package com.ft4sua.sutdapp1d;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ft4sua.sutdapp1d.DatabasePackage.EventsHelper;
import com.ft4sua.sutdapp1d.DatabasePackage.FirebaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CalendarNavigatorDialogFragment.CalendarNavigatorDialogListener{
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    Date date = new Date();

    private Intent profilePageIntent;
    private Intent subsEventsIntent;
    private Intent eventManagerIntent;
    private Intent calendarIntent;
    private Intent addEventIntent;
    private SectionPagerAdapter mAdapter;
    private ViewPager mPager;

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseHelper.getInstance(this).setListener();
        setContentView(R.layout.activity_main);

        try {
            Intent intent = getIntent();
            String dateString = intent.getStringExtra("Date");

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH);
            try {
                date = sdf.parse(dateString);// all done

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        catch(Exception ex){

        }
        // set initial fragment
        //CalendarFragment calFrag = new CalendarFragment();
        //android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.fragment_container, calFrag).commit();

        mAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        mAdapter.setItem(date);

        mPager = (ViewPager)findViewById(R.id.viewpager);
        mPager.setAdapter(mAdapter);

        mPager.setCurrentItem(4);

        // Toolbar to replace ActionBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer view
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // nav bar
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // nav header
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView studentName = (TextView) headerView.findViewById(R.id.student_name);
        ImageView profilePic = (ImageView) headerView.findViewById(R.id.profile_pic);
        //TODO: if database records student names, change this to display student name. Otherwise just student ID. Link to settings?
        // or edit student name from here idk
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        studentName.setText(prefs.getString("Name","Student Name"));
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePageIntent = new Intent(MainActivity.this, ProfilePage.class);
                startActivity(profilePageIntent);
            }
        });

        /***---Database Test
         * Check out this class for sample usage---***/
        //DatabaseTester test=new DatabaseTester();
        //test.test(this);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // return Settings page for Calendar.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // note! must do the fragment thing inside the if clause
        int id = item.getItemId();
        if (id == R.id.nav_event_manager) {
            Toast.makeText(MainActivity.this, "Events Subscribed", Toast.LENGTH_SHORT).show();
//            Class fragmentClass = EventManagerFragment.class;
            Class activityClass = EventManagerFragment.class;
            navigateToActivity(activityClass);
        } else if (id == R.id.nav_subs_events) {
            Toast.makeText(MainActivity.this, "Subscribed Tags", Toast.LENGTH_SHORT).show();
            //Class fragmentClass = SubsEventsActivity.class;
            Class activityClass = SubsEventsActivity.class;
            navigateToActivity(activityClass);

        } else if (id == R.id.nav_sync_timetable) {
            Toast.makeText(MainActivity.this, "Timetable synced", Toast.LENGTH_SHORT).show();
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        } else if (id==R.id.action_logout) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().putInt(getString(R.string.login_key), 0).apply();
            EventsHelper.getInstance(MainActivity.this).clearDataBase();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } //else if (id==R.id.nav_cal){

            // set initial fragment
//            Class fragmentClass = SectionPagerAdapter.class;
//            navigateToFragment(fragmentClass);
//        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigateToActivity(Class activityClass){
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }


    private void navigateToFragment(Class fragmentClass){
        android.support.v4.app.Fragment fragment = null;
        try {
            fragment = (android.support.v4.app.Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }
}
