package com.pepetech.squadhouse.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Club;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ClubActivity extends AppCompatActivity {
    public static final String TAG = "ClubActivity";
    TextView tvClubName, tvClubDescription, tvInterests;
    ImageView ivClubImage;
    Club club;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
        ////////////////////////////////////////////////////////////
        // Initialize club page variables
        ////////////////////////////////////////////////////////////
//        club = (Club) ParseUser.getCurrentUser().getList("clubs").get(0); // DEBUG
        tvClubDescription = findViewById(R.id.tvClubDescription);
        tvInterests = findViewById(R.id.tvInterests);
        tvClubName = findViewById(R.id.tvClubName);
        ivClubImage = findViewById(R.id.ivClubImage);


        ////////////////////////////////////////////////////////////
        // Query club info and populate
        ////////////////////////////////////////////////////////////
        queryClubProfile();

        populateClubProfileElements();
    }

    // TODO complete club profile element populating
    private void populateClubProfileElements() {
    }

    // TODO complete club profile data querying
    private void queryClubProfile() {
    }
}
