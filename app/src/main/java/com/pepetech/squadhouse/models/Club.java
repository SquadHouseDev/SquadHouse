package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ParseClassName("Club")
public class Club extends ParseObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_MEMBERS = "members";
    public static final String KEY_INTERESTS = "interests";

    ////////////////////////////////////////////////////////////
    // Getter Methods
    ////////////////////////////////////////////////////////////
    public String getName() {
        try {
            String rv = fetchIfNeeded().getString(KEY_NAME);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getString(KEY_NAME);
    }

    public String getDescription() {
        try {
            String rv = fetchIfNeeded().getString(KEY_DESCRIPTION);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getString(KEY_DESCRIPTION);
    }

    public List<? extends Object> getFollowers() {
        List<Object> rv = null;
        try {
            rv = fetchIfNeeded().getList(KEY_FOLLOWERS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rv == null)
            return new ArrayList<ParseUser>();
        return rv;
    }

    public ParseFile getImage() {
        ParseFile rv = null;
        try {
            rv = fetchIfNeeded().getParseFile(KEY_IMAGE);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public List<ParseObject> getMembers() {
        List<ParseObject> rv = null;
        try {
            rv = fetchIfNeeded().getList(KEY_MEMBERS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rv == null)
            return new ArrayList<ParseObject>();
        return rv;
    }

    public List<? extends ParseObject> getInterests() {
        List<Interest> rv = null;
        try {
            rv = fetchIfNeeded().getList(KEY_INTERESTS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rv == null)
            return new ArrayList<Interest>();
        return rv;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Setter Methods: need to call saveInBackground on ParseUser in order to effect changes
    ////////////////////////////////////////////////////////////////////////////////////////////
    private void setName(String newName) {
        put(KEY_NAME, newName);
    }

    private void setDescription(String newDescription) {
        put(KEY_DESCRIPTION, newDescription);
    }

    private void setImage(File newImage) {
        put(KEY_IMAGE, new ParseFile(newImage));
    }

    private void setImage(ParseFile newImage) {
        put(KEY_IMAGE, newImage);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Update Methods: automatically calls saveInBackground on ParseUser to effect updates
    ////////////////////////////////////////////////////////////////////////////////////////////
    public boolean addInterest(ParseObject interest) {
        ArrayList<ParseObject> interests = (ArrayList<ParseObject>) getInterests();
        if (interests.contains(interest)) {
            return false;
        }
        interests.add(interest);
        put("interests", interests);
        saveInBackground();
        return true;
    }

    /**
     * Add a ParseObject to the Array of ParseObject
     *
     * @param follower a ParseObject that defines the origin of the follow relation
     * @return
     */
    public boolean addFollower(ParseObject follower) {
        ArrayList<ParseObject> followers = (ArrayList<ParseObject>) getFollowers();
        if (followers.contains(follower)) {
            return false;
        }
        followers.add(follower);
        put("followers", followers);
        saveInBackground();
        return true;
    }

    /**
     * Remove a ParseObject to the Array of ParseObject
     *
     * @param follower a ParseObject that defines the origin of the follow relation
     * @return
     */
    public boolean removeFollower(ParseObject follower) {
        ArrayList<ParseObject> followers = (ArrayList<ParseObject>) getFollowers();
        if (followers.contains(follower)) {
            followers.remove(follower);
            put("followers", followers);
            saveInBackground();
            return true;
        } else
            return false;
    }

    public boolean addMember(ParseObject member) {
        List<ParseObject> members = getMembers();
        if (members.contains(member)) {
            return false;
        }
        members.add(member);
        put("interests", members);
        saveInBackground();
        return true;
    }

    public boolean removeMember(ParseObject member) {
        List<ParseObject> members = getMembers();
        if (!members.contains(member)) {
            return false;
        } else {
            members.remove(member);
        }
        put("interests", members);
        saveInBackground();
        return true;
    }

    public boolean updateImage(File image) {
        setImage(image);
        saveInBackground();
        return true;
    }

    public boolean updateImage(ParseFile image) {
        setImage(image);
        saveInBackground();
        return true;
    }

    public boolean updateDescription(String description) {
        setDescription(description);
        saveInBackground();
        return true;
    }

    public boolean updateName(String name) {
        setName(name);
        saveInBackground();
        return true;
    }
}
