package com.example.xyzreader.remote;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class Config {
    public static final URL BASE_URL;
    private static String TAG = Config.class.toString();
    private static Context context;

    static {
        URL url = null;
        try {
            url = new URL("https://go.udacity.com/xyz-reader-json" );
        } catch (MalformedURLException ignored) {
            Toast.makeText(context, "Please check your internet connection.",
                    Toast.LENGTH_LONG).show();
            Log.e(TAG, "Please check your internet connection.");
        }

        BASE_URL = url;
    }
}
