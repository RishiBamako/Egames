package com.rginfotech.egames.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.ImageViewZoomActivity;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductDetailSliderAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> imageSliderLists;
    //private ImageLoader imageLoader;

    public ProductDetailSliderAdapter(List<String> imageSliderLists,Context context) {
        this.imageSliderLists = imageSliderLists;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(imageSliderLists!=null && imageSliderLists.size()>0){
            return imageSliderLists.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.product_detail_image_slider, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.slide_imageView);

        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(API.ProductURL+imageSliderLists.get(position), ImageLoader.getImageListener(imageView, R.drawable.no_img, R.drawable.no_img));
        Picasso.get()
                .load(API.ProductURL+imageSliderLists.get(position))
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(position == 0){
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                } else if(position == 1){
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
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
