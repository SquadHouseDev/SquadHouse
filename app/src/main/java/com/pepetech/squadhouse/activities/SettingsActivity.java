package com.pepetech.squadhouse.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
//        setupOnClickListeners();

    }

    private void setupOnClickListeners(){
        btnInterests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), SettingsInterestActivity.class);
                startActivity(i);
            }
        });
    }
}
