package com.pepetech.squadhouse.activities.Explore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.Interest;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Activity that allows for navigation to a Club's profile for viewing.
 * An example of using this class is as follows:
 * <br>
 * <blockquote>
 * Intent i = new Intent(context, ExploreClubActivity.class);<br>
 * i.putExtra("club", Parcels.wrap(clubElement));<br>
 * context.startActivity(i);<br>
 * </blockquote>
 */
public class ExploreClubActivity extends AppCompatActivity {
    //    public static final String TAG = ExploreAClubActivity.class.getName();
    public static final String TAG = "ExploreAClubActivity";
    ConstraintLayout clFollowers, clFollowing;
    TextView tvClubName, tvClubDescription, tvInterests, tvMembers;
    LinearLayout llExploreClub;
    Button btnClubFollow;
    ImageView ivClubImage;
    Club club;
    boolean wasFollowed;
    private ParseUser parseUser;
    User currentUser;
    List<Interest> interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_club_profile);

        ////////////////////////////////////////////////////////////
        // Initialize club page variables
        ////////////////////////////////////////////////////////////
        tvClubDescription = findViewById(R.id.tvClubDescription);
        tvInterests = findViewById(R.id.tvInterests);
        tvClubName = findViewById(R.id.tvClubName);
        llExploreClub = findViewById(R.id.llExploreClub);
        tvMembers = findViewById(R.id.tvMembers);
        btnClubFollow = findViewById(R.id.btnClubFollow);
        ivClubImage = findViewById(R.id.ivClubImage);
        clFollowers = findViewById(R.id.clFollowers);
        clFollowing = findViewById(R.id.clFollowing);

        ////////////////////////////////////////////////////////////
        // Query club info and populate
        ////////////////////////////////////////////////////////////
        // unwrap pass Club
        club = Parcels.unwrap(getIntent().getParcelableExtra("club"));
        parseUser = ParseUser.getCurrentUser();
        currentUser = new User(parseUser);
        interests = new ArrayList<>();
        wasFollowed = false;
        queryAndPopulate();
        setupOnClickListeners();
    }

    /**
     * Main method for handling button setup
     */
    private void setupOnClickListeners() {
        if (isClubFollowedByUser(currentUser.getParseUser(), club)) {
            Log.i(TAG, club.getName() + " is currently followed by " + (new User(parseUser)).getFirstName());
            btnClubFollow.setText("Following");
            setupCurrentlyFollowingButton(club);
        } else {
            Log.i(TAG, club.getName() + " is not currently followed by " + (new User(parseUser)).getFirstName());
            setupDefaultFollowButton(club);
        }
    }

    /**
     * Method for validating if the input Club is currently followed by input User
     *
     * @param user
     * @param club
     * @return
     */
    boolean isClubFollowedByUser(ParseUser user, Club club) {
        boolean rv = false;
        for (ParseObject u : club.getFollowers()) {
            Log.i(TAG, "User: " + user.getObjectId() + "FollowerId: " + u.getObjectId());
            if (user.getObjectId().equals(u.getObjectId())) {
//                Log.i(TAG, "User: " + user.getObjectId() + "FollowerId: " + u.getObjectId());
                rv = true;
            }
        }
        return rv;
    }

    /**
     * Current User is not following the Club therefore configure
     * buttons to reflect the default case of encouraging the User to follow.
     *
     * @param clubElement
     */
    private void setupDefaultFollowButton(Club clubElement) {
        btnClubFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Follow button clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Follow button clicked!");
                // toggle button follow
                if (wasFollowed) {
                    wasFollowed = false;
                    btnClubFollow.setText("Follow");
                    // apply unfollow from the user by removing the club to the user
                    currentUser.unfollow(clubElement);
                    currentUser.getParseUser().saveInBackground();
                    // reflect the unfollow from the user by removing user from the club
                    club.removeFollower(currentUser.getParseUser());
                    club.saveInBackground();
                } else {
                    wasFollowed = true;
                    btnClubFollow.setText("Following");
                    // apply follow from the user by adding the club to the user
                    currentUser.follow(clubElement);
                    currentUser.getParseUser().saveInBackground();
                    // reflect the new follower by adding the user to the club
                    club.addFollower(currentUser.getParseUser());
                    club.saveInBackground();

                }
            }
        });
    }

    /**
     * Configuration of follow button when current user is already following
     * the Club
     *
     * @param clubElement
     */
    private void setupCurrentlyFollowingButton(Club clubElement) {
        btnClubFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Follow button clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Follow button clicked!");
                // toggle button follow
                if (wasFollowed) {
                    wasFollowed = false;
                    btnClubFollow.setText("Following");
                    // apply follow from the user by adding the club to the user
                    currentUser.follow(clubElement);
                    currentUser.getParseUser().saveInBackground();
                    // reflect the new follower by adding the user to the club
                    club.addFollower(currentUser.getParseUser());
                    club.saveInBackground();
                }
                // toggle button to unfollow
                else {
                    wasFollowed = true;
                    btnClubFollow.setText("Follow");
                    // apply unfollow from the user by removing the club to the user
                    currentUser.unfollow(clubElement);
                    currentUser.getParseUser().saveInBackground();
                    // reflect the unfollow from the user by removing user from the club
                    club.removeFollower(currentUser.getParseUser());
                    club.saveInBackground();
                }
            }
        });
    }

    /**
     *
     */
    private void queryAndPopulate() {
        Log.i(TAG, "queryAndPopulate call");
        // query and populate interests text
        interests = (List<Interest>) club.getInterests();
        String interestStr = "";
        for (int i = 0; i < interests.size(); i++) {
            interestStr += interests.get(i).getArchetypeEmoji() + interests.get(i).getName();
            if (i < interests.size() - 1)
                interestStr += " · ";
        }
        tvInterests.setText(interestStr);
        // populate description text
        tvClubDescription.setText(club.getDescription());
        // populate name text
        tvClubName.setText(club.getName());
        // query and populate image
        ParseFile image = club.getImage();
        if (image != null)
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivClubImage);
        // query and populate member text
        int memberCount = club.getMembers().size();
        if (memberCount > 1) {
            // TODO: apply processing for shortening large number values ie 1,000 ==> 1k
            tvMembers.setText(String.valueOf(memberCount) + " Members");
        } else {
            tvMembers.setText(String.valueOf(memberCount) + " Member");
        }

        List<ParseUser> members = club.getMembers();
        Log.i(TAG, "members: " + members.size());
        for (ParseUser m : members) {
            User u = new User(m);
            Log.i(TAG, u.getBiography());
            Log.i(TAG, u.getFirstName());
            Log.i(TAG, u.getLastName());
//            Log.i(TAG, (String) m.getString("username"));
        }
    }


}
