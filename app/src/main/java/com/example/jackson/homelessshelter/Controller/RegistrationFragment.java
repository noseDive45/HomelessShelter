package com.example.jackson.homelessshelter.Controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jackson.homelessshelter.Model.DrawerLocker;
import com.example.jackson.homelessshelter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// Firebase

//
public class RegistrationFragment extends Fragment {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private DatabaseReference database;
    private DatabaseReference userRef;
    private EditText username;
    private EditText password;
    private EditText password2;
    private View regView;
    private View progressView;
    private FirebaseAuth fAuth;
    private ToggleButton admin;
    private DrawerLocker lockheed;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        lockheed.unlocked(false);
        return inflater.inflate(R.layout.activity_registration, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeValues();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lockheed = (DrawerLocker) activity;
    }

    // Get all references on start

    private void initializeValues() {
        admin = (ToggleButton) getActivity().findViewById(R.id.admin);
        database = FirebaseDatabase.getInstance().getReference();
        userRef = database.child("user");
        firstName = (TextInputEditText) getActivity().findViewById(R.id.reg_firstname);
        lastName = (TextInputEditText) getActivity().findViewById(R.id.reg_lastname);
        password = (EditText) getActivity().findViewById(R.id.reg_password);
        password2 = (EditText) getActivity().findViewById(R.id.reg_password2);
        username = (EditText) getActivity().findViewById(R.id.reg_email);
        fAuth = FirebaseAuth.getInstance();
        regView = getActivity().findViewById(R.id.linlay);
        progressView = getActivity().findViewById(R.id.progressBar);

        // Give button its function
        Button regButton = (Button) getActivity().findViewById(R.id.reg_submit);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    // Attempts registration starting at my cases then proceeding to
    // firebase's cases

    private void attemptRegistration() {

        firstName.setError(null);
        lastName.setError(null);
        username.setError(null);
        password.setError(null);
        password2.setError(null);
        String fn = firstName.getText().toString();
        String ln = lastName.getText().toString();
        String un = username.getText().toString();
        String p1 = password.getText().toString();
        String p2 = password2.getText().toString();
        boolean cancel = false;
        View focusView = null;

        // Check for a first name

        if (TextUtils.isEmpty(fn)) {
            firstName.setError("This field is mandatory!  Please enter your first name.");
            focusView = firstName;
            cancel = true;
        }

        // Check for a last name

        if (TextUtils.isEmpty(ln)) {
            lastName.setError("This field is mandatory!  Please enter your last name.");
            focusView = lastName;
            cancel = true;
        }

        // Check for a username
        if (TextUtils.isEmpty(un)) {
            username.setError("This field is mandatory!  Please enter a username.");
            focusView = username;
            cancel = true;
        }
        // Makes sure username is not ignored as an input

        if (!TextUtils.isEmpty(un) && un.length() < 1) {
            username.setError("This field is mandatory!  Please enter a unique username or email.");
            focusView = username;
            cancel = true;
        }

        // Check for a valid password

        if (TextUtils.isEmpty(p1)) {
            password.setError("This field is mandatory!  Please enter your password.");
            focusView = password;
            cancel = true;
        }

        // Check for a valid password confirmation

        if (TextUtils.isEmpty(p2)) {
            password2.setError("This field is mandatory!  Please verify your password.");
            focusView = password2;
            cancel = true;
        }

        // Checks if the passwords match

        if (!TextUtils.isEmpty(p1) && !TextUtils.isEmpty(p2) && !matches(p1, p2)) {
            password2.setError("The passwords you entered do not matach.");
            focusView = password2;
            cancel = true;
        }
        if (cancel) {

            // There was an error; don't attempt login and focus the first
            // form field with an error.

            focusView.requestFocus();
        } else {

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            System.out.println(un + " " + p1);
            Log.e("Trying", "login");
            if (!isValidEmail(un)) {
                un += "@seekingshelter.com";
            }
            showProgress(true);
            Log.w("Username", un);
            fAuth.createUserWithEmailAndPassword(un, p1)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = fAuth.getCurrentUser();
                                addToDataBase(user);
                                android.support.v4.app.Fragment loggedIn = new LoggedInFragment();
                                android.support.v4.app.FragmentManager fm = getFragmentManager();
                                android.support.v4.app.FragmentTransaction trans = fm
                                        .beginTransaction();
                                trans.replace(R.id.frag_container, loggedIn);
                                trans.commit();
                            } else {
                                FirebaseAuthException e = (FirebaseAuthException )task
                                        .getException();
                                Toast.makeText(getActivity(),
                                        "Failed Registration: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                showProgress(false);
                                return;
                            }

                            // ...
                        }
                    });
        }
    }

    // Adds user data to firebase

    private void addToDataBase(FirebaseUser user) {
        String uid = user.getUid();
        userRef.child(uid);
        userRef.child(uid).child("email").setValue(user.getEmail());
        if (!isValidEmail(username.getText().toString())) {
            userRef.child(uid).child("username").setValue(username.getText().toString());
        } else {
            userRef.child(uid).child("username")
                    .setValue(username.getText().toString().split("@")[0]);
        }
        userRef.child(uid).child("admin").setValue(admin.isChecked());
        userRef.child(uid).child("firstName").setValue(firstName.getText().toString());
        userRef.child(uid).child("occupiedBeds").setValue(0);
        userRef.child(uid).child("lastName").setValue(lastName.getText().toString());
    }

    // Sees if you are registering with an email or a username

    private boolean isValidEmail(String email) {
        if (email.split("@").length > 1 && email.split("\\.").length > 1
                && email.split("@")[1].length() >= 4
                && email.split("\\.")[1].length() == 3) {
            return true;
        }
        return false;
    }

    // Checks if both the passwords match

    private boolean matches(CharSequence p1, CharSequence p2) {
        int counter = 0;
        if (p1.length() != p2.length()) {
            return false;
        }
        for (int i = 0; i < p1.length(); i++) {
            if (p1.charAt(i) == p2.charAt(i))  {
                counter++;
            }
        }
        return counter == p1.length();
    }

    /**
     * Shows the progress UI and hides the login form.
     */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            regView.setVisibility(show ? View.GONE : View.VISIBLE);
            regView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    regView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            regView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
