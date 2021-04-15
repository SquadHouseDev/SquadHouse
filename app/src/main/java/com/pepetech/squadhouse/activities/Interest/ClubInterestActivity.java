package com.pepetech.squadhouse.activities.Interest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pepetech.squadhouse.R;

/**
 * Activity allow a User to select a MAX number of
 * Interests for the Club during its creation process. The Activity utilizes
 * the approach for implementing a nested Recycler View using
 * InnerInterestAdapter and OuterInterestAdapter.
 */
public class ClubInterestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_interest);
    }
}