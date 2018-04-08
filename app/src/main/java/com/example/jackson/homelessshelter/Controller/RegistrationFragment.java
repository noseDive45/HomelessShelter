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
import android.text.Editable;
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

/**
 * This fragment allows for registration on the app
 */
public class RegistrationFragment extends Fragment {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private DatabaseReference userRef;
    private EditText username;
    private EditText password;
    private EditText password2;
    private View regView;
    private View progressView;
    private FirebaseAuth fAuth;
    private ToggleButton admin;
    private DrawerLocker lockheed;
    private Activity activity;
    private Editable fnEditable;
    private Editable lnEditable;
    private Editable unEditable;
    private Editable p1Editable;
    private Editable p2Editable;

    @Override
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
        activity = getActivity();
        admin = activity.findViewById(R.id.admin);
        userRef = FirebaseDatabase.getInstance().getReference().child("user");
        firstName = activity.findViewById(R.id.reg_firstname);
        lastName = activity.findViewById(R.id.reg_lastname);
        password = activity.findViewById(R.id.reg_password);
        password2 = activity.findViewById(R.id.reg_password2);
        username = activity.findViewById(R.id.reg_email);
        fAuth = FirebaseAuth.getInstance();
        regView = activity.findViewById(R.id.linlay);
        progressView = activity.findViewById(R.id.progressBar);

        // Give button its function
        Button regButton = activity.findViewById(R.id.reg_submit);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
        fnEditable = firstName.getText();
        lnEditable = lastName.getText();
        unEditable = username.getText();
        p1Editable = password.getText();
        p2Editable = password2.getText();
        firstName.setError(null);
        lastName.setError(null);
        username.setError(null);
        password.setError(null);
        password2.setError(null);
    }

    // Attempts registration starting at my cases then proceeding to
    // firebase's cases


    private void attemptRegistration() {

        String fn = fnEditable.toString();
        String ln = lnEditable.toString();
        String un = unEditable.toString();
        String p1 = p1Editable.toString();
        String p2 = p2Editable.toString();
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

        if ((!TextUtils.isEmpty(un)) && (un.length() < 1)) {
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
            password2.setError("The passwords you entered do not match.");
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

            Log.e("Trying", "login");
            if (!isValidEmail(un)) {
                un += "@seekingshelter.com";
            }
            showProgress(true);
            Log.w("Username", un);
            fAuth.createUserWithEmailAndPassword(un, p1)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
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
                                Toast.makeText(activity,
                                        "Failed Registration: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }

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
            userRef.child(uid).child("username").setValue(unEditable.toString());
        } else {
            userRef.child(uid).child("username")
                    .setValue(unEditable.toString().split("@")[0]);
        }
        userRef.child(uid).child("admin").setValue(admin.isChecked());
        userRef.child(uid).child("firstName").setValue(fnEditable.toString());
        userRef.child(uid).child("occupiedBeds").setValue(0);
        userRef.child(uid).child("lastName").setValue(lnEditable.toString());
    }

    // Sees if you are registering with an email or a username

    /**
     * Checks if the user is registering with an email or username
     * @param email String the username or email
     * @return boolean saying if the input is an email
     */

    public boolean isValidEmail(String email) {
        return (email.split("@").length > 1)
                && (email.split("\\.").length > 1)
                && (email.split("@")[1].length() >= 5)
                && (email.split("\\.")[1].length() == 3);
    }

    /**
     * Checks if both the passwords match
     * @param p1 String the first password input
     * @param p2 String the second password input
     * @return boolean whether the passwords match
     */

    public boolean matches(CharSequence p1, CharSequence p2) {
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
    }
}
