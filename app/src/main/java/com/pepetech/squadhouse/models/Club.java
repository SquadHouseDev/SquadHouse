package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@ParseClassName("Club")
public class Club extends ParseObject {
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FOLLOWERS = "followers";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_MEMBERS = "members";
    public static final String KEY_INTERESTS = "interests";
    public static final String KEY_MEMBERS_VISIBLE = "membersVisible";
    public static final String KEY_IS_FOLLOWABLE = "isFollowable";
    public static final String KEY_MEMBERS_CAN_CREATE_ROOMS = "membersCanCreateRooms";

    ////////////////////////////////////////////////////////////
    // Getter Methods
    ////////////////////////////////////////////////////////////
    public String getName() {
        try {
            String rv = fetchIfNeeded().getString(KEY_NAME);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getString(KEY_NAME);
    }

    public boolean isMembersVisible() {
        try {
            boolean rv = fetchIfNeeded().getBoolean(KEY_MEMBERS_VISIBLE);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isFollowable() {
        try {
            boolean rv = fetchIfNeeded().getBoolean(KEY_IS_FOLLOWABLE);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getMembersCanCreateRooms() {
        try {
            boolean rv = fetchIfNeeded().getBoolean(KEY_MEMBERS_CAN_CREATE_ROOMS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getDescription() {
        try {
            String rv = fetchIfNeeded().getString(KEY_DESCRIPTION);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getString(KEY_DESCRIPTION);
    }

    public ParseFile getImage() {
        ParseFile rv = null;
        try {
            rv = fetchIfNeeded().getParseFile(KEY_IMAGE);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public List<ParseUser> getFollowers() {
        List<ParseUser> rv = null;
        try {
            rv = fetchIfNeeded().getList(KEY_FOLLOWERS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rv == null)
            return new ArrayList<ParseUser>();
        return rv;
    }

    public List<ParseUser> getMembers() {
        List<ParseUser> rv = null;
        try {
            rv = fetchIfNeeded().getList(KEY_MEMBERS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rv == null)
            return new ArrayList<ParseUser>();
        return rv;
    }

    public List<? extends ParseObject> getInterests() {
        List<Interest> rv = null;
        try {
            rv = fetchIfNeeded().getList(KEY_INTERESTS);
            return rv;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (rv == null)
            return new ArrayList<Interest>();
        return rv;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Setter Methods: need to call saveInBackground on ParseUser in order to effect changes
    ////////////////////////////////////////////////////////////////////////////////////////////
    private void setName(String newName) {
        put(KEY_NAME, newName);
    }

    private void setDescription(String newDescription) {
        put(KEY_DESCRIPTION, newDescription);
    }

    private void setImage(File newImage) {
        put(KEY_IMAGE, new ParseFile(newImage));
    }

    private void setImage(ParseFile newImage) {
        put(KEY_IMAGE, newImage);
    }

    private void setMembersVisible(boolean isVisible) {
        put(KEY_MEMBERS_VISIBLE, isVisible);
    }

    private void setFollowable(boolean isVisible) {
        put(KEY_IS_FOLLOWABLE, isVisible);

    }

    private void setMembersCanCreateRooms(boolean canCreate) {
        put(KEY_MEMBERS_CAN_CREATE_ROOMS, canCreate);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // Update Methods: automatically calls saveInBackground on ParseUser to effect updates
    ////////////////////////////////////////////////////////////////////////////////////////////
    public boolean addInterest(Interest interest) {
        addUnique(KEY_INTERESTS, interest);
        saveInBackground();
        return true;
    }

    /**
     * Add the objectId of the follower to the club follower list
     *
     * @param follower
     * @return
     */
    public boolean addFollower(ParseUser follower) {
        addUnique(KEY_FOLLOWERS, follower);
        saveInBackground();
        return true;
    }

    /**
     * Add the objectId of the follower to the club follower list
     *
     * @param follower
     * @return
     */
    public boolean removeFollower(ParseUser follower) {
        List<ParseUser> followers = getFollowers();
        ArrayList<ParseUser> toRemove = new ArrayList<>();
        toRemove.add(follower);
        removeAll(KEY_FOLLOWERS, toRemove);
        saveInBackground();
        return true;
    }

    public boolean addMember(ParseUser member) {
        addUnique(KEY_FOLLOWERS, member);
        saveInBackground();
        return true;
    }

    public boolean removeMember(ParseUser member) {
        List<ParseUser> members = getFollowers();
        if (!members.contains(member)) {
            return false;
        }
        ArrayList<ParseUser> toRemove = new ArrayList<>();
        toRemove.add(member);
        removeAll(KEY_MEMBERS, toRemove);
        saveInBackground();
        return true;
    }

    public boolean updateImage(File image) {
        setImage(image);
        saveInBackground();
        return true;
    }

    public boolean updateImage(ParseFile image) {
        setImage(image);
        saveInBackground();
        return true;
    }

    public boolean updateDescription(String description) {
        setDescription(description);
        saveInBackground();
        return true;
    }

    public boolean updateMembersCanCreateRooms(boolean canCreate) {
        setMembersCanCreateRooms(canCreate);
        saveInBackground();
        return true;
    }

    public boolean updateFollowable(boolean isFollowable) {
        setFollowable(isFollowable);
        saveInBackground();
        return true;
    }

    public boolean updateMembersVisible(boolean isVisible) {
        setMembersCanCreateRooms(isVisible);
        saveInBackground();
        return true;
    }

    public boolean updateName(String name) {
        setName(name);
        saveInBackground();
        return true;
    }
}
