package com.gero.newpass.model.database;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 * class responsible for managing the instances of database helper class.
 */
public class DatabaseServiceLocator {
    @SuppressLint("StaticFieldLeak")
    private static DatabaseHelper databaseHelper; // Use a static method in the Service Locator class to retrieve the database instance.

    public static void init(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}

