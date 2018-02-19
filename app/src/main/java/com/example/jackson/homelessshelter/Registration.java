package com.example.jackson.homelessshelter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

// Firebase
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private Registration.UserRegistrationTask authTask = null;

    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private EditText username;
    private EditText password;
    private EditText password2;
    private View regView;
    private View progressView;
    private FirebaseAuth fAuth;
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button regButton = (Button) findViewById(R.id.reg_submit);

        firstName = (TextInputEditText) findViewById(R.id.reg_firstname);
        lastName = (TextInputEditText) findViewById(R.id.reg_lastname);
        password = (EditText) findViewById(R.id.reg_password);
        password2 = (EditText) findViewById(R.id.reg_password2);
        username = (EditText) findViewById(R.id.reg_email);
        fAuth = FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
        regView = findViewById(R.id.linlay);
        progressView = findViewById(R.id.progressBar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    public void attemptRegistration() {

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
        if (!TextUtils.isEmpty(un) && un.length() < 1) {
            username.setError("This field is mandatory!  Please enter a unique username or email.");
            focusView = username;
            cancel = true;
        }
        if (isTaken(un)) {
            username.setError("That username is taken.  Please try again.");
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
            showProgress(true);fAuth.createUserWithEmailAndPassword(un, p1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = fAuth.getCurrentUser();
                                Intent next = new Intent(Registration.this, LoggedIn.class);
                                startActivity(next);
                            } else {
                                FirebaseAuthException e = (FirebaseAuthException )task.getException();
                                Toast.makeText(Registration.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // ...
                        }
                    });
        }
    }
    private boolean isTaken(String un) {
        return false;
    }
    private boolean matches(String p1, String p2) {
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

    public class UserRegistrationTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserRegistrationTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            authTask = null;
            showProgress(false);
            finish();
        }

        @Override
        protected void onCancelled() {
            authTask = null;
            showProgress(false);
        }
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
