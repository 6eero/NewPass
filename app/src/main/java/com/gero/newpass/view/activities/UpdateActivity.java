package com.gero.newpass.view.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

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

import com.gero.newpass.Activities.MainActivity;
import com.gero.newpass.model.encryption.EncryptionHelper;
import com.gero.newpass.R;
import com.gero.newpass.databinding.ActivityUpdateBinding;
import com.gero.newpass.viewmodel.UpdateViewModel;

public class UpdateActivity extends AppCompatActivity {

    private EditText name_input, email_input, password_input;
    private String entry, name, email, password;
    private UpdateViewModel updateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUpdateBinding binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeBarsColor(R.color.background_primary);

        updateViewModel = new ViewModelProvider(this).get(UpdateViewModel.class);

        name_input = binding.nameInput2;
        email_input = binding.emailInput2;
        password_input = binding.passwordInput2;
        ImageButton updateButton = binding.updateButton;
        ImageButton backButton = binding.backButton;
        ImageButton deleteButton = binding.deleteButton;
        ImageButton copyButton = binding.copyButton;

        try {
            getAndSetIntentData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Observe any feedback messages from the ViewModel
        updateViewModel.getMessageLiveData().observe(this, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show());

        updateButton.setOnClickListener(view -> {

            name = name_input.getText().toString().trim();
            email = email_input.getText().toString().trim();
            password = password_input.getText().toString().trim();

            updateViewModel.updateEntry(entry, name, email, password);

        });

        deleteButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete " + name + " ?");
            builder.setMessage("Are you sure you want to delete " + name + " ?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                updateViewModel.deleteEntry(entry);
                finish();
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {

            });
            builder.create().show();

        });

        copyButton.setOnClickListener(v -> {
            copyToClipboard(password_input.getText().toString());

            Toast.makeText(UpdateActivity.this, "Text copied to the clipboard", Toast.LENGTH_SHORT).show();
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });



    }

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