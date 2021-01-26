package com.rginfotech.egames;

import androidx.annotation.RequiresApi;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.adapter.GiftAdapter;
import com.rginfotech.egames.adapter.UserBillingAddressAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.interfacelenzzo.GiftInterface;
import com.rginfotech.egames.interfacelenzzo.UserAddressInterface;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.model.GiftModel;
import com.rginfotech.egames.model.UserBillingAddressModel;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckOutActivity extends BaseActivity implements View.OnClickListener, UserAddressInterface, GiftInterface {

    private UserBillingAddressModel userBillingAddressModel;
    private List<UserBillingAddressModel> userBillingAddressModelList;
    private UserBillingAddressAdapter userBillingAddressAdapter;
    private RecyclerView address_recycler_view;
    private SessionManager sessionManager;
    private String sub_total;
    private String current_currency;
    private TextView total_price_text_view;
    private String addressId="";
    private List<GiftModel> giftModelList;
    private GiftAdapter giftAdapter;
    private Dialog dialog;
    private RecyclerView gift_recycler_view;
    private TextView close_dialog;
    private Button btn_done;
    private Button btn_skip;
    private String giftId="";
    private String cart_id;
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
    private String coupon_id="";
    private String coupon_code="";
    private String coupon_price="";
    private ImageView back_image,home_back;
    private LinearLayout liner,relativeLayout,liner_layout1,liner_layout2;
    boolean isThisFromAccount = false;
    TextView title_text_view;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        sessionManager = new SessionManager(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.home_back).setOnClickListener(this);
        findViewById(R.id.relativeLayout1).setOnClickListener(this);
        findViewById(R.id.liner_layout2).setOnClickListener(this);
        address_recycler_view = (RecyclerView)findViewById(R.id.address_recycler_view);
        total_price_text_view = (TextView)findViewById(R.id.total_price_text_view);
        relativeLayout = findViewById(R.id.relativeLayout);
        liner_layout1 = findViewById(R.id.liner_layout1);
        liner_layout2 = findViewById(R.id.liner_layout2);
        back_image = (ImageView)findViewById(R.id.back_image);
        home_back = (ImageView)findViewById(R.id.home_back);
        title_text_view = findViewById(R.id.title_text_view);
        /*liner = (LinearLayout)findViewById(R.id.liner);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }*/

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);

        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
                    }
        /*if (CommanMethod.isInternetConnected(CheckOutActivity.this)){
            getUserShippingAddress();
        }*/

        if(getIntent().hasExtra("MYACCOUNT")){
            relativeLayout.setVisibility(View.GONE);
            liner_layout1.setVisibility(View.GONE);
            liner_layout2.setVisibility(View.GONE);
            home_back.setVisibility(View.GONE);
            isThisFromAccount = true;
            title_text_view.setText(getResources().getString(R.string.edit_devivery_address));
        }
        else{
            title_text_view.setText(getResources().getString(R.string.check_out_title));
            relativeLayout.setVisibility(View.VISIBLE);
            liner_layout1.setVisibility(View.VISIBLE);
            liner_layout2.setVisibility(View.VISIBLE);
            home_back.setVisibility(View.VISIBLE);
            isThisFromAccount = false;

            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                sub_total = bundle.getString("sub_total");
                current_currency = bundle.getString("current_currency");
                cart_id = bundle.getString("cart_id");
                total_price_text_view.setText(sub_total+" "+current_currency);
                coupon_id = bundle.getString("coupon_id");
                coupon_code = bundle.getString("coupon_code");
                coupon_price = bundle.getString("coupon_price");
            }
        }




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                 super.onBackPressed();
                break;
            case R.id.home_back:
                Intent intent = new Intent(CheckOutActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            case R.id.relativeLayout1:
                Intent intent1 = new Intent(CheckOutActivity.this,AddNewAddressActivity.class);
                startActivity(intent1);
                break;
            case R.id.liner_layout2:
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    if(addressId.equals("") || addressId.length()==0){
                        CommanMethod.getCustomOkAlert(this, this.getString(R.string.select_any_one_address));
                    }else {
                        if(CommanMethod.isInternetConnected(this)){
                           // getGiftList();
                           continueToPayment();
                        }

                    }
                }else {
                    Intent intent2 = new Intent(CheckOutActivity.this,LoginActivity.class);
                    intent2.putExtra("from", "checkout");
                    startActivity(intent2);

                }
                break;
        }
    }

    /*private void dialogBox(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.success_dialog_box);
        TextView alert_text = (TextView)dialog.findViewById(R.id.item_text_view);
        alert_text.setText(this.getString(R.string.alert_message));
        TextView item_text_view1 = (TextView)dialog.findViewById(R.id.item_text_view1);
        item_text_view1.setText(this.getString(R.string.select_any_one_address));
        LinearLayout layout = (LinearLayout)dialog.findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }*/

    private void giftDialogBox(){
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.gift_dialog_screen);
        gift_recycler_view = (RecyclerView)dialog.findViewById(R.id.gift_recycler_view);
        close_dialog = (TextView)dialog.findViewById(R.id.close_dialog);
        btn_done = (Button)dialog.findViewById(R.id.btn_done);
        btn_skip = (Button)dialog.findViewById(R.id.btn_skip);
        TextView text_dialog = dialog.findViewById(R.id.text_dialog);
        TextView step_text = dialog.findViewById(R.id.step_text);
        if(this.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            text_dialog.setGravity(Gravity.START);
            step_text.setGravity(Gravity.END);
        }else{
            text_dialog.setGravity(Gravity.END);
            step_text.setGravity(Gravity.START);
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(CheckOutActivity.this,1);
        gift_recycler_view.setLayoutManager(gridLayoutManager);
        giftAdapter = new GiftAdapter(CheckOutActivity.this,giftModelList);
        gift_recycler_view.setAdapter(giftAdapter);


        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(CheckOutActivity.this,PaymentActivity.class);
                intent.putExtra("cart_id",cart_id);
                intent.putExtra("giftId",giftId);
                intent.putExtra("sub_total",sub_total);
                intent.putExtra("address_id",addressId);
                intent.putExtra("name",name);
                intent.putExtra("area",area);
                intent.putExtra("block",block);
                intent.putExtra("street",street);
                intent.putExtra("avenue",avenue);
                intent.putExtra("house",house);
                intent.putExtra("floor",floor);
                intent.putExtra("flat",flat);
                intent.putExtra("phone",phone);
                intent.putExtra("comments",comments);
                intent.putExtra("coupon_id",coupon_id);
                intent.putExtra("coupon_code",coupon_code);
                intent.putExtra("coupon_price",coupon_price);
                intent.putExtra("total", getIntent().getStringExtra("total"));
                intent.putExtra("loyality_point", getIntent().getStringExtra("loyality_point"));
                startActivity(intent);

            }
        });
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(CheckOutActivity.this,PaymentActivity.class);
                intent.putExtra("cart_id",cart_id);
                intent.putExtra("giftId",giftId);
                intent.putExtra("sub_total",sub_total);
                intent.putExtra("address_id",addressId);
                intent.putExtra("name",name);
                intent.putExtra("area",area);
                intent.putExtra("block",block);
                intent.putExtra("street",street);
                intent.putExtra("avenue",avenue);
                intent.putExtra("house",house);
                intent.putExtra("floor",floor);
                intent.putExtra("flat",flat);
                intent.putExtra("phone",phone);
                intent.putExtra("comments",comments);
                intent.putExtra("coupon_id",coupon_id);
                intent.putExtra("coupon_code",coupon_code);
                intent.putExtra("coupon_price",coupon_price);
                intent.putExtra("total", getIntent().getStringExtra("total"));
                intent.putExtra("loyality_point", getIntent().getStringExtra("loyality_point"));
                startActivity(intent);
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void getUserShippingAddress(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"user_billing_address", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    userBillingAddressModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("user_billing_address_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("wishlist"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            UserBillingAddressModel userBillingAddressModel = new UserBillingAddressModel();
                            userBillingAddressModel.setId(jsonObject2.getString("id"));
                            userBillingAddressModel.setFull_name(jsonObject2.getString("full_name"));
                            userBillingAddressModel.setArea(jsonObject2.getString("area"));
                            userBillingAddressModel.setBlock(jsonObject2.getString("block"));
                            userBillingAddressModel.setStreet(jsonObject2.getString("street"));
                            userBillingAddressModel.setAvenue(jsonObject2.getString("avenue"));
                            userBillingAddressModel.setHouse_no(jsonObject2.getString("house_no"));
                            userBillingAddressModel.setFloor_no(jsonObject2.getString("floor_no"));
                            userBillingAddressModel.setFlat_no(jsonObject2.getString("flat_no"));
                            userBillingAddressModel.setPhone_no(jsonObject2.getString("phone_no"));
                            userBillingAddressModel.setComments(jsonObject2.getString("comments"));
                            userBillingAddressModel.setCurrrent_location(jsonObject2.getString("currrent_location"));
                            userBillingAddressModel.setLatitude(jsonObject2.getString("latitude"));
                            userBillingAddressModel.setLongitude(jsonObject2.getString("longitude"));
                            userBillingAddressModel.setUser_id(jsonObject2.getString("user_id"));
                            userBillingAddressModel.setPaci_number(jsonObject2.getString("paci_number"));
                            userBillingAddressModel.setAddress_type(jsonObject2.getString("address_type"));
                            userBillingAddressModel.setSelected(false);
                            userBillingAddressModelList.add(userBillingAddressModel);
                        }

                    }else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

                //GridLayoutManager gridLayoutManager = new GridLayoutManager(CheckOutActivity.this,1);
                address_recycler_view.setLayoutManager(new LinearLayoutManager(CheckOutActivity.this, RecyclerView.VERTICAL, false));
                userBillingAddressAdapter = new UserBillingAddressAdapter(CheckOutActivity.this,userBillingAddressModelList,isThisFromAccount);
                address_recycler_view.setAdapter(userBillingAddressAdapter);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
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

    private void getGiftList(){
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"giftlist", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    giftModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONArray jsonArray = new JSONArray(object.getString("result"));
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            GiftModel giftModel = new GiftModel();
                            giftModel.setId(jsonObject.getString("id"));
                            //giftModel.setTitle(jsonObject.getString("title"));
                            giftModel.setImage(API.GiftsURL+jsonObject.getString("image"));
                            giftModel.setStatus(jsonObject.getString("status"));

                            if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                                giftModel.setTitle(jsonObject.getString("title"));
                                giftModel.setDescription(jsonObject.getString("description"));
                            }else{
                                giftModel.setTitle(jsonObject.getString("title_ar"));
                                giftModel.setDescription(jsonObject.getString("description_ar"));
                            }

                            giftModelList.add(giftModel);
                        }

                        giftDialogBox();

                    }else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        });
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

    @Override
    public void onResume(){
        super.onResume();

        if (CommanMethod.isInternetConnected(CheckOutActivity.this)){
            getUserShippingAddress();
        }
    }

    @Override
    public void getAddressId(String addressId,String name,String area,String block,String street,String avenue,String house,String floor,String flat,String phone,String comments) {
        this.addressId=addressId;
        this.name=name;
        this.area=area;
        this.block=block;
        this.street=street;
        this.avenue=avenue;
        this.house=house;
        this.floor=floor;
        this.flat=flat;
        this.phone=phone;
        this.comments=comments;
    }


    @Override
    public void getGiftId(String giftId) {
        this.giftId = giftId;
    }


    public void continueToPayment() {
        Intent intent = new Intent(CheckOutActivity.this,PaymentActivity.class);
        intent.putExtra("cart_id",cart_id);
        intent.putExtra("giftId",giftId);
        intent.putExtra("sub_total",sub_total);
        intent.putExtra("address_id",addressId);
        intent.putExtra("name",name);
        intent.putExtra("area",area);
        intent.putExtra("block",block);
        intent.putExtra("street",street);
        intent.putExtra("avenue",avenue);
        intent.putExtra("house",house);
        intent.putExtra("floor",floor);
        intent.putExtra("flat",flat);
        intent.putExtra("phone",phone);
        intent.putExtra("comments",comments);
        intent.putExtra("coupon_id",coupon_id);
        intent.putExtra("coupon_code",coupon_code);
        intent.putExtra("coupon_price",coupon_price);
        intent.putExtra("current_currency",current_currency);
        intent.putExtra("total", getIntent().getStringExtra("total"));
        intent.putExtra("loyality_point", getIntent().getStringExtra("loyality_point"));
        startActivity(intent);
    }
}
