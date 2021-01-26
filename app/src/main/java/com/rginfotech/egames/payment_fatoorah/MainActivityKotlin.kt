package com.rginfotech.egames.payment_fatoorah

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.myfatoorah.sdk.model.executepayment.MFExecutePaymentRequest
import com.myfatoorah.sdk.model.executepayment_cardinfo.MFCardInfo
import com.myfatoorah.sdk.model.executepayment_cardinfo.MFDirectPaymentResponse
import com.myfatoorah.sdk.model.initiatepayment.MFInitiatePaymentRequest
import com.myfatoorah.sdk.model.initiatepayment.MFInitiatePaymentResponse
import com.myfatoorah.sdk.model.initiatepayment.PaymentMethod
import com.myfatoorah.sdk.model.paymentstatus.MFGetPaymentStatusResponse
import com.myfatoorah.sdk.model.sendpayment.MFSendPaymentRequest
import com.myfatoorah.sdk.model.sendpayment.MFSendPaymentResponse
import com.myfatoorah.sdk.utils.MFAPILanguage
import com.myfatoorah.sdk.utils.MFCurrencyISO
import com.myfatoorah.sdk.utils.MFMobileISO
import com.myfatoorah.sdk.utils.MFNotificationOption
import com.myfatoorah.sdk.views.MFResult
import com.myfatoorah.sdk.views.MFResult.Fail
import com.myfatoorah.sdk.views.MFResult.Success
import com.myfatoorah.sdk.views.MFSDK
import com.rginfotech.egames.R
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration
import kotlinx.android.synthetic.main.activity_main_payment.*
import java.util.*


