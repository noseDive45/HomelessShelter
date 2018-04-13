package com.example.jackson.homelessshelter.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jackson.homelessshelter.R;

public class NotLoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_logged_in);
        android.support.v4.app.Fragment login = new LoginFragment();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.notLoggedInFrag, login);
        trans.commit();
    }
}
