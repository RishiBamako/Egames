package com.rginfotech.egames.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.CommanSocialActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.model.SocialModel;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<SocialModel> socialModelList;
    private ImageLoader imageLoader;
    private Context context;

    public SocialAdapter(Context ctx, List<SocialModel> socialModelList){
        inflater = LayoutInflater.from(ctx);
        this.socialModelList = socialModelList;
        this.context=ctx;
    }

    @Override
    public SocialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.social_recycler_view_item, parent, false);
        SocialAdapter.MyViewHolder holder = new SocialAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SocialAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(socialModelList.get(position).getImage(), ImageLoader.getImageListener(holder.social_image, R.drawable.no_img, R.drawable.no_img));

        Picasso.get()
                .load(socialModelList.get(position).getImage())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.social_image);

        holder.social_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommanSocialActivity.class);
                intent.putExtra("url",socialModelList.get(position).getUrl());
                intent.putExtra("title",socialModelList.get(position).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return socialModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView social_image;
        public MyViewHolder(View itemView) {
            super(itemView);
            social_image = (ImageView)itemView.findViewById(R.id.social_image);
        }
    }

}
