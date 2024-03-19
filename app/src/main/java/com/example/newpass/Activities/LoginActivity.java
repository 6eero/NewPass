package com.example.newpass.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newpass.R;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity {

    private EditText loginTextViewName, loginTextViewPassword;
    private TextView welcomeTextView;
    private ImageButton buttonLogin, buttonCreateUser;
    private EncryptedSharedPreferences sharedPreferences;
    private Context context;
    private MasterKey masterKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginTextViewName = findViewById(R.id.login_tw_name);
        loginTextViewPassword = findViewById(R.id.login_tw_password);
        welcomeTextView = findViewById(R.id.welcome_tw);
        buttonLogin = findViewById(R.id.login_button);
        buttonCreateUser = findViewById(R.id.create_user_button);

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

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        buttonCreateUser.setOnClickListener(new View.OnClickListener() {
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
        editor.putString(name, password);
        editor.apply();

        Toast.makeText(this, "Utente creato: " + name, Toast.LENGTH_SHORT).show();
    }

    private void loginUser() {
        String name = loginTextViewName.getText().toString();
        String password = loginTextViewPassword.getText().toString();

        String savedPasswordSharedPreferences = sharedPreferences.getString(name, "");

        if (savedPasswordSharedPreferences.equals(password)) {

            Toast.makeText(this, "Accesso effettuato per: " + name, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            StringUtility.setSharedString(savedPasswordSharedPreferences);

            startActivity(intent);
        } else {
            Toast.makeText(this, "Accesso non riuscito", Toast.LENGTH_SHORT).show();
        }
    }
}