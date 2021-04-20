package com.pepetech.squadhouse.activities.MyProfile.update;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

public class UpdateFullNameActivity extends AppCompatActivity {
    public static final String TAG = "UpdateFullNameActivity";

    ParseUser parseuser;
    User user;

    TextView tvfirstName, tvlastName;
    Button update;
    EditText etfirstname, etlastname;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_fullname);
        //SETUP VIEW ELEMENTS

        //text views
        tvfirstName = findViewById(R.id.tvPrompt);
        tvlastName = findViewById(R.id.tvLastName);

        //buttons
        update = findViewById(R.id.bttnUpdate);
        //user
        user = new User(ParseUser.getCurrentUser());

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "UPDATE BUTTON CLICKED!");
                etfirstname = (EditText) findViewById(R.id.etfirstname);
                etlastname = (EditText) findViewById(R.id.etlastname);
                String firstname = etfirstname.getText().toString();
                String lastname = etlastname.getText().toString();
                System.out.println(user.getFirstName() + user.getLastName());
                Log.i(TAG, firstname);
                Log.i(TAG, lastname);
                user.updateFirstName(firstname);
                user.updateLastName(lastname);
                Toast t = Toast.makeText(v.getContext(), "Update Complete!", Toast.LENGTH_SHORT);
                t.show();

            }
        });
    }


}
