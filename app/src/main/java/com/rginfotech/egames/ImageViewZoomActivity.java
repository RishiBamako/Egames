package com.rginfotech.egames;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.customviews.CustomTextViewNormal;
import com.rginfotech.egames.utility.CustomViewPager;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.LinePageIndicator;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageViewZoomActivity extends AppCompatActivity {

    CustomViewPager cViewPager;
    private int currentPage;
    private String product_images;
    ArrayList<String> aList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_image_view_zoom);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            product_images = bundle.getString("image");
            if(!product_images.isEmpty()){
                aList= new ArrayList(Arrays.asList(product_images.split(",")));
                for(int i=0;i<aList.size();i++){
                    //NUM_PAGES++;
                }
            }
        }


       /* Process: com.rginfotech.egames, PID: 19774
        android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?
        at android.view.ViewRootImpl.setView(ViewRootImpl.java:798)
        at android.view.WindowManagerGlobal.addView(WindowManagerGlobal.java:356)
        at android.view.WindowManagerImpl.addView(WindowManagerImpl.java:93)
        at android.widget.PopupWindow.invokePopup(PopupWindow.java:1434)
        at android.widget.PopupWindow.showAsDropDown(PopupWindow.java:1284)
        at android.widget.ListPopupWindow.show(ListPopupWindow.java:696)
        at android.widget.AutoCompleteTextView.showDropDown(AutoCompleteTextView.java:1217)
        at com.rginfotech.egames.ProductDetailsActivity$33.onResponse(ProductDetailsActivity.java:1100)
        at com.rginfotech.egames.ProductDetailsActivity$33.onResponse(ProductDetailsActivity.java:1039)
        at com.android.volley.toolbox.StringRequest.deliverResponse(StringRequest.java:78)
        at com.android.volley.toolbox.StringRequest.deliverResponse(StringRequest.java:30)
        at com.android.volley.ExecutorDelivery$ResponseDeliveryRunnable.run(ExecutorDelivery.java:106)
        at android.os.Handler.handleCallback(Handler.java:873)*/


        try {
            /*String images = getIntent().getStringExtra("images");
            JSONArray jsonArray = new JSONArray(images);
            //String first_image = images.getString(0);


            for (int i = 0; i < jsonArray.length(); i++) {
                ImageModel imageModel = new ImageModel();
                imageModel.setImageUrl(jsonArray.getString(i));
                imageList.add(imageModel);
            }*/

        }catch (Exception ex){
            ex.printStackTrace();
        }

        CustomTextViewNormal counter_tv = findViewById(R.id.counter_tv);
        LinearLayout back_img = findViewById(R.id.back_iv);
        final CirclePageIndicator tableLayout = (CirclePageIndicator)findViewById(R.id.tabDots);
        //some method for creating ic_account list
        cViewPager = (CustomViewPager)findViewById(R.id.cViewpager);
        cViewPager.setAdapter(new ImagePagerAdapter(counter_tv, aList));
        //cViewPager.setOnPageChangeListener(new DetailOnPageChangeListener(counter_tv));
        //tableLayout.setCounter(counter_tv, imageList.size());
        tableLayout.setViewPager(cViewPager, 0);

        cViewPager.post(new Runnable(){
            @Override
            public void run()
            {
                tableLayout.onPageSelected(cViewPager.getCurrentItem());
            }
        });

        cViewPager.setCurrentItem(getIntent().getIntExtra("position",0), true);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    class ImagePagerAdapter extends PagerAdapter {
        LayoutInflater mLayoutInflater;
        ArrayList<String> imageList;
        TextView counter_tv;

        ImagePagerAdapter(TextView counter_tv, ArrayList<String> imageList){
            this.counter_tv = counter_tv;
            this.imageList = imageList;
            mLayoutInflater = LayoutInflater.from(ImageViewZoomActivity.this);
        }

        @Override
        public int getCount() {
            return (null != imageList) ? imageList.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(ImageViewZoomActivity.this);

            //PhotoView imgDisplay;


            //String text = (new DetailOnPageChangeListener().getCurrentPage())+" of "+ imageList.size();
            //counter_tv.setText(text);


            if (imageList.get(position).contains("users"))
            {
                Picasso.get()
                        .load(imageList.get(position))
                        .placeholder(R.drawable.no_img)
                        .error(R.drawable.no_img)
                        .into(photoView);
            }
            else {
                Picasso.get()
                        .load(API.ProductURL + imageList.get(position))
                        .placeholder(R.drawable.no_img)
                        .error(R.drawable.no_img)
                        .into(photoView);
            }
            container.addView(photoView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }
}
