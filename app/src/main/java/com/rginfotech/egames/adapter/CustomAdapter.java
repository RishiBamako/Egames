package com.rginfotech.egames.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.rginfotech.egames.R;
import com.rginfotech.egames.model.FAQModel;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<FAQModel>{

    private ArrayList<FAQModel> faqModels;
    Context mContext;

    private static class ViewHolder {
        TextView txtName;
        ImageView info;
    }

    public CustomAdapter(ArrayList<FAQModel> faqModels, Context context) {
        super(context, R.layout.row_item, faqModels);
        this.faqModels = faqModels;
        this.mContext=context;

    }

    private int lastPosition = -1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FAQModel dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        lastPosition = position;

        if(Locale.getDefault().getLanguage().equals("en")){
            viewHolder.txtName.setText(dataModel.getName());
        }else{
            viewHolder.txtName.setText(dataModel.getName_ar());
        }

        viewHolder.info.setTag(position);
        return convertView;
    }
}
