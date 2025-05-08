package com.example.elakil.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weekly_plans")
public class WeeklyPlan {
    @PrimaryKey(autoGenerate = true)
    private int id ;
    @ColumnInfo(name = "mealId")
    private String mealId ;
    @ColumnInfo(name = "dayOfWeek")
    private String dayOfWeek;
    @ColumnInfo(name = "weekStartDate")
    private long weekStartDate ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public long getWeekStartDate() {
        return weekStartDate;
    }

    public void setWeekStartDate(long weekStartDate) {
        this.weekStartDate = weekStartDate;
    }
}
