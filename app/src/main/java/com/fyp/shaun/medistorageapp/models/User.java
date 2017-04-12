package com.fyp.shaun.medistorageapp.models;

/**
 * This is a UserPOJO
 */
public class User {

    private static User signedInUser = null;

    private String _id;
    private String fullname;
    private String userName;
    private String password;
    private String title;

    private User() {  }

    // Singleton
    public static User getInstance()
    {
        if(signedInUser == null)
        {
            signedInUser = new User();
        }

        return signedInUser;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
