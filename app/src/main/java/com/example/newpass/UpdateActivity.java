package com.example.newpass;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText name_input, email_input, password_input;
    String entry, name, email, password;
    ImageButton back_button, update_button, delete_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Change the color of the status bar
        changeStatusBarColor(R.color.background_primary);
        setStatusBarIconsDark(false);

        name_input = findViewById(R.id.name_input2);
        email_input = findViewById(R.id.email_input2);
        password_input = findViewById(R.id.password_input2);
        update_button = findViewById(R.id.update_button);
        back_button = findViewById(R.id.btn_back);
        delete_button = findViewById(R.id.delete_button);

        getAndSetIntentData();

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //And only then we call this
                DatabaseHelper myDB = new DatabaseHelper(UpdateActivity.this);
                name = name_input.getText().toString().trim();
                email = email_input.getText().toString().trim();
                password = password_input.getText().toString().trim();
                myDB.updateData(entry, name, email, password);
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("entry") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("email") && getIntent().hasExtra("password")){
            //Getting Data from Intent
            entry = getIntent().getStringExtra("entry");
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");

            //Setting Intent Data
            name_input.setText(name);
            email_input.setText(email);
            password_input.setText(password);
            //Log.d("stev", title+" "+author+" "+pages);
        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(entry);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
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