package com.fyp.shaun.medistorageapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * This is a Utility class that deals with all the network connections with the RESTful API
 */
public class NetworkUtils {

    private static final String TAG = "shauns.tag";
    final static String PARAM_QUERY = "name";


    /*
     * This class returns the URL for GET Request with optional name parameter
     */
    public static URL buildUrlForGET(String nameSearchQuery) {
        String myUri = SystemUtils.getURI_BASE_PATH() + SystemUtils.getURI_PATIENT_SEGMENT();

        Uri builtUri = Uri.parse(myUri).buildUpon()
                .appendQueryParameter(PARAM_QUERY, nameSearchQuery)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    /*
     * This method creates and returns a URL with the patients ID appended onto it
     */
    public static URL buildPatientUrlWithId(String id) {
        String myUri = SystemUtils.getURI_BASE_PATH() + SystemUtils.getURI_PATIENT_SEGMENT() + id;
        Uri builtUri = Uri.parse(myUri).buildUpon().build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /*
     * This method returns a URL for the Login Request
     */
    public static URL buildUrlLogin(String userName) {
        String myUri = SystemUtils.getURI_BASE_PATH() + SystemUtils.getURI_USER_SEGMENT();
        if(!userName.equals(""))
        {
            myUri += userName;
        }
        Uri builtUri = Uri.parse(myUri).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    /*
     * This method connects to the RESTAPI to try and authenticate the user
     */
    public static String getUserFromHttpUrl(URL url, String credBase64) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Authorization", "Basic "+
                credBase64);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    /*
     * This method performs a GET request to the REST API at the specified URL
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String encodedCredentials = SystemUtils.getBase64Credentials();
        urlConnection.setRequestProperty("Authorization", "Basic "+
                encodedCredentials);
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /*
     * This method performs POST & PUT requests to the url passed in as a string.
     * The argument: String json is the body of the request
     * The type of call to the API is determined by the last argument : String method
     */
    public static String addAndUpdatePatientRequest(String url, String json, int timeout, String method) {
        HttpURLConnection urlConnection = null;
        try {

            URL u = new URL(url);
            urlConnection = (HttpURLConnection) u.openConnection();
            urlConnection.setRequestMethod(method);
            String encodedCredentials = SystemUtils.getBase64Credentials();
            urlConnection.setRequestProperty("Authorization", "Basic "+
                    encodedCredentials);

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);

            if (json != null) {
                urlConnection.setRequestProperty("Content-length", json.getBytes().length + "");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);

                OutputStream outputStream = urlConnection.getOutputStream();
                outputStream.write(json.getBytes("UTF-8"));
                outputStream.close();
            }

            urlConnection.connect();

            int status = urlConnection.getResponseCode();
            switch (status) {
                case 200:
                    return "Update Successful";
                case 201:
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    bufferedReader.close();
                    return sb.toString();
                case 409:
                    return "Duplicate PPSN!\nThere is someone in the system that has this PPSN.";
            }

        } catch (MalformedURLException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (IOException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (Exception ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    Log.e("HTTP Client", "Error in http connection" + ex.toString());
                }
            }
        }
        return null;
    }


    /*
     * This method performs DELETE Request to the specified URL.
     */
    public static String patientDeleteRequest(URL url, int timeout, String method) {
        HttpURLConnection urlConnection = null;
        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(method);
            String encodedCredentials = SystemUtils.getBase64Credentials();
            urlConnection.setRequestProperty("Authorization", "Basic "+
                    encodedCredentials);

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            urlConnection.setAllowUserInteraction(false);
            urlConnection.setConnectTimeout(timeout);
            urlConnection.setReadTimeout(timeout);

            urlConnection.connect();

            int status = urlConnection.getResponseCode();
            switch (status) {
                case 204:
                    Log.i(TAG, "deleteNetworkUtils --- case 204");
                    return status+"";
                case 404:
                    Log.i(TAG, "deleteNetworkUtils --- case 404");
                    return null;
            }

        } catch (MalformedURLException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (IOException ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } catch (Exception ex) {
            Log.e("HTTP Client", "Error in http connection" + ex.toString());
        } finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    Log.e("HTTP Client", "Error in http connection" + ex.toString());
                }
            }
        }
        return null;
    }
}
