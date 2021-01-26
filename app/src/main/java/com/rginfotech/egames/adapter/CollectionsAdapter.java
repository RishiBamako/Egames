package com.rginfotech.egames.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.CollectionsActivity;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductListActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.fragment.CollectionFragment;
import com.rginfotech.egames.model.ChildModel;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<ChildModel> childModelList;
    private ImageLoader imageLoader;
    private Context context;

    public CollectionsAdapter(Context ctx, List<ChildModel> childModelList){
        inflater = LayoutInflater.from(ctx);
        this.childModelList = childModelList;
        this.context=ctx;
    }

    @Override
    public CollectionsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.collections_recycler_view_item, parent, false);
        CollectionsAdapter.MyViewHolder holder = new CollectionsAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CollectionsAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(childModelList.get(position).getImage(), ImageLoader.getImageListener(holder.child_image, R.drawable.no_img, R.drawable.no_img));

        Picasso.get()
                .load(childModelList.get(position).getImage())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.child_image);

        holder.child_name.setText(childModelList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(childModelList.get(position).getJsonArray());
                if(childModelList.get(position).getJsonArray().length()>0){
                    /*Intent intent = new Intent(context, CollectionsActivity.class);
                    intent.putExtra("child_array_list", childModelList.get(position).getJsonArray().toString());
                    context.startActivity(intent);*/
                    CollectionFragment collectionFragment = new CollectionFragment(true);
                    Bundle bundle = new Bundle();
                    bundle.putString("child_array_list",childModelList.get(position).getJsonArray().toString());
                    collectionFragment.setArguments(bundle);
                    ((HomeActivity)context).replaceFragment(collectionFragment);
                }else{
                    Intent intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("brand_id", childModelList.get(position).getReference());
                    intent.putExtra("brand_name", childModelList.get(position).getName());
                    intent.putExtra("family_id",childModelList.get(position).getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            }
        });
        if(!childModelList.get(position).getOffer_id().equals("0") && !childModelList.get(position).getOffer_name().equals("")){
            holder.offer_text_view.setVisibility(View.VISIBLE);
            holder.offer_text_view.setText(childModelList.get(position).getOffer_name());
        }


    }

    @Override
    public int getItemCount() {
        return childModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView child_image;
        TextView child_name;
        TextView offer_text_view;

        public MyViewHolder(View itemView) {
            super(itemView);
            child_name = (TextView)itemView.findViewById(R.id.child_name);
            child_image = (ImageView)itemView.findViewById(R.id.child_image);
            offer_text_view = (TextView)itemView.findViewById(R.id.offer_text_view);
            offer_text_view.setVisibility(View.GONE);
        }
    }
}
