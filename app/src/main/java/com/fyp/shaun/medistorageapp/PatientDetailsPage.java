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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.shaun.medistorageapp.models.Address;
import com.fyp.shaun.medistorageapp.models.PatientModel;
import com.fyp.shaun.medistorageapp.utils.JsonUtils;
import com.fyp.shaun.medistorageapp.utils.NetworkUtils;
import com.fyp.shaun.medistorageapp.utils.SystemUtils;

import java.net.URL;

/**
 * This class is responsible for Displaying the patients details, updating patients, and deleting patients.
 */
public class PatientDetailsPage extends AppCompatActivity {

    private PatientModel thePatient;

    // Text Views Member Variables
    private TextView mName;
    private TextView mPPSN;
    private TextView mCondition;
    private TextView mAddressLine1;
    private TextView mAddressLine2;
    private TextView mCity;
    private TextView mCounty;
    private TextView mCountry;
    private TextView mPostCode;

    // Edit Text Member Variables
    private EditText mEditName;
    private EditText mEditPPSN;
    private EditText mEditCondition;

    private EditText mEditAddressLine1;
    private EditText mEditAddressLine2;
    private EditText mEditCity;
    private EditText mEditCounty;
    private EditText mEditCountry;
    private EditText mEditPostCode;

    // Confirm Button Member Variables
    private Button mNameConfirmBtn;
    private Button mPPSNConfirmBtn;
    private Button mConditionConfirmBtn;

