package com.example.elakil.model.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;

import java.util.List;

@Dao
public interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMeal(Meal meal);

    @Update
    void updateMeal(Meal meal);

    @Delete
    void deleteMeal(Meal meal);

    @Query("SELECT * FROM meals WHERE isFavorite = 1")
    LiveData<List<Meal>> getFavoriteMeals();

    @Query("SELECT * FROM meals WHERE idMeal = :mealId LIMIT 1")
    Meal getMealByid(String mealId);


    @Query("DELETE FROM meals")
    void clearMeals();



}
