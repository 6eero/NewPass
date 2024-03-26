package com.gero.newpass.ViewModels;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoginViewModelTest {

    @Test
    public void isValidCredentials_ValidData_ReturnsFalse() {
        LoginViewModel loginViewModel = new LoginViewModel();
        boolean isValid = loginViewModel.isValidCredentials("", "password") &&
                loginViewModel.isValidCredentials("username", "") &&
                loginViewModel.isValidCredentials("username", "password");
        assertFalse(isValid);
    }
}

