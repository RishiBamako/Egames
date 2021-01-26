package com.rginfotech.egames.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.ProductDetailsActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.UserCartActivity;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.fragment.BrandListFragment;
import com.rginfotech.egames.fragment.ProductListFragment;
import com.rginfotech.egames.interfacelenzzo.RefreshProductList;
import com.rginfotech.egames.model.ProductList;
import com.rginfotech.egames.model.UserWishlist;
import com.rginfotech.egames.utility.CommanClass;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewProductListAdapter extends RecyclerView.Adapter<NewProductListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private List<ProductList> productLists;
    private ImageLoader imageLoader;
    private Context context;
    int button = 0;
    private List<UserWishlist> userWishlists;
    private TextView textView;
    private RefreshProductList refreshProductList;

    String type;

    public NewProductListAdapter(Context ctx, List<ProductList> productLists,String type){
        inflater = LayoutInflater.from(ctx);
        this.productLists = productLists;
        this.context=ctx;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.new_product_list_recycler_view_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewProductListAdapter.MyViewHolder holder, final int position) {
        //imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        //imageLoader.get(productLists.get(position).getProduct_image(), ImageLoader.getImageListener(holder.product_image, R.drawable.no_img, R.drawable.no_img));

        Picasso
                .get()
                .load(productLists.get(position).getProduct_image())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                ///.fitCenter()
                .into(holder.product_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("product_detail")) {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("product_id", productLists.get(position).getId());
                    intent.putExtra("current_currency", productLists.get(position).getCurrent_currency());
                    intent.putExtra("title_name", productLists.get(position).getTitle());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView product_image;
        TextView name_text_view;
        TextView material_name_text_view;
        TextView price_text_view;
        TextView free_text_view;
        Button buy_button, view_detail_button;
        ImageView wish_list_image;
        ImageView wish_list_image1;
        RelativeLayout latest_text;
        TextView sale_price_text_view;
        RelativeLayout relative_wishlist;
        TextView tag_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            product_image = (ImageView)itemView.findViewById(R.id.product_image);
          //  view_detail_button = (Button)itemView.findViewById(R.id.view_detail_button);

         /*   name_text_view = (TextView)itemView.findViewById(R.id.name_text_view);
            material_name_text_view = (TextView)itemView.findViewById(R.id.material_name_text_view);
            price_text_view = (TextView)itemView.findViewById(R.id.price_text_view);
            free_text_view = (TextView)itemView.findViewById(R.id.free_text_view);
            buy_button = (Button)itemView.findViewById(R.id.buy_button);
            view_detail_button = (Button)itemView.findViewById(R.id.view_detail_button);
            wish_list_image = (ImageView)itemView.findViewById(R.id.wish_list_image);
            wish_list_image1 = (ImageView)itemView.findViewById(R.id.wish_list_image1);
            latest_text = (RelativeLayout)itemView.findViewById(R.id.latest_text);
            sale_price_text_view = (TextView)itemView.findViewById(R.id.sale_price_text_view);
            sale_price_text_view.setPaintFlags(sale_price_text_view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            relative_wishlist = (RelativeLayout)itemView.findViewById(R.id.relative_wishlist);
            tag_text = (TextView)itemView.findViewById(R.id.tag_text);*/
        }
    }

    private void wishListAdd(final String product_id){
        final SessionManager sessionManager = new SessionManager(context);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"wishlist_add", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        refreshProductList.refreshList();
                    }else{
                        //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

    private void dialogBox(){

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

   private void addToCart(final String product_id){
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


                   }else{
                       //Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                   }
               }catch (JSONException e){
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
