package com.example.elakil.data;

import androidx.lifecycle.LiveData;

import com.example.elakil.data.remote.MealsRemoteDataSource;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealResponse;
import com.example.elakil.model.WeeklyPlan;

import java.util.List;

public interface MealsRepository {
    void getRandomMeal(NetworkCallback callback);
    void searchMealsByName(String name, NetworkCallback callback);
    void filterByCategory(String category , NetworkCallback callback);
    void filterByArea(String area , NetworkCallback callback);
    void filterByIngredient(String ingredient , NetworkCallback callback);
    void getCategories(NetworkCallback callback);
    void getCountries(NetworkCallback callback);
    void getMealDetails(String mealId, NetworkCallback callback);
    void insertMeals(Meal meal , LocalCallback callback);
    void deleteMeals(Meal meal , LocalCallback callback);

    LiveData<List<Meal>> getFavoriteMeals();
    Meal getMealById(String mealId);

    void insertWeeklyPlan(WeeklyPlan plan , LocalCallback callback);
    void deleteWeeklyPlan(WeeklyPlan plan , LocalCallback callback);
    LiveData<List<WeeklyPlan>> getWeeklyPlans(long weekStartDate);


    interface NetworkCallback{
        void onSuccess(MealResponse response);
        void onFailure(String errorMessage);
    }
    interface LocalCallback {
        void onComplete(boolean success);
    }
}
