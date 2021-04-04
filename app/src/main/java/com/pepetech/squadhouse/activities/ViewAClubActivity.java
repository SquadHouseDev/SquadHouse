package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.os.Parcel;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Club;

import org.parceler.Parcels;

public class ViewAClubActivity extends AppCompatActivity {
    public static final String TAG = "ViewAClubActivity";
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
//        club = Parcels.unwrap("club");
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
