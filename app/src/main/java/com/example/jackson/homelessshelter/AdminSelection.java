package com.example.jackson.homelessshelter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminSelection extends AppCompatActivity {

    DatabaseReference database;
    DatabaseReference users;
    ListView mListView;
    ArrayList<String> usersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mListView = (ListView) findViewById(R.id.list);

        // Instantiate database references
        database = FirebaseDatabase.getInstance().getReference();
        users = database.child("user");
        usersList = new ArrayList<String>();
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("username").getValue(String.class);
                    usersList.add(name);
                }
                ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, new TextView(), usersList);
                mListView.setAdapter(adapt);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//
//                    String name = ds.child("name").getValue(String.class);
//                    usersList.add(name);
//
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.list_content, usersList);
//
//                mListView.setAdapter(adapter);

    }

//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        users.addListenerForSingleValueEvent(eventListener);
}
