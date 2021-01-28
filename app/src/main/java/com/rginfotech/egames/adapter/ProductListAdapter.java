package com.rginfotech.egames.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.RoundedTransformation;
import com.rginfotech.egames.UserCartActivity;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.model.ProductList;
import com.rginfotech.egames.model.UserWishlist;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;
import com.rginfotech.egames.utility.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    int button = 0;
    boolean isThisFromWishList = false;
    SessionManager sessionManager;
    private LayoutInflater inflater;
    private List<ProductList> productLists;
    private ImageLoader imageLoader;
    private Context context;
    private List<UserWishlist> userWishlists;
    private TextView textView;

    public ProductListAdapter(Context ctx, List<ProductList> productLists, List<UserWishlist> userWishlists, TextView textView) {
        inflater = LayoutInflater.from(ctx);
        this.productLists = productLists;
        this.userWishlists = userWishlists;
        this.context = ctx;
        this.textView = textView;
        isThisFromWishList = false;
        sessionManager = new SessionManager(ctx);

    }

    public ProductListAdapter(Context ctx, List<ProductList> productLists, List<UserWishlist> userWishlists, TextView textView, boolean isThisFromWishList) {
        inflater = LayoutInflater.from(ctx);
        this.productLists = productLists;
        this.userWishlists = userWishlists;
        this.context = ctx;
        this.textView = textView;
        this.isThisFromWishList = isThisFromWishList;
        sessionManager = new SessionManager(ctx);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.product_list_recycler_view_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProductListAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(productLists.get(position).getProduct_image(), ImageLoader.getImageListener(holder.product_image, R.drawable.no_img, R.drawable.no_img));
        final int radius = 12;
        final int margin = 0;
        final Transformation transformation = new RoundedTransformation(radius, margin);

        Picasso
                .get()
                .load(productLists.get(position).getProduct_image()).transform(transformation)
                .placeholder(R.drawable.image).
                resize(200, 310)
                .into(holder.product_image);

        if (Utils.isFromDealOfDays) {
            holder.name_text_view.setText(productLists.get(position).getBrand_name());
            holder.offerPrice_text_view.setVisibility(View.VISIBLE);
            try {
                String deal_otd = productLists.get(position).getDeal_otd();
                if (deal_otd.equals("1")) {
                    // Double offerPrice = Double.parseDouble(productLists.get(position).getDeal_otd_discount());
                    holder.offerPrice_text_view.setText(context.getResources().getString(R.string.offerpricedot) + " " + productLists.get(position).getDeal_otd_discount() + " " + productLists.get(position).getCurrent_currency());
                } else
                    holder.offerPrice_text_view.setVisibility(View.GONE);
            } catch (Exception EE) {
            }

        } else {
            holder.name_text_view.setText(productLists.get(position).getTitle());
            holder.offerPrice_text_view.setVisibility(View.GONE);

        }
        if (productLists.size() > 0) {
            String currency_code = productLists.get(0).getCurrent_currency();
            if (!TextUtils.isEmpty(currency_code)) {
                sessionManager.setCountryCode(currency_code);
            }
        }
        if (productLists.get(position).getQuantity().equals("0")) {
            holder.instockTextViewId.setText(context.getString(R.string.out_of_stock));
        } else {
            holder.instockTextViewId.setText(context.getString(R.string.add_to_cart));
        }


        //  holder.material_name_text_view.setText(Html.fromHtml(productLists.get(position).getDescription()));

        if (Locale.getDefault().getLanguage().equals("en")) {
            holder.buy_button.setBackgroundResource(R.drawable.button_corner_radius);
            //holder.relative_wishlist.setBackgroundResource(R.drawable.wishlist_button_cornar_radius);
        } else {
            holder.buy_button.setBackgroundResource(R.drawable.button_corner_radius_ar);
            // holder.relative_wishlist.setBackgroundResource(R.drawable.wishlist_button_cornar_radius_ar);
        }

        holder.relative_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("wishlist button");
            }
        });

        if (context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR) {
            holder.price_text_view.setBackground(context.getResources().getDrawable(R.drawable.btn_rounded_bot_left));
            holder.buy_button.setBackground(context.getResources().getDrawable(R.drawable.btn_rounded_bot_right));

        } else {
            holder.price_text_view.setBackground(context.getResources().getDrawable(R.drawable.btn_rounded_bot_left_arabic));
            holder.buy_button.setBackground(context.getResources().getDrawable(R.drawable.btn_rounded_bot_right_arabic));
        }


       /* if (productLists.get(position).getSale_price() != null
                && !productLists.get(position).getSale_price().equals("0.00")
                && !productLists.get(position).getSale_price().equals("0.000")
                && !productLists.get(position).getSale_price().equals("0")) {
            Double priceD = Double.parseDouble(productLists.get(position).getPrice());
            holder.price_text_view.setText(priceD.intValue() + " " + productLists.get(position).getCurrent_currency());
            //  holder.sale_price_text_view.setText(productLists.get(position).getPrice()+" "+productLists.get(position).getCurrent_currency());
            //holder.sale_price_text_view.setVisibility(View.VISIBLE);
        } else {
            Double priceD = Double.parseDouble(productLists.get(position).getPrice());
            holder.price_text_view.setText(priceD.intValue() + " " + productLists.get(position).getCurrent_currency());
            // holder.sale_price_text_view.setVisibility(View.GONE);
        }*/
        //Double priceD = Double.parseDouble(productLists.get(position).getPrice());
        holder.price_text_view.setText(productLists.get(position).getPrice() + " " + productLists.get(position).getCurrent_currency());


        if (productLists.get(position).getTags().equals("new")
                || productLists.get(position).getTags().equals("latest")
                || productLists.get(position).getTags().equals("exclusive")) {
            if (productLists.get(position).getTags().equals("exclusive")) {
                holder.tag_text.setText(context.getResources().getString(R.string.exclusive));
            } else {
                holder.tag_text.setText(context.getResources().getString(R.string.latest));
            }
            holder.latest_text.setVisibility(View.GONE);

        } else {
            holder.latest_text.setVisibility(View.GONE);
        }

       /* if(CommanMethod.isOutOfStock(productLists.get(position).getStock_flag(), productLists.get(position).getQuantity())){
            holder.buy_button.setText(context.getResources().getString(R.string.out_of_stock_small));
            holder.buy_button.setEnabled(false);
            holder.buy_button.setTextColor(Color.WHITE);
        }
        else{
            if (CommanMethod.isOutOfStock(productLists.get(position).getStock_flag(), productLists.get(position).getQuantity())){
                holder.buy_button.setText(context.getResources().getString(R.string.out_of_stock_small));
                holder.buy_button.setEnabled(false);
                holder.buy_button.setTextColor(Color.WHITE);
            }
            else {
                holder.buy_button.setEnabled(true);
                holder.buy_button.setText(context.getResources().getString(R.string.add_to_cart));
                holder.buy_button.setTextColor(Color.WHITE);
            }
        }*/
        if (position == productLists.size() - 1) {
            holder.ll_main_layout.setPadding(0, 10, 0, 20);
        } else {
            holder.ll_main_layout.setPadding(0, 15, 0, 10);
        }

        if (productLists.get(position).getWishlist().equals("1")) {
            holder.wish_list_image1.setVisibility(View.VISIBLE);
            holder.wish_list_image.setVisibility(View.GONE);
        } else {
            holder.wish_list_image1.setVisibility(View.GONE);
            holder.wish_list_image.setVisibility(View.VISIBLE);
        }

        /*if (productLists.get(position).getCate_id().equals("2")) {
            holder.buy_button.setVisibility(View.GONE);
            holder.view_detail_button.setVisibility(View.VISIBLE);
        } else {
            holder.buy_button.setVisibility(View.VISIBLE);
            holder.view_detail_button.setVisibility(View.GONE);
        }*/

 /*       if(userWishlists.size()>0) {
            for (int i = 0; i < userWishlists.size(); i++) {
                if (userWishlists.get(i).getProduct_id().equals(productLists.get(position).getId())) {
                    holder.wish_list_image1.setVisibility(View.VISIBLE);
                }
            }
        }
*/
        holder.wish_list_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishListAdd(productLists.get(position).getId(), holder, position);

            }
        });
        holder.wish_list_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wishListAdd(productLists.get(position).getId(), holder, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id", productLists.get(position).getId());
                intent.putExtra("current_currency", productLists.get(position).getCurrent_currency());
                intent.putExtra("title_name", productLists.get(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        holder.view_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommanMethod.isInternetConnected(context)) {
                    addToCart(productLists.get(position).getId());
                }

               /* Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id",productLists.get(position).getId());
                intent.putExtra("current_currency",productLists.get(position).getCurrent_currency());
                intent.putExtra("title_name",productLists.get(position).getTitle());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);*/
            }
        });
        holder.buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!productLists.get(position).getStock_flag().equals("0") && !productLists.get(position).getQuantity().equals("0")) {
                    if (CommanMethod.isInternetConnected(context)) {
                        addToCart(productLists.get(position).getId());
                    }
                }
                //dialogBox(productLists.get(position).getId(),productLists.get(position).getBrand_id(),productLists.get(position).getBrand_name());
            }
        });

        //      holder.free_text_view.setText(productLists.get(position).getOffer_name());

       /* if(productLists.get(position).getOffer_name().isEmpty()){
            holder.free_text_view.setVisibility(View.GONE);
        }else{
            holder.free_text_view.setText(productLists.get(position).getOffer_name());
            holder.free_text_view.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    private void wishListAdd(final String product_id, ProductListAdapter.MyViewHolder myViewHolder, int position) {
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        final SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "wishlist_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        //refreshProductList.refreshList();
                        if (productLists.get(position).getWishlist().equals("1")) {
                            productLists.get(position).setWishlist("0");
                            if (isThisFromWishList) {
                                productLists.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, productLists.size());
                            } else {
                                notifyItemChanged(position);
                            }
                        } else {
                            productLists.get(position).setWishlist("1");
                            notifyItemChanged(position);
                        }


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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("product_id", product_id);
                params.put("shade", "colorDefault");
                params.put("left_eye_power", "0.0");
                params.put("right_eye_power", "0.0");
                Log.e("user_id", sessionManager.getUserId());
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
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.buy_now_dialog);
        TextView continue_text_view = (TextView) dialog.findViewById(R.id.continue_text_view);
        TextView go_to_text_view = (TextView) dialog.findViewById(R.id.go_to_text_view);
        continue_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //addToCart(product_id,getBrand_id,getBrand_Name,"continue");
            }
        });
        go_to_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(context, UserCartActivity.class);
                context.startActivity(intent);
                //addToCart(product_id,getBrand_id,getBrand_Name,"goToCart");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addToCart(final String product_id) {
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        final SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    CommanClass.addCustomView(context, HomeActivity.navView, HomeActivity.cartCountTextView);
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        CommanClass.getCartValue(context, textView);
                        dialogBox();
                        /*if(keyValue.equals("continue")){
                         *//*Intent intent = new Intent(context,ProductListActivity.class);
                           intent.putExtra("brand_id",getBrand_id);
                           intent.putExtra("brand_name",getBrand_Name);
                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           context.startActivity(intent);*//*
                           CommanClass.getCartValue(context, textView);
                       }else if(keyValue.equals("goToCart")){
                           Intent intent=new Intent(context, UserCartActivity.class);
                           context.startActivity(intent);
                       }*/


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
                if (!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()) {
                    params.put("user_id", sessionManager.getUserId());
                } else {
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("product_id", product_id);
                params.put("quantity", "1");
                params.put("shade", "");
                params.put("left_eye_power", "0.0");
                params.put("right_eye_power", "0.0");
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

    public void getCartValue() {
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "usercart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        JSONObject jsonObject = new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));

                        if (jsonArray.length() > 0) {
                            textView.setText(String.valueOf(jsonArray.length()));
                        } else {
                            textView.setText("");
                        }
                    } else {
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String> params = new HashMap<String, String>();
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

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView name_text_view;
        TextView instockTextViewId;
        TextView offerPrice_text_view;
        //   TextView material_name_text_view;
        TextView price_text_view;
        //  TextView free_text_view;
        Button view_detail_button;
        LinearLayout buy_button;
        ImageView wish_list_image;
        ImageView wish_list_image1;
        RelativeLayout latest_text;
        TextView sale_price_text_view;
        RelativeLayout relative_wishlist;
        TextView tag_text;
        LinearLayout ll_main_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.product_image);
            name_text_view = (TextView) itemView.findViewById(R.id.name_text_view);
            instockTextViewId = (TextView) itemView.findViewById(R.id.instockTextViewId);
            offerPrice_text_view = (TextView) itemView.findViewById(R.id.offerPrice_text_view);
            ll_main_layout = (LinearLayout) itemView.findViewById(R.id.ll_main_layout);
            //   material_name_text_view = (TextView)itemView.findViewById(R.id.material_name_text_view);
            price_text_view = (TextView) itemView.findViewById(R.id.price_text_view);
            // free_text_view = (TextView)itemView.findViewById(R.id.free_text_view);
            buy_button = (LinearLayout) itemView.findViewById(R.id.buy_button);
            view_detail_button = (Button) itemView.findViewById(R.id.view_detail_button);
            wish_list_image = (ImageView) itemView.findViewById(R.id.wish_list_image);
            wish_list_image1 = (ImageView) itemView.findViewById(R.id.wish_list_image1);
            latest_text = (RelativeLayout) itemView.findViewById(R.id.latest_text);
            //sale_price_text_view = (TextView)itemView.findViewById(R.id.sale_price_text_view);
            // sale_price_text_view.setPaintFlags(sale_price_text_view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            relative_wishlist = (RelativeLayout) itemView.findViewById(R.id.relative_wishlist);
            tag_text = (TextView) itemView.findViewById(R.id.tag_text);
        }
    }
}
