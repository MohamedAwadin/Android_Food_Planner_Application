package com.example.elakil.presentation.auth.presenter;

public interface AuthContract {
    interface LoginView{
        void showLoading();
        void hideLoding();
        void showError(String message);
        void navigateToMain();
        void navigateToSignUp();
    }

    interface SignUpView{
        void showLoading();
        void hideLoding();
        void showError(String message);
        void navigateToMain();

    }

    interface Presenter{
        void loginWithEmail(String email , String password);
        void loginWithGoogle(String idToken);
        void loginAsGuest();
        void navigateToSignUp();
        void signUp(String username , String email , String password, String confirmPassword);
        void logout();

        void signUpWithGoogle(String idToken);
    }



}
