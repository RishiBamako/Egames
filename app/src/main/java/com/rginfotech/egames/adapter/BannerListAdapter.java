package com.rginfotech.egames.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.fragment.BrandListFragment;
import com.rginfotech.egames.fragment.ProductListFragment;
import com.rginfotech.egames.model.BannerList;
import com.rginfotech.egames.utility.CustomVolleyRequest;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerListAdapter extends RecyclerView.Adapter<BannerListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<BannerList> bannerLists;
    private ImageLoader imageLoader;
    private Context context;

    public BannerListAdapter(Context ctx, List<BannerList> bannerLists){
        inflater = LayoutInflater.from(ctx);
        this.bannerLists = bannerLists;
        this.context=ctx;
    }

    @Override
    public BannerListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.banner_list_recycle_item, parent, false);
        BannerListAdapter.MyViewHolder holder = new BannerListAdapter.MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(BannerListAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(bannerLists.get(position).getPath(), ImageLoader.getImageListener(holder.banner_image, R.drawable.no_img, R.drawable.no_img));
        Picasso.get()
                .load(bannerLists.get(position).getPath())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.banner_image);

        holder.banner_image.getRootView().setOnClickListener(v -> {
            if (!bannerLists.get(position).getBrand_id().equals("") && !bannerLists.get(position).getBrand_id().equals("0")){
                Bundle bundle = new Bundle();
                bundle.putString("brand_id", bannerLists.get(position).getBrand_id());
                bundle.putString("brand_name", bannerLists.get(position).getBrand_name());
                bundle.putString("from","");
                ProductListFragment productListFragment = new ProductListFragment();
                productListFragment.setArguments(bundle);
                ((HomeActivity)context).replaceFragment(productListFragment);
              /*  BrandListFragment collectionFragment = new BrandListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category_id",bannerLists.get(position).getCategory_id());
                collectionFragment.setArguments(bundle);
                replaceFragment(collectionFragment);*/
            }

            else if (!bannerLists.get(position).getProduct_id().equals("")&&!bannerLists.get(position).getProduct_id().equals("0")){
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id",bannerLists.get(position).getProduct_id());
                intent.putExtra("current_currency","");
                intent.putExtra("title_name","");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }else if (bannerLists.get(position).getType().equals("Brand")){


            }else if (bannerLists.get(position).getType().equals("Offer")){

            }


        });
    }
    @Override
    public int getItemCount() {
       // return bannerLists.size();
      return 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView banner_image;


        public MyViewHolder(View itemView) {
            super(itemView);
            banner_image = itemView.findViewById(R.id.banner_image);
        }
    }

    private void replaceFragment(Fragment fragment){
//        ((AppCompatActivity)context).getSupportActionBar().hide();
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

}
