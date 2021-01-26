package com.rginfotech.egames.model;

public class FilterData {
    private String categoryName;
    private String id;
    private String titleMain;

    public FilterData(String categoryName, String id, String titleMain) {
        this.categoryName = categoryName;
        this.id = id;
        this.titleMain = titleMain;
    }

    public String getTitleMain() {
        return titleMain;
    }

    public void setTitleMain(String titleMain) {
        this.titleMain = titleMain;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
