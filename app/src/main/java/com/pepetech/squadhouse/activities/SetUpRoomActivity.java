package com.pepetech.squadhouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Room;
import com.pepetech.squadhouse.models.RoomRoute;

import java.util.ArrayList;
import java.util.List;

public class SetUpRoomActivity extends AppCompatActivity {
    public static final String TAG = "SetUpRoomActivity";

    EditText roomTitleEt;
    EditText roomDescriptionEt;
    Button startRoomButton;

    List<RoomRoute> allRoutes;

    Room newRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_room);

        roomTitleEt = findViewById(R.id.roomTitleEt);
        roomDescriptionEt = findViewById(R.id.roomDescriptionEt);
        startRoomButton = findViewById(R.id.startRoomButton);

        allRoutes = new ArrayList<>();

        setUpOnClickListeners();
    }

    private void setUpOnClickListeners() {
        startRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allRoutes.clear();
                ParseQuery<RoomRoute> mainQuery = new ParseQuery<RoomRoute>(RoomRoute.class);
                mainQuery.whereEqualTo(RoomRoute.KEY_IS_AVAILABLE, true);
                mainQuery.findInBackground(new FindCallback<RoomRoute>() {
                    @Override
                    public void done(List<RoomRoute> routesFound, ParseException e) {
                       if (e == null) {
                           allRoutes.addAll(routesFound);
                           Log.i(TAG, String.valueOf(allRoutes.size()) + " routes found");
                           String toDisplay = "";
                           for (RoomRoute r : routesFound) {
                               Log.i(TAG, r.getPhoneNumber());
                               toDisplay += r.getPhoneNumber();
                               toDisplay += "\n";
                           }
                           Toast.makeText(getBaseContext(), toDisplay, Toast.LENGTH_SHORT).show();

                           //pasted stuff
                           if(!allRoutes.isEmpty()){
                               Intent i = new Intent(SetUpRoomActivity.this, RoomActivity.class);

                               //create and post room
                               newRoom = new Room();
                               newRoom.setTitle(String.valueOf(roomTitleEt.getText()));
                               newRoom.setDescription(String.valueOf(roomDescriptionEt.getText()));

                               //set the host equal to the current user, getParseUser  returns a ParseUser which
                               //will reflect in back4app.com as a pointer to a specific user.
                               newRoom.setHost(ParseUser.getCurrentUser());
                               newRoom.setPhoneNumber(allRoutes.get(0).getPhoneNumber());
                               Log.i(TAG, allRoutes.get(0).getPhoneNumber());
                               newRoom.saveInBackground();

                               //set room availability to false
                               allRoutes.get(0).lockNumber();

                               i.putExtra("Room", newRoom);
                               startActivity(i);
                           }
                           else{
                               Toast.makeText(getBaseContext(), "Error creating room", Toast.LENGTH_SHORT).show();
                           }
                       } else {
                           // add handling if there are issues with querying
                           Log.i(TAG, "Error querying routes");
                       }
                    }
                });
                //Log.i(TAG, String.valueOf(allRoutes.isEmpty()));
            }
        });
    }

    public void queryAvailableRoutes(List<RoomRoute> allRoutes) {
        allRoutes.clear();
        ParseQuery<RoomRoute> mainQuery = new ParseQuery<RoomRoute>(RoomRoute.class);
        mainQuery.whereEqualTo(RoomRoute.KEY_IS_AVAILABLE, true);
        mainQuery.findInBackground(new FindCallback<RoomRoute>() {
            @Override
            public void done(List<RoomRoute> routesFound, ParseException e) {
                if (e == null) {
                    allRoutes.addAll(routesFound);
                    Log.i(TAG, String.valueOf(allRoutes.size()) + " routes found");
                    String toDisplay = "";
                    for (RoomRoute r : routesFound) {
                        Log.i(TAG, r.getPhoneNumber());
                        toDisplay += r.getPhoneNumber();
                        toDisplay += "\n";
                    }
                    Toast.makeText(getBaseContext(), toDisplay, Toast.LENGTH_SHORT).show();
                    // add handling for no-routes found case

                    // assignment should be done immediately... on the first resource found?

                    // create room here and navigate accordingly away

                    // current implementation opens itself up to race conditions tho unlikely

                } else {
                    // add handling if there are issues with querying
                    Log.i(TAG, "Error querying routes");
                }
            }
        });
    }

}
