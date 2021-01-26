package com.rginfotech.egames.adapter;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.R;
import com.rginfotech.egames.model.CountryList;
import com.rginfotech.egames.utility.CustomVolleyRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CountryListAdapter extends ArrayAdapter<CountryList> {

    private ArrayList<CountryList> countryLists;
    Context mContext;
    private ImageLoader imageLoader;
    public int getselectedPosition=-1;
    private static class ViewHolder {
        TextView txtName;
        ImageView info;
        ImageView checkImage;
    }

    public CountryListAdapter(ArrayList<CountryList> countryLists, Context context) {
        super(context, R.layout.country_list_item, countryLists);
        this.countryLists = countryLists;
        this.mContext=context;
    }

    private int lastPosition = -1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CountryList dataModel = getItem(position);
        final CountryListAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new CountryListAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.country_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtViewCountryName);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.imgViewFlag);
            viewHolder.checkImage = (ImageView)convertView.findViewById(R.id.imgViewcheck);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CountryListAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        lastPosition = position;

        if(mContext.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            viewHolder.txtName.setGravity(Gravity.START);
        }else{
            viewHolder.txtName.setGravity(Gravity.END);
        }
        viewHolder.txtName.setText(dataModel.getAsciiname());
        //imageLoader = CustomVolleyRequest.getInstance(getContext()).getImageLoader();
        //imageLoader.get(dataModel.getFlag(),ImageLoader.getImageListener(viewHolder.info, R.drawable.no_img, R.drawable.no_img));

        Picasso.get()
                .load(dataModel.getFlag())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(viewHolder.info);

        viewHolder.checkImage.setTag(position);
        if(getselectedPosition==position){
            viewHolder.checkImage.setVisibility(View.VISIBLE);
        }else{
            viewHolder.checkImage.setVisibility(View.GONE);
        }
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastPosition==position){
                    viewHolder.checkImage.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.checkImage.setVisibility(View.GONE);
                }

            }
        });*/
        return convertView;
    }
}
