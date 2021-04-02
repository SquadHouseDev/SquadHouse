package com.pepetech.squadhouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.adapters.HomeFeedAdapter;
import com.pepetech.squadhouse.adapters.ParticipantAdapter;
import com.pepetech.squadhouse.models.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity extends AppCompatActivity {

    public static final String TAG = "RoomActivity";

    ParseUser user;

    Button invite_button;
    Button end_button;
    RecyclerView rvParticipants;
    ParticipantAdapter adapter;

    Button display_button;

    //make room model/class push to back4app
    List<ParseObject> allUsers;
    //create room here, instantiate participant list, in inviteactivity, make calls to back4app
    //to update room.participantList
    Room newRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        invite_button = findViewById(R.id.invite_button);
        end_button = findViewById(R.id.end_button);
        rvParticipants = findViewById(R.id.rvParticipants);

        allUsers = new ArrayList<>();
        rvParticipants = findViewById(R.id.rvParticipants);
        adapter = new ParticipantAdapter(this, allUsers);
        rvParticipants.setAdapter(adapter);
        rvParticipants.setLayoutManager(new LinearLayoutManager(this));

        display_button = findViewById(R.id.display_button);

        setUpRoom();
        queryUsers();

        setOnClickListeners();
    }

    private void setUpRoom() {
        newRoom = new Room();


    }

    private void queryUsers() {

        //based on room.participantList, display ppl with adapter??
    }

    private void setOnClickListeners() {
        invite_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "invite button clicked");
                Toast.makeText(v.getContext(), "Invite clicked!", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(RoomActivity.this, InviteActivity.class);
                startActivity(i);
            }
        });

        end_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "end button clicked");
                Toast.makeText(v.getContext(), "End Button clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        display_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "display button clicked");

                user = ParseUser.getCurrentUser();
                Log.i(TAG, user.toString());
                Toast.makeText(v.getContext(), "End Button clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
