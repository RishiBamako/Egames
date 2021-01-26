package com.rginfotech.egames.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.rginfotech.egames.R;
import com.rginfotech.egames.interfacelenzzo.SortByInterface;
import com.rginfotech.egames.fragment.WishListFragment;
import com.rginfotech.egames.model.SortByModel;

import java.util.List;

public class SortByAdapterFragment extends RecyclerView.Adapter<SortByAdapterFragment.MyViewHolder>{

    private LayoutInflater inflater;
    private List<SortByModel> sortByModelList;
    private ImageLoader imageLoader;
    private Context context;
    private int lastSelectedPosition = -1;
    private SortByInterface sortByInterface;
    private WishListFragment wishListFragment;

    public SortByAdapterFragment(Context ctx, WishListFragment wishListFragment, List<SortByModel> sortByModelList){
        inflater = LayoutInflater.from(ctx);
        this.sortByModelList = sortByModelList;
        this.context=ctx;
        this.wishListFragment = wishListFragment;
        this.sortByInterface = wishListFragment;
    }

    @Override
    public SortByAdapterFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.sort_by_recycler_view_item, parent, false);
        SortByAdapterFragment.MyViewHolder holder = new SortByAdapterFragment.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SortByAdapterFragment.MyViewHolder holder, final int position) {
        holder.text_sort.setText(sortByModelList.get(position).getTitle());
        holder.redia_button.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return sortByModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        RadioButton redia_button;
        TextView text_sort;

        public MyViewHolder(View itemView) {
            super(itemView);
            redia_button = (RadioButton) itemView.findViewById(R.id.redia_button);
            text_sort = (TextView)itemView.findViewById(R.id.text_sort);
            redia_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    sortByInterface.sortByPrice(sortByModelList.get(lastSelectedPosition).getKey(),sortByModelList.get(lastSelectedPosition).getValue());
                }
            });
        }
    }
}
