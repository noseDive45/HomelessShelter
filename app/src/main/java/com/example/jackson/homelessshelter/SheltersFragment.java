package com.example.jackson.homelessshelter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SheltersFragment extends Fragment {

    private DatabaseReference database;
    private DatabaseReference shelters;
    private ArrayAdapter adapt;
    private ArrayList<String> list;
    private ListView listView;
    private DrawerLocker lockheed;
    private PopupWindow shelterPopup;
    private PopupWindow filterPopup;
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
    private ConstraintLayout lin;
    private TextView capacityView;
    private TextView genderView;
    private TextView longitudeView;
    private TextView latitudeView;
    private Spinner genderSpinner;
    private Spinner ageSpinner;
    private EditText searchCriteria;
    private Button submitFilter;
    private ArrayList<Shelter> baseList;
    private String lastSearch;

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
        lin = (ConstraintLayout) getActivity().findViewById(R.id.mainViewShelter);
        list = new ArrayList<String>();
        adapt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        baseList = new ArrayList<Shelter>();
        listView = getActivity().findViewById(R.id.listView);
        listView.setAdapter(adapt);
        // Allow list items to be selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);
                giveDetails(itemValue);
            }
        });
        database = FirebaseDatabase.getInstance().getReference();
        shelters = database.child("shelter");
        // Populate list with shelter names
        shelters.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Shelter addMe = new Shelter("useless",
                        dataSnapshot.child("name").getValue(String.class),
                        dataSnapshot.child("capacity").getValue(Integer.class),
                        dataSnapshot.child("restrictions").getValue(String.class),
                        dataSnapshot.child("longitude").getValue(Double.class),
                        dataSnapshot.child("latitude").getValue(Double.class),
                        dataSnapshot.child("address").getValue(String.class),
                        dataSnapshot.child("special").getValue(String.class),
                        dataSnapshot.child("phone").getValue(String.class));
                baseList.add(addMe);
                list.add(dataSnapshot.child("name").getValue(String.class));
                adapt.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                baseList.remove(dataSnapshot.getValue(String.class));
                adapt.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ageSpinner = (Spinner) getActivity().findViewById(R.id.ageSpinner);
        genderSpinner = (Spinner) getActivity().findViewById(R.id.genderSpinner);
        searchCriteria = (EditText) getActivity().findViewById(R.id.searchContent);
        lastSearch = "";
        searchCriteria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generateList(ageSpinner.getSelectedItem().toString(),
                        genderSpinner.getSelectedItem().toString(),
                        charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                generateList(ageSpinner.getSelectedItem().toString(),
                        genderSpinner.getSelectedItem().toString(),
                        searchCriteria.getText().toString());
            }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    return;
                }
        });
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                generateList(ageSpinner.getSelectedItem().toString(),
                        genderSpinner.getSelectedItem().toString(),
                        searchCriteria.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    private void generateList(String ageFilt, String gendFilt, CharSequence search) {
        list.clear();
        for (Shelter shelts : baseList) {
            list.add(shelts.getName());
        }
        if (!Objects.equals(gendFilt, "Both")) {
            for (Shelter shelts : baseList) {
                boolean confirmed = false;
                String checking = shelts.getRestrictions();
                while (checking.split(",").length > 1 && !confirmed) {
                    if (Objects.equals(checking.split(",")[0], gendFilt)) {
                        confirmed = true;
                    }
                    checking = checking.split(",")[1];
                }
                if (Objects.equals(checking, gendFilt) || confirmed) {
                } else {
                    list.remove(shelts.getName());
                }
            }
        }
        if (!Objects.equals(ageFilt, "Anyone")) {
            for (Shelter shelts : baseList) {
                boolean confirmed = false;
                String checking = shelts.getRestrictions();
                while (checking.split(",").length > 1 && !confirmed) {
                    if (Objects.equals(checking.split(",")[0], ageFilt)) {
                        confirmed = true;
                    }
                    checking = checking.split(",")[1];
                }
                System.out.println("confirmed?" + confirmed);
                if (Objects.equals(checking, ageFilt) || confirmed) {
                } else {
                    list.remove(shelts.getName());
                }
            }
        }
        System.out.println("swap");
        System.out.println(!Objects.equals(lastSearch, search.toString()));
        System.out.println(!lastSearch.equals(search.toString()));
        if (search.length() != 0) {
            ArrayList<String> legend = new ArrayList<>();
            for (String name : list) {
                if (!name.toLowerCase().contains(search.toString().toLowerCase())) {
                    legend.add(name);
                }
            }
            list.removeAll(legend);
        }
        adapt.notifyDataSetChanged();
        lastSearch = search.toString();
    }

    // Create popup window with shelter details

    private void giveDetails(String itemValue) {
        DatabaseReference shelters = FirebaseDatabase.getInstance().getReference("shelter");
        Query query = shelters.orderByChild("name").equalTo(itemValue);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
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
                    // Create shelter details popup window
                    layoutInflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.popwindow, null);
                    shelterPopup = new PopupWindow(container, (int) Math.round(lin.getWidth() * .7), (int) Math.round(lin.getHeight() * .8), true);
                    nameView = shelterPopup.getContentView().findViewById(R.id.name);
                    addressView = shelterPopup.getContentView().findViewById(R.id.address);
                    phoneView = shelterPopup.getContentView().findViewById(R.id.phone);
                    capacityView = shelterPopup.getContentView().findViewById(R.id.capacity);
                    longitudeView = shelterPopup.getContentView().findViewById(R.id.longitude);
                    latitudeView = shelterPopup.getContentView().findViewById(R.id.latitude);
                    genderView = shelterPopup.getContentView().findViewById(R.id.genders);
                    nameView.setText(name);
                    addressView.setText(address);
                    phoneView.setText(phone);
                    capacityView.setText(String.valueOf(capacity));
                    longitudeView.setText(Objects.toString(longitude));
                    latitudeView.setText(String.format("%f", latitude));
                    genderView.setText(gender);
                    shelterPopup.showAtLocation(getActivity().findViewById(R.id.linlayShelter), Gravity.CENTER, 0, 0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
