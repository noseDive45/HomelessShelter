package com.example.jackson.homelessshelter.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jackson.homelessshelter.Model.DrawerLocker;
import com.example.jackson.homelessshelter.Model.Shelter;
import com.example.jackson.homelessshelter.Model.User;
import com.example.jackson.homelessshelter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShelterDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShelterDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShelterDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private TextView nameView;
    private TextView addressView;
    private TextView phoneView;
    private TextView capacityView;
    private TextView genderView;
    private TextView longitudeView;
    private TextView latitudeView;
    private DrawerLocker lockheed;
    private EditText roomNumber;
    private Button commitReservation;
    private Shelter currentShelter;
    private Bundle bundle;
    private android.support.v4.app.FragmentManager fragmentManager;
    private FirebaseAuth fAuth;
    private DatabaseReference database;
    private User currentUser;
    private LinearLayout linlayCommit;
    private TextView occupiedShelter;
    private TextView occupiedCount;
    private LinearLayout linlayCommitted;
    private TextView warning;
    private DatabaseReference userRef;
    private Button release;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lockheed.unlocked(false);
        return inflater.inflate(R.layout.fragment_shelter_details, container, false);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lockheed = (DrawerLocker) activity;
    }

    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        userRef = database.child("user").child(fAuth.getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                currentUser.callDatabases();
                determineVisibility();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        fragmentManager = getFragmentManager();
        linlayCommit = getActivity().findViewById(R.id.aboveCommit);
        nameView = getActivity().findViewById(R.id.name);
        addressView = getActivity().findViewById(R.id.address);
        phoneView = getActivity().findViewById(R.id.phone);
        capacityView = getActivity().findViewById(R.id.capacity);
        genderView = getActivity().findViewById(R.id.genders);
        longitudeView = getActivity().findViewById(R.id.longitude);
        latitudeView = getActivity().findViewById(R.id.latitude);
        roomNumber = getActivity().findViewById(R.id.numberOfRooms);
        occupiedCount = getActivity().findViewById(R.id.occupiedCount);
        occupiedShelter = getActivity().findViewById(R.id.occupiedShelter);
        linlayCommitted = getActivity().findViewById(R.id.linLayCommitted);
        warning = getActivity().findViewById(R.id.warning);
        bundle = getArguments();
        currentShelter = bundle.getParcelable("Shelter");
        nameView.setText(currentShelter.getName());
        addressView.setText(currentShelter.getAddress());
        phoneView.setText(currentShelter.getPhone());
        capacityView.setText(Integer.toString(currentShelter.getCapacity()));
        genderView.setText(currentShelter.getRestrictions());
        longitudeView.setText(Double.toString(currentShelter.getLongitude()));
        latitudeView.setText(Double.toString(currentShelter.getLatitude()));
        commitReservation = (Button) getActivity().findViewById(R.id.commit);
        commitReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveARoom();
            }
        });
        release = getActivity().findViewById(R.id.release);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseRoom();
            }
        });
    }

    private void reserveARoom() {
        String rooms = roomNumber.getText().toString();
        int number = 0;
        View focusView = null;
        roomNumber.setError(null);
        if (!rooms.equals("")) {
            try {
                number = Integer.parseInt(rooms);
            } catch (Exception e) {

            }
            if (number == 0) {
                roomNumber.setError("You cannot reserve 0 rooms");
                focusView = roomNumber;
                focusView.requestFocus();
            } else if (currentShelter.getCapacity() >= number) {
                // Reserve the room && update capacity && update occupying for user
                currentShelter.setCapacityFirebase(currentShelter.getCapacity() - number);
                currentUser.setCurrentShelter(currentShelter.getName(), true);
                currentUser.setOccupiedBeds(number, true);
                linlayCommit.setVisibility(View.GONE);
                commitReservation.setVisibility(View.GONE);
                fragmentManager.popBackStack();
            } else {
                // Say the number of rooms requested is more than are available
                roomNumber.setError("You have requested more rooms than are available.  "
                        + "Please check the capacity above.");
                focusView = roomNumber;
                focusView.requestFocus();
            }
        }
    }

    private void releaseRoom() {
        currentShelter.setCapacityFirebase(currentUser.getOccupiedBeds()
                + currentShelter.getCapacity());
        currentUser.setCurrentShelter("NA", true);
        currentUser.setOccupiedBeds(0, true);
        System.out.println("howmanytimesforme");
        linlayCommitted.setVisibility(View.GONE);
        release.setVisibility(View.GONE);
        fragmentManager.popBackStack();
    }


    private void determineVisibility() {
        if (currentUser.getOccupiedBeds() == 0) {
            linlayCommit.setVisibility(View.VISIBLE);
            commitReservation.setVisibility(View.VISIBLE);
        } else if (currentUser.getCurrentShelter().equals(currentShelter.getName())) {
            occupiedCount.setText(Integer.toString(currentUser.getOccupiedBeds()));
            occupiedShelter.setText(currentUser.getCurrentShelter());
            linlayCommitted.setVisibility(View.VISIBLE);
            release.setVisibility(View.VISIBLE);
        } else {
            warning.setVisibility(View.VISIBLE);
            occupiedCount.setText(Integer.toString(currentUser.getOccupiedBeds()));
            occupiedShelter.setText(currentUser.getCurrentShelter());
            linlayCommitted.setVisibility(View.VISIBLE);
        }
    }










}
