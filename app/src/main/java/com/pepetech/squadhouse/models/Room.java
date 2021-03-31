package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: delete/add participants
 * TODO: CRUD for the following -- cohosts, participants, startedAt, endedAt
 * TODO: decide on design choice of startedAt, endedAt with existing variables: createdAt, updatedAt
 */
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
    public static final String KEY_IS_ACTIVE = "isActive";

    ////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////
    public String getDescription() { return getString(KEY_DESCRIPTION); }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public String getClubName() {
        return getString(KEY_CLUB_NAME);
    }

    public ParseObject getHost() {
        return getParseObject(KEY_HOST);
    }

    public boolean isActive() {
        return getBoolean(KEY_IS_ACTIVE);
    }

    public ArrayList<Object> getCohosts(){ return (ArrayList<Object>) getList(KEY_COHOSTS);}

    public ArrayList<Object> getParticipants(){ return (ArrayList<Object>) getList(KEY_PARTICIPANTS);}

    ////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////
    public void setDescription(String newDescription) {
        put(KEY_DESCRIPTION, newDescription);
    }

    public void setActiveState(boolean newRoomState) {
        put(KEY_IS_ACTIVE, newRoomState);
    }

    public void setTitle(String newTitle) {
        put(KEY_TITLE, newTitle);
    }

    public void setClubName(String newName) {
        put(KEY_CLUB_NAME, newName);
    }

    public void setHost(ParseUser newParseUser) {
        put(KEY_HOST, newParseUser);
    }
}
