package com.rginfotech.egames.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.utility.CommanMethod;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateNotificationService  extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        //Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();

        //myPlayer = MediaPlayer.create(this, R.raw.sun);
        //myPlayer.setLooping(false); // Set looping
    }
    @Override
    public void onStart(Intent intent, int startid) {
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        //myPlayer.start();
    }
    @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
        //myPlayer.stop();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Toast.makeText(this, intent.getStringExtra("isOnline"), Toast.LENGTH_LONG).show();
        updateNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateNotification(){

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, API.BASE_URL+"notification_update", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String message = CommanMethod.getMessage(UpdateNotificationService.this, object);
                    stopSelf();
                    if(status.equals("success")){

                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }else{

                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    //dialog.dismiss();
                    //gifImageView.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //parseVolleyError(error);
                //gifImageView.setVisibility(View.GONE);
                //dialog.dismiss();
                stopSelf();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                if(OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId() != null) {
                    params.put("device_id", OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId());
                }

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
