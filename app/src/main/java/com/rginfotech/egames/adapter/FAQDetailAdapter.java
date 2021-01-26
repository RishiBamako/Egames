package com.rginfotech.egames.adapter;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import com.rginfotech.egames.R;
import com.rginfotech.egames.model.FAQDetailModel;

import java.util.ArrayList;
import java.util.Locale;

public class FAQDetailAdapter extends ArrayAdapter<FAQDetailModel> {

    private ArrayList<FAQDetailModel> faqDetailModelArrayList;
    Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView description_textview;
    }

    public FAQDetailAdapter(ArrayList<FAQDetailModel> faqDetailModelArrayList, Context context) {
        super(context, R.layout.faq_detail_row_item, faqDetailModelArrayList);
        this.faqDetailModelArrayList = faqDetailModelArrayList;
        this.mContext=context;

    }

    private int lastPosition = -1;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FAQDetailModel dataModel = getItem(position);
        FAQDetailAdapter.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new FAQDetailAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.faq_detail_row_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.description_textview = (TextView)convertView.findViewById(R.id.description_textview);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FAQDetailAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        lastPosition = position;
        if(Locale.getDefault().getLanguage().equals("en")){
            viewHolder.txtName.setText(dataModel.getTitle());
            Spanned spanned = HtmlCompat.fromHtml(dataModel.getDescription(), HtmlCompat.FROM_HTML_MODE_COMPACT);
            viewHolder.description_textview.setText(spanned);
        }else{
            viewHolder.txtName.setText(dataModel.getTitle_ar());
            Spanned spanned = HtmlCompat.fromHtml(dataModel.getDescription_ar(), HtmlCompat.FROM_HTML_MODE_COMPACT);
            viewHolder.description_textview.setText(spanned);
        }

        return convertView;
    }
}
