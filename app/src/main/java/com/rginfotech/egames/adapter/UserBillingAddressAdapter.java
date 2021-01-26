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
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.rginfotech.egames.AddNewAddressActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.interfacelenzzo.UserAddressInterface;
import com.rginfotech.egames.model.UserBillingAddressModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBillingAddressAdapter extends RecyclerView.Adapter<UserBillingAddressAdapter.MyViewHolder> {

    UserAddressInterface userAddressInterface;
    boolean isThisFromAccount;
    private LayoutInflater inflater;
    private List<UserBillingAddressModel> userBillingAddressModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;

    public UserBillingAddressAdapter(Context ctx, List<UserBillingAddressModel> userBillingAddressModelList, boolean isThisFromAccount) {
        inflater = LayoutInflater.from(ctx);
        this.userBillingAddressModelList = userBillingAddressModelList;
        this.context = ctx;
        this.userAddressInterface = ((UserAddressInterface) context);
        this.isThisFromAccount = isThisFromAccount;
    }


    @Override
    public UserBillingAddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.user_billing_address_recycler_item, parent, false);
        UserBillingAddressAdapter.MyViewHolder holder = new UserBillingAddressAdapter.MyViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final UserBillingAddressAdapter.MyViewHolder holder, final int position) {
        /*if(context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            holder.select_layour.setGravity(Gravity.END);
        }else{
            holder.select_layour.setGravity(Gravity.START);
        }*/
        holder.name_text_view.setText(userBillingAddressModelList.get(position).getFull_name());
        holder.area_text_view.setText(userBillingAddressModelList.get(position).getArea());
        holder.block_text_view.setText(userBillingAddressModelList.get(position).getBlock());
        holder.street_text_view.setText(userBillingAddressModelList.get(position).getStreet());
        holder.avenue_text_view.setText(userBillingAddressModelList.get(position).getAvenue());
        holder.house_text_view.setText(userBillingAddressModelList.get(position).getHouse_no());
        holder.floor_text_view.setText(userBillingAddressModelList.get(position).getFloor_no());
        holder.flat_text_view.setText(userBillingAddressModelList.get(position).getFlat_no());
        holder.phone_text_view.setText(userBillingAddressModelList.get(position).getPhone_no());
        holder.paci_text_view.setText(userBillingAddressModelList.get(position).getPaci_number());
        holder.comment_text_view.setText(userBillingAddressModelList.get(position).getComments());
        holder.current_location_text_view.setText(userBillingAddressModelList.get(position).getCurrrent_location());
        holder.addressTypeTextViewId.setText(" "+userBillingAddressModelList.get(position).getAddress_type());

        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getCurrrent_location())) {
            holder.cur_location_lay.setVisibility(View.GONE);
            //holder.comment_lay.computeScroll();
        } else {
            holder.cur_location_lay.setVisibility(View.VISIBLE);

        }
        if (isThisFromAccount) {
            holder.select_layour.setVisibility(View.GONE);
        }
        else{
            holder.select_layour.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getFull_name())) {
            holder.liner_layout_n.setVisibility(View.GONE);
        } else {
            holder.liner_layout_n.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getArea())) {
            holder.liner_layout_a.setVisibility(View.GONE);
        } else {
            holder.liner_layout_a.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getBlock())) {
            holder.liner_layout_b.setVisibility(View.GONE);
        } else {
            holder.liner_layout_b.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getStreet())) {
            holder.liner_layout_s.setVisibility(View.GONE);
        } else {
            holder.liner_layout_s.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getAvenue())) {
            holder.liner_layout_av.setVisibility(View.GONE);
        } else {
            holder.liner_layout_av.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getHouse_no())) {
            holder.liner_layout_h.setVisibility(View.GONE);
        } else {
            holder.liner_layout_h.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getFloor_no())) {
            holder.liner_layout_fl.setVisibility(View.GONE);
        } else {
            holder.liner_layout_fl.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getFlat_no())) {
            holder.liner_layout_fa.setVisibility(View.GONE);
        } else {
            holder.liner_layout_fa.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getPhone_no())) {
            holder.liner_layout_ph.setVisibility(View.GONE);
        } else {
            holder.liner_layout_ph.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getPaci_number())) {
            holder.liner_layout_pa.setVisibility(View.GONE);
        } else {
            holder.liner_layout_pa.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(userBillingAddressModelList.get(position).getComments())) {
            holder.liner_layout_c.setVisibility(View.GONE);
        } else {
            holder.liner_layout_c.setVisibility(View.VISIBLE);
        }

        holder.redia_button.setChecked(userBillingAddressModelList.get(position).isSelected());

        holder.delete_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox(userBillingAddressModelList.get(position).getId(), position);
            }
        });
        holder.edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNewAddressActivity.class);
                intent.putExtra("user_billing_address_id", userBillingAddressModelList.get(position).getId());
                intent.putExtra("get_full_name", userBillingAddressModelList.get(position).getFull_name());
                intent.putExtra("get_area_text", userBillingAddressModelList.get(position).getArea());
                intent.putExtra("get_block_text", userBillingAddressModelList.get(position).getBlock());
                intent.putExtra("get_street_text", userBillingAddressModelList.get(position).getStreet());
                intent.putExtra("get_avenue_text", userBillingAddressModelList.get(position).getAvenue());
                intent.putExtra("get_house_text", userBillingAddressModelList.get(position).getHouse_no());
                intent.putExtra("get_floor_text", userBillingAddressModelList.get(position).getFloor_no());
                intent.putExtra("get_flat_text", userBillingAddressModelList.get(position).getFlat_no());
                intent.putExtra("get_phone_text", userBillingAddressModelList.get(position).getPhone_no());
                intent.putExtra("get_paci_text", userBillingAddressModelList.get(position).getPaci_number());
                intent.putExtra("get_comment_text", userBillingAddressModelList.get(position).getComments());
                intent.putExtra("get_current_location_name", userBillingAddressModelList.get(position).getCurrrent_location());
                intent.putExtra("get_current_location_lat", userBillingAddressModelList.get(position).getLatitude());
                intent.putExtra("address_type", userBillingAddressModelList.get(position).getAddress_type());
                intent.putExtra("get_current_location_long", userBillingAddressModelList.get(position).getLongitude());
                context.startActivity(intent);

            }
        });

    }

    private void ManipulateClick(int position) {
        if (userBillingAddressModelList.size() > 0) {
            for (int i = 0; i < userBillingAddressModelList.size(); i++) {
                if (i == position) {
                    userBillingAddressModelList.get(i).setSelected(true);
                } else {
                    userBillingAddressModelList.get(i).setSelected(false);
                }
            }
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return userBillingAddressModelList.size();
    }

    private void dialogBox(final String user_billing_address_id, final int position) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.address_delete_dialog);
        TextView yes_text_view = (TextView) dialog.findViewById(R.id.yes_text_view);
        TextView no_text_view = (TextView) dialog.findViewById(R.id.no_text_view);
        yes_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                userAddressDelete(user_billing_address_id, position);
            }
        });
        no_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void userAddressDelete(final String user_billing_address_id, final int position) {
        //gifImageView.setVisibility(View.VISIBLE);
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL + "user_billing_address_delete", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //gifImageView.setVisibility(View.GONE);
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.equals("success")) {
                        removeAt(position);
                    } else {
                        // String message = object.getString("message");
                        //Toast.makeText(AddNewAddressActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //gifImageView.setVisibility(View.GONE);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // gifImageView.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_billing_address_id", user_billing_address_id);
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
        userBillingAddressModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userBillingAddressModelList.size());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout edit_layout;
        LinearLayout delete_layout;
        LinearLayout select_layour;
        TextView name_text_view;
        TextView addressTypeTextViewId;
        TextView area_text_view;
        TextView block_text_view;
        TextView street_text_view;
        TextView avenue_text_view;
        TextView house_text_view;
        TextView floor_text_view;
        TextView flat_text_view;
        TextView phone_text_view;
        TextView paci_text_view;
        TextView comment_text_view;
        TextView current_location_text_view;
        RadioButton redia_button;
        LinearLayout liner_layout_n, address_lay;
        LinearLayout liner_layout_a;
        LinearLayout liner_layout_b;
        LinearLayout liner_layout_s;
        LinearLayout liner_layout_av;
        LinearLayout liner_layout_h;
        LinearLayout liner_layout_fl;
        LinearLayout liner_layout_fa;
        LinearLayout liner_layout_ph;
        LinearLayout liner_layout_pa;
        LinearLayout liner_layout_c;
        LinearLayout cur_location_lay;

        public MyViewHolder(View itemView) {
            super(itemView);
            edit_layout = (LinearLayout) itemView.findViewById(R.id.edit_layout);
            delete_layout = (LinearLayout) itemView.findViewById(R.id.delete_layout);
            select_layour = (LinearLayout) itemView.findViewById(R.id.select_layour);
            name_text_view = (TextView) itemView.findViewById(R.id.name_text_view);
            addressTypeTextViewId = (TextView) itemView.findViewById(R.id.addressTypeTextViewId);
            area_text_view = (TextView) itemView.findViewById(R.id.area_text_view);
            block_text_view = (TextView) itemView.findViewById(R.id.block_text_view);
            street_text_view = (TextView) itemView.findViewById(R.id.street_text_view);
            avenue_text_view = (TextView) itemView.findViewById(R.id.avenue_text_view);
            house_text_view = (TextView) itemView.findViewById(R.id.house_text_view);
            floor_text_view = (TextView) itemView.findViewById(R.id.floor_text_view);
            flat_text_view = (TextView) itemView.findViewById(R.id.flat_text_view);
            phone_text_view = (TextView) itemView.findViewById(R.id.phone_text_view);
            paci_text_view = (TextView) itemView.findViewById(R.id.paci_text_view);
            comment_text_view = (TextView) itemView.findViewById(R.id.comment_text_view);
            current_location_text_view = (TextView) itemView.findViewById(R.id.current_location_text_view);
            cur_location_lay = (LinearLayout) itemView.findViewById(R.id.cur_location_lay);
            liner_layout_n = (LinearLayout) itemView.findViewById(R.id.liner_layout_n);
            liner_layout_a = (LinearLayout) itemView.findViewById(R.id.liner_layout_a);
            liner_layout_b = (LinearLayout) itemView.findViewById(R.id.liner_layout_b);
            liner_layout_s = (LinearLayout) itemView.findViewById(R.id.liner_layout_s);
            liner_layout_av = (LinearLayout) itemView.findViewById(R.id.liner_layout_av);
            liner_layout_h = (LinearLayout) itemView.findViewById(R.id.liner_layout_h);
            liner_layout_fl = (LinearLayout) itemView.findViewById(R.id.liner_layout_fl);
            liner_layout_fa = (LinearLayout) itemView.findViewById(R.id.liner_layout_fa);
            liner_layout_ph = (LinearLayout) itemView.findViewById(R.id.liner_layout_ph);
            liner_layout_pa = (LinearLayout) itemView.findViewById(R.id.liner_layout_pa);
            liner_layout_c = (LinearLayout) itemView.findViewById(R.id.liner_layout_c);
            address_lay = itemView.findViewById(R.id.address_lay);

            redia_button = (RadioButton) itemView.findViewById(R.id.redia_button);
            redia_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    ManipulateClick(lastSelectedPosition);
                    System.out.println("check position " + userBillingAddressModelList.get(lastSelectedPosition).getArea() + "fdgfd " + userBillingAddressModelList.get(lastSelectedPosition).getId());
                    userAddressInterface.getAddressId(userBillingAddressModelList.get(lastSelectedPosition).getId(), userBillingAddressModelList.get(lastSelectedPosition).getFull_name(), userBillingAddressModelList.get(lastSelectedPosition).getArea(), userBillingAddressModelList.get(lastSelectedPosition).getBlock(), userBillingAddressModelList.get(lastSelectedPosition).getStreet(), userBillingAddressModelList.get(lastSelectedPosition).getAvenue(), userBillingAddressModelList.get(lastSelectedPosition).getHouse_no(), userBillingAddressModelList.get(lastSelectedPosition).getFloor_no(), userBillingAddressModelList.get(lastSelectedPosition).getFlat_no(), userBillingAddressModelList.get(lastSelectedPosition).getPhone_no(), userBillingAddressModelList.get(lastSelectedPosition).getComments());
                    notifyDataSetChanged();
                }
            });

            address_lay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    ManipulateClick(lastSelectedPosition);
                    System.out.println("check position " + userBillingAddressModelList.get(lastSelectedPosition).getArea() + "fdgfd " + userBillingAddressModelList.get(lastSelectedPosition).getId());
                    userAddressInterface.getAddressId(userBillingAddressModelList.get(lastSelectedPosition).getId(), userBillingAddressModelList.get(lastSelectedPosition).getFull_name(), userBillingAddressModelList.get(lastSelectedPosition).getArea(), userBillingAddressModelList.get(lastSelectedPosition).getBlock(), userBillingAddressModelList.get(lastSelectedPosition).getStreet(), userBillingAddressModelList.get(lastSelectedPosition).getAvenue(), userBillingAddressModelList.get(lastSelectedPosition).getHouse_no(), userBillingAddressModelList.get(lastSelectedPosition).getFloor_no(), userBillingAddressModelList.get(lastSelectedPosition).getFlat_no(), userBillingAddressModelList.get(lastSelectedPosition).getPhone_no(), userBillingAddressModelList.get(lastSelectedPosition).getComments());
                    notifyDataSetChanged();
                }
            });
        }
    }
}
