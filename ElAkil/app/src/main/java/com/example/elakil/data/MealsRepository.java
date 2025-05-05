package com.example.elakil.data;

import androidx.lifecycle.LiveData;

import com.airbnb.lottie.L;
import com.example.elakil.model.CategoryListResponse;
import com.example.elakil.model.CountryListResponse;
import com.example.elakil.model.IngredientListResponse;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealListResponse;
import com.example.elakil.model.WeeklyPlan;

import java.util.List;

public interface MealsRepository {
    void getRandomMeal(NetworkCallback<MealListResponse> callback);
    void searchMealsByName(String name, NetworkCallback<MealListResponse> callback);
    void filterByCategory(String category , NetworkCallback<MealListResponse> callback);
    void filterByArea(String area , NetworkCallback<MealListResponse> callback);
    void filterByIngredient(String ingredient , NetworkCallback<MealListResponse> callback);
    void getCategories(NetworkCallback<CategoryListResponse> callback);
    void getCountries(NetworkCallback<CountryListResponse> callback);
    void getIngredients(NetworkCallback<IngredientListResponse> callback);
    void getMealDetails(String mealId, NetworkCallback<MealListResponse> callback);
    void insertMeals(Meal meal , LocalCallback callback);
    void deleteMeals(Meal meal , LocalCallback callback);

    LiveData<List<Meal>> getFavoriteMeals();
    Meal getMealById(String mealId);

    void insertWeeklyPlan(WeeklyPlan plan , LocalCallback callback);
    void deleteWeeklyPlan(WeeklyPlan plan , LocalCallback callback);
    LiveData<List<WeeklyPlan>> getWeeklyPlans(long weekStartDate);

    LiveData<Meal> getMealByIdLiveData(String mealId);


    interface NetworkCallback<T>{
        void onSuccess(T response);
        void onFailure(String errorMessage);
    }
    interface LocalCallback {
        void onComplete(boolean success);
    }
}
