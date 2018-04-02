package com.example.jackson.homelessshelter.Controller;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.jackson.homelessshelter.Model.Shelter;
import com.example.jackson.homelessshelter.R;
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
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Map.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Map extends Fragment implements OnMapReadyCallback{

    private MapView mapView;
    private GoogleMap map;
    private LocationManager locationManager;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private String provider;
    private DatabaseReference database;
    private DatabaseReference shelters;
    private List<Shelter> baseList;
    private Spinner ageSpinner;
    private Spinner genderSpinner;
    private EditText searchCriteria;
    private List<Shelter> list;


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
//        View crap = (View) getActivity().findViewById(R.id.totalMap);
        ageSpinner = (Spinner) getActivity().findViewById(R.id.ageSpinner);
        genderSpinner = (Spinner) getActivity().findViewById(R.id.genderSpinner);
        searchCriteria = (EditText) getActivity().findViewById(R.id.searchContent);
        list = new ArrayList<>();
        searchCriteria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generateList(ageSpinner.getSelectedItem().toString(),
                        genderSpinner.getSelectedItem().toString(),
                        charSequence);
                putMarkers(list);
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
                putMarkers(list);
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
                putMarkers(list);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = getActivity().findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
//        mapView = getActivity().findViewById(R.id.mapView);
//        if (mapView != null) {
//            mapView = getActivity().findViewById(R.id.mapView);
//            mapView.onResume();
//            mapView.getMapAsync((OnMapReadyCallback) getActivity());
//        }
    }

    private void putMarkers(List<Shelter> list) {
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
        database = FirebaseDatabase.getInstance().getReference();
        shelters = database.child("shelter");
        // Populate list with shelter names
        baseList = new ArrayList<>();
        shelters.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Shelter addMe = dataSnapshot.getValue(Shelter.class);
                System.out.println(addMe);
                baseList.add(addMe);
                String snippet = "";
                if (addMe.getCapacity() == 0) {
                    snippet += "This facility is full";
                } else {
                    snippet += "Restrictions: " + addMe.getRestrictions();
                }
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(addMe.getLatitude(), addMe.getLongitude()))
                        .title(addMe.getName() + " (" + Integer.toString(addMe.getCapacity()) + ")")
                        .snippet(snippet));
                mapView.onResume();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                baseList.remove(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        System.out.println(baseList + "legend");
//        for (Shelter shelter : baseList) {
//            map.addMarker(new MarkerOptions()
//                    .position(new LatLng(shelter.getLatitude(), shelter.getLongitude()))
//                    .title(shelter.getName()).snippet("Restrictions: " + shelter.getRestrictions()));
//            System.out.println(shelter.getName() + "legend");
//        }
        mapView.onResume();
    }


    private void generateList(String ageFilt, String gendFilt, CharSequence search) {
        list.clear();
        list.addAll(baseList);
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
                    list.remove(shelts);
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
                    list.remove(shelts);
                }
            }
        }
        if (search.length() != 0) {
            List<Shelter> legend = new ArrayList<>();
            for (Shelter name : list) {
                if (!name.getName().toLowerCase().contains(search.toString().toLowerCase())) {
                    legend.add(name);
                }
            }
            list.removeAll(legend);
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("Legengerino");
        map = googleMap;
//        map.getUiSettings().setMyLocationButtonEnabled(false);
        if (checkLocationPermission()) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            map.setMyLocationEnabled(true);
//            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(map.getMyLocation()
//                    .getLatitude(), map.getMyLocation().getLongitude())));
            startMarkers();
//            return;
        }
//        map.setMyLocationEnabled(true);
//        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.8, 84.1)));
//        map.addMarker(new MarkerOptions().position(new LatLng(43.8, 84.1)).title("Fuck You"));
//        mapView.onResume();
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Services")
                        .setMessage("Allow Location Services?")
                        .setPositiveButton("For Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface  dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

}
