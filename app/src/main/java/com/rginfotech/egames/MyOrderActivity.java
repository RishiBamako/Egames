package com.rginfotech.egames;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.adapter.MyOrderAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.model.MyOrderModel;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class MyOrderActivity extends AppCompatActivity implements View.OnClickListener {

   private SessionManager sessionManager;
   private LinearLayout liner,backLinlayoutId;
   private ImageView cart_image;
   private TextView number;
   private TextView order_count_text;
   private RecyclerView my_order_recycler;
   private GifImageView gifImageView;
   private TextView product_not_av;
   private ImageView back_image;
   private List<MyOrderModel> myOrderModelList;
   private MyOrderAdapter myOrderAdapter;
   private String total_count="";
   private int total_value;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        sessionManager = new SessionManager(this);
        liner = findViewById(R.id.liner);
        back_image = findViewById(R.id.back_image);
        backLinlayoutId = findViewById(R.id.backLinlayoutId);
        backLinlayoutId.setOnClickListener(this);
        findViewById(R.id.cart_image).setOnClickListener(this);
        number = findViewById(R.id.number);
        order_count_text = findViewById(R.id.order_count_text);
        my_order_recycler = findViewById(R.id.my_order_recycler);
        gifImageView = findViewById(R.id.gifImageView);
        product_not_av = findViewById(R.id.product_not_av);

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(this)){
            getOrderHistory();
            CommanClass.getCartValue(this, number);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backLinlayoutId:
                super.onBackPressed();
                break;
            case R.id.cart_image:
                Intent intent = new Intent(this,UserCartActivity.class);
                startActivity(intent);
                break;

        }
    }


  private void getOrderHistory(){
      final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
      dialog.setCancelable(true);
      dialog.show();
      //gifImageView.setVisibility(View.VISIBLE);
      RequestQueue mRequestQueue = Volley.newRequestQueue(this);
      StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"orderlist", new com.android.volley.Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              try {
                  dialog.dismiss();
                  //gifImageView.setVisibility(View.GONE);
                  myOrderModelList = new ArrayList<>();
                  JSONObject object = new JSONObject(response);
                  String status = object.getString("status");
                  if(status.equals("success")){
                      JSONObject jsonObject = new JSONObject(object.getString("response"));
                      JSONObject jsonObject1 = new JSONObject(jsonObject.getString("orderlist_Array"));
                      JSONArray jsonArray = new JSONArray(jsonObject1.getString("orderlist"));

                      order_count_text.setText(getResources().getString(R.string.my_orders)+" ("+jsonArray.length()+")");

                      if(jsonArray.length()>0){
                          product_not_av.setVisibility(View.GONE);

                          for(int i=0;i<jsonArray.length();i++){
                              JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                              MyOrderModel myOrderModel = new MyOrderModel();
                              myOrderModel.setId(jsonObject2.getString("id"));
                              myOrderModel.setInvoice_id(jsonObject2.getString("invoice_id"));
                              myOrderModel.setProduct_order_id(getResources().getString(R.string.order_id)+": "+jsonObject2.getString("product_order_id")+" ");
                              myOrderModel.setUser_id(jsonObject2.getString("user_id"));
                              myOrderModel.setPrice(jsonObject2.getString("price"));
                              myOrderModel.setDisplay_price(jsonObject2.getString("display_price"));
                              myOrderModel.setQuantity(jsonObject2.getString("quantity"));
                              myOrderModel.setQuantityLeft(jsonObject2.optString("quantity_left", "0"));
                              myOrderModel.setQuantityRight(jsonObject2.optString("quantity_right", "0"));
                              myOrderModel.setFree_quantity(jsonObject2.getString("free_quantity"));
                              myOrderModel.setGet_type(jsonObject2.getString("get_type"));
                              myOrderModel.setDiscount_type(jsonObject2.getString("discount_type"));
                              myOrderModel.setDiscount(jsonObject2.getString("discount"));
                              myOrderModel.setDisplay_discount(jsonObject2.getString("display_discount"));
                              myOrderModel.setOffer_id(jsonObject2.getString("offer_id"));
                              myOrderModel.setOffer_name(jsonObject2.getString("offer_name"));
                              myOrderModel.setOffer_name_ar(jsonObject2.getString("offer_name_ar"));
                              myOrderModel.setTotal(jsonObject2.getString("total"));
                              myOrderModel.setDisplay_total(jsonObject2.getString("display_total"));
                              myOrderModel.setShade(jsonObject2.getString("shade"));
                              myOrderModel.setLeft_eye_power(jsonObject2.getString("left_eye_power"));
                              myOrderModel.setRight_eye_power(jsonObject2.getString("right_eye_power"));
                              myOrderModel.setCoupon_code(jsonObject2.getString("coupon_code"));
                              myOrderModel.setCoupon_price(jsonObject2.getString("coupon_price"));
                              myOrderModel.setCoupon_id(jsonObject2.getString("coupon_id"));
                              myOrderModel.setTax_cost(jsonObject2.getString("tax_cost"));
                              myOrderModel.setAttribute_values(jsonObject2.getString("attribute_values"));
                              myOrderModel.setIs_email_send_to_seller(jsonObject2.getString("is_email_send_to_seller"));
                              myOrderModel.setEmail_sent_to_seller_count(jsonObject2.getString("email_sent_to_seller_count"));
                              myOrderModel.setEmail_sent_to_shopper_count(jsonObject2.getString("email_sent_to_shopper_count"));
                              myOrderModel.setOrder_shipping_date(jsonObject2.getString("order_shipping_date"));
                              myOrderModel.setEmail_sent_to_shopper_date(jsonObject2.getString("email_sent_to_shopper_date"));
                              myOrderModel.setOffer_discount_price(jsonObject2.getString("offer_discount_price"));
                              myOrderModel.setOffer_date(jsonObject2.getString("offer_date"));
                              myOrderModel.setIs_offer_email_send(jsonObject2.getString("is_offer_email_send"));
                              myOrderModel.setProductstatus(jsonObject2.getString("productstatus"));
                              myOrderModel.setShip_type(jsonObject2.getString("ship_type"));
                              myOrderModel.setShip_price(jsonObject2.getString("ship_price"));
                              myOrderModel.setShip_price_other(jsonObject2.getString("ship_price_other"));
                              myOrderModel.setShip_api_service_name(jsonObject2.getString("ship_api_service_name"));
                              myOrderModel.setShip_handle_price(jsonObject2.getString("ship_handle_price"));
                              myOrderModel.setShipp_api_service_type(jsonObject2.getString("shipp_api_service_type"));
                              myOrderModel.setSeller_pay_type(jsonObject2.getString("seller_pay_type"));
                              myOrderModel.setNote(jsonObject2.getString("note"));
                              myOrderModel.setCreated_at(" Date:"+jsonObject2.getString("created_at"));
                              myOrderModel.setUpdated_at(jsonObject2.getString("updated_at"));
                              myOrderModel.setProduct_id(jsonObject2.getString("product_id"));
                              myOrderModel.setCategory_id(jsonObject2.getString("category_id"));
                              myOrderModel.setProduct_name(jsonObject2.getString("product_name"));
                              myOrderModel.setProduct_name_ar(jsonObject2.getString("product_name_ar"));
                              myOrderModel.setOrder_status_code(jsonObject2.getString("order_status_code"));
                              myOrderModel.setShip_id(jsonObject2.getString("ship_id"));
                              myOrderModel.setProduct_image(API.ProductURL+jsonObject2.getString("product_image"));
                              myOrderModel.setCurrent_currency(jsonObject2.getString("current_currency"));
                              myOrderModel.setCurrent_currency_price(jsonObject2.getString("current_currency_price"));
                              myOrderModel.setLoyality_point(jsonObject2.getString("loyality_point"));
                              myOrderModel.setLoyality_point_price(jsonObject2.getString("loyality_point_price"));
                              myOrderModel.setEarn_loyality_point(jsonObject2.getString("earn_loyality_point"));
                              myOrderModel.setEarn_loyality_point_price(jsonObject2.getString("earn_loyality_point_price"));
                              myOrderModel.setGift_id(jsonObject2.getString("gift_id"));
                              myOrderModel.setGift_name(jsonObject2.getString("gift_name"));
                              myOrderModel.setGift_name_ar(jsonObject2.getString("gift_name_ar"));
                              myOrderModel.setEarn_loyality_point_status(jsonObject2.getString("earn_loyality_point_status"));
                              myOrderModel.setPayment_status(jsonObject2.getString("payment_status"));
                              myOrderModel.setOrder_status(jsonObject2.getString("order_status"));
                              myOrderModel.setPayment_mode_id(jsonObject2.getString("payment_mode_id"));
                              myOrderModel.setPayment_mode_name(jsonObject2.getString("payment_mode_name"));
                              myOrderModel.setPayment_mode_name_ar(jsonObject2.getString("payment_mode_name_ar"));
                              myOrderModel.setFull_name(jsonObject2.getString("full_name"));
                              myOrderModel.setArea(jsonObject2.getString("area"));
                              myOrderModel.setBlock(jsonObject2.getString("block"));
                              myOrderModel.setStreet(jsonObject2.getString("street"));
                              myOrderModel.setAvenue(jsonObject2.getString("avenue"));
                              myOrderModel.setHouse_no(jsonObject2.getString("house_no"));
                              myOrderModel.setFloor_no(jsonObject2.getString("floor_no"));
                              myOrderModel.setFlat_no(jsonObject2.getString("flat_no"));
                              myOrderModel.setPhone_no(jsonObject2.getString("phone_no"));
                              myOrderModel.setComments(jsonObject2.getString("comments"));
                              myOrderModel.setLatitude(jsonObject2.getString("latitude"));
                              myOrderModel.setLongitude(jsonObject2.getString("longitude"));
                              myOrderModel.setCurrrent_location(jsonObject2.getString("currrent_location"));
                              myOrderModel.setTotal_order_price(jsonObject2.getString("total_order_price"));
                              myOrderModel.setDelivery_charge(jsonObject2.getString("delivery_charge"));
                              myOrderModel.setFeedbackStatus(jsonObject2.getString("feedback_status"));
                              myOrderModel.setOrderTrackingId(jsonObject2.optString("order_tracking_id", ""));
                              myOrderModelList.add(myOrderModel);
                          }
                      }
                      else{
                          product_not_av.setVisibility(View.VISIBLE);

                      }

                      //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_success), Toast.LENGTH_SHORT).show();
                  }else{
                      product_not_av.setVisibility(View.VISIBLE);
                      //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_fail)+message, Toast.LENGTH_SHORT).show();
                  }
              }catch (JSONException e){
                  e.printStackTrace();
                  dialog.dismiss();
                  //gifImageView.setVisibility(View.GONE);
              }

              //GridLayoutManager gridLayoutManager = new GridLayoutManager(MyOrderActivity.this,1);
              my_order_recycler.setLayoutManager(new LinearLayoutManager(MyOrderActivity.this, RecyclerView.VERTICAL, false));
              myOrderAdapter = new MyOrderAdapter(MyOrderActivity.this,myOrderModelList);
              my_order_recycler.setAdapter(myOrderAdapter);


          }
      }, new com.android.volley.Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              //gifImageView.setVisibility(View.GONE);
              dialog.dismiss();
          }
      }){
          @Override
          protected Map<String, String> getParams()
          {
              Map<String, String>  params = new HashMap<String, String>();
              params.put("user_id",sessionManager.getUserId());
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

    public void getCartValue(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));
                        if(jsonArray.length()> 0) {
                            total_value = jsonArray.length();
                            total_count = String.valueOf(total_value);
                            number.setText(total_count);
                        }else {
                            number.setText("");
                        }
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, error -> {
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                }else{
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

}
