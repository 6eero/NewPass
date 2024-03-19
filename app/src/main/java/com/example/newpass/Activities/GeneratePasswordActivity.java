package com.example.newpass.Activities;

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

import com.example.newpass.R;

import java.util.Random;

public class GeneratePasswordActivity extends AppCompatActivity {

    private Boolean uppercase = false, number = false, special = false;
    private int lenght = 8;
    private boolean stateUppercase = false, stateSpecial = false, stateNumber = false;
    private TextView textViewLength, textViewPassword;
    private ImageButton buttonUppercase, buttonNumber, buttonSpecial, buttonRegenerate, backButton;
    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_password);

        changeStatusBarColor(R.color.background_primary);
        setStatusBarIconsDark(false);


        textViewLength = findViewById(R.id.textView_Lenght_Value);
        textViewPassword = findViewById(R.id.textView_Password);

        backButton = findViewById(R.id.btn_back);

        buttonUppercase = findViewById(R.id.button_Uppercase1);
        buttonNumber = findViewById(R.id.button_Number1);
        buttonSpecial = findViewById(R.id.button_Special1);

        buttonRegenerate = findViewById(R.id.button_Regenerate);

        seekBar = findViewById(R.id.seekBar);


        textViewLength.setText("["+ lenght +"]");
        textViewPassword.setText(generaPassword(lenght, uppercase, number, special));


        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                copyToClipboard(textViewPassword.getText().toString());

                Toast.makeText(GeneratePasswordActivity.this, "Testo copiato nella clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        buttonUppercase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stateUppercase = !stateUppercase;

                if (stateUppercase) {
                    buttonUppercase.setImageDrawable(getResources().getDrawable(R.drawable.btn_yes));
                } else {
                    buttonUppercase.setImageDrawable(getResources().getDrawable(R.drawable.btn_no));
                }

                if (uppercase) {
                    uppercase = false;
                    textViewPassword.setText(generaPassword(lenght, false, number, special));
                } else {
                    uppercase = true;
                    textViewPassword.setText(generaPassword(lenght, true, number, special));
                }
            }
        });

        buttonNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stateNumber = !stateNumber;

                if (stateNumber) {
                    buttonNumber.setImageDrawable(getResources().getDrawable(R.drawable.btn_yes));
                } else {
                    buttonNumber.setImageDrawable(getResources().getDrawable(R.drawable.btn_no));
                }
                if (number) {
                    number = false;
                    textViewPassword.setText(generaPassword(lenght, uppercase, false, special));
                } else {
                    number = true;
                    textViewPassword.setText(generaPassword(lenght, uppercase, true, special));
                }
            }
        });

        buttonSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stateSpecial = !stateSpecial;

                if (stateSpecial) {
                    buttonSpecial.setImageDrawable(getResources().getDrawable(R.drawable.btn_yes));
                } else {
                    buttonSpecial.setImageDrawable(getResources().getDrawable(R.drawable.btn_no));
                }

                if (special) {
                    special = false;
                    textViewPassword.setText(generaPassword(lenght, uppercase, number, false));
                } else {
                    special = true;
                    textViewPassword.setText(generaPassword(lenght, uppercase, number, true));
                }
            }
        });

        buttonRegenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewPassword.setText(generaPassword(lenght, uppercase, number, special));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneratePasswordActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                lenght = progress;

                textViewLength.setText("["+ lenght +"]");
                textViewPassword.setText(generaPassword(lenght, uppercase, number, special));
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
     * @return a random string with this characteristics. If the 3 booleans are 'false', it will gnerate a random string using only lowercase characters
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

        // Build the random string
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }


    /**
     * Method for copying text to the clipboard
     * @param text text to copy to the clipboard
     */
    private void copyToClipboard(String text) {
        // Ottieni un riferimento al ClipboardManager
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // Crea un oggetto ClipData contenente il testo da copiare
        ClipData clipData = ClipData.newPlainText("Testo copiato", text);
        // Copia il ClipData nella clipboard
        clipboardManager.setPrimaryClip(clipData);
    }

    /**
     * Change the color of the status bar of the current activity.
     *
     * @param color The color to set on the status bar. Must be a valid color value.
     * @throws IllegalArgumentException If the provided color is invalid.
     */
    private void changeStatusBarColor(int color) {
        try {

            // Get the window of the current activity
            Window window = getWindow();

            // Add the flag to draw the status bar background
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // Set the color of the status bar
            window.setStatusBarColor(getResources().getColor(color));
            window.setNavigationBarColor(getResources().getColor(color));
        } catch (IllegalArgumentException e) {
            // If an IllegalArgumentException occurs, throw an exception with an explanatory message
            throw new IllegalArgumentException("The provided color is invalid.");
        }
    }


    /**
     * Sets the color of the status bar icons (such as time, battery, etc.) to either dark or light mode.
     *
     * @param dark True to set the status bar icons to dark mode, false to set them to light mode.
     */
    private void setStatusBarIconsDark(boolean dark) {

        // Get the decor view of the window
        View decor = getWindow().getDecorView();

        // Set the system UI visibility based on the provided mode
        if (dark) {

            // Set status bar icons to dark mode
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {

            // Set status bar icons to light mode
            decor.setSystemUiVisibility(0);
        }
    }
}
