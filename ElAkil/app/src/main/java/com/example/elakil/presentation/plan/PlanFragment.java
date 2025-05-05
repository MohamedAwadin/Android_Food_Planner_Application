package com.example.elakil.presentation.plan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elakil.R;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.data.MealsRepositoryImpl;
import com.example.elakil.data.local.MealsLocalDataSource;
import com.example.elakil.data.local.MealsLocalDataSourceImpl;
import com.example.elakil.data.remote.MealsRemoteDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;
import com.example.elakil.presentation.auth.view.SignUpActivity;
import com.example.elakil.presentation.main.view.MainActivity;
import com.example.elakil.presentation.mealdetails.DishAllDetailedActivity;
import com.example.elakil.presentation.profile.ProfileFragment;
import com.example.elakil.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class PlanFragment extends Fragment implements PlanContract.View, PlanMealAdapter.OnPlanMealClickListener{

    private TextView textViewEmpty ;
    private RecyclerView recyclerViewPlan ;
    private PlanMealAdapter planAdapter ;

    private List<Meal> plannedMeals;
    private List<String> days ;
    private PlanContract.Presenter presenter;

    private MealsRepository repository ;
    private SharedPreferencesUtils sharedPreferencesUtils;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        recyclerViewPlan = view.findViewById(R.id.recyclerViewPlan);

        plannedMeals = new ArrayList<>();
        days = new ArrayList<>();

        planAdapter = new PlanMealAdapter(plannedMeals , days , this);
        recyclerViewPlan.setAdapter(planAdapter);


        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(getContext());
        repository = MealsRepositoryImpl.getInstance(remoteDataSource , localDataSource);
        sharedPreferencesUtils = new SharedPreferencesUtils(getContext());

        presenter = new PlanPresenter(this , repository , new SharedPreferencesUtils(getContext()));
        //presenter.loadWeeklyPlan();

        observeWeeklyPlan();

        return view;
    }

    private void observeWeeklyPlan(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        calendar.set(Calendar.HOUR_OF_DAY , 0);
        calendar.set(Calendar.MINUTE , 0);
        calendar.set(Calendar.SECOND , 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long weekStartDate = calendar.getTimeInMillis();
        System.out.println("Debug: Current weekStartDate = " + weekStartDate); // Debug log

        repository.getWeeklyPlans(weekStartDate).observe(getViewLifecycleOwner(), new Observer<List<WeeklyPlan>>() {
            private Map<String , LiveData<Meal>> mealLiveDataMap = new HashMap<>();
            private int pendingMeals = 0;
            @Override
            public void onChanged(List<WeeklyPlan> weeklyPlans) {
                plannedMeals.clear();
                days.clear();
                if (weeklyPlans != null &&!weeklyPlans.isEmpty()){
                    pendingMeals = weeklyPlans.size();
                    for (WeeklyPlan plan : weeklyPlans){
                        System.out.println("Debug: Processing plan - mealId: " + plan.getMealId() + ", day: " + plan.getDayOfWeek() + ", weekStartDate: " + plan.getWeekStartDate()); // Debug log
                        LiveData<Meal> mealLiveData = repository.getMealByIdLiveData(plan.getMealId());
                        mealLiveDataMap.put(plan.getMealId(), mealLiveData);
                        final String day = plan.getDayOfWeek();
                        mealLiveData.observe(getViewLifecycleOwner() , meal -> {
                            if (meal != null){
                                System.out.println("Debug: Found meal - id: " + meal.getIdMeal() + ", name: " + meal.getStrMeal()); // Debug log
                                plannedMeals.add(meal);
                                days.add(plan.getDayOfWeek());

                            }else {
                                System.out.println("Debug: Meal not found for mealId: " + plan.getMealId()); // Debug log
                            }
                            pendingMeals--;
                            if (pendingMeals == 0) {
                                planAdapter.notifyDataSetChanged();
                                textViewEmpty.setVisibility(plannedMeals.isEmpty() ? View.VISIBLE : View.GONE);
                                recyclerViewPlan.setVisibility(plannedMeals.isEmpty() ? View.GONE : View.VISIBLE);
                            }

                        });

                    }
                } else {
                    textViewEmpty.setVisibility(View.VISIBLE);
                    recyclerViewPlan.setVisibility(View.GONE);
                    System.out.println("Debug: No weekly plans found for weekStartDate: " + weekStartDate); // Debug log
                }
            }
        });
    }



    @Override
    public void showWeeklyPlan(List<Meal> meals, List<String> days) {
        plannedMeals.clear();
        plannedMeals.addAll(meals);
        this.days.clear();
        this.days.addAll(days);
        planAdapter.notifyDataSetChanged();
        textViewEmpty.setVisibility(plannedMeals.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerViewPlan.setVisibility(plannedMeals.isEmpty() ? View.GONE : View.VISIBLE);

    }



    @Override
    public void navigateToMealDetails(Meal meal) {
        Intent intent = new Intent(getActivity(), DishAllDetailedActivity.class);
        intent.putExtra("MEAL_ID", meal.getIdMeal());
        startActivity(intent);
    }

    @Override
    public void OnMealClick(Meal meal) {
        presenter.onMealClicked(meal);

    }
}