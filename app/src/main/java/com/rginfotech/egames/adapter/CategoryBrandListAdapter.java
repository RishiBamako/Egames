package com.rginfotech.egames.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rginfotech.egames.Banner_Redirect;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.fragment.BrandListFragment;
import com.rginfotech.egames.fragment.ProductListFragment;
import com.rginfotech.egames.model.BannerList;
import com.rginfotech.egames.model.CategoryBrandList;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class CategoryBrandListAdapter extends RecyclerView.Adapter {

    private static int TYPE_NORMAL = 1;
    private static int TYPE_BANNER = 2;
    BannerList bannerList;
    Banner_Redirect banner_redirect;
    private LayoutInflater inflater;
    private List<CategoryBrandList> categoryBrandLists;
    //private ImageLoader imageLoader;
    private Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public CategoryBrandListAdapter(Context ctx, List<CategoryBrandList> categoryBrandLists, BannerList bannerList, Banner_Redirect banner_redirect) {
        inflater = LayoutInflater.from(ctx);
        this.categoryBrandLists = categoryBrandLists;
        this.context = ctx;
        this.bannerList = bannerList;
        this.banner_redirect = banner_redirect;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            View view = inflater.inflate(R.layout.banner_list_recycle_item, parent, false);
            MyBannerHolder holder = new MyBannerHolder(view);
            return holder;
        } else {
            View view = inflater.inflate(R.layout.category_randlists_recycler_view_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
    }

   /* @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }*/
    /*
    @Override
    public RecyclerView.ViewHolder.onCreateViewHolder(ViewGroup parent, int viewType) {


    }*/

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int positionn) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();

        if (holder.getItemViewType() == TYPE_BANNER) {
            if (!bannerList.getPath().equals("")) {
                ((MyBannerHolder) holder).banner_image.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(bannerList.getPath())
                        .placeholder(R.drawable.no_img)
                        .error(R.drawable.no_img)
                        .into(((MyBannerHolder) holder).banner_image);

                ((MyBannerHolder) holder).banner_image.getRootView().setOnClickListener(v -> {

                    String is_redirect_deal_otd = bannerList.getIs_redirect_deal_otd();
                    if (!TextUtils.isEmpty(is_redirect_deal_otd) && is_redirect_deal_otd.equals("1")) {
                        banner_redirect.redirect();
                    } else {
                        if (!bannerList.getBrand_id().equals("") && !bannerList.getBrand_id().equals("0")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("brand_id", bannerList.getBrand_id());
                            bundle.putString("brand_name", bannerList.getBrand_name());
                            bundle.putString("from", "");
                            ProductListFragment productListFragment = new ProductListFragment();
                            productListFragment.setArguments(bundle);
                            ((HomeActivity) context).replaceFragment(productListFragment);
              /*  BrandListFragment collectionFragment = new BrandListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category_id",bannerLists.get(position).getCategory_id());
                collectionFragment.setArguments(bundle);
                replaceFragment(collectionFragment);*/
                        } else if (!bannerList.getProduct_id().equals("") && !bannerList.getProduct_id().equals("0")) {
                            Intent intent = new Intent(context, ProductDetailsActivity.class);
                            intent.putExtra("product_id", bannerList.getProduct_id());
                            intent.putExtra("current_currency", "");
                            intent.putExtra("title_name", "");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                        } else if (bannerList.getType().equals("Brand")) {


                        } else if (bannerList.getType().equals("Offer")) {

                        }
                    }
                });
            } else {
                ((MyBannerHolder) holder).banner_image.setVisibility(View.GONE);
            }
        } else {
            String categoryName;
            int position;
            if (positionn > 2)
                position = positionn - 1;
            else
                position = positionn;
            if (Locale.getDefault().getLanguage().equals("en")) {
                categoryName = categoryBrandLists.get(position).getCategoryName().trim();
            } else {
                categoryName = categoryBrandLists.get(position).getCategoryName_ar().trim();
            }
            String[] a = categoryName.split(" ");
            String first = "";
            if (a.length > 1) {
                first = a[0];
            }
            String coloredText = "";
            String restText = "";

            if (!first.equalsIgnoreCase("") && !first.equals("PlayStation")) {
                restText = categoryName.replace(first, "");
                coloredText = "<font color='#FFFFFF'>" + first + "</font>";
                ((MyViewHolder) holder).categoryName.setText(Html.fromHtml(coloredText + restText));
            } else if (first.equals("PlayStation")) {
                ((MyViewHolder) holder).categoryName.setText(categoryName);
            } else {
                ((MyViewHolder) holder).categoryName.setText(categoryName);
            }


     /*   if(Locale.getDefault().getLanguage().equals("en")){
            holder.categoryName.setText(categoryBrandLists.get(position).getCategoryName());
        }else{
            holder.categoryName.setText(categoryBrandLists.get(position).getCategoryName_ar());
        }*/
            ((MyViewHolder) holder).view_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("cat id" + categoryBrandLists.get(position).getCategory_id());
                /*Intent intent =new Intent(context, BrandListActivity.class);
                intent.putExtra("category_id",categoryBrandLists.get(position).getCategory_id());
                context.startActivity(intent);*/
                    BrandListFragment collectionFragment = new BrandListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("category_id", categoryBrandLists.get(position).getCategory_id());
                    collectionFragment.setArguments(bundle);
                    replaceFragment(collectionFragment);
                }
            });
            BrandListAdapter brandListAdapter = new BrandListAdapter(context, categoryBrandLists.get(position).getCategoryBrandList());
            ((MyViewHolder) holder).brand_recycler.setAdapter(brandListAdapter);
            ((MyViewHolder) holder).brand_recycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public int getItemCount() {
        return categoryBrandLists.size() + 1;
    }

    private void replaceFragment(Fragment fragment) {
//        ((AppCompatActivity)context).getSupportActionBar().hide();
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2) {
            return TYPE_BANNER;
        } else
            return TYPE_NORMAL;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        TextView view_all;
        RecyclerView brand_recycler;

        public MyViewHolder(View itemView) {
            super(itemView);
            categoryName = (TextView) itemView.findViewById(R.id.categoryName);
            view_all = (TextView) itemView.findViewById(R.id.view_all);
            brand_recycler = (RecyclerView) itemView.findViewById(R.id.brand_recycler);

        }
    }

    class MyBannerHolder extends RecyclerView.ViewHolder {

        ImageView banner_image;

        public MyBannerHolder(View itemView) {
            super(itemView);
            banner_image = itemView.findViewById(R.id.banner_image);
        }
    }

}
