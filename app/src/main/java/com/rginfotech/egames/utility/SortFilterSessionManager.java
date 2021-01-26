package com.rginfotech.egames.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SortFilterSessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    private static String Filter_Brands = "Brands";
    private static String Filter_Tags = "Tags";
    private static String Filter_Colors = "Color";
    private static String Filter_Replacement = "Replacement";
    private static String Filter_Gender = "Gender";
    private static String Filter_Rating = "Rating";

    public SortFilterSessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Utils.FILTER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }


    public void clearPreferences() {
        pref.edit().clear().apply();

    }
    public void clearSelectedFilter(){
        pref.edit().remove("Brands")
                .remove("Tags")
                .remove("Color")
                .remove("Replacement")
                .remove("Gender")
                .remove("Rating")
                .apply();
    }
    public void delete(String filterName,String slug){
        pref.edit().remove(filterName).remove(slug).commit();
    }

    public String getFilter_Brands() {
        return pref.getString(Filter_Brands, "");
    }

    public void setFilter_Brands(String filter_Brands) {
        editor.putString(Filter_Brands, filter_Brands);
        editor.commit();
    }

    public String getFilter_Tags() {
        return pref.getString(Filter_Tags, "");
    }

    public void setFilter_Tags(String filter_Tags) {
        editor.putString(Filter_Tags, filter_Tags);
        editor.commit();
    }

    public String getFilter_Colors() {
        return pref.getString(Filter_Colors, "");
    }

    public void setFilter_Colors(String filter_Colors) {
        editor.putString(Filter_Colors, filter_Colors);
        editor.commit();
    }

    public String getFilter_Replacement() {
        return pref.getString(Filter_Replacement, "");
    }

    public void setFilter_Replacement(String filter_Replacement) {
        editor.putString(Filter_Replacement, filter_Replacement);
        editor.commit();
    }

    public String getFilter_Gender() {
        return pref.getString(Filter_Gender, "");
    }

    public void setFilter_Gender(String filter_Gender) {
        editor.putString(Filter_Gender, filter_Gender);
        editor.commit();
    }

    public String getFilter_Rating() {
        return pref.getString(Filter_Rating, "");
    }

    public void setFilter_Rating(String filter_Rating) {
        editor.putString(Filter_Rating, filter_Rating);
        editor.commit();
    }
}
