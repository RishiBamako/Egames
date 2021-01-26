package com.rginfotech.egames.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.fragment.BrandListFragment;
import com.rginfotech.egames.fragment.CollectionFragment;
import com.rginfotech.egames.fragment.ProductListFragment;
import com.rginfotech.egames.model.ImageSliderList;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.rginfotech.egames.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ImageSliderList> imageSliderLists;
    private ImageLoader imageLoader;

    public ImageSliderAdapter(List<ImageSliderList> imageSliderLists,Context context) {
        this.imageSliderLists = imageSliderLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageSliderLists.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custome_image_slide_layout, null);
        ImageSliderList utils = imageSliderLists.get(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.slide_imageView);
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(utils.getPath(), ImageLoader.getImageListener(imageView, R.drawable.no_img, R.drawable.no_img));
        Picasso.get()
                .load(utils.getPath())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(position == 0){
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }*/

                if (!imageSliderLists.get(position).getCategory_id().equals("0")){
                    //  Brand list
                    Bundle bundle=new Bundle();
                    bundle.putString("category_id", imageSliderLists.get(position).getCategory_id());
                    BrandListFragment brandListFragment = new BrandListFragment();
                    brandListFragment.setArguments(bundle);
                    ((HomeActivity)context).replaceFragment(brandListFragment);
                }else if (!imageSliderLists.get(position).getProduct_id().equals("0")){
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("product_id",imageSliderLists.get(position).getProduct_id());
                    intent.putExtra("current_currency","");
                    intent.putExtra("title_name","");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }else if (!imageSliderLists.get(position).getBrand_id().equals("0")){
                    if (CommanMethod.isInternetConnected(context)){
                        getBrandChildList(imageSliderLists.get(position).getBrand_id());
                    }

                }else if (!imageSliderLists.get(position).getOffer_id().equals("0")){
                    // Open offer fragment
                    if (CommanMethod.isInternetConnected(context)){
                        getOfferList(imageSliderLists.get(position).getOffer_id());
                    }

                }

            }
        });
        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }

    private void getBrandChildList(final String brand_id){
        SessionManager sessionManager = new SessionManager(context);
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"getchildfrombrand", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("child"));
                        if(jsonArray.length()> 0) {
                            sessionManager.setCollectionBrandId(brand_id);
                            CollectionFragment collectionFragment = new CollectionFragment(true);
                            Bundle bundle = new Bundle();
                            bundle.putString("child_array_list",jsonArray.toString());
                            collectionFragment.setArguments(bundle);
                            ((HomeActivity)context).replaceFragment(collectionFragment);

                        }else {
                            Bundle bundle = new Bundle();
                            bundle.putString("brand_id", brand_id);
                            bundle.putString("brand_name", "");
                            bundle.putString("from","");
                            ProductListFragment productListFragment = new ProductListFragment();
                            productListFragment.setArguments(bundle);
                            ((HomeActivity)context).replaceFragment(productListFragment);
                        }
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("brandid", brand_id);
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

    private void getOfferList(final String offerId){
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"offer_brandlist_of_category", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("offer_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("offer_list"));
                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                            JSONObject offer_jsonArray = new JSONObject(jsonObject3.getString("offer"));
                            /*OffersModel offersModel = new OffersModel();
                            //offersModel.setId(jsonObject3.getString("id"));
                            offersModel.setId(jsonObject3.getString("brand_id"));
                            offersModel.setName(jsonObject3.getString("name"));
                            offersModel.setSlug(jsonObject3.getString("slug"));


                            offersModel.setBrand_image(API.ProductURL+offer_jsonArray.getString("image"));
                            if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                                offersModel.setOffer_subtitle(offer_jsonArray.getString("offer_subtitle"));
                            }else{
                                offersModel.setOffer_subtitle(offer_jsonArray.getString("offer_subtitle_ar"));
                            }
                            offersModel.setOffer_id(offer_jsonArray.getString("id"));
                            offersModel.setFamilyId(offer_jsonArray.getString("family_id"));*/
                            //offersModel.setChild_json_array(child_jsonArray);
                            if (offerId.equals(offer_jsonArray.getString("id"))) {
                                Bundle bundle = new Bundle();
                                bundle.putString("brand_id", jsonObject3.getString("brand_id"));
                                bundle.putString("brand_name", jsonObject3.getString("name"));
                                bundle.putString("family_id", offer_jsonArray.getString("family_id"));
                                bundle.putString("from", "");
                                ProductListFragment productListFragment = new ProductListFragment();
                                productListFragment.setArguments(bundle);
                                ((HomeActivity)context).replaceFragment(productListFragment);
                                break;
                            }
                        }

                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog.dismiss();
                }



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("offer_filter", "offer_filter");
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
