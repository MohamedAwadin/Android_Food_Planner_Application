package com.example.elakil.presentation.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.elakil.R;
import com.example.elakil.data.FirebaseSyncRepository;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.data.MealsRepositoryImpl;
import com.example.elakil.data.local.MealsLocalDataSource;
import com.example.elakil.data.local.MealsLocalDataSourceImpl;
import com.example.elakil.data.remote.FirebaseDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.model.Meal;
import com.example.elakil.presentation.mealdetails.DishAllDetailedActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment implements HomeContract.View {
    private RecyclerView recyclerViewDaily , recyclerViewMoreMeals ;
    private ProgressBar progressBar ;
    private MealAdapter dailyMealAdapter, moreMealAdapter;

    private List<Meal> dailyMeals, moreMeals;
    private HomeContract.Presenter presenter ;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewDaily = view.findViewById(R.id.recyclerViewDaily);
        recyclerViewMoreMeals = view.findViewById(R.id.recyclerViewMoreMeals);
        progressBar = view.findViewById(R.id.progressBar);

        dailyMeals = new ArrayList<>();
        moreMeals = new ArrayList<>();

        dailyMealAdapter = new MealAdapter(dailyMeals , meal -> presenter.onMealClicked(meal), true);
        moreMealAdapter = new MealAdapter( moreMeals, meal -> presenter.onMealClicked(meal), false);

        recyclerViewDaily.setAdapter(dailyMealAdapter);
        recyclerViewMoreMeals.setAdapter(moreMealAdapter);

        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(getContext());
        FirebaseDataSource firebaseDataSource = new FirebaseDataSource();
        FirebaseSyncRepository firebaseSyncRepository = FirebaseSyncRepository.getInstance(firebaseDataSource);
        MealsRepository repository = MealsRepositoryImpl.getInstance(remoteDataSource, localDataSource, firebaseSyncRepository);


        presenter = new HomePresenter(this , repository);
        presenter.loadDailyRecommendation();
        presenter.loadMoreMeals();

        return view ;
    }

    @Override
    public void showDailyRecommendation(List<Meal> meals) {
        dailyMeals.clear();
        dailyMeals.addAll(meals);
        dailyMealAdapter.notifyDataSetChanged();

    }

    @Override
    public void showMoreMeals(List<Meal> meals) {
        moreMeals.clear();
        moreMeals.addAll(meals);
        moreMealAdapter.notifyDataSetChanged();

    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void navigateToMealDetails(Meal meal) {
        Intent intent = new Intent(getActivity() , DishAllDetailedActivity.class);
        intent.putExtra("MEAL_ID", meal.getIdMeal());
        startActivity(intent);

    }
}