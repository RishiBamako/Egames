package com.rginfotech.egames.model;

public class RightPowerModel {
    private String value;
    private boolean isSelected;
    private String r_p_n_available;
    private boolean outOfStock;

    public boolean isOutOfStock() {
        return outOfStock;
    }

    public void setOutOfStock(boolean outOfStock) {
        this.outOfStock = outOfStock;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getR_p_n_available() {
        return r_p_n_available;
    }

    public void setR_p_n_available(String r_p_n_available) {
        this.r_p_n_available = r_p_n_available;
    }
}
