package com.rginfotech.egames.model;

import java.io.Serializable;

public class OfferModel implements Serializable {
    private String id;
    private String name;
    private String offer_subtitle;
    private String buy;
    private String get_type;
    private String free;
    private String discount_type;
    private String discount;
    private String offer_type_id;
    private String category_id;
    private String brand_id;
    private String family_id;
    private String product_id;
    private String start_date;
    private String end_date;
    private String status;

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

    public String getOffer_subtitle() {
        return offer_subtitle;
    }

    public void setOffer_subtitle(String offer_subtitle) {
        this.offer_subtitle = offer_subtitle;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getGet_type() {
        return get_type;
    }

    public void setGet_type(String get_type) {
        this.get_type = get_type;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOffer_type_id() {
        return offer_type_id;
    }

    public void setOffer_type_id(String offer_type_id) {
        this.offer_type_id = offer_type_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
