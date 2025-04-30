package com.example.elakil.presentation.search;

import com.example.elakil.data.MealsRepository;
import com.example.elakil.model.Category;
import com.example.elakil.model.CategoryListResponse;
import com.example.elakil.model.Country;
import com.example.elakil.model.CountryListResponse;
import com.example.elakil.model.FilterItem;
import com.example.elakil.model.Ingredient;
import com.example.elakil.model.IngredientListResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter implements SearchContract.Presenter{
    private SearchContract.View view;
    private MealsRepository repository;
    private List<FilterItem> allAreas = new ArrayList<>();
    private List<FilterItem> allCategories = new ArrayList<>();
    private List<FilterItem> allIngredients = new ArrayList<>();

    private String currentFilterType = "area" ;

    public SearchPresenter(SearchContract.View view, MealsRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void loadFilters() {
        view.showLoading();

        repository.getCountries(new MealsRepository.NetworkCallback<CountryListResponse>() {
            @Override
            public void onSuccess(CountryListResponse response) {
                if (response != null && response.getCountries() != null){
                    allAreas.clear();
                    List<String> areaNames = new ArrayList<>();
                    for (Country country : response.getCountries()){
                        String areaName = country.getStrArea();
                        String flagResource = "flag_" + areaName.toLowerCase();
                        allAreas.add(new FilterItem(areaName, flagResource, "area"));
                        areaNames.add(areaName);
                    }
                    view.setAreaSpinner(areaNames);
                    if (currentFilterType.equals("area")){
                        view.showAreas(allAreas);
                    }
                }
                checkLoadingComplete();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                checkLoadingComplete();

            }
        });

        repository.getCategories(new MealsRepository.NetworkCallback<CategoryListResponse>() {
            @Override
            public void onSuccess(CategoryListResponse response) {
                if (response != null && response.getCategories() != null){
                    allCategories.clear();
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : response.getCategories()){
                        String categoryName = category.getStrCategory();
                        String imageUrl = "https://www.themealdb.com/images/category/" + categoryName + ".png";
                        allCategories.add(new FilterItem(categoryName , imageUrl , "category"));
                        categoryNames.add(categoryName);

                    }
                    view.setCategorySpinner(categoryNames);
                    if (currentFilterType.equals("category")){
                        view.showCategories(allCategories);
                    }
                }
                checkLoadingComplete();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                checkLoadingComplete();

            }
        });

        repository.getIngredients(new MealsRepository.NetworkCallback<IngredientListResponse>() {
            @Override
            public void onSuccess(IngredientListResponse response) {
                if (response != null && response.getIngredients() != null){
                    allIngredients.clear();
                    List<String> ingredientNames = new ArrayList<>();
                    for (Ingredient ingredient : response.getIngredients()){
                        String ingredientName = ingredient.getStrIngredient();
                        String imageUrl = "https://www.themealdb.com/images/ingredients/" + ingredientName + ".png";
                        allIngredients.add(new FilterItem(ingredientName, imageUrl,"ingredient"));
                        ingredientNames.add(ingredientName);
                    }
                    view.setIngredientSpinner(ingredientNames);
                    if (currentFilterType.equals("ingredient")){
                        view.showIngredients(allIngredients);
                    }
                }
                checkLoadingComplete();
            }

            @Override
            public void onFailure(String errorMessage) {
                view.showError(errorMessage);
                checkLoadingComplete();

            }
        });

    }

    private void checkLoadingComplete(){
        if (!allAreas.isEmpty() && !allCategories.isEmpty() && !allIngredients.isEmpty()){
            view.hideLoading();
        }
    }

    @Override
    public void onSearchTextChanged(String query) {
        List<FilterItem> filteredList = new ArrayList<>();
        List<FilterItem> sourceList = getCurrentFilterList();

        for (FilterItem item : sourceList){
            if (item.getName().toLowerCase().contains(query.toLowerCase())){
                filteredList.add(item);
            }
        }
        displayFilteredList(filteredList);

    }

    @Override
    public void onAreaSelected(String area) {
        currentFilterType = "area";
        view.showAreas(allAreas);
        onSearchTextChanged("");

    }

    @Override
    public void onCategorySelected(String category) {
        currentFilterType = "category";
        view.showCategories(allCategories);
        onSearchTextChanged("");

    }

    @Override
    public void onIngredientSelected(String ingredient) {
        currentFilterType = "ingredient";
        view.showIngredients(allIngredients);
        onSearchTextChanged("");

    }

    @Override
    public void onFilterItemClicked(FilterItem filterItem) {

        view.navigateToFilteredDishSearch(filterItem);

    }

    private List<FilterItem> getCurrentFilterList(){
        switch (currentFilterType){
            case "category":
                return allCategories;
            case "ingredient":
                return allIngredients;
            case "area":
                return allAreas;
            default:
                return allAreas;
        }
    }

    private void displayFilteredList(List<FilterItem> filteredList){
        switch (currentFilterType){
            case "category":
                view.showCategories(filteredList);
                break;
            case "ingredient":
                view.showIngredients(filteredList);
                break;
            case "area":
                view.showAreas(filteredList);
                break;
            default:
                view.showAreas(filteredList);
                break;

        }
    }
}
