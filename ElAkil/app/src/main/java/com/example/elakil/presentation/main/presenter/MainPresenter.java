package com.example.elakil.presentation.main.presenter;

import com.example.elakil.presentation.favorites.FavoritesFragment;
import com.example.elakil.presentation.home.HomeFragment;
import com.example.elakil.presentation.plan.PlanFragment;
import com.example.elakil.presentation.profile.ProfileFragment;
import com.example.elakil.presentation.search.SearchFragment;

public class MainPresenter implements MainContract.Presenter{
    private MainContract.View view ;

    public MainPresenter(MainContract.View view){
        this.view = view ;
    }

    @Override
    public void navigateToHome() {
        view.showFragment(new HomeFragment());

    }

    @Override
    public void navigateToSearch() {
        view.showFragment(new SearchFragment());

    }

    @Override
    public void navigateToFavorites() {
            view.showFragment(new FavoritesFragment());
    }

    @Override
    public void navigateToPlan() {
            view.showFragment(new PlanFragment());
    }

    @Override
    public void navigateToProfile() {
        view.showFragment(new ProfileFragment());

    }
}
