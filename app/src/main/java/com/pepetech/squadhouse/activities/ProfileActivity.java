package com.pepetech.squadhouse.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ParseUser user;
    public static final String TAG = "ProfileActivity";
    AppCompatImageView ivProfile, ivProfileNominator;
    TextView tvFullName, tvUsername, tvFollowersCount, tvFollowingCount, tvBiography, tvUserJoinDate, tvNominator;
    Button btnLogout;
    ImageButton btnSettings;

    List<ParseObject> following;
    List<ParseObject> followers;
//    ScrollView svProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        // image views
        ivProfile = findViewById(R.id.ivProfile);
        ivProfileNominator = findViewById(R.id.ivProfileNominator);
        // text views
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvFollowersCount = findViewById(R.id.tvFollowersCount);
        tvFollowingCount = findViewById(R.id.tvFollowingCount);
        tvBiography = findViewById(R.id.tvBiography);
        tvUserJoinDate = findViewById(R.id.tvUserJoinDate);
        tvNominator = findViewById(R.id.tvNominator);
        ////////////////////////////////////////////////////////////
        // Setup buttons
        ////////////////////////////////////////////////////////////
        btnLogout = findViewById(R.id.btnLogout);
        btnSettings = findViewById(R.id.btnSettings);
        setupOnClickListeners();
        ////////////////////////////////////////////////////////////
        // setting up user profile
        ////////////////////////////////////////////////////////////
        // 1. query profile data
        queryUserProfile();
        // 2. populate profile with queried profile data
        populateProfileElements();

    }

    private void setupOnClickListeners() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getBaseContext(), "Sign out button clicked!", Toast.LENGTH_SHORT);
                Log.i(TAG, "Sign out button clicked!");
                signoutUser();
                goToLoginActivity();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(getBaseContext(), "Settings button clicked!", Toast.LENGTH_SHORT);
                Log.i(TAG, "Settings button clicked!");
                goToSettingsActivity();
            }
        });

    }

    private void populateProfileElements() {
        Log.i(TAG, "Populating profile elements");
        // load profile picture
        ParseFile image = user.getParseFile(User.KEY_IMAGE);
        if (image != null)
            Glide.with(this.getBaseContext())
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfile);
        // load profile text information
        tvFullName.setText(user.getString(User.KEY_FIRST_NAME) + " " + user.getString(User.KEY_LAST_NAME));
        tvBiography.setText(user.getString(User.KEY_BIOGRAPHY));
        tvUsername.setText("@" + user.getUsername());
        // load following and followers count
        tvFollowersCount.setText(String.valueOf(followers.size()));
        tvFollowingCount.setText(String.valueOf(following.size()));
    }

    private void queryUserProfile() {
        Log.i(TAG, "Querying user profile data");
        user = ParseUser.getCurrentUser();
        Log.i(TAG, "User object_id: " + user.getObjectId());
        following = user.getList(User.KEY_FOLLOWING);
        followers = user.getList(User.KEY_FOLLOWERS);
        // empty case
        if (following == null) {
            following = new ArrayList<>();
        }
        else {
            Log.i(TAG, "Following size: " + following.size());
        }
        if (followers == null) {
            followers = new ArrayList<>();
        }
        else {
            Log.i(TAG, "Followers: " + followers.size());
        }
    }

    private void signoutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
    }

    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish(); // disable user ability to renavigate after a successful login
    }

    private void goToSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
//        overridePendingTransition(R.anim.slide_to_top, R.anim.slide_to_left);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

}