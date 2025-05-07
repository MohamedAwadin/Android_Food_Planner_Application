package com.example.elakil.model.data.remote;


import com.example.elakil.model.data.MealsRepository.NetworkCallback;
import com.example.elakil.model.CategoryListResponse;
import com.example.elakil.model.CountryListResponse;
import com.example.elakil.model.IngredientListResponse;
import com.example.elakil.model.MealListResponse;

public interface MealsRemoteDataSource {
    void getRandomMeal(NetworkCallback<MealListResponse> callback);
    void searchMealsByName(String name,NetworkCallback<MealListResponse> callback);
    void filterByCategory(String category , NetworkCallback<MealListResponse> callback);
    void filterByArea(String area , NetworkCallback<MealListResponse> callback);
    void filterByIngredient(String ingredient , NetworkCallback<MealListResponse> callback);
    void getCategories(NetworkCallback<CategoryListResponse> callback);
    void getCountries(NetworkCallback<CountryListResponse> callback);

    void getIngredients(NetworkCallback<IngredientListResponse> callback);
    void getMealDetails(String mealId,NetworkCallback<MealListResponse> callback);


}
