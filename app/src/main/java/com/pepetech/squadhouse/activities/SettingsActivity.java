package com.pepetech.squadhouse.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pepetech.squadhouse.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("SETTINGS");
    }
}