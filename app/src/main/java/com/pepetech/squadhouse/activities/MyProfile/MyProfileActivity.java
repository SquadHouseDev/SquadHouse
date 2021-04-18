package com.pepetech.squadhouse.activities.MyProfile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Explore.ExploreUserActivity;
import com.pepetech.squadhouse.activities.FollowersActivity;
import com.pepetech.squadhouse.activities.Following.FollowingActivity;
import com.pepetech.squadhouse.activities.Login.LoginActivity;
import com.pepetech.squadhouse.activities.MyProfile.helpers.BiographyBottomSheetDialog;
import com.pepetech.squadhouse.activities.MyProfile.helpers.UpdateFullNameActivity;
import com.pepetech.squadhouse.activities.MyProfile.helpers.UpdateProfileImageActivity;
import com.pepetech.squadhouse.activities.MyProfile.helpers.UpdateUsernameActivity;
import com.pepetech.squadhouse.activities.Settings.SettingsActivity;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyProfileActivity extends AppCompatActivity {
    ParseUser parseUser;
    User currentUser;
    public static final String TAG = "MyProfileActivity";
    AppCompatImageView ivProfile, ivProfileNominator;
    TextView tvFullName, tvUsername, tvFollowersCount, tvFollowingCount,
            tvBiography, tvUserJoinDate, tvNominatorName, tvTitleText, tvFollowersLabel, tvFollowingLabel;
    Button btnLogout;
    ImageButton btnSettings;
    ParseObject nominator;
    List<ParseObject> following;
    List<ParseObject> followers;
    List<Club> clubs;
    ConstraintLayout clFollowing, clFollowers;
    RecyclerView rvClubIcons;

    Button buttonUpdateName;
    Button createAlias;
    Button cancel;
    //text views
    TextView textPrompt;
    PopupWindow pw = null;

//    ScrollView svProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("PROFILE");
        setContentView(R.layout.activity_my_profile);
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        // image views
        ivProfile = findViewById(R.id.ivProfile);
        ivProfileNominator = findViewById(R.id.ivProfileNominator);
        // text views
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        clFollowing = findViewById(R.id.clFollowing);
        tvFollowersLabel = findViewById(R.id.tvFollowersLabel);
        tvFollowingLabel = findViewById(R.id.tvFollowingLabel);
        clFollowers = findViewById(R.id.clFollowers);
        tvFollowersCount = findViewById(R.id.tvFollowersCount);
        tvFollowingCount = findViewById(R.id.tvFollowingCount);
        tvBiography = findViewById(R.id.tvBiography);
        tvUserJoinDate = findViewById(R.id.tvUserJoinDate);
        tvNominatorName = findViewById(R.id.tvNominatorName);
        rvClubIcons = findViewById(R.id.rvClubIcons);
        ////////////////////////////////////////////////////////////
        // Setup buttons
        ////////////////////////////////////////////////////////////
        btnLogout = findViewById(R.id.btnLogout);
        btnSettings = findViewById(R.id.btnSettings);
//        msgBttn = findViewById(R.id.msgBttn);
        setupOnClickListeners();
        ////////////////////////////////////////////////////////////
        // setting up user profile
        ////////////////////////////////////////////////////////////
        refreshMyFollowerCount();
        parseUser = ParseUser.getCurrentUser();
        currentUser = new User(parseUser);
        // 1. query profile data
        queryUserProfile();
        // 2. populate profile with queried profile data
        populateProfileElements();
        clubs = new ArrayList<>();
        MyClubsAdapter clubsAdapter = new MyClubsAdapter(this, clubs);
        GridLayoutManager layoutManager = new GridLayoutManager(
                this, 1, GridLayoutManager.HORIZONTAL, false);
        rvClubIcons.setAdapter(clubsAdapter);
        rvClubIcons.setLayoutManager(layoutManager);

    }

    /**
     * Main method used for setting up elements in need of on-click listeners.
     */
    private void setupOnClickListeners() {

        clFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Followers clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Followers clicked!");
                // Navigate to FollowersActivity
                goToFollowersActivity(currentUser);
                currentUser = new User(ParseUser.getCurrentUser());
            }
        });

        clFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Following clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Following clicked!");
                // Navigate to FollowingActivity
                goToFollowingActivity(currentUser);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Sign out button clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Sign out button clicked!");
                signoutUser();
                goToLoginActivity();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Settings button clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Settings button clicked!");
                // TODO: Call a bottom sheet here
                goToSettingsActivity();
            }
        });
        // TODO
        ivProfileNominator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Nominator profile clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Nominator profile clicked!");
                if (nominator != null) {

                    User toPass = new User((ParseUser) nominator);
                    Intent i = new Intent(v.getContext(), ExploreUserActivity.class);
                    i.putExtra("user", Parcels.wrap(toPass));
                    startActivity(i);
                }
