package com.pepetech.squadhouse.models;

/**
 * Class for assisting in grouping of Interests by their archetype.
 * Utilized by the ExploreInterestAdapter and ExploreActivity classes.
 */
public class InterestGroup {
    public String archetype, archetypeEmoji, names;

    public InterestGroup() {
        archetype = "";
        archetypeEmoji = "";
        names = "";
    }
}
