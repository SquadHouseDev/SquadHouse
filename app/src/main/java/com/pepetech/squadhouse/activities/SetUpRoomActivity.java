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

import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.parse.FindCallback;
import com.parse.GetCallback;
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
                            //Toast.makeText(getBaseContext(), toDisplay, Toast.LENGTH_SHORT).show();

                            Log.i(TAG, String.valueOf(allRoutes.isEmpty()));
                            //pasted stuff
                            if (!allRoutes.isEmpty()) {
                                //create and post room
                                newRoom = new Room();
                                newRoom.setTitle(String.valueOf(roomTitleEt.getText()));
                                newRoom.setDescription(String.valueOf(roomDescriptionEt.getText()));
                                //set the host equal to the current user, getParseUser  returns a ParseUser which
                                //will reflect in back4app.com as a pointer to a specific user.
                                newRoom.setHost(ParseUser.getCurrentUser());
                                newRoom.setPhoneNumber(allRoutes.get(0).getPhoneNumber());
                                Log.i(TAG, allRoutes.get(0).getPhoneNumber());
                                newRoom.setActiveState(true);
                                newRoom.setAPSID(allRoutes.get(0).getAPSID());
                                newRoom.saveInBackground();
                                ParseQuery<Room> roomQuery = new ParseQuery<Room>(Room.class);
                                roomQuery.whereEqualTo(Room.KEY_TITLE, newRoom.getTitle());
                                roomQuery.whereEqualTo(Room.KEY_HOST, ParseUser.getCurrentUser());
                                roomQuery.whereEqualTo(Room.KEY_DESCRIPTION, newRoom.getDescription());
                                roomQuery.whereEqualTo(Room.KEY_IS_ACTIVE, newRoom.isActive());
                                roomQuery.getFirstInBackground(new GetCallback<Room>() {
                                    @Override
                                    public void done(Room object, ParseException e) {
                                        if (e == null) {
                                            Log.i(TAG, object.getObjectId());
                                            newRoom = object;
                                            newRoom.saveInBackground();

                                            //set room availability to false
                                            allRoutes.get(0).lockNumber();
                                            Intent i = new Intent(SetUpRoomActivity.this, RoomActivity.class);
                                            i.putExtra("Room", newRoom);
                                            //i.putExtra("AP_SID", allRoutes.get(0).getAPSID());
                                            startActivity(i);
                                        }
                                    }
                                });
                            } else {
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

}
