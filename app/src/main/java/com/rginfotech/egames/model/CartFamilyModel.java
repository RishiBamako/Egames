package com.rginfotech.egames.model;

import java.util.List;

public class CartFamilyModel {
     /*"family_id": "44",
             "family_name": "One-Day (Mystic)",
             "family_name_ar": "One-Day (Mystic)",
             "price": "5.500",
             "total": "16.500",
             "quantity": "1",
             "quantity_left": null,
             "quantity_right": "0",
             "free_quantity": "0",
             "get_type": "0",
             "discount_type": "0",
             "current_currency": "KWD",
             "offer_flag": 0,
             "offer_id": "77",
             "offer_name": "Buy3+1",
             "offer_name_ar": "Buy3+BAG",
             "child": [*/

    private String familyId;
    private String familyName;
    private String familyNameAr;
    private String price;
    private String total;
    private String quantity;
    private String quantityLeft;
    private String quantityRight;
    private String freeQuantity;
    private String getType;
    private String discountType;
    private String currentCurrency;
    private String offerFlag;
    private String offerId;
    private String offerName;
    private String offerNameAr;
    private List<UserCartModel> userCartModelList;

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getFreeQuantity() {
        return freeQuantity;
    }

    public void setFreeQuantity(String freeQantity) {
        this.freeQuantity = freeQantity;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyNameAr() {
        return familyNameAr;
    }

    public void setFamilyNameAr(String familyNameAr) {
        this.familyNameAr = familyNameAr;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(String quantityLeft) {
        this.quantityLeft = quantityLeft;
    }

    public String getQuantityRight() {
        return quantityRight;
    }

    public void setQuantityRight(String quantityRight) {
        this.quantityRight = quantityRight;
    }

    public String getGetType() {
        return getType;
    }

    public void setGetType(String getType) {
        this.getType = getType;
    }

    public String getCurrentCurrency() {
        return currentCurrency;
    }

    public void setCurrentCurrency(String currentCurrency) {
        this.currentCurrency = currentCurrency;
    }

    public String getOfferFlag() {
        return offerFlag;
    }

    public void setOfferFlag(String offerFlag) {
        this.offerFlag = offerFlag;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferNameAr() {
        return offerNameAr;
    }

    public void setOfferNameAr(String offerNameAr) {
        this.offerNameAr = offerNameAr;
    }

    public List<UserCartModel> getUserCartModelList() {
        return userCartModelList;
    }

    public void setUserCartModelList(List<UserCartModel> userCartModelList) {
        this.userCartModelList = userCartModelList;
    }
}
