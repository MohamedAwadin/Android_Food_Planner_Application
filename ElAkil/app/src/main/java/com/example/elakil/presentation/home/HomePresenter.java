package com.example.elakil.presentation.home;

import android.util.Log;
import com.example.elakil.model.data.MealsRepository;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealListResponse;

public class HomePresenter implements HomeContract.Presenter {

    private static final String TAG = "HomePresenter";
    private HomeContract.View view;
    private MealsRepository repository;

    public HomePresenter(HomeContract.View view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadDailyRecommendation() {
        if (!view.isNetworkAvailable()) {
            view.hideLoading();
            view.showNoInternetLayout();
            return;
        }

        view.showLoading();
        repository.getRandomMeal(new MealsRepository.NetworkCallback<MealListResponse>() {
            @Override
            public void onSuccess(MealListResponse response) {
                view.hideLoading();
                if (response != null && response.getMeals() != null) {
                    view.hideNoInternetLayout();
                    view.showDailyRecommendation(response.getMeals());
                } else {
                    view.showError("No daily recommendation available");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                Log.e(TAG, "loadDailyRecommendation failed: " + errorMessage);
                if (isNetworkError(errorMessage)) {
                    view.showNoInternetLayout();
                } else {
                    view.showError(errorMessage);
                }
            }
        });
    }

    @Override
    public void loadMoreMeals() {
        if (!view.isNetworkAvailable()) {
            view.hideLoading();
            view.showNoInternetLayout();
            return;
        }

        view.showLoading();
        repository.filterByCategory("Dessert", new MealsRepository.NetworkCallback<MealListResponse>() {
            @Override
            public void onSuccess(MealListResponse response) {
                view.hideLoading();
                if (response != null && response.getMeals() != null) {
                    view.hideNoInternetLayout();
                    view.showMoreMeals(response.getMeals());
                } else {
                    view.showError("No meals available");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                Log.e(TAG, "loadMoreMeals failed: " + errorMessage);
                if (isNetworkError(errorMessage)) {
                    view.showNoInternetLayout();
                } else {
                    view.showError(errorMessage);
                }
            }
        });
    }

    @Override
    public void onMealClicked(Meal meal) {
        view.navigateToMealDetails(meal);
    }

    @Override
    public void retryLoading() {
        loadDailyRecommendation();
        loadMoreMeals();
    }

    private boolean isNetworkError(String errorMessage) {
        if (errorMessage == null) return false;
        String msg = errorMessage.toLowerCase();
        return msg.contains("network") ||
                msg.contains("internet") ||
                msg.contains("failed to connect") ||
                msg.contains("unable to resolve") ||
                msg.contains("timeout") ||
                msg.contains("no route to host");
    }
}