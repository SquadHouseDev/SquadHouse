package com.pepetech.squadhouse.activities.Club;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Club.fragments.ClubPhotoUploadBottomSheetDialog;
import com.pepetech.squadhouse.activities.Interest.ClubInterestActivity;

public class ClubCreateActivity extends AppCompatActivity {
    public static final String TAG = "ClubCreateActivity";
    ImageView ivClubImage;
    EditText etClubName, etDescription;
    SwitchCompat switchAllowFollowers, switchAllowMembersStartRoom, switchMemberListPrivate;
    ConstraintLayout clNavigateInterestTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_create);
        setTitle("NEW CLUB");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_outline_cancel_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivClubImage = findViewById(R.id.ivClubImage);
        etClubName = findViewById(R.id.etClubName);
        etDescription = findViewById(R.id.etDescription);
        switchAllowFollowers = findViewById(R.id.switchAllowFollowers);
        switchAllowMembersStartRoom = findViewById(R.id.switchAllowMembersStartRoom);
        switchMemberListPrivate = findViewById(R.id.switchMemberListPrivate);
        clNavigateInterestTopics = findViewById(R.id.clNavigateInterestTopics);
        //-----------------------------------------------------------------------------
        setupOnClickListeners();
    }

    void setupOnClickListeners() {
        // setup on tap for club image routing to a bottom sheet for image selection
        ivClubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClubPhotoUploadBottomSheetDialog bottomSheet = new ClubPhotoUploadBottomSheetDialog();
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });
        // ----------------------------------------------------------------------
        // setup on tap for nagivation to an activity for selecting 3 club interests
        clNavigateInterestTopics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ClubInterestActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        // ----------------------------------------------------------------------

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done_cancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            // overriden to be the cancel button
            case android.R.id.home:
//                Toast.makeText(this, "Cancel..!!", Toast.LENGTH_SHORT).show();
                finish();
                return true;

            case R.id.action_done:
//                Toast.makeText(this, "Done..!!", Toast.LENGTH_SHORT).show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}