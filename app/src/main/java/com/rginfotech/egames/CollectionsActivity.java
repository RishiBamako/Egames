package com.rginfotech.egames;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.adapter.CollectionsAdapter;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.model.ChildModel;
import com.rginfotech.egames.model.CollectionBannerModel;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.rginfotech.egames.utility.SessionManager;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

public class CollectionsActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView collection_recycler;
    private GifImageView gifImageView;
    private List<ChildModel> childModelList;
    private CollectionsAdapter collectionsAdapter;
    private String jsonArray;
    private SessionManager sessionManager;
    private TextView number;
    private int total_value;
    private String total_count="";
    private RelativeLayout relativeLayout;
    private ImageView back_image;
    private LinearLayout liner;
    private ViewPager slide_viewPager;
    private CirclePageIndicator circlePageIndicator;
    private int currentPage = 0;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        sessionManager = new SessionManager(this);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        findViewById(R.id.back_image).setOnClickListener(this);
        findViewById(R.id.cart_image).setOnClickListener(this);
        number = (TextView)findViewById(R.id.number);
        collection_recycler = (RecyclerView)findViewById(R.id.collection_recycler);
        collection_recycler.setNestedScrollingEnabled(false);
        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        back_image = (ImageView)findViewById(R.id.back_image);
        liner = (LinearLayout)findViewById(R.id.liner);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
            liner.setGravity(Gravity.END);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
            liner.setGravity(Gravity.START);
        }

        jsonArray = getIntent().getStringExtra("child_array_list");
        System.out.println("all list :-- "+jsonArray);
        try {
            JSONArray child_jsonArray = new JSONArray(jsonArray);
            childModelList =  new ArrayList<ChildModel>();
            for(int j=0;j<child_jsonArray.length();j++){
                JSONObject child_jsonObject =child_jsonArray.getJSONObject(j);
                ChildModel childModel = new ChildModel();
                childModel.setId(child_jsonObject.getString("id"));
                if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                    childModel.setName(child_jsonObject.optString("name", ""));
                }else{
                    childModel.setName(child_jsonObject.optString("name_ar", ""));
                }
                childModel.setSlug(child_jsonObject.getString("slug"));
                childModel.setImage(API.ProductURL+child_jsonObject.getString("image"));
                childModel.setParent_id(child_jsonObject.getString("parent_id"));
                childModel.setReference(child_jsonObject.getString("reference"));
                childModel.setOffer_id(child_jsonObject.getString("offer_id"));
                //childModel.setOffer_name(child_jsonObject.getString("offer_name"));
                childModel.setOffer_subtitle(child_jsonObject.getString("offer_subtitle"));

                String offerData = String.valueOf(child_jsonObject.get("offer"));
                childModel.setOffer_name(CommanMethod.getOfferName(CollectionsActivity.this, offerData));

                JSONArray sub_child_array = new JSONArray(child_jsonObject.getString("child"));
                childModel.setJsonArray(sub_child_array);
                childModelList.add(childModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        collection_recycler.setLayoutManager(gridLayoutManager);
        collectionsAdapter = new CollectionsAdapter(this,childModelList);
        collection_recycler.setAdapter(collectionsAdapter);

        if (CommanMethod.isInternetConnected(CollectionsActivity.this)){
            CommanClass.getCartValue(this, number);
        }

        slide_viewPager = findViewById(R.id.slide_viewPager);
        circlePageIndicator = findViewById(R.id.indicator);
        if (CommanMethod.isInternetConnected(this)){
            getBanner();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.cart_image:
                Intent intent = new Intent(CollectionsActivity.this,UserCartActivity.class);
                startActivity(intent);
                break;
        }
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
                        if(jsonArray!=null && jsonArray.length()> 0) {
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
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
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

    public void getBanner(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"brandlistbanners_of_category", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    /*{"response":{"collection22":[],
                    "collection22val":[],"id":"1","valbrand_id":"22","valbrand_name":null,
                    "images":"pro_gallery_0.83143300 1576246132_0.jpg,pro_gallery_0.15076200 1576518065_0.png",
                    "ui_order":"1","status":"1","created_at":"2019-12-13 02:08:52",
                    "updated_at":"2019-12-16 05:41:21","collection":{"Product":null},"type":["Product"],
                    "value":[null]},"status":"success","message":"Record fetched successfully."}*/
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        findViewById(R.id.slider_banner).setVisibility(View.VISIBLE);
                        JSONObject responseObject=new JSONObject(object.getString("response"));
                        String[] images = responseObject.getString("images").split(",");
                        JSONArray typeJsonArray = responseObject.getJSONArray("type");
                        JSONArray valueJsonArray = responseObject.getJSONArray("value");

                        ArrayList<CollectionBannerModel> bannerList = new ArrayList<>();
                        for (int i = 0; i< images.length; i++){
                            bannerList.add(new CollectionBannerModel(images[i], typeJsonArray.getString(i), valueJsonArray.getString(i)));

                        }

                        CollectionBannerAdapter collectionBannerAdapter = new CollectionBannerAdapter(bannerList, CollectionsActivity.this);
                        slide_viewPager.setAdapter(collectionBannerAdapter);
                        circlePageIndicator.setViewPager(slide_viewPager);

                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                            public void run() {
                                if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                                    if (currentPage == bannerList.size()) {
                                        currentPage = 0;
                                    }
                                    slide_viewPager.setCurrentItem(currentPage++, true);
                                }else{
                                    if (currentPage == 0) {
                                        currentPage = bannerList.size();
                                        slide_viewPager.setCurrentItem(0, true);
                                    }else {
                                        slide_viewPager.setCurrentItem(currentPage--, true);
                                    }

                                }
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, 4000, 4000);


                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        findViewById(R.id.slider_banner).setVisibility(View.GONE);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                SessionManager sessionManager = new SessionManager(CollectionsActivity.this);

                Map<String, String>  params = new HashMap<String, String>();
                params.put("brandid", sessionManager.getCollectionBrandId());
                //params.put("brandid", "6");
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


    class CollectionBannerAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;
        private List<CollectionBannerModel> bannerModelList;
        private ImageLoader imageLoader;

        public CollectionBannerAdapter(List<CollectionBannerModel> imageSliderLists,Context context) {
            this.bannerModelList = imageSliderLists;
            this.context = context;
        }

        @Override
        public int getCount() {
            return bannerModelList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.custome_image_slide_layout, null);
            CollectionBannerModel collectionBannerModel = bannerModelList.get(position);
            ImageView imageView = (ImageView) view.findViewById(R.id.slide_imageView);
            //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
            //imageLoader.get(API.ProductURL+collectionBannerModel.getImage(), ImageLoader.getImageListener(imageView, R.drawable.no_img, R.drawable.no_img));

            Picasso.get()
                    .load(API.ProductURL+collectionBannerModel.getImage())
                    .placeholder(R.drawable.ic_banner_default)
                    .error(R.drawable.ic_banner_default)
                    .into(imageView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (collectionBannerModel.getType().equals("Category")){

                    }else if (collectionBannerModel.getType().equals("Product")){
                        Intent intent = new Intent(context, ProductDetailsActivity.class);
                        intent.putExtra("product_id",collectionBannerModel.getValue());
                        intent.putExtra("current_currency","");
                        intent.putExtra("title_name","");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else if (collectionBannerModel.getType().equals("Brand")){

                    }else if (collectionBannerModel.getType().equals("Offer")){

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
    }

    @Override
    public void onResume(){
        super.onResume();
        if (CommanMethod.isInternetConnected(this)){
            CommanClass.getCartValue(this, number);
        }
    }
}
