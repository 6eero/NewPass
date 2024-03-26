package com.gero.newpass.ViewModels;

import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    public boolean isValidCredentials(String name, String password) {

        return name.length() >= 4 &&
                name.length() <= 10 &&
                password.length() >= 4 &&
                password.length() <= 10;
    }
}

