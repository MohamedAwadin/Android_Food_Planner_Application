package com.example.elakil.model.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.elakil.model.WeeklyPlan;

import java.util.List;

@Dao
public interface WeekPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeeklyPlan(WeeklyPlan plan);

    @Delete
    void deleteWeeklyPlan(WeeklyPlan plan);


    @Query("DELETE FROM weekly_plans WHERE mealId = :mealId AND dayOfWeek = :dayOfWeek AND weekStartDate = :weekStartDate")
    void deleteWeeklyPlanByDetails(String mealId, String dayOfWeek, long weekStartDate);

    @Query("SELECT * FROM weekly_plans WHERE weekStartDate= :weekStartDate")
    LiveData<List<WeeklyPlan>> getWeeklyPlans(long weekStartDate);

    @Query("DELETE FROM weekly_plans")
    void clearWeeklyPlans();

}
