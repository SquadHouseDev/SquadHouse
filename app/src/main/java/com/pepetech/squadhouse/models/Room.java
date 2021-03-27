package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Room")
public class Room extends ParseObject {

    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CLUB_NAME = "clubName";

    public static final String KEY_HOST = "host";
    public static final String KEY_COHOSTS = "cohosts";
    public static final String KEY_PARTICIPANTS = "participants";
    public static final String KEY_STARTED_AT = "startedAt";
    public static final String KEY_ENDED_AT = "endedAt";


    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String newDescription) {
        put(KEY_DESCRIPTION, newDescription);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String newTitle) {
        put(KEY_TITLE, newTitle);
    }

    public String getClubName() {
        return getString(KEY_CLUB_NAME);
    }

    public void setClubName(String newName) {
        put(KEY_CLUB_NAME, newName);
    }

    public ParseUser getHost() {
        return getParseUser(KEY_HOST);
    }

    public void setHost(ParseUser newParseUser) {
        put(KEY_HOST, newParseUser);
    }
}
