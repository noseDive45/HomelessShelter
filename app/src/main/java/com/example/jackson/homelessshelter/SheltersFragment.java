package com.example.jackson.homelessshelter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SheltersFragment extends Fragment {

    private DatabaseReference database;
    private DatabaseReference shelters;
    private ArrayAdapter adapt;
    private ArrayList<String> list;
    private ListView listView;
    private DrawerLocker lockheed;
    private PopupWindow popup;
    private LayoutInflater layoutInflater;
    private float longitude;
    private float latitude;
    private String address;
    private String special;
    private String name;
    private String gender;
    private String restrictions;
    private String phone;
    private String passedName;
    private float capacity;
    private TextView nameView;
    private TextView addressView;
    private TextView phoneView;
    private LinearLayout lin;
    private TextView capacityView;
    private TextView genderView;
    private TextView longitudeView;
    private TextView latitudeView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lockheed.unlocked(true);
        return inflater.inflate(R.layout.activity_list_of_shelters, container, false);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lockheed = (DrawerLocker) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    // Initialize references and populate list

    private void initialize() {
        list = new ArrayList<String>();
        adapt = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
        listView= getActivity().findViewById(R.id.listView);
        // Allow list items to be selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                DatabaseReference shelters = FirebaseDatabase.getInstance().getReference("shelter");
                Query query = shelters.orderByChild("name").equalTo(itemValue);
                System.out.println("hey");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                System.out.println(snap.getValue());
                                System.out.println(snap.child("name").getValue(String.class));
                                name = snap.child("name").getValue(String.class);
                                special = snap.child("special").getValue(String.class);
                                address = snap.child("address").getValue(String.class);
                                restrictions = snap.child("restrictions").getValue(String.class);
                                phone = snap.child("phone").getValue(String.class);
                                capacity = snap.child("capacity").getValue(Float.class);
                                latitude = snap.child("latitude").getValue(Float.class);
                                longitude = snap.child("longitude").getValue(Float.class);
                                gender = snap.child("restrictions").getValue(String.class);
                            }
                            System.out.println("recorded");
                            layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popwindow, null);
                            lin = (LinearLayout) getActivity().findViewById(R.id.linlayShelter);
                            popup = new PopupWindow(container, (int) Math.round(lin.getWidth() * .7), (int) Math.round(lin.getHeight() * .8), true);
                            nameView = popup.getContentView().findViewById(R.id.name);
                            addressView = popup.getContentView().findViewById(R.id.address);
                            phoneView = popup.getContentView().findViewById(R.id.phone);
                            capacityView = popup.getContentView().findViewById(R.id.capacity);
                            longitudeView = popup.getContentView().findViewById(R.id.longitude);
                            latitudeView = popup.getContentView().findViewById(R.id.latitude);
                            genderView = popup.getContentView().findViewById(R.id.genders);
                            nameView.setText(name);
                            addressView.setText(address);
                            phoneView.setText(phone);
                            capacityView.setText(String.valueOf(capacity));
                            longitudeView.setText(Objects.toString(longitude));
                            latitudeView.setText(String.format("%f", latitude));
                            genderView.setText(gender);
                            popup.showAtLocation(getActivity().findViewById(R.id.linlayShelter), Gravity.CENTER, 0, 0);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        });
        listView.setAdapter(adapt);
        database = FirebaseDatabase.getInstance().getReference();
        shelters = database.child("shelter");
        // Populate list with shelter names
        shelters.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.child("name").getValue(String.class));
                adapt.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                list.remove(dataSnapshot.getValue(String.class));
                adapt.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
