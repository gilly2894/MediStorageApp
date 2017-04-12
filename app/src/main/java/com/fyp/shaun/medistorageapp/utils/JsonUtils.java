package com.fyp.shaun.medistorageapp.utils;

import com.fyp.shaun.medistorageapp.models.Address;
import com.fyp.shaun.medistorageapp.models.Links;
import com.fyp.shaun.medistorageapp.models.PatientModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a Utility class that deals with all the JSON parsing.
 */
public class JsonUtils {

    private static final String TAG = "shauns.tag";

    /*
     * This method returns a list of PatientModels parsed from the full JSON String returned from the RESTful API.
     */
    public static List<PatientModel> parseJSONAsModels(String jsonString)
    {
        List<PatientModel> listOfJsonObj = new ArrayList<PatientModel>();
        if(jsonString.equals("[]"))
            return null;
        if(jsonString.startsWith("["))
        {
            listOfJsonObj = parseJSONListAsModels(jsonString);
        }
        else
        {
            try {
                listOfJsonObj.add(parseJSONObjectAsModel(new JSONObject(jsonString)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listOfJsonObj;
    }

    /*
     * This method is used to split out the array of patients inside the JSON String. This is for a list of patients.
     * It passes each individual JSONObject to the parseJSONObjectAsModel method.
     * Returns the full list of PatientModels
     */
    public static List<PatientModel> parseJSONListAsModels(String jsonString)
    {
        List<PatientModel> listOfPatientModels = new ArrayList<PatientModel>();
        JSONObject jsonObject;
        try {
            JSONArray jsonArray = new JSONArray(jsonString);

            for(int n = 0; n < jsonArray.length(); n++)
            {
                jsonObject = jsonArray.getJSONObject(n);
                listOfPatientModels.add(parseJSONObjectAsModel(jsonObject));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfPatientModels;
    }


    /*
     * This is the method that actually parses the individual JSONObjects into PatientModel
     */
    public static PatientModel parseJSONObjectAsModel(JSONObject parentObj)
    {
        PatientModel patientModel = new PatientModel();
        try {

            patientModel.set_id(parentObj.getString("_id"));
            patientModel.setName(parentObj.getString("name"));
            patientModel.setIllness(parentObj.getString("illness"));

            // Address
            JSONObject addressObj = parentObj.getJSONObject("address");
            Address addressModel = new Address();
            addressModel.setAddressLine1(addressObj.getString("addressLine1"));
            addressModel.setAddressLine2(addressObj.getString("addressLine2"));
            addressModel.setCity(addressObj.getString("city"));
            addressModel.setCounty(addressObj.getString("county"));
            addressModel.setCountry(addressObj.getString("country"));
            addressModel.setPostCode(addressObj.getString("postCode"));

            patientModel.setAddress(addressModel);

            // Links
            Links linkModel = new Links();
            JSONArray linkObjectList = parentObj.getJSONArray("links");
            for(int n = 0; n < linkObjectList.length(); n++)
            {
                JSONObject linkObject = linkObjectList.getJSONObject(n);
                linkModel.setLink(linkObject.getString("link"));
                linkModel.setRel(linkObject.getString("rel"));
            }
            patientModel.setLinks(linkModel);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return patientModel;
    }

    /*
     * This method converts a PatientModel back into a JSON String
     */
    public static String convertPatientToJSON(PatientModel patient)
    {
        String jsonString = "{";
        jsonString += " \"_id\": \"" + patient.get_id() + "\",";
        jsonString += " \"name\": \"" + patient.getName() + "\",";
        jsonString += " \"address\": {";
        jsonString += " \"addressLine1\": \"" + patient.getAddress().getAddressLine1() + "\",";
        jsonString += " \"addressLine2\": \"" + patient.getAddress().getAddressLine2() + "\",";
        jsonString += " \"city\": \"" + patient.getAddress().getCity() + "\",";
        jsonString += " \"county\": \"" + patient.getAddress().getCounty() + "\",";
        jsonString += " \"country\": \"" + patient.getAddress().getCountry() + "\",";
        jsonString += " \"postCode\": \"" + patient.getAddress().getPostCode() + "\" },";
        jsonString += " \"illness\": \"" + patient.getIllness() + "\" }";

        return jsonString;
    }
}
