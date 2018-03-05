package com.example.jackson.homelessshelter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.Fragment;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Firebase
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginFragment extends Fragment implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private boolean admin;
    private DrawerLocker lockheed;

    // Get firebase auth & database instance
    private FirebaseAuth fAuth;
    private DatabaseReference database;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lockheed.unlocked(false);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();

        // See if user already logged in

        if (fAuth.getCurrentUser() != null) {
            nextPageSelection();
        }
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        lockheed = (DrawerLocker) activity;
    }

    // Get initial References

    private void initialize() {
        mEmailView = (AutoCompleteTextView) getView().findViewById(R.id.email);
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        mPasswordView = (EditText) getView().findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) getView().findViewById(R.id.log_in);
        Button registration = (Button) getView().findViewById(R.id.register);
        registration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = getView().findViewById(R.id.login_form);
        mProgressView = getView().findViewById(R.id.login_progress);
    }

//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }

//        getLoaderManager().initLoader(0, null, getActivity());
//    }

//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }

    private void nextPageSelection() {
//        database.child("user").child(fAuth.getCurrentUser()
//                .getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                admin = (boolean) dataSnapshot.child("admin").getValue();
//                if (admin) {
//                    Intent next = new Intent(getActivity(), AdminSelection.class);
//                    System.out.println("2");
//                    startActivity(next);
//                } else {
//                    Intent next = new Intent(getActivity(), LoggedIn.class);
//                    System.out.println("3");
//                    startActivity(next);
//                }
//            }

//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

//        System.out.println(admin);
//        System.out.println("1");
//        if (admin) {
//            Intent next = new Intent(LoginFragment.this, AdminSelection.class);
//            System.out.println("2");
//            startActivity(next);
//        } else {
//            Intent next = new Intent(LoginFragment.this, LoggedIn.class);
//            System.out.println("3");
//            startActivity(next);
//        }
        android.support.v4.app.Fragment loggedIn = new LoggedInFragment();
        android.support.v4.app.FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.frag_container, loggedIn);
        trans.commit();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }

        Log.wtf("Trying", "login");
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
//        else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (!isValidEmail(email)) {
                email += "@seekingshelter.com";
            }
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(),
                    new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = fAuth.getCurrentUser();
                        showProgress(false);
                        nextPageSelection();
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            mEmailView.setError("Invalid Emaild Id");
                            mEmailView.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            mPasswordView.setError("Invalid Password");
                            mPasswordView.requestFocus();
                        } catch (FirebaseNetworkException e) {
                            Log.wtf("Trying", "login");
                        } catch (Exception e) {
                            Log.wtf("Trying", "login");
                        }
                        showProgress(false);
                    }
                }
            });
//            mAuthTask = new UserLoginTask(email, password);
//            Intent next = new Intent(LoginFragment.this, LoggedIn.class);
//            startActivity(next);
        }
    }

    // Checks if user is logging in with an email or a username

    private boolean isValidEmail(String email) {
        if (email.split("@").length > 1 && email.split("\\.").length > 1
                && email.split("@")[1].length() >= 4
                && email.split("\\.")[1].length() == 3) {
            return true;
        }
        return false;
    }

    public void register() {
        android.support.v4.app.Fragment reg = new RegistrationFragment();
        android.support.v4.app.FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.frag_container, reg);
        trans.commit();
//        Intent regScreen = new Intent(getActivity(), Registration.class);
//        startActivity(regScreen);
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                                                                     .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }
//
//            // TODO: register the new account here.
//            return true;
//        }

//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }

    // Manual upload of shelters (f that)

