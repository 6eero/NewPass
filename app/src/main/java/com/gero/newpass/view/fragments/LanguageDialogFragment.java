package com.gero.newpass.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.gero.newpass.R;
import com.gero.newpass.SharedPreferences.SharedPreferencesHelper;

import java.util.Arrays;
import java.util.Objects;

public class LanguageDialogFragment extends DialogFragment {
    int position;

    public interface LanguageListener {
        void onPositiveButtonClicked(String[] list, int position);
        void onNegativeButtonClicked();
    }

    LanguageListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (LanguageListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity() + "LanguageListener must be implemented!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        String currentLanguage = SharedPreferencesHelper.getCurrentLanguage(requireContext());

        String[] languageList = requireActivity().getResources().getStringArray(R.array.language_options);

        position = getLanguagePosition(languageList, currentLanguage.substring(0, 2));

        //Log.i("32465034", Arrays.toString(languageList) + " " + position + " " + currentLanguage.substring(0, 2));

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getActivity());

        dialog.setTitle(R.string.languagedialog_select_your_choice)
                .setSingleChoiceItems(languageList, position, (dialogIn, which) -> position = which)
                .setPositiveButton("Ok", (dialogIn, which) -> {
                    mListener.onPositiveButtonClicked(languageList, position);
                    Toast.makeText(requireContext(), getString(R.string.language_changed), Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialogIn, which) -> mListener.onNegativeButtonClicked());

        return dialog.create();
    }

    private int getLanguagePosition(String[] languageList, String currentLanguage) {

        for (int i = 0; i < languageList.length; i++) {

            if (Objects.equals(languageList[i].toLowerCase().substring(0, 2), currentLanguage)) {
                position = i;
                break;
            }
        }
        return position;
    }
}
