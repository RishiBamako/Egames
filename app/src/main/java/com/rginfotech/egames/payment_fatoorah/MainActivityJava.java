package com.rginfotech.egames.payment_fatoorah;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.myfatoorah.sdk.model.executepayment.MFExecutePaymentRequest;
import com.myfatoorah.sdk.model.executepayment_cardinfo.MFCardInfo;
import com.myfatoorah.sdk.model.executepayment_cardinfo.MFDirectPaymentResponse;
import com.myfatoorah.sdk.model.initiatepayment.MFInitiatePaymentRequest;
import com.myfatoorah.sdk.model.initiatepayment.MFInitiatePaymentResponse;
import com.myfatoorah.sdk.model.initiatepayment.PaymentMethod;
import com.myfatoorah.sdk.model.paymentstatus.MFGetPaymentStatusResponse;
import com.myfatoorah.sdk.model.sendpayment.MFSendPaymentRequest;
import com.myfatoorah.sdk.model.sendpayment.MFSendPaymentResponse;
import com.myfatoorah.sdk.utils.MFAPILanguage;
import com.myfatoorah.sdk.utils.MFCurrencyISO;
import com.myfatoorah.sdk.utils.MFMobileISO;
import com.myfatoorah.sdk.utils.MFNotificationOption;
import com.myfatoorah.sdk.views.MFResult;
import com.myfatoorah.sdk.views.MFResult.*;
import com.myfatoorah.sdk.views.MFSDK;
import com.rginfotech.egames.R;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;
import kotlin.Unit;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainActivityJava extends AppCompatActivity implements View.OnClickListener,
        OnListFragmentInteractionListener {

    private MyItemRecyclerViewAdapter adapter = null;
    private PaymentMethod selectedPaymentMethod = null;
    private String TAG = MainActivityKotlin.class.getSimpleName();
    private OnListFragmentInteractionListener listener = null;
    private Button btPay;
    private Button btSendPayment;
    private ProgressBar pbLoading;
    private EditText etAmount;
    private EditText etCardNumber;
    private EditText etExpiryMonth;
    private EditText etExpiryYear;
    private EditText etSecurityCode;
    private EditText etCardHolderName;
    private RecyclerView rvPaymentMethod;
    private LinearLayout llDirectPaymentLayout;

    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_payment);




        if(Config.API_KEY.isEmpty()) {
            showAlertDialog("Missing API Key.. You can get it from here: https://myfatoorah.readme.io/docs/demo-information");
            return;
        }

        // TODO, don't forget to init the MyFatoorah SDK with the following line
        MFSDK.INSTANCE.init(Config.BASE_URL, Config.API_KEY);

        // You can custom your action bar, but this is optional not required to set this line
        MFSDK.INSTANCE.setUpActionBar("MyFatoorah Payment", R.color.toolbar_title_color,
                R.color.toolbar_background_color, true);

        initViews();
        initListeners();
        initiatePayment();
    }

    private void initViews() {
        btPay = findViewById(R.id.btPay);
        btSendPayment = findViewById(R.id.btSendPayment);
        pbLoading = findViewById(R.id.pbLoading);
        etAmount = findViewById(R.id.etAmount);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiryMonth = findViewById(R.id.etExpiryMonth);
        etExpiryYear = findViewById(R.id.etExpiryYear);
        etSecurityCode = findViewById(R.id.etSecurityCode);
        etCardHolderName = findViewById(R.id.etCardHolderName);
        rvPaymentMethod = findViewById(R.id.rvPaymentMethod);
        llDirectPaymentLayout = findViewById(R.id.llDirectPaymentLayout);

        btPay.setOnClickListener(this);
        btSendPayment.setOnClickListener(this);

        if(getIntent().getExtras()!=null){
            amount = getIntent().getExtras().getString("amount");
            String valueAfter = amount.replace(" KWD","");
            etAmount.setText(valueAfter);
        }
    }


    private void initListeners() {
        btPay.setOnClickListener(this);
        btSendPayment.setOnClickListener(this);

        listener = this;
    }

    private void sendPayment(){
        pbLoading.setVisibility(View.VISIBLE);

        double invoiceAmount = Double.parseDouble(etAmount.getText().toString());
        MFSendPaymentRequest request = new MFSendPaymentRequest(invoiceAmount,
                "Customer name", MFNotificationOption.LINK);

        request.setCustomerEmail("test@test.com"); // The email required if you choose MFNotificationOption.ALL or MFNotificationOption.EMAIL
        request.setCustomerMobile("12345678"); // The mobile required if you choose MFNotificationOption.ALL or MFNotificationOption.SMS
        request.setMobileCountryCode(MFMobileISO.KUWAIT);

        MFSDK.INSTANCE.sendPayment(request, MFAPILanguage.EN, (MFResult<MFSendPaymentResponse> result) -> {
            if (result instanceof Success) {
                Log.d(TAG, "Response: " + new Gson().toJson(
                        ((Success<MFSendPaymentResponse>) result).getResponse()));
                showAlertDialog("Invoice created successfully");
            } else if (result instanceof Fail) {
                Log.d(TAG, "Error: " + new Gson().toJson(((Fail) result).getError()));
                showAlertDialog("Invoice failed");
            }
            pbLoading.setVisibility(View.GONE);

            return Unit.INSTANCE;
        });
    }

    private void initiatePayment() {
        pbLoading.setVisibility(View.VISIBLE);

        double invoiceAmount = Double.parseDouble(etAmount.getText().toString());
        MFInitiatePaymentRequest request = new MFInitiatePaymentRequest(
                invoiceAmount, MFCurrencyISO.KUWAIT_KWD);

        MFSDK.INSTANCE.initiatePayment(request, MFAPILanguage.EN,
                (MFResult<MFInitiatePaymentResponse> result) -> {
            if(result instanceof Success) {
                Log.d(TAG, "Response: " + new Gson().toJson(
                        ((Success<MFInitiatePaymentResponse>) result).getResponse()));
                setAvailablePayments((((Success<MFInitiatePaymentResponse>) result)
                        .getResponse().getPaymentMethods()));
            }
            else if(result instanceof Fail) {
                Log.d(TAG, "Error: " + new Gson().toJson(((Fail) result).getError()));
            }
            pbLoading.setVisibility(View.GONE);

            return Unit.INSTANCE;
        });
    }

    private void executePayment(Integer paymentMethod) {

        double invoiceAmount = Double.parseDouble(etAmount.getText().toString());
        MFExecutePaymentRequest request = new MFExecutePaymentRequest(paymentMethod, invoiceAmount);
        request.setDisplayCurrencyIso(MFCurrencyISO.KUWAIT_KWD);

        MFSDK.INSTANCE.executePayment(this, request, MFAPILanguage.EN,
                (String invoiceId, MFResult<MFGetPaymentStatusResponse> result) -> {
                    if (result instanceof Success) {
                        Log.d(TAG, "Response: " + new Gson().toJson(
                                ((Success<MFGetPaymentStatusResponse>) result).getResponse()));
                        showAlertDialog("Payment done successfully");
                    } else if (result instanceof Fail) {
                        Log.d(TAG, "Error: " + new Gson().toJson(((Fail) result).getError()));
                        showAlertDialog("Payment failed");
                    }
                    Log.d(TAG, "invoiceId: " + invoiceId);

                    pbLoading.setVisibility(View.GONE);

                    return Unit.INSTANCE;
                });
    }

    private void executePaymentWithCardInfo(Integer paymentMethod) {
        pbLoading.setVisibility(View.VISIBLE);

        double invoiceAmount = Double.parseDouble(etAmount.getText().toString());
        MFExecutePaymentRequest request = new MFExecutePaymentRequest(paymentMethod, invoiceAmount);
        request.setDisplayCurrencyIso(MFCurrencyISO.KUWAIT_KWD);

//        MFCardInfo mfCardInfo = new MFCardInfo("Your token here");
        MFCardInfo mfCardInfo = new MFCardInfo(
                etCardNumber.getText().toString(),
                etExpiryMonth.getText().toString(),
                etExpiryYear.getText().toString(),
                etSecurityCode.getText().toString(),
                etCardHolderName.getText().toString(),
                true,
                false
        );

        MFSDK.INSTANCE.executeDirectPayment(this, request, mfCardInfo, MFAPILanguage.EN,
                (String invoiceId, MFResult<MFDirectPaymentResponse> result) -> {
                    if (result instanceof Success) {
                        Log.d(TAG, "Response: " + new Gson().toJson(
                                ((Success<MFDirectPaymentResponse>) result).getResponse()));
                        showAlertDialog("Payment done successfully");
                    } else if (result instanceof Fail) {
                        Log.d(TAG, "Error: " + new Gson().toJson(((Fail) result).getError()));
                        showAlertDialog("Payment failed");
                    }
                    Log.d(TAG, "invoiceId:" + invoiceId);

                    pbLoading.setVisibility(View.GONE);

                    return Unit.INSTANCE;
                });
    }

    private void executeRecurringPayment(Integer paymentMethod) {
        pbLoading.setVisibility(View.VISIBLE);

        double invoiceAmount = Double.parseDouble(etAmount.getText().toString());
        MFExecutePaymentRequest request = new MFExecutePaymentRequest(paymentMethod, invoiceAmount);
        request.setDisplayCurrencyIso(MFCurrencyISO.KUWAIT_KWD);

        MFCardInfo mfCardInfo = new MFCardInfo(
                etCardNumber.getText().toString(),
                etExpiryMonth.getText().toString(),
                etExpiryYear.getText().toString(),
                etSecurityCode.getText().toString(),
                etCardHolderName.getText().toString());

        int intervalDays = 5;

        MFSDK.INSTANCE.executeRecurringPayment(request, mfCardInfo, intervalDays, MFAPILanguage.EN,
                (String invoiceId, MFResult<MFDirectPaymentResponse> result) -> {
                    if (result instanceof Success) {
                        Log.d(TAG, "Response: " + new Gson().toJson(
                                ((Success<MFDirectPaymentResponse>) result).getResponse()));
                        showAlertDialog("Payment done successfully");
                    } else if (result instanceof Fail) {
                        Log.d(TAG, "Error: " + new Gson().toJson(((Fail) result).getError()));
                        showAlertDialog("Payment failed");
                    }
                    Log.d(TAG, "invoiceId:" + invoiceId);

                    pbLoading.setVisibility(View.GONE);

                    return Unit.INSTANCE;
                });
    }

    private void cancelRecurringPayment() {
        pbLoading.setVisibility(View.VISIBLE);

        MFSDK.INSTANCE.cancelRecurringPayment("4WJpq0EmgROY/ynyADYwcA==", MFAPILanguage.EN,
                (MFResult<Boolean> result) -> {
                    if (result instanceof Success) {
                        Log.d(TAG, "Response: " + new Gson().toJson(((Success<Boolean>) result).getResponse()));
                    } else if (result instanceof Fail) {
                        Log.d(TAG, "Error: " + new Gson().toJson(((Fail) result).getError()));
                    }

                    pbLoading.setVisibility(View.GONE);

                    return Unit.INSTANCE;
                });
    }

    private void cancelToken() {
        pbLoading.setVisibility(View.VISIBLE);

        MFSDK.INSTANCE.cancelToken("dFF2txV3SzqzQpWv9FAd7ILPPgetQ69BIjfVRQPWuIw=", MFAPILanguage.EN,
                (MFResult<Boolean> result) -> {
                    if (result instanceof Success) {
                        Log.d(TAG, "Response: " + new Gson().toJson(((Success<Boolean>) result).getResponse()));
                    } else if (result instanceof Fail) {
                        Log.d(TAG, "Error: " + new Gson().toJson(((Fail) result).getError()));
                    }

                    pbLoading.setVisibility(View.GONE);

                    return Unit.INSTANCE;
                });
    }

    private void setAvailablePayments(ArrayList<PaymentMethod> paymentMethods) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        adapter = new MyItemRecyclerViewAdapter(paymentMethods, listener);
        rvPaymentMethod.setLayoutManager(layoutManager);
        rvPaymentMethod.setAdapter(adapter);
        rvPaymentMethod.addItemDecoration(new LayoutMarginDecoration(5, 10));
    }

    private void showAlertDialog(String text) {
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> { })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btPay: {
                if (selectedPaymentMethod == null) {
                    showAlertDialog("Please select payment method first");
                    return;
                }

                if (!selectedPaymentMethod.getDirectPayment())
                    executePayment(selectedPaymentMethod.getPaymentMethodId());
                else {
                    if (etAmount.getText() == null || etAmount.getText().toString().isEmpty()) {
                        etCardNumber.setError("Required");
                    } else if (etCardNumber.getText().toString().isEmpty()) {
                        etCardNumber.setError("Required");
                    } else if (etExpiryMonth.getText().toString().isEmpty()) {
                        etExpiryMonth.setError("Required");
                    } else if (etExpiryYear.getText().toString().isEmpty()) {
                        etExpiryYear.setError("Required");
                    } else if (etSecurityCode.getText().toString().isEmpty()) {
                        etSecurityCode.setError("Required");
                    } else {
                        executePaymentWithCardInfo(selectedPaymentMethod.getPaymentMethodId());
                    }
                }
            } break;
            case R.id.btSendPayment:
                sendPayment();
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(int position, @NotNull PaymentMethod paymentMethod) {
        selectedPaymentMethod = paymentMethod;
        adapter.changeSelected(position);

        if(paymentMethod.getDirectPayment())
            llDirectPaymentLayout.setVisibility(View.VISIBLE);
        else
            llDirectPaymentLayout.setVisibility(View.GONE);
    }
}
