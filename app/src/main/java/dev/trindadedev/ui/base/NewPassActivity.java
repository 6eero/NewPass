package dev.trindadedev.ui.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.gero.newpass.ContextWrapper.NewPassContextWrapper;
import com.gero.newpass.SharedPreferences.SharedPreferencesHelper;
import com.gero.newpass.database.DatabaseServiceLocator;

import com.gero.newpass.R;
import com.gero.newpass.utilities.SystemBarColorHelper;

public class NewPassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarColorHelper.changeBarsColor(this, R.color.background_primary);
    }
}