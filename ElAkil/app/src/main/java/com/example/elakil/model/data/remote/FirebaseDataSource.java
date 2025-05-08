package com.example.elakil.model.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FirebaseDataSource {
    private static final String TAG = "FirebaseDataSource" ;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public FirebaseDataSource(){
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    private String getUserId(){
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null){
            Log.d(TAG,"Debug: No authenticated user found");
            return null;
        }
        String userId = user.getUid();
        Log.d(TAG,"Debug: Authenticated user ID: " + userId);
        return userId;
    }

    public void backupFavoriteMeal(Meal meal , Callback callback){
        String userId = getUserId();
        if (userId == null){
            Log.d(TAG,"Debug: backupFavoriteMeal failed - User not logged in");
            callback.onFailure("user not logged in");
            return;
        }
        databaseReference.child("users").child(userId).child("favorites").child(meal.getIdMeal())
                .setValue(meal , (error, ref) -> {
                    if (error == null){
                        Log.d(TAG,"Debug: Successfully backed up favorite meal " + meal.getIdMeal());
                        callback.onSuccess();
                    } else {
                        Log.d(TAG,"Debug: Failed to back up favorite meal " + meal.getIdMeal() + ": " + error.getMessage());
                        callback.onFailure(error.getMessage());
                    }
                });
    }

    public void backupWeeklyPlan(WeeklyPlan plan , Callback callback){
        String userId = getUserId();
        if (userId == null){
            Log.d(TAG,"Debug: backupWeeklyPlan failed - User not logged in");
            callback.onFailure("User logged in");
            return;
        }
        String planId = String.valueOf(plan.hashCode());
        databaseReference.child("users").child(userId).child("weekly_plans").child(planId)
                .setValue(plan, (error, ref) -> {
                    if (error == null){
                        Log.d(TAG,"Debug: Successfully backed up weekly plan for meal " + plan.getMealId());

                        callback.onSuccess();
                    } else {
                        Log.d(TAG,"Debug: Failed to back up weekly plan for meal " + plan.getMealId() + ": " + error.getMessage());
                        callback.onFailure(error.getMessage());
                    }
                });
    }

    public void retrieveFavoriteMeals(FavoriteCallback callback){
        String userId = getUserId();
        if (userId == null){
            Log.d(TAG,"Debug: retrieveFavoriteMeals failed - User not logged in");
            callback.onFailure("User not logged in ");
            return;
        }

        databaseReference.child("users").child(userId).child("favorites")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Meal> meals = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Meal meal = snapshot.getValue(Meal.class);
                            if (meal != null){
                                meals.add(meal);
                            }
                        }
                        callback.onSuccess(meals);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onFailure(error.getMessage());
                    }
                });

    }

    public void retrieveWeeklyPlans(WeeklyPlansCallback callback){
        String userId = getUserId();
        if (userId == null){
            callback.onFailure("User not logged in ");
            return;
        }
        databaseReference.child("users").child(userId).child("weekly_plans")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<WeeklyPlan> plans = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            WeeklyPlan plan = snapshot.getValue(WeeklyPlan.class);
                            if (plan != null){
                                plans.add(plan);
                            }
                        }
                        callback.onSuccess(plans);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onFailure(error.getMessage());
                    }
                });


    }

    public void clearUserData(Callback callback){
        String userId = getUserId();
        if (userId == null) {
            Log.d(TAG,"Debug: clearUserData failed - User not logged in");
            callback.onFailure("User not logged in");
            return;
        }
        databaseReference.child("users").child(userId).removeValue((error, ref) -> {
            if (error == null) {
                Log.d(TAG,"Debug: Successfully cleared Firebase data for user " + userId);
                callback.onSuccess();
            } else {
                Log.d(TAG,"Debug: Failed to clear Firebase data for user " + userId + ": " + error.getMessage());
                callback.onFailure(error.getMessage());
            }
        });
    }

    public interface Callback{
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public interface FavoriteCallback{
        void onSuccess(List<Meal> meals);
        void onFailure(String errorMessage);
    }

    public interface WeeklyPlansCallback{
        void onSuccess(List<WeeklyPlan> plans);
        void onFailure(String errorMessage);
    }


}
