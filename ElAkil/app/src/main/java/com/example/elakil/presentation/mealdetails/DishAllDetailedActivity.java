package com.example.elakil.presentation.mealdetails;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elakil.R;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.data.MealsRepositoryImpl;
import com.example.elakil.data.local.MealsLocalDataSource;
import com.example.elakil.data.local.MealsLocalDataSourceImpl;
import com.example.elakil.data.remote.MealsRemoteDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.model.IngredientItem;
import com.example.elakil.model.Meal;
import com.example.elakil.presentation.home.MealAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DishAllDetailedActivity extends AppCompatActivity implements DishAllDetailedContract.View{

    private TextView textViewMealName , textViewCountry , textViewSteps ;
    private ImageView imageViewMeal , imageViewFlag ;
    private RecyclerView recyclerViewIngredients ;
    private WebView webViewVideo ;
    private Button buttonFavorite ;
    private DishAllDetailedContract.Presenter presenter;
    private IngredientAdapter ingredientAdapter;
    private ProgressBar progressBar;
    

    private ExecutorService executorService;
    private Handler mainHandler ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_all_detailed);

        textViewMealName = findViewById(R.id.textViewMealName);
        textViewSteps = findViewById(R.id.textViewSteps);
        textViewCountry = findViewById(R.id.textViewCountry);
        imageViewMeal = findViewById(R.id.imageViewMeal);
        imageViewFlag = findViewById(R.id.imageViewFlag);
        recyclerViewIngredients = findViewById(R.id.recyclerViewIngredients);
        buttonFavorite = findViewById(R.id.buttonFavorite);
        webViewVideo = findViewById(R.id.webViewVideo);
        progressBar = findViewById(R.id.progressBar);

        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(this);
        MealsRepository repository = MealsRepositoryImpl.getInstance(remoteDataSource , localDataSource);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());


        presenter = new DishAllDetailedPresenter(this , repository , executorService , mainHandler);

        ingredientAdapter = new IngredientAdapter(new ArrayList<>());
        recyclerViewIngredients.setAdapter(ingredientAdapter);

        webViewVideo.getSettings().setJavaScriptEnabled(true);
        webViewVideo.setWebChromeClient(new WebChromeClient());

        String mealId = getIntent().getStringExtra("MEAL_ID");
        if (mealId != null){
            presenter.loadMealDetails(mealId);
        }
        buttonFavorite.setOnClickListener(v -> presenter.toggleFavorite());

    }

    @Override
    public void showMealDetails(Meal meal) {
        textViewMealName.setText(meal.getStrMeal());
        Glide.with(this).load(meal.getStrMealThumb()).into(imageViewMeal);

        String country = meal.getStrArea();
        textViewCountry.setText(country);
        int flagResource = getResources().getIdentifier("flag_" + country.toLowerCase(), "drawable", getPackageName());
        if (flagResource != 0){
            imageViewFlag.setImageResource(flagResource);
        } else {
            imageViewFlag.setImageResource(android.R.drawable.ic_menu_help);
        }

        textViewSteps.setText(meal.getStrInstructions() != null ? meal.getStrInstructions() : "No Instruction Available");

        String videoUrl = meal.getStrYoutube();
        if (videoUrl != null && !videoUrl.isEmpty()){
            String videoId = videoUrl.substring(videoUrl.lastIndexOf("=") +1 );
            webViewVideo.loadData("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>", "text/html", "utf-8");
        } else {
            webViewVideo.loadData("<p>No video available</p>", "text/html", "utf-8");
        }


    }

    @Override
    public void showIngredients(List<IngredientItem> ingredients) {
        ingredientAdapter = new IngredientAdapter(ingredients);
        recyclerViewIngredients.setAdapter(ingredientAdapter);
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public void updateFavoriteButton(boolean isFavorite) {

        buttonFavorite.setText(isFavorite ? "Remove from Favorites" : "Add to Favorites");

    }

    @Override
    public void navigateBack() {
        onBackPressed();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()){
            executorService.shutdown();
        }
    }
}