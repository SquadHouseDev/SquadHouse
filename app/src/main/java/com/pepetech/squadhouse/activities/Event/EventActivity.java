package com.pepetech.squadhouse.activities.Event;

import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.Event;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AppCompatActivity {

    public static final String TAG = "EventActivity";
    RecyclerView rvEvents;
    List<Event> events;
    EventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MY EVENTS");
        setContentView(R.layout.activity_event);
        events = new ArrayList<>();
        events.add(new Event());
        rvEvents = findViewById(R.id.rvEvents);
        adapter = new EventAdapter(this, events);
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setAdapter(adapter);
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