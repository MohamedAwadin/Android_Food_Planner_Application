package com.example.elakil.presentation.search;

import com.example.elakil.model.FilterItem;

import java.util.List;

public interface SearchContract {
    interface View{
        void showLoading();
        void hideLoading();
        void showError(String message);
        void showAreas(List<FilterItem> areas);
        void showCategories(List<FilterItem> categories);
        void showIngredients(List<FilterItem> ingredients);

        void setAreaSpinner(List<String> areas);
        void setCategorySpinner(List<String> categories);
        void setIngredientSpinner(List<String> ingredients);

        void navigateToFilteredDishSearch(FilterItem filterItem);

    }

    interface Presenter{
        void loadFilters();
        void onSearchTextChanged(String query);
        void onAreaSelected(String area);
        void onCategorySelected(String category);
        void onIngredientSelected(String ingredient);

        void onFilterItemClicked(FilterItem filterItem);

    }
}
