package com.rginfotech.egames.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.CollectionsActivity;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.fragment.CollectionFragment;
import com.rginfotech.egames.fragment.ProductListFragment;
import com.rginfotech.egames.model.OffersModel;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.rginfotech.egames.utility.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder>{


    private LayoutInflater inflater;
    private List<OffersModel> offersModelList;
    private ImageLoader imageLoader;
    private Context context;

    public OffersAdapter(Context ctx, List<OffersModel> offersModelList){
        inflater = LayoutInflater.from(ctx);
        this.offersModelList = offersModelList;
        this.context=ctx;
    }

    @Override
    public OffersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_list_recycler_view_item, parent, false);
        OffersAdapter.MyViewHolder holder = new OffersAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OffersAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(offersModelList.get(position).getBrand_image(), ImageLoader.getImageListener(holder.image, R.drawable.no_img, R.drawable.no_img));
        Picasso.get()
                .load(offersModelList.get(position).getBrand_image())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************By Ajay****************/
                /*if(offersModelList.get(position).getChild_json_array().length()>0){
                    Intent intent = new Intent(context, CollectionsActivity.class);
                    intent.putExtra("child_array_list", offersModelList.get(position).getChild_json_array().toString());
                    context.startActivity(intent);
                }else {
                    *//*Intent intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("brand_id", offersModelList.get(position).getId());
                    intent.putExtra("brand_name", offersModelList.get(position).getName());
                    context.startActivity(intent);*//*
                }*/
                /************By Vikas****************/
                Bundle bundle = new Bundle();
                bundle.putString("brand_id", offersModelList.get(position).getId());
                bundle.putString("brand_name", offersModelList.get(position).getName());
                bundle.putString("family_id", offersModelList.get(position).getFamilyId());
                bundle.putString("from","");
                ProductListFragment productListFragment = new ProductListFragment();
                productListFragment.setArguments(bundle);
                ((HomeActivity)context).replaceFragment(productListFragment);
                /*if(offersModelList.get(position).getChild_json_array().length()>0){
                    *//*Intent intent = new Intent(context, CollectionsActivity.class);
                    intent.putExtra("child_array_list", brandLists.get(position).getJsonArray().toString());
                    context.startActivity(intent);*//*
                    SessionManager sessionManager = new SessionManager(context);
                    sessionManager.setCollectionBrandId(offersModelList.get(position).getId());
                    CollectionFragment collectionFragment = new CollectionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("child_array_list",offersModelList.get(position).getChild_json_array().toString());
                    collectionFragment.setArguments(bundle);
                    ((HomeActivity)context).replaceFragment(collectionFragment);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString("brand_id", offersModelList.get(position).getId());
                    bundle.putString("brand_name", offersModelList.get(position).getName());
                    bundle.putString("from","");
                    ProductListFragment productListFragment = new ProductListFragment();
                    productListFragment.setArguments(bundle);
                    ((HomeActivity)context).replaceFragment(productListFragment);*/

                    /*Intent intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("brand_id", brandLists.get(position).getId());
                    intent.putExtra("brand_name", brandLists.get(position).getName());
                    context.startActivity(intent);
                }*/
            }
        });
        if(!TextUtils.isEmpty(offersModelList.get(position).getOffer_subtitle())){
            holder.offer_image.setVisibility(View.VISIBLE);
   //         holder.offer_text_view.setVisibility(View.VISIBLE);
       //     holder.offer_text_view.setText(offersModelList.get(position).getOffer_subtitle());
        }else {
            holder.offer_image.setVisibility(View.GONE);
     //       holder.offer_text_view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return offersModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageView offer_image;
        TextView offer_text_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.product_image);
            offer_image = (ImageView)itemView.findViewById(R.id.product_image);
     /*       offer_text_view = (TextView)itemView.findViewById(R.id.offer_text_view);
            offer_text_view.setVisibility(View.GONE);*/
        }
    }
}
