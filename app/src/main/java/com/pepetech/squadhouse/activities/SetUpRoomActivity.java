package com.pepetech.squadhouse.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.RoomRoute;

import java.util.List;

public class SetUpRoomActivity extends AppCompatActivity {
    public static final String TAG = "SetUpRoomActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_room);


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
