package com.example.elakil.presentation.plan;

import com.example.elakil.model.Meal;

import java.util.List;
import java.util.Map;

public interface PlanContract {
    interface View{
        void showWeeklyPlan(List<Meal> meals , List<String> days);



        void navigateToMealDetails(Meal meal);



    }

    interface Presenter{
        void loadWeeklyPlan();
        void onMealClicked(Meal meal);
        void removeMealFromPlan(Meal meal , String day);
    }
}
