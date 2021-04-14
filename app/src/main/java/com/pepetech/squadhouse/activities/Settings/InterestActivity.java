package com.pepetech.squadhouse.activities.Settings;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Settings.adapters.OuterInterestAdapter;
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the page routed to upon selection of Interest button
 * on the Settings page. This activity utilizes 2 RecyclerViews to
 * implement the one page scrollable User interface. The outer adapter is used
 * for handling the rows of interests grouped by their archetype displayed
 * in a vertical linear layout. The inner adapter uses a grid layout with
 * rows dynamically configured.
 */
public class InterestActivity extends AppCompatActivity {
    public static final String TAG = "InterestActivity";
    OuterInterestAdapter outerAdapter;
    List<List<Interest>> interests;
    RecyclerView rvOuterInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        setTitle("MY INTERESTS");
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        rvOuterInterest = findViewById(R.id.rvOuterInterest);
        interests = new ArrayList<>();
        outerAdapter = new OuterInterestAdapter(this, interests, new User(ParseUser.getCurrentUser()));
        rvOuterInterest.setAdapter(outerAdapter);
        rvOuterInterest.setLayoutManager(new LinearLayoutManager(this));
        queryAndGroupInterestsByArchetype(outerAdapter, interests);
    }

    /**
     * Function for querying for all Interests to create the 2D List.
     * A linked hashmap is used to provide keyword access grouping by archetype
     * as well as maintenance of order insertion.
     *
     * @param inputAdapter
     * @param inputInterests
     */
    private void queryAndGroupInterestsByArchetype(RecyclerView.Adapter inputAdapter, List<List<Interest>> inputInterests) {
        // toggle for debug logging
        boolean DEBUG = false;
        inputInterests.clear();
        Log.i(TAG, "queryAndGroupInterestsByArchetype call");
        ParseQuery<Interest> query = ParseQuery.getQuery(Interest.class);
        query.setLimit(200);
        query.findInBackground(new FindCallback<Interest>() {
            @Override
            public void done(List<Interest> interestsQueried, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                LinkedHashMap<String, List<Interest>> lhmInterests = new LinkedHashMap<>();
                User u = new User(ParseUser.getCurrentUser());
                List<Interest> userInterests = u.getInterests();
                Log.i(TAG, "Before MAP Interest List size: " + userInterests.size());
                for (Interest i : userInterests) {
                    Log.i(TAG, i.getObjectId() + " : " + i.getName());
                }

                // map archetype to list of interests
                for (Interest i : interestsQueried) {
                    if (!lhmInterests.keySet().contains(i.getArchetype())) {
                        // create new empty list
                        lhmInterests.put(i.getArchetype(), new ArrayList<>());
                        // check if interest is in User's interest list
                        Log.i(TAG, "IF start loop objectId: " + i.getName());
                        Log.i(TAG, "Interest Checked: " + i.getName());
                        for (Interest _ : userInterests) {
                            Log.i(TAG, "Interest List size: " + userInterests.size());
                            if (_.getObjectId().equals(i.getObjectId())) {
                                Log.i(TAG, "Create new list " + i.getName() + " is in User's list");
                                i.isSelected = true;
                                break;
                            } else {
                                Log.i(TAG, "Create new list " + i.getName() + " is not in User's list");
                                i.isSelected = false;
                            }
                        }
                        Log.i(TAG, "IF end loop objectId: " + i.getName());
                        // add to newly created list
                        lhmInterests.get(i.getArchetype()).add(i);
                        Log.i(TAG, "Not in hashmap keys: " + i.getArchetype() + " ==> " + i.toString());    // DEBUG
                    } else {

                        // check if interest is in User's interest list
                        Log.i(TAG, "ELSE start loop objectId: " + i.getName());
                        Log.i(TAG, "Key exists adding: " + i.toString());   // DEBUG
                        Log.i(TAG, "Interest Checked: " + i.getName());
                        Log.i(TAG, "Interest List size: " + userInterests.size());
                        for (Interest _ : userInterests) {
                            if (_.getObjectId().equals(i.getObjectId())) {
                                Log.i(TAG, "Add to existing list " + i.getName() + " is in User's list");
                                i.isSelected = true;
                                break;
                            } else {
                                Log.i(TAG, "Add to existing list " + i.getName() + " is not in User's list");
                                i.isSelected = false;
                            }
                        }
                        Log.i(TAG, "ELSE end loop objectId: " + i.getName());
                        // add to existing list
                        lhmInterests.get(i.getArchetype()).add(i);
                    }
                }
                // add to 2d list
                for (Map.Entry<String, List<Interest>> entry : lhmInterests.entrySet()) {
                    inputInterests.add((List<Interest>) entry.getValue());
                    Log.i(TAG, "inputInterests size: " + String.valueOf(inputInterests.size()));
                    Log.i(TAG, inputInterests.get(0).get(0).getArchetype());
                }
                // DEBUG
                for (List<Interest> i : inputInterests) {
                    for (Interest j : i) {
                        if (j.isSelected) {
                            Log.i(TAG, j.toString() + " is selected");
                        }
                    }
                }
                inputAdapter.notifyDataSetChanged();
            }
        });
    }
}
