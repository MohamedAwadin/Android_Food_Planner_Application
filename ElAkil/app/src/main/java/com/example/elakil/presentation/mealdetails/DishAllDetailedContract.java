package com.example.elakil.presentation.mealdetails;

import com.example.elakil.model.Ingredient;
import com.example.elakil.model.IngredientItem;
import com.example.elakil.model.Meal;

import java.util.List;

public interface DishAllDetailedContract {
    interface View{
        void showMealDetails(Meal meal);
        void showIngredients(List<IngredientItem> ingredients);
        void showLoading();
        void hideLoading();
        void showError(String message);
        void updateFavoriteButton(boolean isFavorite);
        void navigateBack();

        void onDestroy();

    }

    interface Presenter{
        void loadMealDetails(String mealId);
        void toggleFavorite();
    }
}
