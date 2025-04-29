package com.example.elakil.presentation.main.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


import com.example.elakil.R;
import com.example.elakil.presentation.main.presenter.MainContract;
import com.example.elakil.presentation.main.presenter.MainPresenter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements MainContract.View{


    private long pressedTime;

    private BottomNavigationView bottomNavigationView;

    private MainContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        presenter = new MainPresenter(this);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.nav_home){
                presenter.navigateToHome();
                return true ;
            }
            else if (item.getItemId() == R.id.nav_search){
                presenter.navigateToSearch();
                return true ;
            }
            else if (item.getItemId() == R.id.nav_favorites){
                presenter.navigateToFavorites();
                return true ;
            }
            else if (item.getItemId() == R.id.nav_plan){
                presenter.navigateToPlan();
                return true ;
            }
            else if (item.getItemId() == R.id.nav_profile){
                presenter.navigateToProfile();
                return true ;
            }
            else {
                return false ;
            }
        });

        if (savedInstanceState == null){
            presenter.navigateToHome();
        }

    }

    @Override
    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //presenter.navigateToHome();
        if (pressedTime + 2000 > System.currentTimeMillis()) {

            super.onBackPressed();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();


        }
        pressedTime = System.currentTimeMillis();

    }
}