package com.rginfotech.egames.model;

public class PowerModel {
    private String value;
    private boolean isSelected;
    private String l_p_n_available;
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

    public String getL_p_n_available() {
        return l_p_n_available;
    }

    public void setL_p_n_available(String l_p_n_available) {
        this.l_p_n_available = l_p_n_available;
    }
}
