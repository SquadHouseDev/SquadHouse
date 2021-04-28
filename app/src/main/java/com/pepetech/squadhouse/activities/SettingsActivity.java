package com.pepetech.squadhouse.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Interest.MyInterestActivity;

public class SettingsActivity extends AppCompatActivity {

    Button btnAccount, btnInterests, btnWhatsNew, btnFAQ, btnGuidelines, btnTOS, btnPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("SETTINGS");
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        btnAccount = findViewById(R.id.btnAccount);
        btnInterests = findViewById(R.id.btnInterests);
        btnWhatsNew = findViewById(R.id.btnWhatsNew);
        btnFAQ = findViewById(R.id.btnFAQ);
        btnGuidelines = findViewById(R.id.btnGuidelines);
        btnTOS = findViewById(R.id.btnTOS);
        btnPrivacyPolicy = findViewById(R.id.btnPrivacyPolicy);
        ////////////////////////////////////////////////////////////
        // Setup button listeners
        ////////////////////////////////////////////////////////////
        setupOnClickListeners();
    }

    private void setupOnClickListeners() {
        btnInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MyInterestActivity.class);
                startActivity(i);
            }
        });
        btnGuidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("https://squadhousedev.github.io/Community/");
            }
        });
        btnTOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("https://squadhousedev.github.io/TOS/");
            }
        });
        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("https://squadhousedev.github.io/Privacy/");
            }
        });
        btnFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebURL("https://squadhousedev.github.io/FAQ/");
            }
        });

    }

    public void openWebURL(String inURL) {
        Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(inURL));
        startActivity(browse);
    }


}
