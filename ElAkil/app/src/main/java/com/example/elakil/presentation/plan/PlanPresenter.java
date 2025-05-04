package com.example.elakil.presentation.plan;

import android.os.Looper;
import android.os.Handler;

import com.airbnb.lottie.L;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;
import com.example.elakil.utils.SharedPreferencesUtils;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class PlanPresenter implements PlanContract.Presenter{
    private PlanContract.View view;
    private MealsRepository repository ;
    private static final List<String> DAYS_OF_WEEK = Arrays.asList("Saturday" , "Sunday" , "Monday" , "Tuesday" , "Wednesday" , "Thursday" , "Friday");

    private SharedPreferencesUtils sharedPreferencesUtils;

    private ExecutorService executorService ;
    private Handler mainHandler ;


    public PlanPresenter(PlanContract.View view, MealsRepository repository, SharedPreferencesUtils sharedPreferencesUtils) {
        this.view = view;
        this.repository = repository;
        this.sharedPreferencesUtils = sharedPreferencesUtils;
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void loadWeeklyPlan() {

        executorService.execute(() -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long weekStartDate = calendar.getTimeInMillis();

            List<WeeklyPlan> weeklyPlans = repository.getWeeklyPlans(weekStartDate).getValue();
            List<Meal> plannedMeals = new ArrayList<>();
            List<String> days = new ArrayList<>();

            if (weeklyPlans != null && !weeklyPlans.isEmpty()){
                for (WeeklyPlan plan : weeklyPlans){
                    Meal meal = repository.getMealById(plan.getMealId());
                    if (meal != null){
                        plannedMeals.add(meal);
                        days.add(plan.getDayOfWeek());
                    }
                }
            }
            mainHandler.post(() -> view.showWeeklyPlan(plannedMeals , days));
        });

    }

    @Override
    public void onMealClicked(Meal meal) {
        //view.navigateToMealDetails(meal);
        mainHandler.post(() -> view.navigateToMealDetails(meal));

    }
}
