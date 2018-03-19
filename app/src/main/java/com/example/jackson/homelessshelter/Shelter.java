package com.example.jackson.homelessshelter;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jackson on 3/12/2018.
 */

public class Shelter implements Parcelable {

    private String key;
    private String name;
    private int capacity;
    private String restrictions;
    private double longitude;
    private double latitude;
    private String address;
    private String special;
    private String phone;

    public Shelter(String key, String name, int capacity, String restrictions, double longitude,
                   double latitude, String address, String special, String phone) {
        this.key = key;
        this.name = name;
        this.capacity = capacity;
        this.restrictions = restrictions;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.special = special;
        this.phone = phone;
    }

    public Shelter(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);

        this.key = data[0];
        this.name = data[1];
        this.capacity = Integer.parseInt(data[2]);
        this.restrictions = data[3];
        this.longitude = Double.parseDouble(data[4]);
        this.latitude = Double.parseDouble(data[5]);
        this.address = address;
        this.special = special;
        this.phone = phone;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.key,
                this.name, Integer.toString(this.capacity),
                this.restrictions, Double.toString(this.longitude),
                Double.toString(this.latitude), this.address,
                this.special, this.phone
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

    public void setKey(String key) {
        this.key = key;
    }

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

    public String getAddress() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toString() {
        return name;
    }
}
