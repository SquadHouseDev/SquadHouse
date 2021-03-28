package com.pepetech.squadhouse.activities;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.adapters.RoomsAdapter;
import com.pepetech.squadhouse.models.Room;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: add cell_search to the recycler view
 * TODO: add cell_room_future to the recycler view
 * Recycler view should start with:
 * 1. cell_search
 * 2. cell_room_future
 * 3. cell_room_active
 * 4. cell_room_active
 * .
 * .
 * N
 *
 */
public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "HomeActivity";

    Button btnCreateRoom;
    ImageButton btnSearch;
    FloatingActionButton fabCreateRoomWithFollowers;
    ImageButton btnProfile;
    ImageButton btnActivityHistory;
    ImageButton btnCalendar;
    ImageButton btnInvite;

    RecyclerView rvRooms;
    RoomsAdapter adapter;
    
    List<Room> allRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // initialize buttons
        fabCreateRoomWithFollowers = findViewById(R.id.fabCreateRoomWithFollowers);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnSearch = findViewById(R.id.btnSearch);
        btnProfile = findViewById(R.id.btnProfile);
        btnActivityHistory = findViewById(R.id.btnActivityHistory);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnInvite = findViewById(R.id.btnInvite);
        // setup recycler view
        allRooms = new ArrayList<>();
        rvRooms = findViewById(R.id.rvRooms);
        adapter = new RoomsAdapter(this, allRooms);
        rvRooms.setAdapter(adapter);
        rvRooms.setLayoutManager(new LinearLayoutManager(this));
        queryRooms();
        // setup button click listeners
        setupOnClickListeners();
    }

    /**
     * Main method for configuring button listeners
     */
    private void setupOnClickListeners() {
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getBaseContext(), "Create room clicked!", Toast.LENGTH_SHORT);
                Log.i(TAG, "Create room clicked!");
            }
        });

        fabCreateRoomWithFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Create room with follower(s) clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Search clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Search clicked!");

            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Profile clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Profile clicked!");
                Intent profileIntent = new Intent(v.getContext(), ProfileActivity.class );
                v.getContext().startActivity(profileIntent);
                overridePendingTransition( R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        btnActivityHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Activity hisory clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Activity hisory clicked!");

            }
        });
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Calendar clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Calendar clicked!");

            }
        });
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Invite clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Invite clicked!");

            }
        });
    }



    /**
     *
     */
    private void queryRooms() {
        ParseQuery<Room> query = ParseQuery.getQuery(Room.class);
        query.include(Room.KEY_IS_ACTIVE);
        query.findInBackground(new FindCallback<Room>() {
            @Override
            public void done(List<Room> rooms, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
//                for (Room p : posts) {
//                    Log.i(TAG, "Room: " + p.getDescription() + ", username: " + p.getUser().getUsername());
//                }
                allRooms.addAll(rooms);
                adapter.notifyDataSetChanged();
            }
        });
    }
}