    private Button mAddressLine1ConfirmBtn;
    private Button mAddressLine2ConfirmBtn;
    private Button mCityConfirmBtn;
    private Button mCountyConfirmBtn;
    private Button mCountryConfirmBtn;
    private Button mPostCodeConfirmBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_details);
        thePatient = new PatientModel();

        //Initializing textview member variables
        mName = (TextView) findViewById(R.id.tv_patient_details_name);
        mPPSN = (TextView) findViewById(R.id.tv_patient_details_ppsn);
        mCondition = (TextView) findViewById(R.id.tv_patient_details_condition);
        mAddressLine1 = (TextView) findViewById(R.id.tv_details_address_line1);
        mAddressLine2 = (TextView) findViewById(R.id.tv_details_address_line2);
        mCity = (TextView) findViewById(R.id.tv_patient_details_city);
        mCounty = (TextView) findViewById(R.id.tv_patient_details_county);
        mCountry = (TextView) findViewById(R.id.tv_patient_details_country);
        mPostCode = (TextView) findViewById(R.id.tv_patient_details_postcode);


        //Inivializing edittext member variables
        mEditName = (EditText) findViewById(R.id.hidden_et_name);
        mEditPPSN = (EditText) findViewById(R.id.hidden_et_ppsn);
        mEditCondition = (EditText) findViewById(R.id.hidden_et_condition);
        mEditAddressLine1 = (EditText) findViewById(R.id.hidden_et_address_line1);
        mEditAddressLine2 = (EditText) findViewById(R.id.hidden_et_address_line2);
        mEditCity = (EditText) findViewById(R.id.hidden_et_city);
        mEditCounty = (EditText) findViewById(R.id.hidden_et_county);
        mEditCountry = (EditText) findViewById(R.id.hidden_et_country);
        mEditPostCode = (EditText) findViewById(R.id.hidden_et_postcode);

        //Inivializing confirmation button member variables
        mNameConfirmBtn = (Button) findViewById(R.id.confirm_name);
        mPPSNConfirmBtn = (Button) findViewById(R.id.confirm_ppsn);
        mConditionConfirmBtn = (Button) findViewById(R.id.confirm_condition);
        mAddressLine1ConfirmBtn = (Button) findViewById(R.id.confirm_address_line1);
        mAddressLine2ConfirmBtn = (Button) findViewById(R.id.confirm_address_line2);
        mCityConfirmBtn = (Button) findViewById(R.id.confirm_city);
        mCountyConfirmBtn = (Button) findViewById(R.id.confirm_county);
        mCountryConfirmBtn = (Button) findViewById(R.id.confirm_country);
        mPostCodeConfirmBtn = (Button) findViewById(R.id.confirm_postcode);

        // Getting the PatientModel Object that was passed in the intent
        Intent intent = getIntent();
        thePatient = (PatientModel) intent.getSerializableExtra("PATIENT");

        mName.append(thePatient.getName());
        mPPSN.append(thePatient.get_id());
        mCondition.append(thePatient.getIllness());
        mAddressLine1.append(thePatient.getAddress().getAddressLine1());
        mAddressLine2.append(thePatient.getAddress().getAddressLine2());
        mCity.append(thePatient.getAddress().getCity());
        mCounty.append(thePatient.getAddress().getCounty());
        mCountry.append(thePatient.getAddress().getCountry());
        mPostCode.append(thePatient.getAddress().getPostCode());

    }

    /*
     *  Sets the new information for the patient on an update
     */
    private void setThePatient() {
        String ppsn = mPPSN.getText().toString();
        thePatient.set_id(ppsn.substring(ppsn.indexOf(":")+1).trim());

        String name = mName.getText().toString();
        thePatient.setName(name.substring(name.indexOf(":")+1).trim());

        String illness = mCondition.getText().toString();
        thePatient.setIllness(illness.substring(illness.indexOf(":")+1).trim());

        Address a = new Address();
        String addressLine1 = mAddressLine1.getText().toString();
        a.setAddressLine1(addressLine1.substring(addressLine1.indexOf(":")+1).trim());

        String addressLine2 = mAddressLine2.getText().toString();
        a.setAddressLine2(addressLine2.substring(addressLine2.indexOf(":")+1).trim());

        String city = mCity.getText().toString();
        a.setCity(city.substring(city.indexOf(":")+1).trim());

        String county = mCounty.getText().toString();
        a.setCounty(county.substring(county.indexOf(":")+1).trim());

        String country = mCountry.getText().toString();
        a.setCountry(country.substring(country.indexOf(":")+1).trim());

        String postcode = mPostCode.getText().toString();
        a.setPostCode(postcode.substring(postcode.indexOf(":")+1).trim());

        thePatient.setAddress(a);
    }

    private void updatePatient() {
        setThePatient();
        String jsonPatient = JsonUtils.convertPatientToJSON(thePatient);

        String uri = SystemUtils.getURI_BASE_PATH() + SystemUtils.getURI_PATIENT_SEGMENT() + thePatient.get_id();
        new UpdatePatientOperation().execute(uri, jsonPatient);

    }

    /*
     * This is an innerclass that does this Update Operation on a different thread to ease the workload on the main UI thread
     * This is done by extending AsyncTask and Overriding the onPreExecute, doInBackground and onPostExecute methods.
     */
    public class UpdatePatientOperation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String searchUrl = params[0];
            String jsonString = params[1];
            String mySearchResults = null;

            mySearchResults = NetworkUtils.addAndUpdatePatientRequest(searchUrl, jsonString, 5000, "PUT");
            return mySearchResults;
        }

        @Override
        protected void onPostExecute(String patientsJsonString) {
            if (patientsJsonString != null && !patientsJsonString.equals("")) {
                Toast.makeText(PatientDetailsPage.this, "Patient: " + thePatient.getName() + " was successfully updated", Toast.LENGTH_LONG);
                backToMenu();
            } else {
                showErrorAlert("Unsuccessful", "Try Again");
            }
        }
    }

    private void deletePatient() {
        URL patientUrlWithId = NetworkUtils.buildPatientUrlWithId(thePatient.get_id());
        new deletePatientOperation().execute(patientUrlWithId);
        Intent i = new Intent(this, PatientSearch.class);
        startActivity(i);
    }

    /*
        This is an innerclass that does this Delete Operation on a different thread to ease the workload on the main UI thread
        This is done by extending AsyncTask and Overriding the onPreExecute, doInBackground and onPostExecute methods.
     */
    public class deletePatientOperation extends AsyncTask<URL, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String statusCode="";
            statusCode = NetworkUtils.patientDeleteRequest(searchUrl, 5000, "DELETE");
            return statusCode;
        }

        @Override
        protected void onPostExecute(String code) {
            if(code != null && !code.equals("")) {
                Toast.makeText(PatientDetailsPage.this, "Patient has been deleted", Toast.LENGTH_SHORT).show();
                backToMenu();
            } else {
                String errorMessage = "Couldn't Delete Patient";
                String buttonMessage = "Try Again";
                showErrorAlert(errorMessage, buttonMessage);
            }
        }
    }

    /*
        Switches back to the HomeScreen Activity
     */
    private void backToMenu()
    {
        Intent i = new Intent(this, HomeScreen.class);
        startActivity(i);
    }

    /*
     *   This method takes in the errorMessage and button text and displays Error Alert with this info
     */
    private void showErrorAlert(String errorMessage, String buttonMessage) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PatientDetailsPage.this);
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

    // Text View Clicks
    public void nameTV_Click(View view) {
        fieldClicked(mName, mEditName, mNameConfirmBtn);
    }

    public void ppsnTV_Click(View view) {
        fieldClicked(mPPSN, mEditPPSN, mPPSNConfirmBtn);
    }

    public void conditionTV_Click(View view) { fieldClicked(mCondition, mEditCondition, mConditionConfirmBtn); }

    public void addressline1TV_Click(View view) { fieldClicked(mAddressLine1, mEditAddressLine1, mAddressLine1ConfirmBtn); }

    public void addressline2TV_Click(View view) { fieldClicked(mAddressLine2, mEditAddressLine2, mAddressLine2ConfirmBtn); }

    public void cityTV_Click(View view) { fieldClicked(mCity, mEditCity, mCityConfirmBtn); }

    public void countyTV_Click(View view) {fieldClicked(mCounty, mEditCounty, mCountyConfirmBtn); }

    public void countryTV_Click(View view) { fieldClicked(mCountry, mEditCountry, mCountryConfirmBtn); }

    public void postcodeTV_Click(View view) { fieldClicked(mPostCode, mEditPostCode, mPostCodeConfirmBtn); }

    /*
        This Method is used to switch the TextView to an EditText to allow user to input new value
        Changes visibility of TextView to Invisible and sets EditText and the Button to VISIBLE.
    */
    public void fieldClicked(TextView tv, EditText et, Button btn)
    {
        tv.setVisibility(View.INVISIBLE);
        String[] a = tv.getText().toString().split(": ");
        if(a[1] != null)
            et.setText(a[1]);
        et.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
    }

    // Confirmation Button Clicks
    public void nameConfirmClick(View view) { editFieldConfirm(mName, mEditName, mNameConfirmBtn, "Name: "); }

    public void ppsnConfirmClick(View view) { editFieldConfirm(mPPSN, mEditPPSN, mPPSNConfirmBtn, "PPSN: "); }

    public void conditionConfirmClick(View view) { editFieldConfirm(mCondition, mEditCondition, mConditionConfirmBtn, "Condition: "); }

    public void addressLine1ConfirmClick(View view) { editFieldConfirm(mAddressLine1, mEditAddressLine1, mAddressLine1ConfirmBtn, "Address Line 1: "); }

    public void addressLine2ConfirmClick(View view) { editFieldConfirm(mAddressLine2, mEditAddressLine2, mAddressLine2ConfirmBtn, "Address Line 2: "); }

    public void cityConfirmClick(View view) { editFieldConfirm(mCity, mEditCity, mCityConfirmBtn, "City: "); }

    public void countyConfirmClick(View view) { editFieldConfirm(mCounty, mEditCounty, mCountyConfirmBtn, "County: "); }

    public void countryConfirmClick(View view) { editFieldConfirm(mCountry, mEditCountry, mCountryConfirmBtn, "Country: "); }

    public void postcodeConfirmClick(View view) { editFieldConfirm(mPostCode, mEditPostCode, mPostCodeConfirmBtn, "Postcode: "); }

    /*
     This Method is used to make the change back to the textview with the updated value.
     Changes visibility of EditText and the button back to INVISIBLE.
     */
    public void editFieldConfirm(TextView tv, EditText et, Button btn, String field)
    {
        tv.setText(field + et.getText().toString());
        et.setVisibility(View.INVISIBLE);
        et.setText("");
        btn.setVisibility(View.INVISIBLE);
        tv.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_UpdatePatient) {
            updatePatient();
            return true;
        }
        else if (itemThatWasClickedId == R.id.action_addNewPatient) {
            Intent i = new Intent(this, AddPatient.class);
            startActivity(i);
            return true;
        }
        else if(itemThatWasClickedId == R.id.action_deletePatient){
            deletePatient();
            return true;
        }
        else if(itemThatWasClickedId == R.id.action_logout){
            Context context = PatientDetailsPage.this;
            String message = "Logged Out Successfully";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, LoginScreen.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
