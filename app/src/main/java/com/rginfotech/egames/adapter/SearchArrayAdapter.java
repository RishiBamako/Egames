package com.rginfotech.egames.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.rginfotech.egames.model.ProductSearchModel;

import java.util.ArrayList;
import java.util.List;

public class SearchArrayAdapter extends ArrayAdapter<ProductSearchModel> implements Filterable {

    List<ProductSearchModel> allCodes;
    List<ProductSearchModel> originalCodes;
    StringFilter filter;

    public SearchArrayAdapter(Context context, int resource,int id, List<ProductSearchModel> keys) {
        super(context, resource,id, keys);
        this.allCodes=keys;
        this.originalCodes=keys;
    }

    public int getCount() {
        return allCodes.size();
    }

    public ProductSearchModel getItem(int position) {
        return allCodes.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private class StringFilter extends android.widget.Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            final List<ProductSearchModel> list = originalCodes;
            int count = list.size();
            final ArrayList<ProductSearchModel> nlist = new ArrayList<>(count);
            ProductSearchModel filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getTitle().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            allCodes = (ArrayList<ProductSearchModel>) results.values;
            notifyDataSetChanged();
        }

    }


    @Override
    public Filter getFilter()
    {
        return new StringFilter();
    }
}
