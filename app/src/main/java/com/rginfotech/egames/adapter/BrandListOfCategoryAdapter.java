package com.rginfotech.egames.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.BrandListActivity;
import com.rginfotech.egames.CollectionsActivity;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductListActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.fragment.CollectionFragment;
import com.rginfotech.egames.fragment.ProductListFragment;
import com.rginfotech.egames.model.BrandListOfCategory;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.rginfotech.egames.utility.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrandListOfCategoryAdapter extends RecyclerView.Adapter<BrandListOfCategoryAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<BrandListOfCategory> brandListOfCategoryList;
    private ImageLoader imageLoader;
    private Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String exposeFrom;

    public BrandListOfCategoryAdapter(Context ctx, List<BrandListOfCategory> brandListOfCategoryList, String exposeFrom){
        inflater = LayoutInflater.from(ctx);
        this.brandListOfCategoryList = brandListOfCategoryList;
        this.context=ctx;
        this.exposeFrom = exposeFrom;
    }

    @Override
    public BrandListOfCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.brandlist_of_category_recycle_item, parent, false);
        BrandListOfCategoryAdapter.MyViewHolder holder = new BrandListOfCategoryAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BrandListOfCategoryAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(brandListOfCategoryList.get(position).getBrand_image(), ImageLoader.getImageListener(holder.brand_image, R.drawable.no_img, R.drawable.no_img));

        Picasso.get()
                .load(brandListOfCategoryList.get(position).getBrand_image())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.brand_image);

        holder.brand_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = null;
                if(exposeFrom.equals("BrandList")){
                    from = ((BrandListActivity)context).getIntent().getStringExtra("from");
                }

                if (from != null){
                    if (from.equals("filter")) {
                        Intent intent = new Intent();
                        intent.putExtra("brand_id", brandListOfCategoryList.get(position).getId());
                        ((BrandListActivity) context).setResult(Activity.RESULT_OK, intent);
                        ((BrandListActivity) context).finish();
                    }
                }else if(brandListOfCategoryList.get(position).getJsonArray().length()>0){
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.setCollectionBrandId(brandListOfCategoryList.get(position).getId());
                    if(exposeFrom.equals("BrandList")){

                    Intent intent = new Intent(context, CollectionsActivity.class);
                    intent.putExtra("child_array_list", brandListOfCategoryList.get(position).getJsonArray().toString());
                    context.startActivity(intent);

                        /*CollectionFragment collectionFragment = new CollectionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("child_array_list",brandListOfCategoryList.get(position).getJsonArray().toString());
                        collectionFragment.setArguments(bundle);
                        ((HomeActivity)context).replaceFragment(collectionFragment);*/

                    }else{
                        CollectionFragment collectionFragment = new CollectionFragment(true);
                        Bundle bundle = new Bundle();
                        bundle.putString("child_array_list",brandListOfCategoryList.get(position).getJsonArray().toString());
                        collectionFragment.setArguments(bundle);
                        replaceFragment(collectionFragment);
                    }

                }else{

                    ProductListFragment productListFragment = new ProductListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("brand_id", brandListOfCategoryList.get(position).getId());
                    bundle.putString("brand_name", brandListOfCategoryList.get(position).getName());
                    bundle.putString("from","");
                    //bundle.putString("child_array_list",brandListOfCategoryList.get(position).getJsonArray().toString());
                    productListFragment.setArguments(bundle);
                    ((HomeActivity)context).replaceFragment(productListFragment);

                    /*Intent intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("brand_id", brandListOfCategoryList.get(position).getId());
                    intent.putExtra("brand_name", brandListOfCategoryList.get(position).getName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);*/
                }
            }
        });
        if(!brandListOfCategoryList.get(position).getOffer_id().equals("0") && !brandListOfCategoryList.get(position).getOffer_name().equals("")){
            holder.offer_image.setVisibility(View.VISIBLE);
            holder.offer_text_view.setVisibility(View.VISIBLE);
            holder.offer_text_view.setText(brandListOfCategoryList.get(position).getOffer_subtitle());
        }
    }

    @Override
    public int getItemCount() {
        return brandListOfCategoryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView brand_image;
        ImageView offer_image;
        TextView offer_text_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            brand_image = (ImageView)itemView.findViewById(R.id.brand_image);
            offer_image = (ImageView)itemView.findViewById(R.id.offer_image);
            offer_image.setVisibility(View.GONE);
            offer_text_view = (TextView)itemView.findViewById(R.id.offer_text_view);
            offer_text_view.setVisibility(View.GONE);
        }
    }


    private void replaceFragment(Fragment fragment){
       // ((AppCompatActivity)context).getSupportActionBar().hide();
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
