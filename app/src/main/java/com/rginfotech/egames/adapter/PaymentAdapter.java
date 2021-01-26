package com.rginfotech.egames.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.R;
import com.rginfotech.egames.interfacelenzzo.PaymentInterface;
import com.rginfotech.egames.model.PaymentModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<PaymentModel> paymentModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    private PaymentInterface paymentInterface;

    public PaymentAdapter(Context ctx, List<PaymentModel> paymentModelList) {
        inflater = LayoutInflater.from(ctx);
        this.paymentModelList = paymentModelList;
        this.context = ctx;
        this.paymentInterface = ((PaymentInterface) context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.payment_recycler_view_item, parent, false);
        //MyViewHolder holder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(paymentModelList.get(position).getLogo(), ImageLoader.getImageListener(holder.logo, R.drawable.no_img, R.drawable.no_img));

        Picasso.get()
                .load(paymentModelList.get(position).getLogo())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.logo);


        Log.e("data", paymentModelList.get(position).getLogo());

        holder.radioButton.setChecked(paymentModelList.get(position).isSelected());

        if (context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            holder.payment_text.setText(paymentModelList.get(position).getName());
        } else {
            holder.payment_text.setText(paymentModelList.get(position).getName_ar());
        }
    }

    @Override
    public int getItemCount() {
        return paymentModelList.size();
    }

    private void clickManipulation(int adapterPosition) {
        for (int p = 0; p < paymentModelList.size(); p++) {
            if (p == adapterPosition) {
                paymentModelList.get(p).setSelected(true);
            } else {
                paymentModelList.get(p).setSelected(false);
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView payment_text;
        ImageView logo;
        LinearLayout pay_option_lay;

        public MyViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView.findViewById(R.id.radioButton);
            payment_text = (TextView) itemView.findViewById(R.id.payment_text);
            pay_option_lay = itemView.findViewById(R.id.pay_option_lay);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    paymentInterface.paymentMode(paymentModelList.get(lastSelectedPosition).getId(), paymentModelList.get(lastSelectedPosition).getName());
                    clickManipulation(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            pay_option_lay.setOnClickListener(view -> {
                lastSelectedPosition = getAdapterPosition();
                paymentInterface.paymentMode(paymentModelList.get(lastSelectedPosition).getId(), paymentModelList.get(lastSelectedPosition).getName());
                clickManipulation(getAdapterPosition());
                notifyDataSetChanged();
            });
        }
    }
}
