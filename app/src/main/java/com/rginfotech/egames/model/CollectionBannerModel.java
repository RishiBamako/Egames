package com.rginfotech.egames.model;

public class CollectionBannerModel {

    private String image;
    private String type;
    private String value;

    public CollectionBannerModel(String image, String type, String value) {
        this.image = image;
        this.type = type;
        this.value = value;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
