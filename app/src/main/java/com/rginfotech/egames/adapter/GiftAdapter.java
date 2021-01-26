package com.rginfotech.egames.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.interfacelenzzo.GiftInterface;
import com.rginfotech.egames.R;
import com.rginfotech.egames.model.GiftModel;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<GiftModel> giftModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    GiftInterface giftInterface;

    public GiftAdapter(Context ctx, List<GiftModel> giftModelList){
        inflater = LayoutInflater.from(ctx);
        this.giftModelList = giftModelList;
        this.context=ctx;
        giftInterface = ((GiftInterface)context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.gift_recycler_view_item, parent, false);
        //MyViewHolder holder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(giftModelList.get(position).getImage(), ImageLoader.getImageListener(holder.gift_image, R.drawable.no_img, R.drawable.no_img));

        Picasso.get()
                .load(giftModelList.get(position).getImage())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.gift_image);

        holder.gift_name.setText(giftModelList.get(position).getTitle());
        holder.gift_description.setText(giftModelList.get(position).getDescription());
        holder.redia_button.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return giftModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView gift_image;
        RadioButton redia_button;
        TextView gift_name;
        TextView gift_description;
        LinearLayout single_gift_lay;

        public MyViewHolder(View itemView) {
            super(itemView);
            gift_image = itemView.findViewById(R.id.gift_image);
            redia_button = itemView.findViewById(R.id.redia_button);
            gift_name = itemView.findViewById(R.id.gift_name);
            gift_description = itemView.findViewById(R.id.gift_description);
            single_gift_lay = itemView.findViewById(R.id.single_gift_lay);
            redia_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    giftInterface.getGiftId(giftModelList.get(lastSelectedPosition).getId());
                    notifyDataSetChanged();
                }
            });

            single_gift_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    giftInterface.getGiftId(giftModelList.get(lastSelectedPosition).getId());
                    notifyDataSetChanged();
                }
            });
        }
    }

}
