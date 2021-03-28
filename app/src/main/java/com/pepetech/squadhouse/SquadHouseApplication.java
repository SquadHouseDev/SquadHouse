package com.pepetech.squadhouse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;
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
                .applicationId("zeWif7JuAu8yjGcetjhFUkfNbbdvWnmcciFEvMJA")
                .clientKey("5MYxEN3UHd1JIdpajxRKqvrcAB7WNEJPwJXHeGLb")
                .server("https://parseapi.back4app.com")
                .build()
        );
        // handle error in which session token is invalid
        ParseUser.enableRevocableSessionInBackground();
    }

}
