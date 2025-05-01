package com.example.elakil.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryListResponse {
    @SerializedName("meals")
    private List<Country> countries;

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}
