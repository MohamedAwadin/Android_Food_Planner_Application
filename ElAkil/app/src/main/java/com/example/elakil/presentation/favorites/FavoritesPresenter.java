package com.example.elakil.presentation.favorites;

import android.os.Handler;
import android.os.Looper;


import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.Meal;
import com.example.elakil.utils.SharedPreferencesUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesPresenter implements FavoritesContract.Presenter{

    private FavoritesContract.View view ;

    private MealsRepository repository;

    private SharedPreferencesUtils sharedPreferencesUtils;

    private ExecutorService executorService ;
    private Handler mainHandler ;

    public FavoritesPresenter(FavoritesContract.View view, MealsRepository repository, SharedPreferencesUtils sharedPreferencesUtils) {
        this.view = view            ;
        this.repository = repository;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void loadFavoriteMeals() {
//        if (sharedPreferencesUtils.isGuestMode()){
//            mainHandler.post(() -> view.showGuestMessage());
//        } else {
            executorService.execute(() -> {
                List<Meal> meals = repository.getFavoriteMeals().getValue();
                if (meals != null){
                    mainHandler.post(() -> view.showFavoritesMeals(meals));
                }
            });
        //}
    }

    @Override
    public void removeFromFavorites(Meal meal) {
        if (meal != null){
            executorService.execute(() -> {
                meal.setFavorite(false);
                repository.deleteMeals(meal , success -> {
                    mainHandler.post(() -> {
                        if (success){

                        } else {

                        }
                    });
                });
            });
        }

    }

    @Override
    public void onMealClicked(Meal meal) {
        view.navigateToMealDetails(meal);

    }
}
