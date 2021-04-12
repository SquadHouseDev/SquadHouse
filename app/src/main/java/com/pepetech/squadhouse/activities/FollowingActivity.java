package com.pepetech.squadhouse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pepetech.squadhouse.R;

/**
 * Activity that routes using a Hetero Recycler View.
 * Items should be displayed in the order of Clubs being first followed by
 * Users. 
 */
public class FollowingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
    }
}
