package com.rginfotech.egames.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.onesignal.OneSignal;
import com.rginfotech.egames.AddNewAddressActivity;
import com.rginfotech.egames.ChangePasswordActivity;
import com.rginfotech.egames.CheckOutActivity;
import com.rginfotech.egames.EditProfileActivity;
import com.rginfotech.egames.FilterActivity;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.LoginActivity;
import com.rginfotech.egames.LoyaltyPointActivity;
import com.rginfotech.egames.MyOrderActivity;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.SearchResultsActivity;
import com.rginfotech.egames.UserCartActivity;
import com.rginfotech.egames.WishList_NewActivity;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.interfacelenzzo.SortByInterface;
import com.rginfotech.egames.localization.LocaleManager;
import com.rginfotech.egames.model.ProductList;
import com.rginfotech.egames.model.ProductSearchModel;
import com.rginfotech.egames.model.SortByModel;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.rginfotech.egames.utility.SessionManager;
import com.rginfotech.egames.utility.SortFilterSessionManager;
import com.rginfotech.egames.utility.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccountFragment extends Fragment implements View.OnClickListener, SortByInterface {

    TextView product_not_av;
    Intent intent;
    private TextView number;
    private CircleImageView profile_image;
    private TextView name_text_view;
    private TextView email_text_view;
    private TextView mobile_text_view, wishlist_text_view;
    private SessionManager sessionManager;
    private Dialog dialog;
    private List<ProductSearchModel> productLists;
    private ArrayAdapter<ProductSearchModel> searchadapter;
    private ArrayList<ProductList> array_of_product_lists;
    private String search_text = "";
    private ImageView search_image;
    private String total_count = "";
    private int total_value;
    private ImageLoader imageLoader;
    private ImageView back_image;
    private LinearLayout liner,liner_layout1;
    private TextView loyalty_point_text;
    private AutoCompleteTextView searchView;
    private String product_images;
    private LinearLayout phone_lay;
    private ImageView eng_icon;
    private ImageView arb_icon;
    private String selectLanguage = "";
    private Dialog dialog1;
    private List<SortByModel> sortByModelList;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public MyAccountFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        sessionManager = new SessionManager(getActivity());
        view.findViewById(R.id.back_image).setOnClickListener(this);
        view.findViewById(R.id.cart_image).setOnClickListener(this);
        view.findViewById(R.id.search_image).setOnClickListener(this);
        view.findViewById(R.id.filter_image).setOnClickListener(this);
        view.findViewById(R.id.eng_lay).setOnClickListener(this);
        //male_textview = (TextView)findViewById(R.id.male_textview);
        view.findViewById(R.id.arb_lay).setOnClickListener(this);
        phone_lay = view.findViewById(R.id.phone_lay);
        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        view.findViewById(R.id.profile_image).setOnClickListener(this);
        name_text_view = (TextView) view.findViewById(R.id.name_text_view);
        wishlist_text_view = (TextView) view.findViewById(R.id.wishlist_text_view);
        number = (TextView) view.findViewById(R.id.number);
        email_text_view = (TextView) view.findViewById(R.id.email_text_view);
        mobile_text_view = (TextView) view.findViewById(R.id.mobile_text_view);

        eng_icon = (ImageView) view.findViewById(R.id.eng_icon);
        arb_icon = (ImageView) view.findViewById(R.id.arb_icon);
        if ("ar".equals(sessionManager.getLanguageSelected())) {
            arb_icon.setVisibility(View.VISIBLE);
            eng_icon.setVisibility(View.GONE);
        } else {
            arb_icon.setVisibility(View.GONE);
            eng_icon.setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.order_text_view).setOnClickListener(this);
        view.findViewById(R.id.loyalty_text_view).setOnClickListener(this);
        view.findViewById(R.id.edit_profile_text_view).setOnClickListener(this);
        view.findViewById(R.id.edit_address_text_view).setOnClickListener(this);
        view.findViewById(R.id.change_password_text_view).setOnClickListener(this);
        view.findViewById(R.id.logout_text_view).setOnClickListener(this);
        wishlist_text_view.setOnClickListener(this::onClick);

        loyalty_point_text = (TextView) view.findViewById(R.id.loyalty_point_text);
        //gifImageView = (GifImageView)view.findViewById(R.id.gifImageView);
        back_image = (ImageView) view.findViewById(R.id.back_image);
        liner = (LinearLayout) view.findViewById(R.id.liner);
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        } else {
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }
        if (CommanMethod.isInternetConnected(getContext())) {
            getProfileDetails();
            //searchProduct();
            CommanClass.getCartValue(getActivity(), number);
            getTotalRedeemPoint();
        }
        getSortBy();
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
            case R.id.back_image:

                Intent intent4 = new Intent(getActivity(), HomeActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent4);

                break;
            case R.id.order_text_view:
                if (sessionManager.getUserType().equals("Guest")) {
                    //CommanMethod.getCustomOkAlert(getActivity(), getResources().getString(R.string.guest_account_alert));
                    getCustomOkLoginReg(getActivity(), getResources().getString(R.string.guest_account_alert));
                } else {
                    Intent intent2 = new Intent(getActivity(), MyOrderActivity.class);
                    startActivity(intent2);
                }

                break;
            case R.id.wishlist_text_view:
                Intent intent = new Intent(getActivity(), WishList_NewActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);

                break;
            case R.id.loyalty_text_view:
                if (sessionManager.getUserType().equals("Guest")) {
                    //CommanMethod.getCustomOkAlert(getActivity(), getResources().getString(R.string.guest_account_alert));
                    getCustomOkLoginReg(getActivity(), getResources().getString(R.string.guest_account_alert));
                } else {
                    Intent loya_intent = new Intent(getActivity(), LoyaltyPointActivity.class);
                    startActivity(loya_intent);
                }

                break;
            case R.id.edit_profile_text_view:
                if (sessionManager.getUserType().equals("Guest")) {
                    //CommanMethod.getCustomOkAlert(getActivity(), getResources().getString(R.string.guest_account_alert));
                    getCustomOkLoginReg(getActivity(), getResources().getString(R.string.guest_account_alert));
                } else {
                    Intent intent5 = new Intent(getActivity(), EditProfileActivity.class);
                    startActivity(intent5);
                }
                break;
            case R.id.edit_address_text_view:
                if (sessionManager.getUserType().equals("Guest")) {
                    getCustomOkLoginReg(getActivity(), getResources().getString(R.string.guest_account_alert));
                } else {
                    Intent intent5 = new Intent(getActivity(), CheckOutActivity.class);
                    intent5.putExtra("MYACCOUNT","YES");
                    startActivity(intent5);
                }
                break;
            case R.id.change_password_text_view:
                if (sessionManager.getUserType().equals("Guest")) {
                    //CommanMethod.getCustomOkAlert(getActivity(), getResources().getString(R.string.guest_account_alert));
                    getCustomOkLoginReg(getActivity(), getResources().getString(R.string.guest_account_alert));
                } else {
                    Intent intent3 = new Intent(getActivity(), ChangePasswordActivity.class);
                    startActivity(intent3);
                }

                break;
            case R.id.logout_text_view:
                /*sessionManager.removeId("user_email","user_password");
                Intent intent4 = new Intent(getActivity(),HomeActivity.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent4);*/
                if (CommanMethod.isInternetConnected(getContext())) {
                    doLogout();
                }
                break;
            case R.id.cart_image:
                intent = new Intent(getActivity(), UserCartActivity.class);
                startActivity(intent);
                break;
            case R.id.search_image:
                searchsortDialog();
                break;
            case R.id.filter_image:
                Intent intent1 = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent1);
                break;
            case R.id.profile_image:
                /*Intent intent11 = new Intent(getActivity(), ImageViewZoomActivity.class);
                intent11.putExtra("image", product_images);
                startActivity(intent11);*/
                break;

            case R.id.eng_lay:
                selectLanguage = "eng";
                eng_icon.setVisibility(View.VISIBLE);
                arb_icon.setVisibility(View.GONE);
                setNewLocale(LocaleManager.LANGUAGE_ENGLISH);
                break;
            case R.id.arb_lay:
                selectLanguage = "arb";
                arb_icon.setVisibility(View.VISIBLE);
                eng_icon.setVisibility(View.GONE);
                setNewLocale(LocaleManager.LANGUAGE_ARABIC);
                break;
            case R.id.layout1:
                sortDialog();
                break;
            case R.id.layout:
                intent1 = new Intent(getActivity(), FilterActivity.class);
                intent1.putExtra("from", "list_fragment");
                startActivity(intent1);
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
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

    public void getCustomOkLoginReg(Context context, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.ok_alert_layout);
        TextView info_tv = dialog.findViewById(R.id.info_tv);
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        info_tv.setText(message);
        ok_tv.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("from", "register");
            startActivity(intent);
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void searchsortDialog() {
        dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
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
                if (CommanMethod.isInternetConnected(getActivity())) {
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
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
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
                if (productLists.size() > 0) {
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
                        Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                        intent.putExtra("array_of_product_lists", array_of_product_lists);
                        intent.putExtra("search_string", search_text);
                        startActivity(intent);
                        Utils.hideKeyBoard(getActivity(), searchView);
                    }
                }

            }
        });
        dialog.show();
    }

    private void searchProduct(String search_text) {

        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "productlist_of_brand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        productLists.clear();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("product_list_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("product_list"));
                        if (jsonArray != null) {
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
                        }
                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }
                searchadapter = new ArrayAdapter<ProductSearchModel>(getContext(), android.R.layout.simple_list_item_1, productLists);
                searchView.setAdapter(searchadapter);
                // searchView.showDropDown();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //gifImageView.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("search_text", search_text);
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

    private void getProfileDetails() {
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
        dialog.show();
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "my_profile", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        imageLoader = CustomVolleyRequest.getInstance(getActivity()).getImageLoader();
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        if (sessionManager.getUserType().equals("Guest")) {
                            name_text_view.setText(getResources().getString(R.string.guest_user));
                            //mobile_text_view.setText("");
                            phone_lay.setVisibility(View.GONE);

                        } else {
                            name_text_view.setText(CommanMethod.capitalize(jsonObject.getString("name")));
                            mobile_text_view.setText("+" + jsonObject.getString("country_code") + " " + jsonObject.getString("phone"));
                        }

                        email_text_view.setText(jsonObject.getString("email"));

                        product_images = API.PROFILE_IMAGE + jsonObject.getString("profilephoto");
                        //imageLoader.get(API.PROFILE_IMAGE+jsonObject.getString("profilephoto"), ImageLoader.getImageListener(profile_image, R.drawable.no_img, android.R.drawable.ic_dialog_alert));
                        Picasso.get().load(API.PROFILE_IMAGE + jsonObject.getString("profilephoto")).placeholder(R.drawable.ic_banner_default)
                                .error(R.drawable.ic_banner_default).into(profile_image);


                        // Log.e("image",API.PROFILE_IMAGE+jsonObject.getString("profilephoto"));
                    } else {
                        // Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

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
                params.put("user_id", sessionManager.getUserId());
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

    public void getCartValue() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
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

    private void getTotalRedeemPoint() {
        RequestQueue mRequestQueue = Volley.newRequestQueue(getContext());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "totalRedeemPoint", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        //avi_point.setText(object.getString("point"));
                        //point_text.setText("( "+object.getString("price")+" "+object.getString("current_currency")+" )");
                        //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_success), Toast.LENGTH_SHORT).show();
                        loyalty_point_text.setText(getResources().getString(R.string.you_have) + " " + object.getString("point") + " (" + CommanMethod.getCountryWiseDecimalNumber(getActivity(), object.getString("price")) + " " + object.getString("current_currency") + ") " + getResources().getString(R.string.loy_point));
                    } else {
                        //Toast.makeText(UserCartActivity.this, getString(R.string.toast_message_fail)+message, Toast.LENGTH_SHORT).show();
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
                params.put("user_id", sessionManager.getUserId());
                params.put("current_currency", sessionManager.getCurrencyCode());
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
            public void retry(VolleyError error) {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void doLogout() {
        //gifImageView.setVisibility(View.VISIBLE);
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(getActivity());
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "logout",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            //gifImageView.setVisibility(View.GONE);
                            JSONObject object = new JSONObject(response);
                            String status = object.getString("status");
                            if (status.equals("success")) {
                                //JSONObject jsonObject=new JSONObject(object.getString("response"));
                                //sessionManager.removeId("user_email","user_password");

                                SortFilterSessionManager sortFilterSessionManager = new SortFilterSessionManager(getActivity());
                                //JSONObject jsonObject=new JSONObject(object.getString("response"));
                                //sessionManager.removeId("user_email","user_password");
                                sessionManager.clearSelectedPreferences();
                                sortFilterSessionManager.clearSelectedFilter();
                                if (TextUtils.isEmpty(sessionManager.getRandomValue())) {
                                    sessionManager.setRandomValue(CommanMethod.getRandomNumber());
                                }
                                Intent intent4 = new Intent(getActivity(), HomeActivity.class);
                                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent4);
                            } else if (status.equals("failed")) {
                                String message = CommanMethod.getMessage(getActivity(), object);
                                CommanMethod.getCustomOkAlert(getActivity(), message);
                                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                            //gifImageView.setVisibility(View.GONE);
                        }

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
                /*if(FirebaseInstanceId.getInstance().getToken() != null) {
                    params.put("device_id", FirebaseInstanceId.getInstance().getToken());
                }*/
                if (OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId() != null) {
                    params.put("device_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                }
                params.put("user_id", sessionManager.getUserId());
                params.put("email", sessionManager.getUserEmail());
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
        if (CommanMethod.isInternetConnected(getContext())) {
            getProfileDetails();
            getTotalRedeemPoint();
        }
        //System.out.println("onResume Ajay");
    }

    private void setNewLocale(String language) {
        LocaleManager.setNewLocale(getContext(), language);
        sessionManager.setLanguageSelected(language);
        Intent refresh = new Intent(getContext(), HomeActivity.class);
        startActivity(refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();
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
