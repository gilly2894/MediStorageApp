package com.fyp.shaun.medistorageapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.fyp.shaun.medistorageapp.models.User;
import com.fyp.shaun.medistorageapp.utils.NetworkUtils;
import com.fyp.shaun.medistorageapp.utils.SystemUtils;

import java.io.IOException;
import java.net.URL;

/**
 * This is the Login Activity
 */
public class LoginScreen extends AppCompatActivity {

    private static final String TAG = "shauns.tag";

    private String password = "";

    private TextView userNameTextView;
    private TextView passwordTextView;

    private ProgressBar mLoadingCircle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        userNameTextView = (TextView) findViewById(R.id.et_user_name_login_input);
        passwordTextView = (TextView) findViewById(R.id.et_password_login_input);
        mLoadingCircle = (ProgressBar) findViewById(R.id.pb_cirle_login_screen);
    }

    /*
     * onClick Listener for the login button
     */
    public void loginClicked(View view) {
        String userName = userNameTextView.getText().toString();
        password = passwordTextView.getText().toString();
        String credentials = userName + ":" + password;
        String encodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        if(userName != null && (!userName.equals("")))
            new RestLoginOperation().execute(userName, encodedCredentials);

    }

    /*
     * makes the call to the REST API to make sure credentials are correct.
     * Performs this on a new thread with AsyncTask to ease the work on the Main UI Thread.
     */
    public class RestLoginOperation extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingCircle.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String userName = params[0];
            String credentialsBase64 = params[1];

            URL userNameSearchUrl = NetworkUtils.buildUrlLogin(userName);

            String mySearchResults = null;
            try {
                mySearchResults = NetworkUtils.getUserFromHttpUrl(userNameSearchUrl, credentialsBase64);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return mySearchResults;
        }

        @Override
        protected void onPostExecute(String userJsonString) {
            mLoadingCircle.setVisibility(View.INVISIBLE);
            if (userJsonString != null && !userJsonString.equals("")) {

                Gson gson = new Gson();
                User user = gson.fromJson(userJsonString, User.class);
                if(user.getPassword().equals(password)) {
                    SystemUtils.setUser(user);
                    goToHomeScreen();
                }
                else
                    showLoginErrorMessage();
            } else {
                showLoginErrorMessage();
            }
        }
    }

    private void goToHomeScreen() {
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
    }

    public void showLoginErrorMessage()
    {
        passwordTextView.setText("");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginScreen.this);
        alertBuilder.setMessage("Login Failed!\n\nUsername or Password is incorrect");
        alertBuilder.setCancelable(true);

        alertBuilder.setPositiveButton(
                "Try Again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
