package com.rginfotech.egames.model;

import java.io.Serializable;

public class ProductList implements Serializable {

    private String id;
    private String quantity;
    private String deal_otd;
    private String deal_otd_discount;
    private String sell_quantity;
    private String user_id;
    private String cate_id;
    private String cate_name;
    private String title;
    private String wishlist;





    private String description;
    private String product_image;
    private String product_images;
    private String model_no;
    private String sku_code;
    private String price;
    private String current_currency;
    private String sale_price;
    private String negotiable;
    private String brand_name;
    private String brand_id;
    private String variation_color;
    private String tags;
    private String is_hide;
    private String reviewed;
    private String featured;
    private String archived;
    private String deleted_at;
    private String created_at;
    private String updated_at;
    private String status;
    private String stock_flag;
    private String rating;
    private String replacement;
    private String releted_product;
    private String offer_id;
    private String offer_name;
    private boolean isSelected;

    public String getWishlist() {
        return wishlist;
    }

    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }

    public String getDeal_otd() {
        return deal_otd;
    }

    public void setDeal_otd(String deal_otd) {
        this.deal_otd = deal_otd;
    }

    public String getDeal_otd_discount() {
        return deal_otd_discount;
    }

    public void setDeal_otd_discount(String deal_otd_discount) {
        this.deal_otd_discount = deal_otd_discount;
    }

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_images() {
        return product_images;
    }

    public void setProduct_images(String product_images) {
        this.product_images = product_images;
    }

    public String getModel_no() {
        return model_no;
    }

    public void setModel_no(String model_no) {
        this.model_no = model_no;
    }

    public String getSku_code() {
        return sku_code;
    }

    public void setSku_code(String sku_code) {
        this.sku_code = sku_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrent_currency() {
        return current_currency;
    }

    public void setCurrent_currency(String current_currency) {
        this.current_currency = current_currency;
    }

    public String getSale_price() {
        return sale_price;
    }

    public void setSale_price(String sale_price) {
        this.sale_price = sale_price;
    }

    public String getNegotiable() {
        return negotiable;
    }

    public void setNegotiable(String negotiable) {
        this.negotiable = negotiable;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getVariation_color() {
        return variation_color;
    }

    public void setVariation_color(String variation_color) {
        this.variation_color = variation_color;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIs_hide() {
        return is_hide;
    }

    public void setIs_hide(String is_hide) {
        this.is_hide = is_hide;
    }

    public String getReviewed() {
        return reviewed;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getArchived() {
        return archived;
    }

    public void setArchived(String archived) {
        this.archived = archived;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStock_flag() {
        return stock_flag;
    }

    public void setStock_flag(String stock_flag) {
        this.stock_flag = stock_flag;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public String getReleted_product() {
        return releted_product;
    }

    public void setReleted_product(String releted_product) {
        this.releted_product = releted_product;
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


    public String getSell_quantity() {
        return sell_quantity;
    }

    public void setSell_quantity(String sell_quantity) {
        this.sell_quantity = sell_quantity;
    }
}
