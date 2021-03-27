package com.pepetech.squadhouse.activities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "HomeActivity";

    Button btnCreateRoom;
    ImageButton btnSearch;
    FloatingActionButton fabCreateRoomWithFollowers;
    ImageButton btnProfile;
    ImageButton btnActivityHistory;
    ImageButton btnCalendar;
    ImageButton btnInvite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fabCreateRoomWithFollowers = findViewById(R.id.fabCreateRoomWithFollowers);

        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnSearch = findViewById(R.id.btnSearch);
        btnProfile = findViewById(R.id.btnProfile);
        btnActivityHistory = findViewById(R.id.btnActivityHistory);
        btnCalendar = findViewById(R.id.btnCalendar);
        btnInvite = findViewById(R.id.btnInvite);

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
            }
        });
        btnActivityHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Activity hisory clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Calendar clicked!", Toast.LENGTH_SHORT).show();
            }
        });
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Invite clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}