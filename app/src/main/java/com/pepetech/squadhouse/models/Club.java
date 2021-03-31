package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

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
    public String getName() { return getString(KEY_NAME); }

    public String getDescription() { return getString(KEY_DESCRIPTION); }

    public String getFollowers() { return getString(KEY_FOLLOWERS); }

    public ParseFile getImage() { return getParseFile(KEY_IMAGE); }

    public List<ParseObject> getMembers() { return getList(KEY_MEMBERS); }

    public List<ParseObject> getInterests() {
        List<ParseObject> rv;
        rv = getList(KEY_INTERESTS);
        if (rv == null)
            return new ArrayList<ParseObject>();
        return rv;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Setter Methods: need to call saveInBackground on ParseUser in order to effect changes
    ////////////////////////////////////////////////////////////////////////////////////////////
    private void setName(String newName) { put(KEY_NAME, newName); }

    private void setDescription(String newDescription) { put(KEY_DESCRIPTION, newDescription); }

    private void setImage(File newImage) { put(KEY_IMAGE, new ParseFile(newImage)); }

    private void setImage(ParseFile newImage) { put(KEY_IMAGE, newImage); }

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

    public boolean removeMember(ParseObject member){
        List<ParseObject> members = getMembers();
        if (!members.contains(member)) {
            return false;
        }
        else {
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
