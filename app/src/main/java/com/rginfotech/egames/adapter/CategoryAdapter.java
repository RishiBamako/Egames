package com.rginfotech.egames.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.BrandListActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.fragment.BrandListFragment;
import com.rginfotech.egames.model.CategoryList;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<CategoryList> categoryLists;
    private ImageLoader imageLoader;
    private Context context;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public CategoryAdapter(Context ctx, List<CategoryList> categoryLists){
        inflater = LayoutInflater.from(ctx);
        this.categoryLists = categoryLists;
        this.context=ctx;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.category_recycler_view_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(categoryLists.get(position).getCategory_image(), ImageLoader.getImageListener(holder.category_image, R.drawable.no_img, R.drawable.no_img));
        Picasso
                .get()
                .load(categoryLists.get(position).getCategory_image())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                ///.fitCenter()
                .into(holder.category_image);
        holder.category_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent =new Intent(context, BrandListActivity.class);
                intent.putExtra("category_id",categoryLists.get(position).getId());
                context.startActivity(intent);*/

                BrandListFragment collectionFragment = new BrandListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category_id",categoryLists.get(position).getId());
                collectionFragment.setArguments(bundle);
                replaceFragment(collectionFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView category_image;

        public MyViewHolder(View itemView) {
            super(itemView);
            category_image = (ImageView)itemView.findViewById(R.id.category_image);
        }
    }

    private void replaceFragment(Fragment fragment){
        ((AppCompatActivity)context).getSupportActionBar().hide();
        fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.add(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}

