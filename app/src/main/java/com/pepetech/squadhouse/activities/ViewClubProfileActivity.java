package com.pepetech.squadhouse.activities;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.User;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// TODO: implement heterogenous recycler view
// 1st section should be the club profile
// 2nd section should be a recycler of all members with buttons to follow
// NOTE: current code reflects testing using clubs followed by a user
public class ViewClubProfileActivity extends AppCompatActivity {
    public static final String TAG = "ViewClubProfileActivity";
    TextView tvClubName, tvClubDescription, tvInterests;
    ImageView ivClubImage;
    Club club;
    ArrayList<ParseObject> allClubs;    // TESTING
    List<ParseObject> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club_profile);
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
        // call club profile info loading methods here
        queryClubProfile();
        // call club element populating methods here
        populateClubProfileElements();
    }

    // TODO complete club profile element populating
    private void populateClubProfileElements() {
        loadClubProfileImage();
        tvClubDescription.setText(club.getDescription());
        tvClubName.setText(club.getName());
        String interestsStr = "";
        for (ParseObject i : interests) {
            Interest interest = ((Interest) i);
            interestsStr += interest.getArchetypeEmoji() + interest.getName();
            if (interests.size() > 1)
                interestsStr += "·";    // append floating middle dot
        }
        tvInterests.setText(interestsStr);

    }

    // TODO complete club profile data querying
    private void queryClubProfile() {
        // test code needs to be updated to reflect a club a user has clicked on
        User user = new User(ParseUser.getCurrentUser());
        allClubs = user.getClubs(); // TESTING
        club = (Club) allClubs.get(0); // TESTING
        interests = (List<ParseObject>) club.getInterests();
        for (ParseObject i : interests)
            Log.i(TAG, ((Interest) i).getName());
//        loadClubProfileImage();
    }

    private void loadClubProfileImage() {
        // needs to be updated to reflect a club a user has clicked on
        ParseFile image = null;
        club = (Club) allClubs.get(0); // TESTING
        try {
            if (club == null)
                return;
            image = club.fetchIfNeeded().getParseFile("image");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (image != null)
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivClubImage);
    }

    /**
     * Method for creating the action Club bar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_club_profile, menu);
        return true;
    }

}
