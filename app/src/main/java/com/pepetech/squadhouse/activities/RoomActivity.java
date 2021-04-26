package com.pepetech.squadhouse.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pepetech.squadhouse.BuildConfig;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Home.HomeActivity;
import com.pepetech.squadhouse.adapters.ParticipantAdapter;
import com.pepetech.squadhouse.models.Room;
import com.pepetech.squadhouse.models.RoomRoute;
import com.pepetech.squadhouse.models.User;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.VoiceGrant;
import com.twilio.voice.Call;
import com.twilio.voice.CallException;
import com.twilio.voice.ConnectOptions;
import com.twilio.voice.Voice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * This is the activity for handling live conference calls. Usage
 * requires passage of a Room object from the previous activity
 * as a Parcel to be unwrapped in the Room Activity class.
 */
public class RoomActivity extends AppCompatActivity {

    private static final int MIC_PERMISSION_REQUEST_CODE = 1;
    public static final String TAG = "RoomActivity";
    public final String ACCOUNT_SID = BuildConfig.ACCOUNT_SID;
    public final String AUTH_TOKEN = BuildConfig.AUTH_TOKEN;
    public final String API_KEY = BuildConfig.API_KEY;
    public final String API_SECRET = BuildConfig.API_SECRET;
    public static AccessToken accessToken;
    private final Handler handler = new Handler();

    User user;
    Room room;

    Button invite_button;
    Button end_button;
    RecyclerView rvParticipants;
    ParticipantAdapter adapter;
    Button display_button;

    LinearLayout roomLayout;
    //make room model/class push to back4app
    ArrayList<ParseObject> allParticipants;
    //create room here, instantiate participant list, in inviteactivity, make calls to back4app
    //to update room.participantList

    HashMap<String, String> params = new HashMap<>();
    Call.Listener callListener = callListener();
    private AudioManager audioManager;
    private int savedAudioMode = AudioManager.MODE_NORMAL;
    private Call activeCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        roomLayout = findViewById(R.id.roomLayout);
        invite_button = findViewById(R.id.invite_button);
        end_button = findViewById(R.id.end_button);

        allParticipants = new ArrayList<>();
        rvParticipants = findViewById(R.id.rvParticipants);
        adapter = new ParticipantAdapter(this, allParticipants);
        rvParticipants.setAdapter(adapter);
        rvParticipants.setLayoutManager(new GridLayoutManager(this, 3));
//        rvParticipants.setLayoutManager(new LinearLayoutManager(this));

        display_button = findViewById(R.id.display_button);
        user = new User(ParseUser.getCurrentUser());

        Intent i = getIntent();
        room = i.getParcelableExtra("Room");
        Log.i(TAG, "objectID " + room.getObjectId());

        queryUsers();
        setOnClickListeners();

