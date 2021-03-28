package com.pepetech.squadhouse.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    AppCompatImageView ivProfile, ivProfileNominator;
    TextView tvFullName, tvUsername, tvFollowers, tvFollowing, tvBiography, tvUserJoinDate, tvNominator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // setup view elements
        ivProfile = findViewById(R.id.ivProfile);
        ivProfileNominator = findViewById(R.id.ivProfileNominator);
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);
        tvBiography = findViewById(R.id.tvBiography);
        tvUserJoinDate = findViewById(R.id.tvUserJoinDate);
        tvNominator = findViewById(R.id.tvNominator);
        setupProfile();
    }

    private void setupProfile() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseFile image = user.getParseFile("image");
        if (image != null)
            Glide.with(this.getBaseContext()).load(image.getUrl()).into(ivProfile);
    }


}