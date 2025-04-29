package com.example.elakil.data.remote;


import com.example.elakil.data.MealsRepository.NetworkCallback;
import com.example.elakil.model.MealResponse;

public interface MealsRemoteDataSource {
    void getRandomMeal(NetworkCallback callback);
    void searchMealsByName(String name,NetworkCallback callback);
    void filterByCategory(String category , NetworkCallback callback);
    void filterByArea(String area , NetworkCallback callback);
    void filterByIngredient(String ingredient , NetworkCallback callback);
    void getCategories(NetworkCallback callback);
    void getCountries(NetworkCallback callback);

    void getIngredients(NetworkCallback callback);
    void getMealDetails(String mealId,NetworkCallback callback);


}
