package com.pepetech.squadhouse.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import java.util.ArrayList;
import java.util.List;


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
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                signUpUser(username, password, email, firstName, lastName);
                try {
                    ParseUser.logIn(username, password);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                goToHomeActivity();

            }
        });
    }

    private boolean isValidFields(String username, String password, String email, String firstName, String lastName) {
        final ParseQuery<ParseUser> emailQuery = ParseUser.getQuery();
        emailQuery.whereEqualTo("email", email);
        emailQuery.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> results, ParseException e) {
                // results has the list of users that match either the email address or username
            }
        });
        return false;
    }

    private void signUpUser(String username, String password, String email, String firstName, String lastName) {
        user = new ParseUser();
        // configure standard ParseUser fields
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        // configure extra ParseUser fields
        user.put(User.KEY_FIRST_NAME, etFirstName.getText().toString());
        user.put(User.KEY_LAST_NAME,  etLastName.getText().toString());
        Toast.makeText(getBaseContext(), "Starting sign up", Toast.LENGTH_SHORT);
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