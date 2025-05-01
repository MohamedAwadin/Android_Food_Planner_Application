package com.example.elakil.data.remote;

import com.example.elakil.data.MealsRepository.NetworkCallback;
import com.example.elakil.model.CategoryListResponse;
import com.example.elakil.model.CountryListResponse;
import com.example.elakil.model.IngredientListResponse;
import com.example.elakil.model.MealListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSourceImpl implements MealsRemoteDataSource{
    private static final String BASE_URL = "https://themealdb.com/";
    private static MealsRemoteDataSourceImpl instance = null ;
    private final ApiService apiService ;

    public MealsRemoteDataSourceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static MealsRemoteDataSourceImpl getInstance(){
        if (instance == null){
            instance = new MealsRemoteDataSourceImpl();
        }
        return instance ;
    }

    @Override
    public void getRandomMeal(NetworkCallback<MealListResponse> callback) {
        apiService.getRandomMeal().enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.body());
                }else {
                    callback.onFailure("Failed to fetch random meal");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });

    }



    @Override
    public void searchMealsByName(String name, NetworkCallback<MealListResponse> callback) {
        apiService.searchMealsByName(name).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to search meals");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });

    }

    @Override
    public void filterByCategory(String category, NetworkCallback<MealListResponse> callback) {
        apiService.filterByCategory(category).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to filter by category");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });

    }

    @Override
    public void filterByArea(String area, NetworkCallback<MealListResponse> callback) {
        apiService.filterByArea(area).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to filter by area");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });

    }

    @Override
    public void filterByIngredient(String ingredient, NetworkCallback<MealListResponse> callback) {
        apiService.filterByIngredient(ingredient).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to filter by ingredient");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });

    }

    @Override
    public void getCategories(NetworkCallback<CategoryListResponse> callback) {
        apiService.getCategories().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });

    }

    @Override
    public void getCountries(NetworkCallback<CountryListResponse> callback) {
        apiService.getCountries().enqueue(new Callback<CountryListResponse>() {
            @Override
            public void onResponse(Call<CountryListResponse> call, Response<CountryListResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch countries");
                }
            }

            @Override
            public void onFailure(Call<CountryListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });

    }

    @Override
    public void getIngredients(NetworkCallback<IngredientListResponse> callback) {
        apiService.getIngredients().enqueue(new Callback<IngredientListResponse>() {
            @Override
            public void onResponse(Call<IngredientListResponse> call, Response<IngredientListResponse> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.body());
                }else {
                    callback.onFailure("Failed to fetch ingredients");
                }
            }

            @Override
            public void onFailure(Call<IngredientListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });


    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<MealListResponse> callback) {
        apiService.getMealDetails(mealId).enqueue(new Callback<MealListResponse>() {
            @Override
            public void onResponse(Call<MealListResponse> call, Response<MealListResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("Failed to fetch meal details");
                }
            }

            @Override
            public void onFailure(Call<MealListResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });

    }

}
