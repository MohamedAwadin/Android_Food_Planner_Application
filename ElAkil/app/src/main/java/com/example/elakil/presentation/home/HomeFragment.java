package com.example.elakil.presentation.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.elakil.R;
import com.example.elakil.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {
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

        // Inflate the layout for this fragment
        return view ;
    }
}