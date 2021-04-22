package com.pepetech.squadhouse.activities.Event;
import com.pepetech.squadhouse.R;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EventActivity extends AppCompatActivity {

    public static final String TAG = "EventActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MY EVENTS");
        setContentView(R.layout.activity_event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_create_event:
                i = new Intent(this, EventCreateActivity.class);
                this.startActivity(i);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}