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

    public ArrayList<ParseObject> getCohosts() {
        List<ParseObject> rv;
        rv = getList(KEY_COHOSTS);
        if (rv == null)
            return new ArrayList<ParseObject>();
        return (ArrayList<ParseObject>) rv;
    }

    public ArrayList<ParseObject> getParticipants() {
        List<ParseObject> rv;
        rv = getList(KEY_PARTICIPANTS);
        if (rv == null)
            return new ArrayList<ParseObject>();
        return (ArrayList<ParseObject>) rv;
    }

    ////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////
    private void setDescription(String newDescription) {
        put(KEY_DESCRIPTION, newDescription);
    }

    private void setActiveState(boolean newRoomState) {
        put(KEY_IS_ACTIVE, newRoomState);
    }

    private void setTitle(String newTitle) {
        put(KEY_TITLE, newTitle);
    }

    private void setClubName(String newName) {
        put(KEY_CLUB_NAME, newName);
    }

    private void setParticipants(ArrayList<ParseObject> participants) {
        put(KEY_PARTICIPANTS, participants);
    }

    private void setCohosts(ArrayList<ParseObject> cohosts) {
        put(KEY_COHOSTS, cohosts);
    }

    private void setHost(ParseUser newParseUser) {
        put(KEY_HOST, newParseUser);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    // Update Methods: automatically calls saveInBackground on ParseUser to effect updates
    /////////////////////////////////////////////////////////////////////////////////////////////

    private boolean updateDescription(String newDescription) {
        put(KEY_DESCRIPTION, newDescription);
        saveInBackground();
        return true;
    }

    private boolean updateActiveState(boolean newRoomState) {
        put(KEY_IS_ACTIVE, newRoomState);
        saveInBackground();
        return true;
    }

    private boolean updateTitle(String newTitle) {
        put(KEY_TITLE, newTitle);
        saveInBackground();
        return true;
    }

    private boolean updateClubName(String newName) {
        put(KEY_CLUB_NAME, newName);
        saveInBackground();
        return true;
    }

    public boolean addCohost(ParseObject cohost) {
        ArrayList<ParseObject> cohosts = getCohosts();
        ArrayList<ParseObject> participants = getParticipants();
        if (!cohosts.contains(cohost) && participants.contains(cohost)){
            cohosts.add(cohost);
        }
        else
            return false;
        setCohosts(cohosts);
        saveInBackground();
        return true;
    }

    public boolean removeCohost(ParseObject cohost) {
        ArrayList<ParseObject> cohosts = getCohosts();
        ArrayList<ParseObject> participants = getParticipants();
        if (cohosts.contains(cohost) && participants.contains(cohost)){
            cohosts.remove(cohost);
        }
        else
            return false;
        setCohosts(cohosts);
        saveInBackground();
        return true;
    }

    public boolean addParticipant(ParseObject participant) {
        ArrayList<ParseObject> participants = getParticipants();
        if (!participants.contains(participant)){
            participants.add(participant);
        }
        else
            return false;
        setParticipants(participants);
        saveInBackground();
        return true;
    }

    public boolean removeParticipant(ParseObject participant) {
        ArrayList<ParseObject> participants = getParticipants();
        if (participants.contains(participant)){
            participants.remove(participant);
        }
        else
            return false;
        setParticipants(participants);
        saveInBackground();
        return true;
    }
}
