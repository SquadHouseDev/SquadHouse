package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.adapters.ExploreClubAdapter;
import com.pepetech.squadhouse.adapters.ExploreInterestAdapter;
import com.pepetech.squadhouse.adapters.ExploreUserAdapter;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.InterestGroup;
import com.pepetech.squadhouse.models.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This is the main Explore Activity class managing User exploration
 * of Clubs, Interests and Users. The user is able to toggle between
 * searching for Users or Clubs in which they are presented based on
 * their search configuration and keyword entry. Toggling the search
 * configuration button applies a switch between the ClubAdapter and
 * the UserAdapter as well as the method of querying for data
 * -- both used in populating the RecyclerView.
 * <p>
 * ExploreActivity
 * ==> ExploreClub
 * ==> ExploreUser
 * =================> clFollowers ==> FollowersActivity <== FollowerAdapter
 * =================> clFollowing
 * =================>
 */
public class ExploreActivity extends AppCompatActivity {
    public static final String TAG = "ExploreActivity";
    List<User> allUsers;
    //    List<Follow> allFollowings;
    LinkedHashMap<ParseObject, Boolean> allFollowings;
    List<Club> allClubs;
    TextView tvElementsLabel;
    RecyclerView rvElementsFound, rvInterests;
    EditText etSearch;
    Button btnSearch;
    Switch switchUserClub;
    ExploreUserAdapter exploreUserAdapter;
    ExploreClubAdapter exploreClubAdapter;
    ExploreInterestAdapter exploreInterestAdapter;
    List<ParseObject> following;
    List<Interest> allInterests;
    private List<InterestGroup> interestGroupList;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        tvElementsLabel = findViewById(R.id.tvElementsLabel);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        switchUserClub = findViewById(R.id.switchUserClub);
        rvElementsFound = findViewById(R.id.rvElementsFound);
        rvInterests = findViewById(R.id.rvInterests);

        // setup current User and the collection of Users
        currentUser = new User(ParseUser.getCurrentUser());
        following = new ArrayList<>();

        // disable auto focus keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ////////////////////////////////////////////////////////////
        // Setup recycler views
        ////////////////////////////////////////////////////////////
        interestGroupList = new ArrayList<>();
        allUsers = new ArrayList<>();
//        allFollowings = new LinkedHashMap<>();
        allClubs = new ArrayList<>();
        allInterests = new ArrayList<>();

        // 2 adapters to toggle between with the toggle switch
        exploreClubAdapter = new ExploreClubAdapter(this, allClubs, currentUser);
        exploreUserAdapter = new ExploreUserAdapter(this, allUsers, currentUser);

        // configure layout managers
        rvElementsFound.setLayoutManager(new LinearLayoutManager(this));
        rvInterests.setLayoutManager(new GridLayoutManager(this, 2));
        exploreInterestAdapter = new ExploreInterestAdapter(this, interestGroupList);
        rvInterests.setAdapter(exploreInterestAdapter);

        // query data for populating with adapters
        setupOnClickListeners();

