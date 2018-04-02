package com.example.jackson.homelessshelter.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public Shelter(String name, int capacity, String restrictions, double longitude,
                   double latitude, String address, String special, String phone) {
        this.name = name;
        this.capacity = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.special = special;
        this.phone = phone;
    }

    public Shelter() {

    }

    public void setCurrentShelter(DatabaseReference currentShelter) {
        this.currentShelter = currentShelter;
    }
//
//    public DatabaseReference getCurrentShelter() {
//        return currentShelter;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCapacityFirebase(int capacity) {
        this.capacity = capacity;
        currentShelter.child("capacity").setValue(capacity);
    }

    public String getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(String restrictions) {
        this.restrictions = restrictions;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public CharSequence getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public CharSequence getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString() {
        return name;
    }

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

    public int describeContents(){
        return 0;
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Shelter createFromParcel(Parcel in) {
            return new Shelter(in);
        }

        public Shelter[] newArray(int size) {
            return new Shelter[size];
        }
    };

}
