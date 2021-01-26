package com.rginfotech.egames.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.rginfotech.egames.FilterActivity;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.UserCartActivity;
import com.rginfotech.egames.adapter.ImageSliderAdapter;
import com.rginfotech.egames.adapter.ProductListAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.interfacelenzzo.RefreshProductList;
import com.rginfotech.egames.interfacelenzzo.SortByInterface;
import com.rginfotech.egames.model.ImageSliderList;
import com.rginfotech.egames.model.ProductList;
import com.rginfotech.egames.model.ProductSearchModel;
import com.rginfotech.egames.model.SortByModel;
import com.rginfotech.egames.model.UserWishlist;
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

public class ProductListFragment extends Fragment implements View.OnClickListener, SortByInterface, RefreshProductList {
    View view;
    Bundle bundle;
    //private GifImageView gifImageView;
    private ProductListAdapter productListAdapter;
    private List<ProductList> productListList;
    private RecyclerView productLists_recycler;
    private String brand_id;
    private TextView brand_name_text;
    private String brand_name;
    private TextView product_not_av;
    private List<UserWishlist> userWishlists = new ArrayList<>();
    private ImageView cart_image;
    private TextView number;
    private SessionManager sessionManager;
    private int total_value;
    private String total_count = "";
    private String family_id = "";
    private RelativeLayout relativeLayout;
    private List<SortByModel> sortByModelList;
    private String key = "";
    private String value = "";
    private Dialog dialog1;
    private SortFilterSessionManager sortFilterSessionManager;
    private String isCall;
    private Dialog dialog;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private ImageView search_image;
    private ImageView back_image;
    private LinearLayout liner, backLinLayoutId;
    private AutoCompleteTextView searchView;
    private LinearLayout llToolbar;
    private List<ImageSliderList> imageSliderLists;
    private ImageSliderAdapter imageSliderAdapter;
    private ViewPager slide_viewPager;

    public ProductListFragment() {

    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return (resourceId > 0) ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return (resourceId > 0) ? resources.getDimensionPixelSize(resourceId) : 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_product_list, container, false);

        sessionManager = new SessionManager(getActivity());
        sortFilterSessionManager = new SortFilterSessionManager(getActivity());
        relativeLayout = view.findViewById(R.id.relativeLayout);
        view.findViewById(R.id.back_image);
        view.findViewById(R.id.backLinLayoutId).setOnClickListener(this);
        //gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        productLists_recycler = view.findViewById(R.id.productLists_recycler);
        productLists_recycler.setNestedScrollingEnabled(false);
        brand_name_text = view.findViewById(R.id.brand_name_text);
        product_not_av = view.findViewById(R.id.product_not_av);
        view.findViewById(R.id.layout1).setOnClickListener(this);
        view.findViewById(R.id.layout).setOnClickListener(this);
        cart_image = view.findViewById(R.id.cart_image);
        llToolbar = view.findViewById(R.id.toolbar);
        view.findViewById(R.id.cart_image).setOnClickListener(this);
        number = view.findViewById(R.id.number);
        view.findViewById(R.id.search_image).setOnClickListener(this);

        bundle = getArguments();
        if (bundle.getString("from").equals("wish")) {
            view.findViewById(R.id.search_image).performClick();
        }

