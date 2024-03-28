package com.gero.newpass.view.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gero.newpass.Activities.MainActivity;
import com.gero.newpass.R;
import com.gero.newpass.databinding.ActivityGeneratePasswordBinding;
import com.gero.newpass.viewmodel.GeneratePasswordViewModel;

public class GeneratePasswordActivity extends AppCompatActivity {

    private GeneratePasswordViewModel generatePasswordViewModel;
    private SeekBar seekBar;
    private TextView textViewLength, textViewPassword;
    private ImageButton buttonUppercase, buttonNumber, buttonSpecial;
    private ImageButton buttonRegenerate, backButton;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGeneratePasswordBinding binding = ActivityGeneratePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeBarsColor(R.color.background_primary);

        initViews(binding);
        generatePasswordViewModel = new ViewModelProvider(this).get(GeneratePasswordViewModel.class);

        /*TODO: generates and displays the password when starting the activity*/

        buttonRegenerate.setOnClickListener(v -> generatePasswordViewModel.generatePassword());



        textViewPassword.setOnClickListener(v -> {
            copyToClipboard(textViewPassword.getText().toString());
            Toast.makeText(GeneratePasswordActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                generatePasswordViewModel.setPasswordLength(progress);

                textViewLength.setText("[" + String.valueOf(progress) + "]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        buttonUppercase.setOnClickListener(v -> generatePasswordViewModel.toggleUppercase());
        buttonNumber.setOnClickListener(v -> generatePasswordViewModel.toggleNumber());
        buttonSpecial.setOnClickListener(v -> generatePasswordViewModel.toggleSpecial());

        generatePasswordViewModel.getUppercaseStateLiveData().observe(this, uppercaseState -> {
            int imageResource = (uppercaseState) ? R.drawable.btn_yes : R.drawable.btn_no;
            buttonUppercase.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), imageResource));
        });

        generatePasswordViewModel.getNumberStateLiveData().observe(this, numberState -> {
            int imageResource = (numberState) ? R.drawable.btn_yes : R.drawable.btn_no;
            buttonNumber.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), imageResource));
        });

        generatePasswordViewModel.getSpecialStateLiveData().observe(this, specialState -> {
            int imageResource = (specialState) ? R.drawable.btn_yes : R.drawable.btn_no;
            buttonSpecial.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), imageResource));
        });



        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(GeneratePasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }



    private void initViews(ActivityGeneratePasswordBinding binding) {
        seekBar = binding.seekBar;
        textViewLength = binding.textViewLenghtValue;
        textViewPassword = binding.textViewPassword;
        buttonUppercase = binding.buttonUppercase1;
        buttonNumber = binding.buttonNumber1;
        buttonSpecial = binding.buttonSpecial1;
        buttonRegenerate = binding.buttonRegenerate;
        backButton = binding.backButton;
    }


    private void copyToClipboard(String text) {

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text copied to clipboard", text);
        clipboardManager.setPrimaryClip(clipData);
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
