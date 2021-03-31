package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("Interest")
public class Interest extends ParseObject {

    public static final String KEY_ARCHETYPE = "archetype";
    public static final String KEY_NAME = "name";

    ////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////
    public String getName() { return getString(KEY_NAME); }

    public String getArchetype() {
        return getString(KEY_ARCHETYPE);
    }
}
