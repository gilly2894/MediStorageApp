package com.fyp.shaun.medistorageapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.shaun.medistorageapp.models.Address;
import com.fyp.shaun.medistorageapp.models.PatientModel;
import com.fyp.shaun.medistorageapp.utils.JsonUtils;
import com.fyp.shaun.medistorageapp.utils.NetworkUtils;
import com.fyp.shaun.medistorageapp.utils.SystemUtils;

import java.io.Serializable;

/**
 * This is the activity responsible for creating a new patient
 */
public class AddPatient extends AppCompatActivity {
    PatientModel patient;

    private TextView mName;
    private TextView mPPSN;
    private TextView mCondition;
    private TextView mAddressLine1;
    private TextView mAddressLine2;
    private TextView mCity;
    private TextView mCounty;
    private TextView mCountry;
    private TextView mPostCode;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient);

        mName = (TextView) findViewById(R.id.input_name);
        mPPSN = (TextView) findViewById(R.id.input_ppsn);
        mCondition = (TextView) findViewById(R.id.input_condition);
        mAddressLine1 = (TextView) findViewById(R.id.input_address_line1);
        mAddressLine2 = (TextView) findViewById(R.id.input_address_line2);
        mCity = (TextView) findViewById(R.id.input_city);
        mCounty = (TextView) findViewById(R.id.input_county);
        mCountry = (TextView) findViewById(R.id.input_country);
        mPostCode = (TextView) findViewById(R.id.input_postcode);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_add_patient_indicator);

    }

    /*
     * onClick Listener for Add Patient Button
     */
    public void createPatientBtnClick(View view) {
        hideKeyBoard();
        patient = new PatientModel();
        patient.setName(mName.getText().toString());
        patient.set_id(mPPSN.getText().toString());
        patient.setIllness(mCondition.getText().toString());

        Address address = new Address();
        address.setAddressLine1(mAddressLine1.getText().toString());
        address.setAddressLine2(mAddressLine2.getText().toString());
        address.setCity(mCity.getText().toString());
        address.setCounty(mCounty.getText().toString());
        address.setCountry(mCountry.getText().toString());
        address.setPostCode(mPostCode.getText().toString());

        patient.setAddress(address);

        String jsonPatient = JsonUtils.convertPatientToJSON(patient);

        String uri = SystemUtils.getURI_BASE_PATH() + SystemUtils.getURI_PATIENT_SEGMENT();
        new NewPatientPostOperation().execute(uri, jsonPatient);
    }

    /*
     * makes the POST Request to the REST API and passes the Patient in the body of the request in JSON format.
     * Performs this on a new thread with AsyncTask to ease the work on the Main UI Thread.
     */
    public class NewPatientPostOperation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String searchUrl = params[0];
            String jsonString = params[1];
            String mySearchResults = null;

            mySearchResults = NetworkUtils.addAndUpdatePatientRequest(searchUrl, jsonString, 5000, "POST");

            return mySearchResults;
        }

        @Override
        protected void onPostExecute(String patientsJsonString) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (patientsJsonString != null && !patientsJsonString.equals("")) {
                clearAllFields();
                goToPatientDetails();
            } else {
                clearAllFields();
                showErrorAlert("Unsuccessful", "Try Again");
            }
        }
    }

    /*
        Clears all the fields so user doesn't have to do it manually
     */
    private void clearAllFields() {
        mName.setText("");
        mPPSN.setText("");
        mCondition.setText("");
        mAddressLine1.setText("");
        mAddressLine2.setText("");
        mCity.setText("");
        mCounty.setText("");
        mCountry.setText("");
        mPostCode.setText("");
    }

    /*
     * This changes activity to the Patient Details activity.
     * It passes along the PatientModel to the new Activity to use to populate the fields
     */
    public void goToPatientDetails()
    {
        Toast.makeText(this, "Patient: " + patient.getName() + " has been created!", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, PatientDetailsPage.class);
        i.putExtra("PATIENT", (Serializable) patient);
        startActivity(i);
    }

    /*
     * This method makes the virtual keyboard drop off the screen so the user doesn't  have to hit the back key
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddPatient.this);
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
}
