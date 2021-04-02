package com.pepetech.squadhouse.activities;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.pepetech.squadhouse.R;

public class PopUpWindowActivity {
    public void displayPopupWindow(final View v) {

        Toast.makeText(v.getContext(),"WOW, THAT SHIT IS POPPIN",Toast.LENGTH_SHORT).show();
        //create a view object through inflater
        LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popupwindow,null);
        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        //Make items outside of popupwindow inactive
        boolean focusable = true;
        //create a window with our parameters
       final PopupWindow pw = new PopupWindow(popupView,width,height,focusable);
        //SET LOCATION OF THE WINDOW ON THE SCREEN
        pw.showAtLocation(v, Gravity.CENTER,0,0);
        //INITIALIZE THE ELEMENTS OF OUR WINDOW, INSTALL THE HANDLER
        TextView test2 = popupView.findViewById(R.id.titleText);
        test2.setText(R.string.app_name);
        Button buttonEdit = popupView.findViewById(R.id.mssgBttn);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //As an example, display the message
                Toast.makeText(v.getContext(), "Wow, popup action button", Toast.LENGTH_SHORT).show();

            }
        });
        //Handle clicking on the inactive zone(the outside area) of the window
        //Handler for clicking on the inactive zone of the window

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Close the window when clicked
                pw.dismiss();
                return true;
            }
        });

    }

}
