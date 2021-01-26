package com.rginfotech.egames;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import com.rginfotech.egames.adapter.PowerAdapter;
import com.rginfotech.egames.adapter.PowerListAdapter;
import com.rginfotech.egames.adapter.ProductDetailSliderAdapter;
import com.rginfotech.egames.adapter.RealtedProductListAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.interfacelenzzo.RefreshProductList;
import com.rginfotech.egames.interfacelenzzo.SortByInterface;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.model.PowerModel;
import com.rginfotech.egames.model.ProductList;
import com.rginfotech.egames.model.ProductSearchModel;
import com.rginfotech.egames.model.RightPowerModel;
import com.rginfotech.egames.model.SortByModel;
import com.rginfotech.egames.model.UserWishlist;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;
import com.rginfotech.egames.utility.SortFilterSessionManager;
import com.rginfotech.egames.utility.Utils;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener, RefreshProductList, ObservableScrollViewCallbacks, SortByInterface,RelatedClicked {

    TextView product_not_av;
    private String product_id;
    private String current_currency;
    private String title_name;
    private TextView title_text_view;
    private TextView offer_price;
    private ViewPager slide_viewPager;
    private CirclePageIndicator circlePageIndicator;
    private List<String> aList;
    private ProductDetailSliderAdapter productDetailSliderAdapter;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private TextView product_name;
    private TextView product_code;
    private RatingBar ratingBar;
    //private TextView price_text_view;
    private TextView discription_text_view;
    //private AutoCompleteTextView qty_dropdown_text_view;
    private Button select_power_button;
    //private List<String> qtyList;
    //private  ArrayAdapter<String> adapter;
    private LinearLayout related_pro_text;
    private RecyclerView related_product_recycler_view;
    private List<ProductList> productListList;
    private RealtedProductListAdapter realtedProductListAdapter;
    private List<UserWishlist> userWishlists;
    //private GifImageView gifImageView;
    private ImageView wish_list_image;
    private ImageView wish_list_image1;
    //private Button wishlist_button;
    //private Button remove_wishlist_button;
    private Button add_to_bag;
    private ImageView cart_image, product_image;
    private TextView number;
    private int total_value;
    private String total_count = "";
    private SessionManager sessionManager;
    private String getProduct_id = "";
    private String getBrand_id = "";
    private String getBrand_name = "", cate_id = "";
    public static String wishList = "";
    private String select_qty = "";
    private TextView offer_text_view1;
    private TextView offer_text_view;
    //private NestedScrollView scrollView;
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text = "";
    private ImageView search_image;
    private LinearLayout liner_layout2;
    private LinearLayout power_layout;
    private TextView left_power_text;
    private TextView right_power_text;
    private String start_range = "", end_range = "", diff_range = "", range_above = "", diff_range_above = "";
    private List<PowerModel> powerList;
    private PowerAdapter powerAdapter;
    private PowerListAdapter powerListAdapter;
    private List<RightPowerModel> rightPowerModelList;
    private String leftPower = "0.00", rightPower = "0.00", price, sale_price = "0", currency, singleLeftLensPrice, singleRightLensPrice;
    private String with_power_price;
    private String l_p_n_available = "";
    private String r_p_n_available = "";
    private List<String> leftPowerList;
    private List<String> rightPowerlist;
    private TextView left_price_tv, left_sale_price_tv, right_price_tv, right_sale_price_tv, left_eye_text, right_eye_text;
    //private String sale_price1;
    private ImageView back_image;
    private LinearLayout backLinLayout;
    private LinearLayout liner, share_layout;
    private int rating_count;
    private String avg_rating;
    private TextView review_text_view, tv_price;
    private Button buy_button;
    private ImageView share_image_view;
    private String product_images, quantity = "10";
    private AutoCompleteTextView searchView;
    private String[] powerRangeArray;
    private List<SortByModel> sortByModelList;
    private SortFilterSessionManager sortFilterSessionManager;
    RelatedClicked relatedClicked;


    private Dialog dialog1;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        sessionManager = new SessionManager(this);
        //scrollView = (NestedScrollView)findViewById(R.id.scrollView);
        //scrollView.setNestedScrollingEnabled(false);
        sortFilterSessionManager = new SortFilterSessionManager(ProductDetailsActivity.this);
        relatedClicked = this::relatedClicked;
        title_text_view = (TextView) findViewById(R.id.title_text_view);
        offer_price = (TextView) findViewById(R.id.offer_price);
        buy_button = (Button) findViewById(R.id.btn_buy);

        slide_viewPager = (ViewPager) findViewById(R.id.slide_viewPager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        product_name = (TextView) findViewById(R.id.product_name);
        product_code = (TextView) findViewById(R.id.product_code);
        product_image = (ImageView) findViewById(R.id.product_image);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        review_text_view = (TextView) findViewById(R.id.review_text_view);
        //price_text_view = (TextView)findViewById(R.id.price_text_view);
        offer_text_view = (TextView) findViewById(R.id.offer_text_view);
        tv_price = findViewById(R.id.tv_price);
        offer_text_view1 = (TextView) findViewById(R.id.offer_text_view1);
        discription_text_view = (TextView) findViewById(R.id.discription_text_view);
        //qty_dropdown_text_view = (AutoCompleteTextView)findViewById(R.id.qty_dropdown_text_view);
        select_power_button = (Button) findViewById(R.id.select_power_button);
        findViewById(R.id.select_power_button).setOnClickListener(this);
        related_pro_text = (LinearLayout) findViewById(R.id.related_pro_text);
        related_pro_text.setVisibility(View.GONE);
        related_product_recycler_view = (RecyclerView) findViewById(R.id.related_product_recycler_view);
        related_product_recycler_view.setNestedScrollingEnabled(false);
        //gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        wish_list_image = (ImageView) findViewById(R.id.wish_list_image);
        findViewById(R.id.wish_list_image).setOnClickListener(this);
        wish_list_image1 = (ImageView) findViewById(R.id.wish_list_image1);
        findViewById(R.id.wish_list_image1).setOnClickListener(this);
        //wishlist_button = (Button)findViewById(R.id.wishlist_button);
        //findViewById(R.id.wishlist_button).setOnClickListener(this);
        //remove_wishlist_button = (Button)findViewById(R.id.remove_wishlist_button);
        //findViewById(R.id.remove_wishlist_button).setOnClickListener(this);
        add_to_bag = (Button) findViewById(R.id.add_to_bag);
        findViewById(R.id.add_to_bag).setOnClickListener(this);
        cart_image = (ImageView) findViewById(R.id.cart_image);
        findViewById(R.id.cart_image).setOnClickListener(this);
        number = (TextView) findViewById(R.id.number);
        findViewById(R.id.filterr_image).setOnClickListener(this);
        findViewById(R.id.search_image).setOnClickListener(this);
        liner_layout2 = (LinearLayout) findViewById(R.id.liner_layout2);
        power_layout = (LinearLayout) findViewById(R.id.power_layout);
        left_power_text = (TextView) findViewById(R.id.left_power_text);
        right_power_text = (TextView) findViewById(R.id.right_power_text);
        left_price_tv = (TextView) findViewById(R.id.left_price_tv);
        left_sale_price_tv = findViewById(R.id.left_sale_price_tv);
        right_price_tv = findViewById(R.id.right_price_tv);
        right_sale_price_tv = findViewById(R.id.right_sale_price_tv);
        left_price_tv.setPaintFlags(left_price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        right_price_tv.setPaintFlags(right_price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        back_image = (ImageView) findViewById(R.id.back_image);
        backLinLayout = (LinearLayout) findViewById(R.id.back_LinLayoutId);
        backLinLayout.setOnClickListener(this);
        liner = (LinearLayout) findViewById(R.id.liner);
        share_layout = (LinearLayout) findViewById(R.id.share_layout);
        share_image_view = findViewById(R.id.share_image_view);
        findViewById(R.id.share_image_view).setOnClickListener(this);

        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            back_image.setImageResource(R.drawable.arrow_30);
            share_layout.setGravity(Gravity.END);
        } else {
            back_image.setImageResource(R.drawable.arrow_right_30);
            share_layout.setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)) {
            CommanClass.getCartValue(this, number);
            //getWishlist();
            //searchProduct();
        }


   /*     ObservableScrollView mScrollView =  findViewById(R.id.scrollView);
        mScrollView.setScrollViewCallbacks(this);*/

        findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
        findViewById(R.id.left_right_divider).setVisibility(View.GONE);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                slide_viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);


        left_power_text.setText(getResources().getString(R.string.left_eye_power) + " " + leftPower);
        right_power_text.setText(getResources().getString(R.string.right_eye_power) + " " + rightPower);
        findViewById(R.id.left_plus_tv).setOnClickListener(this);
        findViewById(R.id.left_minus_tv).setOnClickListener(this);
        findViewById(R.id.right_plus_tv).setOnClickListener(this);
        findViewById(R.id.right_minus_tv).setOnClickListener(this);

        buy_button.setOnClickListener(v -> addToCart(product_id, getBrand_id, getBrand_name, "select"));

        getSortBy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_LinLayoutId:
                sessionManager.setLeftEyePower("");
                sessionManager.setRightEyePower("");
                super.onBackPressed();
                break;
            case R.id.wish_list_image:
                if (CommanMethod.isInternetConnected(this)) {
                    wishListAdd(product_id);
                }
                break;
             case R.id.add_to_bag:
                if (cate_id.equals("2") && (leftPower.equals("0.00") || rightPower.equals("0.00"))) {
                    if (powerRangeArray != null && powerRangeArray.length > 0) {
                        powerDialogBox("add_bag");
                    }
                } else {
                    //dialogBox(getProduct_id,getBrand_id,getBrand_name);
                    if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)) {
                        addToCart(product_id, getBrand_id, getBrand_name, "continue");
                    }
                }

                break;
            case R.id.wish_list_image1:
                if (CommanMethod.isInternetConnected(this)) {
                    wishListAdd(product_id);
                }
                break;
            case R.id.cart_image:
                Intent intent = new Intent(ProductDetailsActivity.this, UserCartActivity.class);
                startActivity(intent);
                break;
            case R.id.filterr_image:
                Intent intent1 = new Intent(ProductDetailsActivity.this, FilterActivity.class);
                startActivity(intent1);
                break;
            case R.id.search_image:
                searchsortDialog();
                break;
            case R.id.select_power_button:
                if (powerRangeArray != null && powerRangeArray.length > 0) {
                    powerDialogBox("select_power");
                }
                break;
            case R.id.share_image_view:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                //String shareBody = API.HOME_URL+"share.php?productid=" + getProduct_id; //new add
                //  String shareBody = "https://www.e-gamesstore.com/"; //new add
                String shareBody = "https://e-gamesstore.com?productid=" + getProduct_id; //new add
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_with)));
                break;

            case R.id.left_plus_tv:

                int quantityInt = Integer.parseInt(quantity);

                if (quantityInt <= 10) {
                    if (Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) <= quantityInt) {
                        ((TextView) findViewById(R.id.left_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) + 1));
                        ((TextView) findViewById(R.id.left_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))) + " " + currency);
                        //((TextView)findViewById(R.id.left_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))+" "+currency);
                    }
                } else {
                    if (Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) < 10) {
                        ((TextView) findViewById(R.id.left_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) + 1));
                        ((TextView) findViewById(R.id.left_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))) + " " + currency);
                        //((TextView)findViewById(R.id.left_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))+" "+currency);
                    }
                }

                break;
            case R.id.left_minus_tv:
                if (Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) != 1) {
                    ((TextView) findViewById(R.id.left_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) - 1));
                    ((TextView) findViewById(R.id.left_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))) + " " + currency);
                    //((TextView)findViewById(R.id.left_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.left_count_tv)).getText().toString()) * Float.parseFloat(singleLeftLensPrice))+" "+currency);
                }
                break;
            case R.id.right_plus_tv:
                int quantityInt1 = Integer.parseInt(quantity);
                if (quantityInt1 <= 10) {
                    if (Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) <= quantityInt1) {
                        ((TextView) findViewById(R.id.right_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) + 1));
                        ((TextView) findViewById(R.id.right_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))) + " " + currency);
                        //((TextView)findViewById(R.id.right_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))+" "+currency);
                    }
                } else {
                    if (Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) < 10) {
                        ((TextView) findViewById(R.id.right_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) + 1));
                        ((TextView) findViewById(R.id.right_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))) + " " + currency);
                        //((TextView)findViewById(R.id.right_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))+" "+currency);
                    }
                }

                break;
            case R.id.right_minus_tv:
                if (Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) != 1) {
                    ((TextView) findViewById(R.id.right_count_tv)).setText(String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) - 1));
                    ((TextView) findViewById(R.id.right_sale_price_tv)).setText(CommanMethod.getCountryWiseDecimalNumber(this, String.valueOf(Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))) + " " + currency);
                    //((TextView)findViewById(R.id.right_sale_price_tv)).setText(String.valueOf(Integer.parseInt(((TextView)findViewById(R.id.right_count_tv)).getText().toString()) * Float.parseFloat(singleRightLensPrice))+" "+currency);
                }
            case R.id.layout1:
                sortDialog();
                break;
            case R.id.layout:
                intent1 = new Intent(ProductDetailsActivity.this, FilterActivity.class);
                intent1.putExtra("from", "list_fragment");
                startActivity(intent1);
                break;
        }
    }


    private void getProductDetail(final Dialog dialog) {
        /*final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();*/
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "product_detail", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    //qtyList=new ArrayList<>();
                    productListList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_detail_Array"));
                        JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("product_detail"));
                        //JSONObject offerObject = new JSONObject(jsonObject2.getString("offer"));
                        getProduct_id = jsonObject2.getString("id");
                        getBrand_id = jsonObject2.getString("brand_id");
                        getBrand_name = jsonObject2.getString("brand_name");
                        wishList = jsonObject2.getString("wishlist");
                        cate_id = jsonObject2.getString("cate_id");
                        cate_id = jsonObject2.getString("cate_id");

                        if (!TextUtils.isEmpty(wishList) && wishList.equals("1")) {
                            wish_list_image1.setVisibility(View.VISIBLE);
                            wish_list_image.setVisibility(View.GONE);
                        } else {
                            wish_list_image1.setVisibility(View.GONE);
                            wish_list_image.setVisibility(View.VISIBLE);
                        }

                        Picasso
                                .get()
                                .load(API.ProductURL + jsonObject2.getString("product_image"))
                                .placeholder(R.drawable.no_img)
                                .error(R.drawable.no_img)
                                ///.fitCenter()
                                .into(product_image);

                        product_code.setText(getResources().getString(R.string.product_code) + ": " + jsonObject2.getString("model_no"));

                        currency = jsonObject2.getString("current_currency");
                        price = jsonObject2.getString("price");

                        if (jsonObject2.has("deal_otd")) {
                            String deal_otd = jsonObject2.getString("deal_otd");
                            if (deal_otd.equals("1")) {
                                //Double offerPrice = Double.parseDouble(jsonObject2.getString("deal_otd_discount"));
                                offer_price.setText(getString(R.string.offerpricedot) + " " + jsonObject2.getString("deal_otd_discount") + " " + currency);
                            } else
                                offer_price.setVisibility(View.GONE);

                        } else {
                            offer_price.setVisibility(View.GONE);
                        }
                        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                            tv_price.setText(price + " " + current_currency);
                        } else {
                            tv_price.setText(price + " " + current_currency);

                        }



                      /*  if(jsonObject2.has("sale_price")){
                            //sale_price1=jsonObject2.getString("sale_price");
                            sale_price = jsonObject2.getString("sale_price");
                            if(sale_price.equals("0.00")
                                    || sale_price.equals("0.000")
                                    || sale_price.equals("0")){
                                //price_text_view.setText(sale_price);
                                left_sale_price_tv.setText(price +" "+ currency);
                                right_sale_price_tv.setText(price +" "+ currency);
                                left_price_tv.setVisibility(View.GONE);
                                right_price_tv.setVisibility(View.GONE);
                                //left_price_tv.setVisibility(View.VISIBLE);
                            }else {
                                left_price_tv.setText(price +" "+ currency);
                                right_price_tv.setText(price +" "+ currency);
                                left_sale_price_tv.setText(sale_price +" "+ currency);
                                right_sale_price_tv.setText(sale_price +" "+ currency);
                            }

                        }
                        else {
                            left_sale_price_tv.setText(price +" "+ currency);
                            right_sale_price_tv.setText(price +" "+ currency);
                            left_price_tv.setVisibility(View.GONE);
                            right_price_tv.setVisibility(View.GONE);
                        }*/
                        singleLeftLensPrice = left_sale_price_tv.getText().toString().trim().split(" ")[0];
                        singleRightLensPrice = right_sale_price_tv.getText().toString().trim().split(" ")[0];
                        with_power_price = jsonObject2.getString("with_power_price");
                        l_p_n_available = jsonObject2.getString("l_p_n_available");
                        r_p_n_available = jsonObject2.getString("r_p_n_available");

                        String offerData = String.valueOf(jsonObject2.get("offer"));

                        if (!jsonObject2.getString("offer_id").equals("0") && !jsonObject2.getString("offer_name").equals("")) {
                            liner_layout2.setVisibility(View.VISIBLE);
                        }
                        offer_text_view1.setText(CommanMethod.getOfferName(ProductDetailsActivity.this, offerData));
                        if (CommanMethod.isOutOfStock(jsonObject2.getString("stock_flag"), jsonObject2.getString("quantity"))) {
                            add_to_bag.setEnabled(false);
                            add_to_bag.setTextColor(Color.WHITE);
                            add_to_bag.setText(getResources().getString(R.string.out_of_stock));
                            buy_button.setEnabled(false);
                            buy_button.setText(getResources().getString(R.string.out_of_stock));
                        }


                        if (jsonObject2.has("power") && !jsonObject2.optString("power", "null").equals("null")) {
                            JSONObject powerJsonObject = new JSONObject(jsonObject2.getString("power"));
                            start_range = powerJsonObject.getString("start_range");
                            end_range = powerJsonObject.getString("end_range");
                            diff_range = powerJsonObject.getString("diff_range");
                            range_above = powerJsonObject.getString("range_above");
                            diff_range_above = powerJsonObject.getString("diff_range_above");

                            if (start_range.isEmpty() && end_range.isEmpty()) {
                                findViewById(R.id.line).setVisibility(View.GONE);
                                select_power_button.setVisibility(View.GONE);
                                power_layout.setVisibility(View.GONE);
                                //findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                            }
                        } else {
                            findViewById(R.id.line).setVisibility(View.GONE);
                            select_power_button.setVisibility(View.GONE);
                            power_layout.setVisibility(View.GONE);
                            //findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                        }

                        JSONArray powerRangeJsonArray = jsonObject2.optJSONArray("power_range");
                        if (powerRangeJsonArray != null && powerRangeJsonArray.length() > 0) {
                            powerRangeArray = new String[powerRangeJsonArray.length()];
                            for (int i = 0; i < powerRangeJsonArray.length(); i++) {
                                powerRangeArray[i] = powerRangeJsonArray.getString(i);
                            }
                        } else {
                            findViewById(R.id.line).setVisibility(View.GONE);
                            select_power_button.setVisibility(View.GONE);
                            power_layout.setVisibility(View.GONE);
                        }

                        discription_text_view.setMovementMethod(LinkMovementMethod.getInstance());


                        Log.e("object", jsonObject2.getString("description"));
                        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                            product_name.setText(jsonObject2.getString("title"));
                            title_text_view.setText(jsonObject2.getString("title"));
                            //offer_text_view1.setText(offerObject.optString("name", ""));
                            discription_text_view.setText(Html.fromHtml(jsonObject2.getString("description").replace("<strong>", "").replace("<h1>", "").replace("</h1>", "").replace("</strong>", "")));

                        } else {
                            product_name.setText(jsonObject2.getString("title_ar"));
                            title_text_view.setText(jsonObject2.getString("title_ar"));
                            discription_text_view.setText(Html.fromHtml(jsonObject2.getString("description_ar").replace("<strong>", "").replace("<h1>", "").replace("</h1>", "").replace("</strong>", "")));
                            //offer_text_view1.setText(offerObject.optString("name_ar",""));
                        }

                        if (userWishlists != null && userWishlists.size() > 0) {
                            /*boolean isLiked = false;
                            for (int i = 0; i < userWishlists.size(); i++) {
                                if (product_id.equals(userWishlists.get(i).getProduct_id())) {
                                    //remove_wishlist_button.setVisibility(View.VISIBLE);
                                    //wish_list_image1.setVisibility(View.VISIBLE);
                                    //wish_list_image.setImageResource(R.drawable.heart_fill1);
                                    isLiked = true;
                                    break;
                                }
                            }

                            if (isLiked) {
                                //   wish_list_image.setImageResource(R.drawable.heart_fill1);
                            } else {
                                // wish_list_image.setImageResource(R.drawable.like);
                            }*/

                        }
                        if (!jsonObject2.getString("quantity").isEmpty()) {
                            //int qty = Integer.parseInt(jsonObject2.getString("quantity"));
                            quantity = jsonObject2.getString("quantity");
                            /*for(int i=1;i<=qty;i++){
                                if(i<=10){
                                    //qtyList.add(String.valueOf(i));
                                    if(i==10){
                                        break;
                                    }
                                }
                            }*/
                            //System.out.println("qty"+qtyList);
                        } else {
                            quantity = "10";
                        }
                        product_images = jsonObject2.getString("product_images");
                        if (!product_images.isEmpty()) {
                            aList = new ArrayList(Arrays.asList(product_images.split(",")));
                            for (int i = 0; i < aList.size(); i++) {
                                NUM_PAGES++;
                            }
                        }

                        JSONObject jsonObject3 = new JSONObject(jsonObject.getString("related_product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject3.getString("product_list"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            related_pro_text.setVisibility(View.VISIBLE);
                            JSONObject jsonObject4 = jsonArray.getJSONObject(i);
                            ProductList productList = new ProductList();
                            productList.setId(jsonObject4.getString("id"));
                            productList.setQuantity(jsonObject4.getString("quantity"));
                            productList.setUser_id(jsonObject4.getString("user_id"));
                            productList.setCate_id(jsonObject4.getString("cate_id"));
                            productList.setCate_name(jsonObject4.getString("cate_name"));
                            productList.setTitle(jsonObject4.getString("title"));
                            productList.setDescription(jsonObject4.getString("description"));
                            productList.setProduct_image(API.ProductURL + jsonObject4.getString("product_image"));
                            productList.setProduct_images(jsonObject4.getString("product_images"));
                            productList.setModel_no(jsonObject4.getString("model_no"));
                            productList.setSku_code(jsonObject4.getString("sku_code"));
                            productList.setPrice(jsonObject4.getString("price"));
                            productList.setCurrent_currency(jsonObject4.getString("current_currency"));
                            if (jsonObject4.has("sale_price")) {
                                productList.setSale_price(jsonObject4.getString("sale_price"));
                            }
                            productList.setNegotiable(jsonObject4.getString("negotiable"));
                            productList.setBrand_name(jsonObject4.getString("brand_name"));
                            productList.setBrand_id(jsonObject4.getString("brand_id"));
                            productList.setVariation_color(jsonObject4.getString("variation_color"));
                            productList.setTags(jsonObject4.getString("tags"));
                            productList.setIs_hide(jsonObject4.getString("is_hide"));
                            productList.setReviewed(jsonObject4.getString("reviewed"));
                            productList.setFeatured(jsonObject4.getString("featured"));
                            productList.setArchived(jsonObject4.getString("archived"));
                            productList.setDeleted_at(jsonObject4.getString("deleted_at"));
                            productList.setStatus(jsonObject4.getString("status"));
                            productList.setStock_flag(jsonObject4.getString("stock_flag"));
                            productList.setRating(jsonObject4.getString("rating"));
                            productList.setReplacement(jsonObject4.getString("replacement"));
                            productList.setReleted_product(jsonObject4.getString("releted_product"));
                            productList.setOffer_id(jsonObject4.getString("offer_id"));
                            productList.setOffer_name(jsonObject4.getString("offer_name"));
                            productList.setWishlist(jsonObject4.getString("wishlist"));


                            if (userWishlists.size() > 0) {
                                for (int j = 0; j < userWishlists.size(); j++) {
                                    if (userWishlists.get(j).getProduct_id().equals(jsonObject2.getString("id"))) {
                                        productList.setSelected(true);
                                        break;
                                    }
                                }
                            }

                            productListList.add(productList);

                        }
                        JSONObject product_rating_json = new JSONObject(jsonObject.getString("product_rating_list_Array"));
                        rating_count = product_rating_json.getInt("rating_count");
                        avg_rating = product_rating_json.getString("avg_rating");
                        System.out.println("sdfdsf ds " + rating_count + " " + avg_rating);
                        if (rating_count > 0) {
                            review_text_view.setVisibility(View.VISIBLE);
                            review_text_view.setText(rating_count + " " + getResources().getString(R.string.review_text));
                            ratingBar.setRating(Float.parseFloat(avg_rating));
                        } else {
                            review_text_view.setVisibility(View.VISIBLE);
                        }


                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

                productDetailSliderAdapter = new ProductDetailSliderAdapter(aList, ProductDetailsActivity.this);
                slide_viewPager.setAdapter(productDetailSliderAdapter);
                circlePageIndicator.setViewPager(slide_viewPager);


                findViewById(R.id.pager_item).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ProductDetailsActivity.this, ImageViewZoomActivity.class);
                        intent.putExtra("image", product_images);
                        startActivity(intent);
                    }
                });

                //adapter = new ArrayAdapter<String>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, qtyList);
                //qty_dropdown_text_view.setAdapter(adapter);
                //GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductDetailsActivity.this,2,RecyclerView.HORIZONTAL, false);
                related_product_recycler_view.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.HORIZONTAL, false));

                realtedProductListAdapter = new RealtedProductListAdapter(ProductDetailsActivity.this, productListList, userWishlists, number, ProductDetailsActivity.this,relatedClicked);
                related_product_recycler_view.setAdapter(realtedProductListAdapter);

                /*scrollView.setOnTouchListener(new OnSwipeTouchListener(ProductDetailsActivity.this) {
                    public void onSwipeTop() {
                        //Toast.makeText(ProductDetailsActivity.this, "top", Toast.LENGTH_SHORT).show();
                    }
                    public void onSwipeRight() {
                        //Toast.makeText(ProductDetailsActivity.this, "right", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                    public void onSwipeLeft() {
                        //Toast.makeText(ProductDetailsActivity.this, "left", Toast.LENGTH_SHORT).show();
                    }
                    public void onSwipeBottom() {
                        //Toast.makeText(ProductDetailsActivity.this, "bottom", Toast.LENGTH_SHORT).show();
                    }

                });*/
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
                params.put("product_id", product_id);
                params.put("current_currency", sessionManager.getCurrencyCode());
                params.put("user_id", sessionManager.getUserId());

                Log.e("INFORMATION",product_id+", "+sessionManager.getCurrencyCode()+", "+sessionManager.getUserId());

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

    private void getWishList() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "wishlist", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    userWishlists = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("wishlist_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("wishlist"));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            UserWishlist userWishlist = new UserWishlist();
                            userWishlist.setId(jsonObject2.getString("id"));
                            userWishlist.setUser_id(jsonObject2.getString("user_id"));
                            userWishlist.setProduct_id(jsonObject2.getString("product_id"));
                            userWishlist.setProduct_name(jsonObject2.getString("product_name"));
                            userWishlists.add(userWishlist);
                        }
                        getProductDetail(dialog);
                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
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
            public void retry(VolleyError error) {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void wishListAdd(final String product_id) {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "wishlist_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        if (wishList.equals("1")) {
                            wishList = "0";
                            wish_list_image1.setVisibility(View.GONE);
                            wish_list_image.setVisibility(View.VISIBLE);
                        } else {
                            wishList = "1";
                            wish_list_image1.setVisibility(View.VISIBLE);
                            wish_list_image.setVisibility(View.GONE);
                        }

                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
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
                params.put("user_id", sessionManager.getUserId());
                params.put("product_id", product_id);
                params.put("shade", "colorDefault");
                params.put("left_eye_power", leftPower);
                params.put("right_eye_power", rightPower);
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

    public void getCartValue() {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));
                        String totalcount = jsonObject1.optString("totalcount", "");
                        number.setText(totalcount);
                        /*number.setVisibility(View.GONE);
                        if(jsonArray.length()> 0) {
                            number.setVisibility(View.VISIBLE);
                            total_value = jsonArray.length();
                            total_count = String.valueOf(total_value);
                            number.setText(total_count);
                        }else {
                            number.setText("");
                        }*/
                    } else {
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    private void dialogBox(final String product_id, final String getBrand_id, final String getBrand_Name) {

        final Dialog dialog = new Dialog(ProductDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.buy_now_dialog);
        TextView continue_text_view = (TextView) dialog.findViewById(R.id.continue_text_view);
        TextView go_to_text_view = (TextView) dialog.findViewById(R.id.go_to_text_view);
        continue_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                //System.out.println("user_id"+product_id+getBrand_id+getBrand_Name);
                //addToCart(product_id,getBrand_id,getBrand_Name,"continue");
                finish();
            }
        });
        go_to_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, UserCartActivity.class);
                startActivity(intent);
                dialog.dismiss();
                //addToCart(product_id,getBrand_id,getBrand_Name,"goToCart");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addToCart(final String product_id, final String getBrand_id, final String getBrand_Name, final String keyValue) {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(this);
        dialog.show();

        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        CommanClass.getCartValue(ProductDetailsActivity.this, number);
                        dialogBox(getProduct_id, getBrand_id, getBrand_name);
                       /* if(keyValue.equals("continue")){

                            if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)){
                                CommanClass.getCartValue(ProductDetailsActivity.this, number);
                            }
                        }else if(keyValue.equals("goToCart")){
                            Intent intent=new Intent(ProductDetailsActivity.this, UserCartActivity.class);
                            startActivity(intent);
                        }*/


                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
                /*if(TextUtils.isEmpty(select_qty)){
                    select_qty="1";
                }*/
                params.put("product_id", product_id);
                params.put("shade", "");
                //params.put("quantity",select_qty);
                int leftCount = Integer.parseInt(((TextView) findViewById(R.id.left_count_tv)).getText().toString());
                int rightCount = Integer.parseInt(((TextView) findViewById(R.id.right_count_tv)).getText().toString());
                if (select_power_button.getVisibility() == View.GONE) {
                    params.put("quantity", String.valueOf(leftCount));
                } else {

                    if ((left_power_text.getVisibility() == View.VISIBLE) && (right_power_text.getVisibility() == View.VISIBLE)) {

                        if (leftPower.equals(rightPower)) {
                            params.put("quantity", String.valueOf(leftCount));
                        } else {
                            params.put("quantity", String.valueOf(leftCount + rightCount));
                        }

                        params.put("quantity_left", String.valueOf(leftCount));
                        params.put("quantity_right", String.valueOf(rightCount));
                        params.put("left_eye_power", leftPower);
                        params.put("right_eye_power", rightPower);
                    } else {
                        if (findViewById(R.id.left_power_text).getVisibility() == View.VISIBLE) {
                            params.put("quantity", String.valueOf(leftCount));
                            params.put("quantity_left", String.valueOf(leftCount));
                            params.put("left_eye_power", leftPower);
                        }

                        if (findViewById(R.id.right_power_text).getVisibility() == View.VISIBLE) {
                            params.put("quantity", String.valueOf(rightCount));
                            params.put("quantity_right", String.valueOf(rightCount));
                            params.put("right_eye_power", rightPower);
                        }
                    }
                }
                //params.put("quantity_left", leftPower);
                //params.put("quantity_right", rightPower);

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
                if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)) {
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
                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
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
                        Intent intent = new Intent(ProductDetailsActivity.this, SearchResultsActivity.class);
                        intent.putExtra("array_of_product_lists", array_of_product_lists);
                        intent.putExtra("search_string", search_text);
                        startActivity(intent);
                        Utils.hideKeyBoard(ProductDetailsActivity.this, searchView);

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
                searchadapter = new ArrayAdapter<ProductSearchModel>(ProductDetailsActivity.this, android.R.layout.simple_list_item_1, productLists);
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


    private void powerDialogBox(final String from) {

        Dialog power_dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        power_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        power_dialog.setCancelable(false);
        power_dialog.setContentView(R.layout.power_dialog_list);
        TextView textView = (TextView) power_dialog.findViewById(R.id.close_dialog);
        left_eye_text = (TextView) power_dialog.findViewById(R.id.left_eye_text);
        right_eye_text = (TextView) power_dialog.findViewById(R.id.right_eye_text);
        RecyclerView listView = (RecyclerView) power_dialog.findViewById(R.id.select_list_view);
        RecyclerView listView1 = (RecyclerView) power_dialog.findViewById(R.id.select_list_view1);
        Button btn_done = (Button) power_dialog.findViewById(R.id.btn_done);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power_dialog.dismiss();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftPower = "";
                rightPower = "";
                for (int i = 0; i < powerList.size(); i++) {
                    if (powerList.get(i).isSelected()) {
                        leftPower = powerList.get(i).getValue();
                        sessionManager.setLeftEyePower(leftPower);
                        break;
                    }
                }

                for (int i = 0; i < rightPowerModelList.size(); i++) {
                    if (rightPowerModelList.get(i).isSelected()) {
                        rightPower = rightPowerModelList.get(i).getValue();
                        sessionManager.setRightEyePower(rightPower);
                        break;
                    }
                }

                if (leftPower.equals("0.00") && rightPower.equals("0.00")) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(sale_price + " " + currency);
                        left_price_tv.setText(price + " " + currency);
                    }
                    right_power_text.setVisibility(View.VISIBLE);
                    left_power_text.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                    findViewById(R.id.left_right_divider).setVisibility(View.GONE);

                } else if (leftPower.equals(rightPower)) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(with_power_price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(sale_price + " " + currency);
                        left_price_tv.setText(price + " " + currency);
                    }
                    right_power_text.setVisibility(View.VISIBLE);
                    left_power_text.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                    findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(leftPower) && rightPower.equals("0.00")) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                        //right_sale_price_tv.setText(with_power_price +" "+ currency);
                        //right_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(sale_price + " " + currency);
                        //right_sale_price_tv.setText(sale_price +" "+ currency);
                        left_price_tv.setText(price + " " + currency);
                        //right_price_tv.setText(price +" "+ currency);
                    }
                    right_power_text.setVisibility(View.VISIBLE);
                    left_power_text.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                    findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(leftPower) && !rightPower.equals("0.00")) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(with_power_price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                        //right_sale_price_tv.setText(with_power_price +" "+ currency);
                        //right_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(sale_price + " " + currency);
                        //right_sale_price_tv.setText(sale_price +" "+ currency);
                        left_price_tv.setText(price + " " + currency);
                        //right_price_tv.setText(price +" "+ currency);
                    }
                    right_power_text.setVisibility(View.VISIBLE);
                    left_power_text.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                    findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(rightPower) && leftPower.equals("0.00")) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                        //right_sale_price_tv.setText(with_power_price +" "+ currency);
                        //right_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(sale_price + " " + currency);
                        //right_sale_price_tv.setText(sale_price +" "+ currency);
                        left_price_tv.setText(price + " " + currency);
                        //right_price_tv.setText(price +" "+ currency);
                    }
                    left_power_text.setVisibility(View.VISIBLE);
                    right_power_text.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                    findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(rightPower) && !leftPower.equals("0.00")) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(with_power_price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                        //right_sale_price_tv.setText(with_power_price +" "+ currency);
                        //right_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(sale_price + " " + currency);
                        //right_sale_price_tv.setText(sale_price +" "+ currency);
                        left_price_tv.setText(price + " " + currency);
                        //right_price_tv.setText(price +" "+ currency);
                    }
                    left_power_text.setVisibility(View.VISIBLE);
                    right_power_text.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.GONE);
                    findViewById(R.id.left_right_divider).setVisibility(View.GONE);
                } else if (leftPower.equals("0.00") && !TextUtils.isEmpty(rightPower)) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                        right_sale_price_tv.setText(with_power_price + " " + currency);
                        right_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(price + " " + currency);
                        right_sale_price_tv.setText(with_power_price + " " + currency);
                        left_price_tv.setText(price + " " + currency);
                        right_price_tv.setText(price + " " + currency);
                    }
                    //right_power_text.setVisibility(View.GONE);
                    //((TextView)findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    left_power_text.setVisibility(View.VISIBLE);
                    right_power_text.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                    findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
                } else if (rightPower.equals("0.00") && !TextUtils.isEmpty(leftPower)) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(with_power_price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                        right_sale_price_tv.setText(price + " " + currency);
                        right_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(sale_price + " " + currency);
                        right_sale_price_tv.setText(price + " " + currency);
                        left_price_tv.setText(price + " " + currency);
                        right_price_tv.setText(price + " " + currency);
                    }
                    //right_power_text.setVisibility(View.GONE);
                    //((TextView)findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    left_power_text.setVisibility(View.VISIBLE);
                    right_power_text.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                    findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
                } else if (!TextUtils.isEmpty(leftPower) && !TextUtils.isEmpty(rightPower)) {
                    if (sale_price.equals("0.00")
                            || sale_price.equals("0.000")
                            || sale_price.equals("0")) {
                        left_sale_price_tv.setText(with_power_price + " " + currency);
                        left_price_tv.setVisibility(View.GONE);
                        right_sale_price_tv.setText(with_power_price + " " + currency);
                        right_price_tv.setVisibility(View.GONE);
                    } else {
                        left_sale_price_tv.setText(sale_price + " " + currency);
                        right_sale_price_tv.setText(sale_price + " " + currency);
                        left_price_tv.setText(price + " " + currency);
                        right_price_tv.setText(price + " " + currency);
                    }
                    //right_power_text.setVisibility(View.GONE);
                    //((TextView)findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.qty));
                    left_power_text.setVisibility(View.VISIBLE);
                    right_power_text.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.left_qt_tv)).setText(getResources().getString(R.string.left_qty));
                    findViewById(R.id.right_qty_lay).setVisibility(View.VISIBLE);
                    findViewById(R.id.left_right_divider).setVisibility(View.VISIBLE);
                }


                ((TextView) findViewById(R.id.left_count_tv)).setText("1");
                ((TextView) findViewById(R.id.right_count_tv)).setText("1");
                singleLeftLensPrice = left_sale_price_tv.getText().toString().trim().split(" ")[0];
                singleRightLensPrice = right_sale_price_tv.getText().toString().trim().split(" ")[0];
                left_power_text.setText(getResources().getString(R.string.left_eye_power) + " " + leftPower);
                right_power_text.setText(getResources().getString(R.string.right_eye_power) + " " + rightPower);

                if (cate_id.equals("2") && leftPower.equals("0.00")) {
                    CommanMethod.getCustomOkAlert(ProductDetailsActivity.this, getResources().getString(R.string.left_power_alert));
                } else if (cate_id.equals("2") && rightPower.equals("0.00")) {
                    CommanMethod.getCustomOkAlert(ProductDetailsActivity.this, getResources().getString(R.string.right_power_alert));
                } else {
                    if (from.equals("add_bag")) {
                        addToCart(product_id, getBrand_id, getBrand_name, "select");
                    }
                    power_dialog.dismiss();
                }
            }
        });


        String[] arrSplit = l_p_n_available.split(",");
        leftPowerList = new ArrayList<>();
        leftPowerList.addAll(Arrays.asList(arrSplit));

        String[] arrSplit1 = r_p_n_available.split(",");
        rightPowerlist = new ArrayList<>();
        rightPowerlist.addAll(Arrays.asList(arrSplit1));


        powerList = new ArrayList<>();
        rightPowerModelList = new ArrayList<>();

        if (!start_range.isEmpty() && !end_range.isEmpty()) {
            Float start_number = Float.parseFloat(start_range);
            Float end_number = Float.parseFloat(end_range);


            for (int i = 0; i < powerRangeArray.length; i++) {
                PowerModel powerModel = new PowerModel();
                RightPowerModel rightPowerModel = new RightPowerModel();
                powerModel.setValue(powerRangeArray[i]);
                rightPowerModel.setValue(powerRangeArray[i]);
                for (int j = 0; j < leftPowerList.size(); j++) {
                    if (powerRangeArray[i].equals(leftPowerList.get(j))) {
                        //leftOutOfStock = true;
                        powerModel.setOutOfStock(true);
                    }
                }

                for (int k = 0; k < rightPowerlist.size(); k++) {
                    if (powerRangeArray[i].equals(rightPowerlist.get(k))) {
                        //rightOutOfStock = true;
                        rightPowerModel.setOutOfStock(true);
                    }
                }

                if (!sessionManager.getLeftEyePower().isEmpty()) {
                    if (powerModel.getValue().equals(sessionManager.getLeftEyePower())) {
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog) + ": " + sessionManager.getLeftEyePower());
                        powerModel.setSelected(true);
                    } else if (sessionManager.getLeftEyePower().equals("0.00")) {
                        powerModel.setSelected(false);
                    }
                } else {
                    if (powerModel.getValue().equals("0.00")) {
                        powerModel.setSelected(true);
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog) + ": 0.00");
                    }
                }

                if (!sessionManager.getRightEyePower().isEmpty()) {
                    if (rightPowerModel.getValue().equals(sessionManager.getRightEyePower())) {
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog) + ": " + sessionManager.getRightEyePower());
                        rightPowerModel.setSelected(true);
                    } else if (sessionManager.getLeftEyePower().equals("0.00")) {
                        right_power_text.setText(sessionManager.getRightEyePower());
                        rightPowerModel.setSelected(false);
                    }
                } else {
                    if (rightPowerModel.getValue().equals("0.00")) {
                        rightPowerModel.setSelected(true);
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog) + ": 0.00");
                    }
                }

                powerList.add(powerModel);
                rightPowerModelList.add(rightPowerModel);
            }


            listView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            powerAdapter = new PowerAdapter(this, powerList, leftPowerList);
            listView.setAdapter(powerAdapter);


            //GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this,1);
            listView1.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
            powerListAdapter = new PowerListAdapter(this, rightPowerModelList, rightPowerlist);
            listView1.setAdapter(powerListAdapter);

        }
        power_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        power_dialog.show();
    }

    public void updatePowerSelectionLeft(int position) {

        for (int i = 0; i < powerList.size(); i++) {
            if (i == position) {
                if (powerList.get(position).isSelected()) {
                    boolean rightSelected = false;
                    for (int j = 0; j < rightPowerModelList.size(); j++) {
                        if (rightPowerModelList.get(j).isSelected()) {
                            rightSelected = true;
                            break;
                        }
                    }
                    if (rightSelected) {
                        powerList.get(position).setSelected(false);
                        left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog));
                        sessionManager.setLeftEyePower("");
                    }

                    //System.out.println("get Value1" +powerList.get(position).getValue());
                    //leftPower="0.00";

                } else {
                    powerList.get(position).setSelected(true);
                    //System.out.println("get Value2" +powerList.get(position).getValue());
                    //leftPower = powerList.get(position).getValue();
                    left_eye_text.setText(getResources().getString(R.string.left_eye_text_dialog) + ": " + powerList.get(position).getValue());
                    sessionManager.setLeftEyePower(powerList.get(position).getValue());
                }
            } else {
                powerList.get(i).setSelected(false);
            }

        }
        powerAdapter.notifyDataSetChanged();
    }

    public void updatepowerSelectionRight(int position) {

        for (int i = 0; i < rightPowerModelList.size(); i++) {
            if (i == position) {
                if (rightPowerModelList.get(position).isSelected()) {
                    boolean leftSelected = false;
                    for (int j = 0; j < powerList.size(); j++) {
                        if (powerList.get(j).isSelected()) {
                            leftSelected = true;
                            break;
                        }
                    }
                    if (leftSelected) {
                        rightPowerModelList.get(position).setSelected(false);
                        right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog));
                        sessionManager.setRightEyePower("");
                    }
                    //rightPower="0.00";

                } else {
                    rightPowerModelList.get(position).setSelected(true);
                    //rightPower = rightPowerModelList.get(position).getValue();
                    right_eye_text.setText(getResources().getString(R.string.right_eye_text_dialog) + ": " + rightPowerModelList.get(position).getValue());
                    sessionManager.setRightEyePower(rightPowerModelList.get(position).getValue());
                }
            } else {
                rightPowerModelList.get(i).setSelected(false);
            }

        }
        powerListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)) {
            CommanClass.getCartValue(this, number);
        }
        Intent in = getIntent();
        Uri data = in.getData();
        if (data != null) {
            System.out.println(data.toString().substring(data.toString().indexOf("=") + 1));
            product_id = data.toString().substring(data.toString().indexOf("=") + 1);
            current_currency = sessionManager.getCurrencyCode();
            if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)) {
                getWishList();
                //searchProduct();
            }
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                product_id = bundle.getString("product_id");
                if (bundle.containsKey("current_currency"))
                    current_currency = bundle.getString("current_currency");
                title_name = bundle.getString("title_name");
                title_text_view.setText(title_name);
                if (CommanMethod.isInternetConnected(ProductDetailsActivity.this)) {
                    getWishList();
                    //searchProduct();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        sessionManager.setLeftEyePower("");
        sessionManager.setRightEyePower("");
        super.onBackPressed();
    }

    @Override
    public void refreshList() {

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorPrimary);
        float alpha = Math.min(1, (float) scrollY / getResources().getDimension(R.dimen._160sdp));
        findViewById(R.id.relativeLayout).setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(findViewById(R.id.relativeLayout), scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void getSortBy() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(ProductDetailsActivity.this);
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

    private void sortDialog() {
        dialog1 = new Dialog(ProductDetailsActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.custom_dialog);
        ImageView dialog_back_image = dialog1.findViewById(R.id.dialog_back_image);
        RecyclerView sort_recycler_view = dialog1.findViewById(R.id.sort_recycler_view);

        sort_recycler_view.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, RecyclerView.VERTICAL, false));
        SortByAdapterInFragment sortByAdapter = new SortByAdapterInFragment(ProductDetailsActivity.this, sortByModelList, this);
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

    @Override
    public void sortByPrice(String key, String value) {
        Utils.key = key;
        Utils.value = value;
        this.dialog1.dismiss();
        // this data make effect on search data show result
    }

    @Override
    public void relatedClicked(String prod_id, String currentcy, String title) {
        Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
        intent.putExtra("product_id", prod_id);
        intent.putExtra("current_currency", currentcy);
        intent.putExtra("title_name",title);
        startActivity(intent);
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
