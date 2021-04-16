package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

@ParseClassName("Follow")
public class Follow extends ParseObject {
    public static final String KEY_TO = "to";
    public static final String KEY_FROM = "from";

    ////////////////////////////////////////////////////////////
    // Getter Methods
    ////////////////////////////////////////////////////////////

    public ParseObject getFollowTo() {
        ParseObject rv = null;
        try {
            rv = fetchIfNeeded().getParseObject(KEY_TO);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public ParseObject getFollowFrom() {
        ParseObject rv = null;
        try {
            rv = fetchIfNeeded().getParseObject(KEY_FROM);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }
}
