package com.example.elakil.presentation.plan;

import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;
import com.example.elakil.utils.SharedPreferencesUtils;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class PlanPresenter implements PlanContract.Presenter{
    private PlanContract.View view;
    private MealsRepository repository ;
    private static final List<String> DAYS_OF_WEEK = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

    private SharedPreferencesUtils sharedPreferencesUtils;

    public PlanPresenter(PlanContract.View view, MealsRepository repository, SharedPreferencesUtils sharedPreferencesUtils) {
        this.view = view;
        this.repository = repository;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
    }

    @Override
    public void loadWeeklyPlan() {
        if (sharedPreferencesUtils.isGuestMode()){
            view.showGuestMessage();
        }
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long weekStartDate = calendar.getTimeInMillis();

            repository.getWeeklyPlans(weekStartDate).observeForever(weeklyPlans -> {
                if (weeklyPlans != null && !weeklyPlans.isEmpty()) {
                    List<Meal> plannedMeals = new ArrayList<>();
                    List<String> days = new ArrayList<>();

                    for (WeeklyPlan plan : weeklyPlans) {
                        Meal meal = repository.getMealById(plan.getMealId());
                        if (meal != null) {
                            plannedMeals.add(meal);
                            days.add(plan.getDayOfWeek());
                        }
                    }
                    view.showWeeklyPlan(plannedMeals, days);
                } else {
                    view.showWeeklyPlan(new ArrayList<>(), new ArrayList<>());
                }
            });
        }

    }

    @Override
    public void onMealClicked(Meal meal) {
        view.navigateToMealDetails(meal);

    }
}
