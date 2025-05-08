package com.example.elakil.presentation.favorites;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.elakil.presentation.mealdetails.DishAllDetailedActivity;
import com.example.elakil.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment implements FavoritesContract.View , FavoriteMealAdapter.OnFavoriteMealClickListener{

    private static final String TAG = "FavoritesFragment" ;
    private TextView textViewEmpty ;
    private RecyclerView recyclerViewFavorites ;
    private FavoriteMealAdapter favoriteAdapter;
    private List<Meal> favoriteMeals ;
    private FavoritesContract.Presenter presenter ;
    MealsRepository repository;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_favorites, container, false);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites);

        favoriteMeals = new ArrayList<>();
        favoriteAdapter = new FavoriteMealAdapter(favoriteMeals, this) ;
        recyclerViewFavorites.setAdapter(favoriteAdapter);

        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(getContext());
        FirebaseDataSource firebaseDataSource = new FirebaseDataSource();
        FirebaseSyncRepository firebaseSyncRepository = FirebaseSyncRepository.getInstance(firebaseDataSource);
         repository = MealsRepositoryImpl.getInstance(remoteDataSource , localDataSource, firebaseSyncRepository);

        presenter = new FavoritesPresenter(this , repository , new SharedPreferencesUtils(getContext()));


        observeFavoriteMeals();


        return  view;
    }

    private void observeFavoriteMeals(){
        repository.getFavoriteMeals().observe(getViewLifecycleOwner(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> meals) {
                favoriteMeals.clear();
                if (meals != null && !meals.isEmpty()){
                    favoriteMeals.addAll(meals);
                    Log.d(TAG ,"Debug: Loaded " + meals.size() + " favorite meals offline" );

                } else {
                    Log.d(TAG,"Debug: No favorite meals found");
                }
                favoriteAdapter.notifyDataSetChanged();
                textViewEmpty.setVisibility(favoriteMeals.isEmpty() ? View.VISIBLE : View.GONE);
                recyclerViewFavorites.setVisibility(favoriteMeals.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
    }



    @Override
    public void showFavoritesMeals(List<Meal> meals) {
        favoriteMeals.clear();


        favoriteMeals.addAll(meals);
        favoriteAdapter.notifyDataSetChanged();
        textViewEmpty.setVisibility(favoriteMeals.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerViewFavorites.setVisibility(favoriteMeals.isEmpty() ? View.GONE : View.VISIBLE);

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
    public void OnRemoveMeal(Meal meal) {
        presenter.removeFromFavorites(meal);

    }
}