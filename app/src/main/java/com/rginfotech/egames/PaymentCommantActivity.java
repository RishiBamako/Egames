package com.rginfotech.egames;

import androidx.annotation.RequiresApi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;

public class PaymentCommantActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = PaymentCommantActivity.class.getSimpleName();
    private SessionManager sessionManager;
    //private GifImageView gifImageView;
    private WebView webView;
    private String sourceType;
    private String user_id;
    private String address_id;
    private String cart_id;
    private String gift_id;
    private String payment_mode_id;
    private String delivery_charge;
    private String sub_total;
    private String total_order_price;
    private String current_currency;
    private String transaction_url ="";
    private String coupon_id="";
    private String coupon_code="";
    private String coupon_price="";
    private ImageView back_image;

    private Dialog dialog;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_commant);
        sessionManager = new SessionManager(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        dialog = CommanMethod.getCustomProgressDialog(this);
        back_image = (ImageView)findViewById(R.id.back_image);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
        }
        webView = (WebView)findViewById(R.id.webView);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            sourceType = bundle.getString("sourceType");
            user_id = bundle.getString("user_id");
            address_id = bundle.getString("address_id");
            cart_id = bundle.getString("cart_id");
            gift_id = bundle.getString("gift_id");
            payment_mode_id = bundle.getString("payment_mode_id");
            coupon_code = bundle.getString("coupon_code");
            delivery_charge = bundle.getString("delivery_charge");
            sub_total = bundle.getString("sub_total");
            total_order_price = bundle.getString("total_order_price");
            current_currency = bundle.getString("current_currency");
            coupon_id = bundle.getString("coupon_id");
            coupon_code = bundle.getString("coupon_code");
            coupon_price = bundle.getString("coupon_price");
        }

        try {
            if (CommanMethod.isInternetConnected(PaymentCommantActivity.this)){
                //userPayment();
                paymentRequest();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        dialog.show();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                if(url.contains("payment_by_tap/response?tap_id=")){
                    System.out.println("payment_by_tap shouldOverrideUrlLoading"+url +"sub string "+url.substring(url.indexOf("=")) +" last index "+url.substring(url.lastIndexOf("=")));
                    if (CommanMethod.isInternetConnected(PaymentCommantActivity.this)){
                        userPayment(url.substring(url.indexOf("=")));
                    }
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                dialog.dismiss();
                System.out.println("hello onPageFinished");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                dialog.dismiss();
                System.out.println("hello onReceivedError");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
        }
    }

    private void userPayment(final String tap_id){

        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"payment_by_tap", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        Intent intent =new Intent(PaymentCommantActivity.this,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                System.out.println("hello error "+error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("address_id",address_id);
                params.put("cart_id",cart_id);
                params.put("gift_id",gift_id);
                params.put("payment_mode_id",payment_mode_id);
                params.put("coupon_code","");
                params.put("delivery_charge",delivery_charge);
                params.put("sub_total",sub_total);
                params.put("total_order_price", total_order_price);
                params.put("current_currency",current_currency);
                params.put("tap_id",tap_id);
                if(!coupon_id.isEmpty() && !coupon_code.isEmpty() && !coupon_price.isEmpty()){
                    params.put("coupon_price",coupon_price);
                    params.put("coupon_id",coupon_id);
                    params.put("coupon_code",coupon_code);
                }
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


    private void paymentRequest() throws JSONException {

        final JSONObject obj = new JSONObject();
        obj.put("email",true);
        obj.put("sms",false);

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("first_name",sessionManager.getUserName());
        jsonObject.put("email",sessionManager.getUserEmail());

        final JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id",sourceType);

        final JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("url",API.HOME_URL+"mobile/ayment_by_tap/post");

        final JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("url",API.HOME_URL+"mobile/payment_by_tap/response");

        JSONObject  params1 = new JSONObject();
        params1.put("amount", total_order_price);
        params1.put("currency",current_currency);
        params1.put("threeDSecure","true");
        params1.put("save_card","false");
        params1.put("description","EGames Payment");
        params1.put("statement_descriptor","EGames");
        params1.put("receipt",obj);
        params1.put("customer",jsonObject);
        params1.put("source",jsonObject1);
        params1.put("post",jsonObject2);
        params1.put("redirect",jsonObject3);

        final OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        //String requestJson = "{\"amount\":1.00,\"currency\":\"KWD\",\"threeDSecure\":true,\"save_card\":false,\"description\":\"Test Description\",\"statement_descriptor\":\"Sample\",\"metadata\":{\"udf1\":\"test 1\",\"udf2\":\"test 2\"},\"reference\":{\"transaction\":\"txn_0001\",\"order\":\"ord_0001\"},\"receipt\":{\"email\":false,\"sms\":true},\"customer\":{\"first_name\":\"test\",\"middle_name\":\"test\",\"last_name\":\"test\",\"email\":\"test@test.com\",\"phone\":{\"country_code\":\"965\",\"number\":\"50000000\"}},\"source\":{\"id\":\"src_kw.knet\"},\"post\":{\"url\":\"http://your_website.com/post_url\"},\"redirect\":{\"url\":\"http://your_website.com/redirect_url\"}}";
        RequestBody body = RequestBody.create(mediaType,params1.toString());
        final okhttp3.Request request = new okhttp3.Request.Builder().url("https://api.tap.company/v2/charges").post(body).addHeader("authorization", "Bearer sk_live_srmq974IH51KQEYABvhCwMoF").addHeader("content-type", "application/json").build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Request Failed.");
                    dialog.dismiss();
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if(!response.isSuccessful()){
                        dialog.dismiss();
                        throw new IOException("Error : " + response);

                    }else {
                        Log.d(TAG,"Request Successful.");
                    }
                    String jsonData = response.body().string();
                    try {
                        JSONObject jsonObject4 = new JSONObject(jsonData);
                        JSONObject transaction = new JSONObject(jsonObject4.getString("transaction"));
                        transaction_url = transaction.getString("url");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                    }

                    PaymentCommantActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl(transaction_url);
                        }
                    });
                }
            });
    }
}
