package com.pepetech.squadhouse.models;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;

/**
 * Extension of the ParseUser class
 *
 * Implementation follows a delegate pattern to avoid issues associated with
 * subclassing the ParseUser class.
 *
 * Reference: https://guides.codepath.com/android/Troubleshooting-Common-Issues-with-Parse#extending-parseuser
 *
 * TODO: README documentation in network
 * TODO: delete/add user from following
 * TODO: read followers - a query needs to find all users whose following list contain the target user
 */
public class User {
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_BIOGRAPHY = "biography";
    public static final String KEY_FOLLOWING = "following";
    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";

    private ParseUser user;

    public void User(ParseUser user) {
        this.user = user;
    }

    ////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////
    public ParseFile getImage() {
        return user.getParseFile(KEY_IMAGE);
    }

    public String getFirstName() {
        return (String) user.get(KEY_FIRST_NAME);
    }

    public String getLastName() {
        return (String) user.get(KEY_LAST_NAME);
    }

    public String getBiography() {
        return (String) user.get(KEY_BIOGRAPHY);
    }

    public String getPhoneNumber() {
        return (String) user.get(KEY_PHONE_NUMBER);
    }

    public ArrayList<ParseObject> getFollowing() {
        ArrayList<ParseObject> rv;
        rv = (ArrayList<ParseObject>) user.get(KEY_FOLLOWING);
        if (rv == null)
            return new ArrayList<ParseObject>();
        return (ArrayList<ParseObject>) user.get(KEY_FOLLOWING);
    }

//    public ArrayList<ParseObject> getFollowers() {
//        ArrayList<ParseObject> rv;
//        rv = (ArrayList<ParseObject>) user.get(KEY_FOLLOWERS);
//        if (rv == null)
//            return new ArrayList<ParseObject>();
//        return (ArrayList<ParseObject>) user.get(KEY_FOLLOWERS);
//    }

    ////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////
    public void setFirstName(String firstname) {
        user.put(KEY_FIRST_NAME, firstname);
    }

    public void setLastName(String lastName) {
        user.put(KEY_LAST_NAME, lastName);
    }

    public void setBiography(String biography) {
        user.put(KEY_BIOGRAPHY, biography);
    }

    public void setImage(File image) {
        user.put(KEY_IMAGE, new ParseFile(image));
    }

//    public void setFollowing() {
//        return KEY_FOLLOWING;
//    }

//    public void setFollowers() {
//        return KEY_FOLLOWERS;
//    }

    public void setPhoneNumber(String phoneNumber) {
        user.put(KEY_PHONE_NUMBER, phoneNumber);
    }
}
