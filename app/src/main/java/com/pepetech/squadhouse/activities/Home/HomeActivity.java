package com.pepetech.squadhouse.activities.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
import com.pepetech.squadhouse.models.Event;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.Room;
import com.pepetech.squadhouse.models.RoomRoute;
import com.pepetech.squadhouse.models.User;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains the collection of mixced object types for display:
 * 1. Integer - count 1
 * 2. Event - count varies
 * 3. Room - count varies
 * <p>
 * collection maintains the list of all objects that are used by the hetero recycler
 */
public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "HomeActivity";

    Button btnCreateRoom;
    FloatingActionButton fabCreateRoomWithFollowers;
    RecyclerView rvRooms;
    HomeMultiViewAdapter viewAdapter;
    List<Object> collection;
    List<Event> allEvents;
    List<Room> allRooms;
    List<RoomRoute> availableRoutes;
    SwipeRefreshLayout swipeContainer;

    private void initCollection() {
        collection = new ArrayList<>();
        allEvents = new ArrayList<>();
        allRooms = new ArrayList<>();
    }

    /**
     * Main method chain caller for querying for the collection of mixed object dtypes.
     * Resets the currection mixed dtype collection and adds an integer signifying the explore cell.
     * A query for events is called and any items found are added to the mixed collection. A query for
     * rooms is sent out by the queryEvents method after it is done. This maintains the order in which items are
     * added in while providing the refresh functionality.
     */
    private void queryCollection() {
        collection.clear();
        collection.add(1);
        queryEvents();
    }

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
        initCollection();
        rvRooms = findViewById(R.id.rvHomeFeed);
        availableRoutes = new ArrayList<>();
        viewAdapter = new HomeMultiViewAdapter(this, collection);
        rvRooms.setAdapter(viewAdapter);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        queryCollection();

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
                queryCollection();
                refreshMyFollowerCount();
            }
        });
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
//                Toast.makeText(getBaseContext(), "Create room clicked!", Toast.LENGTH_SHORT).show();
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
//                Toast.makeText(v.getContext(), "Create room with follower(s) clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     */
    public void queryRooms() {
        ParseQuery<Room> query = ParseQuery.getQuery(Room.class);
        allRooms.clear();
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
                collection.addAll(allRooms);
                viewAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        });
    }

    /**
     * Function for querying rooms handling for both empty and non-empty cases for
     * populating the MultiViewAdapter. This function is called when the User
     * performs a pull-to-refresh to show the newest active rooms.
     */
    public void queryRoomsDeprecated() {
        ParseQuery<Room> query = ParseQuery.getQuery(Room.class);
        // case in which there exists rooms

        // the constant value of 2 needs to be the value that varies based upon the sum of 1 + len(allEvents)
        if (collection.size() > 2) {
            Log.i(TAG, String.valueOf(((Room) collection.get(collection.size() - 1)).getCreatedAt()));
            query.whereGreaterThan(Room.KEY_CREATED_AT, ((Room) collection.get(2)).getCreatedAt());
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

                    collection.addAll(2, rooms);
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
                    collection.addAll(rooms);
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
//                Toast.makeText(this, "Calendar clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Calendar clicked!");
                i = new Intent(this, EventActivity.class);
                startActivity(i);
//                this.startActivity(profileIntent);
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            case R.id.action_history:
//                Toast.makeText(this, "History clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "History clicked!");
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            case R.id.action_invite:
//                Toast.makeText(this, "Invite clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Invite clicked!");
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;

            case R.id.action_profile:
//                Toast.makeText(this, "Profile clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Profile clicked!");
                i = new Intent(this, MyProfileActivity.class);
                this.startActivity(i);
                // arg_1: page to navigate to slides from the right
                // arg_2: page navigating from slides to the left
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            case R.id.action_search:
//                Toast.makeText(this, "Search clicked!", Toast.LENGTH_SHORT).show();
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

    /**
     * <p>Pre: allEvents empty</p>
     * <p>Post: allEvents updated</p>
     */
    private void queryEvents() {
        allEvents.clear();
        ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
        // case in which there exists rooms
        // create variable of today
        // query for Event.scheduledFor > today

        // TEST query for all user's events
//        query.whereEqualTo(Room.KEY_HOST, ParseUser.getCurrentUser());
        LocalDateTime now = LocalDateTime.now(); // Gets the current date and time
        query.whereGreaterThan(Event.KEY_SCHEDULED_FOR, now);
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> events, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                allEvents.addAll(events);
                collection.addAll(allEvents);
                viewAdapter.notifyDataSetChanged();
                Log.i(TAG, "# events found: " + allEvents.size());
                queryRooms();
//                collection.addAll(allEvents);
            }
        });
    }


}
