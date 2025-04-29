package com.example.elakil.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealResponse {
    @SerializedName("meals")
    private List<Meal> meals ;
    private List<Category> categories ;
    private List<Country> countries ;
    @SerializedName("meals")
    private List<Ingredient> ingredient;

    public List<Meal> getMeals(){

        return meals ;
    }

    public void setMeals(List<Meal> meals) {

        this.meals = meals;
    }
    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<Ingredient> getIngredients() {
        return ingredient;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredient = ingredients;
    }


}
