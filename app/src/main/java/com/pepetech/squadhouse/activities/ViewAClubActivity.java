package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.os.Parcel;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

public class ViewAClubActivity extends AppCompatActivity {
    public static final String TAG = "ViewAClubActivity";
    ConstraintLayout clFollowers, clFollowing;
    TextView tvClubName, tvClubDescription, tvInterests;
    ImageView ivClubImage;
    Club club;
    private ParseUser parseUser;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        ////////////////////////////////////////////////////////////
        // Initialize club page variables
        ////////////////////////////////////////////////////////////
        tvClubDescription = findViewById(R.id.tvClubDescription);
        tvInterests = findViewById(R.id.tvInterests);
        tvClubName = findViewById(R.id.tvClubName);
        ivClubImage = findViewById(R.id.ivClubImage);
        clFollowers = findViewById(R.id.clFollowers);
        clFollowing = findViewById(R.id.clFollowing);

        ////////////////////////////////////////////////////////////
        // Query club info and populate
        ////////////////////////////////////////////////////////////
        // unwrap pass Club
        club = Parcels.unwrap(getIntent().getParcelableExtra("club"));
        parseUser = ParseUser.getCurrentUser();
        currentUser = new User(parseUser);
        
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
