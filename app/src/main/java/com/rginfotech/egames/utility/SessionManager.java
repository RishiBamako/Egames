package com.rginfotech.egames.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    private static String USER_ID = "user_id";
    private static String USER_EMAIL = "user_email";
    private static String USER_PASSWORD = "user_password";
    private static String RANDOM_VALUE = "random_value";
    private static String USER_NAME = "user_name";
    private static String COUNTRY_CODE = "country_code";
    private static String CURRENCY_CODE = "currency_code";
    private static String BRAND_ID = "brand_id";
    private static String LEFT_EYE_POWER = "left_eye_power";
    private static String RIGHT_EYE_POWER = "right_eye_power";
    private static String LANGUAGE_SELECTED ="language_selected";
    private static String FROM_NOTIFICATION = "from_notification";
    private static String COLLECTION_BRAND_ID = "collection_brand_id";
    private static String USER_TYPE = "user_type";
    private static String APPLY_FILTER = "apply_filter";

    private static String PUSH_BRAND_ID = "push_brand_id";
    private static String PUSH_FAMILY_ID = "push_family_id";


    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(Utils.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void clearPreferences() {
        pref.edit().clear().apply();

    }

    public void clearSelectedPreferences() {
        pref.edit().remove("user_id")
                .remove("user_email")
                .remove("user_password")
                .remove("random_value")
                .remove("user_name")
                .remove("brand_id")
                .remove("left_eye_power")
                .remove("right_eye_power")
                .remove("from_notification")
                .remove("collection_brand_id")
                .remove("user_type")
                .remove("apply_filter")
                .remove("user_type")
                .remove("apply_filter")
                .apply();

    }

    public void removeId(String userId,String pass){
        pref.edit().remove(userId).remove(pass).apply();
    }

    public String getPushFamilyId() {
        return pref.getString(PUSH_FAMILY_ID, "");
    }

    public void setPushFamilyId(String familyId) {
        editor.putString(PUSH_FAMILY_ID, familyId);
        editor.commit();
    }

    public String getPushBrandId() {
        return pref.getString(PUSH_BRAND_ID, "");
    }

    public void setPushBrandId(String brandId) {
        editor.putString(PUSH_BRAND_ID, brandId);
        editor.commit();
    }

    public String getUserType() {
        return pref.getString(USER_TYPE, "");
    }

    public void setUserType(String userType) {
        editor.putString(USER_TYPE, userType);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        editor.putString(USER_EMAIL, userEmail);
        editor.commit();
    }

    public String getUserPassword() {
        return pref.getString(USER_PASSWORD, "");
    }

    public void setUserPassword(String userPassword) {
        editor.putString(USER_PASSWORD, userPassword);
        editor.commit();
    }

    public String getRandomValue() {
        return pref.getString(RANDOM_VALUE, "");
    }

    public void setRandomValue(String randomValue) {
        editor.putString(RANDOM_VALUE, randomValue);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public String getCountryCode() {
        return pref.getString(COUNTRY_CODE, "");
    }

    public void setCountryCode(String countryCode) {
        /*editor.putString(COUNTRY_CODE, countryCode);
        editor.commit();*/
        PreferenceManager.getDefaultSharedPreferences(_context).edit().putString(COUNTRY_CODE, countryCode).apply();
    }

    public String getCurrencyCode() {
      /*  return pref.getString(CURRENCY_CODE, "");*/
        String status = PreferenceManager.getDefaultSharedPreferences(_context).getString(COUNTRY_CODE, "");
        return status;
    }

    public void setCurrencyCode(String currencyCode) {
        editor.putString(CURRENCY_CODE, currencyCode);
        editor.commit();
    }

    public String getBrandId() {
        return pref.getString(BRAND_ID, "");
    }

    public void setBrandId(String brandId) {
        editor.putString(BRAND_ID, brandId);
        editor.commit();
    }

    public String getLeftEyePower() {
        return pref.getString(LEFT_EYE_POWER, "");
    }

    public void setLeftEyePower(String leftEyePower) {
        editor.putString(LEFT_EYE_POWER, leftEyePower);
        editor.commit();
    }

    public String getRightEyePower() {
        return pref.getString(RIGHT_EYE_POWER, "");
    }

    public void setRightEyePower(String rightEyePower) {
        editor.putString(RIGHT_EYE_POWER, rightEyePower);
        editor.commit();
    }

    public String getLanguageSelected() {
        return pref.getString(LANGUAGE_SELECTED, "");
    }

    public void setLanguageSelected(String languageSelected) {
        editor.putString(LANGUAGE_SELECTED, languageSelected);
        editor.commit();
    }

    public boolean getIsFromOfferNotification() {
        return pref.getBoolean(FROM_NOTIFICATION, false);
    }

    public void setIsFromOfferNotification(boolean flag) {
        editor.putBoolean(FROM_NOTIFICATION, flag);
        editor.commit();
    }

    public String getCollectionBrandId() {
        return pref.getString(COLLECTION_BRAND_ID, "");
    }

    public void setCollectionBrandId(String collection_brand_id) {
        editor.putString(COLLECTION_BRAND_ID, collection_brand_id);
        editor.commit();
    }

    public boolean shouldFilterApply() {
        return pref.getBoolean(APPLY_FILTER, false);
    }

    public void setShouldFilterApply(boolean flag) {
        editor.putBoolean(APPLY_FILTER, flag);
        editor.commit();
    }
}
