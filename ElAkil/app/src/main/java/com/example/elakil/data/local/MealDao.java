package com.example.elakil.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;

import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMeal(Meal meal);

    @Delete
    void deleteMeal(Meal meal);

    @Query("SELECT * FROM meals WHERE isFavorite = 1")
    LiveData<List<Meal>> getFavoriteMeals();

    @Query("SELECT * FROM meals WHERE idMeal = :mealId LIMIT 1")
    Meal getMealByid(String mealId);

    @Insert
    void insertWeeklyPlan(WeeklyPlan plan);

    @Delete
    void deleteWeeklyPlan(WeeklyPlan plan);

    @Query("SELECT * FROM weekly_plans WHERE weekStartDate = :weekStartDate")
    LiveData<List<WeeklyPlan>> getWeeklyPlans(long weekStartDate );



}
