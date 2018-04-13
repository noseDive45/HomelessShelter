package com.example.jackson.homelessshelter.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.jackson.homelessshelter.Model.DrawerLocker;
import com.example.jackson.homelessshelter.Model.Shelter;
import com.example.jackson.homelessshelter.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This fragment contains a list of shelters as well as a filter for seeking shelters
 */

public class SheltersFragment extends Fragment {

    private ArrayAdapter adapt;
    private List<String> list;
    private ListView listView;
    private DrawerLocker lockheed;
    private Spinner genderSpinner;
    private Spinner ageSpinner;
    private EditText searchCriteria;
    private List<Shelter> baseList;
    private Shelter queryDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lockheed.unlocked(true);
        return inflater.inflate(R.layout.activity_list_of_shelters, container, false);
    }

    @Override
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
        Activity activity = getActivity();
        list = new ArrayList<>();
        adapt = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, list);
        baseList = new ArrayList<>();
        listView = activity.findViewById(R.id.listView);
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
        DatabaseReference shelters = FirebaseDatabase.getInstance().getReference().child("shelter");
        // Populate list with shelter names
        shelters.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Shelter addMe = dataSnapshot.getValue(Shelter.class);
                baseList.add(addMe);
                list.add(dataSnapshot.child("name").getValue(String.class));
                adapt.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                baseList.remove(dataSnapshot.getValue(Shelter.class));
                adapt.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        ageSpinner = activity.findViewById(R.id.ageSpinner);
        genderSpinner = activity.findViewById(R.id.genderSpinner);
        searchCriteria = activity.findViewById(R.id.searchContent);
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
                adapt.notifyDataSetChanged();
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
                adapt.notifyDataSetChanged();
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
                adapt.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Determines whether the inputs need to be used to filter the list at this time
     * @param ageFilter String containing the age input
     * @param genderFilter String containing the gender input
     * @param search CharSequence containing the search input
     * @return boolean whether the list is being filtered
     */

    public boolean isBeingFiltered(String ageFilter, String genderFilter, CharSequence search) {
        return (!"Anyone".equals(ageFilter))
                || !("Both".equals(genderFilter))
                || (!"".equals(search));
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
                    list.remove(shelters.getName());
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
                    list.remove(shelters.getName());
                }
            }
        }
    }

    private void filterSearch(CharSequence search) {
        if (search.length() != 0) {
            Collection<String> legend = new ArrayList<>();
            for (String name : list) {
                String searchString = search.toString();
                String searchLC = searchString.toLowerCase();
                String nameLC = name.toLowerCase();
                if (!nameLC.contains(searchLC)) {
                    legend.add(name);
                }
            }
            list.removeAll(legend);
        }
    }

    private void generateList(String ageFilter, String genderFilter, CharSequence search) {
        isBeingFiltered(ageFilter, genderFilter, search);
        list.clear();
        for (Shelter shelters : baseList) {
            list.add(shelters.getName());
        }
        filterAge(ageFilter);
        filterGender(genderFilter);
        filterSearch(search);
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
                        queryDetails = snap.getValue(Shelter.class);
                        queryDetails.setCurrentShelter(snap.getRef());
                    }
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Shelter", queryDetails);
                    android.support.v4.app.Fragment det = new ShelterDetailsFragment();
                    android.support.v4.app.FragmentManager fm = getFragmentManager();
                    android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
                    det.setArguments(bundle);
                    trans.replace(R.id.frag_container, det).addToBackStack("last");
                    trans.commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
