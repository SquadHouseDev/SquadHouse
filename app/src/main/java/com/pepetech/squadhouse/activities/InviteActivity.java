package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.adapters.InviteAdapter;
import com.pepetech.squadhouse.adapters.ParticipantAdapter;
import com.pepetech.squadhouse.models.User;

import java.util.ArrayList;
import java.util.List;

public class InviteActivity extends AppCompatActivity {

    public static final String TAG = "InviteActivity";
    List<Object> followingList;
    ParseUser user;

    Button invite_to_call_button,done_button;
    RecyclerView rvFollowers;
    InviteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        invite_to_call_button = findViewById(R.id.invite_to_call_button);
        done_button = findViewById(R.id.done_button);
        rvFollowers = findViewById(R.id.rvFollowers);

        followingList = new ArrayList<>();
        //queryFollowers();


        adapter = new InviteAdapter(this, followingList);
        rvFollowers.setAdapter(adapter);
        rvFollowers.setLayoutManager(new LinearLayoutManager(this));

//        display_button = findViewById(R.id.display_button);

        setOnClickListeners();

    }

    private void queryFollowers() {
        //check back4app??
        user = ParseUser.getCurrentUser();
        followingList = user.getList("following");

        for(int i = 0; i < followingList.size();i++){
            User example = new User();
            //example.User((ParseUser) followingList.get(i));
            Log.i(TAG, followingList.get(i).toString());
            //Log.i(TAG, ((ParseObject) followingList.get(i)).getString("firstName"));
        }
    }

    private void setOnClickListeners() {

        done_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "done button clicked");
                NavUtils.navigateUpFromSameTask(InviteActivity.this);
            }
        });

    }
}
