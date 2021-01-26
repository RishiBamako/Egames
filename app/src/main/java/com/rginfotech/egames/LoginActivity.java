package com.rginfotech.egames;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.api.APIService;
import com.rginfotech.egames.customviews.CustomTextViewMedium;
import com.rginfotech.egames.interfacelenzzo.SortByInterface;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.model.ProductList;
import com.rginfotech.egames.model.ProductSearchModel;
import com.rginfotech.egames.model.SortByModel;
import com.rginfotech.egames.model.UserLogin;
import com.rginfotech.egames.pushnotification.FirebaseIDService;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;
import com.rginfotech.egames.utility.SortFilterSessionManager;
import com.rginfotech.egames.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity implements View.OnClickListener, SortByInterface {

    TextView product_not_av;
    private EditText email_edittext;
    private EditText password_edittext;
    private ImageView back_image;
    private LinearLayout backLinLayoutId;
    //private TextView forget_pass_textview;
    //private Button create_account;
    //private Button login_button;
    private String getemail;
    private String getpassword;
    private SessionManager sessionManager;
    //private GifImageView gifImageView;
    private LinearLayout liner;
    //private ImageView cart_image;
    private TextView number;
    private int total_value;
    private String total_count = "";
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text = "", user_type = "";
    //private ImageView search_image;
    private AutoCompleteTextView searchView;
    private Dialog dialog1;
    private List<SortByModel> sortByModelList;
    private SortFilterSessionManager sortFilterSessionManager;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(LoginActivity.this);
        sortFilterSessionManager = new SortFilterSessionManager(LoginActivity.this);

        back_image = (ImageView) findViewById(R.id.back_image);
        backLinLayoutId = (LinearLayout) findViewById(R.id.backLinLayoutId);
        backLinLayoutId.setOnClickListener(this);
        findViewById(R.id.back_image).setOnClickListener(this);
        //forget_pass_textview = (TextView)findViewById(R.id.forget_pass_textview);
        findViewById(R.id.forget_pass_textview).setOnClickListener(this);
        //create_account = (Button) findViewById(R.id.create_account);
        findViewById(R.id.create_account).setOnClickListener(this);
        //login_button = (Button)findViewById(R.id.login_button);
        findViewById(R.id.login_button).setOnClickListener(this);
        email_edittext = (EditText) findViewById(R.id.dob_edittext);
        password_edittext = (EditText) findViewById(R.id.password_edittext);
        //gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        //cart_image = (ImageView)findViewById(R.id.cart_image);
        if (getIntent().getStringExtra("from").equals("cart")) {
            findViewById(R.id.user_type_rg).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.user_type_rg).setVisibility(View.GONE);
        }

        if (sessionManager.getUserType().equals("Guest")) {
            email_edittext.setText(sessionManager.getUserEmail());
        }

        ((RadioButton) findViewById(R.id.existing_user_rb)).setChecked(true);
        user_type = "user";
        ((RadioGroup) findViewById(R.id.user_type_rg)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.existing_user_rb) {
                    password_edittext.setVisibility(View.VISIBLE);
                    user_type = "user";
                    ((Button) findViewById(R.id.login_button)).setText(getResources().getString(R.string.forgot_login));
                } else if (i == R.id.guest_user_rb) {
                    password_edittext.setVisibility(View.GONE);
                    user_type = "guest";
                    ((Button) findViewById(R.id.login_button)).setText(getResources().getString(R.string.login_as_guest));
                }
            }
        });
        findViewById(R.id.cart_image).setOnClickListener(this);
        number = (TextView) findViewById(R.id.number);
        findViewById(R.id.search_image).setOnClickListener(this);
        findViewById(R.id.filter_image).setOnClickListener(this);

        liner = (LinearLayout) findViewById(R.id.liner);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
            email_edittext.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_black_20, 0, 0, 0);
            password_edittext.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock_20, 0, 0, 0);

            ((CustomTextViewMedium) findViewById(R.id.forget_pass_textview)).setGravity(Gravity.END);
        } else {
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
            email_edittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.user_black_20, 0);
            password_edittext.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.lock_20, 0);
            ((CustomTextViewMedium) findViewById(R.id.forget_pass_textview)).setGravity(Gravity.START);
        }

        if (CommanMethod.isInternetConnected(LoginActivity.this)) {
            CommanClass.getCartValue(this, number);
        }
        getSortBy();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLinLayoutId:
                super.onBackPressed();
                break;
            case R.id.forget_pass_textview:
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.create_account:

                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                intent1.putExtra("from", "login");
                startActivity(intent1);

                break;
            case R.id.login_button:

                if (user_type.equals("user")) {
                    validation();
                } else {
                    guestUser();
                    //CommanMethod.getCustomOkAlert(LoginActivity.this, "Comming soon!");
                }
                break;
            case R.id.cart_image:
                Intent intent11 = new Intent(LoginActivity.this, UserCartActivity.class);
                startActivity(intent11);
                break;
            case R.id.search_image:
                searchsortDialog();
                break;
            case R.id.filter_image:
                Intent intent12 = new Intent(LoginActivity.this, FilterActivity.class);
                startActivity(intent12);
                break;

            case R.id.layout1:
                sortDialog();
                break;
            case R.id.layout:
                intent1 = new Intent(LoginActivity.this, FilterActivity.class);
                intent1.putExtra("from", "list_fragment");
                startActivity(intent1);
                break;
        }
    }

    private void guestUser() {
        getemail = email_edittext.getText().toString();
        if (!CommanMethod.isEmailValid(getemail)) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_login_email));
            email_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_login_email),Toast.LENGTH_SHORT).show();
        } else {
            if (CommanMethod.isInternetConnected(LoginActivity.this)) {
                guestLogin();
            }
        }
    }

    private void guestLogin() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "signup_guest", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                   /*{"id":"139","result":{"id":"139","username":null,"name":null,
                   "password":"bfce0fb3e101b36f6faa196d9ac94e5e","email":"abcd@gmail.com",
                   "country_code":"+965","phone":null,"gender":null,"activation_code":null,"created_at":null,
                   "updated_at":null,"active":"1","status":"1","permission":null,"useracessid":null,
                   "last_login":null,"forgotten_password_code":null,"parent_id":"0","added_by":null,
                   "profilephoto":null,"user_type":"1","device_id":null,"dob":null,"loyality_point":"0",
                   "signup_type":"Guest"},
                   "status":"success","message":"User added successfully"}*/
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("result"));
                        sessionManager.setUserId(jsonObject.getString("id"));
                        sessionManager.setUserName(jsonObject.getString("name"));
                        sessionManager.setUserEmail(getemail);
                        sessionManager.setUserPassword("12345");
                        sessionManager.setUserType(jsonObject.getString("signup_type"));
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        /*Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);*/
                        //CommanMethod.getCustomOkAlert(LoginActivity.this, "Comming soon!");
                    } else if (status.equals("failed")) {
                        String message = CommanMethod.getMessage(LoginActivity.this, object);
                        CommanMethod.getCustomOkAlert(LoginActivity.this, message);
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                //gifImageView.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                /*if(FirebaseInstanceId.getInstance().getToken() != null) {
                    params.put("device_id", FirebaseInstanceId.getInstance().getToken());
                }*/
                if (OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId() != null) {
                    params.put("device_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                }
                params.put("email", getemail);
                params.put("country_code", sessionManager.getCountryCode());
                params.put("guestid", sessionManager.getRandomValue());
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

    private void validation() {
        getemail = email_edittext.getText().toString();
        getpassword = password_edittext.getText().toString();

        if (TextUtils.isEmpty(getemail)) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_login_email));
            //dob_edittext.setError(this.getString(R.string.toast_message_login_email));
            email_edittext.requestFocus();
            //Toast.makeText(this,this.getString(R.string.toast_message_login_email),Toast.LENGTH_SHORT).show();
        } else if (!CommanMethod.isValidEmail(getemail)) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_login_invalid));
            //dob_edittext.setError(this.getString(R.string.toast_message_login_invalid));
            email_edittext.requestFocus();
            //Toast.makeText(this, this.getString(R.string.toast_message_login_invalid), Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(getpassword)) {
            CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_login_pass));
            //password_edittext.setError(this.getString(R.string.toast_message_login_pass));
            password_edittext.requestFocus();
            //Toast.makeText(this, this.getString(R.string.toast_message_login_pass), Toast.LENGTH_SHORT).show();
        } else {
            if (CommanMethod.isInternetConnected(LoginActivity.this)) {
                userLogin();
            }
        }
    }

    private void userLogin() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "login", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //{"status":"success","response":{"id":"106","username":"ajay@gmail.com","name":"ajay",
                    // "password":"fcea920f7412b5da7be0cf42b8c93759","email":"ajay@gmail.com",
                    // "country_code":"+965","phone":"8800985790","gender":"Male",
                    // "activation_code":"715417","created_at":null,"updated_at":"2019-12-09 09:46:19",
                    // "active":"1","status":"1","permission":null,"useracessid":null,"last_login":null,
                    // "forgotten_password_code":null,"parent_id":"0","added_by":null,
                    // "profilephoto":"res_1576151280.jpg","user_type":"1",
                    // "device_id":"cBs23iskTiQ:APA91bFsJzqLEIZcejmOJj870c58oaZeNRaOMf1dOcHJmNf4pyp0QRA2x16OzyLf4a4ZIm0hg3GrsFuWNsNwDNcr_e3M1qpH0faY23ILm-kCPLDvbUF8CZEjsb5gmP6ZLyDY66oAs-1X",
                    // "dob":null,"loyality_point":"300","signup_type":null,
                    // "image_path":"http:\/\/www.lenzzo.com\/uploads\/users\/res_1576151280.jpg"}}
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        sessionManager.setUserId(jsonObject.getString("id"));
                        sessionManager.setUserName(jsonObject.getString("name"));
                        sessionManager.setUserEmail(getemail);
                        sessionManager.setUserPassword(getpassword);
                        sessionManager.setUserType("user");

                        /*Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        setResult(Activity.RESULT_OK, intent);
                        finish();*/
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (status.equals("failed")) {
                        String message = CommanMethod.getMessage(LoginActivity.this, object);
                        //CommanMethod.getCustomOkAlert(LoginActivity.this, getResources().getString(R.string.login_error));
                        CommanMethod.getCustomOkAlert(LoginActivity.this, message);
                        //{"status":"failed","message":"Your account is not verified. So please check your email and click verification link"}
                        /*if(Locale.getDefault().getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)){
                            CommanMethod.getCustomOkAlert(LoginActivity.this, getResources().getString(R.string.login_error));
                        }else{
                            CommanMethod.getCustomOkAlert(LoginActivity.this, object.getString("message_ar"));
                        }*/
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                //gifImageView.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if(!TextUtils.isEmpty(FirebaseInstanceId.getInstance().getToken())) {
                    params.put("device_id", FirebaseInstanceId.getInstance().getToken());
                }
                if (OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId() != null) {
                    params.put("device_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                }
               // Log.e("TOKEN_IS", FirebaseInstanceId.getInstance().getToken());
                params.put("email", getemail);
                params.put("password", getpassword);
                params.put("guestid", sessionManager.getRandomValue());
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

    private void userLogin1() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        APIService service = retrofit.create(APIService.class);
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail(getemail);
        userLogin.setPassword(getpassword);

        Call<UserLogin> call = service.loginUser(userLogin.getEmail(), userLogin.getPassword());
        call.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                UserLogin userLogin1 = response.body();
                System.out.println("hello " + userLogin1.getName());
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                //Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getCartValue() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
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
                        if (jsonArray.length() > 0) {
                            total_value = jsonArray.length();
                            total_count = String.valueOf(total_value);
                            number.setText(total_count);
                        } else {
                            number.setText("");
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


    @Override
    public void onResume() {
        super.onResume();
        if (CommanMethod.isInternetConnected(LoginActivity.this)) {
            CommanClass.getCartValue(this, number);
        }
    }

    /////search wala

    private void searchsortDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.search_dialog_box);
        ImageView dialog_back_image = (ImageView) dialog.findViewById(R.id.dialog_back_image);
        searchView = (AutoCompleteTextView) dialog.findViewById(R.id.searchView);
        Button search_button = (Button) dialog.findViewById(R.id.search_button);
        product_not_av = (TextView) dialog.findViewById(R.id.product_not_av);
        product_not_av.setVisibility(View.GONE);

        productLists = new ArrayList<>();
        searchView.setDropDownBackgroundResource(R.color.white);
        searchView.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            dialog_back_image.setImageResource(R.drawable.arrow_30);
        } else {
            dialog_back_image.setImageResource(R.drawable.arrow_right_30);
        }

        dialog.findViewById(R.id.layout1).setOnClickListener(this);
        dialog.findViewById(R.id.layout).setOnClickListener(this);

        if (!TextUtils.isEmpty(Utils.search_text)) {
            searchView.setText(Utils.search_text);
        }
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("before");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("start");

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("change" + s);
                search_text = s.toString();
                if (CommanMethod.isInternetConnected(LoginActivity.this)) {
                    searchProduct(search_text);
                }
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                array_of_product_lists = new ArrayList<>();
                ProductList productList = new ProductList();
                productList.setId(searchadapter.getItem(position).getId());
                productList.setQuantity(searchadapter.getItem(position).getQuantity());
                productList.setUser_id(searchadapter.getItem(position).getUser_id());
                productList.setCate_id(searchadapter.getItem(position).getCate_id());
                productList.setCate_name(searchadapter.getItem(position).getCate_name());
                productList.setTitle(searchadapter.getItem(position).getTitle());
                productList.setDescription(searchadapter.getItem(position).getDescription());
                productList.setProduct_image(searchadapter.getItem(position).getProduct_image());
                productList.setProduct_images(searchadapter.getItem(position).getProduct_images());
                productList.setModel_no(searchadapter.getItem(position).getModel_no());
                productList.setSku_code(searchadapter.getItem(position).getSku_code());
                productList.setPrice(searchadapter.getItem(position).getPrice());
                productList.setCurrent_currency(searchadapter.getItem(position).getCurrent_currency());
                productList.setSale_price(searchadapter.getItem(position).getSale_price());
                productList.setNegotiable(searchadapter.getItem(position).getNegotiable());
                productList.setBrand_name(searchadapter.getItem(position).getBrand_name());
                productList.setBrand_id(searchadapter.getItem(position).getBrand_id());
                productList.setVariation_color(searchadapter.getItem(position).getVariation_color());
                productList.setTags(searchadapter.getItem(position).getTags());
                productList.setIs_hide(searchadapter.getItem(position).getIs_hide());
                productList.setReviewed(searchadapter.getItem(position).getReviewed());
                productList.setFeatured(searchadapter.getItem(position).getFeatured());
                productList.setArchived(searchadapter.getItem(position).getArchived());
                productList.setStatus(searchadapter.getItem(position).getStatus());
                productList.setStock_flag(searchadapter.getItem(position).getStock_flag());
                productList.setRating(searchadapter.getItem(position).getRating());
                productList.setReplacement(searchadapter.getItem(position).getReplacement());
                productList.setReleted_product(searchadapter.getItem(position).getReleted_product());
                productList.setOffer_id(searchadapter.getItem(position).getOffer_id());
                productList.setOffer_name(searchadapter.getItem(position).getOffer_name());
                array_of_product_lists.add(productList);

                /*Intent intent = new Intent(ProductDetailsActivity.this,SearchResultsActivity.class);
                intent.putExtra("array_of_product_lists",array_of_product_lists);
                startActivity(intent);*/
                Intent intent = new Intent(LoginActivity.this, ProductDetailsActivity.class);
                intent.putExtra("product_id", searchadapter.getItem(position).getId());
                intent.putExtra("current_currency", searchadapter.getItem(position).getCurrent_currency());
                intent.putExtra("title_name", searchadapter.getItem(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        });
        dialog_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(searchView.getText().toString())) {
                    array_of_product_lists = new ArrayList<>();
                    String getSearchText = searchView.getText().toString();
                    if (getSearchText.equals("") || getSearchText.length() == 0) {
                    } else {
                        search_text = getSearchText;
                        /*for (int i = 0; i < productLists.size(); i++) {
                            ProductList productList = new ProductList();
                            productList.setId(productLists.get(i).getId());
                            productList.setQuantity(productLists.get(i).getQuantity());
                            productList.setUser_id(productLists.get(i).getUser_id());
                            productList.setCate_id(productLists.get(i).getCate_id());
                            productList.setCate_name(productLists.get(i).getCate_name());
                            productList.setTitle(productLists.get(i).getTitle());
                            productList.setDescription(productLists.get(i).getDescription());
                            productList.setProduct_image(productLists.get(i).getProduct_image());
                            productList.setProduct_images(productLists.get(i).getProduct_images());
                            productList.setModel_no(productLists.get(i).getModel_no());
                            productList.setSku_code(productLists.get(i).getSku_code());
                            productList.setPrice(productLists.get(i).getPrice());
                            productList.setCurrent_currency(productLists.get(i).getCurrent_currency());
                            productList.setSale_price(productLists.get(i).getSale_price());
                            productList.setNegotiable(productLists.get(i).getNegotiable());
                            productList.setBrand_name(productLists.get(i).getBrand_name());
                            productList.setBrand_id(productLists.get(i).getBrand_id());
                            productList.setVariation_color(productLists.get(i).getVariation_color());
                            productList.setTags(productLists.get(i).getTags());
                            productList.setIs_hide(productLists.get(i).getIs_hide());
                            productList.setReviewed(productLists.get(i).getReviewed());
                            productList.setFeatured(productLists.get(i).getFeatured());
                            productList.setArchived(productLists.get(i).getArchived());
                            productList.setStatus(productLists.get(i).getStatus());
                            productList.setStock_flag(productLists.get(i).getStock_flag());
                            productList.setRating(productLists.get(i).getRating());
                            productList.setReplacement(productLists.get(i).getReplacement());
                            productList.setReleted_product(productLists.get(i).getReleted_product());
                            productList.setOffer_id(productLists.get(i).getOffer_id());
                            productList.setOffer_name(productLists.get(i).getOffer_name());
                            array_of_product_lists.add(productList);
                        }*/
                        Utils.search_text = search_text;
                        Intent intent = new Intent(LoginActivity.this, SearchResultsActivity.class);
                        intent.putExtra("array_of_product_lists", array_of_product_lists);
                        intent.putExtra("search_string", search_text);
                        startActivity(intent);
                        Utils.hideKeyBoard(LoginActivity.this, searchView);
                    }
                }

            }
        });
        dialog.show();
    }

    private void searchProduct(String search_text) {
       /* final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();*/
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "productlist_of_brand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        productLists.clear();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_list"));

                        if (jsonArray.length() > 0) {
                            product_not_av.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                ProductSearchModel productList = new ProductSearchModel();
                                productList.setId(jsonObject2.getString("id"));
                                productList.setQuantity(jsonObject2.getString("quantity"));
                                productList.setUser_id(jsonObject2.getString("user_id"));
                                productList.setCate_id(jsonObject2.getString("cate_id"));
                                productList.setCate_name(jsonObject2.getString("cate_name"));
                                productList.setTitle(jsonObject2.getString("title"));
                                productList.setDescription(jsonObject2.getString("description"));
                                productList.setProduct_image(API.ProductURL + jsonObject2.getString("product_image"));
                                productList.setProduct_images(jsonObject2.getString("product_images"));
                                productList.setModel_no(jsonObject2.getString("model_no"));
                                productList.setSku_code(jsonObject2.getString("sku_code"));
                                productList.setPrice(jsonObject2.getString("price"));
                                productList.setCurrent_currency(jsonObject2.getString("current_currency"));
                                //productList.setSale_price(jsonObject2.getString("sale_price"));
                                productList.setNegotiable(jsonObject2.getString("negotiable"));
                                productList.setBrand_name(jsonObject2.getString("brand_name"));
                                productList.setBrand_id(jsonObject2.getString("brand_id"));
                                productList.setVariation_color(jsonObject2.getString("variation_color"));
                                productList.setTags(jsonObject2.getString("tags"));
                                productList.setIs_hide(jsonObject2.getString("is_hide"));
                                productList.setReviewed(jsonObject2.getString("reviewed"));
                                productList.setFeatured(jsonObject2.getString("featured"));
                                productList.setArchived(jsonObject2.getString("archived"));
                                productList.setDeleted_at(jsonObject2.getString("deleted_at"));
                                productList.setStatus(jsonObject2.getString("status"));
                                productList.setStock_flag(jsonObject2.getString("stock_flag"));
                                productList.setRating(jsonObject2.getString("rating"));
                                productList.setReplacement(jsonObject2.getString("replacement"));
                                productList.setReleted_product(jsonObject2.getString("releted_product"));
                                productList.setOffer_id(jsonObject2.getString("offer_id"));
                                productList.setOffer_name(jsonObject2.getString("offer_name"));

                                productLists.add(productList);

                            }
                        } else {
                            product_not_av.setVisibility(View.VISIBLE);
                        }


                    } else {
                        product_not_av.setVisibility(View.VISIBLE);

                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }
                searchadapter = new ArrayAdapter<ProductSearchModel>(LoginActivity.this, android.R.layout.simple_list_item_1, productLists);
                searchView.setAdapter(searchadapter);
                // searchView.showDropDown();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                //dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("search_text", search_text);
                params.put("current_currency", "KWD");
               /* if (bundle.containsKey("brand_id"))
                    params.put("brand_id", bundle.getString("brand_id"));*/

                /*if (sessionManager.getPushFamilyId() != null) {
                    params.put("family_id", sessionManager.getPushFamilyId());
                }*/

                if (!TextUtils.isEmpty(Utils.key)) {
                    params.put("key", Utils.key);
                    params.put("value", Utils.value);

                    //this is putted here also because Utils.shouldFilterApply is false here so get search text value
                }
                List tempList = new ArrayList();


                   /* params.put("tags", sortFilterSessionManager.getFilter_Tags().replace("[", "").replace("]", ""));
                    params.put("color", sortFilterSessionManager.getFilter_Colors().replace("[", "").replace("]", ""));
                    params.put("replacement", sortFilterSessionManager.getFilter_Replacement().replace("[", "").replace("]", ""));
                    params.put("gender", sortFilterSessionManager.getFilter_Gender().replace("[", "").replace("]", ""));
                    params.put("rating", sortFilterSessionManager.getFilter_Rating().replace("[", "").replace("]", ""));
*/
                if (Utils.selectedData.size() > 0) {
                    for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Launch")) {
                            tempList.add(Utils.selectedData.get(j).getId());
                        }
                        if (j == Utils.selectedData.size() - 1) {
                            params.put("release_days", TextUtils.join(",", tempList));
                        }
                    }
                    tempList = new ArrayList();
                    for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Latest")) {
                            tempList.add(Utils.selectedData.get(j).getId());

                        }
                        if (j == Utils.selectedData.size() - 1) {
                            params.put("latest_sorting", TextUtils.join(",", tempList));
                        }
                    }
                    tempList = new ArrayList();

                    for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Tag")) {
                            tempList.add(Utils.selectedData.get(j).getId());

                        }
                        if (j == Utils.selectedData.size() - 1) {
                            params.put("tag", TextUtils.join(",", tempList));
                        }
                    }

                    tempList = new ArrayList();
                    for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Condition")) {
                            tempList.add(Utils.selectedData.get(j).getId());

                        }
                        if (j == Utils.selectedData.size() - 1) {
                            params.put("condition", TextUtils.join(",", tempList));
                        }
                    }
                }
                    /*for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Brands")) {
                            params.put("brands", Utils.selectedData.get(j).getId());
                        }
                    }*/
                if (Utils.brandsIs.size() > 0)
                    params.put("brand_id", TextUtils.join(",", Utils.brandsIs));

                params.put("country", Utils.lastSelectedCountryId);
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

    private void sortDialog() {
        dialog1 = new Dialog(LoginActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.custom_dialog);
        ImageView dialog_back_image = dialog1.findViewById(R.id.dialog_back_image);
        RecyclerView sort_recycler_view = dialog1.findViewById(R.id.sort_recycler_view);

        sort_recycler_view.setLayoutManager(new LinearLayoutManager(LoginActivity.this, RecyclerView.VERTICAL, false));
        SortByAdapterInFragment sortByAdapter = new SortByAdapterInFragment(LoginActivity.this, sortByModelList, this);
        sort_recycler_view.setAdapter(sortByAdapter);

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            dialog_back_image.setImageResource(R.drawable.arrow_30);
        } else {
            dialog_back_image.setImageResource(R.drawable.arrow_right_30);
        }
        dialog_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    private void getSortBy() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "sort_by", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    sortByModelList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("sortlist_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("sortlist"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            SortByModel sortByModel = new SortByModel();
                            sortByModel.setKey(jsonObject2.getString("key"));
                            sortByModel.setValue(jsonObject2.getString("value"));
                            sortByModel.setTitle(jsonObject2.getString("title"));
                            sortByModel.setTitleAr(jsonObject2.getString("title_ar"));
                            sortByModel.setSelected(false);
                            sortByModelList.add(sortByModel);
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
    public void sortByPrice(String key, String value) {
        Utils.key = key;
        Utils.value = value;
        this.dialog1.dismiss();
        // this data make effect on search data show result
    }

    public class SortByAdapterInFragment extends RecyclerView.Adapter<SortByAdapterInFragment.MyViewHolder> {

        private LayoutInflater inflater;
        private List<SortByModel> sortByModelList;
        private ImageLoader imageLoader;
        private Context context;
        private int lastSelectedPosition = -1;
        private SortByInterface sortByInterface;

        public SortByAdapterInFragment(Context ctx, List<SortByModel> sortByModelList, SortByInterface sortByInterface) {
            inflater = LayoutInflater.from(ctx);
            this.sortByModelList = sortByModelList;
            this.context = ctx;
            this.sortByInterface = sortByInterface;

        }

        @Override
        public SortByAdapterInFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.inflate(R.layout.sort_by_recycler_view_item, parent, false);
            SortByAdapterInFragment.MyViewHolder holder = new SortByAdapterInFragment.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(SortByAdapterInFragment.MyViewHolder holder, final int position) {

            if (context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                holder.text_sort.setText(sortByModelList.get(position).getTitle());
            } else {
                holder.text_sort.setText(sortByModelList.get(position).getTitleAr());
            }
        /*if(Locale.getDefault().getLanguage().equals("en")){
            holder.text_sort.setText(sortByModelList.get(position).getTitle());
        }else{
            holder.text_sort.setText(sortByModelList.get(position).getTitleAr());
        }*/
            holder.redia_button.setChecked(sortByModelList.get(position).isSelected());
        }

        @Override
        public int getItemCount() {
            return sortByModelList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            RadioButton redia_button;
            TextView text_sort;
            LinearLayout sort_lay;

            public MyViewHolder(View itemView) {
                super(itemView);
                sort_lay = itemView.findViewById(R.id.sort_lay);
                redia_button = (RadioButton) itemView.findViewById(R.id.redia_button);
                text_sort = (TextView) itemView.findViewById(R.id.text_sort);
                sort_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastSelectedPosition = getAdapterPosition();
                        Utils.selectedSortPosition = getAdapterPosition();
                        sortByInterface.sortByPrice(sortByModelList.get(lastSelectedPosition).getKey(), sortByModelList.get(lastSelectedPosition).getValue());
                        applySelect(lastSelectedPosition);
                        notifyDataSetChanged();
                    }

                    private void applySelect(int position) {
                        for (int j = 0; j < sortByModelList.size(); j++) {
                            if (j == position) {
                                sortByModelList.get(j).setSelected(true);
                            } else {
                                sortByModelList.get(j).setSelected(false);
                            }
                        }
                    }
                });

                redia_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        lastSelectedPosition = getAdapterPosition();

                        sortByInterface.sortByPrice(sortByModelList.get(lastSelectedPosition).getKey(), sortByModelList.get(lastSelectedPosition).getValue());
                        notifyDataSetChanged();
                    }
                });


            }
        }
    }
}
