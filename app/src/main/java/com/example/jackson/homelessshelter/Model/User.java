package com.example.jackson.homelessshelter.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This model class creates a User object and has methods allowing for database updates
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

    /**
     * Default constructor allowing for the creation of a user
     */

    public User() {
    }

    /**
     * Method allowing for contact with the database for alterations in a user
     */

    public void callDatabases() {
        currentUser = FirebaseDatabase.getInstance()
                .getReference().child("user").child(FirebaseAuth.getInstance().getUid());
    }

    /**
     * Gets whether a user is an admin
     * @return boolean if user is admin
     */

    public boolean getAdmin() {
        return admin;
    }

    /**
     * Sets the admin instance for a user
     * @param admin boolean to set admin to
     */

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * Sets the admin instance for a user and changes the listing in firebase
     * @param admin boolean to set admin to
     */

    public void setAdminFirebase(boolean admin) {
        this.admin = admin;
        currentUser.child("admin").setValue(admin);
    }

    /**
     * Gets the email of a user
     * @return String the email of the user
     */

    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of a user
     * @param email String to set the email of a user to
     */

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the mail of a user locally and in the database
     * @param email String to set the email of the user to
     */

    public void setEmailFirebase(String email) {
        this.email = email;
        currentUser.child("email").setValue(email);
    }

    /**
     * Gets the first name of a user
     * @return String representation of first name of user
     */

    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the user
     * @param firstName String to set first name to
     */

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the first name of the user locally and in firebase
     * @param firstName String to set first name to
     */

    public void setFirstNameFirebase(String firstName) {
        this.firstName = firstName;
        currentUser.child("firstName").setValue(firstName);
    }

    /**
     * Gets the last name of a user
     * @return String representation of last name of user
     */

    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user locally
     * @param lastName String to set the last name
     */

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the last name for the user locally and in the database
     * @param lastName String to set the last name to
     */

    public void setLastNameFirebase(String lastName) {
        this.lastName = lastName;
        currentUser.child("lastName").setValue(lastName);
    }

    /**
     * Get the number of occupied beds of a user
     * @return int the count of currently occupied beds
     */

    public int getOccupiedBeds() {
        return occupiedBeds;
    }

    /**
     * SEts the currently occupied beds of a user locally
     * @param occupiedBeds int number of beds to set to
     */

    public void setOccupiedBeds(int occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
    }

    /**
     * SEts the currently occupied beds of a user locally and in the database
     * @param occupiedBeds int number of beds to set to
     */

    public void setOccupiedBedsFirebase(int occupiedBeds) {
        this.occupiedBeds = occupiedBeds;
        currentUser.child("occupiedBeds").setValue(occupiedBeds);
    }

    /**
     * Gets the current shelter
     * @return CharSequence representation of current shelter of user
     */

    public CharSequence getCurrentShelter() {
        return currentShelter;
    }

    /**
     * Sets the current shelter for the user
     * @param currentShelter String to set current shelter to
     */

    public void setCurrentShelter(String currentShelter) {
        this.currentShelter = currentShelter;
    }

    /**
     * Sets the current shelter for user locally and in database
     * @param currentShelter String to set current shelter to
     */

    public void setCurrentShelterFirebase(String currentShelter) {
        this.currentShelter = currentShelter;
        currentUser.child("currentShelter").setValue(currentShelter);
    }

    /**
     * Gets the username of the user
     * @return String username of user
     */

    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user
     * @param username String to set username to
     */

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the username of the user locally and in database
     * @param username String to set username to
     */

    public void setUsernameFirebase(String username) {
        this.username = username;
        currentUser.child("username").setValue(username);
    }
    @Override
    public String toString() {
        return Integer.toString(occupiedBeds) + username + "legend";
    }
}
