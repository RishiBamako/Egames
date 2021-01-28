package com.rginfotech.egames.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.api.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommanClass {

    public static TextView badgeTextView;
    private Context context;
    private int total_value;
    private String total_count = "";

    public CommanClass(Context context) {
        this.context = context;

    }

    public static void getCartValue(final Context context, TextView textView) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));
                        String totalcount = jsonObject1.optString("totalcount", "");
                        if (totalcount.equals("0")) {
                            if (textView != null)
                                textView.setText("");
                        } else {
                            if (textView != null)
                                textView.setText(totalcount);
                        }

                    } else {
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(context);
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
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

    public static void getCartValue_home(final Context context, BottomNavigationView navView,View view) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));
                        String totalcount = jsonObject1.optString("totalcount", "");
                        if (totalcount.equals("0")) {
                            if (badgeTextView != null && Utils.isFromDelete) {
                                Utils.isFromDelete = false;
                                View cart_badge = LayoutInflater.from(context)
                                        .inflate(R.layout.cart_count_round_layout, navView, false);

                                BottomNavigationMenuView mbottomNavigationMenuView = (BottomNavigationMenuView) navView.getChildAt(0);
                                View view = mbottomNavigationMenuView.getChildAt(2);
                                BottomNavigationItemView itemView = (BottomNavigationItemView) view;
                                itemView.removeAllViews();
                                itemView.addView(cart_badge);
                            }
                        } else {
                            if (badgeTextView != null) {
                                badgeTextView.setText(totalcount);
                                badgeTextView.setVisibility(View.VISIBLE);
                            }
                        }

                    } else {
                        if (badgeTextView != null) {
                            badgeTextView.setText("");
                            badgeTextView.setVisibility(View.GONE);
                        }
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(context);
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
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

    public static void addCustomView(Context context, BottomNavigationView navView, TextView cartCountTextView) {
        BottomNavigationMenuView mbottomNavigationMenuView = (BottomNavigationMenuView) navView.getChildAt(0);
        View view = mbottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) view;
        View cart_badge = LayoutInflater.from(context)
                .inflate(R.layout.cart_count_round_layout, navView, false);
        badgeTextView = cart_badge.findViewById(R.id.notificationsBadge);
        CommanClass.getCartValue_home(context, navView,cart_badge);
        itemView.addView(cart_badge);
    }

    public static String productType(Context context, String product_condition) {
        if (product_condition.equals("1")) {
            return context.getResources().getString(R.string.new_status);
        } else {
            return context.getResources().getString(R.string.preowned_status);
        }
    }
}
