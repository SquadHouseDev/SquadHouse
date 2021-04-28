package com.pepetech.squadhouse.util;

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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pepetech.squadhouse.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * TODO: migrate to potentially be an abstract class?
 */
public class PhotoUploadBottomSheetDialog extends BottomSheetDialogFragment {
    public static final String TAG = "PhotoUploadBottomSheetDialog";
    public static final int REQUEST_CODE = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_upload_dialog, container, false);
        Button photo_button = v.findViewById(R.id.btnPhotoUpload);
        Button cancel_button = v.findViewById(R.id.btnCancel);
        photo_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                galleryIntent();
            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                PhotoUploadBottomSheetDialog.super.dismiss();
            }
        });
        return v;
    }

    //Allows user to select image from Gallery
    protected void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE);
    }

    /**
     * Override for target object file field to be updated after activity completion
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @SuppressLint("LongLogTag")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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




