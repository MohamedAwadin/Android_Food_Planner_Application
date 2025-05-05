package com.example.elakil.presentation.home;

import android.widget.Toast;

import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealListResponse;

public class HomePresenter implements HomeContract.Presenter{

    private HomeContract.View view;
    private MealsRepository repository ;

    public HomePresenter(HomeContract.View view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadDailyRecommendation() {
        view.showLoading();
        repository.getRandomMeal(new MealsRepository.NetworkCallback<MealListResponse>() {
            @Override
            public void onSuccess(MealListResponse response) {
                view.hideLoading();
                if (response != null && response.getMeals() != null){
                    view.showDailyRecommendation(response.getMeals());
                }
                else {
                    view.showError("No daily recommendation Available");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();;
                view.showError(errorMessage);
            }
        });

    }

    @Override
    public void loadMoreMeals() {
        view.showLoading();
        repository.filterByCategory("Dessert", new MealsRepository.NetworkCallback<MealListResponse>() {
            @Override
            public void onSuccess(MealListResponse response) {
                view.hideLoading();
                if (response != null && response.getMeals() != null){
                    view.showMoreMeals(response.getMeals());
                }
                else {
                    view.showError("No meals available");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showError(errorMessage);

            }
        });

    }

    @Override
    public void onMealClicked(Meal meal) {
        view.navigateToMealDetails(meal);

    }
}
