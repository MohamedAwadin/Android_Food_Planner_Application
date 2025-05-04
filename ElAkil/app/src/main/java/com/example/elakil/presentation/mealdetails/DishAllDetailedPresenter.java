package com.example.elakil.presentation.mealdetails;

import android.os.Looper;
import android.os.Handler;
import android.util.Log;

import com.airbnb.lottie.L;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.IngredientItem;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealListResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DishAllDetailedPresenter implements DishAllDetailedContract.Presenter{

    private DishAllDetailedContract.View view;
    private MealsRepository repository ;
    private Meal currentMeal ;

    private static final String TAG = "DishAllDetailedPresenter" ;

    private final ExecutorService executorService;
    private final Handler mainHandler ;

    public DishAllDetailedPresenter(DishAllDetailedContract.View view, MealsRepository repository, ExecutorService executorService, Handler mainHandler) {
        this.view = view;
        this.repository = repository;
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Override
    public void loadMealDetails(String mealId) {
        view.showLoading();
        repository.getMealDetails(mealId, new MealsRepository.NetworkCallback<MealListResponse>() {
            @Override
            public void onSuccess(MealListResponse response) {
                view.hideLoading();
                if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()){
                    currentMeal = response.getMeals().get(0);
                    view.showMealDetails(currentMeal);
                    List<IngredientItem> ingredients = extractIngredients(currentMeal);
                    view.showIngredients(ingredients);
                    checkFavoriteStatus();
                }else {
                    view.showError("Meal details not found");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showError(errorMessage);

            }
        });
    }

    private List<IngredientItem> extractIngredients(Meal meal){
        List<IngredientItem> ingredients = new ArrayList<>();
        for (int i = 1 ; i <= 20 ; i++){
            String ingredientName = getFieldValue(meal , "strIngredient"+i);
            String measurement = getFieldValue(meal , "strMeasure"+i);
            if (ingredientName != null && !ingredientName.isEmpty()){
                String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredientName + ".png";
                ingredients.add(new IngredientItem(ingredientName , imageUrl , measurement));
            } else {
                break;
            }
        }
        return ingredients;
    }

    private String getFieldValue(Meal meal , String fieldName){
        try {
            return (String) Meal.class.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(meal);
        } catch (Exception e) {
            Log.e(TAG, "Error getting field value: " + fieldName, e);
            return null;
        }

    }

    private void checkFavoriteStatus(){
        executorService.execute(() -> {
            Meal favoriteMeal = repository.getMealById(currentMeal.getIdMeal());
            boolean isFavorite  = favoriteMeal != null ;
            mainHandler.post(() -> view.updateFavoriteButton(isFavorite));
        });
    }

    @Override
    public void toggleFavorite() {
        if (currentMeal != null){
            executorService.execute(() -> {
                Meal existingMeal = repository.getMealById(currentMeal.getIdMeal());
                if (existingMeal != null && existingMeal.isFavorite()){
                    currentMeal.setFavorite(false);
                    repository.deleteMeals(currentMeal, new MealsRepository.LocalCallback() {
                        @Override
                        public void onComplete(boolean success) {
                            mainHandler.post(() -> view.updateFavoriteButton(false));
                        }
                    });
                } else {
                    currentMeal.setFavorite(true);
                    repository.insertMeals(currentMeal, new MealsRepository.LocalCallback() {
                        @Override
                        public void onComplete(boolean success) {
                            mainHandler.post(() -> view.updateFavoriteButton(true));
                        }
                    });
                }

            });

        }

    }
}
