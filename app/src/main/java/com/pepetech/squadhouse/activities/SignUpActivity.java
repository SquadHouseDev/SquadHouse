package com.pepetech.squadhouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import java.util.ArrayList;


public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    private EditText etUserName, etPassword, etEmail, etFirstName, etLastName;
    private Button btnSignup;
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signup button");
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                String email = etEmail.getText().toString();
                signUpUser(etUserName.getText().toString(), etPassword.getText().toString(), etEmail.getText().toString());
                try {
                    ParseUser.logIn(username, password);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // add methods for navigating to new activity here
                goToHomeActivity();
            }
        });
    }

    /**
     * Method for ingesting application user input information for creation
     * of a User account
     * @param username
     * @param password
     * @param email
     */
    private void signUpUser(String username, String password, String email) {
        user = new ParseUser();
        // configure standard ParseUser fields
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        // configure extra ParseUser fields
        user.put(User.KEY_FIRST_NAME, etFirstName.getText().toString());
        user.put(User.KEY_LAST_NAME,  etLastName.getText().toString());
        user.put(User.KEY_FOLLOWERS,  new ArrayList<String>());
        user.put(User.KEY_FOLLOWING,  new ArrayList<String>());
        user.put(User.KEY_IS_SEED,  true);
//        user.put(User.KEY_NOMINATOR,  );  // need feature for invite / approving sign ups
        user.put(User.KEY_BIOGRAPHY,  "");
        user.put(User.KEY_PHONE_NUMBER,  ""); // need feature for pinging user for phone number verification
        Toast.makeText(getBaseContext(), "Starting sign up", Toast.LENGTH_SHORT);

        // POST request to the Parse Server
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, "Successfully created account!");
                    Toast.makeText(getBaseContext(), "Account created!", Toast.LENGTH_SHORT);
                } else {
                    // TODO add addtional cases in which a user's input is "incorrect"
                    // 1. password is insecure
                    // 2. email is incorrect
                    switch (e.getCode()) {
                        case ParseException.USERNAME_TAKEN:
                            // TODO reflect on GUI that username is taken, ideally inside of the edit text box
                            Log.d(TAG, "Username is taken!");
                            Toast.makeText(getBaseContext(), "Username is taken!", Toast.LENGTH_SHORT);
                            // report error
                            break;
                        case ParseException.EMAIL_TAKEN:
                            // TODO reflect on GUI that email is taken, ideally inside of the edit text box
                            Log.d(TAG, "Email is taken!");
                            Toast.makeText(getBaseContext(), "Email is taken!", Toast.LENGTH_SHORT);
                            // report error
                            break;
                        default: {
                            // Something else went wrong
                        }
                    }
                }
            }
        });
    }

    private void goToHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish(); // disable user ability to renavigate after a successful login
    }

}