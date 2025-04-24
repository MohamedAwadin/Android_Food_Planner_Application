package com.example.elakil.presentation.main.presenter;

import androidx.fragment.app.Fragment;

public interface MainContract {
    interface View {
        void showFragment(Fragment fragment);
    }

    interface Presenter {
        void navigateToHome();
        void navigateToSearch();
        void navigateToFavorites();
        void navigateToPlan();
        void navigateToProfile();
    }
}