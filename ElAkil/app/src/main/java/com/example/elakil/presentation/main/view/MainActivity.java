package com.example.elakil.presentation.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


import com.example.elakil.R;
import com.example.elakil.presentation.auth.view.SignUpActivity;
import com.example.elakil.presentation.main.presenter.MainContract;
import com.example.elakil.presentation.main.presenter.MainPresenter;
import com.example.elakil.utils.SharedPreferencesUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements MainContract.View{


    private long pressedTime;

    private BottomNavigationView bottomNavigationView;

    private MainContract.Presenter presenter;

    private SharedPreferencesUtils sharedPreferencesUtils ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        sharedPreferencesUtils = new SharedPreferencesUtils(this);
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
            else if ((item.getItemId() == R.id.nav_favorites)){

                if (sharedPreferencesUtils.isGuestMode()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Favorites feature not available, Do you want to sign up ?");
                    builder.setTitle("Guest Mode !");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK" , (DialogInterface.OnClickListener) (dialog, which) ->{
                        Intent intent = new Intent(this, SignUpActivity.class);
                        startActivity(intent);

                        this.finish();

                    });
                    builder.setNegativeButton("No" , (DialogInterface.OnClickListener) (dialog , which)->{
                       dialog.cancel();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return false;
                }
                else {
                    presenter.navigateToFavorites();
                    return true ;
                }

            }
            else if ((item.getItemId() == R.id.nav_plan)){
                if (sharedPreferencesUtils.isGuestMode()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Plan feature not available, Do you want to sign up ?");
                    builder.setTitle("Guest Mode !");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK" , (DialogInterface.OnClickListener) (dialog, which) ->{
                        Intent intent = new Intent(this, SignUpActivity.class);
                        startActivity(intent);

                        this.finish();

                    });
                    builder.setNegativeButton("No" , (DialogInterface.OnClickListener) (dialog , which)->{
                        dialog.cancel();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    return false;
                }
                else {
                    presenter.navigateToPlan();

                    return true ;

                }


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