package com.fyp.shaun.medistorageapp.utils;


import android.util.Base64;

import com.fyp.shaun.medistorageapp.models.User;

/**
 * This is a Utility class that returns the URL of the API and the different Segments of the path
 */
public class SystemUtils {

    // This must be the Public IP address of the ec2 instance that the RESTful API is running on
    private final static String RESTfulAPI_Public_IP = "52.16.195.126";

    private final static String URI_BASE_PATH = ":8080/MediStorage/webapi/";
    private final static String URI_PATIENT_SEGMENT = "patients/";
    private final static String URI_USER_SEGMENT = "users/";

    private static User user = User.getInstance();


    public static String getURI_BASE_PATH() {
        return "http://" + RESTfulAPI_Public_IP + URI_BASE_PATH;
    }

    public static String getURI_PATIENT_SEGMENT() {
        return URI_PATIENT_SEGMENT;
    }

    public static String getURI_USER_SEGMENT() {
        return URI_USER_SEGMENT;
    }


    public static void setUser(User user) {
        SystemUtils.user = user;
    }

    public static User getUser() {
        return user;
    }

    public static String getBase64Credentials() {
        String credentials = user.getUserName() + ":" + user.getPassword();
        return Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
    }

}
