package com.example.jackson.homelessshelter.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This model class creates a Shelter object and has methods allowing for database updates
 */
public class Shelter implements Parcelable {

    private String name;
    private int capacity;
    private String restrictions;
    private double longitude;
    private double latitude;
    private String address;
    private String special;
    private String phone;
    private DatabaseReference currentShelter;
    private String roomType;

    /**
     * This allows for the database to build a shelter object upon request
     */

    public Shelter() {

    }

    /**
     * This sets the current Shelter database reference for updates
     * @param currentShelter is the database reference of the shelter
     */

    public void setCurrentShelter(DatabaseReference currentShelter) {
        this.currentShelter = currentShelter;
    }

    /**
     * Gets the name of the shelter
     * @return the name of the shelter
     */

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the shelter
     * @param name String input to set the name to
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the capacity of the shelter
     * @return int the capacity of the shelter
     */

    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of the shelter
     * @param capacity int input to set the capacity to
     */

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Sets the capacity of the Shelter in the object and the database
     * @param capacity int input to set the capacity to
     */

    public void setCapacityFirebase(int capacity) {
        this.capacity = capacity;
        currentShelter.child("capacity").setValue(capacity);
    }

    /**
     * Gets the restrictions of the shelter
     * @return String the restrictions of the shelter
     */

    public String getRestrictions() {
        return restrictions;
    }

    /**
     * Sets the restrictions of the Shelter
     * @param restrictions String in put to set restrictions to
     */

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    /**
     * Gets the longitude of the shelter
     * @return double the longitude of the shelter
     */

    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the shelter
     * @param longitude double input to set longitude to
     */

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the latitude of the shelter
     * @return double the latitude of the shelter
     */

    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the shelter
     * @param latitude double input to set the latitude to
     */

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the shelter
     * @return CharSequence the address of the shelter
     */

    public CharSequence getAddress() {
        return address;
    }

    /**
     * Sets the address of the shelter
     * @param address String input to set the address to
     */

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the Special statements of the shelter
     * @return String the special listing of the shelter
     */

    public String getSpecial() {
        return special;
    }

    /**
     * Sets the special description of the shelter
     * @param special String input to set the special attribute to
     */

    public void setSpecial(String special) {
        this.special = special;
    }

    /**
     * Gets the phone number of the shelter
     * @return CharSequence representation of phone number
     */

    public CharSequence getPhone() {
        return phone;
    }

    /**
     * Sets the phone number of the shelter
     * @param phone String input to set the phone number to
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets the room type of the shelter
     * @return String representation of room type
     */

    public String getRoomType() {
        return roomType;
    }

    /**
     * Sets the room type of a shelter
     * @param roomType String input to set room type to
     */
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    /**
     * Method to allow passing of shelters between fragments
     * @param in the parcel that is being passed in to build the shelter object
     */

    public Shelter(Parcel in){
        String[] data = new String[8];

        in.readStringArray(data);

        this.name = data[0];
        this.capacity = Integer.parseInt(data[1]);
        this.restrictions = data[2];
        this.longitude = Double.parseDouble(data[3]);
        this.latitude = Double.parseDouble(data[4]);
        this.address = data[5];
        this.special = data[6];
        this.phone = data[7];
        this.currentShelter = FirebaseDatabase.getInstance().getReferenceFromUrl(data[8]);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.name, Integer.toString(this.capacity),
                this.restrictions, Double.toString(this.longitude),
                Double.toString(this.latitude), this.address,
                this.special, this.phone, this.currentShelter.toString()
        });
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Shelter createFromParcel(Parcel in) {
            return new Shelter(in);
        }

        @Override
        public Shelter[] newArray(int size) {
            return new Shelter[size];
        }
    };

}
