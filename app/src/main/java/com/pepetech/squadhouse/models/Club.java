package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

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


    public String getName() {
        return getString(KEY_NAME);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public String getFollowers() {
        return getString(KEY_FOLLOWERS);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public List<Object> getMembers() {
        return getList(KEY_MEMBERS);

    }
    public List<ParseObject> getInterests() {
        List<ParseObject> rv;
        rv =  getList(KEY_INTERESTS);
        if (rv == null)
            return new ArrayList<ParseObject>();
        return rv;
    }
}
