package com.ft4sua.sutdapp1d;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.ft4sua.sutdapp1d.SubsEvents.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.view.View.GONE;

public class SubsEventsActivity extends AppCompatActivity {

    public SubsEventsActivity(){
        //Required empty public constructor
    }

    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SelectedListAdapter selectedListAdapter;
    private Button addButton;
    private ImageButton deleteButton;
    private EditText tagEntry;
    private List<String> tagList = new ArrayList<String>();
    private ListView container;
    private TextView emptyTagMessage;
    private Set<String> setString = new HashSet<String>();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_subs_events);

        emptyTagMessage = (TextView) findViewById(android.R.id.empty);

        addButton = (Button) findViewById(R.id.addtag);
        tagEntry = (EditText) findViewById(R.id.tagentry);
        container = (ListView) findViewById(R.id.drawer_list);
        deleteButton = (ImageButton) findViewById(R.id.delete_button);
        addTags();

        // refer to whatever things you need in the layout
//        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        mViewPager = (ViewPager)findViewById(R.id.container);
//        mViewPager.setAdapter(mViewPagerAdapter);
//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(mViewPager);


    }

//
//    private void setViewPager(View v) {
//        mViewPager = (ViewPager) v.findViewById(R.id.container);
//        mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
//        mViewPager.setAdapter(mViewPagerAdapter);
//
//        tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(mViewPager);
//    }

    private class SelectedListAdapter extends ArrayAdapter<String> {
        public SelectedListAdapter() {
            super(SubsEventsActivity.this, R.layout.nav_drawer_item, tagList);
            if (tagList.size() != 0) {

                //findViewById(android.R.id.empty).setVisibility(View.GONE);
                emptyTagMessage.setVisibility(View.GONE);
                //addButton.setVisibility(View.VISIBLE);
            }
        }
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final String index=tagList.get(position);

            LayoutInflater inflater = (LayoutInflater) SubsEventsActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.nav_drawer_item, parent, false);

            ((TextView) rowView.findViewById(android.R.id.text1)).setText(tagList.get(position));
            rowView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setString.remove(tagList.get(position));
                    tagList.remove(position);
                    notifyDataSetChanged();
                    if (tagList.size() == 0) {
                        findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
//                        itinenaryButton.setVisibility(View.GONE);
                    }
                    SharedPreferences prefs = PreferenceManager
                            .getDefaultSharedPreferences(SubsEventsActivity.this);
                    Set<String> mySubscriptions = prefs
                            .getStringSet(SubsEventsActivity.this.getString(R.string.subscriptions_key),
                                    new HashSet<>(Arrays.asList("")));
                    prefs.edit().remove(getString(R.string.subscriptions_key));
                    prefs.edit().putStringSet(getString(R.string.subscriptions_key),setString).apply();

                    Log.i("Kenjyi", prefs.getStringSet(SubsEventsActivity.this.getString(R.string.subscriptions_key),
                            new HashSet<>(Arrays.asList(""))).toString());
                }
            });
            return rowView;
        }
    }

    public void addTags(){
        prefs = PreferenceManager
                .getDefaultSharedPreferences(SubsEventsActivity.this);
        Set<String> mySubscriptions = prefs
                .getStringSet(SubsEventsActivity.this.getString(R.string.subscriptions_key),
                        new HashSet<>(Arrays.asList("")));
        for(String ii: mySubscriptions){
            tagList.add(ii);
            setString.add(ii);
        }
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagList.add(tagEntry.getText().toString());
                selectedListAdapter.notifyDataSetChanged();
                setString.add(tagEntry.getText().toString());
                tagEntry.setText("");

                prefs.edit().putStringSet(getString(R.string.subscriptions_key),setString).apply();

                Log.i("Kenjyi", prefs.getStringSet(SubsEventsActivity.this.getString(R.string.subscriptions_key),
                        new HashSet<>(Arrays.asList(""))).toString());
            }
        });

//        for (int i=0;i<tagList.size();i++) { selectedPositions.add(i);}
        selectedListAdapter=new SelectedListAdapter();
        container.setAdapter(selectedListAdapter);
    }

//    public void removeTags(){
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tagList.get(index).setSelected(0);
//                databaseHelper.updateSelected(tagList.get(index));
//                selectedPositions.remove(position);
//                notifyDataSetChanged();
//                if (selectedPositions.size() == 0) {
//                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
//                    itinenaryButton.setVisibility(View.GONE);
//                }
//            }
//        });
//    }
}