        /*
         * Needed for setting/abandoning audio focus during a call
         */
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);
        /*
         * Enable changing the volume using the up/down keys during a conversation
         */
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone();
        }

        Log.i(TAG, "ACCOUNT_SID +" + ACCOUNT_SID);
        // copy
        String identity = "user";

        // Create Video grant
        //VideoGrant grant = new VideoGrant().setRoom("cool room");
        VoiceGrant grant = new VoiceGrant();
        grant.setOutgoingApplicationSid(room.getAPSID());

        // Create access token
        accessToken = new AccessToken.Builder(
                ACCOUNT_SID,
                API_KEY,
                API_SECRET
        ).identity(identity).grant(grant).build();

        //Log.i(TAG, accessToken.toString());
        Log.i(TAG, accessToken.toJwt());

        //Consider looking into this after MVP
        //something about custom audio device handling in FileAndMicAudioDevice.java
        //fileAndMicAudioDevice = new FileAndMicAudioDevice(getApplicationContext());
        //Voice.setAudioDevice(fileAndMicAudioDevice);

        params.put("to", room.getPhoneNumber());

        ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken.toJwt())
                .params(params)
                .build();
        activeCall = Voice.connect(RoomActivity.this, connectOptions, callListener);
        doTheAutoRefresh();
    }

    private void queryNumbers() {
        //pull list of numbers

        //pull list of active rooms (ideally rooms have corresponding number field)

        //"cross out" in use numbers off your list (delete, push etc)
        //if(list.contains(room.number) { continue} else{ usableList.push(list[i])}

        //if usableList.length > 0
        //use usableList[0] for new room creation
    }

    private void queryUsers() {

        //based on room.participantList, display ppl with adapter??
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "auto refreshed list of participants");
                // Write code for your refresh logic
                ParseQuery<Room> roomQuery = new ParseQuery<Room>(Room.class);
                roomQuery.whereEqualTo(room.KEY_OBJECT_ID, room.getObjectId());
                roomQuery.getFirstInBackground(new GetCallback<Room>() {
                    @Override
                    public void done(Room object, ParseException e) {
                        if (e == null) {
                            Log.i(TAG, "Updated participant list for " + room.getObjectId());
                            room = object;
                            allParticipants.clear();
                            allParticipants.addAll(room.getParticipants());
                            Log.i(TAG, "# of participants: " + String.valueOf(room.getParticipants().size()));
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.e(TAG, e.toString());
                        }
                    }
                });
                adapter.notifyDataSetChanged();
                doTheAutoRefresh();
            }
        }, 5000);
    }

    private void setOnClickListeners() {
        invite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "invite button clicked");
                Toast.makeText(v.getContext(), "Invite clicked!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RoomActivity.this, InviteActivity.class);
                startActivity(i);
            }
        });

        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "end button clicked");
                Toast.makeText(v.getContext(), "End Button clicked!", Toast.LENGTH_SHORT).show();

                if (activeCall != null && room.getHost().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                    activeCall.disconnect();
                    activeCall = null;

                    //query roomroute and set availability back to true.
                    ParseQuery<RoomRoute> mainQuery = new ParseQuery<RoomRoute>(RoomRoute.class);
                    mainQuery.whereEqualTo(RoomRoute.KEY_PHONE_NUMBER, room.getPhoneNumber());
                    mainQuery.getFirstInBackground(new GetCallback<RoomRoute>() {
                        @Override
                        public void done(RoomRoute object, ParseException e) {
                            if (e == null) {
                                object.freeNumber();
                            }
                        }
                    });

                    ParseQuery<Room> roomQuery = new ParseQuery<Room>(Room.class);
                    roomQuery.whereEqualTo(room.KEY_OBJECT_ID, room.getObjectId());
                    roomQuery.getFirstInBackground(new GetCallback<Room>() {
                        @Override
                        public void done(Room object, ParseException e) {
                            if (e == null) {
                                Log.i(TAG, "objectID " + room.getObjectId());
                                object.setActiveState(false);
                                object.saveInBackground();
                            } else {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });
                }
                // ----------------------------------------------------
                room.removeParticipant(ParseUser.getCurrentUser());
                room.saveInBackground();
                adapter.notifyDataSetChanged();
                Intent i = new Intent(RoomActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

    }

    private Call.Listener callListener() {
        return new Call.Listener() {
            /*
             * This callback is emitted once before the Call.Listener.onConnected() callback when
             * the callee is being alerted of a Call. The behavior of this callback is determined by
             * the answerOnBridge flag provided in the Dial verb of your TwiML application
             * associated with this client. If the answerOnBridge flag is false, which is the
             * default, the Call.Listener.onConnected() callback will be emitted immediately after
             * Call.Listener.onRinging(). If the answerOnBridge flag is true, this will cause the
             * call to emit the onConnected callback only after the call is answered.
             * See answeronbridge for more details on how to use it with the Dial TwiML verb. If the
             * twiML response contains a Say verb, then the call will emit the
             * Call.Listener.onConnected callback immediately after Call.Listener.onRinging() is
             * raised, irrespective of the value of answerOnBridge being set to true or false
             */
            @Override
            public void onRinging(@NonNull Call call) {
                Log.d(TAG, "Ringing");
            }

            @Override
            public void onConnectFailure(@NonNull Call call, @NonNull CallException error) {
                setAudioFocus(false);

                Log.d(TAG, "Connect failure");
                String message = String.format(
                        Locale.US,
                        "Call Error: %d, %s",
                        error.getErrorCode(),
                        error.getMessage());
                Log.e(TAG, message);
                Snackbar.make(roomLayout, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onConnected(@NonNull Call call) {
                setAudioFocus(true);
                //applyFabState(inputSwitchFab, fileAndMicAudioDevice.isMusicPlaying());
                Log.d(TAG, "Connected " + call.getSid());
                activeCall = call;
                doTheAutoRefresh();
            }

            @Override
            public void onReconnecting(@NonNull Call call, @NonNull CallException callException) {
                Log.d(TAG, "onReconnecting");
            }

            @Override
            public void onReconnected(@NonNull Call call) {
                Log.d(TAG, "onReconnected");
            }

            @Override
            public void onDisconnected(@NonNull Call call, CallException error) {
                setAudioFocus(false);
                Log.d(TAG, "Disconnected");
                if (error != null) {
                    String message = String.format(
                            Locale.US,
                            "Call Error: %d, %s",
                            error.getErrorCode(),
                            error.getMessage());
                    Log.e(TAG, message);
                    Snackbar.make(roomLayout, message, Snackbar.LENGTH_LONG).show();
                }
            }
        };
    }

    private void setAudioFocus(boolean setFocus) {
        if (audioManager != null) {
            if (setFocus) {
                savedAudioMode = audioManager.getMode();
                // Request audio focus before making any device switch.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    AudioFocusRequest focusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener(i -> {
                            })
                            .build();
                    audioManager.requestAudioFocus(focusRequest);
                } else {
                    audioManager.requestAudioFocus(
                            focusChange -> {
                            },
                            AudioManager.STREAM_VOICE_CALL,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                }
                /*
                 * Start by setting MODE_IN_COMMUNICATION as default audio mode. It is
                 * required to be in this mode when playout and/or recording starts for
                 * best possible VoIP performance. Some devices have difficulties with speaker mode
                 * if this is not set.
                 */
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(savedAudioMode);
                audioManager.abandonAudioFocus(null);
            }
        }
    }

    private boolean checkPermissionForMicrophone() {
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultMic == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionForMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
            Snackbar.make(roomLayout,
                    "Microphone permissions needed. Please allow in your application settings.",
                    Snackbar.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MIC_PERMISSION_REQUEST_CODE);
        }
    }
}
