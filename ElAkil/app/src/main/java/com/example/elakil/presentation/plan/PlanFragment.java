package com.example.elakil.presentation.plan;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elakil.R;
import com.example.elakil.model.data.FirebaseSyncRepository;
import com.example.elakil.model.data.MealsRepository;
import com.example.elakil.model.data.MealsRepositoryImpl;
import com.example.elakil.model.data.local.MealsLocalDataSource;
import com.example.elakil.model.data.local.MealsLocalDataSourceImpl;
import com.example.elakil.model.data.remote.FirebaseDataSource;
import com.example.elakil.model.data.remote.MealsRemoteDataSource;
import com.example.elakil.model.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;
import com.example.elakil.presentation.mealdetails.DishAllDetailedActivity;
import com.example.elakil.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


public class PlanFragment extends Fragment implements PlanContract.View, PlanMealAdapter.OnPlanMealClickListener{


    private static final String TAG = "PlanFragment" ;
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
        FirebaseDataSource firebaseDataSource = new FirebaseDataSource();
        FirebaseSyncRepository firebaseSyncRepository = FirebaseSyncRepository.getInstance(firebaseDataSource);
        repository = MealsRepositoryImpl.getInstance(remoteDataSource , localDataSource, firebaseSyncRepository);
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
        Log.d(TAG,"Debug: Current weekStartDate = " + weekStartDate);

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
                        Log.d(TAG,"Debug: Processing plan - mealId: " + plan.getMealId() + ", day: " + plan.getDayOfWeek() + ", weekStartDate: " + plan.getWeekStartDate());
                        LiveData<Meal> mealLiveData = repository.getMealByIdLiveData(plan.getMealId());
                        mealLiveDataMap.put(plan.getMealId(), mealLiveData);
                        final String day = plan.getDayOfWeek();
                        mealLiveData.observe(getViewLifecycleOwner() , meal -> {
                            if (meal != null){
                                Log.d(TAG,"Debug: Found meal offline - id: " + meal.getIdMeal() + ", name: " + meal.getStrMeal());
                                plannedMeals.add(meal);
                                days.add(day);

                            }else {
                                Log.d(TAG,"Debug: Meal not found for mealId: " + plan.getMealId());

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
                    Log.d(TAG,"Debug: No weekly plans found for weekStartDate: " + weekStartDate);
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

    @Override
    public void onRemoveFromPlan(Meal meal, String day) {
        presenter.removeMealFromPlan(meal , day);

    }
}