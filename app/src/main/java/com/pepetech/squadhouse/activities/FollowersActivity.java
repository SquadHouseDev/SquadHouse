package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Explore.adapters.ExploreUserAdapter;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class dedicated to viewing any User's list of followers.
 * Usage requires using Parcels to wrap a User instance to be passed
 * to this activity.
 */
public class FollowersActivity extends AppCompatActivity {
    public static final String TAG = "FollowersActivity";
    RecyclerView rvFollowers;
    ExploreUserAdapter followerAdapter;
    List<User> followers;
    User userSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("FOLLOWERS");
        setContentView(R.layout.activity_followers);
        followers = new ArrayList<>();
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        rvFollowers = findViewById(R.id.rvFollowers);
        // configure layout managers
        followerAdapter = new ExploreUserAdapter(this, followers);
//        , userSelected);
        userSelected = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        // TODO: accept the user that was passed from the previous activity
        rvFollowers.setAdapter(followerAdapter);
        rvFollowers.setLayoutManager(new LinearLayoutManager(this));
        queryFollowers(followers, userSelected, followerAdapter);

    }

    /**
     * Reusable method for querying for all ParseUsers that are followers
     * of the input target User
     *
     * @param targetUser User of interest when querying for followers
     * @param adapter    Adapter to be notified of data collection changes
     * @param collection list of Users used to populate the RecyclerView
     */
    void queryFollowers(List<User> collection, User targetUser, RecyclerView.Adapter adapter) {
        Log.i(TAG, "queryFollowers");
        Log.i(TAG, "Target: " + targetUser.getParseUser().getObjectId());
        collection.clear();
        ParseQuery<Follow> mainQuery = new ParseQuery<Follow>(Follow.class);
        // fina all Users following targetUser
        mainQuery.whereEqualTo(Follow.KEY_TO, targetUser.getParseUser());
//        mainQuery.whereNotEqualTo(Follow.KEY_FROM, targetUser.getParseUser());
        mainQuery.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> objects, ParseException e) {
                Log.i(TAG, String.valueOf(objects.size()) + " followings");
                if (e == null) {
                    // for each follower check if the follower is followed by the current user
                    for (Follow c : objects) {
                        // get the User following the target
                        User u = new User((ParseUser) c.getFollowFrom());
                        Log.i(TAG, "FROM: " + u.getFirstName() + " " + u.getLastName());
                        Log.i(TAG, "FROM: " + u.getParseUser().getObjectId());
                        // checking if the followers of the target Users are followed by the target user
                        // User ==> target && User <== target

                        // exclude the current user self
                        if (u.getParseUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            Log.i(TAG, "skipping");
                            continue;
                        }
                        Log.i(TAG, "checking if follow is bidirectional");
                        for (ParseObject user : targetUser.getFollowing()) {
                            if (u.getParseUser().getObjectId().equals(user.getObjectId())) {
                                Log.i(TAG, u.getFirstName() + " share bidirectional follow relation with " + targetUser.getFirstName());
                                u.isFollowed = true;
                            }
                        }
//                        }
                        collection.add(u);
                    }
                } else {
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}