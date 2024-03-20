package com.example.newpass.Activities;

import androidx.appcompat.app.AppCompatActivity;
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
    private TextView welcomeTextView, registerTxt;
    private ImageButton buttonLogin, buttonRegister;
    private ImageView backgroundInputboxName, backgroundInputboxPassword;
    private EncryptedSharedPreferences sharedPreferences;
    private Context context;
    private MasterKey masterKey;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColor(R.color.background_primary);
        setStatusBarIconsDark(false);

        loginTextViewName = findViewById(R.id.login_tw_name);
        loginTextViewPassword = findViewById(R.id.login_tw_password);
        welcomeTextView = findViewById(R.id.welcome_tw);
        buttonLogin = findViewById(R.id.login_button);
        buttonRegister = findViewById(R.id.register_button);
        backgroundInputboxName = findViewById(R.id.background_inputbox_1);
        backgroundInputboxPassword = findViewById(R.id.background_inputbox_2);
        registerTxt = findViewById(R.id.register_txt);

        context = this;

        try {
            masterKey = new MasterKey.Builder(context)
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
            welcomeTextView.setText("Welcome\n" + name + "!");
            buttonRegister.setVisibility(View.GONE);
            loginTextViewName.setVisibility(View.GONE);
            backgroundInputboxName.setVisibility(View.GONE);
            registerTxt.setVisibility(View.GONE);
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    createUser();
                } catch (GeneralSecurityException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void createUser() throws GeneralSecurityException, IOException {
        String name = loginTextViewName.getText().toString();
        String password = loginTextViewPassword.getText().toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.putString("name", name);
        editor.apply();

        Toast.makeText(this, "Utente creato: " + name, Toast.LENGTH_SHORT).show();
    }

    private void loginUser() {
        String name = loginTextViewName.getText().toString();
        String password = loginTextViewPassword.getText().toString();

        String savedPasswordSharedPreferences = sharedPreferences.getString("password", "");
        String savedNameSharedPreferences = sharedPreferences.getString("name", "");

        Log.i("2354325", name + " " + password + " " + savedPasswordSharedPreferences + " " + savedNameSharedPreferences);

        if (savedPasswordSharedPreferences.equals(password)) {

            Toast.makeText(this, "Accesso effettuato per: " + name, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            StringUtility.setSharedString(savedPasswordSharedPreferences);

            startActivity(intent);
        } else {
            Toast.makeText(this, "Accesso non riuscito", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeStatusBarColor(int color) {
        try {

            Window window = getWindow();

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(getResources().getColor(color));
            window.setNavigationBarColor(getResources().getColor(color));
        } catch (IllegalArgumentException e) {

            throw new IllegalArgumentException("The provided color is invalid.");
        }
    }

    private void setStatusBarIconsDark(boolean dark) {

        View decor = getWindow().getDecorView();

        if (dark) {

            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {

            decor.setSystemUiVisibility(0);
        }
    }
}