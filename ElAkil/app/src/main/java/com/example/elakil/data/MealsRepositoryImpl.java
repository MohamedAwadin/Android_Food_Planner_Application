package com.example.elakil.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.airbnb.lottie.L;

import com.example.elakil.data.local.MealsLocalDataSource;
import com.example.elakil.data.remote.FirebaseDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.model.CategoryListResponse;
import com.example.elakil.model.CountryListResponse;
import com.example.elakil.model.IngredientListResponse;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealListResponse;
import com.example.elakil.model.WeeklyPlan;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsRepositoryImpl implements MealsRepository{

    private static final String TAG = "MealsRepositoryImpl" ;
    private static MealsRepositoryImpl instance = null;
    private MealsRemoteDataSource remoteDataSource ;
    private MealsLocalDataSource localDataSource ;

    private FirebaseSyncRepository firebaseSyncRepository;



    public MealsRepositoryImpl(MealsRemoteDataSource remoteDataSource, MealsLocalDataSource localDataSource , FirebaseSyncRepository firebaseSyncRepository) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.firebaseSyncRepository = firebaseSyncRepository;
    }


    public static MealsRepositoryImpl getInstance(MealsRemoteDataSource remoteDataSource , MealsLocalDataSource localDataSource, FirebaseSyncRepository firebaseSyncRepository){
        if (instance == null){
            instance = new MealsRepositoryImpl(remoteDataSource , localDataSource , firebaseSyncRepository);
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
        localDataSource.insertMeal(meal, success -> {
            callback.onComplete(success);
            if (success && meal.isFavorite()) {
                firebaseSyncRepository.syncFavoriteMeal(meal, new FirebaseDataSource.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"Debug: Successfully synced meal " + meal.getIdMeal() + " to Firebase");
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.d(TAG,"Debug: Failed to sync meal to Firebase: " + errorMessage);
                    }
                });
            }
        });

    }

    @Override
    public void deleteMeals(Meal meal, LocalCallback callback) {
        localDataSource.deleteMeal(meal, success -> {
            callback.onComplete(success);
            if (success && !meal.isFavorite()) {
                firebaseSyncRepository.syncFavoriteMeal(meal, new FirebaseDataSource.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"Debug: Successfully removed meal " + meal.getIdMeal() + " from Firebase favorites");
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.d(TAG,"Debug: Failed to remove meal from Firebase: " + errorMessage);
                    }
                });
            }
        });

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
        localDataSource.insertWeeklyPlan(plan, success -> {
            callback.onComplete(success);
            if (success) {
                firebaseSyncRepository.syncWeeklyPlan(plan, new FirebaseDataSource.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"Debug: Successfully synced weekly plan for meal " + plan.getMealId() + " to Firebase");
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.d(TAG,"Debug: Failed to sync weekly plan to Firebase: " + errorMessage);
                    }
                });
            }
        });

    }

    @Override
    public void deleteWeeklyPlan(WeeklyPlan plan, LocalCallback callback) {
        localDataSource.deleteWeeklyPlan(plan, success -> {
            callback.onComplete(success);
            if (success) {
                firebaseSyncRepository.syncWeeklyPlan(plan, new FirebaseDataSource.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG,"Debug: Successfully removed weekly plan for meal " + plan.getMealId() + " from Firebase");
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Log.d(TAG,"Debug: Failed to remove weekly plan from Firebase: " + errorMessage);
                    }
                });
            }
        });

    }

    @Override
    public LiveData<List<WeeklyPlan>> getWeeklyPlans(long weekStartDate) {
        return localDataSource.getWeeklyPlans(weekStartDate);
    }

    @Override
    public LiveData<Meal> getMealByIdLiveData(String mealId) {
        return localDataSource.getMealByIdLiveData(mealId);
    }



    @Override
    public void clearLocalData(LocalCallback callback) {

        executorService.execute(() -> {
            localDataSource.clearAllData(callback);
        });

    }



    public ExecutorService executorService = Executors.newSingleThreadExecutor();
}
