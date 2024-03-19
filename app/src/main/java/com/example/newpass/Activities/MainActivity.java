package com.example.newpass.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.newpass.Adapters.CustomAdapter;
import com.example.newpass.Database.DatabaseHelper;
import com.example.newpass.R;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ImageButton buttonGenerate, buttonAdd;
    private TextView count, no_data;
    private RecyclerView recyclerView;
    private DatabaseHelper myDB;
    private ArrayList<String> row_id, row_name, row_email, row_password;
    private CustomAdapter customAdapter;
    private ImageView empty_imageview;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeStatusBarColor(R.color.background_primary);
        setStatusBarIconsDark(false);

        recyclerView = findViewById(R.id.recyclerView);
        buttonGenerate = findViewById(R.id.button_Generate);
        buttonAdd = findViewById(R.id.button_Add);
        count = findViewById(R.id.textView_count);
        empty_imageview = findViewById(R.id.empty_imageview);
        no_data = findViewById(R.id.no_data);

        myDB = new DatabaseHelper(MainActivity.this);
        row_id = new ArrayList<>();
        row_name = new ArrayList<>();
        row_email = new ArrayList<>();
        row_password = new ArrayList<>();

        storeDataInArrays();

        customAdapter = new CustomAdapter(MainActivity.this, this, row_id, row_name, row_email, row_password);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        count.setText("["+String.valueOf(customAdapter.getItemCount())+"]");

        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GeneratePasswordActivity.class);
                startActivity(intent);
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getKeyForDBEncryption() {
        Intent intent = getIntent();
        return intent.getStringExtra("saved_password");
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