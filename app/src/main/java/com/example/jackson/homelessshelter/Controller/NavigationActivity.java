package com.example.jackson.homelessshelter.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.jackson.homelessshelter.Model.DrawerLocker;
import com.example.jackson.homelessshelter.Model.User;
import com.example.jackson.homelessshelter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This is the activity nesting all framgnets using the drawer
 */

public class NavigationActivity extends AppCompatActivity
        implements DrawerLocker, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private User currentUser;
    private TextView lastFirst;
    private TextView usernameEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Seeking Shelter");
        setSupportActionBar(toolbar);
        android.support.v4.app.Fragment welcome = new LoggedInFragment();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.frag_container, welcome);
        trans.commit();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String uid = fAuth.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference().child("user").child(uid);

        NavigationView navView = findViewById(R.id.nav_view);
        View hView = navView.getHeaderView(0);
        lastFirst = hView.findViewById(R.id.firstLast);
        usernameEmail = hView.findViewById(R.id.usernameOrEmail);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                lastFirst.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                if (currentUser.getEmail().split("@")[1].equals("seekingshelter.com")) {
                    usernameEmail.setText(currentUser.getUsername());
                } else {
                    usernameEmail.setText(currentUser.getEmail());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void unlocked(boolean enable) {
        if (enable) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    @NonNull
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myShelter) {
            android.support.v4.app.Fragment loggedIn = new LoggedInFragment();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.frag_container, loggedIn);
            trans.commit();
        } else if (id == R.id.shelters) {
            android.support.v4.app.Fragment shelters = new SheltersFragment();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.frag_container, shelters);
            trans.commit();
        } else if (id == R.id.map) {
            android.support.v4.app.Fragment map = new MapFragment();
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.frag_container, map);
            trans.commit();
        } else if (id == R.id.log_out) {
            FirebaseAuth.getInstance().signOut();
            Intent returnToLogin = new Intent(this, NotLoggedInActivity.class);
            finish();
            startActivity(returnToLogin);
//            android.support.v4.app.Fragment login = new LoginFragment();
//            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//            android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
//            trans.replace(R.id.frag_container, login);
//            trans.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
