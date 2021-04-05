package com.pepetech.squadhouse.activities;

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
import com.pepetech.squadhouse.models.User;

public class UpdateUsernameActivity extends AppCompatActivity {
    public static final String TAG = "UpdateUsernameActivity";
    ParseUser parseuser;
    User user;
    TextView tvusername;
    Button update;
    EditText etusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_username);
        //text views
        tvusername = findViewById(R.id.tvUsername);
        //buttons
        update = findViewById(R.id.bttnUpdateUsername);
        //unwrap parsed user
        parseuser = ParseUser.getCurrentUser();
        user = new User(parseuser);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"UPDATE BUTTON CLICKED!");
                etusername = (EditText)findViewById(R.id.etfirstname);
                String username = etusername.getText().toString();
                System.out.println(username);
                user.updateUserName(username);
                goToViewMyProfileActivity();
            }
        });


    }
    private void goToViewMyProfileActivity()
    {
        Intent i = new Intent(this,ViewMyProfileActivity.class);
        startActivity(i);
    }
}
