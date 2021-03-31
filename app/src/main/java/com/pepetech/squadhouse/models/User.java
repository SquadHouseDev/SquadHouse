package com.pepetech.squadhouse.models;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Extension of the ParseUser class
 *
 * Implementation follows a delegate pattern to avoid issues associated with
 * subclassing the ParseUser class.
 *
 * Note that when developing and testing the backend, a user logged in needs to be
 * signed out and signed in to query the newly created tables. Accessing newly added
 * column fields without doing so will result in null objects despite proper querying.
 *
 * Reference: https://guides.codepath.com/android/Troubleshooting-Common-Issues-with-Parse#extending-parseuser
 *
 * TODO: README documentation in network
 * TODO: delete/add user from following
 * TODO: read followers - a query needs to find all users whose following list contain the target user
 * TODO: fix get nominator, not broken
 */
public class User {
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_BIOGRAPHY = "biography";
    public static final String KEY_FOLLOWING = "following";
    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_NOMINATOR = "nominator";

    private ParseUser user;

    public void User(ParseUser user) { this.user = user; }

    ////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////
    public ParseFile getImage() { return user.getParseFile(KEY_IMAGE); }

    public String getFirstName() { return (String) user.get(KEY_FIRST_NAME); }

    public String getLastName() { return (String) user.get(KEY_LAST_NAME); }

    public String getBiography() { return (String) user.get(KEY_BIOGRAPHY); }

    public String getPhoneNumber() { return (String) user.get(KEY_PHONE_NUMBER); }

    // TODO: currently broken
    public ParseObject getNominator() { return (ParseObject) user.get(KEY_NOMINATOR); }

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

    //////////////////////////////////////////////////////////////////////////////////
    // Setters: need to call saveInBackground on ParseUser in order to effect changes
    /////////////////////////////////////////////////////////////////////////////////
    public void setFirstName(String firstname) { user.put(KEY_FIRST_NAME, firstname); }

    public void setLastName(String lastName) { user.put(KEY_LAST_NAME, lastName); }

    public void setBiography(String biography) { user.put(KEY_BIOGRAPHY, biography); }

    public void setImage(File image) { user.put(KEY_IMAGE, new ParseFile(image)); }

    public void setPhoneNumber(String phoneNumber) { user.put(KEY_PHONE_NUMBER, phoneNumber); }

    /////////////////////////////////////////////////////////////////////////////////
    // Update: automatically calls saveInBackground on ParseUser to effect updates
    /////////////////////////////////////////////////////////////////////////////////
    public boolean addFollowing(ParseObject user){
        ArrayList<ParseObject> following = getFollowing();
        if (following.contains(user)){
            return false;
        }
        following.add(user);
        user.put("following", following);
        user.saveInBackground();
        return true;
    }
//    public void setFollowing() {
//        return KEY_FOLLOWING;
//    }

//    public void setFollowers() {
//        return KEY_FOLLOWERS;
//    }


}
