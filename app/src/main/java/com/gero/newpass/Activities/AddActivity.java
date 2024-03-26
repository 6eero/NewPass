package com.gero.newpass.Activities;

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
import android.widget.Toast;

import com.gero.newpass.Database.DatabaseHelper;
import com.gero.newpass.R;

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

        add_button.setOnClickListener(v -> {
            DatabaseHelper myDB = new DatabaseHelper(AddActivity.this);
            try {
                String name = name_input.getText().toString().trim();
                String email = email_input.getText().toString().trim();
                String password = password_input.getText().toString().trim();

                if (
                        name.length() >= 4 && name.length() <= 10 &&             // name    [4, 10]
                        email.length() >= 4 && email.length() <= 30 &&           // email   [4, 30]
                        password.length() >= 4 && password.length() <= 15        // psw     [4, 15]
                ) {

                    myDB.addEntry(
                            name,
                            email,
                            password
                    );

                } else {
                    if (name.length() < 4 || name.length() > 10) {
                        Toast.makeText(getApplicationContext(), "Name should be 4 to 10 characters long!", Toast.LENGTH_SHORT).show();

                    } else if (email.length() < 4 || email.length() > 30) {
                        Toast.makeText(getApplicationContext(), "Email should be 4 to 30 characters long!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Password should be 4 to 15 characters long!", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
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