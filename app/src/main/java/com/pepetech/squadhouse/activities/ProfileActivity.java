package com.pepetech.squadhouse.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = "ProfileActivity";
    AppCompatImageView ivProfile, ivProfileNominator;
    TextView tvFullName, tvUsername, tvFollowers, tvFollowing, tvBiography, tvUserJoinDate, tvNominator;
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // setup view elements
        // image views
        ivProfile = findViewById(R.id.ivProfile);
        ivProfileNominator = findViewById(R.id.ivProfileNominator);
        // text views
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);
        tvBiography = findViewById(R.id.tvBiography);
        tvUserJoinDate = findViewById(R.id.tvUserJoinDate);
        tvNominator = findViewById(R.id.tvNominator);
        // buttons
        btnLogout = findViewById(R.id.btnLogout);
        setupProfile();
        setupOnClickListeners();
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
    }

    private void setupProfile() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseFile image = user.getParseFile("image");
        if (image != null)
            Glide.with(this.getBaseContext()).load(image.getUrl()).into(ivProfile);
    }

    private void signoutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
    }

    private void goToLoginActivity(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish(); // disable user ability to renavigate after a successful login
    }

}