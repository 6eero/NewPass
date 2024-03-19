package com.example.newpass.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.newpass.Database.DatabaseHelper;
import com.example.newpass.Encryption.EncryptionHelper;
import com.example.newpass.R;

public class UpdateActivity extends AppCompatActivity {

    private EditText name_input, email_input, password_input;
    private String entry, name, email, password;
    private ImageButton back_button, update_button, delete_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        changeStatusBarColor(R.color.background_primary);
        setStatusBarIconsDark(false);

        name_input = findViewById(R.id.name_input2);
        email_input = findViewById(R.id.email_input2);
        password_input = findViewById(R.id.password_input2);
        update_button = findViewById(R.id.update_button);
        back_button = findViewById(R.id.btn_back);
        delete_button = findViewById(R.id.delete_button);

        try {
            getAndSetIntentData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        update_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //And only then we call this
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
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }

    /**
     * Retrieves data from the intent extras and sets the values to the corresponding input fields.
     * If the intent extras contain entry, name, email, and password data, it sets the values to the input fields.
     * Otherwise, it displays a toast indicating that no data is available.
     *
     * @throws Exception If an error occurs during the decryption process.
     */
    void getAndSetIntentData() throws Exception {
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
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper myDB = new DatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(entry);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
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