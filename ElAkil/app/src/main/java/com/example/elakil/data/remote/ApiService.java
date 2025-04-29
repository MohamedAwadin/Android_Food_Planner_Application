package com.example.elakil.data.remote;

import com.example.elakil.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/json/v1/1/random.php")
    Call<MealResponse> getRandomMeal() ;
    @GET("api/json/v1/1/search.php")
    Call<MealResponse> searchMealsByName(@Query("s") String name) ;

    @GET("api/json/v1/1/filter.php")
    Call<MealResponse> filterByCategory(@Query("c") String category) ;

    @GET("api/json/v1/1/filter.php")
    Call<MealResponse> filterByArea(@Query("a") String area) ;

    @GET("api/json/v1/1/filter.php")
    Call<MealResponse> filterByIngredient(@Query("i") String ingredient) ;

    @GET("api/json/v1/1/list.php?c=list")
    Call<MealResponse> getCategories() ;

    @GET("api/json/v1/1/list.php?a=list")
    Call<MealResponse> getCountries() ;

    @GET("api/json/v1/1/lookup.php")
    Call<MealResponse> getMealDetails(@Query("i") String mealId) ;

    @GET("api/json/v1/1/list.php?i=list")
    Call<MealResponse> getIngredients();
}
