package com.pepetech.squadhouse.activities;

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
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * TODO: add handling for empty keyword search
 */
public class ExploreActivity extends AppCompatActivity {
    public static final String TAG = "SearchActivity";
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
    List<User> users;
    List<Interest> allInterests;
    private LinkedHashMap<String, List<Interest>> interestsGrouped;

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
        setupOnClickListeners();
        ////////////////////////////////////////////////////////////
        // Setup recycler views
        ////////////////////////////////////////////////////////////
        allUsers = new ArrayList<>();
        allClubs = new ArrayList<>();
        rvElementsFound = findViewById(R.id.rvElementsFound);
        exploreClubAdapter = new ExploreClubAdapter(this, allClubs, currentUser);
        exploreUserAdapter = new ExploreUserAdapter(this, allUsers, currentUser);

        //
        rvElementsFound.setLayoutManager(new LinearLayoutManager(this));

        allInterests = new ArrayList<>();
        rvInterests = findViewById(R.id.rvInterests);
        exploreInterestAdapter = new ExploreInterestAdapter(this, allInterests);
        rvInterests.setAdapter(exploreInterestAdapter);
        rvInterests.setLayoutManager(new GridLayoutManager(this, 2));
        queryFollowing();
        queryInterests();
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
     * TODO: Query creates of list of Interests but does not consolidate all interest names under one umbrella term.
     * TODO: The result (Ex. Archetype=Sport : InterestNames=[ Tennis, Basketball, Badminton, Swimming])
     *
     * @param interests
     */
    private void groupInterestsByArchetype(List<Interest> interests) {
        for (Interest i : interests) {
            if (!interestsGrouped.containsKey(i.getArchetype())) {
                interestsGrouped.put(i.getArchetype(), new ArrayList<>());
                interestsGrouped.get(i.getArchetype()).add(i);
            } else {
//                ArrayList<Interest> grouped = new ArrayList<>();
//                grouped.add(i);
                interestsGrouped.get(i.getArchetype()).add(i);
            }
        }
    }

    private void setupOnClickListeners() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Search button clicked!", Toast.LENGTH_SHORT).show();
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
     * Keyword searching for Users by the union of Users that have one or more fields: username, firstname, lastname, biography
     * that contain the substring of the input keyword excluding the current application's User self
     *
     * @param keyword
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
     * Keyword searching for Clubs by the union of Users that have one or more fields: name, description
     * that contain the substring of the input keyword
     *
     * @param keyword
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
