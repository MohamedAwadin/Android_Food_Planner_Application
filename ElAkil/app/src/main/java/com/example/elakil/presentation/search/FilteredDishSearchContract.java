package com.example.elakil.presentation.search;

import com.example.elakil.model.Meal;

import java.util.List;

public interface FilteredDishSearchContract {
    interface View{
        void showLoading();
        void hideLoading();
        void showError(String message);
        void showDishes(List<Meal> dishes);
        void navigateToDishDetails(String mealId);
    }
    interface Presenter{
        void loadDishes(String filterType , String filterValue);
        void onSearchTextChanged(String query);
        void onDishClicked(Meal meal);
    }
}
