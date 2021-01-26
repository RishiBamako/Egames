package com.rginfotech.egames.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.model.RightPowerModel;

import java.util.List;

public class PowerListAdapter extends RecyclerView.Adapter<PowerListAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<RightPowerModel> rightPowerModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    private boolean isDefaultPowerSelected;
    private List<String> rightPowerlist;

    public PowerListAdapter(Context ctx, List<RightPowerModel> rightPowerModelList,List<String> rightPowerlist){
        inflater = LayoutInflater.from(ctx);
        this.rightPowerModelList = rightPowerModelList;
        this.context=ctx;
        this.rightPowerlist=rightPowerlist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.power_list_with_radio_button1, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PowerListAdapter.MyViewHolder holder, final int position) {
        holder.selected_text.setText(rightPowerModelList.get(position).getValue());

        if(rightPowerModelList.get(position).isOutOfStock()){

            holder.selected_text.setText(rightPowerModelList.get(position).getValue()+" "+context.getResources().getString(R.string.out_of_stock_small));
            holder.selected_text.setTextColor(Color.RED);
            holder.redio_button.setEnabled(false);
            holder.power_layout.setEnabled(false);
        }else{
            holder.selected_text.setText(rightPowerModelList.get(position).getValue());
            holder.selected_text.setTextColor(Color.BLACK);
            holder.redio_button.setEnabled(true);
            holder.power_layout.setEnabled(true);
        }

        if (rightPowerModelList.get(position).isSelected()){
            holder.redio_button.setImageResource(R.drawable.radio_fill_50);
        }else {
            holder.redio_button.setImageResource(R.drawable.radio_50);
        }
        holder.power_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ProductDetailsActivity)context).updatepowerSelectionRight(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rightPowerModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView redio_button;
        TextView selected_text;
        LinearLayout power_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            redio_button = (ImageView)itemView.findViewById(R.id.redio_button);
            selected_text=(TextView)itemView.findViewById(R.id.selected_text);
            power_layout = itemView.findViewById(R.id.power_layout);
        }
    }
}
