package com.pepetech.squadhouse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.pepetech.squadhouse.models.Room;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class SquadHouseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Register needed modesl hosted on backend
        ParseObject.registerSubclass(Room.class);

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See http://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("qb7d9CWVF9pXpGcjtUV7K4dPRy8WwbV8AWaJEG7A")
                .clientKey("zof0bRDisJvyV1suSxw8MfBCM5i6D5hj115ewTk0")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }

}
