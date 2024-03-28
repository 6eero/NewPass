package com.gero.newpass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gero.newpass.R;
import com.gero.newpass.model.utilities.StringUtility;
import com.gero.newpass.databinding.ActivityLoginBinding;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity {

    private EditText loginTextViewName, loginTextViewPassword;
    private EncryptedSharedPreferences sharedPreferences;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.gero.newpass.databinding.ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeBarsColor(R.color.background_primary);

        loginTextViewName = binding.loginTwName;
        loginTextViewPassword = binding.loginTwPassword;
        TextView welcomeRegisterTextView = binding.welcomeRegisterTw;
        TextView welcomeLoginTextView = binding.welcomeLoginTw;
        ImageButton buttonLogin = binding.loginButton;
        ImageButton buttonRegister = binding.registerButton;
        ImageView backgroundInputboxName = binding.backgroundInputbox1;
        ImageView logo = binding.logoRegister;
        ImageView logoLogin = binding.logoLogin;

        Context context = this;

        buttonLogin.setVisibility(View.GONE);
        welcomeLoginTextView.setVisibility(View.GONE);
        logoLogin.setVisibility(View.GONE);

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

        if (password.isEmpty() && name.isEmpty()){
            Log.i("2354325", "user not found");

        } else {
            Log.i("2354325", "User found: name: " + name + " password: " + password);
            buttonRegister.setVisibility(View.GONE);
            logo.setVisibility(View.GONE);
            welcomeRegisterTextView.setVisibility(View.GONE);
            loginTextViewName.setVisibility(View.GONE);
            backgroundInputboxName.setVisibility(View.GONE);

            buttonLogin.setVisibility(View.VISIBLE);
            logoLogin.setVisibility(View.VISIBLE);
            welcomeLoginTextView.setVisibility(View.VISIBLE);

            String next = "<font color='"+ String.format("#%06X", 0xFFFFFF &  ContextCompat.getColor(context, R.color.accent)) +"'>"+ name +"</font>";
            welcomeLoginTextView.setText(Html.fromHtml("Welcome back<br>" + next + "!", Html.FROM_HTML_MODE_LEGACY));
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

        if (
                name.length() >= 4 &&
                name.length() <= 10 &&
                password.length() >= 4
        ) {

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

            if (name.length() <= 3) {
                Toast.makeText(this, "username must be at least 4 characters!", Toast.LENGTH_SHORT).show();
            }
            else if ((name.length() > 10)) {
                Toast.makeText(this, "username must contain a maximum of 10 characters!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "password must be at least 4 characters!", Toast.LENGTH_SHORT).show();
            }

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