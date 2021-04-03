package com.pepetech.squadhouse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;
import com.pepetech.squadhouse.R;
import com.pepetech.squadhouse.models.User;

/*

 */
public class PopUpWindowActivity extends AppCompatActivity {
    //TAG
    String TAG = "PopUpWindowActivity";
    //buttons


    //essentially this is my OnCreate for the popupWindow

    ParseUser parseUser;
    User user;
    protected void OnCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);
    }


    public void displayPopupWindow(final View v) {

/*
            This code needs some better rework and clean up
 */
        //create a view object through inflater


        //Specify the length and width through constants

        //Make items outside of popupwindow inactive

        //create a window with our parameters

        //SET LOCATION OF THE WINDOW ON THE SCREEN

        //unwrap parsed user
//        parseUser = ParseUser.getCurrentUser();
//        user= new User(parseUser);



        /*

        IMPORTANT TO IMPLEMENT FIRST

        */


        //Handle clicking on the inactive zone(the outside area) of the window
        //Handler for clicking on the inactive zone of the window




    }
//    public void setup_Popup_Window_On_Click_Listeners() {
//        buttonUpdateName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //As an example, display the message
//                Log.i(TAG,"UPDATE BUTTON CLICKED");
//                        //------------------TEST DATA-------------
////                        System.out.println(parseUser);
////                        System.out.println(user.getFirstName() + user.getLastName());
////                        user.updateFirstName("cheese");
////                        System.out.println(user.getFirstName());
//                        //--------------------------------
//                //GO TO UpdateFullnameActivity
//                Intent i = new Intent(PopUpWindowActivity.this,UpdateFullNameActivity.class);
//                startActivity(i);
//
//            }
//        });
//        /*
//
//        IMPORT TO IMPLEMENT FIRST
//
//         */
//        cancel.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        createAlias.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//    }

}
