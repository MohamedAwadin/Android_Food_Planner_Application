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

        void setSelectedFilter(String filterType);

        void navigateToFilteredDishSearch(FilterItem filterItem);

    }

    interface Presenter{
        void loadFilters();
        void onSearchTextChanged(String query);
        void onFilterButtonClicked(String filterType);

        void onFilterItemClicked(FilterItem filterItem);

    }
}
