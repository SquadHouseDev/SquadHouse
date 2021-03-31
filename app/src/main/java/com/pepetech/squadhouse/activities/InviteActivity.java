package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.adapters.ParticipantAdapter;

import java.util.ArrayList;

public class InviteActivity extends AppCompatActivity {

    public static final String TAG = "InviteActivity";
    ArrayList<ParseUser> followingList;
    ParseUser user;

    Button invite_to_call_button,done_button;
    RecyclerView rvFollowers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        invite_to_call_button = findViewById(R.id.invite_to_call_button);
        done_button = findViewById(R.id.done_button);
        rvFollowers = findViewById(R.id.rvFollowers);

        followingList = new ArrayList<>();
        
        adapter = new ParticipantAdapter(this, allUsers);
        rvParticipants.setAdapter(adapter);
        rvParticipants.setLayoutManager(new LinearLayoutManager(this));

        display_button = findViewById(R.id.display_button);

        setOnClickListeners();

    }

    private void setOnClickListeners() {
    }
}
