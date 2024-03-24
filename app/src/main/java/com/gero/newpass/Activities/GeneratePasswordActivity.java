package com.gero.newpass.Activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gero.newpass.R;

import java.util.Random;

public class GeneratePasswordActivity extends AppCompatActivity {

    private Boolean uppercase = false, number = false, special = false;
    private int length = 8;
    private boolean stateUppercase = false, stateSpecial = false, stateNumber = false;
    private TextView textViewLength, textViewPassword;
    private ImageButton buttonUppercase,  buttonNumber, buttonSpecial;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_password);
        changeBarsColor(R.color.background_primary);

        textViewLength = findViewById(R.id.textView_Lenght_Value);
        textViewPassword = findViewById(R.id.textView_Password);

        ImageButton backButton = findViewById(R.id.btn_back);

        buttonUppercase = findViewById(R.id.button_Uppercase1);
        buttonNumber = findViewById(R.id.button_Number1);
        buttonSpecial = findViewById(R.id.button_Special1);

        ImageButton buttonRegenerate = findViewById(R.id.button_Regenerate);

        SeekBar seekBar = findViewById(R.id.seekBar);


        textViewLength.setText("["+ length +"]");
        textViewPassword.setText(generaPassword(length, uppercase, number, special));


        textViewPassword.setOnClickListener(v -> {

            copyToClipboard(textViewPassword.getText().toString());

            Toast.makeText(GeneratePasswordActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        buttonUppercase.setOnClickListener(v -> {

            stateUppercase = !stateUppercase;

            if (stateUppercase) {
                buttonUppercase.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_yes));
            } else {
                buttonUppercase.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_no));
            }

            if (uppercase) {
                uppercase = false;
                textViewPassword.setText(generaPassword(length, false, number, special));
            } else {
                uppercase = true;
                textViewPassword.setText(generaPassword(length, true, number, special));
            }
        });

        buttonNumber.setOnClickListener(v -> {

            stateNumber = !stateNumber;

            if (stateNumber) {
                buttonNumber.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_yes));
            } else {
                buttonNumber.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_no));
            }
            if (number) {
                number = false;
                textViewPassword.setText(generaPassword(length, uppercase, false, special));
            } else {
                number = true;
                textViewPassword.setText(generaPassword(length, uppercase, true, special));
            }
        });

        buttonSpecial.setOnClickListener(v -> {

            stateSpecial = !stateSpecial;

            if (stateSpecial) {
                buttonSpecial.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_yes));
            } else {
                buttonSpecial.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btn_no));
            }

            if (special) {
                special = false;
                textViewPassword.setText(generaPassword(length, uppercase, number, false));
            } else {
                special = true;
                textViewPassword.setText(generaPassword(length, uppercase, number, true));
            }
        });

        buttonRegenerate.setOnClickListener(v -> textViewPassword.setText(generaPassword(length, uppercase, number, special)));

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(GeneratePasswordActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                length = progress;

                textViewLength.setText("["+ length +"]");
                textViewPassword.setText(generaPassword(length, uppercase, number, special));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /**
     * Returns a random string with the following characteristics:
     * @param length: the length of the string
     * @param uppercase: true if the string contains uppercase characters
     * @param number: true if the string contains numbers
     * @param special: true if the string contains special characters
     * @return a random string with this characteristics. If the 3 booleans are 'false', it will generate a random string using only lowercase characters
     */
    private String generaPassword(int length, Boolean uppercase, Boolean number, Boolean special) {

        String charSet1 = "", charSet2 = "abcdefghijklmnopqrstuvwxyz", charSet3 = "", charSet4 = "";

        if (uppercase){
            charSet1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if (number){
            charSet3 = "0123456789";
        }
        if (special){
            charSet4 = "?#%{}@!$()[]";
        }

        String characters = charSet1 + charSet2 + charSet3 + charSet4;

        Random random = new Random();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
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
