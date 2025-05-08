package com.example.elakil.presentation.favorites;

import com.airbnb.lottie.L;
import com.example.elakil.model.Meal;

import java.util.List;

public class FavoritesContract {
    interface View {
        void showFavoritesMeals(List<Meal> meals);
        //void showGuestMessage();
        void navigateToMealDetails(Meal meal);

    }

    interface Presenter{
        void loadFavoriteMeals();
        void removeFromFavorites(Meal meal);
        void onMealClicked(Meal meal);

    }
}
