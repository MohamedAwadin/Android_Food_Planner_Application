package com.example.elakil.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;

@Database(entities = {Meal.class, WeeklyPlan.class}, version = 1 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MealDao mealDao();
    public abstract WeekPlanDao weekPlanDao();
    private static volatile AppDatabase INSTANCE ;

    public static AppDatabase getINSTANCE(Context context) {
        if (INSTANCE==null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext() , AppDatabase.class, "food_planner_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
