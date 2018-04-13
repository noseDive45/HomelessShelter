package com.example.jackson.homelessshelter.Controller;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.example.jackson.homelessshelter.Model.DrawerLocker;
import com.example.jackson.homelessshelter.Model.Shelter;
import com.example.jackson.homelessshelter.Model.User;
import com.example.jackson.homelessshelter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

/**
 * This fragment is the home screen of the app currently with no actual use
 */

public class LoggedInFragment extends Fragment {

//    private DrawerLocker lockheed;

    private User currentUser;
    private Shelter currentShelter;
    private TextView occupiedShelter;
    private TextView occupiedCount;
    private LinearLayout linlayCommit;
    private TextView warning;
    private Button release;
    private LinearLayout linlayCommitted;
    private android.support.v4.app.FragmentManager fragmentManager;
    private Button commitReservation;
    private EditText roomNumber;
    private boolean noShelter;
    private CharSequence shelterName;
    private TextView nameView;
    private TextView addressView;
    private TextView phoneView;
    private TextView capacityView;
    private TextView genderView;
    private TextView longitudeView;
    private TextView latitudeView;
    private LinearLayout linLayNoShelter;
    private LinearLayout details;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_logged_in, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        lockheed = (DrawerLocker) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    private void initialize() {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference().child("user").child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                currentUser.callDatabases();
                if (!noShelter) {
                    findShelterDetails();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Activity activity = getActivity();
        nameView = activity.findViewById(R.id.name);
        addressView = activity.findViewById(R.id.address);
        phoneView = activity.findViewById(R.id.phone);
        capacityView = activity.findViewById(R.id.capacity);
        genderView = activity.findViewById(R.id.genders);
        longitudeView = activity.findViewById(R.id.longitude);
        latitudeView = activity.findViewById(R.id.latitude);
        linlayCommitted = activity.findViewById(R.id.linLayCommitted);
        release = activity.findViewById(R.id.release);
        linLayNoShelter = activity.findViewById(R.id.hi);
        occupiedCount = activity.findViewById(R.id.occupiedCount);
        details = activity.findViewById(R.id.details);
        occupiedShelter = activity.findViewById(R.id.occupiedShelter);
        release.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseRoom();
            }
        });
    }

    private void findShelterDetails() {
        shelterName = currentUser.getCurrentShelter();
        if ("NA".equals(shelterName)) {
            linLayNoShelter.setVisibility(View.VISIBLE);
            linlayCommitted.setVisibility(View.GONE);
        } else {
            linlayCommitted.setVisibility(View.VISIBLE);
            linLayNoShelter.setVisibility(View.GONE);
            occupiedCount.setText(Integer.toString(currentUser.getOccupiedBeds()));
            occupiedShelter.setText("here");
            DatabaseReference shelters = FirebaseDatabase.getInstance().getReference("shelter");
            Query query = shelters.orderByChild("name").equalTo((String) shelterName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            currentShelter = snap.getValue(Shelter.class);
                            currentShelter.setCurrentShelter(snap.getRef());
                            nameView.setText(currentShelter.getName());
                            addressView.setText(currentShelter.getAddress());
                            phoneView.setText(currentShelter.getPhone());
                            genderView.setText(currentShelter.getRestrictions());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

        private void releaseRoom() {
            currentShelter.setCapacityFirebase(currentUser.getOccupiedBeds()
                    + currentShelter.getCapacity());
            currentUser.setCurrentShelterFirebase("NA");
            currentUser.setOccupiedBedsFirebase(0);
            linlayCommitted.setVisibility(View.GONE);
            details.setVisibility(View.GONE);
        }
}
