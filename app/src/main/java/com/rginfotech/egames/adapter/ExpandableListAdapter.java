package com.rginfotech.egames.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.rginfotech.egames.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> header;
    private HashMap<String, List<String>> child;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this._context = context;
        this.header = listDataHeader;
        this.child = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this.child.get(this.header.get(groupPosition)).get(childPosititon);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.childs, parent, false);
        }

        TextView child_text = (TextView) convertView.findViewById(R.id.child);

        child_text.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        // return children count
        // return this.child.get(this.header.get(groupPosition)).size();
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {

        // Get header position
        return this.header.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        // Get header size
        return this.header.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header, parent, false);
        }

        TextView header_text = (TextView) convertView.findViewById(R.id.header);

        header_text.setText(headerTitle);
        Typeface font= Typeface.createFromAsset(_context.getApplicationContext().getAssets(), "futuralight.ttf");

        if (isExpanded) {
            header_text.setTypeface(font);
            if(_context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                header_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_tick_20, 0, 0, 0);
            }else{
                header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.menu_tick1_20, 0);
            }
            //header_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_tick_20, 0, 0, 0);
        } else {
            header_text.setTypeface(font);
            if(_context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
                header_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_tick_20, 0, 0, 0);
            }else{
                header_text.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.menu_tick1_20, 0);
            }
            //header_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.menu_tick_20, 0, 0, 0);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

