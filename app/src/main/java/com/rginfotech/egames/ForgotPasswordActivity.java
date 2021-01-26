package com.rginfotech.egames;

import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.R;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.utility.CommanMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener{

    private ImageView back_image;
    private EditText forgot_email_edittext;
    private Button reset_password_button;
    private String getEmail;
    private GifImageView gifImageView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        back_image = (ImageView)findViewById(R.id.back_image);
        findViewById(R.id.back_image).setOnClickListener(this);
        forgot_email_edittext = (EditText)findViewById(R.id.forgot_email_edittext);
        reset_password_button = (Button)findViewById(R.id.reset_password_button);
        findViewById(R.id.reset_password_button).setOnClickListener(this);
        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
            case R.id.reset_password_button:
                validation();
                break;
        }
    }


 private void validation(){
     getEmail = forgot_email_edittext.getText().toString();
     if(getEmail.equals("") || getEmail.length()==0){
         CommanMethod.getCustomOkAlert(this, this.getString(R.string.toast_message_forgot_email));
         //Toast.makeText(this,this.getString(R.string.toast_message_forgot_email),Toast.LENGTH_SHORT).show();
       }else{
         if (CommanMethod.isInternetConnected(ForgotPasswordActivity.this)){
             resetPassword();
         }
        }
     }

 private void resetPassword(){
     gifImageView.setVisibility(View.VISIBLE);
     RequestQueue mRequestQueue = Volley.newRequestQueue(this);
     StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"forget_password", new com.android.volley.Response.Listener<String>() {
         @Override
         public void onResponse(String response) {
             try {
                 gifImageView.setVisibility(View.GONE);
                 JSONObject object = new JSONObject(response);
                 String status = object.getString("status");
                 String message = CommanMethod.getMessage(ForgotPasswordActivity.this, object);
                 if(status.equals("success")){
                     Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(ForgotPasswordActivity.this,HomeActivity.class);
                     intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                     startActivity(intent);
                 }else{
                     CommanMethod.getCustomOkAlert(ForgotPasswordActivity.this, message);
                     //Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                 }
             }catch (JSONException e){
                 e.printStackTrace();
                 gifImageView.setVisibility(View.GONE);
             }

         }
     }, new com.android.volley.Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {
             gifImageView.setVisibility(View.GONE);
         }
     })
     {
         @Override
         protected Map<String, String> getParams()
         {
             Map<String, String>  params = new HashMap<String, String>();
             params.put("email", getEmail);
             return params;
         }
     };

     mStringRequest.setRetryPolicy(new RetryPolicy() {
         @Override
         public int getCurrentTimeout() {
             return 50000;
         }

         @Override
         public int getCurrentRetryCount() {
             return 50000;
         }

         @Override
         public void retry(VolleyError error) throws VolleyError {

         }
     });
     mRequestQueue.add(mStringRequest);
    }
}
