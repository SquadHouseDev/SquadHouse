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

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that routes using a Hetero Recycler View.
 * Items should be displayed in the order of Clubs being first followed by
 * Users.
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
        user = new User(ParseUser.getCurrentUser());
        clubsAndUsers = new ArrayList<>();
//        setupCollection(this.clubsAndUsers);
        // configure hetero recycler using dummy values
        rvClubsAndUsers = findViewById(R.id.rvClubsAndUsers);
        viewAdapter = new FollowingAdapter(this, clubsAndUsers);
        rvClubsAndUsers.setAdapter(viewAdapter);
        rvClubsAndUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupCollection(this.clubsAndUsers);
    }

    private void setupCollection(List<Object> clubsAndUsers) {
        clubsAndUsers.clear();
        List<Object> usersFollowed = new ArrayList<>();
        for (ParseObject o : user.getFollowing()) {
            if (o instanceof Club) {
                clubsAndUsers.add(o);
                Log.i(TAG, ((Club) o).getName());
            }
            else {
                usersFollowed.add(new User((ParseUser) o));
            }
        }
        clubsAndUsers.addAll(usersFollowed);
        viewAdapter.notifyDataSetChanged();
    }

}
