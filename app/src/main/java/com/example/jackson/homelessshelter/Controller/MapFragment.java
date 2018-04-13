package com.example.jackson.homelessshelter.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jackson.homelessshelter.Model.Shelter;
import com.example.jackson.homelessshelter.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This is the fragment containing the google map, its markers, and the filter
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private MapView mapView;
    private GoogleMap map;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private List<Shelter> baseList;
    private Spinner ageSpinner;
    private Spinner genderSpinner;
    private EditText searchCriteria;
    private List<Shelter> list;
    private Activity activity;


    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

//        initialize();
//    }

    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
//        View crap = (View) activity().findViewById(R.id.totalMap);

        ageSpinner = activity.findViewById(R.id.ageSpinner);
        genderSpinner = activity.findViewById(R.id.genderSpinner);
        searchCriteria = activity.findViewById(R.id.searchContent);
        list = new ArrayList<>();
        searchCriteria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Object age = ageSpinner.getSelectedItem();
                Object gender = genderSpinner.getSelectedItem();
                Object search = searchCriteria.getText();
                String ageString = age.toString();
                String genderString = gender.toString();
                String searchString = search.toString();
                generateList(ageString, genderString, searchString);
                putMarkers(list);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object age = ageSpinner.getSelectedItem();
                Object gender = genderSpinner.getSelectedItem();
                Object search = searchCriteria.getText();
                String ageString = age.toString();
                String genderString = gender.toString();
                String searchString = search.toString();
                generateList(ageString, genderString, searchString);
                putMarkers(list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Object age = ageSpinner.getSelectedItem();
                Object gender = genderSpinner.getSelectedItem();
                Object search = searchCriteria.getText();
                String ageString = age.toString();
                String genderString = gender.toString();
                String searchString = search.toString();
                generateList(ageString, genderString, searchString);
                putMarkers(list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        activity = getActivity();
        super.onViewCreated(view, savedInstanceState);
        mapView = activity.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
//        mapView = activity().findViewById(R.id.mapView);
//        if (mapView != null) {
//            mapView = activity().findViewById(R.id.mapView);
//            mapView.onResume();
//            mapView.getMapAsync((OnMapReadyCallback) activity());
//        }
    }

    private void putMarkers(Iterable<Shelter> list) {
        map.clear();
        for (Shelter each : list) {
            String snippet = "";
            if (each.getCapacity() == 0) {
                snippet += "This facility is full";
            } else {
                snippet += "Restrictions: " + each.getRestrictions();
            }
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(each.getLatitude(), each.getLongitude()))
                    .title(each.getName() + " (" + Integer.toString(each.getCapacity()) + ")")
                    .snippet(snippet));
        }
        mapView.onResume();
    }

    private void startMarkers() {
        DatabaseReference shelters = FirebaseDatabase.getInstance().getReference().child("shelter");
        // Populate list with shelter names
        baseList = new ArrayList<>();
        shelters.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Shelter addMe = dataSnapshot.getValue(Shelter.class);
                baseList.add(addMe);
                String snippet = "";
                if (addMe.getCapacity() == 0) {
                    snippet += "This facility is full";
                } else {
                    snippet += "Restrictions: " + addMe.getRestrictions();
                }
                LatLng latLng = new  LatLng(addMe.getLatitude(), addMe.getLongitude());
                String title = addMe.getName() + " (" + Integer.toString(addMe.getCapacity()) + ")";
                map.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet));
                mapView.onResume();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                baseList.remove(dataSnapshot.getValue(Shelter.class));
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mapView.onResume();
    }

    private void filterAge(String ageFilter) {
        if (!Objects.equals(ageFilter, "Anyone")) {
            for (Shelter shelters : baseList) {
                boolean confirmed = false;
                String checking = shelters.getRestrictions();
                while ((checking.split(",").length > 1) && (!confirmed)) {
                    if (Objects.equals(checking.split(",")[0], ageFilter)) {
                        confirmed = true;
                    }
                    checking = checking.split(",")[1];
                }
                if (Objects.equals(checking, ageFilter) || confirmed) {
                    Log.w("Swag", "Swag");
                } else {
                    list.remove(shelters);
                }
            }
        }
    }

    private void filterGender(String genderFilter) {
        if (!Objects.equals(genderFilter, "Both")) {
            for (Shelter shelters : baseList) {
                boolean confirmed = false;
                String checking = shelters.getRestrictions();
                while ((checking.split(",").length > 1) && (!confirmed)) {
                    if (Objects.equals(checking.split(",")[0], genderFilter)) {
                        confirmed = true;
                    }
                    checking = checking.split(",")[1];
                }
                if (Objects.equals(checking, genderFilter) || confirmed) {
                    Log.w("Swag", "Swag");
                } else {
                    list.remove(shelters);
                }
            }
        }
    }

    private void filterSearch(CharSequence search) {
        if (search.length() != 0) {
            Collection<Shelter> legend = new ArrayList<>();
            for (Shelter name : list) {
                String nameString = name.getName();
                String nameLC = nameString.toLowerCase();
                String searchString = search.toString();
                String searchLC = searchString.toLowerCase();
                if (!nameLC.contains(searchLC)) {
                    legend.add(name);
                }
            }
            list.removeAll(legend);
        }
    }


    private void generateList(String ageFilter, String genderFilter, CharSequence search) {
        list.clear();
        list.addAll(baseList);
        filterAge(ageFilter);
        filterGender(genderFilter);
        filterSearch(search);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        map.getUiSettings().setMyLocationButtonEnabled(false);
        double lat = 33.7490;
        double lon = -84.3880;
        LatLng atlanta = new LatLng(lat, lon);
        int zoomIn = 11;
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(atlanta);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(zoomIn);
        map.moveCamera(center);
        map.animateCamera(zoom);
        if (checkLocationPermission()) {
            map.setMyLocationEnabled(true);
            startMarkers();
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(activity)
                        .setTitle("Location Services")
                        .setMessage("Allow Location Services?")
                        .setPositiveButton("For Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface  dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


}
