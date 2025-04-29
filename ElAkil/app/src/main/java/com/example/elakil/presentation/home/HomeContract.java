package com.example.elakil.presentation.home;

import com.example.elakil.model.Meal;

import java.util.List;

public interface HomeContract {
    interface View{
        void showDailyRecommendation(List<Meal> meals);
        void showMoreMeals(List<Meal> meals);
        void showLoading();
        void hideLoading();
        void showError(String message);
        void navigateToMealDetails(Meal meal);
    }

    interface Presenter{
        void loadDailyRecommendation();
        void loadMoreMeals();
        void onMealClicked(Meal meal);
    }
}
