package com.pepetech.squadhouse.models;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Class used when querying for available phone numbers to assign
 * to newly created Rooms. This class is also used when ending a call
 * at which point the roomRouted should be set to null using one of the following:
 * <ul>
 * <li>freeNumber()</li>
 * <li>setAvailability(true)</li>
 * </ul>
 * Usage:
 * <ul>
 *     <li>
 * Instances with new numbers should be created for representing newly
 *  acquired and configured Twilio conference routes.
 *     </li>
 *     <li>
 * Updating instances that exist on the Parse Server should operate
 * on the boolean isAvailable field.
 *     </li>
 * </ul>
 */
@ParseClassName("RoomRoute")
public class RoomRoute extends ParseObject {
    public static final String TAG = "RoomRoute";
    public static final String KEY_IS_AVAILABLE = "isAvailable";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    public static final String KEY_AP_SID = "AP_SID";

    ////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////
    public String getPhoneNumber() {
        String rv = null;
        try {
            rv = fetchIfNeeded().getString(KEY_PHONE_NUMBER);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    public String getAPSID() {
        String rv = null;
        try {
            rv = fetchIfNeeded().getString(KEY_AP_SID);
            return rv;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rv;
    }

    ////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////

    public void setAvailability(boolean isAvailable) {
        put(KEY_IS_AVAILABLE, isAvailable);
        saveInBackground();
    }

    public void freeNumber() {
        put(KEY_IS_AVAILABLE, true);
        saveInBackground();
    }

    public void lockNumber() {
        put(KEY_IS_AVAILABLE, false);
        saveInBackground();
    }

}
