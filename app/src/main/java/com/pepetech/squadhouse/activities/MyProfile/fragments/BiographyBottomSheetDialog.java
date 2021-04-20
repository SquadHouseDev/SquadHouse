package com.pepetech.squadhouse.activities.MyProfile.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.activities.MyProfile.MyProfileActivity;
import com.pepetech.squadhouse.models.User;

/**
 * Modal Bottom Sheet for editting a User's Biography. This class returns to the
 * origin activity when completed.
 */
public class BiographyBottomSheetDialog extends BottomSheetDialogFragment {
    User currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_biography_dialog,
                container, false);
        EditText etBiography = v.findViewById(R.id.etBiography);
        Button btnBioUpdate = v.findViewById(R.id.btnBioUpdate);
        currentUser = new User(ParseUser.getCurrentUser());
        etBiography.setText(currentUser.getBiography());
        btnBioUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "Updated bio!", Toast.LENGTH_SHORT)
                        .show();
                // TODO: call method or definition body to apply changes to the user's profile here
                String biography = etBiography.getText().toString();
                currentUser.updateBiography(biography);
                MyProfileActivity myProfileActivity = (MyProfileActivity) getActivity();
                myProfileActivity.updateBiographyText(biography);
                dismiss();
            }
        });
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public boolean isCancelable() {
        return super.isCancelable();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
