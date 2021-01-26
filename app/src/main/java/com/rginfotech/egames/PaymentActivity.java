package com.rginfotech.egames;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.myfatoorah.sdk.model.executepayment.MFExecutePaymentRequest;
import com.myfatoorah.sdk.model.initiatepayment.MFInitiatePaymentRequest;
import com.myfatoorah.sdk.model.initiatepayment.MFInitiatePaymentResponse;
import com.myfatoorah.sdk.model.initiatepayment.PaymentMethod;
import com.myfatoorah.sdk.model.paymentstatus.MFGetPaymentStatusResponse;
import com.myfatoorah.sdk.utils.MFAPILanguage;
import com.myfatoorah.sdk.utils.MFCurrencyISO;
import com.myfatoorah.sdk.views.MFResult;
import com.myfatoorah.sdk.views.MFSDK;
import com.rginfotech.egames.adapter.PaymentAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.interfacelenzzo.PaymentInterface;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.model.PaymentModel;
import com.rginfotech.egames.model.UserCartModel;
import com.rginfotech.egames.payment_fatoorah.Config;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;
import com.rginfotech.egames.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Unit;

public class PaymentActivity extends BaseActivity implements View.OnClickListener, PaymentInterface {

    Dialog dialogPayment;
    private SessionManager sessionManager;
    private TextView total_price_text_view;
    private TextView delivery_price_text_view;
    private TextView grand_price_text_view;
    private LinearLayout pay_liner_layout;
    private LinearLayout shipping_liner_layout;
    private RecyclerView payment_recycler_view;
    private PaymentAdapter paymentAdapter;
    private List<PaymentModel> paymentModelList;
    private ArrayList<PaymentMethod> paymentMethodsListFat;
    private int paymentModeId_Fatoo;
    private String paymentModeid = "";
    private String shippingCharge = "";
    private String address_id;
    private String cart_id;
    private String giftId;
    private String sub_total;
    private String name;
    private String area;
    private String block;
    private String street;
    private String avenue;
    private String house;
    private String floor;
    private String flat;
    private String phone;
    private String comments;
    private Dialog dialog;
    private float grand_total;
    //private GifImageView gifImageView;
    private String sourceType = "";
    private String payment_type = "";
    private String coupon_id = "";
    private String coupon_code = "";
    private String coupon_price = "";
    private ImageView back_image;
    private BigDecimal bd;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        sessionManager = new SessionManager(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            address_id = bundle.getString("address_id");
            cart_id = bundle.getString("cart_id");
            giftId = bundle.getString("giftId");
            sub_total = bundle.getString("sub_total");
            name = bundle.getString("name");
            area = bundle.getString("area");
            block = bundle.getString("block");
            street = bundle.getString("street");
            avenue = bundle.getString("avenue");
            house = bundle.getString("house");
            floor = bundle.getString("floor");
            flat = bundle.getString("flat");
            phone = bundle.getString("phone");
            comments = bundle.getString("comments");
            coupon_id = bundle.getString("coupon_id");
            coupon_code = bundle.getString("coupon_code");
            coupon_price = bundle.getString("coupon_price");
            sessionManager.setCurrencyCode(bundle.getString("current_currency"));

        }

        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.pay_liner_layout).setOnClickListener(this);
        findViewById(R.id.shipping_liner_layout).setOnClickListener(this);

        //gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        total_price_text_view = (TextView) findViewById(R.id.total_price_text_view);
        delivery_price_text_view = (TextView) findViewById(R.id.delivery_price_text_view);
        grand_price_text_view = (TextView) findViewById(R.id.grand_price_text_view);
        payment_recycler_view = (RecyclerView) findViewById(R.id.payment_recycler_view);
        back_image = (ImageView) findViewById(R.id.back_image);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            back_image.setImageResource(R.drawable.arrow_30);
        } else {
            back_image.setImageResource(R.drawable.arrow_right_30);
        }

        online_Payment_Initiation();
        if (CommanMethod.isInternetConnected(PaymentActivity.this)) {
            payNow();
            shippingChange();
        }

        total_price_text_view.setText(sub_total + " " + sessionManager.getCurrencyCode());

    }
    private void online_Payment_Initiation() {
        if (Config.API_KEY.isEmpty()) {
            showAlertDialog("Missing API Key.. You can get it from here: https://myfatoorah.readme.io/docs/demo-information");
            return;
        }
        // TODO, don't forget to init the MyFatoorah SDK with the following line
        MFSDK.INSTANCE.init(Config.BASE_URL, Config.API_KEY);

        // You can custom your action bar, but this is optional not required to set this line
        MFSDK.INSTANCE.setUpActionBar("MyFatoorah Payment", R.color.toolbar_title_color,
                R.color.toolbar_background_color, true);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.pay_liner_layout:
                toCheckCartValue();
            /*if(paymentModeid.equals("") || paymentModeid.length()==0){
                if(grand_total>0.0){
                    dialogBox(getResources().getString(R.string.payment_dialog_alert),getResources().getString(R.string.payment_dialog_option));
                }else{
                    if (CommanMethod.isInternetConnected(PaymentActivity.this)){
                        userPayment();
                    }
                }
            }else{
                if(payment_type.equals("CASH ON DELIVERY")){
                    if (CommanMethod.isInternetConnected(PaymentActivity.this)){
                        userPayment();
                    }
                }else if(payment_type.equals("VISA/MASTER")){
                    sourceType = "src_card";
                    //System.out.println("grand_total" +String.format("%.2f",grand_total));
                    Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                    intent.putExtra("sourceType",sourceType);
                    intent.putExtra("user_id", sessionManager.getUserId());
                    intent.putExtra("address_id",address_id);
                    intent.putExtra("cart_id",cart_id);
                    intent.putExtra("gift_id",giftId);
                    intent.putExtra("payment_mode_id",paymentModeid);
                    intent.putExtra("delivery_charge",shippingCharge);
                    intent.putExtra("sub_total",sub_total);
                    *//*if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                        //intent.putExtra("total_order_price",String.format("%.2f",grand_total));
                        intent.putExtra("total_order_price",""+bd);
                    }else{
                        intent.putExtra("total_order_price",""+bd);
                    }*//*

                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));

                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                    intent.putExtra("coupon_id",coupon_id);
                    intent.putExtra("coupon_code",coupon_code);
                    intent.putExtra("coupon_price",coupon_price);
                    startActivity(intent);
                }else if(payment_type.equals("KNET")){
                    sourceType = "src_kw.knet";
                    Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                    intent.putExtra("sourceType",sourceType);
                    intent.putExtra("user_id", sessionManager.getUserId());
                    intent.putExtra("address_id",address_id);
                    intent.putExtra("cart_id",cart_id);
                    intent.putExtra("gift_id",giftId);
                    intent.putExtra("payment_mode_id",paymentModeid);
                    intent.putExtra("delivery_charge",shippingCharge);
                    intent.putExtra("sub_total",sub_total);
                    *//*if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                        //intent.putExtra("total_order_price",String.format("%.2f",grand_total));
                        intent.putExtra("total_order_price",""+bd);
                    }else{
                        intent.putExtra("total_order_price",""+bd);
                    }*//*
                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                    intent.putExtra("coupon_id",coupon_id);
                    intent.putExtra("coupon_code",coupon_code);
                    intent.putExtra("coupon_price",coupon_price);
                    startActivity(intent);
                }
            }*/
                break;
            case R.id.shipping_liner_layout:
                addressDialogBox();
                break;
        }
    }
   /* private void dialogBox(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.success_dialog_box);
        TextView alert_text = (TextView)dialog.findViewById(R.id.item_text_view);
        alert_text.setText(this.getString(R.string.payment_dialog_alert));
        TextView item_text_view1 = (TextView)dialog.findViewById(R.id.item_text_view1);
        item_text_view1.setText(this.getString(R.string.payment_dialog_option));
        LinearLayout layout = (LinearLayout)dialog.findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }*/

    private void dialogBox(String title, String info) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.comman_alert_dialog_box);
        TextView alerte_title = (TextView) dialog.findViewById(R.id.title_box);
        alerte_title.setText(title);
        TextView message = (TextView) dialog.findViewById(R.id.info_tv);
        message.setText(info);
        TextView ok_tv = (TextView) dialog.findViewById(R.id.ok_tv);

        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void payNow() {
        dialogPayment = CommanMethod.getCustomProgressDialog(this);
        dialogPayment.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "paymentmode", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    paymentModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            PaymentModel paymentModel = new PaymentModel();
                            paymentModel.setId(jsonObject.getString("id"));
                            paymentModel.setName(jsonObject.getString("name"));
                            paymentModel.setName_ar(jsonObject.getString("name_ar"));
                            paymentModel.setLogo(API.PAYMENT_MODE_LOGO + jsonObject.getString("logo"));
                            paymentModel.setStatus(jsonObject.getString("status"));
                            Log.e("IMAGEEEE", paymentModel.getName());
                            paymentModelList.add(paymentModel);
                        }

                        dialogPayment.dismiss();
                        payment_recycler_view.setLayoutManager(new LinearLayoutManager(PaymentActivity.this, RecyclerView.VERTICAL, false));
                        paymentAdapter = new PaymentAdapter(PaymentActivity.this, paymentModelList);
                        payment_recycler_view.setAdapter(paymentAdapter);

                    } else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }

                initiatePayment();


                ///download online payment methods names
                //GridLayoutManager gridLayoutManager = new GridLayoutManager(PaymentActivity.this,1);
               /* payment_recycler_view.setLayoutManager(new LinearLayoutManager(PaymentActivity.this, RecyclerView.VERTICAL, false));
                paymentAdapter = new PaymentAdapter(PaymentActivity.this, paymentModelList);
                payment_recycler_view.setAdapter(paymentAdapter);*/

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sessionManager.getUserId());

                return params;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }
    private void shippingChange() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "country_list", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (jsonObject.getString("currency_code").equals(sessionManager.getCurrencyCode())) {
                                shippingCharge = jsonObject.getString("delivery_charge");
                                /*if(shippingCharge.equals("0")){
                                    shippingCharge = "0.00";
                                }*/
                                delivery_price_text_view.setText(CommanMethod.getCountryWiseDecimalNumber(PaymentActivity.this, shippingCharge) + " " + sessionManager.getCurrencyCode());

                                Log.e("shipp", shippingCharge);
                                grand_total = Float.parseFloat(shippingCharge) + Float.parseFloat(sub_total);
                                Log.e("grand_total", grand_total + "");
                                if (grand_total > 0.0) {
                                    payment_recycler_view.setVisibility(View.VISIBLE);
                                } else {
                                    payment_recycler_view.setVisibility(View.GONE);
                                }//String.format("%.2f",grand_total)

                                if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                                    grand_price_text_view.setText(CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total) + " " + sessionManager.getCurrencyCode());
                                } else {
                                    //bd = new BigDecimal(grand_total).setScale(2, RoundingMode.HALF_UP);
                                    bd = CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total);
                                    //double newInput = bd.doubleValue();
                                    grand_price_text_view.setText(bd + " " + sessionManager.getCurrencyCode());
                                }

                            }
                        }

                    } else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cart_id", cart_id);
                params.put("only_selected_countries", "1");
                String isFreeChange = Utils.getEventStatus("","DELIVERY_OPTION",PaymentActivity.this);
                params.put("option", isFreeChange);

                return params;
            }
        };
        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void addressDialogBox() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.address_dialog);
        TextView close_dialog = (TextView) dialog.findViewById(R.id.close_dialog);
        TextView name = (TextView) dialog.findViewById(R.id.name);
        TextView area = (TextView) dialog.findViewById(R.id.area);
        TextView block = (TextView) dialog.findViewById(R.id.block);
        TextView street = (TextView) dialog.findViewById(R.id.street);
        TextView avenue = (TextView) dialog.findViewById(R.id.avenue);
        TextView house = (TextView) dialog.findViewById(R.id.house);
        TextView floor = (TextView) dialog.findViewById(R.id.floor);
        TextView flat = (TextView) dialog.findViewById(R.id.flat);
        TextView phone = (TextView) dialog.findViewById(R.id.phone);
        TextView comments = (TextView) dialog.findViewById(R.id.comments);
        if (!TextUtils.isEmpty(this.name)) {
            name.setVisibility(View.VISIBLE);
        } else {
            name.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.area)) {
            area.setVisibility(View.VISIBLE);
        } else {
            area.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.block)) {
            block.setVisibility(View.VISIBLE);
        } else {
            block.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.street)) {
            street.setVisibility(View.VISIBLE);
        } else {
            street.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.avenue)) {
            avenue.setVisibility(View.VISIBLE);
        } else {
            avenue.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.house)) {
            house.setVisibility(View.VISIBLE);
        } else {
            house.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.floor)) {
            floor.setVisibility(View.VISIBLE);
        } else {
            floor.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.flat)) {
            flat.setVisibility(View.VISIBLE);
        } else {
            flat.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.phone)) {
            phone.setVisibility(View.VISIBLE);
        } else {
            phone.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(this.comments)) {
            comments.setVisibility(View.VISIBLE);
        } else {
            comments.setVisibility(View.GONE);
        }


        name.setText(getResources().getString(R.string.user_billing_full_name) + " " + this.name);
        area.setText(getResources().getString(R.string.user_billing_area) + " " + this.area);
        block.setText(getResources().getString(R.string.user_billing_block) + " " + this.block);
        street.setText(getResources().getString(R.string.user_billing_street) + " " + this.street);
        avenue.setText(getResources().getString(R.string.user_billing_avenue) + " " + this.avenue);
        house.setText(getResources().getString(R.string.user_billing_house) + " " + this.house);
        floor.setText(getResources().getString(R.string.user_billing_floor) + " " + this.floor);
        flat.setText(getResources().getString(R.string.user_billing_flat) + " " + this.flat);
        phone.setText(getResources().getString(R.string.user_billing_phone) + " " + this.phone);
        comments.setText(getResources().getString(R.string.user_billing_comment) + " " + this.comments);
        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void userPayment() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "payment_by_cod", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        /*Intent intent = new Intent(PaymentActivity.this,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                        Utils.saveEventStatus("","DELIVERY_OPTION",PaymentActivity.this);
                        getCustomOkAlert(PaymentActivity.this, getResources().getString(R.string.order_place));

                    } else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    JSONObject data = new JSONObject(responseBody);
                    String message = CommanMethod.getMessage(PaymentActivity.this, data);
                    //CommanMethod.getCustomOkAlert(ChangePasswordActivity.this, message);
                    Toast.makeText(PaymentActivity.this, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException errorr) {
                    error.printStackTrace();
                }
                // gifImageView.setVisibility(View.GONE);
            }
        }) {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sessionManager.getUserId());
                params.put("address_id", address_id);
                params.put("cart_id", cart_id);
                params.put("gift_id", giftId);
                params.put("payment_mode_id", paymentModeid);
                params.put("delivery_charge", shippingCharge);
                params.put("sub_total", sub_total);
                /*if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                    //params.put("total_order_price",String.valueOf(grand_total));
                    params.put("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                }else{
                    params.put("total_order_price",""+bd);

                }*/
                params.put("total_order_price", "" + CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                params.put("current_currency", sessionManager.getCurrencyCode());
                if (!coupon_id.isEmpty() && !coupon_code.isEmpty() && !coupon_price.isEmpty()) {
                    params.put("coupon_price", coupon_price);
                    params.put("coupon_id", coupon_id);
                    params.put("coupon_code", coupon_code);
                }
                if (grand_total == 0.0) {
                    params.put("payment_status", "free");
                    params.put("payment_mode_id", "1001");
                }
                params.put("payment_status", "pending");
                params.put("loyality_point", getIntent().getStringExtra("loyality_point"));

                String isFreeChange = Utils.getEventStatus("","DELIVERY_OPTION",PaymentActivity.this);
                params.put("option", isFreeChange);
                Log.e("DELIVERY_OPTION",isFreeChange);

                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    public void getCustomOkAlert(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        info_tv.setText(message);
        ok_tv.setOnClickListener(view -> {
            Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void paymentMode(String paymentModeid, String payment_name) {
        this.paymentModeid = paymentModeid;
        this.payment_type = payment_name;

    }

    public void toCheckCartValue() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.setCancelable(true);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    boolean shouldProceed = true;
                    float totalToCheck = 0f;
                    StringBuilder stringBuilder = new StringBuilder();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));

                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject childObject = jsonArray.getJSONObject(j);
                            JSONArray childArray = childObject.getJSONArray("child");
                            for (int i = 0; i < childArray.length(); i++) {
                                JSONObject jsonObject2 = childArray.getJSONObject(i);
                                UserCartModel userCartModel = new UserCartModel();
                                userCartModel.setId(jsonObject2.getString("id"));
                                userCartModel.setUser_id(jsonObject2.getString("user_id"));
                                userCartModel.setStock_flag(jsonObject2.getString("stock_flag"));
                                String stock_flag = jsonObject2.getString("stock_flag");
                                String product_quantity = jsonObject2.getString("product_quantity");
                                String quantity = jsonObject2.getString("quantity");

                                String total = jsonObject2.getString("total");

                                if (CommanMethod.isOutOfStock(stock_flag, quantity)) {
                                    shouldProceed = false;
                                    break;
                                }
                                totalToCheck = totalToCheck + Float.parseFloat(total.replace(",", ""));
                                stringBuilder.append(jsonObject2.getString("id")).append(",");
                            }
                        }

                        String cart_id = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
                        if (!shouldProceed) {
                            CommanMethod.getCustomGOHome(PaymentActivity.this, getResources().getString(R.string.user_cart_out_of_stock));
                        } else if (!cart_id.equals(PaymentActivity.this.cart_id) && CommanMethod.getCountryWiseDecimalNumber(PaymentActivity.this, getIntent().getStringExtra("total")).equals("" + totalToCheck)) {
                            CommanMethod.getCustomGOHome(PaymentActivity.this, getResources().getString(R.string.something_wrong));
                        } else {

                            if (TextUtils.isEmpty(paymentModeid) || TextUtils.isEmpty(paymentModeid)) {
                                dialogBox(getResources().getString(R.string.payment_dialog_alert), getResources().getString(R.string.payment_dialog_option));
                            } else {
                                if (grand_total > 0.0) {
                                    if (payment_type.equalsIgnoreCase("CASH ON DELIVERY")) {
                                        if (CommanMethod.isInternetConnected(PaymentActivity.this)) {
                                            userPayment();
                                        }
                                    } else {
                                        for (int p = 0; p < paymentMethodsListFat.size(); p++) {
                                            String paymentNameFat = paymentMethodsListFat.get(p).getPaymentMethodEn();
                                            if (payment_type.toLowerCase().equals(paymentNameFat.toLowerCase())) {
                                                paymentModeId_Fatoo = paymentMethodsListFat.get(p).getPaymentMethodId();
                                                executePayment(paymentModeId_Fatoo, "" + grand_total);
                                            }
                                            if (p == paymentMethodsListFat.size() - 1) {
                                                if (TextUtils.isEmpty("" + paymentModeId_Fatoo)) {
                                                    CommanMethod.getCustomGOHome(PaymentActivity.this, getResources().getString(R.string.something_wrong));
                                                }
                                            }
                                        }
                                    }
                                    /*else if (payment_type.equals("VISA/MASTER")) {
                                        sourceType = "src_card";
                                        dialogPayment = CommanMethod.getCustomProgressDialog(PaymentActivity.this);
                                        dialogPayment.show();
                                        executePayment(Integer.parseInt(paymentModeid), "" + grand_total);
                                        //System.out.println("grand_total" +String.format("%.2f",grand_total));
                                    *//*Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);*//*
                                    } else if (payment_type.equals("KNET")) {
                                        sourceType = "src_kw.knet";
                                        dialogPayment = CommanMethod.getCustomProgressDialog(PaymentActivity.this);
                                        dialogPayment.show();
                                        executePayment(Integer.parseInt(paymentModeid), "" + grand_total);
                                    *//*Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);*//*
                                    }*/
                                } else {
                                    dialogBox(getResources().getString(R.string.payment_dialog_alert), getResources().getString(R.string.cannot_getting_payment));
                                }
                            }



                            /*if (grand_total > 0) {
                                if (CommanMethod.isInternetConnected(PaymentActivity.this)) {
                                    String amountString = grand_price_text_view.getText().toString().replace("KWD","");
                                    if(!TextUtils.isEmpty(amountString)){
                                        float amountToSend = Float.parseFloat(amountString);
                                        Intent intent = new Intent(PaymentActivity.this, MainActivityJava.class);
                                        intent.putExtra("amount", ""+amountToSend);
                                        startActivity(intent);
                                    }

                                }
                            } else {
                                dialogBox(getResources().getString(R.string.payment_dialog_alert), getResources().getString(R.string.cannot_getting_payment));

                            }
*//*
                            if(paymentModeid.equals("") || paymentModeid.length()==0){
                                if(grand_total>0.0){
                                    dialogBox(getResources().getString(R.string.payment_dialog_alert),getResources().getString(R.string.payment_dialog_option));
                                }else{
                                    if (CommanMethod.isInternetConnected(PaymentActivity.this)){
                                        //userPayment();
                                        Intent intent = new Intent(PaymentActivity.this,MainActivityKotlin.class);
                                        intent.putExtra("amount",CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                        startActivity(intent);
                                    }
                                }
                            }else{
                                if(payment_type.equalsIgnoreCase("Payment Via offline link")){
                                    if (CommanMethod.isInternetConnected(PaymentActivity.this)){
                                       // userPayment();
                                        Intent intent = new Intent(PaymentActivity.this,MainActivityKotlin.class);
                                        intent.putExtra("amount",grand_price_text_view.getText().toString());
                                        startActivity(intent);
                                    }
                                }else if(payment_type.equals("VISA/MASTER")){
                                    sourceType = "src_card";
                                    //System.out.println("grand_total" +String.format("%.2f",grand_total));
                                    Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);
                                }else if(payment_type.equals("KNET")){
                                    sourceType = "src_kw.knet";
                                    Intent intent =new Intent(PaymentActivity.this,PaymentCommantActivity.class);
                                    intent.putExtra("sourceType",sourceType);
                                    intent.putExtra("user_id", sessionManager.getUserId());
                                    intent.putExtra("address_id",address_id);
                                    intent.putExtra("cart_id",cart_id);
                                    intent.putExtra("gift_id",giftId);
                                    intent.putExtra("payment_mode_id",paymentModeid);
                                    intent.putExtra("delivery_charge",shippingCharge);
                                    intent.putExtra("sub_total",sub_total);
                                    intent.putExtra("total_order_price",""+CommanMethod.getCountryWiseDecimalNumberFloatInput(PaymentActivity.this, grand_total));
                                    intent.putExtra("current_currency",sessionManager.getCurrencyCode());
                                    intent.putExtra("coupon_id",coupon_id);
                                    intent.putExtra("coupon_code",coupon_code);
                                    intent.putExtra("coupon_price",coupon_price);
                                    startActivity(intent);
                                }
                            }*/
                        }


                    } else {
                        CommanMethod.getCustomGOHome(PaymentActivity.this, getResources().getString(R.string.something_wrong));
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("current_currency", sessionManager.getCurrencyCode());
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    //////Online Payment Option Integration

    private void initiatePayment() {
        double invoiceAmount = Double.parseDouble("" + grand_total);
        MFInitiatePaymentRequest request = new MFInitiatePaymentRequest(
                invoiceAmount, MFCurrencyISO.KUWAIT_KWD);

        MFSDK.INSTANCE.initiatePayment(request, MFAPILanguage.EN,
                (MFResult<MFInitiatePaymentResponse> result) -> {
                    if (result instanceof MFResult.Success) {
                        Log.d("PAYMENT", "Response: " + new Gson().toJson(
                                ((MFResult.Success<MFInitiatePaymentResponse>) result).getResponse()));
                        setAvailablePayments((((MFResult.Success<MFInitiatePaymentResponse>) result)
                                .getResponse().getPaymentMethods()));
                    } else if (result instanceof MFResult.Fail) {
                        Log.d("PAYMENT", "Error: " + new Gson().toJson(((MFResult.Fail) result).getError()));
                    }


                    return Unit.INSTANCE;
                });
    }

    private void setAvailablePayments(ArrayList<PaymentMethod> paymentMethods) {
        paymentMethodsListFat = paymentMethods;
        /*GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        adapter = new MyItemRecyclerViewAdapter(paymentMethods, listener);
        rvPaymentMethod.setLayoutManager(layoutManager);
        rvPaymentMethod.setAdapter(adapter);
        rvPaymentMethod.addItemDecoration(new LayoutMarginDecoration(5, 10));*/
        /*for (int p = 0; p < paymentMethods.size(); p++) {
            PaymentModel paymentModel = new PaymentModel();
            paymentModel.setId("" + paymentMethods.get(p).getPaymentMethodId());//id
            paymentModel.setName(paymentMethods.get(p).getPaymentMethodEn());//name
            paymentModel.setName_ar(paymentMethods.get(p).getPaymentMethodAr());//rab_name
            paymentModel.setLogo(paymentMethods.get(p).getImageUrl());//logo
            paymentModel.setStatus("1");//status
            paymentModelList.add(paymentModel);
        }*/
        /*dialogPayment.dismiss();
        payment_recycler_view.setLayoutManager(new LinearLayoutManager(PaymentActivity.this, RecyclerView.VERTICAL, false));
        paymentAdapter = new PaymentAdapter(PaymentActivity.this, paymentModelList);
        payment_recycler_view.setAdapter(paymentAdapter);*/
    }

    private void showAlertDialog(String text) {
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void executePayment(Integer paymentMethod, String grandTotal) {
        double invoiceAmount = Double.parseDouble(grandTotal);
        MFExecutePaymentRequest request = new MFExecutePaymentRequest(paymentMethod, invoiceAmount);
        request.setDisplayCurrencyIso(MFCurrencyISO.KUWAIT_KWD);

        MFSDK.INSTANCE.executePayment(this, request, MFAPILanguage.EN,
                (String invoiceId, MFResult<MFGetPaymentStatusResponse> result) -> {
                    if (result instanceof MFResult.Success) {
                        Log.e("PAYMENT", "Response: " + new Gson().toJson(
                                ((MFResult.Success<MFGetPaymentStatusResponse>) result).getResponse()));

                        Log.e("PAYMENT", "invoiceId: " + invoiceId);
                        userPayment(invoiceId);

                    } else if (result instanceof MFResult.Fail) {
                        Log.e("PAYMENT", new Gson().toJson(((MFResult.Fail) result).getError()));
                        showAlertDialog("Payment failed");
                    }
                    dialogPayment.dismiss();
                    return Unit.INSTANCE;
                });
    }

    private void userPayment(final String tap_id) {
        dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "payment_by_tap", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        Utils.saveEventStatus("","DELIVERY_OPTION",PaymentActivity.this);
                        Toast.makeText(PaymentActivity.this, "Payment done successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PaymentActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    } else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                System.out.println("hello error " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", sessionManager.getUserId());
                params.put("address_id", address_id);
                params.put("cart_id", cart_id);
                params.put("gift_id", giftId);
                params.put("payment_mode_id", paymentModeid);
                params.put("delivery_charge", shippingCharge);
                params.put("sub_total", sub_total);
                params.put("total_order_price", "" + grand_total);
                params.put("current_currency", sessionManager.getCurrencyCode());
                params.put("tap_id", tap_id);
                if (!coupon_id.isEmpty() && !coupon_code.isEmpty() && !coupon_price.isEmpty()) {
                    params.put("coupon_price", coupon_price);
                    params.put("coupon_id", coupon_id);
                    params.put("coupon_code", coupon_code);
                }
                String isFreeChange = Utils.getEventStatus("","DELIVERY_OPTION",PaymentActivity.this);
                params.put("option", isFreeChange);
                return params;
            }
        };


        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 30000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 30000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

}
