package com.example.elakil.presentation.favorites;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.example.elakil.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment {

    private TextView textViewEmpty ;
    private RecyclerView recyclerViewFavorites ;
    private FavoriteMealAdapter favoriteAdapter;
    private List<Meal> favoriteMeals ;
    private FavoritesContract.Presenter presenter ;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_favorites, container, false);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites);

        favoriteMeals = new ArrayList<>();
        favoriteAdapter = new FavoriteMealAdapter(favoriteMeals, new FavoriteMealAdapter.OnFavoriteMealClickListener() {
            @Override
            public void OnMealClick(Meal meal) {
                presenter.onMealClicked(meal);
            }

            @Override
            public void OnRemoveMeal(Meal meal) {
                presenter.removeFromFavorites(meal);

            }
        });
        recyclerViewFavorites.setAdapter(favoriteAdapter);

        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(getContext());
        MealsRepository repository = MealsRepositoryImpl.getInstance(remoteDataSource , localDataSource);

        presenter = new FavoritesPresenter(this , repository , new SharedPreferencesUtils(getContext()));
        presenter.loadFavoriteMeals();
        return  view;
    }
}