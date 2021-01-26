package com.rginfotech.egames.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.rginfotech.egames.CollectionsActivity;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductListActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.fragment.CollectionFragment;
import com.rginfotech.egames.fragment.ProductListFragment;
import com.rginfotech.egames.model.BrandList;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.rginfotech.egames.utility.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BrandListAdapter extends RecyclerView.Adapter<BrandListAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<BrandList> brandLists;
    //private ImageLoader imageLoader;
    private Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public BrandListAdapter(Context ctx, List<BrandList> brandLists){
        inflater = LayoutInflater.from(ctx);
        this.brandLists = brandLists;
        this.context=ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.brand_list_recycle_item_home, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(brandLists.get(position).getBrand_image(), ImageLoader.getImageListener(holder.image, R.drawable.no_img, R.drawable.no_img));
        Picasso.get()
                .load(brandLists.get(position).getBrand_image())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(brandLists.get(position).getJsonArray().length()>0){
                    /*Intent intent = new Intent(context, CollectionsActivity.class);
                    intent.putExtra("child_array_list", brandLists.get(position).getJsonArray().toString());
                    context.startActivity(intent);*/
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.setCollectionBrandId(brandLists.get(position).getId());
                    CollectionFragment collectionFragment = new CollectionFragment(true);
                    Bundle bundle = new Bundle();
                    bundle.putString("child_array_list",brandLists.get(position).getJsonArray().toString());
                    collectionFragment.setArguments(bundle);
                    replaceFragment(collectionFragment);
                }  else {
                    Bundle bundle = new Bundle();
                    bundle.putString("brand_id", brandLists.get(position).getId());
                    bundle.putString("brand_name", brandLists.get(position).getName());
                    bundle.putString("from","");
                    ProductListFragment productListFragment = new ProductListFragment();
                    productListFragment.setArguments(bundle);
                    ((HomeActivity)context).replaceFragment(productListFragment);

                    /*Intent intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("brand_id", brandLists.get(position).getId());
                    intent.putExtra("brand_name", brandLists.get(position).getName());
                    context.startActivity(intent);*/
                }
            }
        });

        if(!brandLists.get(position).getOffer_id().equals("0") && !brandLists.get(position).getOffer_name().equals("")){
            holder.offer_image.setVisibility(View.VISIBLE);
            holder.offer_text_view.setVisibility(View.VISIBLE);
            holder.offer_text_view.setText(brandLists.get(position).getOffer_subtitle());
        }
    }

    @Override
    public int getItemCount() {
        return brandLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageView offer_image;
        TextView offer_text_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            offer_image = (ImageView)itemView.findViewById(R.id.offer_image);
            offer_image.setVisibility(View.GONE);
            offer_text_view = (TextView)itemView.findViewById(R.id.offer_text_view);
            offer_text_view.setVisibility(View.GONE);
        }
    }

    private void replaceFragment(Fragment fragment){
//      ((AppCompatActivity)context).getSupportActionBar().hide();
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
