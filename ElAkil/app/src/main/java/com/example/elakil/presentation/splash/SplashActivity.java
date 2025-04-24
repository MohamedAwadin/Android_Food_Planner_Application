package com.example.elakil.presentation.splash;



import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.elakil.R;
import com.example.elakil.presentation.auth.view.LoginActivity;
import com.example.elakil.presentation.main.view.MainActivity;
import com.example.elakil.utils.SharedPreferencesUtils;

public class SplashActivity extends AppCompatActivity implements SplashContract.View{

    private static final int SPALSH_DURATION = 3000;
    private SplashContract.Presenter presenter ;
    private LottieAnimationView lottieAnimationView ;

    private SharedPreferencesUtils sharedPreferencesUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        lottieAnimationView.setAnimation("splash_animation.json");
        lottieAnimationView.playAnimation();

        sharedPreferencesUtils = new SharedPreferencesUtils(this);
        presenter = new SplashPresenter(this, sharedPreferencesUtils);
        presenter.startSplashTimer();

    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(SplashActivity.this , LoginActivity.class));
        finish();

    }

    @Override
    public void navigateToMain() {
        startActivity(new Intent(SplashActivity.this , MainActivity.class));
        //finish();

    }
}