package com.example.elakil.presentation.mealdetails;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.elakil.R;
import com.example.elakil.model.data.FirebaseSyncRepository;
import com.example.elakil.model.data.MealsRepository;
import com.example.elakil.model.data.MealsRepositoryImpl;
import com.example.elakil.model.data.local.MealsLocalDataSource;
import com.example.elakil.model.data.local.MealsLocalDataSourceImpl;
import com.example.elakil.model.data.remote.FirebaseDataSource;
import com.example.elakil.model.data.remote.MealsRemoteDataSource;
import com.example.elakil.model.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.model.IngredientItem;
import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;
import com.example.elakil.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DishAllDetailedActivity extends AppCompatActivity implements DishAllDetailedContract.View{

    private static final String TAG = "DishAllDetailedActivity";

    private TextView textViewMealName , textViewCountry , textViewSteps ;
    private ImageView imageViewMeal , imageViewFlag ;
    private RecyclerView recyclerViewIngredients ;
    private WebView webViewVideo ;
    private Button buttonFavorite, buttonAddToPlan ;
    private DishAllDetailedContract.Presenter presenter;
    private IngredientAdapter ingredientAdapter;
    private ProgressBar progressBar;
    

    private ExecutorService executorService;
    private Handler mainHandler ;

    private SharedPreferencesUtils sharedPreferencesUtils ;

    private Meal currentMeal ;

    private MealsRepository repository ;



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
        buttonAddToPlan = findViewById(R.id.buttonAddToPlan);
        webViewVideo = findViewById(R.id.webViewVideo);
        progressBar = findViewById(R.id.progressBar);

        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(this);
        FirebaseDataSource firebaseDataSource = new FirebaseDataSource();
        FirebaseSyncRepository firebaseSyncRepository = FirebaseSyncRepository.getInstance(firebaseDataSource);
        repository = MealsRepositoryImpl.getInstance(remoteDataSource , localDataSource, firebaseSyncRepository);

        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
        sharedPreferencesUtils = new SharedPreferencesUtils(this);


        presenter = new DishAllDetailedPresenter(this , repository , executorService , mainHandler);

        ingredientAdapter = new IngredientAdapter(new ArrayList<>());
        recyclerViewIngredients.setAdapter(ingredientAdapter);

        webViewVideo.getSettings().setJavaScriptEnabled(true);
        webViewVideo.setWebChromeClient(new WebChromeClient());

        if (sharedPreferencesUtils.isGuestMode()){
            buttonFavorite.setVisibility(View.GONE);
            buttonAddToPlan.setVisibility(View.GONE);
        } else {
            buttonFavorite.setVisibility(View.VISIBLE);
            buttonFavorite.setOnClickListener(v -> presenter.toggleFavorite());
            buttonAddToPlan.setVisibility(View.VISIBLE);
            buttonAddToPlan.setOnClickListener(v -> showWeekdayDialog());
        }

        String mealId = getIntent().getStringExtra("MEAL_ID");
        if (mealId != null){
            presenter.loadMealDetails(mealId);
        }
    }

    private void showWeekdayDialog(){
        final String[] weekdays = {"Saturday" , "Sunday" , "Monday" , "Tuesday" , "Wednesday" , "Thursday" , "Friday"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Day for Your Week Plan");
        builder.setItems(weekdays, ((dialog, which) -> {
            String selectDay = weekdays[which];
            if (currentMeal != null){
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                calendar.set(Calendar.HOUR_OF_DAY , 0);
                calendar.set(Calendar.MINUTE , 0);
                calendar.set(Calendar.SECOND , 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long weekStartDate = calendar.getTimeInMillis();
                //System.out.println("Debug: Adding plan with weekStartDate = " + weekStartDate); // Debug log
                Log.d(TAG , "Debug: Adding plan with weekStartDate = " + weekStartDate);


                WeeklyPlan weeklyPlan = new WeeklyPlan();
                weeklyPlan.setMealId(currentMeal.getIdMeal());
                weeklyPlan.setDayOfWeek(selectDay);
                weeklyPlan.setWeekStartDate(weekStartDate);

                executorService.execute(() -> {
                    repository.insertMeals(currentMeal, success -> {
                        repository.insertWeeklyPlan(weeklyPlan, planSuccess -> {
                            mainHandler.post(() -> {
                                if (planSuccess) {
                                    Toast.makeText(DishAllDetailedActivity.this, "Added to " + selectDay + "'s plan", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(DishAllDetailedActivity.this, "Failed to add to plan", Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
                    });
                });
            }
        }));
        builder.setNegativeButton("Cancel", ((dialog, which) -> dialog.dismiss()));
        builder.show();
    }

    @Override
    public void showMealDetails(Meal meal) {
        this.currentMeal = meal;
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