package com.pepetech.squadhouse.activities.Following;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Following.adapters.FollowingAdapter;
import com.pepetech.squadhouse.models.Club;
import com.pepetech.squadhouse.models.User;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that accepts a wrapped Parser of the target User.
 * The target User's following is displayed using a Hetero Recycler View
 * in the order of Clubs being first followed by Users.
 * Dependencies:
 * <ul>
 *     <li>
 * User user
 *     </li>
 * </ul>
 */
public class FollowingActivity extends AppCompatActivity {
    private static final String TAG = "FollowingActivity";
    List<Object> clubsAndUsers;
    RecyclerView rvClubsAndUsers;
    FollowingAdapter viewAdapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        setTitle("FOLLOWING");
        ////////////////////////////////////////////////////////////
        // Setup recycler view
        ////////////////////////////////////////////////////////////
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        clubsAndUsers = new ArrayList<>();
        // configure hetero recycler using dummy values
        rvClubsAndUsers = findViewById(R.id.rvClubsAndUsers);
        viewAdapter = new FollowingAdapter(this, clubsAndUsers);
        rvClubsAndUsers.setAdapter(viewAdapter);
        rvClubsAndUsers.setLayoutManager(new LinearLayoutManager(this));
        setupCollection(this.clubsAndUsers, user);
    }

    private void setupCollection(List<Object> clubsAndUsers, User user) {
        clubsAndUsers.clear();
        List<Object> usersFollowed = new ArrayList<>();
        for (ParseObject o : user.getFollowing()) {
            if (o instanceof Club) {
                clubsAndUsers.add(o);
                Log.i(TAG, "Added: " + ((Club) o).getName());
            } else {
                User toAdd = new User((ParseUser) o);
                Log.i(TAG, "Added: " + toAdd.getFirstName());
                toAdd.isFollowed = true;
                usersFollowed.add(toAdd);
            }
        }
        clubsAndUsers.addAll(usersFollowed);
        viewAdapter.notifyDataSetChanged();
    }

}