//                ParseObject nominator = user.getNominator()
//                goToProfileActivity();
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
                goToUpdateProfileImageActivity();
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
                goToUpdateUsernameActivity();


            }
        });
        // TODO
        tvFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Fullname clicked!", Toast.LENGTH_SHORT);
                t.show();
                //inflate the layout
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.activity_popup_window, null);
                //intialize the size of our layout
                int width = LinearLayout.LayoutParams.MATCH_PARENT;
                int height = LinearLayout.LayoutParams.MATCH_PARENT;

                boolean focusable = true;
                pw = new PopupWindow(popupView, width, height, focusable);

                pw.showAtLocation(v, Gravity.CENTER, 0, 0);
                //INITIALIZE THE ELEMENTS OF OUR WINDOW
                textPrompt = popupView.findViewById(R.id.tvPrompt);
                buttonUpdateName = popupView.findViewById(R.id.updateNameButton);
                createAlias = popupView.findViewById(R.id.createAliasButton);
                cancel = popupView.findViewById(R.id.CancelButton);
                setupPopupWindowOnClickListeners();

                //if tapped outside, dismiss popup window
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        pw.dismiss();
                        return true;
                    }
                });


            }
        });
        // TODO
        tvBiography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Biography clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Biography clicked!");
                goToUpdateBiographyActivity();
            }
        });
    }

    private void goToFollowingActivity(User userToPass) {
        Intent i = new Intent(this, FollowingActivity.class);
        i.putExtra("user", Parcels.wrap(userToPass));
        startActivity(i);
    }

    //pop up window buttons
    public void setupPopupWindowOnClickListeners() {
        buttonUpdateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "UPDATE BUTTON CLICKED");
                goToUpdateFullNameActivity();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        createAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    /**
     * Main method called when populating all elements in MyProfileActivity
     */
    private void populateProfileElements() {
        Log.i(TAG, "Populating profile elements");
        // load user's profile picture
        ParseFile image = parseUser.getParseFile(User.KEY_IMAGE);
        if (image != null)
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfile);

        // load profile text information
        tvFullName.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        tvBiography.setText(currentUser.getBiography());
        tvUsername.setText("@" + parseUser.getUsername());

        // load following and followers count
        int followingCount, followerCount;
        followerCount = currentUser.getFollowerCount();
        followingCount = currentUser.getFollowing().size();
        if (followerCount > 1) {
            tvFollowersLabel.setText("follower");
        } else {
            tvFollowersLabel.setText("followers");
        }
        tvFollowersCount.setText(String.valueOf(followerCount));
        tvFollowingCount.setText(String.valueOf(followingCount));

        // load nominator's profile picture
        if (!currentUser.isSeed() && nominator != null) {
            loadNominatorProfileImage();
            tvNominatorName.setText(nominator.getString(User.KEY_FIRST_NAME));
        }

        // load clubs
        this.clubs = currentUser.getClubs();
        if (!clubs.contains(null)) {
            clubs.add(null);
        }
    }

    private void queryUserProfile() {
        Log.i(TAG, "Querying user profile data");
        parseUser = ParseUser.getCurrentUser();
        Log.i(TAG, "User object_id: " + parseUser.getObjectId());
        following = parseUser.getList(User.KEY_FOLLOWING);
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
        nominator = currentUser.getNominator();
    }

    private void refreshMyFollowerCount() {
        Log.i(TAG, "refreshMyFollowerCount");
        Log.i(TAG, "Target: " + ParseUser.getCurrentUser().getObjectId());
        List<Follow> collection = new ArrayList<>();
        ParseQuery<Follow> mainQuery = new ParseQuery<Follow>(Follow.class);
        // fina all Users following targetUser
        mainQuery.whereEqualTo(Follow.KEY_TO, ParseUser.getCurrentUser());
//        mainQuery.whereNotEqualTo(Follow.KEY_FROM, targetUser.getParseUser());
        mainQuery.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> follows, ParseException e) {
                Log.i(TAG, String.valueOf(follows.size()) + " followings");
                if (e == null) {
                    // for each follower check if the follower is followed by the current user
                    collection.addAll(follows);
                    ParseUser.getCurrentUser().put(User.KEY_FOLLOWER_COUNT, collection.size());
                    ParseUser.getCurrentUser().saveInBackground();
                } else {
                }
            }
        });
    }

    private void signoutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
    }

    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish(); // disable user ability to renavigate after a successful login
    }

    private void goToSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
//        overridePendingTransition(R.anim.slide_to_top, R.anim.slide_to_left);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    // TODO: broken, doesn't properly reflect changes after an update from the bottom sheet
    private void goToUpdateBiographyActivity() {
//        BottomSheetDialog updateBio
        BiographyBottomSheetDialog bottomSheet = new BiographyBottomSheetDialog();
        bottomSheet.show(getSupportFragmentManager(),
                "ModalBottomSheet");
    }

    private void goToUpdateFullNameActivity() {
        Intent i = new Intent(this, UpdateFullNameActivity.class);
        startActivity(i);
        //finish() ?
    }

    private void goToUpdateUsernameActivity() {
        Intent i = new Intent(this, UpdateUsernameActivity.class);
        startActivity(i);
        //finish() ?
    }

    private void goToUpdateProfileImageActivity() {
        Intent i = new Intent(this, UpdateProfileImageActivity.class);
        startActivity(i);
    }

    /**
     * Helper function for navigating to the
     *
     * @param userToPass User to be Parcels wrapped and passed
     */
    private void goToFollowersActivity(User userToPass) {
        Intent i = new Intent(this, FollowersActivity.class);
//        toPass = currentUser;
        i.putExtra("user", Parcels.wrap(userToPass));
        startActivity(i);
    }

    private void loadNominatorProfileImage() {
        ParseFile image = null;
        if (nominator == null)
            return;
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

    public void updateBiographyText(String biography){
        tvBiography.setText(biography);
    }

    public void updateProfileImage(File image){

    }
}
