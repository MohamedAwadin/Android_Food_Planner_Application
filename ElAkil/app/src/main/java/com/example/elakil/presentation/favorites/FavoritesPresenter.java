package com.example.elakil.presentation.favorites;

import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.Meal;
import com.example.elakil.utils.SharedPreferencesUtils;

public class FavoritesPresenter implements FavoritesContract.Presenter{

    private FavoritesContract.View view ;

    private MealsRepository repository;

    private SharedPreferencesUtils sharedPreferencesUtils;

    public FavoritesPresenter(FavoritesContract.View view, MealsRepository repository, SharedPreferencesUtils sharedPreferencesUtils) {
        this.view = view;
        this.repository = repository;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
    }

    @Override
    public void loadFavoriteMeals() {
        if (sharedPreferencesUtils.isGuestMode()){
            view.showGuestMessage();
        }
        else {
            repository.getFavoriteMeals().observeForever(meals -> {
                if (meals != null){
                    view.showFavoritesMeals(meals);
                }
            });
        }
    }

    @Override
    public void removeFromFavorites(Meal meal) {
        meal.setFavorite(false);
        repository.deleteMeals(meal , success -> {
            loadFavoriteMeals();
        });

    }

    @Override
    public void onMealClicked(Meal meal) {
        view.navigateToMealDetails(meal);

    }
}
