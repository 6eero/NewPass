package com.example.newpass;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_FIRST_RUN = "firstRun";
    private static final String KEY_PASSWORD = "password";

    /**
     * Checks if the application is being run for the first time after installation.
     * Returns true if it is the first run, otherwise returns false.
     *
     * @param context The context of the application.
     * @return True if it is the first run after installation, false otherwise.
     */
    public static boolean isFirstRunAfterInstallation(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean(KEY_FIRST_RUN, true);
        if (isFirstRun) {

            // Set the "firstRun" flag to false after the first run
            prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply();
        }
        return isFirstRun;
    }
}
