package com.gero.newpass.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gero.newpass.Adapters.CustomAdapter;
import com.gero.newpass.Database.DatabaseHelper;
import com.gero.newpass.R;
import com.gero.newpass.databinding.ActivityMainBinding;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TextView no_data;
    private DatabaseHelper myDB;
    private ArrayList<String> row_id, row_name, row_email, row_password;
    private ImageView empty_imageview;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.gero.newpass.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        changeBarsColor(R.color.background_primary);

        RecyclerView recyclerView = binding.recyclerView;
        ImageButton buttonGenerate = binding.buttonGenerate;
        ImageButton buttonAdd = binding.buttonAdd;
        TextView count = binding.textViewCount;
        empty_imageview = binding.emptyImageview;
        no_data = binding.noData;

        myDB = new DatabaseHelper(MainActivity.this);
        row_id = new ArrayList<>();
        row_name = new ArrayList<>();
        row_email = new ArrayList<>();
        row_password = new ArrayList<>();

        storeDataInArrays();

        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, this, row_id, row_name, row_email, row_password);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        count.setText("["+ customAdapter.getItemCount() +"]");

        buttonGenerate.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GeneratePasswordActivity.class);
            startActivity(intent);
        });

        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays() {

        // Retrieve data from the database
        Cursor cursor = myDB.readAllData();

        if (cursor.getCount() == 0) {

            empty_imageview.setVisibility((View.VISIBLE));
            no_data.setVisibility((View.VISIBLE));
        } else {

            while (cursor.moveToNext()) {
                row_id.add(cursor.getString(0));
                row_name.add(cursor.getString(1));
                row_email.add(cursor.getString(2));
                row_password.add(cursor.getString(3));
            }

            empty_imageview.setVisibility((View.INVISIBLE));
            no_data.setVisibility((View.INVISIBLE));
        }
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