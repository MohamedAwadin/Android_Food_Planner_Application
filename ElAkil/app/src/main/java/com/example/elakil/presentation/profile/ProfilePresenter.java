package com.example.elakil.presentation.profile;

import android.util.Log;

import com.example.elakil.model.data.FirebaseSyncRepository;
import com.example.elakil.model.data.MealsRepository;
import com.example.elakil.presentation.auth.presenter.AuthContract;
import com.example.elakil.presentation.auth.presenter.AuthPresenter;
import com.example.elakil.utils.SharedPreferencesUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilePresenter implements ProfileContract.Presenter{

    private static final String TAG = "ProfilePresenter";

    private ProfileContract.View view;
    private MealsRepository repository;
    private FirebaseSyncRepository firebaseSyncRepository;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private FirebaseAuth firebaseAuth;
    private AuthPresenter authPresenter;

    public ProfilePresenter(ProfileContract.View view, MealsRepository repository, SharedPreferencesUtils sharedPreferencesUtils, FirebaseSyncRepository firebaseSyncRepository) {
        this.view = view;
        this.repository = repository;
        this.firebaseSyncRepository = firebaseSyncRepository ;
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

        repository.clearLocalData(success -> {
            if (success) {

                Log.d(TAG , "Debug: Cleared local database on logout");
            } else {

                Log.d(TAG , "Debug: Failed to clear local database on logout");
            }
            authPresenter.logout();
            view.navigateToLogin();
        });

    }
}
