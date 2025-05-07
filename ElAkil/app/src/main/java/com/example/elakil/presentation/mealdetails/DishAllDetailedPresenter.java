package com.example.elakil.presentation.mealdetails;

import android.os.Looper;
import android.os.Handler;
import android.util.Log;

import com.example.elakil.model.data.MealsRepository;
import com.example.elakil.model.IngredientItem;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealListResponse;

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

        executorService.execute(() -> {
            Meal localMeal = repository.getMealById(mealId);
            if (localMeal != null){
                currentMeal = localMeal;
                mainHandler.post(() -> {
                   view.showMealDetails(currentMeal);
                   view.showIngredients(extractIngredients(currentMeal));
                   view.updateFavoriteButton(currentMeal.isFavorite());
                   view.hideLoading();
                });
            } else {
                repository.getMealDetails(mealId, new MealsRepository.NetworkCallback<MealListResponse>() {
                    @Override
                    public void onSuccess(MealListResponse response) {
                        if (response != null && response.getMeals() != null && !response.getMeals().isEmpty()){
                            currentMeal = response.getMeals().get(0);
                            executorService.execute(() -> {
                                repository.insertMeals(currentMeal , success -> {
                                    if (!success){
                                        mainHandler.post(() -> view.showError("Failed to save meal locally"));
                                    }
                                });
                            });
                            mainHandler.post(() -> {
                                view.showMealDetails(currentMeal);
                                view.showIngredients(extractIngredients(currentMeal));
                                view.updateFavoriteButton(currentMeal.isFavorite());
                                view.hideLoading();
                            });
                        } else {
                            mainHandler.post(() -> {
                                view.showError("Meal details not found ");
                                view.hideLoading();
                            });
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        mainHandler.post(() -> {
                            view.showError(errorMessage);
                            view.hideLoading();
                        });
                    }
                });
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
        if (currentMeal == null) return;

        boolean isCurrentlyFavorite = currentMeal.isFavorite();
        currentMeal.setFavorite(!isCurrentlyFavorite);

        executorService.execute(() -> {
            if (currentMeal.isFavorite()) {

                repository.insertMeals(currentMeal, success -> {
                    mainHandler.post(() -> {
                        if (success) {
//                            view.updateFavoriteButton(true);
                            view.updateFavoriteButton(currentMeal.isFavorite());
                            System.out.println("Debug: Toggled favorite status for meal " + currentMeal.getIdMeal() + " to " + currentMeal.isFavorite());
                        } else {
//                            currentMeal.setFavorite(false);
//                            view.updateFavoriteButton(false);
                            currentMeal.setFavorite(isCurrentlyFavorite);
                            view.updateFavoriteButton(isCurrentlyFavorite);
                            view.showError("Failed to add to favorites");
                        }
                    });
                });
            } else {

                repository.deleteMeals(currentMeal, success -> {
                    mainHandler.post(() -> {
                        if (success) {
                            view.updateFavoriteButton(false);
                        } else {
                            currentMeal.setFavorite(true);
                            view.updateFavoriteButton(true);
                            view.showError("Failed to remove from favorites");
                        }
                    });
                });
            }
        });
    }
}
