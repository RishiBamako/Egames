package com.rginfotech.egames.pushnotification;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.MainActivity;
import com.rginfotech.egames.MyOrderActivity;
import com.rginfotech.egames.api.API;
import com.rginfotech.egames.service.UpdateNotificationService;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OneSignalNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler{

    private Context context;
    public OneSignalNotificationOpenedHandler(Context context){
        this.context = context;
    }
    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String launchUrl = result.notification.payload.launchURL; // update docs launchUrl

        context.startService(new Intent(context, UpdateNotificationService.class));
        //Object activityToLaunch = MainActivity.class;
        Intent activityToLaunch;
        if (data != null) {
            //customKey = data.optString("customkey", null);
            //openURL = data.optString("openURL", null);
            String notification_type = data.optString("notification", "");
            SessionManager sessionManager = new SessionManager(context);
            //Intent intent;
            if (notification_type.equals("new_offer")) {
                sessionManager.setIsFromOfferNotification(true);
                /*bundle.putString("brand_id", getIntent().getStringExtra(""));
            bundle.putString("brand_name", getIntent().getStringExtra(""));
            bundle.putString("family_id", getIntent().getStringExtra(""));*/
                activityToLaunch = new Intent(context, HomeActivity.class);
                sessionManager.setPushBrandId(data.optString("brand_id", ""));
                sessionManager.setPushFamilyId(data.optString("family_id", ""));
                /*activityToLaunch.putExtra("offer_id",data.optString("offer_id", ""));
                activityToLaunch.putExtra("brand_id",data.optString("brand_id", ""));
                activityToLaunch.putExtra("family_id",data.optString("family_id", ""));*/
                //activityToLaunch = HomeActivity.class;
            }else if (notification_type.equals("order")){
                activityToLaunch = new Intent(context, MyOrderActivity.class);
                //activityToLaunch = MyOrderActivity.class;
            }else {
                activityToLaunch = new Intent(context, HomeActivity.class);
                //activityToLaunch = HomeActivity.class;
            }

            activityToLaunch.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra("openURL", openURL);
            //Log.i("OneSignalExample", "openURL = " + openURL);
            // startActivity(intent);
            context.startActivity(activityToLaunch);
            /*if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);

            if (openURL != null)
                Log.i("OneSignalExample", "openURL to webview with URL value: " + openURL);*/
        }

        /*if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

            if (result.action.actionID.equals("id1")) {
                Log.i("OneSignalExample", "button id called: " + result.action.actionID);
                //activityToLaunch = GreenActivity.class;
            } else
                Log.i("OneSignalExample", "button id called: " + result.action.actionID);
        }*/
        // The following can be used to open an Activity of your choice.
        // Replace - getApplicationContext() - with any Android Context.
        // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
        //Intent intent = new Intent(context, (Class<?>) activityToLaunch);
        // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        //activityToLaunch.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        //intent.putExtra("openURL", openURL);
        //Log.i("OneSignalExample", "openURL = " + openURL);
        // startActivity(intent);
        //context.startActivity(activityToLaunch);

        // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
        //   if you are calling startActivity above.
        /*
           <application ...>
             <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
           </application>
        */
    }



}


