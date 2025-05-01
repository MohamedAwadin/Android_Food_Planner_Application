package com.example.elakil.presentation.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.elakil.R;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.data.MealsRepositoryImpl;
import com.example.elakil.data.local.MealsLocalDataSource;
import com.example.elakil.data.local.MealsLocalDataSourceImpl;
import com.example.elakil.data.remote.MealsRemoteDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.model.FilterItem;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements SearchContract.View{

    private EditText editTextSearch;
    private Button buttonCountry, buttonCategory, buttonIngredient;
    private RecyclerView recyclerViewFilters ;
    private ProgressBar progressBar ;
    private SearchContract.Presenter presenter ;
    private FilterAdapter filterAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonCountry = view.findViewById(R.id.buttonCountry);
        buttonCategory = view.findViewById(R.id.buttonCategory);
        buttonIngredient = view.findViewById(R.id.buttonIngredient);
        recyclerViewFilters = view.findViewById(R.id.recyclerViewFilters);
        progressBar = view.findViewById(R.id.progressBar);


        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource   = MealsLocalDataSourceImpl.getInstance(getContext());
        MealsRepository repository = MealsRepositoryImpl.getInstance(remoteDataSource,localDataSource);


        presenter = new SearchPresenter(this , repository);

        filterAdapter = new FilterAdapter(new ArrayList<>(), filterItem -> presenter.onFilterItemClicked(filterItem));
        recyclerViewFilters.setAdapter(filterAdapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onSearchTextChanged(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        buttonCountry.setOnClickListener(v -> presenter.onFilterButtonClicked("country"));
        buttonCategory.setOnClickListener(v -> presenter.onFilterButtonClicked("category"));
        buttonIngredient.setOnClickListener(v -> presenter.onFilterButtonClicked("ingredient"));

        presenter.loadFilters();

        return view;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewFilters.setVisibility(View.GONE);

    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        recyclerViewFilters.setVisibility(View.VISIBLE);

    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void showAreas(List<FilterItem> areas) {
        filterAdapter = new FilterAdapter(areas , filterItem -> presenter.onFilterItemClicked(filterItem));
        recyclerViewFilters.setAdapter(filterAdapter);

    }

    @Override
    public void showCategories(List<FilterItem> categories) {
        filterAdapter = new FilterAdapter(categories, filterItem -> presenter.onFilterItemClicked(filterItem));
        recyclerViewFilters.setAdapter(filterAdapter);

    }

    @Override
    public void showIngredients(List<FilterItem> ingredients) {
        filterAdapter = new FilterAdapter(ingredients , filterItem -> presenter.onFilterItemClicked(filterItem));
        recyclerViewFilters.setAdapter(filterAdapter);

    }

    @Override
    public void setSelectedFilter(String filterType) {
        buttonCountry.setSelected(filterType.equals("country"));
        buttonCategory.setSelected(filterType.equals("category"));
        buttonIngredient.setSelected(filterType.equals("ingredient"));

    }



    @Override
    public void navigateToFilteredDishSearch(FilterItem filterItem) {

        Intent intent = new Intent(getActivity(), FilteredDishSearchActivity.class);
        intent.putExtra("FILTER_TYPE", filterItem.getType());
        intent.putExtra("FILTER_VALUE", filterItem.getName());
        startActivity(intent);

    }
}