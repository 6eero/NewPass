package com.example.newpass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.newpass.Database.DatabaseHelper;
import com.example.newpass.R;

public class AddActivity extends AppCompatActivity {

    private EditText name_input, email_input, password_input;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        changeBarsColor(R.color.background_primary);

        name_input = findViewById(R.id.name_input);
        email_input = findViewById(R.id.email_input);
        password_input = findViewById(R.id.password_input);
        ImageButton add_button = findViewById(R.id.add_button);
        ImageButton back_button = findViewById(R.id.btn_back);

        add_button.setOnClickListener(new View.OnClickListener() {

            /**
             * Sets an OnClickListener for a button.
             * When the button is clicked, it creates a new entry in the database using the values
             * entered in the name_input, email_input, and password_input fields.
             *
             * @param v The view (button) that was clicked.
             */
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

        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void changeBarsColor(int color) {

        try {
            Window window = getWindow();
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(0);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, (color)));
            window.setNavigationBarColor(ContextCompat.getColor(this, (color)));

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The provided color is invalid.");
        }
    }
}