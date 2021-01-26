package com.rginfotech.egames.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.interfacelenzzo.ItemRemoved;
import com.rginfotech.egames.interfacelenzzo.UserCartInterface;
import com.rginfotech.egames.model.UserCartModel;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<UserCartModel> userCartModelList;
    private ImageLoader imageLoader;
    private Context context;
    private UserCartInterface userCartInterface;
    ItemRemoved itemRemoved;

    public UserCartAdapter(Context ctx,UserCartInterface userCartInterface, List<UserCartModel> userCartModelList,ItemRemoved itemRemoved) {
        inflater = LayoutInflater.from(ctx);
        this.userCartModelList = userCartModelList;
        this.context = ctx;
        this.userCartInterface = userCartInterface;
        this.itemRemoved = itemRemoved;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public UserCartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.user_cart_recycler_view_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        /*******************By Vikas***************************/
        if (userCartModelList.get(position).getQuantityLeft().equals("0") && userCartModelList.get(position).getQuantityRight().equals("0")) {
            holder.left_qty_text_tv.setText(context.getResources().getString(R.string.qty));
            holder.right_qty_lay.setVisibility(View.GONE);
            //holder.left_price_tv.setText(context.getResources().getString(R.string.left_lense_price)+" "+userCartModelList.get(position).getBasePriceLeft());
            holder.left_count_tv.setText(userCartModelList.get(position).getQuantity());
            holder.left_price_tv.setText(context.getResources().getString(R.string.price) + ": " + userCartModelList.get(position).getPrice() + " " + userCartModelList.get(position).getCurrent_currency());
            holder.right_price_tv.setVisibility(View.GONE);
        } else {
            if (!userCartModelList.get(position).getQuantityLeft().equals("0") && !userCartModelList.get(position).getQuantityLeft().equals("null")) {
                holder.left_qty_text_tv.setText(context.getResources().getString(R.string.left_qty));
                holder.left_count_tv.setText(userCartModelList.get(position).getQuantityLeft());
                holder.left_price_tv.setText(context.getResources().getString(R.string.left_lense_price) + " " + userCartModelList.get(position).getBasePriceLeft() + " " + userCartModelList.get(position).getCurrent_currency());
                holder.left_qty_lay.setVisibility(View.VISIBLE);
                holder.left_price_tv.setVisibility(View.VISIBLE);
            } else {
                holder.left_qty_lay.setVisibility(View.GONE);
                holder.left_price_tv.setVisibility(View.GONE);
            }

            if (!userCartModelList.get(position).getQuantityRight().equals("0") && !userCartModelList.get(position).getQuantityRight().equals("null")) {
                holder.right_count_tv.setText(userCartModelList.get(position).getQuantityRight());
                holder.right_price_tv.setText(context.getResources().getString(R.string.right_lense_price) + " " + userCartModelList.get(position).getBasePriceRight() + " " + userCartModelList.get(position).getCurrent_currency());
                holder.right_qty_lay.setVisibility(View.VISIBLE);
                holder.right_price_tv.setVisibility(View.VISIBLE);
            } else {
                holder.right_qty_lay.setVisibility(View.GONE);
                holder.right_price_tv.setVisibility(View.GONE);
            }
            //holder.left_qty_text_tv.setText(context.getResources().getString(R.string.left_qty));
            //holder.right_qty_lay.setVisibility(View.VISIBLE);
        }

        String quantity = userCartModelList.get(position).getQuantity();
        String productQuantity = userCartModelList.get(position).getProduct_quantity();
        if (!TextUtils.isEmpty(quantity) && !TextUtils.isEmpty(productQuantity)) {
            int quantityInt = Integer.parseInt(quantity);
            int productQuantityInt = Integer.parseInt(productQuantity);
            if (quantityInt > productQuantityInt) {
                userCartModelList.get(position).setOutOfStock(true);
                holder.out_of_stock.setVisibility(View.VISIBLE);
            } else {
                holder.out_of_stock.setVisibility(View.GONE);
                userCartModelList.get(position).setOutOfStock(false);
            }
        }
        /**********************************************************/
        Picasso.get()
                .load(userCartModelList.get(position).getProduct_image())
                .placeholder(R.drawable.ic_banner_default)
                .error(R.drawable.no_img)
                .fit()
                .into(holder.image);


        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(userCartModelList.get(position).getProduct_image(), ImageLoader.getImageListener(holder.image, R.drawable.no_img, R.drawable.no_img));
        holder.item_text_view.setText(context.getResources().getString(R.string.item) + " " + String.valueOf(position + 1));
        Float price = Float.valueOf(userCartModelList.get(position).getTotal());
        //float price = Float.parseFloat(userCartModelList.get(position).getTotal());
        Float qty = Float.valueOf(userCartModelList.get(position).getQuantity());
        //String total = String.format("%.2f",price*qty);
        if (context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            BigDecimal bd = CommanMethod.getCountryWiseDecimalNumberFloatInput(context, price);
            //String total = String.format("%.2f",price);
            holder.total_text_view.setText(context.getResources().getString(R.string.total) + " : " + bd + " " + userCartModelList.get(position).getCurrent_currency());
            holder.cart_total_price_value.setText(bd + " " + userCartModelList.get(position).getCurrent_currency());
        } else {
            //BigDecimal bd = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bd = CommanMethod.getCountryWiseDecimalNumberFloatInput(context, price);
            holder.total_text_view.setText(context.getResources().getString(R.string.total) + " : " + bd + " " + userCartModelList.get(position).getCurrent_currency());
            holder.cart_total_price_value.setText(bd + " " + userCartModelList.get(position).getCurrent_currency());

        }

        holder.name.setText(userCartModelList.get(position).getProduct_name());
        //holder.price.setText(total+" "+userCartModelList.get(position).getCurrent_currency());
        //holder.left_price_tv.setText(userCartModelList.get(position).getPrice()+" "+userCartModelList.get(position).getCurrent_currency());

        //value = value+Float.parseFloat(total);
     /*   if (CommanMethod.isOutOfStock(userCartModelList.get(position).getStock_flag(), userCartModelList.get(position).getProduct_quantity())) {
            holder.out_of_stock.setVisibility(View.VISIBLE);
        } else {
            holder.out_of_stock.setVisibility(View.GONE);
        }*/
        holder.left_dec_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int leftQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityLeft());
                int totalQuantity = Integer.parseInt(userCartModelList.get(position).getQuantity());
                if (Integer.parseInt(holder.left_count_tv.getText().toString()) > 1) {
                    if (leftQuantity > 1) {
                        leftQuantity = leftQuantity - 1;
                    }
                    totalQuantity = totalQuantity - 1;
                    int rightQuantity;
                    try {
                        rightQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityRight());
                    } catch (Exception ex) {
                        rightQuantity = 0;
                    }
                    updateCartItem(userCartModelList.get(position).getId(), totalQuantity, leftQuantity, rightQuantity, position);
                }
                //holder.number.setText(String.valueOf(value));
            }
        });

        holder.left_plus_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantity = userCartModelList.get(position).getQuantity();
                String productQuantity = userCartModelList.get(position).getProduct_quantity();
                if (!TextUtils.isEmpty(quantity) && !TextUtils.isEmpty(productQuantity)) {
                    int quantityInt = Integer.parseInt(quantity);
                    int productQuantityInt = Integer.parseInt(productQuantity);
                    if (quantityInt >=productQuantityInt) {
                        dialogBox();
                    } else {
                        holder.out_of_stock.setVisibility(View.GONE);

                        int leftQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityLeft());
                        int totalQuantity = Integer.parseInt(userCartModelList.get(position).getQuantity());

                        leftQuantity = leftQuantity + 1;
                        totalQuantity = totalQuantity + 1;
                        int rightQuantity;
                        try {
                            rightQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityRight());
                        } catch (Exception ex) {
                            rightQuantity = 0;
                        }
                        //int rightQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityRight());
                        updateCartItem(userCartModelList.get(position).getId(), totalQuantity, leftQuantity, rightQuantity, position);
                    }
                }






                /*int leftQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityLeft());
                int totalQuantity = Integer.parseInt(userCartModelList.get(position).getQuantity());
                if (!userCartModelList.get(position).getStock_flag().equals("0")) {
                    leftQuantity = leftQuantity + 1;
                    totalQuantity = totalQuantity + 1;
                    int rightQuantity;
                    try {
                        rightQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityRight());
                    } catch (Exception ex) {
                        rightQuantity = 0;
                    }
                    //int rightQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityRight());
                    updateCartItem(userCartModelList.get(position).getId(), totalQuantity, leftQuantity, rightQuantity, position);
                } else {

                    dialogBox();
                }*/
            }
        });

        holder.right_dec_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rightQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityRight());

                int totalQuantity = Integer.parseInt(userCartModelList.get(position).getQuantity());
                if (Integer.parseInt(holder.right_count_tv.getText().toString()) > 1) {
                    //rightQuantity=rightQuantity-1;
                    if (rightQuantity > 1) {
                        rightQuantity = rightQuantity - 1;
                    }
                    totalQuantity = totalQuantity - 1;
                    int leftQuantity;
                    try {
                        leftQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityLeft());
                    } catch (Exception ex) {
                        leftQuantity = 0;
                    }
                    //int leftQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityLeft());
                    updateCartItem(userCartModelList.get(position).getId(), totalQuantity, leftQuantity, rightQuantity, position);
                }
                //holder.number.setText(String.valueOf(value));
            }
        });
        holder.right_plus_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int rightQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityRight());

                int totalQuantity = Integer.parseInt(userCartModelList.get(position).getQuantity());
                if (!userCartModelList.get(position).getStock_flag().equals("0")) {
                    rightQuantity = rightQuantity + 1;
                    totalQuantity = totalQuantity + 1;
                    int leftQuantity;
                    try {
                        leftQuantity = Integer.parseInt(userCartModelList.get(position).getQuantityLeft());
                    } catch (Exception ex) {
                        leftQuantity = 0;
                    }

                    updateCartItem(userCartModelList.get(position).getId(), totalQuantity, leftQuantity, rightQuantity, position);
                } else {

                    dialogBox();
                }
            }
        });

        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String fdf = context.getResources().getString(R.string.item_delete_message);
                dialogBox(userCartModelList.get(position).getId(), position, context.getResources().getString(R.string.item_delete_message));
            }
        });

        holder.move_to_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveWishListDialogBox(position, userCartModelList.get(position).getId(), context.getResources().getString(R.string.move_to_wish_list));
            }
        });

        holder.cart_item_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id", userCartModelList.get(position).getProduct_id());
                intent.putExtra("current_currency", "");
                intent.putExtra("title_name", "");
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });


       /*String powerText = "";
        if (!(userCartModelList.get(position).getLeft_eye_power().equals("") || userCartModelList.get(position).getLeft_eye_power().equals("null"))){
            powerText = context.getResources().getString(R.string.left_eye_power)+userCartModelList.get(position).getLeft_eye_power();
        }else {
            powerText = "";
        }

        if (!(userCartModelList.get(position).getRight_eye_power().equals("") || userCartModelList.get(position).getRight_eye_power().equals("null"))){
            if (TextUtils.isEmpty(powerText)){
                powerText = context.getResources().getString(R.string.right_eye_power)+userCartModelList.get(position).getRight_eye_power();
            }else {
                powerText = ", "+context.getResources().getString(R.string.right_eye_power)+userCartModelList.get(position).getRight_eye_power();
            }

        }
        holder.text_power.setText(powerText);
        if(TextUtils.isEmpty(powerText)){
            holder.text_power.setVisibility(View.GONE);
            //holder.text_power.setText(context.getResources().getString(R.string.left_eye_power)+userCartModelList.get(position).getLeft_eye_power()+", "+context.getResources().getString(R.string.right_eye_power)+userCartModelList.get(position).getRight_eye_power());
        }else {
            holder.text_power.setVisibility(View.VISIBLE);
        }*/

    /*    if(!userCartModelList.get(position).getLeft_eye_power().equals("")
                && !userCartModelList.get(position).getLeft_eye_power().equals("null")
                && !userCartModelList.get(position).getRight_eye_power().equals("")
                && !userCartModelList.get(position).getRight_eye_power().equals("null")){
            holder.text_power.setVisibility(View.VISIBLE);
            holder.text_power.setText(context.getResources().getString(R.string.right_eye_power)+" "+userCartModelList.get(position).getRight_eye_power()+", "+context.getResources().getString(R.string.left_eye_power)+" "+userCartModelList.get(position).getLeft_eye_power());
        }else {
            String powerText = "";

            if(!userCartModelList.get(position).getRight_eye_power().equals("")
                    && !userCartModelList.get(position).getRight_eye_power().equals("null")){
                powerText = context.getResources().getString(R.string.left_eye_power)+" "+userCartModelList.get(position).getRight_eye_power();
            }
            if (!userCartModelList.get(position).getLeft_eye_power().equals("")
                    && !userCartModelList.get(position).getLeft_eye_power().equals("null")){
                if (powerText.trim().length() == 0) {
                    powerText = context.getResources().getString(R.string.left_eye_power) + " " + userCartModelList.get(position).getLeft_eye_power();
                }else {
                    powerText = powerText+ " "+context.getResources().getString(R.string.left_eye_power) + " " + userCartModelList.get(position).getLeft_eye_power();
                }

            }

            holder.text_power.setText(powerText);

            if (powerText.trim().length() == 0){
                holder.text_power.setVisibility(View.GONE);
            }




        }*/

    }

    @Override
    public int getItemCount() {
        return userCartModelList.size();
    }

    private void dialogBox(final String user_cart_id, final int posi, String info) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.comman_alert_dialog_yes_no);
        TextView info_message = dialog.findViewById(R.id.item_text_view);
        info_message.setText(info);
        TextView continue_text_view = dialog.findViewById(R.id.change_language_yes);
        TextView go_to_text_view = dialog.findViewById(R.id.change_language_no);
        continue_text_view.setOnClickListener(v -> {
            dialog.dismiss();
            deleteCartItem(user_cart_id, posi);
        });
        go_to_text_view.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void deleteCartItem(final String user_cart_id, final int posi) {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart_delete", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        System.out.println("success");
                        removeAt(posi);
                        successdialogBox(context.getResources().getString(R.string.success), context.getResources().getString(R.string.item_deleted),"DELETE");
                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_cart_id", user_cart_id);
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    public void removeAt(int position) {
        userCartModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userCartModelList.size());
        userCartInterface.getUserCartValue();
    }

    public void updateAt(int position, int quantity) {
        userCartModelList.get(position).setQuantity(String.valueOf(quantity));
        userCartInterface.getUserCartValue();
        notifyItemChanged(position);
    }

    private void successdialogBox(String title, String info,String isfrom) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.comman_alert_dialog_box);
        TextView alerte_title = (TextView) dialog.findViewById(R.id.title_box);
        alerte_title.setText(title);
        TextView message = (TextView) dialog.findViewById(R.id.info_tv);
        message.setText(info);
        TextView ok_tv = (TextView) dialog.findViewById(R.id.ok_tv);

        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(isfrom.equalsIgnoreCase("DELETE")){
                    itemRemoved.isRemoved(dialog);
                }
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void updateCartItem(final String user_cart_id, final int totalQuantity, final int leftQuantity, final int rightQuantity, final int position) {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(context);
        dialog.setCancelable(true);
        dialog.show();
        final SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart_update", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        //holder.number.setText(String.valueOf(value));
                        updateAt(position, leftQuantity);
                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_cart_id", user_cart_id);
                //params.put("user_id",sessionManager.getUserId());
                //params.put("quantity",String.valueOf(quantity));
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }

                if ((userCartModelList.get(position).getLeft_eye_power().equals("") || userCartModelList.get(position).getLeft_eye_power().equals("null"))
                        && (userCartModelList.get(position).getRight_eye_power().equals("") || userCartModelList.get(position).getRight_eye_power().equals("null"))) {
                    params.put("quantity", String.valueOf(totalQuantity));
                } else {

                    if (!((userCartModelList.get(position).getLeft_eye_power().equals("") || userCartModelList.get(position).getLeft_eye_power().equals("null"))
                            && (userCartModelList.get(position).getRight_eye_power().equals("") || userCartModelList.get(position).getRight_eye_power().equals("null")))) {

                        if (userCartModelList.get(position).getLeft_eye_power().equals(userCartModelList.get(position).getRight_eye_power())) {
                            params.put("quantity", String.valueOf(totalQuantity));
                        } else {
                            params.put("quantity", String.valueOf(totalQuantity));
                        }

                        params.put("quantity_left", String.valueOf(leftQuantity));
                        params.put("quantity_right", String.valueOf(rightQuantity));
                        params.put("left_eye_power", userCartModelList.get(position).getLeft_eye_power());
                        params.put("right_eye_power", userCartModelList.get(position).getRight_eye_power());
                    } else {
                        if (!(userCartModelList.get(position).getLeft_eye_power().equals("") || userCartModelList.get(position).getLeft_eye_power().equals("null"))) {
                            params.put("quantity", String.valueOf(totalQuantity));
                            params.put("quantity_left", String.valueOf(leftQuantity));
                            params.put("left_eye_power", userCartModelList.get(position).getLeft_eye_power());
                        }

                        if (!(userCartModelList.get(position).getRight_eye_power().equals("") || userCartModelList.get(position).getRight_eye_power().equals("null"))) {
                            params.put("quantity", String.valueOf(totalQuantity));
                            params.put("quantity_right", String.valueOf(rightQuantity));
                            params.put("right_eye_power", userCartModelList.get(position).getRight_eye_power());
                        }
                    }
                }

                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void moveToWishlist(final int position, final String user_cart_id) {
        final Dialog dialog = CommanMethod.getCustomProgressDialog(context);
        dialog.show();
        final SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart_move_to_wishlist", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        System.out.println("success");
                        removeAt(position);
                        successdialogBox(context.getResources().getString(R.string.alert_message), context.getResources().getString(R.string.item_moved_wishlist),"WISH");

                    } else {
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_cart_id", user_cart_id);
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        mRequestQueue.add(mStringRequest);
    }

    private void dialogBox() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.success_dialog_box);
        //TextView alert_text = (TextView)dialog.findViewById(R.id.item_text_view);
        //alert_text.setText(context.getString(R.string.user_cart_alert));
        TextView info_tv = (TextView) dialog.findViewById(R.id.info_tv);
        info_tv.setText(context.getString(R.string.dialog_product_not_availabel));
        TextView ok_tv = dialog.findViewById(R.id.ok_tv);
        ok_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void moveWishListDialogBox(final int position, final String itemPosition, String info) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.comman_alert_dialog_yes_no);
        TextView info_message = (TextView) dialog.findViewById(R.id.item_text_view);
        info_message.setText(info);
        TextView continue_text_view = (TextView) dialog.findViewById(R.id.change_language_yes);
        TextView go_to_text_view = (TextView) dialog.findViewById(R.id.change_language_no);
        continue_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                moveToWishlist(position, itemPosition);
            }
        });
        go_to_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView item_text_view;
        TextView total_text_view, cart_total_price_value;
        TextView name;
        TextView left_price_tv, left_qty_text_tv, left_dec_tv, left_count_tv, left_plus_tv;
        TextView right_price_tv, right_qty_text_tv, right_dec_tv, right_count_tv, right_plus_tv;

        //TextView number;
        //LinearLayout plus_liner_layout;
        LinearLayout delete_layout, left_qty_lay, right_qty_lay, cart_item_lay;
        LinearLayout move_to_layout;
        TextView out_of_stock;
        TextView text_power;

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public MyViewHolder(View itemView) {
            super(itemView);
            item_text_view = (TextView) itemView.findViewById(R.id.item_text_view);
            total_text_view = (TextView) itemView.findViewById(R.id.total_text_view);
            cart_total_price_value = (TextView) itemView.findViewById(R.id.cart_total_price_value);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);

            left_price_tv = itemView.findViewById(R.id.left_price_tv);
            left_qty_text_tv = itemView.findViewById(R.id.left_qty_text_tv);
            left_dec_tv = itemView.findViewById(R.id.left_dec_tv);
            left_count_tv = itemView.findViewById(R.id.left_count_tv);
            left_plus_tv = itemView.findViewById(R.id.left_plus_tv);
            left_qty_lay = itemView.findViewById(R.id.left_qty_lay);

            right_price_tv = itemView.findViewById(R.id.right_price_tv);
            right_qty_text_tv = itemView.findViewById(R.id.right_qty_text_tv);
            right_dec_tv = itemView.findViewById(R.id.right_dec_tv);
            right_count_tv = itemView.findViewById(R.id.right_count_tv);
            right_plus_tv = itemView.findViewById(R.id.right_plus_tv);
            right_qty_lay = itemView.findViewById(R.id.right_qty_lay);

            cart_item_lay = itemView.findViewById(R.id.cart_item_lay);
            delete_layout = itemView.findViewById(R.id.delete_layout);
            move_to_layout = itemView.findViewById(R.id.move_to_layout);
            out_of_stock = itemView.findViewById(R.id.out_of_stock);
            text_power = itemView.findViewById(R.id.text_power);

        }
    }

}
