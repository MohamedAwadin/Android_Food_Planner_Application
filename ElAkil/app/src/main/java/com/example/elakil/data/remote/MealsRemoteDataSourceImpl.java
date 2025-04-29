package com.example.elakil.data.remote;

import com.example.elakil.model.MealResponse;
import com.example.elakil.data.MealsRepository.NetworkCallback;

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
    public void getRandomMeal(NetworkCallback callback) {
        Call<MealResponse> call = apiService.getRandomMeal();
        enqueueCall(call, callback);

    }



    @Override
    public void searchMealsByName(String name, NetworkCallback callback) {
        Call<MealResponse> call = apiService.searchMealsByName(name);
        enqueueCall(call, callback);

    }

    @Override
    public void filterByCategory(String category, NetworkCallback callback) {
        Call<MealResponse> call = apiService.filterByCategory(category);
        enqueueCall(call, callback);

    }

    @Override
    public void filterByArea(String area, NetworkCallback callback) {
        Call<MealResponse> call = apiService.filterByArea(area);
        enqueueCall(call, callback);

    }

    @Override
    public void filterByIngredient(String ingredient, NetworkCallback callback) {
        Call<MealResponse> call = apiService.filterByIngredient(ingredient);
        enqueueCall(call, callback);

    }

    @Override
    public void getCategories(NetworkCallback callback) {
        Call<MealResponse> call = apiService.getCategories();
        enqueueCall(call, callback);

    }

    @Override
    public void getCountries(NetworkCallback callback) {
        Call<MealResponse> call = apiService.getCountries();
        enqueueCall(call, callback);

    }

    @Override
    public void getIngredients(NetworkCallback callback) {
        apiService.getIngredients().enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful()){
                    callback.onSuccess(response.body());
                }
                else {
                    callback.onFailure("Failed to fetch ingredients");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });

    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback callback) {
        Call<MealResponse> call = apiService.getMealDetails(mealId);
        enqueueCall(call, callback);

    }

    private void enqueueCall(Call<MealResponse> call, NetworkCallback callback) {
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    callback.onSuccess(response.body());
                }
                else {
                    callback.onFailure("Failed to Fetch Data");
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());

            }
        });
    }
}
