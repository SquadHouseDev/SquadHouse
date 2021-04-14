package com.pepetech.squadhouse.activities.Following;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Home.adapters.FollowingAdapter;
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
    List<Object> clubsAndUsers;
    RecyclerView rvClubsAndUsers;
    FollowingAdapter viewAdapter;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        ////////////////////////////////////////////////////////////
        // Setup recycler view
        ////////////////////////////////////////////////////////////
        user = new User(ParseUser.getCurrentUser());
        clubsAndUsers = new ArrayList<>();
        setupCollection(this.clubsAndUsers);
        // configure hetero recycler using dummy values
        rvClubsAndUsers = findViewById(R.id.rvClubsAndUsers);
        viewAdapter = new FollowingAdapter(this, clubsAndUsers);
        rvClubsAndUsers.setAdapter(viewAdapter);
        rvClubsAndUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupCollection(List<Object> clubsAndUsers) {
        List<Object> usersFollowed = new ArrayList<>();
        for (ParseObject o : user.getFollowing()) {
            if (o instanceof Club)
                clubsAndUsers.add(o);
            else
                usersFollowed.add(new User((ParseUser) o));
        }
        clubsAndUsers.addAll(usersFollowed);
    }

}
