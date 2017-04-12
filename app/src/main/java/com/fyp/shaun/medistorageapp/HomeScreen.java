package com.fyp.shaun.medistorageapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.shaun.medistorageapp.utils.SystemUtils;

/**
 * This is the Home Screen it has two options: Search Patients, and Add Patient
 */
public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        if(SystemUtils.getUser() == null)
            return;

        String username = SystemUtils.getUser().getFullname();
        final TextView welcomeText = (TextView) findViewById(R.id.welcomeMessage);
        welcomeText.setText("Welcome " + username);
    }

    /*
     * onclick Listener for search patients button. This then starts the REST activity
     */
    public void searchPatientsBtnClicked(View view) {
        Intent i = new Intent(this, PatientSearch.class);
        startActivity(i);
    }

    /*
     * onclick Listener for search patients button. This then starts the REST activity
     */
    public void addPatientBtnClicked(View view) {
        Intent i = new Intent(this, AddPatient.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem = item.getItemId();
        if(selectedMenuItem == R.id.action_logout)
        {
            Context context = HomeScreen.this;
            String message = "Logged Out Successfully";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            SystemUtils.setUser(null);
            Intent i = new Intent(this, LoginScreen.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
