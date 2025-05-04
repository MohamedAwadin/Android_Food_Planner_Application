package com.example.elakil.presentation.plan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.elakil.presentation.auth.view.SignUpActivity;
import com.example.elakil.presentation.main.view.MainActivity;
import com.example.elakil.presentation.mealdetails.DishAllDetailedActivity;
import com.example.elakil.presentation.profile.ProfileFragment;
import com.example.elakil.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;


public class PlanFragment extends Fragment implements PlanContract.View{

    private TextView textViewEmpty ;
    private RecyclerView recyclerViewPlan ;
    private PlanMealAdapter planAdapter ;

    private List<Meal> plannedMeals;
    private List<String> days ;
    private PlanContract.Presenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_plan, container, false);

        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        recyclerViewPlan = view.findViewById(R.id.recyclerViewPlan);

        plannedMeals = new ArrayList<>();
        days = new ArrayList<>();

        planAdapter = new PlanMealAdapter(plannedMeals , days , meal -> presenter.onMealClicked(meal));
        recyclerViewPlan.setAdapter(planAdapter);


        MealsRemoteDataSource remoteDataSource = MealsRemoteDataSourceImpl.getInstance();
        MealsLocalDataSource localDataSource = MealsLocalDataSourceImpl.getInstance(getContext());
        MealsRepository repository = MealsRepositoryImpl.getInstance(remoteDataSource , localDataSource);

        presenter = new PlanPresenter(this , repository , new SharedPreferencesUtils(getContext()));
        presenter.loadWeeklyPlan();

        return view;
    }



//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }



    @Override
    public void showWeeklyPlan(List<Meal> meals, List<String> days) {
        plannedMeals.clear();
        plannedMeals.addAll(meals);
        this.days.clear();
        this.days.addAll(days);
        planAdapter.notifyDataSetChanged();
        textViewEmpty.setVisibility(plannedMeals.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerViewPlan.setVisibility(plannedMeals.isEmpty() ? View.GONE : View.VISIBLE);

    }

//    @Override
//    public void showGuestMessage() {
//        textViewEmpty.setText("Guest Mode: Plan feature not available");
//        textViewEmpty.setVisibility(View.VISIBLE);
//        recyclerViewPlan.setVisibility(View.GONE);
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setMessage("Plan feature not available, Do you want to sign up ?");
//        builder.setTitle("Guest Mode !");
//        builder.setCancelable(false);
//        builder.setPositiveButton("OK" , (DialogInterface.OnClickListener) (dialog, which) ->{
//
//            Intent intent = new Intent(getActivity(), SignUpActivity.class);
//            startActivity(intent);
//            if (getActivity() != null){
//                getActivity().finish();
//            }
//
//
//        });
//        builder.setNegativeButton("No" , (DialogInterface.OnClickListener) (dialog , which)->{
//            dialog.cancel();
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//    }

    @Override
    public void navigateToMealDetails(Meal meal) {
        Intent intent = new Intent(getActivity(), DishAllDetailedActivity.class);
        intent.putExtra("MEAL_ID", meal.getIdMeal());
        startActivity(intent);

    }
}