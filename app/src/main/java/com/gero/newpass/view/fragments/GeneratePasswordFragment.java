package com.gero.newpass.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gero.newpass.R;
import com.gero.newpass.databinding.FragmentGeneratePasswordBinding;
import com.gero.newpass.utilities.VibrationHelper;
import com.gero.newpass.view.activities.MainViewActivity;
import com.gero.newpass.viewmodel.GeneratePasswordViewModel;

import java.util.Locale;

import dev.trindadedev.ui.base.NewPassFragment;

public class GeneratePasswordFragment extends NewPassFragment {

    private GeneratePasswordViewModel generatePasswordViewModel;
    private SeekBar seekBar;
    private TextView textViewLength, textViewPassword, textViewEntropy;
    private ImageButton buttonUppercase, buttonLowercase, buttonNumber, buttonSpecial;
    private ImageButton buttonRegenerate, backButton;
    private FragmentGeneratePasswordBinding binding;
    private int optionsPerPosition = 26+26+10;
    private ImageView warningImageView;
    private int positions = 12;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGeneratePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViews(binding);
        generatePasswordViewModel = new ViewModelProvider(this).get(GeneratePasswordViewModel.class);

        buttonRegenerate.setOnClickListener(v -> {
           VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Weak);
            generatePasswordViewModel.generatePassword();
        });

        generatePasswordViewModel.generatePassword();

        generatePasswordViewModel.getPasswordLiveData().observe(getViewLifecycleOwner(), newPassword -> {


            SpannableString spannableString = new SpannableString(newPassword);

            positions = newPassword.length();

            for (int i = 0; i < positions; i++) {
                char c = newPassword.charAt(i);
                if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c)) {
                    spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.accent)), i, i + 1, 0);
                }
            }

            textViewPassword.setText(spannableString);
            textViewLength.setText("[" + positions + "]");

            calculateAndSetEntropyLevel(optionsPerPosition, positions);
        });


        textViewPassword.setOnClickListener(v -> {
            if ( Boolean.TRUE.equals(generatePasswordViewModel.getUppercaseStateLiveData().getValue()) ||
                    Boolean.TRUE.equals(generatePasswordViewModel.getLowercaseStateLiveData().getValue()) ||
                    Boolean.TRUE.equals(generatePasswordViewModel.getNumberStateLiveData().getValue()) ||
                    Boolean.TRUE.equals(generatePasswordViewModel.getSpecialStateLiveData().getValue())
            ) {
                copyToClipboard(textViewPassword.getText().toString());
                VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Strong);
                Toast.makeText(this.getContext(), R.string.generate_text_copied_to_clipboard, Toast.LENGTH_SHORT).show();
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                generatePasswordViewModel.setPasswordLength(progress);
                VibrationHelper.vibrate(binding.getRoot(), VibrationHelper.VibrationType.Weak);
                textViewLength.setText("[" + progress + "]");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        buttonUppercase.setOnClickListener(v -> {
            VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Weak);
            optionsPerPosition = generatePasswordViewModel.toggleUppercase(optionsPerPosition);
            calculateAndSetEntropyLevel(optionsPerPosition, positions);
        });
        buttonLowercase.setOnClickListener(v -> {
            VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Weak);
            optionsPerPosition = generatePasswordViewModel.toggleLowercase(optionsPerPosition);
            calculateAndSetEntropyLevel(optionsPerPosition, positions);
        });
        buttonNumber.setOnClickListener(v -> {
            VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Weak);
            optionsPerPosition = generatePasswordViewModel.toggleNumber(optionsPerPosition);
            calculateAndSetEntropyLevel(optionsPerPosition, positions);
    });
        buttonSpecial.setOnClickListener(v -> {
            VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Weak);
            optionsPerPosition = generatePasswordViewModel.toggleSpecial(optionsPerPosition);
            calculateAndSetEntropyLevel(optionsPerPosition, positions);
        });

        generatePasswordViewModel.getUppercaseStateLiveData().observe(getViewLifecycleOwner(), uppercaseState -> {
            int imageResource = (uppercaseState) ? R.drawable.btn_yes : R.drawable.btn_no;
            buttonUppercase.setImageDrawable(ContextCompat.getDrawable(this.requireActivity().getApplicationContext(), imageResource));
        });

        generatePasswordViewModel.getLowercaseStateLiveData().observe(getViewLifecycleOwner(), lowercaseState -> {
            int imageResource = (lowercaseState) ? R.drawable.btn_yes : R.drawable.btn_no;
            buttonLowercase.setImageDrawable(ContextCompat.getDrawable(this.requireActivity().getApplicationContext(), imageResource));
        });

        generatePasswordViewModel.getNumberStateLiveData().observe(getViewLifecycleOwner(), numberState -> {
            int imageResource = (numberState) ? R.drawable.btn_yes : R.drawable.btn_no;
            buttonNumber.setImageDrawable(ContextCompat.getDrawable(this.requireActivity().getApplicationContext(), imageResource));
        });

        generatePasswordViewModel.getSpecialStateLiveData().observe(getViewLifecycleOwner(), specialState -> {
            int imageResource = (specialState) ? R.drawable.btn_yes : R.drawable.btn_no;
            buttonSpecial.setImageDrawable(ContextCompat.getDrawable(this.requireActivity().getApplicationContext(), imageResource));
        });

        Activity activity = this.getActivity();

        backButton.setOnClickListener(v -> {
            if (activity instanceof MainViewActivity) {
                ((MainViewActivity) activity).onBackPressed();
            }
        });
    }

    private void calculateAndSetEntropyLevel(int optionsPerPosition, int positions) {
        double entropyLevel = Math.log(Math.pow(optionsPerPosition, positions))/(Math.log(2));

        textViewEntropy.setText(String.format(Locale.US, "%.2f", entropyLevel));
        if (entropyLevel < 71.45) {
            warningImageView.setVisibility(View.VISIBLE);
        } else {
            warningImageView.setVisibility(View.GONE);
        }
    }

    private void copyToClipboard(String text) {

        ClipboardManager clipboardManager = (ClipboardManager) this.requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(getString(R.string.text_copied_to_clipboard), text);
        clipboardManager.setPrimaryClip(clipData);
    }


    private void initViews(FragmentGeneratePasswordBinding binding) {
        seekBar = binding.seekBar;
        textViewLength = binding.textViewLenghtValue;
        textViewPassword = binding.textViewPassword;
        buttonUppercase = binding.buttonUppercase;
        buttonLowercase = binding.buttonLowercase;
        buttonNumber = binding.buttonNumber;
        buttonSpecial = binding.buttonSpecial;
        buttonRegenerate = binding.buttonRegenerate;
        backButton = binding.backButton;
        textViewEntropy = binding.textViewEntropy;
        warningImageView = binding.imageViewWarning;
    }
}