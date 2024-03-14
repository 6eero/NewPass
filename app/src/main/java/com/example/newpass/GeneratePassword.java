package com.example.newpass;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GeneratePassword extends AppCompatActivity {

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

        //Copia
        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Copia il testo nel TextView nella clipboard
                copyToClipboard(textViewPassword.getText().toString());
                // Visualizza un messaggio di conferma
                Toast.makeText(GeneratePassword.this, "Testo copiato nella clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        // Bottone per disabilitare i caratteri uppercase
        buttonUppercase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Inverti lo stato corrente
                stateUppercase = !stateUppercase;

                // Se lo stato è true, imposta l'immagine su btn_y, altrimenti su btn_n
                if (stateUppercase) {
                    buttonUppercase.setImageDrawable(getResources().getDrawable(R.drawable.btn_y));
                } else {
                    buttonUppercase.setImageDrawable(getResources().getDrawable(R.drawable.btn_n));
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

        // Bottone per disabilitare i caratteri numerici
        buttonNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inverti lo stato corrente
                stateNumber = !stateNumber;

                // Se lo stato è true, imposta l'immagine su btn_y, altrimenti su btn_n
                if (stateNumber) {
                    buttonNumber.setImageDrawable(getResources().getDrawable(R.drawable.btn_y));
                } else {
                    buttonNumber.setImageDrawable(getResources().getDrawable(R.drawable.btn_n));
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

        // Bottone per disabilitare i caratteri speciali
        buttonSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inverti lo stato corrente
                stateSpecial = !stateSpecial;

                // Se lo stato è true, imposta l'immagine su btn_y, altrimenti su btn_n
                if (stateSpecial) {
                    buttonSpecial.setImageDrawable(getResources().getDrawable(R.drawable.btn_y));
                } else {
                    buttonSpecial.setImageDrawable(getResources().getDrawable(R.drawable.btn_n));
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
                Intent intent = new Intent(GeneratePassword.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Aggiungi un listener per il cambiamento del valore della SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                lenght = progress;

                // Aggiorna la TextView con il valore corrente della SeekBar
                textViewLength.setText("["+ lenght +"]");
                textViewPassword.setText(generaPassword(lenght, uppercase, number, special));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Metodo richiesto ma non utilizzato in questo caso
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Metodo richiesto ma non utilizzato in questo caso
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
}
