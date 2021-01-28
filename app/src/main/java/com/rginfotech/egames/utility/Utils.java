package com.rginfotech.egames.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.SearchResultsActivity;
import com.rginfotech.egames.model.FilterData;
import com.rginfotech.egames.model.ProductList;
import com.rginfotech.egames.model.ProductSearchModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    public static final String PREF_NAME = "lenzzouser";
    public static final String FILTER_NAME = "lenzzofilter";
    public static List<FilterData> selectedData = new ArrayList<>();
    public static List<FilterData> selectedDataBackup = new ArrayList<>();
    public static boolean shouldFilterApply = false;
    public static String lastSelectedCountryId = "";
    public static String search_text = "";
    public static String isFromHomeSearch = "";
    public static String search_textBackup = "";
    public static boolean isSearchedApplied = false;
    public static boolean isFromDealOfDays = false;
    public static List<String> brandsIs = new ArrayList();

    public static String key = "";
    public static String value = "";
    public static int selectedSortPosition = 0;
    public static List<ProductSearchModel> productLists = new ArrayList<>();
    public static boolean isFromDelete = false;



    public static void saveEventStatus(String value, String key, Activity activity) {
        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString(key, value).apply();
    }

    public static String getEventStatus(String defaultValue, String key, Activity activity) {
        String status = PreferenceManager.getDefaultSharedPreferences(activity).getString(key, defaultValue);
        return status;
    }



    public static void hideKeyBoard(Context context, AutoCompleteTextView autoCompleteTextView) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
    }
    public static void showKeyBoard(Context context, AutoCompleteTextView autoCompleteTextView) {
        if (autoCompleteTextView.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(autoCompleteTextView, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public static String dateFormateChange(String dateToFormat, String inputFormat, String outputFormat) {
        String result="";
        SimpleDateFormat formatterOld = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(outputFormat, Locale.getDefault());
        Date date=null;
        try {
            date = formatterOld.parse(dateToFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;

    }
}
