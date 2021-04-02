package com.pepetech.squadhouse.activities;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.adapters.HomeFeedAdapter;
import com.pepetech.squadhouse.adapters.SearchAdapter;
import com.pepetech.squadhouse.models.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.opengl.ETC1;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    public static final String TAG = "SearchActivity";
    List<User> allUsers;
    TextView tvElementsLabel;
    RecyclerView rvElementsFound;
    EditText etSearch;
    Button btnSearch;
    SearchAdapter adapter;
    List<User> users;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        currentUser = new User();
        currentUser.User(ParseUser.getCurrentUser());
        users = new ArrayList<>();
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        tvElementsLabel = findViewById(R.id.tvElementsLabel);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        setupOnClickListeners();
        ////////////////////////////////////////////////////////////
        // Setup recycler view
        ////////////////////////////////////////////////////////////
        allUsers = new ArrayList<>();
        rvElementsFound = findViewById(R.id.rvElementsFound);
        adapter = new SearchAdapter(this, allUsers, currentUser);
        rvElementsFound.setAdapter(adapter);
        rvElementsFound.setLayoutManager(new LinearLayoutManager(this));
        queryFollowing();

    }

    private void setupOnClickListeners() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Search button clicked!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Search button clicked!");
                queryAllUsers(); // DEBUG
            }
        });
    }

    /**
     * Test function for loading all users available to be followed
     */
    void queryAllUsers() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (ParseUser u : users) {
                    User converted = new User();
                    converted.User(u);
                    allUsers.add(converted);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    void queryFollowing(){
        List<String> followingIds = ParseUser.getCurrentUser().getList("following");

        if(!followingIds.isEmpty()){
            for (String id: followingIds) {
                Log.i(TAG, id);
                queryUser(id);
            }
        }
    }

    void queryUser(String id){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", id);
//        final ParseUser[] rv = {null};
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser awesome, ParseException e) {
                if (e == null) {
//                    rv[0] = awesome;
                    User u = new User();
                    u.User(awesome);
                    Log.i(TAG, u.getLastName() + u.getFirstName() + u.getBiography() + u.getPhoneNumber() );
                    users.add(u);
                    Log.i(TAG, String.valueOf(users.size()));
                    Log.i(TAG, String.valueOf(users));
                    Log.i(TAG, "Proceed to populate from here or notify that adapter that the data has changed");
//                    onAwesome(awesome, post, itemView);
                } else {
//                    onAwesome(null, post, itemView);
                }
            }
        });

    }

    void queryByKeywordSearch(){

    }
}