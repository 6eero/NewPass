package com.example.newpass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newpass.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity {

    private EditText loginTextViewName, loginTextViewPassword;
    private EncryptedSharedPreferences sharedPreferences;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeBarsColor(R.color.background_primary);

        loginTextViewName = findViewById(R.id.login_tw_name);
        loginTextViewPassword = findViewById(R.id.login_tw_password);
        TextView welcomeTextView = findViewById(R.id.welcome_tw);
        ImageButton buttonLogin = findViewById(R.id.login_button);
        ImageButton buttonRegister = findViewById(R.id.register_button);
        ImageView backgroundInputboxName = findViewById(R.id.background_inputbox_1);
        TextView registerTxt = findViewById(R.id.register_txt);

        Context context = this;

        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    context,
                    "loginkey",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }

        String name = sharedPreferences.getString("name", "");
        String password = sharedPreferences.getString("password", "");

        if (password.equals("") && name.equals("")){
            Log.i("2354325", "user not found");
            welcomeTextView.setText("Welcome\nUser!");

        } else {
            Log.i("2354325", "User found: name: " + name + " password: " + password);
            welcomeTextView.setText("Welcome back\n" + name + "!");
            buttonRegister.setVisibility(View.GONE);
            loginTextViewName.setVisibility(View.GONE);
            backgroundInputboxName.setVisibility(View.GONE);
            registerTxt.setVisibility(View.GONE);
        }

        buttonLogin.setOnClickListener(v -> loginUser());

        buttonRegister.setOnClickListener(v -> {
            try {
                createUser();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void createUser() throws GeneralSecurityException, IOException {

        String name = loginTextViewName.getText().toString();
        String password = loginTextViewPassword.getText().toString();

        if (name.length() > 3 && password.length() > 3) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("password", password);
            editor.putString("name", name);
            editor.apply();

            Toast.makeText(this, "User " + name + " created", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            String savedPasswordSharedPreferences = sharedPreferences.getString("password", "");
            StringUtility.setSharedString(savedPasswordSharedPreferences);

            startActivity(intent);
        } else {

            if (name.length() <= 3)
                Toast.makeText(this, "username must be at least 4 characters!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "password must be at least 4 characters!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUser() {
        String name = loginTextViewName.getText().toString();
        String password = loginTextViewPassword.getText().toString();

        String savedPasswordSharedPreferences = sharedPreferences.getString("password", "");
        String savedNameSharedPreferences = sharedPreferences.getString("name", "");

        Log.i("2354325", name + " " + password + " " + savedPasswordSharedPreferences + " " + savedNameSharedPreferences);

        if (savedPasswordSharedPreferences.equals(password)) {

            Toast.makeText(this, "Login done for: " + savedNameSharedPreferences, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            StringUtility.setSharedString(savedPasswordSharedPreferences);

            startActivity(intent);
        } else {
            Toast.makeText(this, "Access Denied", Toast.LENGTH_SHORT).show();
        }
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