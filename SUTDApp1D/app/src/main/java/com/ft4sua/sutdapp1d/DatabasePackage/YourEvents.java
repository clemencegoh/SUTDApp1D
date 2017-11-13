//package com.ft4sua.sutdapp1d.DatabasePackage;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//
//import com.ft4sua.sutdapp1d.R;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DatabaseError;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.TreeSet;
//import java.util.Set;
//
//public class YourEvents extends AppCompatActivity {
//
//    // FIREBASE
//    private FirebaseDatabase database;
//    private DatabaseReference allEvents;
//    private ChildEventListener myListener;
//
//    // LOCAL
//    protected Set<Event> myEvents;
//    protected List<String> mySubscriptions;
//    protected String id;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_your_events);
//
//        database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(true);            // offline support, i.e. check and update changes upon reconnection
//        allEvents = database.getReference("events");
//        mySubscriptions = new LinkedList<String>();
//        myEvents = new TreeSet<Event>();                 // sort by natural order (comparable)
//        id = "1002169";
//
//        // LISTENER
//        myListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Event e = dataSnapshot.getValue(Event.class);
//                if (mySubscriptions.contains(e.getTag())) {     // user is subscribed
//                    myEvents.add(e);                            // add event to local database
//                    // send notification
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Event e = dataSnapshot.getValue(Event.class);
//                for (Event i : myEvents) {
//                    String uid = i.getUid();
//                    if (s.equals(uid)) {        // event is in local database
//                        myEvents.add(e);        // update local database
//                        // send notification
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                String s = dataSnapshot.getKey();
//                for (Event i : myEvents) {
//                    String uid = i.getUid();
//                    if (s.equals(uid)) {             // event is in local database
//                        myEvents.remove(i);          // remove from local database
//                    }
//                }
//            }
//
//            @Override
//            // NOT REQUIRED
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
//
//            @Override
//            // NOT REQUIRED
//            public void onCancelled(DatabaseError databaseError) {}
//        };
//        allEvents.addChildEventListener(myListener);
//    }
//
//    public void addOnPress(Event e) {
//        myEvents.add(e);
//    }
//
//    public void removeOnPress(String s) {
//        myEvents.remove(s);
//    }
//
//    public void addSubscription(String tag) {
//        mySubscriptions.add(tag);
//    }
//
//    public void cancelSubscription(String tag) {
//        mySubscriptions.remove(tag);
//    }
//}
