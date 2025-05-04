package com.example.elakil.model;

public class IngredientItem {
    private String name ;
    private String imageUrl ;
    private String measurement ;

    public IngredientItem(String name, String imageUrl, String measurement) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
