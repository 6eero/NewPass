package com.example.newpass.Activities;

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

import com.example.newpass.Database.DatabaseHelper;
import com.example.newpass.Encryption.EncryptionHelper;
import com.example.newpass.R;

public class UpdateActivity extends AppCompatActivity {

    private EditText name_input, email_input, password_input;
    private String entry, name, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        changeBarsColor(R.color.background_primary);

        name_input = findViewById(R.id.name_input2);
        email_input = findViewById(R.id.email_input2);
        password_input = findViewById(R.id.password_input2);
        ImageButton update_button = findViewById(R.id.update_button);
        ImageButton back_button = findViewById(R.id.btn_back);
        ImageButton delete_button = findViewById(R.id.delete_button);
        ImageButton copy_button = findViewById(R.id.copy_button);

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
                myDB.updateData(entry, name, email, encryptedPassword);
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