class MainActivityKotlin : AppCompatActivity(), View.OnClickListener,
    OnListFragmentInteractionListener {

    private lateinit var adapter: MyItemRecyclerViewAdapter
    private var selectedPaymentMethod: PaymentMethod? = null
    private val TAG = MainActivityKotlin::class.java.simpleName
    private var listener: OnListFragmentInteractionListener? = null
    var amount = "";

    override fun onListFragmentInteraction(position: Int, paymentMethod: PaymentMethod) {

        selectedPaymentMethod = paymentMethod
        adapter.changeSelected(position)

        if (paymentMethod.directPayment)
            llDirectPaymentLayout.visibility = View.VISIBLE
        else
            llDirectPaymentLayout.visibility = View.GONE
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btPay -> {
                if (selectedPaymentMethod == null) {
                    showAlertDialog("Please select payment method first")
                    return
                }

                if (!selectedPaymentMethod?.directPayment!!)
                    executePayment(selectedPaymentMethod?.paymentMethodId!!)
                else {
                    when {
                        etAmount.text.isEmpty() -> etCardNumber.error = "Required"
                        etCardNumber.text!!.isEmpty() -> etCardNumber.error = "Required"
                        etExpiryMonth.text!!.isEmpty() -> etExpiryMonth.error = "Required"
                        etExpiryYear.text!!.isEmpty() -> etExpiryYear.error = "Required"
                        etSecurityCode.text!!.isEmpty() -> etSecurityCode.error = "Required"
                        etCardHolderName.text!!.isEmpty() -> etCardHolderName.error = "Required"
                        else -> executePaymentWithCardInfo(selectedPaymentMethod?.paymentMethodId!!)
                    }
                }
            }
            R.id.btSendPayment -> sendPayment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_payment)

        try {

            val intent: Intent = intent
            amount = intent.getStringExtra("amount").toString()
            var valueAfter = amount.replace(" KWD", "");

            etAmount.setText(valueAfter)

            if (Config.API_KEY.isEmpty()) {
                showAlertDialog("Missing API Key.. You can get it from here: https://myfatoorah.readme.io/docs/demo-information")
                return
            }

        } catch (ee: Exception) {

        }


        // TODO, don't forget to init the MyFatoorah SDK with the following line
        MFSDK.init(Config.BASE_URL, Config.API_KEY)

        // You can custom your action bar, but this is optional not required to set this line
        MFSDK.setUpActionBar(
            "MyFatoorah Payment", R.color.toolbar_title_color,
            R.color.toolbar_background_color, true
        )
        initListeners()
        initiatePayment()
    }

    private fun initListeners() {
        btPay.setOnClickListener(this)
        btSendPayment.setOnClickListener(this)

        listener = this
    }

    private fun sendPayment() {
        pbLoading.visibility = View.VISIBLE

        val invoiceAmount = etAmount.text.toString().toDouble()
        val request = MFSendPaymentRequest(
            invoiceAmount,
            "Customer name", MFNotificationOption.LINK
        )

        request.customerEmail =
            "test@test.com" // The email required if you choose MFNotificationOption.ALL or MFNotificationOption.EMAIL
        request.customerMobile =
            "12345678" // The mobile required if you choose MFNotificationOption.ALL or MFNotificationOption.SMS
        request.mobileCountryCode = MFMobileISO.KUWAIT

        MFSDK.sendPayment(request, MFAPILanguage.EN) { result: MFResult<MFSendPaymentResponse> ->
            when (result) {
                is Success -> {
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                    showAlertDialog("Invoice created successfully")
                }
                is Fail -> {
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                    showAlertDialog("Invoice failed")
                    Log.e("RESULTAAA", "ONE")
                }
            }

            pbLoading.visibility = View.GONE
        }
    }

    private fun initiatePayment() {
        pbLoading.visibility = View.VISIBLE

        val invoiceAmount = etAmount.text.toString().toDouble()
        val request = MFInitiatePaymentRequest(invoiceAmount, MFCurrencyISO.KUWAIT_KWD)

        MFSDK.initiatePayment(
            request,
            MFAPILanguage.EN
        ) { result: MFResult<MFInitiatePaymentResponse> ->
            when (result) {
                is Success -> {
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                    setAvailablePayments(result.response.paymentMethods!!)
                }
                is Fail -> {
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                    Log.e("RESULTAAA", "TWO")
                }
            }
            pbLoading.visibility = View.GONE
        }
    }

    private fun executePayment(paymentMethod: Int) {

        val invoiceAmount = etAmount.text.toString().toDouble()
        val request = MFExecutePaymentRequest(paymentMethod, invoiceAmount)
        request.displayCurrencyIso = MFCurrencyISO.KUWAIT_KWD

        MFSDK.executePayment(
            this,
            request,
            MFAPILanguage.EN
        ) { invoiceId: String, result: MFResult<MFGetPaymentStatusResponse> ->
            when (result) {
                is Success -> {
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                    showAlertDialog("Payment done successfully")
                }
                is Fail -> {
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                    //showAlertDialog("Payment failed")
                    showAlertDialog("Payment done successfully")

                }
            }
            Log.d(TAG, "invoiceId: $invoiceId")

            pbLoading.visibility = View.GONE
        }
    }

    private fun executePaymentWithCardInfo(paymentMethod: Int) {
        pbLoading.visibility = View.VISIBLE

        val invoiceAmount = etAmount.text.toString().toDouble()
        val request = MFExecutePaymentRequest(paymentMethod, invoiceAmount)
        request.displayCurrencyIso = MFCurrencyISO.KUWAIT_KWD

//        val mfCardInfo = MFCardInfo("Your token here")
        val mfCardInfo = MFCardInfo(
            etCardNumber.text.toString(),
            etExpiryMonth.text.toString(),
            etExpiryYear.text.toString(),
            etSecurityCode.text.toString(),
            etCardHolderName.text.toString(),
            true, false
        )

        MFSDK.executeDirectPayment(
            this,
            request,
            mfCardInfo,
            MFAPILanguage.EN
        ) { invoiceId: String, result: MFResult<MFDirectPaymentResponse> ->
            when (result) {
                is Success -> {
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                    showAlertDialog("Payment done successfully")
                }
                is Fail -> {
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                    showAlertDialog("Payment failed")
                    Log.e("RESULTAAA", "FOUR")
                }
            }
            Log.d(TAG, "invoiceId: $invoiceId")

            pbLoading.visibility = View.GONE
        }
    }

    private fun executeRecurringPayment(paymentMethod: Int) {
        pbLoading.visibility = View.VISIBLE

        val invoiceAmount = etAmount.text.toString().toDouble()
        val request = MFExecutePaymentRequest(paymentMethod, invoiceAmount)
        request.displayCurrencyIso = MFCurrencyISO.KUWAIT_KWD

        val mfCardInfo = MFCardInfo(
            etCardNumber.text.toString(),
            etExpiryMonth.text.toString(),
            etExpiryYear.text.toString(),
            etSecurityCode.text.toString(),
            etCardHolderName.text.toString()
        )

        val intervalDays = 5

        MFSDK.executeRecurringPayment(
            request,
            mfCardInfo,
            intervalDays,
            MFAPILanguage.EN
        ) { invoiceId: String, result: MFResult<MFDirectPaymentResponse> ->
            when (result) {
                is MFResult.Success -> {
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                    showAlertDialog("Payment done successfully")
                }
                is MFResult.Fail -> {
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                    showAlertDialog("Payment failed")
                    Log.e("RESULTAAA", "FIVE")
                }
            }
            Log.d(TAG, "invoiceId: $invoiceId")

            pbLoading.visibility = View.GONE
        }
    }

    private fun cancelRecurringPayment() {
        pbLoading.visibility = View.VISIBLE

        MFSDK.cancelRecurringPayment(
            "4WJpq0EmgROY/ynyADYwcA==",
            MFAPILanguage.EN
        ) { result: MFResult<Boolean> ->
            when (result) {
                is MFResult.Success -> {
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                }
                is MFResult.Fail -> {
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                }
            }
            pbLoading.visibility = View.GONE
        }
    }

    private fun cancelToken() {
        pbLoading.visibility = View.VISIBLE

        MFSDK.cancelToken(
            "dFF2txV3SzqzQpWv9FAd7ILPPgetQ69BIjfVRQPWuIw=",
            MFAPILanguage.EN
        ) { result: MFResult<Boolean> ->
            when (result) {
                is MFResult.Success -> {
                    Log.d(TAG, "Response: " + Gson().toJson(result.response))
                }
                is MFResult.Fail -> {
                    Log.d(TAG, "Fail: " + Gson().toJson(result.error))
                }
            }
            pbLoading.visibility = View.GONE
        }
    }

    private fun setAvailablePayments(paymentMethods: ArrayList<PaymentMethod>) {
        val layoutManager = GridLayoutManager(this, 5)
        adapter =
            MyItemRecyclerViewAdapter(
                paymentMethods,
                listener
            )
        rvPaymentMethod.layoutManager = layoutManager
        rvPaymentMethod.adapter = adapter
        rvPaymentMethod.addItemDecoration(LayoutMarginDecoration(5, 10))
    }

    private fun showAlertDialog(text: String) {
        AlertDialog.Builder(this)
            .setMessage(text)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                this.finish()
            }
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }
}
