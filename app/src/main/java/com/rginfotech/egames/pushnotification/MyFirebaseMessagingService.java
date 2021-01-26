package com.rginfotech.egames.pushnotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rginfotech.egames.HomeActivity;
import com.rginfotech.egames.MyOrderActivity;
import com.rginfotech.egames.R;
import com.rginfotech.egames.utility.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("PushNotification", "From: " + remoteMessage.getFrom());

        // Check if message contains ic_account data payload.
        /*Message data payload: {body={"hasAudio" : "","hasVideo" :"",
        "CallerImageURL" :"","liveUrl" : "","CallerName" : ""}, icon=, title=Calling}*/
        if (remoteMessage.getData().size() > 0) {
            Log.d("PushNotification", "Message data payload: " + remoteMessage.getData());
            SessionManager sessionManager = new SessionManager(this);
            Intent intent;
            if (remoteMessage.getData().get("notification_type").equals("new_offer")) {
                sessionManager.setIsFromOfferNotification(true);
                intent = new Intent(this, HomeActivity.class);
            }else if (remoteMessage.getData().get("notification_type").equals("order")){
                intent = new Intent(this, MyOrderActivity.class);
            }else {
                intent = new Intent(this, HomeActivity.class);
            }

            //Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("body"))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(getID(), builder.build());

        }else if (remoteMessage.getNotification() != null) { // Check if message contains ic_account notification payload.

            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Default";
            NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
                manager.createNotificationChannel(channel);
            }
            manager.notify(getID(), builder.build());
        }

        // Also if you intend on generating your own notifications as ic_account result of ic_account received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    public int getID(){
        //Date now = new Date();
        return Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(new Date()));
    }
}
