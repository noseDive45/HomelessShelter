package com.example.jackson.homelessshelter.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.jackson.homelessshelter.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pop extends Activity {

    protected List<String> list;
    private DatabaseReference database;
    private long longitude;
    private long latitude;
    private String address;
    private String special;
    private String name;
    private String restrictions;
    private String phone;
    private String passedName;
    private long capacity;
    private TextView nameView;
    private TextView addressView;
    private TextView phoneView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) Math.round(width * .9), (int) Math.round(height * .6));
        setContentView(R.layout.popwindow);
        intitialize();
        populate();
    }

    private void intitialize() {
        passedName = getIntent().getStringExtra("passedName");
        nameView = (TextView) findViewById(R.id.name);
        addressView = (TextView) findViewById(R.id.address);
        phoneView = (TextView) findViewById(R.id.phone);
        database = FirebaseDatabase.getInstance().getReference();
        list = new ArrayList<>();database = FirebaseDatabase.getInstance().getReference();
        // Populate list with shelter names
        DatabaseReference shelters = FirebaseDatabase.getInstance().getReference("shelter");
        Query query = shelters.orderByChild("name").equalTo(passedName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot needed = dataSnapshot;
                System.out.println("found");
                name = needed.child("name").getValue(String.class);
//                                                     capacity = needed.child("capacity").getValue(Long.class);
                special = needed.child("special").getValue(String.class);
                address = needed.child("address").getValue(String.class);
                restrictions = needed.child("restrictions").getValue(String.class);
                phone = needed.child("phone").getValue(String.class);
//                                                     latitude = needed.child("latitude").getValue(Long.class);
//                                                     longitude = needed.child("longitude").getValue(Long.class);
                System.out.println("recorded");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        database.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("trying`");
                if (Objects.equals(dataSnapshot.child("name"), passedName)) {
                    System.out.println("legend");
                    name = dataSnapshot.child("name").getValue(String.class);
                    capacity = dataSnapshot.child("capacity").getValue(Long.class);
                    special = dataSnapshot.child("special").getValue(String.class);
                    address = dataSnapshot.child("address").getValue(String.class);
                    restrictions = dataSnapshot.child("restrictions").getValue(String.class);
                    phone = dataSnapshot.child("phone").getValue(String.class);
                    latitude = dataSnapshot.child("latitude").getValue(Long.class);
                    longitude = dataSnapshot.child("longitude").getValue(Long.class);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void populate() {
        nameView.setText(name);
        addressView.setText(address);
        phoneView.setText(phone);
        System.out.println("kitty");
    }
}
