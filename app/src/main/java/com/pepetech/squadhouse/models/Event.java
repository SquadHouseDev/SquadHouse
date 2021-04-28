package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Event")
public class Event extends ParseObject {
    public static final String KEY_HOST = "host";
    public static final String KEY_COHOSTS = "cohosts";
    public static final String KEY_TITLE = "title";
    public static final String KEY_HOST_CLUB = "hostClub";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_SCHEDULED_FOR = "scheduledFor";

    ////////////////////////////////////////////////////////////
    // Setter Methods
    ////////////////////////////////////////////////////////////

    public void setHost(ParseUser host) {
        put(KEY_HOST, host);
    }

    public void addCohost(ParseUser cohost) {
        addUnique(KEY_COHOSTS, cohost);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }

    public void setHostClub(Club hostClub){
        put(KEY_HOST_CLUB, hostClub);
    }

    public void setKeyDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public void setScheduledForDate(Date date){
        put(KEY_SCHEDULED_FOR, date);
    }

    ////////////////////////////////////////////////////////////
    // Getters Methods
    ////////////////////////////////////////////////////////////
    public ParseUser getHost(){
        try {
            ParseUser rv = fetchIfNeeded().getParseUser(KEY_HOST);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ParseUser> getCohosts(){
        try {
            List<ParseUser> rv = fetchIfNeeded().getList(KEY_COHOSTS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public String getTitle(){
        try {
            String rv = fetchIfNeeded().getString(KEY_TITLE);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDescription(){
        try {
            String rv = fetchIfNeeded().getString(KEY_DESCRIPTION);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Date getScheduledDate(){
        try {
            Date rv = fetchIfNeeded().getDate(KEY_SCHEDULED_FOR);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Club getHostClub(){
        try {
            Club rv = (Club) fetchIfNeeded().getParseObject(KEY_HOST_CLUB);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
