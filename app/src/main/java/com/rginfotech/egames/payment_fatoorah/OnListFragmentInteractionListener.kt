package com.rginfotech.egames.payment_fatoorah

import com.myfatoorah.sdk.model.initiatepayment.PaymentMethod

interface OnListFragmentInteractionListener {
    fun onListFragmentInteraction(position: Int, item: PaymentMethod)
}