package com.rginfotech.egames.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.R;
import com.rginfotech.egames.interfacelenzzo.ItemRemoved;
import com.rginfotech.egames.interfacelenzzo.UserCartInterface;
import com.rginfotech.egames.model.CartFamilyModel;
import com.rginfotech.egames.utility.Utils;

import java.util.List;

public class CartParentAdapter extends RecyclerView.Adapter<CartParentAdapter.MyViewHolder> implements ItemRemoved {

    boolean shouldShowPopup = true;
    private LayoutInflater inflater;
    private List<CartFamilyModel> cartFamilyModels;
    private ImageLoader imageLoader;
    private Context context;
    private UserCartInterface userCartInterface;

    public CartParentAdapter(Context ctx, List<CartFamilyModel> cartFamilyModels) {
        inflater = LayoutInflater.from(ctx);
        this.cartFamilyModels = cartFamilyModels;
        this.context = ctx;
        this.userCartInterface = ((UserCartInterface) context);
    }

    public CartParentAdapter(Context ctx, UserCartInterface userCartInterface, List<CartFamilyModel> cartFamilyModels) {
        inflater = LayoutInflater.from(ctx);
        this.cartFamilyModels = cartFamilyModels;
        this.context = ctx;
        this.userCartInterface = userCartInterface;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.single_item_cart_parent_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (cartFamilyModels.get(position).getOfferFlag().equals("1")) {
            holder.offer_layout.setVisibility(View.VISIBLE);
            holder.offer_name.setText(cartFamilyModels.get(position).getOfferName());
        } else {
            holder.offer_layout.setVisibility(View.GONE);
        }

        if (Integer.parseInt(cartFamilyModels.get(position).getDiscountType()) > 0 || Integer.parseInt(cartFamilyModels.get(position).getFreeQuantity()) > 0) {
            holder.offer_tv.setText(context.getResources().getString(R.string.offer) + ":" + cartFamilyModels.get(position).getOfferName());
        } else {
            holder.offer_tv.setText("");
        }

        holder.family_name_tv.setText(cartFamilyModels.get(position).getFamilyName().equals(null) ? "" : cartFamilyModels.get(position).getFamilyName());

        if (cartFamilyModels.get(position).getFamilyName() == null || cartFamilyModels.get(position).getFamilyName().equals("")) {
            holder.family_name_tv.setVisibility(View.GONE);
        }

        holder.total_tv.setText(context.getResources().getString(R.string.total) + ": " + cartFamilyModels.get(position).getTotal());
        holder.product_rv.setAdapter(new UserCartAdapter(context, userCartInterface, cartFamilyModels.get(position).getUserCartModelList(), this::isRemoved));
    }

    @Override
    public int getItemCount() {
        return cartFamilyModels.size();
    }

    @Override
    public void isRemoved(Dialog dialog) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        boolean goFurther = false;
        if (cartFamilyModels.size() > 0) {
            for (int value = 0; value < cartFamilyModels.size(); value++) {
                if (cartFamilyModels.get(value).getUserCartModelList().size() > 0) {
                    goFurther = true;
                }
                if (value == cartFamilyModels.size() - 1) {
                    if (goFurther) {
                        for (int cart = 0; cart < cartFamilyModels.size(); cart++) {
                            for (int child = 0; child < cartFamilyModels.get(cart).getUserCartModelList().size(); child++) {
                                String freeCharge = cartFamilyModels.get(cart).getUserCartModelList().get(child).getFree_delivery();
                                if (freeCharge.equals("0") || TextUtils.isEmpty(freeCharge)) {
                                    shouldShowPopup = false;
                                }
                            }
                            if (cart == cartFamilyModels.size() - 1) {
                                if (shouldShowPopup) {
                                    suggestAboutDeliveryOption();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void suggestAboutDeliveryOption() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_delivery_option);
        TextView shareCodeOnWhatsApp = (TextView) dialog.findViewById(R.id.shareCodeOnWhatsApp_text_view);
        TextView pleaseDeliverIt = (TextView) dialog.findViewById(R.id.pleaseDeliverIt_text_view);
        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancelButton);
        shareCodeOnWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Utils.saveEventStatus("whatsapp", "DELIVERY_OPTION", (Activity) context);
            }
        });
        pleaseDeliverIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Utils.saveEventStatus("deliver", "DELIVERY_OPTION", (Activity) context);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ((Activity) context).finish();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView family_name_tv, offer_tv, total_tv, offer_name;
        RecyclerView product_rv;
        LinearLayout offer_layout;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public MyViewHolder(View itemView) {
            super(itemView);
            family_name_tv = itemView.findViewById(R.id.family_name_tv);
            offer_tv = itemView.findViewById(R.id.offer_tv);
            total_tv = itemView.findViewById(R.id.total_tv);
            offer_name = itemView.findViewById(R.id.offer_name);
            product_rv = itemView.findViewById(R.id.product_rv);
            offer_layout = itemView.findViewById(R.id.offer_layout);
        }
    }


}
