package com.example.elakil.presentation.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.elakil.R;
import com.example.elakil.data.FirebaseSyncRepository;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.data.MealsRepositoryImpl;
import com.example.elakil.data.local.MealsLocalDataSource;
import com.example.elakil.data.local.MealsLocalDataSourceImpl;
import com.example.elakil.data.remote.FirebaseDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSource;
import com.example.elakil.data.remote.MealsRemoteDataSourceImpl;
import com.example.elakil.presentation.auth.view.LoginActivity;
import com.example.elakil.utils.SharedPreferencesUtils;


public class ProfileFragment extends Fragment implements ProfileContract.View {

    private TextView textViewUsername, textViewEmail ;
    private Button buttonLogout ;
    private ProfileContract.Presenter presenter;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        buttonLogout = view.findViewById(R.id.buttonLogout);

        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(getContext());
        FirebaseDataSource firebaseDataSource = new FirebaseDataSource();
        FirebaseSyncRepository firebaseSyncRepository = FirebaseSyncRepository.getInstance(firebaseDataSource);
        MealsRepository repository = MealsRepositoryImpl.getInstance(remoteDataSource, localDataSource, firebaseSyncRepository);


        presenter = new ProfilePresenter(this, repository , new SharedPreferencesUtils(getContext()), firebaseSyncRepository);
        presenter.loadProfile();

        buttonLogout.setOnClickListener(v -> presenter.logout());

        return view;
    }

    @Override
    public void showProfile(String username, String email) {
        textViewEmail.setText(email);
        textViewUsername.setText(username);

    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(getActivity() , LoginActivity.class);
        startActivity(intent);
        getActivity().finish();

    }


}