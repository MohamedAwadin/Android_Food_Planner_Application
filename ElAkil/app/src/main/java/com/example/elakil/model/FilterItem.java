package com.example.elakil.model;

public class FilterItem {
    private String name ;
    private String imageUrl ;
    private String type ;

    public FilterItem(String name, String imageUrl, String type) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }
}
