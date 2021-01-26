package com.rginfotech.egames.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommanMethod {

    public static final int LOGIN_REQ_CODE = 100;
    public static final int REGISTER_REQ_CODE = 200;

    public static String getTwoPlaceDecimalValue(float value){
        //NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        try{
            /*Number number = format.parse(String.format ("%.2f", value));
            return number.floatValue();*/
            //DecimalFormat formatter = new DecimalFormat("###,###,##0.00");
            DecimalFormat formatter = new DecimalFormat("0.00");
            //DecimalFormat formatter = new DecimalFormat("#,###.00");
            return formatter.format(value);

        }catch (Exception ex){
            ex.printStackTrace();
            return "0.00";
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isInternetConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo;
        try {
            netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CommanMethod.getCustomOkAlert(context, context.getResources().getString(R.string.internet_error));
        //Toast.makeText(context, context.getResources().getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        return false;
    }

    public static Bitmap  rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

 /*   public static Bitmap getRotatedBitmap(Bitmap bitmap,String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Bitmap rotatedBitmap = null;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }
*/



    public static Bitmap getRotatedBitmap(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }


    public static void getCustomOkAlert(Context context, String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        info_tv.setText(message);
        ok_tv.setOnClickListener(view ->  {

            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public static Dialog getCustomProgressDialog(Context context){
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.progress_dialog_alert_layout);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.show();


        return dialog;
    }

    public static Dialog getProgressDialogForFragment(Context context){
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.progress_dialog_for_fragment_layout);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.show();


        return dialog;
    }

    public static void getCustomGOHome(Context context, String message){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        info_tv.setText(message);
        ok_tv.setOnClickListener(view ->  {
            Intent intent = new Intent(context,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getStringByLocal(Activity context, int id, String locale) {
        Configuration configuration = new Configuration(context.getResources().getConfiguration());
        configuration.setLocale(new Locale(locale));
        return context.createConfigurationContext(configuration).getResources().getString(id);
    }

    public static String getRandomNumber(){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((100000 + rnd.nextInt(900000)));
        for (int i = 0; i < 6; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);
        return sb.toString();
    }

    public static BigDecimal getCountryWiseDecimalNumber(Context context, String textNumber){
        SessionManager sessionManager = new SessionManager(context);
        String dhjsdh = sessionManager.getCurrencyCode();
        if (sessionManager.getCurrencyCode().equalsIgnoreCase("KWD")){
            return new BigDecimal(textNumber).setScale(3, RoundingMode.HALF_UP);
        }else {

            return new BigDecimal(textNumber).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public static BigDecimal getCountryWiseDecimalNumberFloatInput(Context context, float floatNumber){
        SessionManager sessionManager = new SessionManager(context);
        if (sessionManager.getCurrencyCode().equalsIgnoreCase("KWD")){
            return new BigDecimal(floatNumber).setScale(3, RoundingMode.HALF_UP);
        }else {

            return new BigDecimal(floatNumber).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public static String getMessage(Context context, JSONObject jsonObject){
        try {
            String message = jsonObject.getString("message");
            String message_ar = jsonObject.optString("message_ar", "");
            if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                return message;
            }else{
                if (TextUtils.isEmpty(message_ar)) {
                    return message;
                }else {
                    return message_ar;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return "";
        }
    }

    public static boolean isOutOfStock(String stockFlag, String quantity){

        if(stockFlag.equals("0")){
            return true;
        }else{
            if (quantity.equals("null") || Integer.parseInt(quantity)<1){
               return true;
            }else {
               return false;
            }
        }
    }
    public static boolean isAvailableStock(String stockFlag, String quantity){

        if(stockFlag.equals("0")){
            return true;
        }else{
            if (quantity.equals("null") || Integer.parseInt(quantity)<1){
               return true;
            }else {
               return false;
            }
        }
    }

    public static String getOfferName(Context context, String offerData) {

        try {
            Object json = new JSONTokener(offerData.trim()).nextValue();
            if (json instanceof JSONObject) {
                JSONObject jsonObject = new JSONObject(offerData.trim());
                String offerName = "";
                if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                    offerName = jsonObject.optString("name","");
                }else{
                    offerName = jsonObject.optString("name_ar","");
                }

                return offerName;
            }else if (json instanceof JSONArray){
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    return "";
    }
}
