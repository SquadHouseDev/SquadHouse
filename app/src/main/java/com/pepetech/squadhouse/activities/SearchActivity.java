package com.pepetech.squadhouse.activities;

import com.parse.FindCallback;
import com.parse.ParseException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
        adapter = new SearchAdapter(this, allUsers);
        rvElementsFound.setAdapter(adapter);
        rvElementsFound.setLayoutManager(new LinearLayoutManager(this));
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

    void queryByKeywordSearch(){

    }
}