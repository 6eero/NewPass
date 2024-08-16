package com.gero.newpass.view.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.security.crypto.EncryptedSharedPreferences;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.gero.newpass.ContextWrapper.NewPassContextWrapper;
import com.gero.newpass.R;
import com.gero.newpass.SharedPreferences.SharedPreferencesHelper;
import com.gero.newpass.databinding.ActivityLoginBinding;
import com.gero.newpass.encryption.EncryptionHelper;
import com.gero.newpass.factory.ViewMoldelsFactory;
import com.gero.newpass.repository.ResourceRepository;
import com.gero.newpass.utilities.AnimationsUtility;
import com.gero.newpass.utilities.StringHelper;
import com.gero.newpass.utilities.SystemBarColorHelper;
import com.gero.newpass.utilities.VibrationHelper;
import com.gero.newpass.viewmodel.LoginViewModel;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Locale;

import dev.trindadedev.ui.base.NewPassActivity;

public class LoginActivity extends NewPassActivity {

    private EditText passwordEntry;
    private ImageButton buttonRegisterOrUnlock, buttonPasswordVisibility;
    private ImageView passwordBox, bgImage;
    private TextView welcomeTextView, textViewRegisterOrUnlock;
    private EncryptedSharedPreferences encryptedSharedPreferences;
    private LoginViewModel loginViewModel;
    private Boolean isPasswordVisible = false;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews(binding);

        textViewRegisterOrUnlock.setText(getString(R.string.create_password_button_text));

        welcomeTextView.setText(getString(R.string.welcome_newpass_text));

        loginViewModel = new ViewModelProvider(this, new ViewMoldelsFactory(new ResourceRepository(getApplicationContext()))).get(LoginViewModel.class);

        loginViewModel.getLoginMessageLiveData().observe(this, message -> {
            // Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });

        loginViewModel.getLoginSuccessLiveData().observe(this, success -> {
            String hashedPassword = encryptedSharedPreferences.getString("password", "");

            if (success) {
                Intent intent = new Intent(LoginActivity.this, MainViewActivity.class);
                StringHelper.setSharedString(hashedPassword);
                startActivity(intent);
                finish();
            } else {
                AnimationsUtility.errorAnimation(buttonRegisterOrUnlock, textViewRegisterOrUnlock);
            }
        });

        encryptedSharedPreferences = EncryptionHelper.getEncryptedSharedPreferences(getApplicationContext());

        //Determining whether to set dark or light mode based on shared preferences
        SharedPreferencesHelper.toggleDarkLightModeUI(this);

        String hashedPassword = encryptedSharedPreferences.getString("password", "");
        Boolean isPasswordEmpty = hashedPassword.isEmpty();

        if (!isPasswordEmpty) {
            textViewRegisterOrUnlock.setText(getString(R.string.unlock_newpass_button_text));
            welcomeTextView.setText(getString(R.string.welcome_back_newpass_text));
        } else {
            var dialog = new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.welcome_newpass_text))
                .setMessage(getString(R.string.caution_message))
                .setPositiveButton(getString(R.string.continue_button), (dialogIn, which) -> dialogIn.dismiss())
                .create();
            dialog.show();
        }

        buttonPasswordVisibility.setOnClickListener(v -> {
            if (isPasswordVisible) {
                buttonPasswordVisibility.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.icon_visibility_on));
                passwordEntry.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                buttonPasswordVisibility.setImageDrawable(ContextCompat.getDrawable(LoginActivity.this, R.drawable.icon_visibility_off));
                passwordEntry.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }

            isPasswordVisible = !isPasswordVisible;
        });


        buttonRegisterOrUnlockListener(buttonRegisterOrUnlock, isPasswordEmpty);
    }

    public void buttonRegisterOrUnlockListener(View view, Boolean isPasswordEmpty) {
        if (!isPasswordEmpty) {
            loginUser(view);

        } else {
            registerUser();
        }
    }

    private void loginUser(View view) {
        Log.d("LOGIN_VM", "Already launched before");

        if (SharedPreferencesHelper.isScreenLockEnabled(this)) {
            BiometricManager biometricManager = BiometricManager.from(this);

            int canAuthenticate = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL);

            if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
                Log.d("LOGIN_VM", "App can authenticate using biometrics or device credentials.");

                hideUI(true);

                loginViewModel.loginUserWithBiometricAuth(this);

                loginViewModel.getLoginSuccessLiveData().observe(this, state -> {
                    Log.w("23057", String.valueOf(state));

                    if (!state) {

                        hideUI(false);
                        loginWithPassword(view);
                    }
                });

            } else {
                Log.d("LOGIN_VM", "No biometric or credential authentication features available on this device.");

                loginWithPassword(view);
            }
        } else {
            loginWithPassword(view);
        }
    }

    private void registerUser() {
        Log.d("LOGIN_VM", "First launch");

        buttonRegisterOrUnlock.setOnClickListener(v -> {
            String passwordInput = passwordEntry.getText().toString();
            try {
                loginViewModel.createUser(passwordInput, encryptedSharedPreferences);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
            VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Strong);
        });
    }

    private void hideUI(boolean bool) {
        if (bool) {
            buttonRegisterOrUnlock.setVisibility(View.GONE);
            passwordEntry.setVisibility(View.GONE);
            passwordBox.setVisibility(View.GONE);
            welcomeTextView.setVisibility(View.GONE);
            bgImage.setVisibility(View.GONE);
            buttonPasswordVisibility.setVisibility(View.GONE);
        } else {
            buttonRegisterOrUnlock.setVisibility(View.VISIBLE);
            passwordEntry.setVisibility(View.VISIBLE);
            passwordBox.setVisibility(View.VISIBLE);
            welcomeTextView.setVisibility(View.VISIBLE);
            bgImage.setVisibility(View.VISIBLE);
            buttonPasswordVisibility.setVisibility(View.VISIBLE);
        }
    }

    private void loginWithPassword(View view) {
        view.setOnTouchListener((v, event) -> {

            String passwordInput = passwordEntry.getText().toString();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Weak);
                    return true;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    try {
                        loginViewModel.loginUserWithPassword(passwordInput, encryptedSharedPreferences);
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        throw new RuntimeException(e);
                    }
                    VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Strong);
                    return true;
            }
            return false;
        });
    }


    private void initViews(ActivityLoginBinding binding) {
        passwordEntry = binding.loginTwPassword;
        welcomeTextView = binding.welcomeLoginTw;
        buttonRegisterOrUnlock = binding.registerOrUnlockButton;
        textViewRegisterOrUnlock = binding.registerOrUnlockTextView;
        passwordBox = binding.backgroundInputbox2;
        bgImage = binding.logoLogin;
        buttonPasswordVisibility = binding.passwordVisibilityButton;
    }

    @Override
    protected void attachBaseContext(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferencesHelper.SHARED_PREF_FLAG, MODE_PRIVATE);
        String language = sharedPreferences.getString(SharedPreferencesHelper.LANG_PREF_FLAG, "en");
        super.attachBaseContext(NewPassContextWrapper.wrap(context, language));
        Locale locale = new Locale(language);
        Resources resources = getBaseContext().getResources();
        Configuration conf = resources.getConfiguration();
        conf.setLocale(locale);
        resources.updateConfiguration(conf, resources.getDisplayMetrics());
    }
}