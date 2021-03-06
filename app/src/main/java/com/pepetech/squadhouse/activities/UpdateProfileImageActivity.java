package com.pepetech.squadhouse.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

public class UpdateProfileImageActivity extends AppCompatActivity {
    public static final String TAG = "UpdateProfileImageActivity";
    ParseUser parseUser;
    User user;
    TextView tvChangeImage;
    Button done;
    AppCompatImageView ivProfile;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profileimage);

        ivProfile = findViewById(R.id.ivprofile);
        tvChangeImage = findViewById(R.id.tvChangeImage);
        done = findViewById(R.id.bttnDone);
        parseUser = ParseUser.getCurrentUser();
        user = new User(parseUser);
        populateProfileElements();
        setupOnClickListeners();
    }
    @SuppressLint("LongLogTag")
    private void populateProfileElements() {
        Log.i(TAG, "Populating profile elements");
        // load user's profile picture
        ParseFile image = parseUser.getParseFile(User.KEY_IMAGE);
        if (image != null)
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfile);
        // load profile text information
//        tvFullName.setText(parseUser.getString(User.KEY_FIRST_NAME) + " " + parseUser.getString(User.KEY_LAST_NAME));
//        tvBiography.setText(parseUser.getString(User.KEY_BIOGRAPHY));
//        tvUsername.setText("@" + parseUser.getUsername());
//        // load following and followers count
//        tvFollowersCount.setText(String.valueOf(followers.size()));
//        tvFollowingCount.setText(String.valueOf(following.size()));
//        // load nominator's profile picture
//        if (!user.isSeed() && nominator != null) {
//            loadNominatorProfileImage();
//            tvNominatorName.setText(nominator.getString(User.KEY_FIRST_NAME));
        }


            private void setupOnClickListeners()
            {
                ivProfile.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View v) {
                        Toast t = Toast.makeText(v.getContext(), "Profile image clicked!", Toast.LENGTH_SHORT);
                        t.show();
                        Log.i(TAG,"Profile Image clicked!");
                        BottomSheetDialogActivity bottomSheet = new BottomSheetDialogActivity();
                        bottomSheet.show(getSupportFragmentManager(),"ModalBottomSheet");
                    }
                });

            }
    }
