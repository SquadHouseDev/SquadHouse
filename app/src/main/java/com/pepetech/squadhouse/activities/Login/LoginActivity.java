package com.pepetech.squadhouse.activities.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.Home.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LOGIN");
        // Navigate to home feed given cached credentials
        if (ParseUser.getCurrentUser() != null) {
            goToHomeActivity();
        }
        ////////////////////////////////////////////////////////////
        // Setup view elements
        ////////////////////////////////////////////////////////////
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                goToSignUpActivity();
            }
        });
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempting to login user: " + username);
        // TODO: nav to main activity if the user has signed in properly
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // TODO: better error handling via feedback to user
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                goToHomeActivity();
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT);
            }
        });
    }

    private void signoutUser() {
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
    }

    private void goToSignUpActivity() {
        Log.i(TAG, "Attempting to sign up user");
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    private void goToHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        // disable user ability to re-enter the login screen after a successful login
        finish();
    }
}