package com.example.elakil.presentation.search;

import com.airbnb.lottie.L;
import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.Meal;
import com.example.elakil.model.MealResponse;

import java.util.ArrayList;
import java.util.List;

public class FilteredDishSearchPresenter implements FilteredDishSearchContract.Presenter{
    private  FilteredDishSearchContract.View view ;
    private MealsRepository repository ;
    private List<Meal> allDishes = new ArrayList<>();
    private String filterType ;
    private String filterValue ;

    public FilteredDishSearchPresenter(FilteredDishSearchContract.View view, MealsRepository repository, String filterType, String filterValue) {
        this.view = view;
        this.repository = repository;
        this.filterType = filterType;
        this.filterValue = filterValue;
    }

    @Override
    public void loadDishes(String filterType, String filterValue) {
        view.showLoading();
        MealsRepository.NetworkCallback callback = new MealsRepository.NetworkCallback() {
            @Override
            public void onSuccess(MealResponse response) {
                view.hideLoading();
                if (response != null && response.getMeals() != null){
                    allDishes.clear();
                    allDishes.addAll(response.getMeals());
                    view.showDishes(allDishes);
                }else {
                    view.showError("No dishes found");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                view.hideLoading();
                view.showError(errorMessage);

            }
        };

        switch (filterType){
            case "area":
                repository.filterByArea(filterValue , callback);
                break;
            case "category":
                repository.filterByCategory(filterValue , callback);
                break;
            case "ingredient":
                repository.filterByIngredient(filterValue , callback);
                break;
            default:
                view.showError("Invalid filter type");
                break;
        }

    }

    @Override
    public void onSearchTextChanged(String query) {
        List<Meal> filterDishes = new ArrayList<>();
        for (Meal dish : allDishes){
            if (dish.getStrMeal().toLowerCase().contains(query.toLowerCase())){
                filterDishes.add(dish);
            }
        }
        view.showDishes(filterDishes);
    }

    @Override
    public void onDishClicked(Meal meal) {
        view.navigateToDishDetails(meal.getIdMeal());

    }
}
