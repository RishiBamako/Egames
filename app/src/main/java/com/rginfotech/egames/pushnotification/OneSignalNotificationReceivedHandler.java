package com.rginfotech.egames.pushnotification;

import android.content.Context;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class OneSignalNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

    private Context context;
    public OneSignalNotificationReceivedHandler(Context context){
        this.context = context;
    }
    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String notificationID = notification.payload.notificationID;
        String title = notification.payload.title;
        String body = notification.payload.body;
        String smallIcon = notification.payload.smallIcon;
        String largeIcon = notification.payload.largeIcon;
        String bigPicture = notification.payload.bigPicture;
        String smallIconAccentColor = notification.payload.smallIconAccentColor;
        String sound = notification.payload.sound;
        String ledColor = notification.payload.ledColor;
        int lockScreenVisibility = notification.payload.lockScreenVisibility;
        String groupKey = notification.payload.groupKey;
        String groupMessage = notification.payload.groupMessage;
        String fromProjectNumber = notification.payload.fromProjectNumber;
        String rawPayload = notification.payload.rawPayload;

        String customKey;
//{"notification":"new_offer","family_id":"63","offer_id":53,"brand_id":"6"}
        //{"notification":"new_offer","family_id":"7","offer_id":54,"brand_id":"2"}
        Log.i("OneSignalExample", "NotificationID received: " + notificationID);

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }
    }
}
