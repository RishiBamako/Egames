package com.rginfotech.egames.model;

import java.util.List;

public class CategoryBrandList {
    private String category_id;
    private String categoryName;
    private String categoryName_ar;
    private List<BrandList> categoryBrandList;

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName_ar() {
        return categoryName_ar;
    }

    public void setCategoryName_ar(String categoryName_ar) {
        this.categoryName_ar = categoryName_ar;
    }

    public List<BrandList> getCategoryBrandList() {
        return categoryBrandList;
    }

    public void setCategoryBrandList(List<BrandList> categoryBrandList) {
        this.categoryBrandList = categoryBrandList;
    }
}
