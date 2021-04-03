package com.pepetech.squadhouse.models;


import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * This is a READ-only class for the table of Interest instances in the Parse Server.
 * Interest objects should not be modified unless there is need for changing labelling
 * of a User or a Club's Interests.
 */
@ParseClassName("Interest")
public class Interest extends ParseObject {
    ////////////////////////////////////////////////////////////
    // Table Keys
    ////////////////////////////////////////////////////////////
    // General Label
    public static final String KEY_ARCHETYPE = "archetype";
    // Emoji associated with General Label
    public static final String KEY_ARCHETYPE_EMOJI = "archetypeEmoji";
    // Specific Label
    public static final String KEY_NAME = "name";
    // Emoji associated with specific Label
    public static final String KEY_EMOJI = "emoji";
    ////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////
    /**
     * Accessor to the Interest name.
     *
     * @return the Interest name
     */
    public String getName() {
        try {
            String rv = fetchIfNeeded().getString(KEY_NAME);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getString(KEY_NAME);
    }
    /**
     * Accessor to the Interest archetype. The archetype denotes the category of the Interest instance.
     *
     * @return the Interest archetype
     */
    public String getArchetype() {
        try {
            String rv = fetchIfNeeded().getString(KEY_ARCHETYPE);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getString(KEY_ARCHETYPE);
    }
    /**
     * Accessor to the Interest archetypeEmoji. The archetypeEmoji is the visual representation of the Interest archetype.
     *
     * @return the Interest archetypeEmoji
     */
    public String getArchetypeEmoji() {
        try {
            String rv = fetchIfNeeded().getString(KEY_ARCHETYPE_EMOJI);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getString(KEY_ARCHETYPE_EMOJI);
    }

    /**
     * TODO
     * Accessor to the Interest emoji. The emoji is the visual representation of the Interest name.
     *
     * @return the Interest archetypeEmoji
     */
    private String getEmoji() {
        try {
            String rv = fetchIfNeeded().getString(KEY_EMOJI);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getString(KEY_EMOJI);
    }
    /**
     * String representation of the Interest object
     *
     * @return
     */
    public String toString() {
        return getArchetype() + "," + getArchetypeEmoji() + "," + getName();
    }
}
