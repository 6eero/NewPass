package com.gero.newpass.view.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gero.newpass.R;
import com.gero.newpass.databinding.FragmentAddPasswordBinding;
import com.gero.newpass.factory.ViewMoldelsFactory;
import com.gero.newpass.repository.ResourceRepository;
import com.gero.newpass.utilities.VibrationHelper;
import com.gero.newpass.view.activities.MainViewActivity;
import com.gero.newpass.viewmodel.AddViewModel;

import dev.trindadedev.ui.base.NewPassFragment;

public class AddPasswordFragment extends NewPassFragment {

    private EditText nameInput, emailInput, passwordInput;
    private ImageButton buttonAdd, buttonBack, buttonPasswordVisibility;
    private FragmentAddPasswordBinding binding;
    private Boolean isPasswordVisible = false;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddPasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ResourceRepository resourceRepository = new ResourceRepository(requireContext());
        ViewMoldelsFactory factory = new ViewMoldelsFactory(resourceRepository);
        AddViewModel addViewModel = new ViewModelProvider(this, factory).get(AddViewModel.class);

        initViews(binding);

        Activity activity = this.getActivity();

        buttonPasswordVisibility.setOnClickListener(v -> {

            if (isPasswordVisible) {
                buttonPasswordVisibility.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_visibility_on));
                passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                buttonPasswordVisibility.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icon_visibility_off));
                passwordInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }

            isPasswordVisible = !isPasswordVisible;
        });

        buttonAdd.setOnTouchListener((v, event) -> {

            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Weak);
                    return true;
                case MotionEvent.ACTION_UP:
                    v.performClick();
                    addViewModel.addEntry(requireContext(), name, email, password);
                    VibrationHelper.vibrate(v, VibrationHelper.VibrationType.Strong);
                    return true;
            }
            return false;
        });

        addViewModel.getSuccessLiveData().observe(getViewLifecycleOwner(), success -> {
                    if (success) {
                        if (activity instanceof MainViewActivity) {
                            Bundle result = new Bundle();
                            //Result key for the main fragment to update the addition
                            result.putString("resultKey", "1");
                            getParentFragmentManager().setFragmentResult("requestKey", result);
                            ((MainViewActivity) activity).onBackPressed();
                        }
                    }
                }
        );

        buttonBack.setOnClickListener(v -> {
            if (activity instanceof MainViewActivity) {
                ((MainViewActivity) activity).onBackPressed();
            }
        });


        // Observe any feedback messages from the ViewModel
        addViewModel.getMessageLiveData().observe(getViewLifecycleOwner(), message ->
                Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT).show());
    }

    private void initViews(FragmentAddPasswordBinding binding) {
        nameInput = binding.nameInput;
        emailInput = binding.emailInput;
        passwordInput = binding.passwordInput;
        buttonAdd = binding.addButton;
        buttonBack = binding.backButton;
        buttonPasswordVisibility = binding.passwordVisibilityButton;
    }
}