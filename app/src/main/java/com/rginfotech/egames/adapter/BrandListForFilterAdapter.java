package com.rginfotech.egames.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rginfotech.egames.BrandListForFilterActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.model.BrandListOfCategory;
import com.rginfotech.egames.utility.Utils;

import java.util.List;

public class BrandListForFilterAdapter extends RecyclerView.Adapter<BrandListForFilterAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<BrandListOfCategory> brandListOfCategoryList;
    private Context context;

    public BrandListForFilterAdapter(Context ctx, List<BrandListOfCategory> brandListOfCategoryList) {
        inflater = LayoutInflater.from(ctx);
        this.brandListOfCategoryList = brandListOfCategoryList;
        this.context = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_item_brand_name_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.brand_tv.setText(brandListOfCategoryList.get(position).getName());
        if (brandListOfCategoryList.get(position).isSelected()) {
            holder.radio_iv.setImageResource(R.drawable.slect_100);
        } else {
            holder.radio_iv.setImageResource(R.drawable.de_select_100);
        }
        holder.brand_lay.setOnClickListener(view -> {

            if (Utils.brandsIs.size() > 0) {
                String brandId = brandListOfCategoryList.get(position).getId();
                if (Utils.brandsIs.contains(brandId)) {
                    Utils.brandsIs.remove(brandId);
                } else {
                    Utils.brandsIs.add(brandId);
                }
            } else {
                Utils.brandsIs.add(brandListOfCategoryList.get(position).getId());
            }
            ((BrandListForFilterActivity) context).selectBrand(position);

        });

    }

    @Override
    public int getItemCount() {
        return brandListOfCategoryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView radio_iv;
        TextView brand_tv;
        LinearLayout brand_lay;

        public MyViewHolder(View itemView) {
            super(itemView);
            radio_iv = itemView.findViewById(R.id.radio_iv);
            brand_tv = itemView.findViewById(R.id.brand_tv);
            brand_lay = itemView.findViewById(R.id.brand_lay);

        }
    }


}
