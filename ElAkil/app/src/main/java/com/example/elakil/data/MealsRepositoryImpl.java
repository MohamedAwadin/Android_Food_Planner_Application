package com.example.elakil.data;

import androidx.lifecycle.LiveData;

import com.airbnb.lottie.L;
import com.example.elakil.data.local.MealsLocalDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.model.CategoryListResponse;
import com.example.elakil.model.CountryListResponse;
import com.example.elakil.model.IngredientListResponse;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealListResponse;
import com.example.elakil.model.WeeklyPlan;

import java.util.List;

public class MealsRepositoryImpl implements MealsRepository{
    private static MealsRepositoryImpl instance = null;
    private MealsRemoteDataSource remoteDataSource ;
    private MealsLocalDataSource localDataSource ;

    public MealsRepositoryImpl(MealsRemoteDataSource remoteDataSource, MealsLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    public static MealsRepositoryImpl getInstance(MealsRemoteDataSource remoteDataSource , MealsLocalDataSource localDataSource){
        if (instance == null){
            instance = new MealsRepositoryImpl(remoteDataSource , localDataSource);
        }
        return instance;
    }

    @Override
    public void getRandomMeal(NetworkCallback<MealListResponse> callback) {
        remoteDataSource.getRandomMeal(callback);

    }

    @Override
    public void searchMealsByName(String name, NetworkCallback<MealListResponse> callback) {
        remoteDataSource.searchMealsByName(name ,  callback);

    }

    @Override
    public void filterByCategory(String category, NetworkCallback<MealListResponse> callback) {
        remoteDataSource.filterByCategory(category ,  callback);

    }

    @Override
    public void filterByArea(String area, NetworkCallback<MealListResponse> callback) {
        remoteDataSource.filterByArea(area ,  callback);

    }

    @Override
    public void filterByIngredient(String ingredient, NetworkCallback<MealListResponse> callback) {

        remoteDataSource.filterByIngredient(ingredient , callback);
    }

    @Override
    public void getCategories(NetworkCallback<CategoryListResponse> callback) {
        remoteDataSource.getCategories( callback);

    }

    @Override
    public void getCountries(NetworkCallback<CountryListResponse> callback) {
        remoteDataSource.getCountries( callback);

    }

    @Override
    public void getIngredients(NetworkCallback<IngredientListResponse> callback) {
        remoteDataSource.getIngredients(callback);
    }

    @Override
    public void getMealDetails(String mealId, NetworkCallback<MealListResponse> callback) {
        remoteDataSource.getMealDetails(mealId ,  callback);

    }

    @Override
    public void insertMeals(Meal meal, LocalCallback callback) {
        localDataSource.insertMeal(meal, callback);

    }

    @Override
    public void deleteMeals(Meal meal, LocalCallback callback) {
        localDataSource.deleteMeal(meal  , callback);

    }

    @Override
    public LiveData<List<Meal>> getFavoriteMeals() {
        return localDataSource.getFavoriteMeals();
    }

    @Override
    public Meal getMealById(String mealId) {
        return localDataSource.getMealById(mealId);
    }

    @Override
    public void insertWeeklyPlan(WeeklyPlan plan, LocalCallback callback) {
        localDataSource.insertWeeklyPlan(plan,callback);

    }

    @Override
    public void deleteWeeklyPlan(WeeklyPlan plan, LocalCallback callback) {
        localDataSource.deleteWeeklyPlan(plan, callback);

    }

    @Override
    public LiveData<List<WeeklyPlan>> getWeeklyPlans(long weekStartDate) {
        return localDataSource.getWeeklyPlans(weekStartDate);
    }

    @Override
    public LiveData<Meal> getMealByIdLiveData(String mealId) {
        return localDataSource.getMealByIdLiveData(mealId);
    }
}
