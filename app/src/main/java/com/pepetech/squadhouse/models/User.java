package com.pepetech.squadhouse.models;

import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

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
 * TODO: read followers - a query needs to find all users whose following list contain the target user
 */
@Parcel
public class User {
    ////////////////////////////////////////////////////////////
    // ParseUser Column Names
    ////////////////////////////////////////////////////////////
    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_BIOGRAPHY = "biography";
    public static final String KEY_FOLLOWING = "following";
    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_CLUBS = "clubs";
    public static final String KEY_INTERESTS = "interests";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_NOMINATOR = "nominator";
    public static final String KEY_IS_SEED = "isSeed";
    public static final String KEY_USER_NAME = "username";


    private ParseUser user;
    public User() {}
    public User(ParseUser user) { this.user = user; }

    ////////////////////////////////////////////////////////////
    // Getter Methods
    ////////////////////////////////////////////////////////////
    public ParseUser getParseUser() { return user;}

    public ParseFile getImage() { return user.getParseFile(KEY_IMAGE); }

    public String getFirstName() { return (String) user.get(KEY_FIRST_NAME); }

    public String getLastName() { return (String) user.get(KEY_LAST_NAME); }

    public String getBiography() { return (String) user.get(KEY_BIOGRAPHY); }

    public String getPhoneNumber() { return (String) user.get(KEY_PHONE_NUMBER); }

    public String getUserName() { return (String) user.get(KEY_USER_NAME);}

    public boolean isSeed() { return (boolean) user.getBoolean(KEY_IS_SEED); }

    public ParseObject getNominator() { return (ParseObject) user.get(KEY_NOMINATOR); }



    public ArrayList<ParseObject> getFollowing() {
        ArrayList<ParseObject> rv;
        rv = (ArrayList<ParseObject>) user.get(KEY_FOLLOWING);
        if (rv == null)
            return new ArrayList<ParseObject>();
        return rv;
    }

    public ArrayList<ParseObject> getClubs() {
        ArrayList<ParseObject> rv;
        rv = (ArrayList<ParseObject>) user.get(KEY_CLUBS);
        if (rv == null)
            return new ArrayList<ParseObject>();
        return rv;
    }

    public ArrayList<ParseObject> getInterests() {
        ArrayList<ParseObject> rv;
        rv = (ArrayList<ParseObject>) user.get(KEY_INTERESTS);
        if (rv == null)
            return new ArrayList<ParseObject>();
        return rv;
    }

//    public ArrayList<ParseObject> getFollowers() {
//        ArrayList<ParseObject> rv;
//        rv = (ArrayList<ParseObject>) user.get(KEY_FOLLOWERS);
//        if (rv == null)
//            return new ArrayList<ParseObject>();
//        return (ArrayList<ParseObject>) user.get(KEY_FOLLOWERS);
//    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Setter Methods: need to call saveInBackground on ParseUser in order to effect changes
    /////////////////////////////////////////////////////////////////////////////////////////////
    private void setFirstName(String firstname) { user.put(KEY_FIRST_NAME, firstname); }

    private void setLastName(String lastName) { user.put(KEY_LAST_NAME, lastName); }

    private void setBiography(String biography) { user.put(KEY_BIOGRAPHY, biography); }

    private void setImage(File image) { user.put(KEY_IMAGE, new ParseFile(image)); }

    private void setPhoneNumber(String phoneNumber) { user.put(KEY_PHONE_NUMBER, phoneNumber); }

    private void setUserName(String username) { user.put(KEY_USER_NAME, username);}

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Update Methods: automatically calls saveInBackground on ParseUser to effect updates
    /////////////////////////////////////////////////////////////////////////////////////////////
    public boolean addFollowing(String userId){
        List<String> followings = new ArrayList<String>();
        followings.add(userId);
        user.addAllUnique("following", followings);
        user.saveInBackground();
        return true;
    }

    public boolean removeFollowing(String userId){
        ArrayList<ParseObject> followings = getFollowing();
        if (!followings.contains(userId)){
            return false;
        }
        ArrayList<String> toRemove = new ArrayList<>();
        toRemove.add(userId);
        user.removeAll(KEY_FOLLOWING, toRemove);
        user.saveInBackground();
        return true;
    }

    // TODO: testing needed
    public boolean addInterest(ParseObject interest) {
        ArrayList<ParseObject> interests = getInterests();
        if (interests.contains(interest)) {
            return false;
        }
        interests.add(interest);
        user.put("interests", interests);
        user.saveInBackground();
        return true;
    }

    // TODO: testing needed
    public boolean removeInterest(Interest interest){
        ArrayList<ParseObject> interests = getInterests();
        if (!interests.contains(interest)) {
            return false;
        }
        else {
            interests.remove(interest);
        }
        user.put("interests", interests);
        user.saveInBackground();
        return true;
    }
    // TODO: testing needed
    public void updateFirstName(String firstname) {
        setFirstName(firstname);
        user.saveInBackground();
    }
    // TODO: testing needed
    public void updateLastName(String lastName) {
        setLastName(lastName);
        user.saveInBackground();
    }
    // TODO: testing needed
    public void updateBiography(String biography) {
        setBiography(biography);
        user.saveInBackground();
    }
    // TODO: testing needed
    public void updateImage(File image) {
        setImage(image);
        user.saveInBackground();
    }
    // TODO: testing needed
    public void updatePhoneNumber(String phoneNumber) {
        setPhoneNumber(phoneNumber);
        user.saveInBackground();
    }
    public void updateUserName(String username)
    {
        setUserName(username);
        user.saveInBackground();
    }

}
