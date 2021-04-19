package com.pepetech.squadhouse.activities.MyProfile.helpers;

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
    Button btnDone;
    AppCompatImageView ivUpdateProfile;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_image);

        ivUpdateProfile = findViewById(R.id.ivUpdateProfile);
        tvChangeImage = findViewById(R.id.tvChangeImage);
        btnDone = findViewById(R.id.btnDone);
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
                    .into(ivUpdateProfile);
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


    private void setupOnClickListeners() {
        ivUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Profile image clicked!", Toast.LENGTH_SHORT);
                t.show();
                Log.i(TAG, "Profile Image clicked!");
                // TODO: fix broken navigation to previous activity
                BottomSheetDialogActivity bottomSheet = new BottomSheetDialogActivity();
                bottomSheet.show(getSupportFragmentManager(), "ModalBottomSheet");
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "Done clicked!", Toast.LENGTH_SHORT);
                finish();
            }
        });

    }
}