        back_image = view.findViewById(R.id.back_image);
        backLinLayoutId = view.findViewById(R.id.backLinLayoutId);
        liner = view.findViewById(R.id.liner);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);

        } else {
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);

        }

        sessionManager.setShouldFilterApply(false);
        Utils.shouldFilterApply = false;

        /*if (CommanMethod.isInternetConnected(getActivity()) && !bundle.getString("from").equals("wish")) {
            final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
            getProductList(dialog);
            CommanClass.getCartValue(getContext(), number);
            //searchProduct();
        }*/
        getSortBy();
        if (bundle.getString("from").equals("wish")) {
            brand_name_text.setText(getString(R.string.search_button));
        } else {
            brand_name_text.setText(getString(R.string.brands));
            String brandNameToolbar = bundle.getString("brand_name");
            brand_name_text.setText(brandNameToolbar);
        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Intent intent4 = new Intent(getActivity(), HomeActivity.class);
                    intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent4);
                    return true;
                }
                return false;
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLinLayoutId:
                Intent intent4 = new Intent(getActivity(), HomeActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent4);
                getActivity().onBackPressed();
                break;

            case R.id.layout1:
                //System.out.println("sort");
                sortDialog();
                break;
            case R.id.layout:
                //System.out.println("filter");
                Intent intent1 = new Intent(getActivity(), FilterActivity.class);
                intent1.putExtra("from", "list_fragment");
                startActivity(intent1);
                break;
            case R.id.cart_image:
                Intent intent = new Intent(getActivity(), UserCartActivity.class);
                startActivity(intent);
                break;
            case R.id.search_image:
                sortDialog1();
                break;
        }
    }

    private void getProductList(final Dialog dialog) {

        /*final Dialog dialog = CommanMethod.getCustomProgressDialog(getActivity());
        dialog.show();*/
        if (!dialog.isShowing()) {
            dialog.show();
        }
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "product_search_by_name", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    productListList = new ArrayList<>();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_list"));
                        product_not_av.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            product_not_av.setVisibility(View.GONE);
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            //JSONObject offerObject = jsonObject2.getJSONObject("offer");
                            /*JSONArray offerArray =jsonObject2.getJSONArray("offer");
                            JSONObject offerObject = null;
                            if (offerArray.length()>0) {
                                offerObject = offerArray.getJSONObject(0);
                            }*/
                            ProductList productList = new ProductList();
                            productList.setId(jsonObject2.getString("id"));
                            productList.setQuantity(jsonObject2.getString("quantity"));
                            productList.setUser_id(jsonObject2.getString("user_id"));
                            productList.setCate_id(jsonObject2.getString("cate_id"));
                            productList.setCate_name(jsonObject2.getString("cate_name"));

                            if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
                                productList.setTitle(jsonObject2.getString("title"));
                                productList.setDescription(jsonObject2.getString("description"));
                            } else {
                                productList.setTitle(jsonObject2.getString("title_ar"));
                                productList.setDescription(jsonObject2.getString("description_ar"));
                            }

                            productList.setProduct_image(API.ProductURL + jsonObject2.getString("product_image"));
                            productList.setProduct_images(jsonObject2.getString("product_images"));
                            productList.setModel_no(jsonObject2.getString("model_no"));
                            productList.setSku_code(jsonObject2.getString("sku_code"));
                            productList.setPrice(jsonObject2.getString("price"));
                            productList.setCurrent_currency(jsonObject2.getString("current_currency"));
                            if (jsonObject2.has("sale_price")) {
                                productList.setSale_price(jsonObject2.getString("sale_price"));
                            }

                            if (jsonObject2.has("deal_otd"))
                                productList.setDeal_otd(jsonObject2.getString("deal_otd"));

                            if (jsonObject2.has("deal_otd_discount"))
                                productList.setDeal_otd_discount(jsonObject2.getString("deal_otd_discount"));


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
                            productList.setWishlist(jsonObject2.getString("wishlist"));
                            productList.setReleted_product(jsonObject2.getString("releted_product"));
                            productList.setOffer_id(jsonObject2.getString("offer_id"));
                            //productList.setOffer_name(jsonObject2.optString("offer_name",""));
                            String offerData = String.valueOf(jsonObject2.get("offer"));
                            productList.setOffer_name(CommanMethod.getOfferName(getActivity(), offerData));
                            /*if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                                if(offerObject != null){
                                    productList.setOffer_name(offerObject.optString("name",""));
                                }

                            }else{
                                if(offerObject != null){
                                    productList.setOffer_name(offerObject.optString("name_ar",""));
                                }
                            }*/

                        /*    if(userWishlists.size()>0) {
                                for (int j = 0; j < userWishlists.size(); j++) {
                                    if (userWishlists.get(j).getProduct_id().equals(jsonObject2.getString("id"))) {
                                        productList.setSelected(true);
                                        break;
                                    }
                                }
                            }*/

                            productListList.add(productList);
                        }
                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }



          /*      GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
                productLists_recycler.setLayoutManager(gridLayoutManager);
*/
          /*      RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                productLists_recycler.setLayoutManager(ff);*/
                productLists_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

                productListAdapter = new ProductListAdapter(getActivity(), productListList, userWishlists, number);
                productLists_recycler.setAdapter(productListAdapter);
                productListAdapter.notifyDataSetChanged();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Bundle bundle = getArguments();
                if (bundle.getString("from").equals("wish")) {
                    /*if (!sessionManager.getUserEmail().isEmpty()
                            && !sessionManager.getUserPassword().isEmpty()) {
                        params.put("user_id", sessionManager.getUserId());
                    } else {
                        params.put("user_id", sessionManager.getRandomValue());
                    }*/
                } else {
                    //params.put("brand_id", sortFilterSessionManager.getFilter_Brands());
                }
                params.put("user_id", sessionManager.getUserId());

                params.put("current_currency", "KWD");
                if (family_id != null) {
                    params.put("family_id", family_id);
                }
                if (!TextUtils.isEmpty(key)) {
                    params.put("key", key);
                    params.put("value", value);

                    if (bundle.getString("from").equals("wish")) {
                        if (TextUtils.isEmpty(Utils.search_text)) {
                            Utils.search_text = Utils.search_textBackup;
                        }
                        params.put("search_text", Utils.search_text);
                    } /////this is putted here also because Utils.shouldFilterApply is false here so get search text value
                }


                List tempList = new ArrayList();

                if (bundle.getString("from").equals("wish")) {
                    if (TextUtils.isEmpty(Utils.search_text)) {
                        Utils.search_text = Utils.search_textBackup;
                    }
                    params.put("search_text", Utils.search_text);
                }


                   /* params.put("tags", sortFilterSessionManager.getFilter_Tags().replace("[", "").replace("]", ""));
                    params.put("color", sortFilterSessionManager.getFilter_Colors().replace("[", "").replace("]", ""));
                    params.put("replacement", sortFilterSessionManager.getFilter_Replacement().replace("[", "").replace("]", ""));
                    params.put("gender", sortFilterSessionManager.getFilter_Gender().replace("[", "").replace("]", ""));
                    params.put("rating", sortFilterSessionManager.getFilter_Rating().replace("[", "").replace("]", ""));
*/
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
                    /*for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Brands")) {
                            params.put("brands", Utils.selectedData.get(j).getId());
                        }
                    }*/
                params.put("country", Utils.lastSelectedCountryId);


                if (Utils.brandsIs.size() > 0)
                    params.put("brand_id", TextUtils.join(",", Utils.brandsIs));

                params.put("country", Utils.lastSelectedCountryId);
                Log.e("SEARCHAA", String.valueOf(params));

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
        dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.custom_dialog);
        ImageView dialog_back_image = dialog1.findViewById(R.id.dialog_back_image);
        RecyclerView sort_recycler_view = dialog1.findViewById(R.id.sort_recycler_view);

        sort_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        SortByAdapterInFragment sortByAdapter = new SortByAdapterInFragment(getActivity(), sortByModelList, this);
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

    public void getWishlist() {
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
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

                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        brand_id = bundle.getString("brand_id");
                        if (brand_id != null) {
                            sortFilterSessionManager.setFilter_Brands(brand_id);
                            brand_name = bundle.getString("brand_name");
                            family_id = bundle.getString("family_id");
                            brand_name_text.setText(brand_name);
                        }
                        isCall = bundle.getString("isCall");
                        getProductList(dialog);

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
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    public void getCartValue() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
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

    private void getSortBy() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
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

    private void sortDialog1() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.search_dialog_box);
        ImageView dialog_back_image = dialog.findViewById(R.id.dialog_back_image);
        LinearLayout backlinLayoutid = dialog.findViewById(R.id.backlinLayoutid);
        searchView = dialog.findViewById(R.id.searchView);

        Button search_button = dialog.findViewById(R.id.search_button);
        searchView.setDropDownBackgroundResource(R.color.white);
        searchView.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            dialog_back_image.setImageResource(R.drawable.arrow_30);
        } else {
            dialog_back_image.setImageResource(R.drawable.arrow_right_30);
        }

        dialog.findViewById(R.id.layout1).setOnClickListener(this);
        dialog.findViewById(R.id.layout).setOnClickListener(this);

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
                Utils.search_text = s.toString();
                Log.e("SEARCHAAAAAA", s.toString());
                if (CommanMethod.isInternetConnected(getActivity())) {
                    searchProduct(Utils.search_text);
                }
            }
        });

        searchView.setOnItemClickListener((parent, view, position, id) -> {
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

            productList.setDeal_otd(searchadapter.getItem(position).getDeal_otd());
            productList.setDeal_otd_discount(searchadapter.getItem(position).getDeal_otd_discount());

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

            /*Intent intent = new Intent(ProductListActivity.this,SearchResultsActivity.class);
            intent.putExtra("array_of_product_lists",array_of_product_lists);
            startActivity(intent);*/
            if (dialog1 != null)
                dialog1.dismiss();

            Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
            intent.putExtra("product_id", searchadapter.getItem(position).getId());
            intent.putExtra("current_currency", searchadapter.getItem(position).getCurrent_currency());
            intent.putExtra("title_name", searchadapter.getItem(position).getTitle());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            //searchView.getText().clear();
            Utils.hideKeyBoard(getActivity(), searchView);


        });
        backlinLayoutid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array_of_product_lists = new ArrayList<>();
                String getSearchText = searchView.getText().toString();
                if (getSearchText.equals("") || getSearchText.length() == 0) {

                } else {
                    Utils.isSearchedApplied = true;
                    Utils.search_text = getSearchText;
                    for (int i = 0; i < Utils.productLists.size(); i++) {
                        ProductList productList = new ProductList();
                        productList.setId(Utils.productLists.get(i).getId());
                        productList.setQuantity(Utils.productLists.get(i).getQuantity());
                        productList.setUser_id(Utils.productLists.get(i).getUser_id());
                        productList.setCate_id(Utils.productLists.get(i).getCate_id());
                        productList.setCate_name(Utils.productLists.get(i).getCate_name());
                        productList.setTitle(Utils.productLists.get(i).getTitle());
                        productList.setDescription(Utils.productLists.get(i).getDescription());
                        productList.setProduct_image(Utils.productLists.get(i).getProduct_image());
                        productList.setProduct_images(Utils.productLists.get(i).getProduct_images());
                        productList.setModel_no(Utils.productLists.get(i).getModel_no());
                        productList.setSku_code(Utils.productLists.get(i).getSku_code());
                        productList.setPrice(Utils.productLists.get(i).getPrice());
                        productList.setCurrent_currency(Utils.productLists.get(i).getCurrent_currency());
                        productList.setSale_price(Utils.productLists.get(i).getSale_price());
                        productList.setNegotiable(Utils.productLists.get(i).getNegotiable());
                        productList.setBrand_name(Utils.productLists.get(i).getBrand_name());

                        productList.setDeal_otd_discount(Utils.productLists.get(i).getDeal_otd_discount());
                        productList.setDeal_otd(Utils.productLists.get(i).getDeal_otd());

                        productList.setBrand_id(Utils.productLists.get(i).getBrand_id());
                        productList.setVariation_color(Utils.productLists.get(i).getVariation_color());
                        productList.setTags(Utils.productLists.get(i).getTags());
                        productList.setIs_hide(Utils.productLists.get(i).getIs_hide());
                        productList.setReviewed(Utils.productLists.get(i).getReviewed());
                        productList.setFeatured(Utils.productLists.get(i).getFeatured());
                        productList.setArchived(Utils.productLists.get(i).getArchived());
                        productList.setStatus(Utils.productLists.get(i).getStatus());
                        productList.setStock_flag(Utils.productLists.get(i).getStock_flag());
                        productList.setRating(Utils.productLists.get(i).getRating());
                        productList.setReplacement(Utils.productLists.get(i).getReplacement());
                        productList.setReleted_product(Utils.productLists.get(i).getReleted_product());
                        productList.setOffer_id(Utils.productLists.get(i).getOffer_id());
                        productList.setOffer_name(Utils.productLists.get(i).getOffer_name());
                        productList.setWishlist(Utils.productLists.get(i).getWishlist());
                        array_of_product_lists.add(productList);
                    }

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    productLists_recycler.setLayoutManager(gridLayoutManager);

                    //TODO
                    productListAdapter = new ProductListAdapter(getActivity(), array_of_product_lists, userWishlists, number);
                    productLists_recycler.setAdapter(productListAdapter);
                    productListAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                    //dkjdfkd
                  /*  Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                    intent.putExtra("array_of_product_lists",array_of_product_lists);
                    intent.putExtra("search_string", search_text);
                    startActivity(intent);
*/
                    //searchProduct(search_text);

                }
            }
        });
        dialog.show();
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    private void searchProduct(String search_text) {
       /* final Dialog dialog = CommanMethod.getCustomProgressDialog(getActivity());
        dialog.show();*/

        if (TextUtils.isEmpty(Utils.search_text)) {
            Utils.search_text = Utils.search_textBackup;
        }


        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());

        Bundle bundle = getArguments();
        String callAPI = "";
        if (bundle.getString("from").equals("wish")) {
            callAPI = "product_search_by_name";
        } else {
            callAPI = "productlist_of_brand";
        }


        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + callAPI, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        Utils.productLists.clear();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_list"));
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

                            productList.setDeal_otd_discount(jsonObject2.getString("deal_otd_discount"));
                            productList.setDeal_otd(jsonObject2.getString("deal_otd"));

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
                            productList.setWishlist(jsonObject2.getString("wishlist"));
                            Utils.productLists.add(productList);

                        }
                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    //dialog.dismiss();
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }
                if (Utils.productLists.size() > 0)
                    product_not_av.setVisibility(View.GONE);
                else
                    product_not_av.setVisibility(View.VISIBLE);

                searchadapter = new ArrayAdapter<>(getActivity(), R.layout.layout_search_item, R.id.text1, Utils.productLists);
                searchView.setAdapter(searchadapter);
                // searchView.showDropDown();
            }
        }, error -> {
            //dialog.show();
            //gifImageView.setVisibility(View.GONE);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("search_text", Utils.search_text);

                Bundle bundle = getArguments();
                if (bundle.getString("from").equals("wish")) {
                    /*if (!sessionManager.getUserEmail().isEmpty()
                            && !sessionManager.getUserPassword().isEmpty()) {
                        params.put("user_id", sessionManager.getUserId());
                    } else {
                        params.put("user_id", sessionManager.getRandomValue());
                    }*/
                } else {
                    params.put("brand_id", sortFilterSessionManager.getFilter_Brands());
                }

                if (bundle.containsKey("brand_id"))
                    params.put("brand_id", bundle.getString("brand_id"));
                params.put("current_currency", "KWD");
                if (family_id != null) {
                    params.put("family_id", family_id);
                }
                if (!TextUtils.isEmpty(key)) {
                    params.put("key", key);
                    params.put("value", value);

                    if (bundle.getString("from").equals("wish")) {
                        if (TextUtils.isEmpty(Utils.search_text)) {
                            Utils.search_text = Utils.search_textBackup;
                        }
                        params.put("search_text", Utils.search_text);
                    } /////this is putted here also because Utils.shouldFilterApply is false here so get search text value
                }
                List tempList = new ArrayList();
                if (bundle.getString("from").equals("wish")) {
                    if (TextUtils.isEmpty(Utils.search_text)) {
                        Utils.search_text = Utils.search_textBackup;
                    }
                }


                   /* params.put("tags", sortFilterSessionManager.getFilter_Tags().replace("[", "").replace("]", ""));
                    params.put("color", sortFilterSessionManager.getFilter_Colors().replace("[", "").replace("]", ""));
                    params.put("replacement", sortFilterSessionManager.getFilter_Replacement().replace("[", "").replace("]", ""));
                    params.put("gender", sortFilterSessionManager.getFilter_Gender().replace("[", "").replace("]", ""));
                    params.put("rating", sortFilterSessionManager.getFilter_Rating().replace("[", "").replace("]", ""));
*/
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
                    /*for (int j = 0; j < Utils.selectedData.size(); j++) {
                        if (Utils.selectedData.get(j).getTitleMain().equals("Brands")) {
                            params.put("brands", Utils.selectedData.get(j).getId());
                        }
                    }*/
                if (Utils.brandsIs.size() > 0)
                    params.put("brands", TextUtils.join(",", Utils.brandsIs));

                params.put("country", Utils.lastSelectedCountryId);
                Log.e("SEARCHAA", String.valueOf(params));
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
        if (CommanMethod.isInternetConnected(getActivity())) {
           /* CommanClass.getCartValue(getActivity(), number);
            if (Utils.shouldFilterApply) {
                final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
                getProductList(dialog);
            } else {

            }*/
            final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
            getProductList(dialog);
        }
    }

    @Override
    public void sortByPrice(String key, String value) {
        this.key = key;
        this.value = value;
        //gifImageView.setVisibility(View.GONE);
        if (CommanMethod.isInternetConnected(getActivity())) {
            Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
            getProductList(dialog);
        }
        this.dialog1.dismiss();
    }

    @Override
    public void refreshList() {
        //  getWishlist();
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
        getProductList(dialog);
    }


    public class SortByAdapterInFragment extends RecyclerView.Adapter<SortByAdapterInFragment.MyViewHolder> {

        private LayoutInflater inflater;
        private List<SortByModel> sortByModelList;
        private ImageLoader imageLoader;
        private Context context;
        private int lastSelectedPosition = -1;
        private SortByInterface sortByInterface;

        public SortByAdapterInFragment(Context ctx, List<SortByModel> sortByModelList, ProductListFragment productListFragment) {
            inflater = LayoutInflater.from(ctx);
            this.sortByModelList = sortByModelList;
            this.context = ctx;
            this.sortByInterface = productListFragment;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = inflater.inflate(R.layout.sort_by_recycler_view_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

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
