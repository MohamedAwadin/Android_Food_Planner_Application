package com.example.elakil.presentation.profile;

public interface ProfileContract {
    interface View{
        void showProfile(String username , String email );
        void navigateToLogin();
    }
    interface Presenter {
        void loadProfile();
        void logout();
    }
}
