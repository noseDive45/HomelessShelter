package com.example.jackson.homelessshelter;

/**
 * Created by Jackson on 2/26/2018.
 */

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CsvUploader {
    public static void main(String args[]) {
        String url = "C:/Users/Jackson/Desktop/2340/importMe.csv";
        DatabaseReference database;
        DatabaseReference shelterRef;
        String line = "";
        database = FirebaseDatabase.getInstance().getReference();
        shelterRef = database.child("shelter");
        try {
            BufferedReader br = new BufferedReader(new FileReader(url));
            int counter = 0;
            while ((line = br.readLine()) != null) {
                int counter2 = 0;
                while (line.split(",").length > 1) {
                    if (counter2 == 0) {
                        shelterRef.child(Integer.toString(counter));
                    } else if (counter2 == 1) {
                        shelterRef.child(Integer.toString(counter)).child("name")
                                .setValue(line.split(",")[0]);
                    } else if (counter2 == 2) {
                        shelterRef.child(Integer.toString(counter)).child("capacity")
                                .setValue(Integer.parseInt(line.split(",")[0]));
                    } else if (counter2 == 3) {
                        shelterRef.child(Integer.toString(counter)).child("restrictor").setValue(line.split(",")[0]);
                    } else if (counter2 == 4) {
                        shelterRef.child(Integer.toString(counter)).child("longitude")
                                .setValue(Double.parseDouble((line.split(",")[0])));
                    } else if (counter2 == 5) {
                        shelterRef.child(Integer.toString(counter)).child("latitude")
                                .setValue(Double.parseDouble((line.split(",")[0])));
                    } else if (counter2 == 6) {
                        shelterRef.child(Integer.toString(counter)).child("address")
                                .setValue((line.split(",")[0]));
                    } else if (counter2 == 7) {
                        shelterRef.child(Integer.toString(counter)).child("special")
                                .setValue((line.split(",")[0]));
                        shelterRef.child(Integer.toString(counter)).child("phone").setValue(line.split(",")[1]);
                    }
                    line = line.split(",")[1];
                    counter2++;
                }
                counter++;
            }
        } catch (Exception e) {
            Log.w("bad", "no worky");
        }
    }
}
