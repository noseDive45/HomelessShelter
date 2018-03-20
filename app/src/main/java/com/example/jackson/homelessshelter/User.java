package com.example.jackson.homelessshelter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Jackson on 3/20/2018.
 */

public class User {

    private boolean admin;
    private String email;
    private String firstName;
    private String lastName;
    private int occupiedBeds;
    private String currentShelter;
    private String username;
    private DatabaseReference currentUser;
    private FirebaseAuth fAuth;


    public User(boolean admin, String email, String firstName, String lastName,
                int occupiedBeds, String currentShelter, String username) {
        this.admin = admin;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupiedBeds = occupiedBeds;
        this.currentShelter = currentShelter;
        this.username = username;
        callDatabases();
    }

    public User() {
        fAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseDatabase.getInstance()
                .getReference().child("user").child(fAuth.getUid());
        currentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                admin = dataSnapshot.child("admin").getValue(Boolean.class);
                email = dataSnapshot.child("email").getValue(String.class);
                firstName = dataSnapshot.child("firstName").getValue(String.class);
                lastName = dataSnapshot.child("lastName").getValue(String.class);
                occupiedBeds = dataSnapshot.child("occupiedBeds").getValue(Integer.class);
                username = dataSnapshot.child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void callDatabases() {
        currentUser = FirebaseDatabase.getInstance()
                .getReference().child("user").child(fAuth.getUid());
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
        currentUser.child("admin").setValue(admin);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        currentUser.child("email").setValue(email);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        currentUser.child("firstName").setValue(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        currentUser.child("lastName").setValue(lastName);
    }

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    public void setOccupiedBeds(int occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
        currentUser.child("occupiedBeds").setValue(occupiedBeds);
    }

    public String getCurrentShelter() {
        return currentShelter;
    }

    public void setCurrentShelter(String currentShelter) {
        this.currentShelter = currentShelter;
        currentUser.child("currentShelter").setValue(currentShelter);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        currentUser.child("username").setValue(username);
    }

}
