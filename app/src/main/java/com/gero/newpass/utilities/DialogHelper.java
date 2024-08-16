package com.gero.newpass.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.security.crypto.EncryptedSharedPreferences;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.gero.newpass.R;
import com.gero.newpass.database.DatabaseHelper;
import com.gero.newpass.encryption.HashUtils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DialogHelper {

    public static void showChangePasswordDialog(Context context, EncryptedSharedPreferences encryptedSharedPreferences) {
        var dialog = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        dialog.setView(dialogView);

        EditText firstInput = dialogView.findViewById(R.id.first_input);
        EditText secondInput = dialogView.findViewById(R.id.second_input);
        EditText thirdInput = dialogView.findViewById(R.id.third_input);

        dialog.setTitle(R.string.settings_change_password)
                .setPositiveButton(R.string.update_alertdialog_yes, (dialogIn, id) -> {

                    String inputOldPassword = firstInput.getText().toString();
                    String inputNewPassword = secondInput.getText().toString();
                    String inputConfirmNewPassword = thirdInput.getText().toString();

                    String hashedPasswordFromSharedPrefs = encryptedSharedPreferences.getString("password", "");

                    try {
                        if (HashUtils.verifyPassword(inputOldPassword, hashedPasswordFromSharedPrefs) && inputNewPassword.length() >= 4 && inputNewPassword.equals(inputConfirmNewPassword)) {

                            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();

                            String hashedPassword = HashUtils.hashPassword(inputNewPassword);
                            editor.putString("password", hashedPassword);
                            editor.apply();

                            DatabaseHelper.changeDBPassword(hashedPassword, context);
                        } else if (inputNewPassword.length() < 4) {
                            Toast.makeText(context, R.string.password_must_be_at_least_4_characters_long, Toast.LENGTH_SHORT).show();
                        } else if (!inputNewPassword.equals(inputConfirmNewPassword)) {
                            Toast.makeText(context, R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, R.string.wrong_password, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        throw new RuntimeException(e);
                    }
                })
                .setNegativeButton(R.string.update_alertdialog_no, (dialogIn, id) -> dialogIn.cancel());
        dialog.show();
    }

    public static void showExportingDialog(Context context) {
        var dialog = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_export_or_import_db, null);
        dialog.setView(dialogView);
        EditText input = dialogView.findViewById(R.id.input);

        dialog.setTitle(R.string.export_database)
                .setPositiveButton(R.string.confirm, (dialogIn, id) -> {
                    String password = input.getText().toString();

                    if (password.isEmpty()) {
                        Toast.makeText(context, context.getString(R.string.password_cannot_be_empty), Toast.LENGTH_LONG).show();
                    } else {
                        DatabaseHelper.exportDatabaseToJson(context, password);
                    }

                })
                .setNegativeButton(R.string.cancel, (dialogIn, id) -> dialogIn.cancel());
        dialog.show();
    }

    public static void showImportingDialog(Context context, Uri fileURL) {
        var dialog = new MaterialAlertDialogBuilder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_export_or_import_db, null);
        dialog.setView(dialogView);
        EditText input = dialogView.findViewById(R.id.input);

        dialog.setTitle(R.string.import_database)
                .setPositiveButton(R.string.confirm, (dialogIn, id) -> {
                    String password = input.getText().toString();
                    try {
                        DatabaseHelper.importJsonToDatabase(context, fileURL, password);
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        Log.e("8953467", "Error: ", e);
                    }
                })
                .setNegativeButton(R.string.cancel, (dialogIn, id) -> dialogIn.cancel());
        dialog.show();
    }
}
