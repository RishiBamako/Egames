package com.rginfotech.egames.model;

import org.json.JSONArray;

public class FilterListModel {

    private String id;
    private String name;
    private String name_ar;
    private JSONArray jsonArray;
    private String slug;
    private String value;
    private boolean isCountrySelected;

    private String category;

    public boolean isCountrySelected() {
        return isCountrySelected;
    }

    public void setCountrySelected(boolean countrySelected) {
        isCountrySelected = countrySelected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_ar() {
        return name_ar;
    }

    public void setName_ar(String name_ar) {
        this.name_ar = name_ar;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
