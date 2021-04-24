package com.pepetech.squadhouse.activities.Home;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Event.EventActivity;
import com.pepetech.squadhouse.activities.Explore.ExploreActivity;
import com.pepetech.squadhouse.activities.Home.adapters.HomeMultiViewAdapter;
import com.pepetech.squadhouse.activities.MyProfile.MyProfileActivity;
import com.pepetech.squadhouse.activities.SetUpRoomActivity;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.Room;
import com.pepetech.squadhouse.models.RoomRoute;
import com.pepetech.squadhouse.models.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "HomeActivity";

    Button btnCreateRoom;
    FloatingActionButton fabCreateRoomWithFollowers;
    RecyclerView rvRooms;
    HomeMultiViewAdapter viewAdapter;
    List<Object> allRooms;
    List<RoomRoute> availableRoutes;
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ////////////////////////////////////////////////////////////
        // Setup buttons
        ////////////////////////////////////////////////////////////
        fabCreateRoomWithFollowers = findViewById(R.id.fabCreateRoomWithFollowers);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        setupOnClickListeners();

        ////////////////////////////////////////////////////////////
        // Setup recycler view
        ////////////////////////////////////////////////////////////
        allRooms = new ArrayList<>();
        // configure hetero recycler using dummy values
        allRooms.add(1);    // any integer value denotes the explore cell
        allRooms.add("future"); // any future string denotes the cell of personalized future events for the user
        rvRooms = findViewById(R.id.rvHomeFeed);
        availableRoutes = new ArrayList<>();
        viewAdapter = new HomeMultiViewAdapter(this, allRooms);
        rvRooms.setAdapter(viewAdapter);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));

        ////////////////////////////////////////////////////////////
        // Setup swipe to refresh feature
        ////////////////////////////////////////////////////////////
        swipeContainer = findViewById(R.id.swipeContainer);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "Refresh called -> fetching new data!");
                queryRooms();
            }
        });
        queryRooms();
        // needed to refresh for any app user
        refreshMyFollowerCount();
    }

    /**
     * Setup non-toolbar buttons to route to associated pages
     */
    private void setupOnClickListeners() {
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Create room clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Create room clicked!");

                Intent i = new Intent(HomeActivity.this, SetUpRoomActivity.class);
                startActivity(i);
                //RoomRoute r = new RoomRoute();
//                r.put(RoomRoute.KEY_PHONE_NUMBER, "+16193045061");
//                r.remove(RoomRoute.KEY_ROOM_ROUTED);
//                r.saveInBackground();
                // TODO: Call a bottom sheet here
            }
        });

        fabCreateRoomWithFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Create room with follower(s) clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Function for querying rooms handling for both empty and non-empty cases for
     * populating the MultiViewAdapter. This function is called when the User
     * performs a pull-to-refresh to show the newest active rooms.
     */
    private void queryRooms() {
        ParseQuery<Room> query = ParseQuery.getQuery(Room.class);
        // case in which there exists rooms
        if (allRooms.size() > 2) {
            Log.i(TAG, String.valueOf(((Room) allRooms.get(allRooms.size() - 1)).getCreatedAt()));
            query.whereGreaterThan(Room.KEY_CREATED_AT, ((Room) allRooms.get(2)).getCreatedAt());
            query.whereEqualTo(Room.KEY_IS_ACTIVE, true);
            query.orderByDescending(Room.KEY_CREATED_AT);
            query.findInBackground(new FindCallback<Room>() {
                @Override
                public void done(List<Room> rooms, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting posts", e);
                        return;
                    }
                    // refresh should show... most recent at the top to oldest at the bottom
                    // because the recycler uses a collection of Objects that is prepopulated
                    // for denoting cell elems, the point of insertion is after index 1 ie index 2
                    // 0 --> explore cell
                    // 1 --> future cell
                    // 2 --> active of the previously most recent room
                    allRooms.addAll(2, rooms);
                    viewAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                }
            });
        }
        // case where there does not exist any rooms
        else {
            query.whereEqualTo(Room.KEY_IS_ACTIVE, true);
            query.orderByDescending(Room.KEY_CREATED_AT);
            query.findInBackground(new FindCallback<Room>() {
                @Override
                public void done(List<Room> rooms, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting posts", e);
                        return;
                    }
                    allRooms.addAll(rooms);
                    viewAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                }
            });
        }
    }

    /**
     * Menu icons are inflated just as they were with actionbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    /**
     * Setup toolbar buttons to route to associated pages
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_calendar:
                Toast.makeText(this, "Calendar clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Calendar clicked!");
                i = new Intent(this, EventActivity.class);
                startActivity(i);
//                this.startActivity(profileIntent);
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            case R.id.action_history:
                Toast.makeText(this, "History clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "History clicked!");
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            case R.id.action_invite:
                Toast.makeText(this, "Invite clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Invite clicked!");
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;

            case R.id.action_profile:
                Toast.makeText(this, "Profile clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Profile clicked!");
                i = new Intent(this, MyProfileActivity.class);
                this.startActivity(i);
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            case R.id.action_search:
                Toast.makeText(this, "Search clicked!", Toast.LENGTH_SHORT).show();
                i = new Intent(this, ExploreActivity.class);
                this.startActivity(i);
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshMyFollowerCount();
    }

    /**
     * Function called when a User navigates to the HomeFeed
     */
    private void refreshMyFollowerCount() {
        Log.i(TAG, "refreshMyFollowerCount");
        Log.i(TAG, "Target: " + ParseUser.getCurrentUser().getObjectId());
        List<Follow> collection = new ArrayList<>();
        ParseQuery<Follow> mainQuery = new ParseQuery<Follow>(Follow.class);
        // fina all Users following targetUser
        mainQuery.whereEqualTo(Follow.KEY_TO, ParseUser.getCurrentUser());
        mainQuery.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> follows, ParseException e) {
                Log.i(TAG, String.valueOf(follows.size()) + " followings");
                if (e == null) {
                    // for each follower check if the follower is followed by the current user
                    collection.addAll(follows);
                    ParseUser.getCurrentUser().put(User.KEY_FOLLOWER_COUNT, collection.size());
                    ParseUser.getCurrentUser().saveInBackground();
                } else {
                }
            }
        });
    }


}
