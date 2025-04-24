package com.example.elakil.presentation.auth.view;

import android.app.ComponentCaller;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elakil.R;
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

public class SignUpActivity extends AppCompatActivity implements AuthContract.SignUpView {

    private EditText editTextUsername , editTextEmail , editTextPassword, editTextConfirmPassword ;
    private TextView textViewLogin;
    private Button buttonSignUp  ;

    private ImageButton buttonGoogleSignUp ;
    private ProgressBar progressBar ;

    private AuthContract.Presenter presenter;
    private GoogleSignInClient googleSignInClient ;
    private static final int REQ_SIGN_UP = 9002 ; // Request code for Google Sign-Up

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        textViewLogin = findViewById(R.id.textViewLoginA);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonGoogleSignUp  = findViewById(R.id.imageButton);
        progressBar = findViewById(R.id.progress_bar);

        presenter = new AuthPresenter(this , new SharedPreferencesUtils(this));

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();
                googleSignInClient = GoogleSignIn.getClient(this,gso);


        buttonSignUp.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();
            presenter.signUp(username,email , password , confirmPassword);
        });

        buttonGoogleSignUp.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, REQ_SIGN_UP);
        });

        textViewLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_SIGN_UP){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                presenter.signUpWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                showError("Google Sign-Up Faild: " + e.getMessage());
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void navigateToMain() {
        startActivity(new Intent(SignUpActivity.this , MainActivity.class));
        finish();

    }
}