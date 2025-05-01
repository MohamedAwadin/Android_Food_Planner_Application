package com.example.elakil.data.remote;

import com.example.elakil.model.CategoryListResponse;
import com.example.elakil.model.CountryListResponse;
import com.example.elakil.model.IngredientListResponse;
import com.example.elakil.model.MealListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/json/v1/1/random.php")
    Call<MealListResponse> getRandomMeal() ;
    @GET("api/json/v1/1/search.php")
    Call<MealListResponse> searchMealsByName(@Query("s") String name) ;

    @GET("api/json/v1/1/filter.php")
    Call<MealListResponse> filterByCategory(@Query("c") String category) ;

    @GET("api/json/v1/1/filter.php")
    Call<MealListResponse> filterByArea(@Query("a") String area) ;

    @GET("api/json/v1/1/filter.php")
    Call<MealListResponse> filterByIngredient(@Query("i") String ingredient) ;

    @GET("api/json/v1/1/list.php?c=list")
    Call<CategoryListResponse> getCategories() ;

    @GET("api/json/v1/1/list.php?a=list")
    Call<CountryListResponse> getCountries() ;

    @GET("api/json/v1/1/lookup.php")
    Call<MealListResponse> getMealDetails(@Query("i") String mealId) ;

    @GET("api/json/v1/1/list.php?i=list")
    Call<IngredientListResponse> getIngredients();
}
