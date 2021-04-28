package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

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
}
