package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

@ParseClassName("Interest")
public class Interest extends ParseObject {
    ////////////////////////////////////////////////////////////
    // Table Keys
    ////////////////////////////////////////////////////////////
    // Parent Label
    public static final String KEY_ARCHETYPE = "archetype";
    // Parent Emoji
    public static final String KEY_ARCHETYPE_EMOJI = "archetypeEmoji";
    // Child Name
    public static final String KEY_NAME = "name";
    // Child Emoji
    public static final String KEY_EMOJI = "emoji";


    ////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////

    /**
     * READ Interest name
     * @return
     */
    public String getName() { return getString(KEY_NAME); }
    /**
     * READ Interest archetype
     * @return
     */
    public String getArchetype() { return getString(KEY_ARCHETYPE); }
    /**
     * READ Interest archetype emoji
     * @return
     */
    public String getArchetypeEmoji() { return getString(KEY_ARCHETYPE_EMOJI); }
}
