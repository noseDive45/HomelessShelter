package com.example.jackson.homelessshelter.Controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
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
import android.text.Editable;
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
import com.example.jackson.homelessshelter.Model.DrawerLocker;
import com.example.jackson.homelessshelter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

/**
 * A login screen that offers login via email/password.
 */

public class LoginFragment extends Fragment implements LoaderCallbacks<Cursor> {


    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private DrawerLocker lockheed;

    // Get firebase auth & database instance
    private FirebaseAuth fAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lockheed.unlocked(false);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        // See if user already logged in
        initialize();
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
        View view = getView();
        mEmailView = view.findViewById(R.id.email);
        fAuth = FirebaseAuth.getInstance();
        mPasswordView = view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((id == EditorInfo.IME_ACTION_DONE) || (id == EditorInfo.IME_NULL)) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = view.findViewById(R.id.log_in);
        Button registration = view.findViewById(R.id.register);
        registration.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable editEmail = mEmailView.getText();
                String email = editEmail.toString();
                Editable editPassword = mPasswordView.getText();
                String password = editPassword.toString();
                if (fieldsPopulated(email, password)) {
                    attemptLogin();
                }
            }
        });
        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
    }


    private void nextPageSelection() {
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

        // Store values at the time of the login attempt.
        Editable editEmail = mEmailView.getText();
        String email = editEmail.toString();
        Editable editPassword = mPasswordView.getText();
        String password = editPassword.toString();

        showProgress(true);
        if (!isValidEmail(email)) {
            email += "@seekingshelter.com";
        }
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showProgress(false);
                            nextPageSelection();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                mEmailView.setError("Invalid Email Id");
                                mEmailView.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                mPasswordView.setError("Invalid Password");
                                mPasswordView.requestFocus();
                            } catch (Exception e) {
                                return;
                            }
                            showProgress(false);
                        }
                    }
                });
//        }
    }

    /**
     * Determines if both of the fields are filled
     * @param email String representing the email input
     * @param password String representing the password input
     * @return boolean whether the fields are both populated
     */
    public boolean fieldsPopulated(CharSequence email, CharSequence password) {
        // Reset errors.
//        mEmailView.setError(null);
//        mPasswordView.setError(null);

        boolean cancel = false;
//        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (password.length() == 0) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (email.length() == 0) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
            cancel = true;
        }
        return !cancel;
//        if (cancel) {
//            focusView.requestFocus();
//            return false;
//        } else {
//            return true;
//        }
    }

    // Checks if user is logging in with an email or a username

    private boolean isValidEmail(String email) {
        return (email.split("@").length > 1)
                && (email.split("\\.").length > 1)
                && (email.split("@")[1].length() >= 4)
                && (email.split("\\.")[1].length() == 3);
    }

    private void register() {
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
        Resources useless = getResources();
        int shortAnimTime = useless.getInteger(android.R.integer.config_shortAnimTime);

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
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


}

