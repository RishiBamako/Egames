package com.rginfotech.egames.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
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
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.UserCartActivity;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.model.UserWishlist;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.rginfotech.egames.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    private List<UserWishlist> userWishlists;
    private ImageLoader imageLoader;
    private Context context;
    int button = 0;
    private TextView textView, product_not_av;

    public WishListAdapter(Context ctx,List<UserWishlist> userWishlists, TextView textView, TextView product_not_av){
        inflater = LayoutInflater.from(ctx);
        this.userWishlists = userWishlists;
        this.context=ctx;
        this.textView = textView;
        this.product_not_av = product_not_av;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.wish_list_adapter_recycler_view, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(userWishlists.get(position).getProduct_image(), ImageLoader.getImageListener(holder.product_image, R.drawable.no_img, R.drawable.no_img));

        Picasso.get()
                .load(userWishlists.get(position).getProduct_image())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(holder.product_image);

        holder.name_text_view.setText(userWishlists.get(position).getProduct_name());
        //holder.material_name_text_view.setText(Html.fromHtml(userWishlists.get(position).getDescription(),Html.FROM_HTML_MODE_COMPACT));

        /*if(CommanMethod.isOutOfStock(userWishlists.get(position).getStockFlag(), userWishlists.get(position).getQuantity())){
            holder.buy_button.setText(context.getResources().getString(R.string.out_of_stock_small));
            holder.buy_button.setEnabled(false);
            holder.buy_button.setTextColor(Color.RED);
        }else{
            if (CommanMethod.isOutOfStock(userWishlists.get(position).getStockFlag(), userWishlists.get(position).getQuantity())){
                holder.buy_button.setText(context.getResources().getString(R.string.out_of_stock_small));
                holder.buy_button.setEnabled(false);
                holder.buy_button.setTextColor(Color.RED);
            }else {
                holder.buy_button.setEnabled(true);
                holder.buy_button.setText(context.getResources().getString(R.string.buy_now));
                holder.buy_button.setTextColor(Color.WHITE);
            }
        }*/
        holder.price_text_view.setText(userWishlists.get(position).getPrice()+" "+userWishlists.get(position).getCurrent_currency());
        holder.relative_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommanMethod.isInternetConnected(context)) {
                    wishListAdd(userWishlists.get(position).getProduct_id(), position);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_id",userWishlists.get(position).getProduct_id());
                intent.putExtra("current_currency",userWishlists.get(position).getCurrent_currency());
                intent.putExtra("title_name",userWishlists.get(position).getProduct_name());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        holder.buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox(userWishlists.get(position).getProduct_id());
            }
        });

        if(Locale.getDefault().getLanguage().equals("en")){
            holder.buy_button.setBackgroundResource(R.drawable.button_corner_radius);
            holder.relative_wishlist.setBackgroundResource(R.drawable.wishlist_button_cornar_radius);
        }else{
            holder.buy_button.setBackgroundResource(R.drawable.button_corner_radius_ar);
            holder.relative_wishlist.setBackgroundResource(R.drawable.wishlist_button_cornar_radius_ar);
        }

    }

    @Override
    public int getItemCount() {
        return userWishlists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView product_image;
        TextView name_text_view;
        TextView material_name_text_view;
        TextView price_text_view;
        Button buy_button;
        ImageView wish_list_image1;
        RelativeLayout latest_text;
        RelativeLayout relative_wishlist;

        public MyViewHolder(View itemView) {
            super(itemView);
            product_image = (ImageView)itemView.findViewById(R.id.product_image);
            name_text_view = (TextView)itemView.findViewById(R.id.name_text_view);
            material_name_text_view = (TextView)itemView.findViewById(R.id.material_name_text_view);
            price_text_view = (TextView)itemView.findViewById(R.id.price_text_view);
            buy_button = (Button)itemView.findViewById(R.id.buy_button);
            wish_list_image1 = (ImageView)itemView.findViewById(R.id.wish_list_image1);
            latest_text = (RelativeLayout)itemView.findViewById(R.id.latest_text);
            relative_wishlist = (RelativeLayout)itemView.findViewById(R.id.relative_wishlist);
        }
    }

    private void wishListAdd(final String product_id,final int posi){
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        final SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"wishlist_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        removeAt(posi);
                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                }else{
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("product_id",product_id);
                params.put("shade", "colorDefault");
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

    public void removeAt(int position) {

        userWishlists.remove(position);
        notifyDataSetChanged();
        if (userWishlists.size() == 0){
            product_not_av.setVisibility(View.VISIBLE);
        }

    }

    private void dialogBox(final String product_id){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.buy_now_dialog);
        TextView continue_text_view = (TextView)dialog.findViewById(R.id.continue_text_view);
        TextView go_to_text_view = (TextView)dialog.findViewById(R.id.go_to_text_view);
        continue_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addToCart(product_id,"continue");
            }
        });
        go_to_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                addToCart(product_id,"goToCart");
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addToCart(final String product_id,final String keyValue){
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        final SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        System.out.println("success");
                        if(keyValue.equals("continue")){
                            CommanClass.getCartValue(context, textView);
                        }else if(keyValue.equals("goToCart")){
                            Intent intent = new Intent(context, UserCartActivity.class);
                            context.startActivity(intent);
                        }


                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                }else{
                    params.put("user_id", sessionManager.getRandomValue());
                }
                params.put("product_id",product_id);
                params.put("quantity","1");
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

    public void getCartValue(){
        final Dialog dialog = CommanMethod.getProgressDialogForFragment(context);
        dialog.show();
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"usercart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    dialog.dismiss();
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        JSONObject jsonObject=new JSONObject(object.getString("response"));
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("usercart_Array"));
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("usercart"));

                        if(jsonArray.length()> 0) {
                            textView.setText(String.valueOf(jsonArray.length()));
                        }else {
                            textView.setText("");
                        }
                    }else{
                        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    dialog.dismiss();
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                SessionManager sessionManager = new SessionManager(context);
                Map<String, String>  params = new HashMap<String, String>();
                if(!sessionManager.getUserEmail().isEmpty() && !sessionManager.getUserPassword().isEmpty()){
                    params.put("user_id", sessionManager.getUserId());
                }else{
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
}
