package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ViewAClubActivity extends AppCompatActivity {
    public static final String TAG = "ViewAClubActivity";
    ConstraintLayout clFollowers, clFollowing;
    TextView tvClubName, tvClubDescription, tvInterests;
    ImageView ivClubImage;
    Club club;
    private ParseUser parseUser;
    User currentUser;
    List<Interest> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club_profile);

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
        interests = new ArrayList<>();
        queryAndPopulate();
    }

    // TODO complete club profile element populating
    private void queryAndPopulate() {
        try {
            interests = club.fetchIfNeeded().getList(Club.KEY_INTERESTS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String interestStr = "";
        for (int i = 0; i < interests.size(); i++) {
            interestStr += interests.get(i).getArchetypeEmoji() + interests.get(i).getName();
            if (i < interests.size() - 1)
                interestStr += "·";
        }
        tvInterests.setText(interestStr);
        tvClubDescription.setText(club.getDescription());
        tvClubName.setText(club.getName());
        ParseFile image = club.getImage();
        if (image != null)
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivClubImage);
    }

}
