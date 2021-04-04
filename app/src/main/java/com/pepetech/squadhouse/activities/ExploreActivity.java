package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.adapters.ExploreClubAdapter;
import com.pepetech.squadhouse.adapters.ExploreInterestAdapter;
import com.pepetech.squadhouse.adapters.ExploreUserAdapter;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.InterestGroup;
import com.pepetech.squadhouse.models.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * TODO: add handling for empty keyword search
 */
public class ExploreActivity extends AppCompatActivity {
    public static final String TAG = "ExploreActivity";
    List<User> allUsers;
    List<Club> allClubs;
    TextView tvElementsLabel;
    RecyclerView rvElementsFound, rvInterests;
    EditText etSearch;
    Button btnSearch;
    Switch switchUserClub;
    ExploreUserAdapter exploreUserAdapter;
    ExploreClubAdapter exploreClubAdapter;
    ExploreInterestAdapter exploreInterestAdapter;
    ExploreInterestAdapter exploreInterestAdapter;
    List<User> users;
    List<Interest> allInterests;
    private List<InterestGroup> interestsGrouped;

    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        // disable auto focus keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // setup current User and the collection of Users
        currentUser = new User(ParseUser.getCurrentUser());
        users = new ArrayList<>();
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        tvElementsLabel = findViewById(R.id.tvElementsLabel);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        switchUserClub = findViewById(R.id.switchUserClub);
        rvElementsFound = findViewById(R.id.rvElementsFound);
        rvInterests = findViewById(R.id.rvInterests);
        setupOnClickListeners();
        ////////////////////////////////////////////////////////////
        // Setup recycler views
        ////////////////////////////////////////////////////////////
        interestsGrouped = new ArrayList<>();
        allUsers = new ArrayList<>();
        allClubs = new ArrayList<>();
        allInterests = new ArrayList<>();
        // 2 adapters to toggle between with the toggle switch
        exploreClubAdapter = new ExploreClubAdapter(this, allClubs, currentUser);
        exploreUserAdapter = new ExploreUserAdapter(this, allUsers, currentUser);
        // configure layout managers
        rvElementsFound.setLayoutManager(new LinearLayoutManager(this));
        rvInterests.setLayoutManager(new GridLayoutManager(this, 2));
        exploreInterestAdapter = new ExploreInterestAdapter(this, interestsGrouped);
        rvInterests.setAdapter(exploreInterestAdapter);
        queryFollowing();
        queryAndGroupInterestsByArchetype();
//        queryInterests();
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
                Toast.makeText(v.getContext(), "Search button clicked!", Toast.LENGTH_SHORT).show();
                // check state of toggle switch for selecting which adapter to use when populating
                if (switchUserClub.isChecked()) {
                    queryClubsByKeyword(etSearch.getText().toString());
                    rvElementsFound.setAdapter(exploreClubAdapter);
                } else {
                    queryUsersByKeyword(etSearch.getText().toString());
                    rvElementsFound.setAdapter(exploreUserAdapter);
                }
            }
        });

        if (switchUserClub.isChecked()) {
            Toast.makeText(getBaseContext(), "Search for Users", Toast.LENGTH_SHORT).show(); // display the current state for switch's
        } else {
            Toast.makeText(getBaseContext(), "Search for Clubs", Toast.LENGTH_SHORT).show(); // display the current state for switch's
        }
    }


    /**
     * TODO: need to fix grouping of interests by archetypes ie "Tech" : [Engineering, SaaS, DTC, ...]
     * Main method for query interests
     */
    private void queryInterests() {
        ParseQuery<Interest> query = ParseQuery.getQuery(Interest.class);
//        query.include("archetype");
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
     * Function for querying for all Interests and organizing the collection of Interests into an InterestGroup object.
     * InterestGroup is used to store the string aggregation of Interest names that share
     * the same archetype.
     */
    private void queryAndGroupInterestsByArchetype() {
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
                        Log.i(TAG, "Not in hashmap keys: " + i.getArchetype() + " ==> " + i.toString());
                    } else {
                        Log.i(TAG, "Key exists adding: " + i.toString());
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
                    interestsGrouped.add(ig);
                }
                exploreInterestAdapter.notifyDataSetChanged();
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
     * Query for all Users followed by the current User
     */
    void queryFollowing() {
//        List<String> followingIds = ParseUser.getCurrentUser().getList("following");
        List<String> followingIds = (List<String>) currentUser.getFollowing();
        if (followingIds == null)
            return;
        if (!followingIds.isEmpty()) {
            for (String id : followingIds) {
                Log.i(TAG, id);
                queryUser(id);
            }
        }
    }

    void queryUser(String id) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", id);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser awesome, ParseException e) {
                if (e == null) {
                    User u = new User(awesome);
                    Log.i(TAG, u.getLastName() + u.getFirstName() + u.getBiography() + u.getPhoneNumber());
//                    users.clear();
                    users.add(u);
                    Log.i(TAG, String.valueOf(users.size()));
                    Log.i(TAG, String.valueOf(users));
                    Log.i(TAG, "Proceed to populate from here or notify that adapter that the data has changed");
                } else {
                }
            }
        });
    }

    /**
     * Create and run a ParseQuery for searching for Users based on the union of the following fields:
     * <ul>
     *     <li>username</li>
     *     <li>firstname</li>
     *     <li>lastname</li>
     *     <li>biography</li>
     * </ul>
     *
     * @param keyword substring to search in the User fields
     */
    void queryUsersByKeyword(String keyword) {
        // reset list of Users found
        allUsers.clear();
        // substring querying
        // create querying by username
        ParseQuery<ParseUser> queryByUsername = ParseUser.getQuery();
        queryByUsername.whereContains("username", keyword);
        queryByUsername.whereNotEqualTo("objectId", currentUser.getParseUser().getObjectId());    // exclude the user's self
        // create querying by firstname
        ParseQuery<ParseUser> queryByFirstName = ParseUser.getQuery();
        queryByFirstName.whereNotEqualTo("objectId", currentUser.getParseUser().getObjectId());    // exclude the user's self
        queryByFirstName.whereContains("firstName", keyword);
        // create querying by lastname
        ParseQuery<ParseUser> queryByLastName = ParseUser.getQuery();
        queryByLastName.whereNotEqualTo("objectId", currentUser.getParseUser().getObjectId());    // exclude the user's self
        queryByLastName.whereContains("lastName", keyword);
        // create querying by biography
        ParseQuery<ParseUser> queryByBiography = ParseUser.getQuery();
        queryByBiography.whereNotEqualTo("objectId", currentUser.getParseUser().getObjectId());    // exclude the user's self
        queryByBiography.whereContains("biography", keyword);
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
                        if (!allUsers.contains(u))
                            allUsers.add(u);
                        Log.i(TAG, String.valueOf(users.size()));
                        Log.i(TAG, String.valueOf(users));
                        Log.i(TAG, "Proceed to populate from here or notify that adapter that the data has changed");
                    }
                } else {
                }
                exploreUserAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Create and run a ParseQuery for searching for Clubs based on the union of the following fields:
     * <ul>
     *     <li>name</li>
     *     <li>description</li>
     * </ul>
     *
     * @param keyword substring to search in the Club fields
     */
    void queryClubsByKeyword(String keyword) {
        // reset list of Clubs found
        allClubs.clear();
        // substring querying
        // create querying by name
        ParseQuery<Club> queryByName = new ParseQuery<Club>(Club.class);
        queryByName.whereContains("name", keyword);
        // create querying by description
        ParseQuery<Club> queryByDescription = new ParseQuery<Club>(Club.class);
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
//                    allUsers.clear();
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


}
