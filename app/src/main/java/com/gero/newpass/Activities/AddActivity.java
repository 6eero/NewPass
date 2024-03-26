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
import com.gero.newpass.Database.DatabaseServiceLocator;
import com.gero.newpass.R;
import com.gero.newpass.databinding.ActivityAddBinding;

public class AddActivity extends AppCompatActivity {

    private EditText name_input, email_input, password_input;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.gero.newpass.databinding.ActivityAddBinding binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeBarsColor(R.color.background_primary);

        name_input = binding.nameInput;
        email_input = binding.emailInput;
        password_input = binding.passwordInput;
        ImageButton add_button = binding.addButton;
        ImageButton back_button = binding.backButton;

        add_button.setOnClickListener(v -> {
            DatabaseServiceLocator.init(getApplicationContext());
            DatabaseHelper myDB = DatabaseServiceLocator.getDatabaseHelper();

            try {
                String name = name_input.getText().toString().trim();
                String email = email_input.getText().toString().trim();
                String password = password_input.getText().toString().trim();

                if (
                        name.length() >= 4 && name.length() <= 10 &&             // name    [4, 10]
                        email.length() >= 4 && email.length() <= 30 &&           // email   [4, 30]
                        password.length() >= 4 && password.length() <= 15        // psw     [4, 15]
                ) {

                    if (!myDB.checkIfAccountAlreadyExist(name, email)) {
                        myDB.addEntry(
                                name,
                                email,
                                password
                        );
                    } else  {
                        Toast.makeText(getApplicationContext(), "This account already exist!", Toast.LENGTH_SHORT).show();
                    }

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