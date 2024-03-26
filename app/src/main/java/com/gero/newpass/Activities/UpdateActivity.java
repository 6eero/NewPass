package com.gero.newpass.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.ClipData;
import android.content.ClipboardManager;

import com.gero.newpass.Database.DatabaseHelper;
import com.gero.newpass.Encryption.EncryptionHelper;
import com.gero.newpass.R;
import com.gero.newpass.databinding.ActivityUpdateBinding;

public class UpdateActivity extends AppCompatActivity {

    private EditText name_input, email_input, password_input;
    private String entry, name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.gero.newpass.databinding.ActivityUpdateBinding binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeBarsColor(R.color.background_primary);

        name_input = binding.nameInput2;
        email_input = binding.emailInput2;
        password_input = binding.passwordInput2;
        ImageButton update_button = binding.updateButton;
        ImageButton back_button = binding.backButton;
        ImageButton delete_button = binding.deleteButton;
        ImageButton copy_button = binding.copyButton;

        try {
            getAndSetIntentData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        update_button.setOnClickListener(view -> {

            DatabaseHelper myDB = new DatabaseHelper(UpdateActivity.this);
            name = name_input.getText().toString().trim();
            email = email_input.getText().toString().trim();
            password = password_input.getText().toString().trim();

            try {
                String encryptedPassword = EncryptionHelper.encrypt(password);

                if (
                        name.length() >= 4 && name.length() <= 10 &&                     // name    [4, 10]
                                email.length() >= 4 && email.length() <= 30 &&           // email   [4, 30]
                                password.length() >= 4 && password.length() <= 15        // psw     [4, 15]
                ) {
                    myDB.updateData(entry, name, email, encryptedPassword);
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
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        delete_button.setOnClickListener(v -> confirmDialog());
        copy_button.setOnClickListener(v -> {
            copyToClipboard(password_input.getText().toString());

            Toast.makeText(UpdateActivity.this, "Text copied to the clipboard", Toast.LENGTH_SHORT).show();
        });

    }

    /**
     * Retrieves data from the intent extras and sets the values to the corresponding input fields.
     * If the intent extras contain entry, name, email, and password data, it sets the values to the input fields.
     * Otherwise, it displays a toast indicating that no data is available.
     *
     */
    void getAndSetIntentData() {
        if(getIntent().hasExtra("entry") && getIntent().hasExtra("name") &&
                getIntent().hasExtra("email") && getIntent().hasExtra("password")){

            entry = getIntent().getStringExtra("entry");
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");

            String decryptedPassword = EncryptionHelper.decrypt(password);

            name_input.setText(name);
            email_input.setText(email);
            password_input.setText(decryptedPassword);

        }else{
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Displays a confirmation dialog for deleting an entry.
     * If the user confirms deletion, the entry is deleted from the database.
     * If the user cancels deletion, the dialog is dismissed.
     */
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + name + " ?");
        builder.setMessage("Are you sure you want to delete " + name + " ?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            DatabaseHelper myDB = new DatabaseHelper(UpdateActivity.this);
            myDB.deleteOneRow(entry);
            finish();
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> {

        });
        builder.create().show();
    }

    /**
     * Method for copying text to the clipboard
     * @param text text to copy to the clipboard
     */
    private void copyToClipboard(String text) {

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Text copied to the clipboard", text);
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