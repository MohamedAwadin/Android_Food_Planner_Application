package com.example.elakil.model.data;

import com.example.elakil.model.data.remote.FirebaseDataSource;
import com.example.elakil.model.Meal;
import com.example.elakil.model.WeeklyPlan;

public class FirebaseSyncRepository {
    private static FirebaseSyncRepository instance = null ;
    private FirebaseDataSource firebaseDataSource ;

    public FirebaseSyncRepository(FirebaseDataSource firebaseDataSource) {
        this.firebaseDataSource = firebaseDataSource;
    }

    public static FirebaseSyncRepository getInstance(FirebaseDataSource firebaseDataSource){
        if (instance == null){
            instance = new FirebaseSyncRepository(firebaseDataSource);
        }
        return instance;
    }

    public void syncFavoriteMeal(Meal meal , FirebaseDataSource.Callback callback){
        firebaseDataSource.backupFavoriteMeal(meal , callback);
    }
    public void syncWeeklyPlan(WeeklyPlan plan , FirebaseDataSource.Callback callback){
        firebaseDataSource.backupWeeklyPlan(plan , callback);
    }
    public void retrieveFavorites(FirebaseDataSource.FavoriteCallback callback){
        firebaseDataSource.retrieveFavoriteMeals(callback);
    }
    public void retrieveWeeklyPlans(FirebaseDataSource.WeeklyPlansCallback callback){
        firebaseDataSource.retrieveWeeklyPlans(callback);
    }

    public void clearFirebaseData(FirebaseDataSource.Callback callback) {
        firebaseDataSource.clearUserData(callback);
    }



}
