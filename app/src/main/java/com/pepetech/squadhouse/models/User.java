package com.pepetech.squadhouse.models;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for interacting with the ParseUser class using an adapter pattern to avoid issues associated with
 * subclassing the ParseUser class.
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

    ////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////
    private String firstName, lastName, biography, phoneNumber;
    private boolean isSeed;
    private ParseUser nominator;
    private List<String> following, followers;
    private ParseUser user;

    ////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////
    public User() {
        this(null, "", "", null, false, new ArrayList<>(), new ArrayList<>());
    }

    public User(ParseUser user) {
        this(user, "", "", null, false, new ArrayList<>(), new ArrayList<>());
    }

    public User(ParseUser user, String firstName, String lastName) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        nominator = null;
        isSeed = false;
        following = new ArrayList<>();
        followers = new ArrayList<>();
    }

    public User(ParseUser user, String firstName, String lastName, ParseUser nominator, boolean isSeed, List<String> following, List<String> followers) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nominator = nominator;
        this.isSeed = isSeed;
        this.followers = followers;
        this.following = following;
    }

    public void saveInBackground() {
        user.put(KEY_FIRST_NAME, firstName);
        user.put(KEY_LAST_NAME, lastName);
//        user.put(KEY_NOMINATOR, nominator);
        user.put(KEY_IS_SEED, isSeed);
        user.put(KEY_FOLLOWING, following);
        user.put(KEY_FOLLOWERS, followers);
        user.saveInBackground();
    }

    ////////////////////////////////////////////////////////////
    // Getter Methods
    ////////////////////////////////////////////////////////////
    public ParseUser getParseUser() {
        return user;
    }

    public ParseFile getImage() {
        ParseFile rv = null;
        try {
            rv = user.fetchIfNeeded().getParseFile(KEY_IMAGE);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public String getFirstName() {
        String rv = null;
        try {
            rv = user.fetchIfNeeded().getString(KEY_FIRST_NAME);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public String getLastName() {
        String rv = null;
        try {
            rv = user.fetchIfNeeded().getString(KEY_LAST_NAME);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public String getBiography() {
        String rv = null;
        try {
            rv = user.fetchIfNeeded().getString(KEY_BIOGRAPHY);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }
//    public String getUserName() { return (String) user.get(KEY_USER_NAME);}

//    public boolean isSeed() { return (boolean) user.getBoolean(KEY_IS_SEED); }

    public String getPhoneNumber() {
        String rv = null;
        try {
            rv = user.fetchIfNeeded().getString(KEY_PHONE_NUMBER);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public boolean isSeed() {
        boolean rv = Boolean.parseBoolean(null);
        try {
            rv = user.fetchIfNeeded().getBoolean(KEY_IS_SEED);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public ParseUser getNominator() {
        ParseUser rv = null;
        try {
            rv = user.fetchIfNeeded().getParseUser(KEY_NOMINATOR);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

//    public ArrayList<ParseObject> getFollowing() {
//        ArrayList<ParseObject> rv;
//        rv = (ArrayList<ParseObject>) user.get(KEY_FOLLOWING);
//        if (rv == null)
//            return new ArrayList<ParseObject>();
//        return rv;
//    }

    public List<? extends Object> getFollowing() {
        List<Object> rv = null;
        try {
            rv = user.fetchIfNeeded().getList(KEY_FOLLOWING);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rv == null)
            return new ArrayList<ParseUser>();
        return rv;
    }

    public List<? extends Object> getClubs() {
        List<Object> rv = null;
        try {
            rv = user.fetchIfNeeded().getList(KEY_CLUBS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
    private void setFirstName(String firstname) {
        user.put(KEY_FIRST_NAME, firstname);
    }

    private void setLastName(String lastName) {
        user.put(KEY_LAST_NAME, lastName);
    }

    private void setBiography(String biography) {
        user.put(KEY_BIOGRAPHY, biography);
    }

    private void setImage(File image) {
        user.put(KEY_IMAGE, new ParseFile(image));
    }

    private void setPhoneNumber(String phoneNumber) {
        user.put(KEY_PHONE_NUMBER, phoneNumber);
    }

    private void setUserName(String username) { user.put(KEY_USER_NAME, username);}

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Update Methods: automatically calls saveInBackground on ParseUser to effect updates
    /////////////////////////////////////////////////////////////////////////////////////////////
    public boolean addFollowing(String userId) {
        List<String> followings = new ArrayList<String>();
        followings.add(userId);
        user.addAllUnique("following", followings);
        user.saveInBackground();
        return true;
    }

    public boolean removeFollowing(String userId) {
        ArrayList<ParseObject> followings = (ArrayList<ParseObject>) getFollowing();
        if (!followings.contains(userId)) {
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
    public boolean removeInterest(Interest interest) {
        ArrayList<ParseObject> interests = getInterests();
        if (!interests.contains(interest)) {
            return false;
        } else {
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