//        shelterRef = database.child("shelter");
//        shelterRef.child("0");
//        shelterRef.child("0").child("name").setValue("My Sister's House");
//        shelterRef.child("0").child("roomType").setValue("single");
//        shelterRef.child("0").child("capacity").setValue(264);
//        shelterRef.child("0").child("restrictions").setValue("Women,Children");
//        shelterRef.child("0").child("longitude").setValue(-84.410142);
//        shelterRef.child("0").child("latitude").setValue(33.780174);
//        shelterRef.child("0").child("address").setValue("921 Howell Mill Road, Atlanta, Georgia 30318");
//        shelterRef.child("0").child("special").setValue("Temporary,Emergency,Residential Recovery");
//        shelterRef.child("0").child("phone").setValue("(404)367-2465");
//        shelterRef.child("1");
//        shelterRef.child("1").child("name").setValue("The Atlanta Day Center for Women & Children");
//        shelterRef.child("1").child("roomType").setValue("single");
//        shelterRef.child("1").child("capacity").setValue(140);
//        shelterRef.child("1").child("restrictions").setValue("Women,Children");
//        shelterRef.child("1").child("longitude").setValue(-84.408771);
//        shelterRef.child("1").child("latitude").setValue(33.784889);
//        shelterRef.child("1").child("address").setValue("655 Ethel Street, Atlanta, Georgia 30318");
//        shelterRef.child("1").child("special").setValue("Career Facilitation");
//        shelterRef.child("1").child("phone").setValue("(404)588-4007");
//        shelterRef.child("2");
//        shelterRef.child("2").child("name").setValue("The Shepherd's Inn");
//        shelterRef.child("2").child("roomType").setValue("single");
//        shelterRef.child("2").child("capacity").setValue(450);
//        shelterRef.child("2").child("restrictions").setValue("Men");
//        shelterRef.child("2").child("longitude").setValue(-84.39265);
//        shelterRef.child("2").child("latitude").setValue(33.765162);
//        shelterRef.child("2").child("address").setValue("156 Mills Street, Atlanta, Georgia 30313");
//        shelterRef.child("2").child("special").setValue("Temporary,Residential Recovery");
//        shelterRef.child("2").child("phone").setValue("(404)367-2493");
//        shelterRef.child("3");
//        shelterRef.child("3").child("name").setValue("Fuqua Hall");
//        shelterRef.child("3").child("roomType").setValue("single");
//        shelterRef.child("3").child("capacity").setValue(92);
//        shelterRef.child("3").child("restrictions").setValue("Men");
//        shelterRef.child("3").child("longitude").setValue(-84.392273);
//        shelterRef.child("3").child("latitude").setValue(33.76515);
//        shelterRef.child("3").child("address").setValue("144 Mills Street, Atlanta, Georgia 30313");
//        shelterRef.child("3").child("special").setValue("Transitional Housing");
//        shelterRef.child("3").child("phone").setValue("(404)367-2492");
//        shelterRef.child("4");
//        shelterRef.child("4").child("name").setValue("Atlanta's Children Center");
//        shelterRef.child("4").child("roomType").setValue("single");
//        shelterRef.child("4").child("capacity").setValue(40);
//        shelterRef.child("4").child("restrictions").setValue("Families w/ Children under 5");
//        shelterRef.child("4").child("longitude").setValue(-84.384433);
//        shelterRef.child("4").child("latitude").setValue(33.770949);
//        shelterRef.child("4").child("address").setValue("607 Peachtree Street NE Atlanta, GA 30308");
//        shelterRef.child("4").child("special").setValue("Children's Programs,Early Childhood Education");
//        shelterRef.child("4").child("phone").setValue("(404)892-3713");
//        shelterRef.child("5.0");
//        shelterRef.child("5.0").child("name").setValue("Eden Village - Families");
//        shelterRef.child("5.0").child("roomType").setValue("family");
//        shelterRef.child("5.0").child("capacity").setValue(32);
//        shelterRef.child("5.0").child("restrictions").setValue("Women & Children");
//        shelterRef.child("5.0").child("longitude").setValue(-84.43023);
//        shelterRef.child("5.0").child("latitude").setValue(33.762316);
//        shelterRef.child("5.0").child("address").setValue("1300 Joseph E. Boone Blvd NW, Atlanta, GA 30314");
//        shelterRef.child("5.0").child("special").setValue("General Recovery Services");
//        shelterRef.child("5.0").child("phone").setValue("(404)874-2241");
//        shelterRef.child("5.1");
//        shelterRef.child("5.1").child("name").setValue("Eden Village - Singles");
//        shelterRef.child("5.1").child("roomType").setValue("single");
//        shelterRef.child("5.1").child("capacity").setValue(80);
//        shelterRef.child("5.1").child("restrictions").setValue("Women,Children");
//        shelterRef.child("5.1").child("longitude").setValue(-84.43023);
//        shelterRef.child("5.1").child("latitude").setValue(33.762316);
//        shelterRef.child("5.1").child("address").setValue("1300 Joseph E. Boone Blvd NW, Atlanta, GA 30314");
//        shelterRef.child("5.1").child("special").setValue("General Recovery Services");
//        shelterRef.child("5.1").child("phone").setValue("(404)874-2241");
//        shelterRef.child("6");
//        shelterRef.child("6").child("name").setValue("Our House");
//        shelterRef.child("6").child("roomType").setValue("family");
//        shelterRef.child("6").child("capacity").setValue(76);
//        shelterRef.child("6").child("restrictions").setValue("Families w/ Newborns");
//        shelterRef.child("6").child("longitude").setValue(-84.371706);
//        shelterRef.child("6").child("latitude").setValue(33.759138);
//        shelterRef.child("6").child("address").setValue("173 Boulevard Northeast, Atlanta, GA 30312");
//        shelterRef.child("6").child("special").setValue("Families w/ Newborns,Pre-K Education");
//        shelterRef.child("6").child("phone").setValue("(404)522-6056");
//        shelterRef.child("7");
//        shelterRef.child("7").child("name").setValue("Covenant House Georgia");
//        shelterRef.child("7").child("roomType").setValue("single");
//        shelterRef.child("7").child("capacity").setValue(80);
//        shelterRef.child("7").child("restrictions").setValue("Children,Young Adults");
//        shelterRef.child("7").child("longitude").setValue(-84.437988);
//        shelterRef.child("7").child("latitude").setValue(33.78823);
//        shelterRef.child("7").child("address").setValue("1559 Johnson Road NW, Atlanta, GA 30318");
//        shelterRef.child("7").child("special").setValue("Crisis Services,Career Preparation");
//        shelterRef.child("7").child("phone").setValue("(404)937-6957");
//        shelterRef.child("8");
//        shelterRef.child("8").child("name").setValue("Nicholas House");
//        shelterRef.child("8").child("roomType").setValue("n/a");
//        shelterRef.child("8").child("capacity").setValue(0);
//        shelterRef.child("8").child("restrictions").setValue("Families");
//        shelterRef.child("8").child("longitude").setValue(-84.367953);
//        shelterRef.child("8").child("latitude").setValue(33.731823);
//        shelterRef.child("8").child("address").setValue("830 Boulevard SE, Atlanta, GA 30312");
//        shelterRef.child("8").child("special").setValue("Family Services");
//        shelterRef.child("8").child("phone").setValue("(404)9622-0793");
//        shelterRef.child("9");
//        shelterRef.child("9").child("name").setValue("Hope Atlanta");
//        shelterRef.child("9").child("roomType").setValue("apartment");
//        shelterRef.child("9").child("capacity").setValue(22);
//        shelterRef.child("9").child("restrictions").setValue("Anyone");
//        shelterRef.child("9").child("longitude").setValue(-84.390429);
//        shelterRef.child("9").child("latitude").setValue(33.753594);
//        shelterRef.child("9").child("address").setValue("34 Peachtree Street NW, Suite 700, Atlanta, GA 30303");
//        shelterRef.child("9").child("special").setValue("Emergency Shelter");
//        shelterRef.child("9").child("phone").setValue("(404)817-7070");
//        shelterRef.child("10");
//        shelterRef.child("10").child("name").setValue("Gateway Center");
//        shelterRef.child("10").child("roomType").setValue("single");
//        shelterRef.child("10").child("capacity").setValue(330);
//        shelterRef.child("10").child("restrictions").setValue("Men");
//        shelterRef.child("10").child("longitude").setValue(-84.394529);
//        shelterRef.child("10").child("latitude").setValue(33.747618);
//        shelterRef.child("10").child("address").setValue("275 Pryor St. SW, Atlanta, GA 30303");
//        shelterRef.child("10").child("special").setValue("Shelter and recovery services");
//        shelterRef.child("10").child("phone").setValue("(404)215-6600");
//        shelterRef.child("11");
//        shelterRef.child("11").child("name").setValue("Young Adult Guidance Center");
//        shelterRef.child("11").child("roomType").setValue("single");
//        shelterRef.child("11").child("capacity").setValue(12);
//        shelterRef.child("11").child("restrictions").setValue("Young Adults");
//        shelterRef.child("11").child("longitude").setValue(-84.470567);
//        shelterRef.child("11").child("latitude").setValue(33.789157);
//        shelterRef.child("11").child("address").setValue("1230 Hightower Road NW Atlanta, GA 30318");
//        shelterRef.child("11").child("special").setValue("Emergency,Independent living,Restoration");
//        shelterRef.child("11").child("phone").setValue("(404)792-7616");
//        shelterRef.child("12");
//        shelterRef.child("12").child("name").setValue("Homes of Light");
//        shelterRef.child("12").child("roomType").setValue("n/a");
//        shelterRef.child("12").child("capacity").setValue(0);
//        shelterRef.child("12").child("restrictions").setValue("Veterans");
//        shelterRef.child("12").child("longitude").setValue(-84.328691);
//        shelterRef.child("12").child("latitude").setValue(33.747641);
//        shelterRef.child("12").child("address").setValue("1800 Memorial Dr SE G3, Atlanta, GA 30317");
//        shelterRef.child("12").child("special").setValue("Veterans");
//        shelterRef.child("12").child("phone").setValue("(844)289-8382");
}

