package com.example.jackson.homelessshelter.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
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

import java.util.Locale;

/**
 * This activity houses information regarding the selected shelter
 */

public class ShelterDetailsFragment extends Fragment {

    private DrawerLocker lockheed;
    private EditText roomNumber;
    private Button commitReservation;
    private Shelter currentShelter;
    private android.support.v4.app.FragmentManager fragmentManager;
    private User currentUser;
    private LinearLayout linlayCommit;
    private TextView occupiedShelter;
    private TextView occupiedCount;
    private LinearLayout linlayCommitted;
    private TextView warning;
    private Button release;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        lockheed.unlocked(false);
        return inflater.inflate(R.layout.fragment_shelter_details, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lockheed = (DrawerLocker) activity;
    }

    /**
     * This method allows for the return to previous fragments
     */

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

    private void setEverything() {
        Activity activity = getActivity();
        commitReservation = activity.findViewById(R.id.commit);
        roomNumber = activity.findViewById(R.id.numberOfRooms);
        occupiedShelter = activity.findViewById(R.id.occupiedShelter);
        occupiedCount = activity.findViewById(R.id.occupiedCount);
        linlayCommitted = activity.findViewById(R.id.linLayCommitted);
        warning = activity.findViewById(R.id.warning);
        commitReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserveARoom();
            }
        });
    }

    private void initialize() {
        setEverything();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        Activity activity = getActivity();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference().child("user").child(fAuth.getUid());

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
        linlayCommit = activity.findViewById(R.id.aboveCommit);
        TextView nameView = activity.findViewById(R.id.name);
        TextView addressView = activity.findViewById(R.id.address);
        TextView phoneView = activity.findViewById(R.id.phone);
        TextView capacityView = activity.findViewById(R.id.capacity);
        TextView genderView = activity.findViewById(R.id.genders);
        TextView longitudeView = activity.findViewById(R.id.longitude);
        TextView latitudeView = activity.findViewById(R.id.latitude);
        Bundle bundle = getArguments();
        currentShelter = bundle.getParcelable("Shelter");
        nameView.setText(currentShelter.getName());
        addressView.setText(currentShelter.getAddress());
        phoneView.setText(currentShelter.getPhone());
        capacityView.setText(String.format(Locale.getDefault(),
                "%d", currentShelter.getCapacity()));
        genderView.setText(currentShelter.getRestrictions());
        longitudeView.setText(String.format(Locale.getDefault(),
                "%f", currentShelter.getLongitude()));
        latitudeView.setText(String.format(Locale.getDefault(),
                "%f", currentShelter.getLatitude()));
        release = activity.findViewById(R.id.release);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseRoom();
            }
        });
    }

    private void reserveARoom() {
        Editable swag = roomNumber.getText();
        String rooms = swag.toString();
        int number = 0;
        View focusView;
        roomNumber.setError(null);
        if (!"".equals(rooms)) {
            try {
                number = Integer.parseInt(rooms);
            } catch (Exception e) {
                roomNumber.setError("You have not entered the number of rooms you desire");
            }
            if (number == 0) {
                roomNumber.setError("You cannot reserve 0 rooms");
                focusView = roomNumber;
                focusView.requestFocus();
            } else if (currentShelter.getCapacity() >= number) {
                // Reserve the room && update capacity && update occupying for user
                currentShelter.setCapacityFirebase(currentShelter.getCapacity() - number);
                currentUser.setCurrentShelterFirebase(currentShelter.getName());
                currentUser.setOccupiedBedsFirebase(number);
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
        currentUser.setCurrentShelterFirebase("NA");
        currentUser.setOccupiedBedsFirebase(0);
        linlayCommitted.setVisibility(View.GONE);
        linlayCommit.setVisibility(View.VISIBLE);
        fragmentManager.popBackStack();
    }


    private void determineVisibility() {
        CharSequence currentUserShelter = currentUser.getCurrentShelter();
        CharSequence currentShelterName = currentShelter.getName();
        if (currentUser.getOccupiedBeds() == 0) {
            linlayCommitted.setVisibility(View.GONE);
            linlayCommit.setVisibility(View.VISIBLE);
            commitReservation.setVisibility(View.VISIBLE);
        } else if (currentUserShelter.equals(currentShelterName)) {
            occupiedCount.setText(String.format(Locale.getDefault(),
                    "%d", currentUser.getOccupiedBeds()));
            occupiedShelter.setText(currentUser.getCurrentShelter());
            linlayCommitted.setVisibility(View.VISIBLE);
            release.setVisibility(View.VISIBLE);
        } else {
            warning.setVisibility(View.VISIBLE);
            occupiedCount.setText(String.format(Locale.getDefault(),
                    "%d", currentUser.getOccupiedBeds()));
            System.out.println("fuck" + currentUser.getCurrentShelter());
            occupiedShelter.setText(currentUser.getCurrentShelter());
            linlayCommitted.setVisibility(View.VISIBLE);
            release.setVisibility(View.GONE);
        }
    }










}
