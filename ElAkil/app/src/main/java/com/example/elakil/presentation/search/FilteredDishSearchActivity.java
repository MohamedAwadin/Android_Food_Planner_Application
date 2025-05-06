package com.example.elakil.presentation.search;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
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
import com.example.elakil.presentation.mealdetails.DishAdapter;
import com.example.elakil.presentation.mealdetails.DishAllDetailedActivity;
import java.util.ArrayList;
import java.util.List;

public class FilteredDishSearchActivity extends AppCompatActivity implements FilteredDishSearchContract.View {
    private EditText editTextSearchDishes;
    private RecyclerView recyclerViewDishes;
    private ProgressBar progressBar;
    private FilteredDishSearchContract.Presenter presenter;
    private DishAdapter dishAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_dish_search);

        editTextSearchDishes = findViewById(R.id.editTextSearchDishes);
        recyclerViewDishes = findViewById(R.id.recyclerViewDishes);
        progressBar = findViewById(R.id.progressBar);

        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(this);
        FirebaseDataSource firebaseDataSource = new FirebaseDataSource();
        FirebaseSyncRepository firebaseSyncRepository = FirebaseSyncRepository.getInstance(firebaseDataSource);
        MealsRepository repository = MealsRepositoryImpl.getInstance(remoteDataSource, localDataSource, firebaseSyncRepository);

        String filterType = getIntent().getStringExtra("FILTER_TYPE");
        String filterValue = getIntent().getStringExtra("FILTER_VALUE");

        presenter = new FilteredDishSearchPresenter(this, repository, filterType, filterValue);

        dishAdapter = new DishAdapter(new ArrayList<>(), meal -> presenter.onDishClicked(meal));
        recyclerViewDishes.setAdapter(dishAdapter);

        editTextSearchDishes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onSearchTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        presenter.loadDishes(filterType, filterValue);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewDishes.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        recyclerViewDishes.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showDishes(List<Meal> dishes) {
        dishAdapter = new DishAdapter(dishes, meal -> presenter.onDishClicked(meal));
        recyclerViewDishes.setAdapter(dishAdapter);
    }

    @Override
    public void navigateToDishDetails(String mealId) {
        Intent intent = new Intent(this, DishAllDetailedActivity.class);
        intent.putExtra("MEAL_ID", mealId);
        startActivity(intent);
    }
}