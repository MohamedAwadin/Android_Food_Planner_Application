package com.example.elakil.presentation.profile;

import com.example.elakil.data.MealsRepository;
import com.example.elakil.presentation.auth.presenter.AuthContract;
import com.example.elakil.presentation.auth.presenter.AuthPresenter;
import com.example.elakil.utils.SharedPreferencesUtils;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class ProfilePresenter implements ProfileContract.Presenter{
    private ProfileContract.View view;
    private MealsRepository repository;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private FirebaseAuth firebaseAuth;
    private AuthPresenter authPresenter;

    public ProfilePresenter(ProfileContract.View view, MealsRepository repository, SharedPreferencesUtils sharedPreferencesUtils) {
        this.view = view;
        this.repository = repository;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.authPresenter = new AuthPresenter((AuthContract.LoginView) null , sharedPreferencesUtils);
    }

    @Override
    public void loadProfile() {
        if (sharedPreferencesUtils.isGuestMode()){
            view.showProfile("Guest" , "N/A");
        }
        else {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null){
                String username = user.getDisplayName() != null ? user.getDisplayName() : "User" ;
                String email    = user.getEmail() != null ? user.getEmail() : "N/A" ;
                view.showProfile(username , email);
            }else {
                view.showProfile("User" , "N/A");
            }
        }
    }

    @Override
    public void logout() {
        authPresenter.logout();
        view.navigateToLogin();

    }
}
