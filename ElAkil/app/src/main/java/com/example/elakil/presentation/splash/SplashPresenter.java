package com.example.elakil.presentation.splash;

import android.os.Looper;
import android.os.Handler;
import com.example.elakil.utils.SharedPreferencesUtils;



public class SplashPresenter implements SplashContract.Presenter{
    private SplashContract.View view ;
    private SharedPreferencesUtils sharedPreferencesUtils ;

    public SplashPresenter(SplashContract.View view, SharedPreferencesUtils sharedPreferencesUtils) {
        this.view = view;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
    }

    private static final int SPLASH_DURATION = 3000 ;

    @Override
    public void startSplashTimer() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                if (sharedPreferencesUtils.isLoggedIn()){
                    view.navigateToMain();
                }
                else {
                    view.navigateToLogin();
                }
            }
        },SPLASH_DURATION);

    }
}
