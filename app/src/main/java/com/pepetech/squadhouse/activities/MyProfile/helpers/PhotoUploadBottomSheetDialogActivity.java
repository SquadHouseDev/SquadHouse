package com.pepetech.squadhouse.activities.MyProfile.helpers;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

// TODO: broken, doesn't navigate to previous activity or refocuses on the previous activity
public class PhotoUploadBottomSheetDialogActivity extends BottomSheetDialogFragment {
    public static final String TAG = "BottomSheetDialogActivity";
    private static final int REQUEST_CODE = 1;
    ParseUser parseUser;
    User user;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parseUser = ParseUser.getCurrentUser();
        user = new User(parseUser);
        View v = inflater.inflate(R.layout.fragment_photo_upload_dialog, container, false);
        Button photo_button = v.findViewById(R.id.photoBttn);
        photo_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                galleryIntent();
                Toast.makeText(getActivity(), "Accessing Media Files", Toast.LENGTH_SHORT).show();

            }
        });
        return v;
    }

    //Allows user to select image from Gallery
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
//       intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE);
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
//            String thePath = "no-path-found";
//            String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
//            Cursor cursor = getContext().getContentResolver().query(photoURI, filePathColumn, null, null, null);
//            if (cursor.moveToFirst()) {
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                thePath = cursor.getString(columnIndex);
//                System.out.println("USER:" + user.getImage().toString());
////            File f = new File(new File(thePath).getAbsolutePath());
//            }
//            cursor.close();
//           String path = photoURI.getPath();

        }


    }
    public String getRealPathFromURI(Context c, Uri uri)
    {
        final ContentResolver contentresolver = getContext().getContentResolver();
        if(contentresolver == null)
                return null;
            //create file path inside app's data dir
        String filePath = getContext().getApplicationInfo().dataDir + File.separator + System.currentTimeMillis();
        File file = new File(filePath);
        try {
            InputStream inputStream = contentresolver.openInputStream(uri);
            if(inputStream == null)
                return null;
            OutputStream outputstream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len = inputStream.read(buf)) > 0)
            {
                outputstream.write(buf,0,len);
            }
            outputstream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }
        return file.getAbsolutePath();
    }
}
/*

// And to convert the image URI to the direct file system path of the image file
cite used:
https://stackoverflow.com/questions/57093479/get-real-path-from-uri-data-is-deprecated-in-android-q


 */





