package com.example.elakil.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "meals")
public class Meal {
    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getStrArea() {
        return strArea;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public String getStrInstruction() {
        return strInstruction;
    }

    public void setStrInstruction(String strInstruction) {
        this.strInstruction = strInstruction;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<String> measurements) {
        this.measurements = measurements;
    }

    @PrimaryKey
    private String idMeal;
    private String strMeal;
    private String strMealThumb;
    private String strArea;
    private String strInstruction;
    private String strYoutube;
    private boolean isFavorite ;

    @Ignore
    private List<String> ingredients ;
    @Ignore
    private List<String> measurements ;



}
