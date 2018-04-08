package com.example.jackson.homelessshelter.Controller;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jackson.homelessshelter.Model.DrawerLocker;
import com.example.jackson.homelessshelter.R;

/**
 * This fragment is the home screen of the app currently with no actual use
 */

public class LoggedInFragment extends Fragment {

    private DrawerLocker lockheed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lockheed.unlocked(true);
        return inflater.inflate(R.layout.content_logged_in, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lockheed = (DrawerLocker) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
