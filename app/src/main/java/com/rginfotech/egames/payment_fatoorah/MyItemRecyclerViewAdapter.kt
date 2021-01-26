package com.rginfotech.egames.payment_fatoorah

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myfatoorah.sdk.model.initiatepayment.PaymentMethod
import com.rginfotech.egames.R
import kotlinx.android.synthetic.main.fragment_item.view.*


class MyItemRecyclerViewAdapter(
    public val mValues: java.util.ArrayList<PaymentMethod>,
    public val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    public val mOnClickListener: View.OnClickListener
    public var listSelected = ArrayList<Boolean>()

    init {
        for (i in 0..mValues.size){
            listSelected.add(false)
        }

        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as PaymentMethod
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(v.id, item)
            changeSelected(v.id)
        }
    }

    fun changeSelected(position: Int) {
        for (i in 0..mValues.size)
            listSelected[i] = i == position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]

        Glide.with(holder.mView.context)
            .load(item.imageUrl)
            .into(holder.mImage)

        if(listSelected[position])
            holder.mcbSelected.visibility = View.VISIBLE
        else
            holder.mcbSelected.visibility = View.GONE

        with(holder.mView) {
            tag = item
            id = position
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mImage: ImageView = mView.image
        val mcbSelected: CheckBox = mView.cbSelected

    }
}
