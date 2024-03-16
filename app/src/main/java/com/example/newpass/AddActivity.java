package com.example.newpass;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class AddActivity extends AppCompatActivity {

    private EditText name_input, email_input, password_input;
    private ImageButton back_button, add_button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // Change the color of the status bar
        changeStatusBarColor(R.color.background_primary);
        setStatusBarIconsDark(false);

        name_input = findViewById(R.id.name_input);
        email_input = findViewById(R.id.email_input);
        password_input = findViewById(R.id.password_input);
        add_button = findViewById(R.id.add_button);
        back_button = findViewById(R.id.btn_back);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper myDB = new DatabaseHelper(AddActivity.this);
                try {
                    myDB.addEntry(name_input.getText().toString().trim(),
                            email_input.getText().toString().trim(),
                            password_input.getText().toString().trim());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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