        queryAndGroupInterestsByArchetype();
    }

    /**
     * Main setup function for any Views that need an OnClickListener:
     * <ol>
     *     <li>Search button configured with a toggle switch for Parse Querying and populating with User/Club in Recycler View</li>
     * </ol>
     */
    private void setupOnClickListeners() {
        // setup search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValidKeyword = false;
                Toast.makeText(v.getContext(), "Search button clicked!", Toast.LENGTH_SHORT).show();
                // qualify if there exists a keyword search entry
                if (etSearch.getText().toString().length() > 1) {
                    // check state of toggle switch for selecting which adapter to use when populating
                    if (switchUserClub.isChecked()) {
                        queryClubsByKeyword(etSearch.getText().toString(), isValidKeyword);
//                        queryFollowings();
                        rvElementsFound.setAdapter(exploreClubAdapter);
                        exploreClubAdapter.notifyDataSetChanged();

                    } else {
                        queryUsersByKeyword(etSearch.getText().toString(), isValidKeyword);
//                        queryFollowings();
                        rvElementsFound.setAdapter(exploreUserAdapter);
                        exploreClubAdapter.notifyDataSetChanged();
                    }
                } else {
                    // TODO: enter stuff here for handling invalid keyword entry cases
                }
            }
        });
        // DEBUG
        if (switchUserClub.isChecked()) {
            Toast.makeText(getBaseContext(), "Search for Users", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Search for Clubs", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Function for querying for all Interests and organizing the collection of Interests into an InterestGroup object.
     * InterestGroup is used to store the string aggregation of Interest names that share
     * the same archetype. A linked hashmap is used to provide keyword access grouping by archetype
     * as well as maintenance of order insertion.
     */
    private void queryAndGroupInterestsByArchetype() {
        Log.i(TAG, "queryAndGroupInterestsByArchetype call");
        ParseQuery<Interest> query = ParseQuery.getQuery(Interest.class);
        query.setLimit(200);
        query.findInBackground(new FindCallback<Interest>() {
            @Override
            public void done(List<Interest> interests, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                LinkedHashMap<String, List<Interest>> lhmInterests = new LinkedHashMap<>();
                for (Interest i : interests) {
                    if (!lhmInterests.keySet().contains(i.getArchetype())) {
                        lhmInterests.put(i.getArchetype(), new ArrayList<>());
                        lhmInterests.get(i.getArchetype()).add(i);
//                        Log.i(TAG, "Not in hashmap keys: " + i.getArchetype() + " ==> " + i.toString());    // DEBUG
                    } else {
//                        Log.i(TAG, "Key exists adding: " + i.toString());   // DEBUG
                        lhmInterests.get(i.getArchetype()).add(i);
                    }
                }
                for (String k : lhmInterests.keySet()) {
                    String names = "";
                    for (Interest i : lhmInterests.get(k)) {
                        names += i.getName() + ", ";
                    }
                    InterestGroup ig = new InterestGroup();
                    ig.names = names;
                    ig.archetypeEmoji = lhmInterests.get(k).get(0).getArchetypeEmoji();
                    ig.archetype = lhmInterests.get(k).get(0).getArchetype();
                    interestGroupList.add(ig);
                }
                exploreInterestAdapter.notifyDataSetChanged();

            }
        });
    }

    /**
     * Create and run a keyword-field indifferent matching ParseQuery
     * for searching Users based on the union of the following fields:
     * <ul>
     *     <li>username</li>
     *     <li>firstname</li>
     *     <li>lastname</li>
     *     <li>biography</li>
     * </ul>
     * The list of ParseUsers found are wrapped by the User class and updated to denote the follow
     * state represented by the current User's following list.
     *
     * @param keyword substring to search in the User fields
     */
    void queryUsersByKeyword(String keyword, boolean isValidKeyword) {
        Log.i(TAG, "queryUsersByKeyword");
        // reset list of Users found
        allUsers.clear();
        if (!isValidKeyword)
            exploreUserAdapter.notifyDataSetChanged();

//        ParseQuery<Follow> mainQuery = new ParseQuery<Follow>(Follow.class);
//        mainQuery.whereEqualTo(Follow.KEY_FROM, currentUser.getParseUser().getObjectId());

        // substring querying
        // create querying by username
        ParseQuery<ParseUser> queryByUsername = ParseUser.getQuery();
        queryByUsername.whereMatches("username", keyword, "i");
        // exclude the user's self
        queryByUsername.whereNotEqualTo("objectId",
                currentUser.getParseUser().getObjectId());

        // create querying by firstname
        ParseQuery<ParseUser> queryByFirstName = ParseUser.getQuery();
        // exclude the user's self
        queryByFirstName.whereNotEqualTo("objectId",
                currentUser.getParseUser().getObjectId());
        queryByFirstName.whereMatches("firstName", keyword, "i");

        // create querying by lastname
        ParseQuery<ParseUser> queryByLastName = ParseUser.getQuery();
        // exclude the user's self
        queryByLastName.whereNotEqualTo("objectId", currentUser.getParseUser().getObjectId());
        queryByLastName.whereMatches("lastName", keyword, "i");

        // create querying by biography
        ParseQuery<ParseUser> queryByBiography = ParseUser.getQuery();
        // exclude the user's self
        queryByBiography.whereNotEqualTo("objectId", currentUser.getParseUser().getObjectId());
        queryByBiography.whereMatches("biography", keyword, "i");

        // create the collection of queries
        List<ParseQuery<ParseUser>> queries = new ArrayList<ParseQuery<ParseUser>>();
        queries.add(queryByUsername);
        queries.add(queryByFirstName);
        queries.add(queryByLastName);
        queries.add(queryByBiography);

        // create the union for all queries
        ParseQuery<ParseUser> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
//                    allUsers.clear();
                    for (ParseUser o : objects) {
                        User u = new User(o);
                        Log.i(TAG, u.getLastName() + u.getFirstName() + u.getBiography() + u.getPhoneNumber());
                        // process user's that are followed by the current user
                        for (ParseObject user : currentUser.getFollowing()) {
                            if (o.getObjectId().equals(user.getObjectId())) {
                                u.isFollowed = true;
                            }
                        }
                        allUsers.add(u);
                        Log.i(TAG, String.valueOf(following.size()));
                        Log.i(TAG, String.valueOf(following));
                    }
                } else {
                }
                exploreUserAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Create and run a keyword-field indifferent matching ParseQuery
     * for searching Clubs based on the union of the following fields:
     * <ul>
     *     <li>name</li>
     *     <li>description</li>
     * </ul>
     *
     * @param keyword substring to search in the Club fields
     */
    void queryClubsByKeyword(String keyword, boolean isValidKeyword) {
        // reset list of Clubs found
        allClubs.clear();
        if (!isValidKeyword)
            exploreClubAdapter.notifyDataSetChanged();

        // substring query creation
        // create querying by name
        ParseQuery<Club> queryByName = new ParseQuery<Club>(Club.class);
        // modifier i makes the search matching ignorant of uppercase/lowercase matching in characters
        queryByName.whereMatches("name", keyword, "i");

        // create querying by description
        ParseQuery<Club> queryByDescription = new ParseQuery<Club>(Club.class);
        queryByDescription.whereMatches("description", keyword, "i");

        // create the collection of queries
        List<ParseQuery<Club>> queries = new ArrayList<ParseQuery<Club>>();
        queries.add(queryByName);
        queries.add(queryByDescription);

        // create the union for all queries
        ParseQuery<Club> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<Club>() {
            @Override
            public void done(List<Club> objects, ParseException e) {
                if (e == null) {
                    for (Club c : objects) {
                        if (!allClubs.contains(c))
                            allClubs.add(c);
                        Log.i(TAG, c.getName());
                    }
                } else {
                }
                exploreClubAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Test function for loading all users available to be followed
     */
    void queryAllUsers() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (ParseUser u : users) {
                    User converted = new User(u);
                    allUsers.add(converted);
                }
                exploreUserAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Testing method for querying all existing Interest instances
     */
    private void queryInterests() {
        ParseQuery<Interest> query = ParseQuery.getQuery(Interest.class);
        query.setLimit(200);
        query.findInBackground(new FindCallback<Interest>() {
            @Override
            public void done(List<Interest> interests, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                // TODO: needs refactor to incorporate all interest names to be under one archetype
                allInterests.addAll(interests);
                Log.i(TAG, "Length of all interests: " + String.valueOf(allInterests.size()));
                exploreInterestAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * TODO: need to refactor to be applied during the keyword search
     * Code for usage in the view User profile
     * Query for all ParseObjects followed by the current User
     */
    void queryFollowings() {
        Log.i(TAG, "queryFollowings");
//        allFollowings.clear();
        ParseQuery<Follow> mainQuery = new ParseQuery<Follow>(Follow.class);
        mainQuery.whereEqualTo(Follow.KEY_FROM, currentUser.getParseUser().getObjectId());
        mainQuery.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> objects, ParseException e) {
                Log.i(TAG, String.valueOf(objects.size()) + " followings");
                if (e == null) {
                    // iterate over all Users found from search
                    for (User u : allUsers) {
                        // iterate over all Follow entries
                        for (Follow c : objects) {
                            // compare
                            Log.i(TAG, String.format("Current: %s From: %s To: %s",
                                    u.getParseUser().getObjectId(),
                                    c.getFollowFrom().getObjectId(),
                                    c.getFollowTo().getObjectId()));
                            if (u.getParseUser().getObjectId().equals(c.getFollowTo().getObjectId())) {
                                u.isFollowed = true;
                                exploreUserAdapter.notifyDataSetChanged();
                            }
                            Log.i(TAG, c.getFollowTo().getObjectId());
                        }
                    }

                } else {
                }
                exploreUserAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Code for usage in the view User profile
     * Query for all ParseObjects that are followers of the input current User
     * Usage of this function requires central collection variable
     *
     * @param targetUser User of interest when querying for followers
     * @param adapter    Adapter to be notified of data collection changes
     */
    void queryFollowers(User targetUser, BaseAdapter adapter, Collection<ParseUser> collection) {
        Log.i(TAG, "queryFollowers");
        collection.clear();
        ParseQuery<Follow> mainQuery = new ParseQuery<Follow>(Follow.class);
        mainQuery.whereEqualTo(Follow.KEY_TO, targetUser.getParseUser().getObjectId());
        mainQuery.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> objects, ParseException e) {
                Log.i(TAG, String.valueOf(objects.size()) + " followings");
                if (e == null) {
                    // iterate over all Follow entries
                    for (Follow c : objects) {
                        // compare
                        Log.i(TAG, c.getFollowTo().getObjectId());
                        collection.add((ParseUser) c.getFollowFrom());
                    }
                } else {
                }
                // modify adapter to be used in
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Test method for querying for a User using an objectId
     *
     * @param id
     */
    void queryUser(String id) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", id);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser awesome, ParseException e) {
                if (e == null) {
                    User u = new User(awesome);
                    Log.i(TAG, u.getLastName() + u.getFirstName() + u.getBiography() + u.getPhoneNumber());
                    following.add(u.getParseUser());
                    Log.i(TAG, String.valueOf(following.size()));
                    Log.i(TAG, String.valueOf(following));
                    Log.i(TAG, "Proceed to populate from here or notify that adapter that the data has changed");
                } else {
                }
            }
        });
    }


}
