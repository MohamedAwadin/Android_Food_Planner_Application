package com.example.elakil.data.local;

import androidx.lifecycle.LiveData;

import com.example.elakil.data.MealsRepository.LocalCallback;
import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;

import java.util.List;

public interface MealsLocalDataSource {
    void insertMeal(Meal meal , LocalCallback callback);
    void deleteMeal(Meal meal , LocalCallback callback);
    LiveData<List<Meal>> getFavoriteMeals();
    Meal getMealById(String mealId);

    void insertWeeklyPlan(WeeklyPlan plan , LocalCallback callback);
    void deleteWeeklyPlan(WeeklyPlan plan , LocalCallback callback);
    LiveData<List<WeeklyPlan>> getWeeklyPlans(long weekStartDate);

    LiveData<Meal> getMealByIdLiveData(String mealId);


}
