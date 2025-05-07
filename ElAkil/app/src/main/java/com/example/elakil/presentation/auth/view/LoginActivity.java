package com.example.elakil.presentation.auth.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.elakil.presentation.auth.presenter.AuthContract;
import com.example.elakil.presentation.auth.presenter.AuthPresenter;
import com.example.elakil.presentation.main.view.MainActivity;
import com.example.elakil.utils.SharedPreferencesUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements AuthContract.LoginView {
    private static final String TAG = "LoginActivity";
    private static final int REQCODE_SIGN_IN = 9001;

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonSignUp, buttonGuest;
    private ImageButton buttonGoogleSignIn;
    private ProgressBar progressBar;
    private AuthContract.Presenter presenter;
    private GoogleSignInClient googleSignInClient;
    private MealsRepository mealsRepository;
    private FirebaseSyncRepository firebaseSyncRepository;
    //private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonGuest = findViewById(R.id.buttonGuest);
        buttonGoogleSignIn = findViewById(R.id.buttonGoogleSignIn);
        progressBar = findViewById(R.id.progressBar);

        presenter = new AuthPresenter(this, new SharedPreferencesUtils(this));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize repositories
        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(this);
        FirebaseDataSource firebaseDataSource = new FirebaseDataSource();
        firebaseSyncRepository = FirebaseSyncRepository.getInstance(firebaseDataSource);
        mealsRepository = MealsRepositoryImpl.getInstance(remoteDataSource, localDataSource, firebaseSyncRepository);

        //mainHandler = new Handler(Looper.getMainLooper());

        buttonLogin.setOnClickListener(v -> {
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            presenter.loginWithEmail(email, password);
        });

        buttonSignUp.setOnClickListener(v -> presenter.navigateToSignUp());

        buttonGuest.setOnClickListener(v -> presenter.loginAsGuest());

        buttonGoogleSignIn.setOnClickListener(v -> {
            Intent signIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signIntent, REQCODE_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQCODE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.loginWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                showError("Google Sign-In Failed: The User Not Found");
            }
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoding() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateToMain() {

        syncDataFromFirebase();
    }

    @Override
    public void navigateToSignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    private void syncDataFromFirebase() {
        showLoading();
        firebaseSyncRepository.retrieveFavorites(new FirebaseDataSource.FavoriteCallback() {
            @Override
            public void onSuccess(List<Meal> meals) {
                for (Meal meal : meals){
                    mealsRepository.insertMeals(meal, success -> {
                        if (success) {
                            Log.d(TAG ,"Debug: Synced favorite meal " + meal.getIdMeal() + " to local database" );
                        }
                    });
                }
                firebaseSyncRepository.retrieveWeeklyPlans(new FirebaseDataSource.WeeklyPlansCallback() {
                    @Override
                    public void onSuccess(List<WeeklyPlan> plans) {
                        for (WeeklyPlan plan : plans){
                            mealsRepository.insertWeeklyPlan(plan , success -> {
                                Log.d(TAG , "Debug: Synced weekly plan for meal " + plan.getMealId() + " to local database");
                            });
                        }
                        hideLoding();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        hideLoding();
                        showError("Failed to sync weekly plans: " + errorMessage);
                        startActivity(new Intent(LoginActivity.this , MainActivity.class));
                        finish();


                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {

                hideLoding();
                showError("Failed to sync favorites: " + errorMessage);
                // Proceed to MainActivity even if sync fails
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();

            }
        });
    }

//    private void retrieveWeeklyPlans() {
//        System.out.println("Debug: Retrieving weekly plans from Firebase");
//        firebaseSyncRepository.retrieveWeeklyPlans(new FirebaseDataSource.WeeklyPlansCallback() {
//            @Override
//            public void onSuccess(List<WeeklyPlan> plans) {
//                System.out.println("Debug: Retrieved " + plans.size() + " weekly plans from Firebase");
//                if (plans.isEmpty()) {
//                    // If no plans, complete the sync process
//                    completeSync();
//                    return;
//                }
//
//                // Batch insert weekly plans to local database
//                AtomicInteger pendingInserts = new AtomicInteger(plans.size());
//                for (WeeklyPlan plan : plans) {
//                    mealsRepository.insertWeeklyPlan(plan, success -> {
//                        if (success) {
//                            System.out.println("Debug: Synced weekly plan for meal " + plan.getMealId() + " to local database");
//                        } else {
//                            System.out.println("Debug: Failed to sync weekly plan for meal " + plan.getMealId());
//                        }
//                        if (pendingInserts.decrementAndGet() == 0) {
//                            // All plans inserted, complete the sync
//                            completeSync();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//                System.out.println("Debug: Failed to retrieve weekly plans from Firebase: " + errorMessage);
//                // Complete sync even if retrieval fails
//                completeSync();
//            }
//        });
//    }
//
//    private void completeSync() {
//        System.out.println("Debug: Firebase sync completed");
//        mainHandler.post(() -> {
//            hideLoding();
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        });
//    }
}