package com.pepetech.squadhouse.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/*

Use when clicking on the profile image of the nominator

*/


public class ViewAUserProfileActivity extends AppCompatActivity {
    ParseUser parseUser;
    User currentUser, userSelected;
    public static final String TAG = "ViewAUserActivity";
    AppCompatImageView ivProfile, ivProfileNominator;
    TextView tvFullName, tvUsername, tvFollowersCount, tvFollowingCount, tvBiography, tvUserJoinDate, tvNominatorName;
    Button btnFollow;
    ImageButton btnSettings;
    ParseUser nominator;
    List<ParseObject> following;
    List<ParseObject> followers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_a_user_profile);
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
        tvNominatorName = findViewById(R.id.tvNominatorName);
        ////////////////////////////////////////////////////////////
        // Setup buttons
        ////////////////////////////////////////////////////////////
        btnFollow = findViewById(R.id.btnFollow);
        setupOnClickListeners();
        ////////////////////////////////////////////////////////////
        // Setting up selected User's profile
        ////////////////////////////////////////////////////////////
        // unwrap passed User
        userSelected = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        parseUser = ParseUser.getCurrentUser();
        currentUser = new User(parseUser);
        // 1. query profile data
        queryUserProfile();
        // 2. populate profile with queried profile data
        populateProfileElements();

    }

    private void setupOnClickListeners() {
        ivProfileNominator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Nominator profile clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Nominator profile clicked!");
                if (nominator!=null){
                    Intent i = new Intent(v.getContext(), ViewAUserProfileActivity.class);
                    User toPass = new User(nominator);
                    i.putExtra("user", Parcels.wrap(toPass));
                    startActivity(i);
                }
            }
        });
        // TODO
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Nominator profile clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "User profile clicked!");
//                ParseObject nominator = user.getNominator()
//                goToProfileActivity();
            }
        });
        // TODO
        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Username clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Username clicked!");
//                ParseObject nominator = user.getNominator()
//                goToProfileActivity();
            }
        });
        // TODO
        tvFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Fullname clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Fullname clicked!");
//                ParseObject nominator = user.getNominator()
//                goToProfileActivity();
            }
        });
        // TODO
        tvBiography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ProfileActivity.this, "Biography clicked!", Toast.LENGTH_SHORT).show();
//                Toast.makeText(getConte, "Biography clicked!", Toast.LENGTH_SHORT).show();
                Toast.makeText(v.getContext(), "Biography clicked!", Toast.LENGTH_SHORT).show();
//                t.show();
                Log.i(TAG, "Biography clicked!");
//                ParseObject nominator = user.getNominator()
//                goToProfileActivity();
            }
        });
    }

    private void populateProfileElements() {
        Log.i(TAG, "Populating profile elements");
        // load user's profile picture
        ParseFile image = userSelected.getParseUser().getParseFile(User.KEY_IMAGE);
        if (image != null)
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfile);
        // load profile text information
        tvFullName.setText(userSelected.getParseUser().getString(User.KEY_FIRST_NAME) + " " + userSelected.getParseUser().getString(User.KEY_LAST_NAME));
        tvBiography.setText(userSelected.getParseUser().getString(User.KEY_BIOGRAPHY));
        tvUsername.setText("@" + userSelected.getParseUser().getUsername());
        // load following and followers count
        tvFollowersCount.setText(String.valueOf(followers.size()));
        tvFollowingCount.setText(String.valueOf(following.size()));
        // load nominator's profile picture
//        boolean isSeed = user.isSeed();
        if (!userSelected.isSeed()) {
            loadNominatorProfileImage();
            tvNominatorName.setText(nominator.getString(User.KEY_FIRST_NAME));
        }
    }

    private void queryUserProfile() {
        Log.i(TAG, "Querying user profile data");
        parseUser = ParseUser.getCurrentUser();
        Log.i(TAG, "User object_id: " + userSelected.getParseUser().getObjectId());
        following = userSelected.getParseUser().getList(User.KEY_FOLLOWING);
        followers = userSelected.getParseUser().getList(User.KEY_FOLLOWERS);
        // empty case
        if (following == null) {
            following = new ArrayList<>();
        } else {
            Log.i(TAG, "Following size: " + following.size());
        }
        if (followers == null) {
            followers = new ArrayList<>();
        } else {
            Log.i(TAG, "Followers: " + followers.size());
        }
        nominator = userSelected.getParseUser().getParseUser("nominator");
    }

    private void loadNominatorProfileImage() {
        ParseFile image = null;
        try {
            image = nominator.fetchIfNeeded().getParseFile("image");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (image != null)
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfileNominator);
    }


}