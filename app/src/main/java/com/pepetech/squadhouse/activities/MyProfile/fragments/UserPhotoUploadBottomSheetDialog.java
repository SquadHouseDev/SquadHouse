package com.pepetech.squadhouse.activities.MyProfile.fragments;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;
import com.pepetech.squadhouse.util.PhotoUploadBottomSheetDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UserPhotoUploadBottomSheetDialog extends PhotoUploadBottomSheetDialog {
    public static final String TAG = "UserPhotoUploadBottomSheetDialog";
    ParseUser parseUser;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parseUser = ParseUser.getCurrentUser();
        user = new User(parseUser);
        View v = inflater.inflate(R.layout.fragment_photo_upload_dialog, container, false);
        Button photo_button = v.findViewById(R.id.btnPhotoUpload);
        Button cancel_button = v.findViewById(R.id.btnCancel);
        photo_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                galleryIntent();
//                Toast.makeText(getActivity(), "Accessing Media Files", Toast.LENGTH_SHORT).show();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                UserPhotoUploadBottomSheetDialog.super.dismiss();
            }
        });
        return v;
    }

    @SuppressLint("LongLogTag")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data != null) && requestCode == REQUEST_CODE) {
            System.out.println("PHOTO:" + data);
//           String filepath = data.getData().getPath();
            Uri photoURI = data.getData();
            System.out.println("PATH:" + photoURI.getPath());
            String path = getRealPathFromURI(getContext().getApplicationContext(),photoURI);
            System.out.println("REAL PATH:" + path);
            File f = new File(path);
            user.updateImage(f);
        }
    }
}
/*

// And to convert the image URI to the direct file system path of the image file
cite used:
https://stackoverflow.com/questions/57093479/get-real-path-from-uri-data-is-deprecated-in-android-q


 */





