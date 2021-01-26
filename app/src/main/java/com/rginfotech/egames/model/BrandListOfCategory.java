package com.rginfotech.egames.model;

import org.json.JSONArray;

import java.io.Serializable;

public class BrandListOfCategory implements Serializable {

    private String id;
    private String name;
    private String slug;
    private String icon;
    private String status;
    private String brand_image;
    private String category_id;
    private String ui_order;
    private String start_range;
    private String end_range;
    private String offer_id;
    private String offer_name;
    private String offer_subtitle;
    private boolean isSelected;
    private JSONArray jsonArray;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getUi_order() {
        return ui_order;
    }

    public void setUi_order(String ui_order) {
        this.ui_order = ui_order;
    }

    public String getStart_range() {
        return start_range;
    }

    public void setStart_range(String start_range) {
        this.start_range = start_range;
    }

    public String getEnd_range() {
        return end_range;
    }

    public void setEnd_range(String end_range) {
        this.end_range = end_range;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_name() {
        return offer_name;
    }

    public void setOffer_name(String offer_name) {
        this.offer_name = offer_name;
    }

    public String getOffer_subtitle() {
        return offer_subtitle;
    }

    public void setOffer_subtitle(String offer_subtitle) {
        this.offer_subtitle = offer_subtitle;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
