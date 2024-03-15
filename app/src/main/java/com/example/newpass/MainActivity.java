package com.example.newpass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageButton buttonGenerate, buttonAdd;
    private TextView count, no_data;
    private RecyclerView recyclerView;
    private DatabaseHelper myDB;
    private ArrayList<String> row_id, row_name, row_email, row_password;
    private CustomAdapter customAdapter;
    private ImageView empty_imageview;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Change the color of the status bar
        changeStatusBarColor(R.color.background_primary);
        setStatusBarIconsDark(false);

        // Assign the IDs to the corresponding variable
        recyclerView = findViewById(R.id.recyclerView);
        buttonGenerate = findViewById(R.id.button_Generate);
        buttonAdd = findViewById(R.id.button_Add);
        count = findViewById(R.id.textView_count);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        myDB = new DatabaseHelper(MainActivity.this);
        row_id = new ArrayList<>();
        row_name = new ArrayList<>();
        row_email = new ArrayList<>();
        row_password = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this, this, row_id, row_name, row_email, row_password);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        count.setText("["+String.valueOf(customAdapter.getItemCount())+"]");

        // Called when the generate button has been clicked -> Leads to the corresponding activity where you can generate a new password
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GeneratePassword.class);
                startActivity(intent);
            }
        });

        // Called when add button has been clicked -> Leads to the corresponding activity where you can add a new entry
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Called when an activity launched by this activity exits, giving the result.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }


    /**
     * Retrieves data from the database and stores it into arrays.
     * Displays appropriate views if no data is found or if data is retrieved successfully.
     */
    void storeDataInArrays() {

        // Retrieve data from the database
        Cursor cursor = myDB.readAllData();

        if (cursor.getCount() == 0) {

            // If no data is found, show appropriate views with the image and a textview
            empty_imageview.setVisibility((View.VISIBLE));
            no_data.setVisibility((View.VISIBLE));
        } else {

            // If data is found, iterate through the cursor and store data into arrays
            while (cursor.moveToNext()) {
                row_id.add(cursor.getString(0));
                row_name.add(cursor.getString(1));
                row_email.add(cursor.getString(2));
                row_password.add(cursor.getString(3));
            }
            // Hide the image and textview as data is available
            empty_imageview.setVisibility((View.INVISIBLE));
            no_data.setVisibility((View.INVISIBLE));
        }
    }


    /**
     * Change the color of the status bar of the current activity.
     *
     * @param color The color to set on the status bar. Must be a valid color value.
     * @throws IllegalArgumentException If the provided color is invalid.
     */
    private void changeStatusBarColor(int color) {
        try {

            // Get the window of the current activity
            Window window = getWindow();

            // Add the flag to draw the status bar background
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // Set the color of the status bar
            window.setStatusBarColor(getResources().getColor(color));
            window.setNavigationBarColor(getResources().getColor(color));
        } catch (IllegalArgumentException e) {
            // If an IllegalArgumentException occurs, throw an exception with an explanatory message
            throw new IllegalArgumentException("The provided color is invalid.");
        }
    }


    /**
     * Sets the color of the status bar icons (such as time, battery, etc.) to either dark or light mode.
     *
     * @param dark True to set the status bar icons to dark mode, false to set them to light mode.
     */
    private void setStatusBarIconsDark(boolean dark) {

        // Get the decor view of the window
        View decor = getWindow().getDecorView();

        // Set the system UI visibility based on the provided mode
        if (dark) {

            // Set status bar icons to dark mode
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {

            // Set status bar icons to light mode
            decor.setSystemUiVisibility(0);
        }
    }
}