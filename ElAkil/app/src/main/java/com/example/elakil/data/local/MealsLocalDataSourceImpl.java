package com.example.elakil.data.local;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.elakil.data.MealsRepository.LocalCallback;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource{
    private static MealsLocalDataSourceImpl instance = null ;
    private MealDao mealDao ;
    private WeekPlanDao weekPlanDao ;
    private ExecutorService executorService ;

    public MealsLocalDataSourceImpl(Context context) {
        AppDatabase database = AppDatabase.getINSTANCE(context);
        mealDao = database.mealDao();
        weekPlanDao = database.weekPlanDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public static MealsLocalDataSourceImpl getInstance(Context context){
        if (instance == null){
            instance = new MealsLocalDataSourceImpl(context);
        }
        return instance ;
    }

    @Override
    public void insertMeal(Meal meal, LocalCallback callback) {
//        executorService.execute(() -> {
//            mealDao.insertMeal(meal);
//            callback.onComplete(true);
//        });

        executorService.execute(() -> {
            try {
                Meal existingMeal = mealDao.getMealByid(meal.getIdMeal());
                if (existingMeal != null) {
                    mealDao.updateMeal(meal); // Update existing meal
                } else {
                    mealDao.insertMeal(meal); // Insert new meal
                }
                callback.onComplete(true);
                System.out.println("Debug: Inserted/Updated meal " + meal.getIdMeal() + " with isFavorite = " + meal.isFavorite());
            } catch (Exception e) {
                e.printStackTrace();
                callback.onComplete(false);
            }
        });


    }

    @Override
    public void deleteMeal(Meal meal, LocalCallback callback) {
        executorService.execute(() -> {
            mealDao.deleteMeal(meal);
            callback.onComplete(true);
        });

    }

    @Override
    public LiveData<List<Meal>> getFavoriteMeals() {
        return mealDao.getFavoriteMeals();
    }

    @Override
    public Meal getMealById(String mealId) {
        return mealDao.getMealByid(mealId);
    }

    @Override
    public void insertWeeklyPlan(WeeklyPlan plan, MealsRepository.LocalCallback callback) {
        executorService.execute(() -> {
            weekPlanDao.insertWeeklyPlan(plan);
            callback.onComplete(true);
        });

    }

    @Override
    public void deleteWeeklyPlan(WeeklyPlan plan, MealsRepository.LocalCallback callback) {
        executorService.execute(() -> {
            weekPlanDao.deleteWeeklyPlan(plan);
            callback.onComplete(true);
        });

    }

    @Override
    public LiveData<List<WeeklyPlan>> getWeeklyPlans(long weekStartDate) {
        return weekPlanDao.getWeeklyPlans(weekStartDate);
    }

    @Override
    public LiveData<Meal> getMealByIdLiveData(String mealId) {
        MutableLiveData<Meal> liveData = new MutableLiveData<>();
        executorService.execute(() -> {
            Meal meal = mealDao.getMealByid(mealId);
            liveData.postValue(meal);
        });
        return liveData;
    }

    @Override
    public void clearAllData(LocalCallback callback) {
        executorService.execute(() -> {
            mealDao.clearMeals();
            weekPlanDao.clearWeeklyPlans();
            callback.onComplete(true);
        });
    }
}
