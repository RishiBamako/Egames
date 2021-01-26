package com.rginfotech.egames.model;

import org.json.JSONArray;

public class FilterModel {
    private String title;
    private boolean isCountrySelected;
    private String title_ar;
    private JSONArray jsonArray;

    public boolean isCountrySelected() {
        return isCountrySelected;
    }

    public void setCountrySelected(boolean countrySelected) {
        isCountrySelected = countrySelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

}
