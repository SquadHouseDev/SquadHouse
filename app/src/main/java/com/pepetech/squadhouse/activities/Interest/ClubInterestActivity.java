package com.pepetech.squadhouse.activities.Interest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Interest.adapters.OuterInterestAdapter;
import com.pepetech.squadhouse.models.Interest;

import java.util.List;

/**
 * Activity allow a User to select a MAX number of
 * Interests for the Club during its creation process. The Activity utilizes
 * the approach for implementing a nested Recycler View using
 * InnerInterestAdapter and OuterInterestAdapter.
 */
public class ClubInterestActivity extends AppCompatActivity {
    OuterInterestAdapter outerAdapter;
    List<List<Interest>> interests;
    RecyclerView rvOuterInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_interest);
        setTitle("NEW CLUB CHOOSE TOPICS");
    }
}