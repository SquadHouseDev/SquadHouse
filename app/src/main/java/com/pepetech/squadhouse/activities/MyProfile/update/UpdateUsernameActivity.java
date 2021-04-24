package com.pepetech.squadhouse.activities.MyProfile.update;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.MyProfile.MyProfileActivity;
import com.pepetech.squadhouse.models.User;

public class UpdateUsernameActivity extends AppCompatActivity {
    public static final String TAG = "UpdateUsernameActivity";
    ParseUser parseuser;
    User user;
    TextView tvUsername;
    Button btnUpdateUsername;
    EditText etUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_username);
        //text views
        tvUsername = findViewById(R.id.tvUsername);
        //buttons
        btnUpdateUsername = findViewById(R.id.btnUpdateUsername);
        //unwrap parsed user
        parseuser = ParseUser.getCurrentUser();
        user = new User(parseuser);
        btnUpdateUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "UPDATE BUTTON CLICKED!");
                etUsername = (EditText) findViewById(R.id.etfirstname);
                String username = etUsername.getText().toString();
                System.out.println(username);
                user.updateUserName(username);
                goToViewMyProfileActivity();
            }
        });
    }

    private void goToViewMyProfileActivity() {
        Intent i = new Intent(this, MyProfileActivity.class);
        startActivity(i);
    }
}