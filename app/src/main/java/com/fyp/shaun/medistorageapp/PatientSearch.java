package com.fyp.shaun.medistorageapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fyp.shaun.medistorageapp.models.PatientModel;
import com.fyp.shaun.medistorageapp.utils.JsonUtils;
import com.fyp.shaun.medistorageapp.utils.NetworkUtils;
import com.fyp.shaun.medistorageapp.utils.SystemUtils;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * This is the activity responsible for searching for patients
 */
public class PatientSearch extends AppCompatActivity {

    private static final String TAG = "shauns.tag";

    private EditText mPatientNameInput;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_search);

        mPatientNameInput = (EditText) findViewById(R.id.et_patient_name_input);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

    /*
     * Onclick listener for the Search By Name button
     */
    public void searchByNameBtnClick(View view) {
        String patientName = mPatientNameInput.getText().toString();

        if(patientName.isEmpty())
        {
            String errorMessage = "You have not entered a name!";
            String buttonMessage = "Try Again";
            showErrorAlert(errorMessage, buttonMessage);
        }
        else{
            hideKeyBoard();
            Toast.makeText(this, "You have entered : " + patientName, Toast.LENGTH_LONG).show();
            URL restSearchUrl = NetworkUtils.buildUrlForGET(patientName);
            new GetPatientsOperation().execute(restSearchUrl);
        }
    }

    /*
     * Onclick listener for the Search All Patients button
     */
    public void searchAllPatientsBtnClick(View view) {
        hideKeyBoard();
        URL restSearchUrl = NetworkUtils.buildUrlForGET("");
        new GetPatientsOperation().execute(restSearchUrl);
    }

    public class GetPatientsOperation extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String mySearchResults = null;
            try {
                mySearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return mySearchResults;
        }

        @Override
        protected void onPostExecute(String patientsJsonString) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (patientsJsonString != null && !patientsJsonString.equals("")) {
                List<PatientModel> listOfPatients = JsonUtils.parseJSONAsModels(patientsJsonString);
                displayListOfPatients(listOfPatients);
            } else {
                String errorMessage = "Invalid Search";
                String buttonMessage = "Try Again";
                showErrorAlert(errorMessage, buttonMessage);
            }
        }
    }

    /*
     * This method sets the adapter: "CustomerAdapter" to the  ListView: patientsListView
     */
    public void displayListOfPatients(List<PatientModel> listOfPatients)
    {
        ListAdapter patientsAdapter = new CustomAdapter(this, listOfPatients);
        ListView patientsListView = (ListView) findViewById(R.id.lv_search_patients);
        patientsListView.setAdapter(patientsAdapter);

        /*
         * onItemClickListener for each item in the list
         */
        patientsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        PatientModel p = (PatientModel) parent.getItemAtPosition(position);
                        URL patientUrlWithId = NetworkUtils.buildPatientUrlWithId(p.get_id());
                        new GetPatientDetailsOperation().execute(patientUrlWithId);
                    }
                }
        );
    }

    /*
     * makes a GET Request to the REST API to get the details of the patient click on in the list .
     * Performs this on a new thread with AsyncTask to ease the work on the Main UI Thread.
     */
    public class GetPatientDetailsOperation extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String mySearchResults = null;
            try {
                mySearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mySearchResults;
        }

        @Override
        protected void onPostExecute(String patientsJsonString) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (patientsJsonString != null && !patientsJsonString.equals("")) {
                List<PatientModel> listOfPatients = JsonUtils.parseJSONAsModels(patientsJsonString);
                goToPatientDetails(listOfPatients.get(0));
            } else {
                String errorMessage = "Couldn't Load Patients Page";
                String buttonMessage = "Try Again";
                showErrorAlert(errorMessage, buttonMessage);
            }
        }
    }

    /*
     * Switches activity to PatientDetailsPage and passes in the patientModel
     */
    private void goToPatientDetails(PatientModel patientModel) {
        Intent i = new Intent(this, PatientDetailsPage.class);
        i.putExtra("PATIENT", (Serializable) patientModel);
        startActivity(i);
    }

    /*
    * This method makes the virtual keyboard drop off the screen so the user doesn't have to hit the back key
    */
    public void hideKeyBoard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMM = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
            inputMM.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showErrorAlert(String errorMessage, String buttonMessage) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientSearch.this);
        alertBuilder.setMessage(errorMessage);
        alertBuilder.setCancelable(true);

        alertBuilder.setPositiveButton(
                buttonMessage,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
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
            Context context = PatientSearch.this;
            String message = "Logged Out Successfully";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            SystemUtils.setUser(null);
            Intent i = new Intent(this, LoginScreen.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
