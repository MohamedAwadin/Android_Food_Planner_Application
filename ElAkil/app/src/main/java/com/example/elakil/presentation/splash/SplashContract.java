package com.example.elakil.presentation.splash;

public interface SplashContract {
    interface View{
        void navigateToLogin();
        void navigateToMain();
    }
    interface Presenter{
        void startSplashTimer();
    }
}
