package com.pepetech.squadhouse.activities;

import android.content.Intent;
import android.nfc.Tag;
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
import com.pepetech.squadhouse.models.User;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.twilio.Twilio;
import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.Application;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.voice.Conference;
import com.twilio.twiml.voice.Dial;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.TwiMLException;

public class RoomActivity extends AppCompatActivity {

    public static final String TAG = "RoomActivity";
    public static final String ACCOUNT_SID = String.valueOf(R.string.ACCOUNT_SID);
    public static final String AUTH_TOKEN = String.valueOf(R.string.AUTH_TOKEN);

    User user;
    Room newRoom;

    Button invite_button;
    Button end_button;
    RecyclerView rvParticipants;
    ParticipantAdapter adapter;
    Button display_button;

    //make room model/class push to back4app
    List<ParseObject> allUsers;
    //create room here, instantiate participant list, in inviteactivity, make calls to back4app
    //to update room.participantList

    Conference conference;
    Dial dial;
    //

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
        user = new User(ParseUser.getCurrentUser());

        setUpRoom();
        queryUsers();
        setOnClickListeners();
        setUpConference();
        connectToConference();
    }

    private void connectToConference() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        /*
        Application application = Application.creator()
                .setVoiceMethod(HttpMethod.GET)
                .setVoiceUrl(URI.create("http://demo.twilio.com/docs/voice.xml"))
                .setFriendlyName("Phone Me")
                .create();

        System.out.println(application.getSid());

        Call call = Call.creator(
                new com.twilio.type.PhoneNumber("user.phoneNumber"),
                new com.twilio.type.PhoneNumber("designated phone number to link to twimlbin as well??"),
                URI.create("twimlbin??"))
                .create();
        */
        
    }

    private void setUpConference() {
        Log.i(TAG, "setting up conference call");

        conference = new Conference.Builder("Room 1234").build();
        dial = new Dial.Builder().conference(conference).build();
        VoiceResponse response = new VoiceResponse.Builder().dial(dial).build();
        try {
            Log.i(TAG, response.toString());
            //Log.i(TAG, response.toXml());
        } catch (TwiMLException e) {
            e.printStackTrace();
        }

        //the above code creates this xml
        /*
        <?xml version="1.0" encoding="UTF-8"?>
            <Response>
              <Dial>
                <Conference>My conference Example</Conference>
              </Dial>
            </Response>
         */

        Log.i(TAG, "finished setting up conference call");
    }

    private void setUpRoom() {
        Log.i(TAG, "initiating room");
        newRoom = new Room();
        newRoom.setTitle("newROom from blah");

        //set the host equal to the current user, getParseUser  returns a ParseUser which
        //will reflect in back4app.com as a pointer to a specific user.
        newRoom.setHost(user.getParseUser());

        newRoom.saveInBackground();
        Log.i(TAG, "finished initiating room");
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

                Log.i(TAG, user.toString());
                Toast.makeText(v.getContext(), "End Button clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
