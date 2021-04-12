package com.pepetech.squadhouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import android.content.Intent;
import android.os.Bundle;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ViewMyProfileActivity extends AppCompatActivity {
    ParseUser parseUser;
    User user;
    public static final String TAG = "ProfileActivity";
    AppCompatImageView ivProfile, ivProfileNominator;
    TextView tvFullName, tvUsername, tvFollowersCount, tvFollowingCount, tvBiography, tvUserJoinDate, tvNominatorName, tvTitleText;
    Button btnLogout;
    ImageButton btnSettings;
    ParseObject nominator;
    List<ParseObject> following;
    List<ParseObject> followers;

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
        setContentView(R.layout.activity_view_my_profile);
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        // image views
        ivProfile = findViewById(R.id.ivProfile);
        ivProfileNominator = findViewById(R.id.ivProfileNominator);
        // text views
        tvFullName = findViewById(R.id.tvFullName);
        tvUsername = findViewById(R.id.tvUsername);
        tvFollowersCount = findViewById(R.id.tvFollowersCount);
        tvFollowingCount = findViewById(R.id.tvFollowingCount);
        tvBiography = findViewById(R.id.tvBiography);
        tvUserJoinDate = findViewById(R.id.tvUserJoinDate);
        tvNominatorName = findViewById(R.id.tvNominatorName);
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
        parseUser = ParseUser.getCurrentUser();
        user = new User(parseUser);
        // 1. query profile data
        queryUserProfile();
        // 2. populate profile with queried profile data
        populateProfileElements();

    }

    private void setupOnClickListeners() {
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
                textPrompt = popupView.findViewById(R.id.tvFirstName);
                buttonUpdateName = popupView.findViewById(R.id.updateNameButton);
                createAlias = popupView.findViewById(R.id.createAliasButton);
                cancel = popupView.findViewById(R.id.CancelButton);
                setup_Popup_Window_On_Click_Listeners();

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



            }
        });
    }


    //pop up window buttons
    public void setup_Popup_Window_On_Click_Listeners() {
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
        tvFullName.setText(parseUser.getString(User.KEY_FIRST_NAME) + " " + parseUser.getString(User.KEY_LAST_NAME));
        tvBiography.setText(parseUser.getString(User.KEY_BIOGRAPHY));
        tvUsername.setText("@" + parseUser.getUsername());
        // load following and followers count
        tvFollowersCount.setText(String.valueOf(followers.size()));
        tvFollowingCount.setText(String.valueOf(following.size()));
        // load nominator's profile picture
        if (!user.isSeed() && nominator != null) {
            loadNominatorProfileImage();
            tvNominatorName.setText(nominator.getString(User.KEY_FIRST_NAME));
        }
    }

    private void queryUserProfile() {
        Log.i(TAG, "Querying user profile data");
        parseUser = ParseUser.getCurrentUser();
        Log.i(TAG, "User object_id: " + parseUser.getObjectId());
        following = parseUser.getList(User.KEY_FOLLOWING);
        followers = parseUser.getList(User.KEY_FOLLOWERS);
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
        nominator = user.getNominator();
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

    // TODO: refactor to use a bottom sheet
    private void goToSettingsActivity() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
//        overridePendingTransition(R.anim.slide_to_top, R.anim.slide_to_left);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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
    private void goToUpdateProfileImageActivity()
    {
        Intent i = new Intent(this, UpdateProfileImageActivity.class);
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

}
