package com.example.elakil.presentation.auth.presenter;

import com.example.elakil.utils.SharedPreferencesUtils;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthPresenter implements AuthContract.Presenter{
    private AuthContract.LoginView loginView ;
    private AuthContract.SignUpView signUpView;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private FirebaseAuth firebaseAuth;

    public AuthPresenter(AuthContract.LoginView loginView, SharedPreferencesUtils sharedPreferencesUtils) {
        this.loginView = loginView;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    public AuthPresenter(AuthContract.SignUpView signUpView, SharedPreferencesUtils sharedPreferencesUtils) {
        this.signUpView = signUpView;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void loginWithEmail(String email, String password) {
        if (email.isEmpty() || password.isEmpty()){
            if (loginView != null){
                loginView.showError("Email or Password cannot be empty");
            }
            return;
        }
        loginView.showLoading();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    loginView.hideLoding();
                    if (task.isSuccessful()){
                        sharedPreferencesUtils.setLoggedIn(true);
                        sharedPreferencesUtils.setGuestMode(false);
                        loginView.navigateToMain();
                    }
                    else {
                        loginView.showError("Login Failed: " + task.getException().getMessage());
                    }
                });
    }

    @Override
    public void loginWithGoogle(String idToken) {
        loginView.showLoading();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken ,null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        sharedPreferencesUtils.setLoggedIn(true);
                        sharedPreferencesUtils.setGuestMode(false);
                        loginView.navigateToMain();
                    }
                    else {
                        loginView.showError("Google Sign-In Failed: " + task.getException().getMessage());
                    }
                });

    }

    @Override
    public void loginAsGuest() {
        sharedPreferencesUtils.setLoggedIn(true);
        sharedPreferencesUtils.setGuestMode(true);
        loginView.navigateToMain();

    }

    @Override
    public void navigateToSignUp() {
        loginView.navigateToSignUp();

    }

    @Override
    public void signUp(String username, String email, String password, String confirmPassword) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty() || confirmPassword.isEmpty()){
            signUpView.showError("All Fields are required");
            return;
        }
        if (!password.equals(confirmPassword)){
            signUpView.showError("Password do not match");
            return;
        }
        signUpView.showLoading();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                   signUpView.hideLoding();
                   if (task.isSuccessful()){
                       FirebaseUser user = firebaseAuth.getCurrentUser();
                       if (user != null){
                           user.updateProfile(new UserProfileChangeRequest.Builder()
                                   .setDisplayName(username)
                                   .build());
                       }
                       sharedPreferencesUtils.setLoggedIn(true);
                       sharedPreferencesUtils.setGuestMode(false);
                       signUpView.navigateToMain();
                   }
                   else {
                       signUpView.showError("Sign Up Failed: " + task.getException().getMessage());
                   }
                });
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
        sharedPreferencesUtils.clear();

    }

    @Override
    public void signUpWithGoogle(String idToken) {
        if (signUpView == null) return;
        signUpView.showLoading();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken , null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    signUpView.hideLoding();
                    if (task.isSuccessful())
                    {
                        System.out.println("Debug: Google Sign-Up successful");
                        sharedPreferencesUtils.setLoggedIn(true);
                        sharedPreferencesUtils.setGuestMode(false);
                        signUpView.navigateToMain();
                    }
                    else {
                        System.out.println("Debug: Google Sign-Up failed: " + task.getException().getMessage());
                        signUpView.showError("Google Sign-Up Failed: " + task.getException().getMessage());
                    }
                });
    }
}
