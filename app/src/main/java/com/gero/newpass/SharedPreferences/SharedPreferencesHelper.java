package com.gero.newpass.SharedPreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatDelegate;

import com.gero.newpass.R;

import java.util.Objects;

public class SharedPreferencesHelper {

    private static final String SCREEN_LOCK_FLAG = "screenlock";
    public static String DARK_MODE_FLAG = "isDarkModeOn";
    public static String SHARED_PREF_FLAG = "SharedPref";
    public static String LANG_PREF_FLAG = "language";

    //Obtain shared preferences
    public static synchronized SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SHARED_PREF_FLAG, Context.MODE_PRIVATE);
    }

    public static String getCurrentLanguage(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        String currentLanguage = sharedPreferences.getString(LANG_PREF_FLAG, "");

        if (currentLanguage.isEmpty()) {
            currentLanguage = "English";

        } else if (Objects.equals(currentLanguage, "zh")) {
            currentLanguage = "中国人";

        } else if (Objects.equals(currentLanguage, "ru")) {
            currentLanguage = "Русский";
            
        } else if (Objects.equals(currentLanguage, "pt-rBR")) {
            currentLanguage = "Português";
        } 
        
        return currentLanguage;
    }

    public static void setLanguage(Context context, String selectedLanguage) {

        if (Objects.equals(selectedLanguage, "中国人")) {
            selectedLanguage = "zh";
        }

        if (Objects.equals(selectedLanguage, "Русский")) {
            selectedLanguage = "ru";
        } 
        
        if (Objects.equals(selectedLanguage, "Portuguse")) {
            selectedLanguage = "pt-rBR";
        }
        
        if (Objects.equals(selectedLanguage, "Português")) {
            selectedLanguage = "pt-rBR";
        }

        SharedPreferences sharedPreferences = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(SharedPreferencesHelper.LANG_PREF_FLAG, selectedLanguage.toLowerCase().substring(0, 2));
        editor.apply();
    }

    //Return is dark mode is set or not from shared preferences, default value is true
    public static Boolean isDarkModeSet(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(DARK_MODE_FLAG, true);
    }

    //Set dark mode using app compat delegate
    public static void setDarkMode() {
        AppCompatDelegate
                .setDefaultNightMode(
                        AppCompatDelegate
                                .MODE_NIGHT_YES);
    }

    //Set light mode using app compat delegate
    public static void setLightMode() {
        AppCompatDelegate
                .setDefaultNightMode(
                        AppCompatDelegate
                                .MODE_NIGHT_NO);
    }

    //Edit dark mode shared preferences when needed (eg settings) for dark mode
    public static void setAndEditSharedPrefForDarkMode(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        setDarkMode();
        editor.putBoolean(
                DARK_MODE_FLAG, true);
        editor.apply();
    }

    //Edit dark mode shared preferences when needed (eg settings) for light mode
    public static void setAndEditSharedPrefForLightMode(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        setLightMode();
        editor.putBoolean(
                DARK_MODE_FLAG, false);
        editor.apply();
    }

    //Apply dark/ light mode based on shared preferences on UI, including the navigation bar
    public static void toggleDarkLightModeUI(Activity activity) {
        Boolean isDarkModeSet = isDarkModeSet(activity.getApplicationContext());
        if (isDarkModeSet) {
            setDarkMode();
        } else {
            setLightMode();
        }
        updateNavigationBarColor(isDarkModeSet, activity);
    }

    //Apply dark/ light mode on the navigation bar
    public static void updateNavigationBarColor(Boolean isDarkMode, Activity activity) {
        Window window = activity.getWindow();
        if (isDarkMode) {
            // Set dark color for navigation bar
            window.setNavigationBarColor(activity.getResources().getColor(R.color.navigationbar_dark_mode));
        } else {
            // Set light color for navigation bar
            window.setNavigationBarColor(activity.getResources().getColor(R.color.navigationbar_light_mode));
            // Additionally, if your navigation bar icons are not visible against the light background, you can make them dark:
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }

    //Return is dark mode is set or not from shared preferences, default value is true
    public static Boolean isScreenLockEnabled(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getBoolean(SCREEN_LOCK_FLAG, true);
    }

    public static void setUseScreenLockToUnlock(Context context) {
        Boolean currentState = SharedPreferencesHelper.isScreenLockEnabled(context);
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(SCREEN_LOCK_FLAG, !currentState);
        editor.apply();
    }
}
