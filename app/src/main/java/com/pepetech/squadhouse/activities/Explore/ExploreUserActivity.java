package com.pepetech.squadhouse.activities.Explore;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.FollowersActivity;
import com.pepetech.squadhouse.activities.Following.FollowingActivity;
import com.pepetech.squadhouse.models.Follow;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Activity that allows for navigation to another User's profile for viewing.
 * An example of using this class is as follows:
 * <br>
 * <blockquote>
 * Intent i = new Intent(context, ViewAUserProfileActivity.class);<br>
 * i.putExtra("user", Parcels.wrap(userElement));<br>
 * context.startActivity(i);<br>
 * </blockquote>
 */
public class ExploreUserActivity extends AppCompatActivity {
    ParseUser parseUser;
    User currentUser, userSelected;
    public static final String TAG = "ViewAUserActivity";
    AppCompatImageView ivProfile, ivProfileNominator;
    TextView tvFullName, tvUsername, tvFollowersCount, tvFollowingCount, tvBiography, tvUserJoinDate, tvNominatorName;
    Button btnFollow;
    ImageButton btnSettings;
    ParseUser nominator;
    Follow follow;
    List<ParseObject> following;
    List<User> followers;
    ConstraintLayout clFollowing, clFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_user_profile);

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
        // constraint layouts
        clFollowers = findViewById(R.id.clFollowers);
        clFollowing = findViewById(R.id.clFollowing);
        // buttons
        btnFollow = findViewById(R.id.btnFollow);

        ////////////////////////////////////////////////////////////
        // Setting up selected User's profile
        ////////////////////////////////////////////////////////////
        follow = null;
        followers = new ArrayList<>();
        // unwrap passed User
        userSelected = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        parseUser = ParseUser.getCurrentUser();
        currentUser = new User(parseUser);
        for (ParseObject o: currentUser.getFollowing()){
            if (o.getObjectId().equals(userSelected.getParseUser().getObjectId())){
                userSelected.isFollowed = true;
                break;
            }
        }
        // query profile data
        queryUserProfile();
        // populate profile with queried profile data
        populateProfileElements();
        setupOnClickListeners(userSelected);
    }

    private void setupOnClickListeners(User userSelected) {
        ivProfileNominator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Nominator profile clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Nominator profile clicked!");
                if (nominator != null) {
                    Intent i = new Intent(v.getContext(), ExploreUserActivity.class);
                    User toPass = new User(nominator);
                    i.putExtra("user", Parcels.wrap(toPass));
                    startActivity(i);
                }
            }
        });

        clFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Followers clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Followers clicked!");
                // setup for routing to the next activity
                Intent i = new Intent(v.getContext(), FollowersActivity.class);
                // wrap and pass the user that was selected
                User toPass = userSelected;
                i.putExtra("user", Parcels.wrap(toPass));
                startActivity(i);
            }
        });
        clFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Followers clicked!", Toast.LENGTH_SHORT).show();;
                Log.i(TAG, "Following clicked!");
                Intent i = new Intent(v.getContext(), FollowingActivity.class);
                User toPass = new User(nominator);
                i.putExtra("user", Parcels.wrap(toPass));
                startActivity(i);
            }
        });
        // TODO navigate to an activity for viewing a larger image
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Nominator profile clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "User profile clicked!");
//                ParseObject nominator = user.getNominator()
//                goToProfileActivity();
            }
        });
        tvFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Fullname clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Fullname clicked!");
            }
        });
        tvBiography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Biography clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Biography clicked!");
            }
        });

        configureUserSelection(userSelected);
    }

    private void configureUserSelection(User userElement) {
        Log.i(TAG, "Configuring interest for " + userElement.getFirstName() + " such that selection: " + userElement.isFollowed);
        if (userElement.isFollowed) {
            // apply background to show pre-existing selection
            btnFollow.setText("Following");
            handleFollowToggle(userElement);

        } else {
            // apply background to show no existing selection
            btnFollow.setText("Follow");
            handleFollowToggle(userElement);
        }
    }

    private void handleFollowToggle(User userElement) {
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on interest toggle and current not select --> user has toggled to add the interest to list
                if (!userElement.isFollowed) {
                    // safety check for interest existing
                    currentUser.follow(userElement.getParseUser());
                    // update interest state to be selected
                    follow = new Follow();
                    follow.put(Follow.KEY_TO, userElement.getParseUser());
                    follow.put(Follow.KEY_FROM, currentUser.getParseUser());
                    follow.saveInBackground();
                    userElement.isFollowed = true;
                    // update GUI to show selection
                    btnFollow.setText("Following");
                }
                // on interest toggle and currently selected --> user has toggled to remove the interest to list
                else {
                    btnFollow.setText("Follow");
                    if (follow != null)
                        follow.deleteInBackground();
                    currentUser.unfollow(userElement.getParseUser());
                    userElement.isFollowed = false;
                }
//                notifyDataSetChanged();
            }
        });
    }

    private void populateProfileElements() {
        userSelected = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        Log.i(TAG, "Populating profile elements");
        // load user's profile picture
        ParseFile image = userSelected.getParseUser().getParseFile(User.KEY_IMAGE);
        if (image != null)
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfile);
        // load profile text information
        tvFullName.setText(userSelected.getParseUser().getString(User.KEY_FIRST_NAME) + " " + userSelected.getParseUser().getString(User.KEY_LAST_NAME));
        tvBiography.setText(userSelected.getParseUser().getString(User.KEY_BIOGRAPHY));
        tvUsername.setText("@" + userSelected.getParseUser().getUsername());
        // load following and followers count
        tvFollowersCount.setText(String.valueOf(userSelected.getFollowerCount())); // DEBUG
        tvFollowingCount.setText(String.valueOf(userSelected.getFollowing().size())); // DEBUG
        // load nominator's profile picture
        if (!userSelected.isSeed()) {
            loadNominatorProfileImage();
            tvNominatorName.setText(nominator.getString(User.KEY_FIRST_NAME));
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_sprout)
                    .fitCenter()
                    .circleCrop()
                    .into(ivProfileNominator);
        }
    }

    private void queryUserProfile() {
        Log.i(TAG, "Querying user profile data");
        parseUser = ParseUser.getCurrentUser();
        Log.i(TAG, "User object_id: " + userSelected.getParseUser().getObjectId());
        following = userSelected.getParseUser().getList(User.KEY_FOLLOWING);
//        followers = userSelected.getParseUser().getList(User.KEY_FOLLOWERS);

        // empty case
        if (following == null) {
            following = new ArrayList<>();
        } else {
            Log.i(TAG, "Following size: " + following.size());
        }
        nominator = userSelected.getParseUser().getParseUser("nominator");
    }

    private void loadNominatorProfileImage() {
        ParseFile image = null;
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

    /**
     * Code for usage in the view User profile
     * Query for all ParseObjects that are followers of the input current User
     * Usage of this function requires central collection variable
     *
     * @param targetUser User of interest when querying for followers
     * @param adapter    Adapter to be notified of data collection changes
     */
    void queryFollowers(User targetUser, RecyclerView.Adapter adapter, List<ParseUser> collection) {
        Log.i(TAG, "queryFollowers");
        collection.clear();
        ParseQuery<Follow> mainQuery = new ParseQuery<Follow>(Follow.class);
        mainQuery.whereEqualTo(Follow.KEY_TO, targetUser.getParseUser().getObjectId());
        mainQuery.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> objects, ParseException e) {
                Log.i(TAG, String.valueOf(objects.size()) + " followings");
                if (e == null) {
                    // iterate over all Follow entries
                    for (Follow c : objects) {
                        // compare
                        Log.i(TAG, c.getFollowTo().getObjectId());
                        collection.add((ParseUser) c.getFollowFrom());
                    }
                } else {
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}