package com.rginfotech.egames.localization;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.android.volley.VolleyLog;
import com.rginfotech.egames.pushnotification.OneSignalNotificationOpenedHandler;
import com.rginfotech.egames.pushnotification.OneSignalNotificationReceivedHandler;
import com.onesignal.OneSignal;
import com.rginfotech.egames.utility.FontOverride;

public class App extends Application {

    private final String TAG = "App";

    /*For language and multidex*/

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyLog.DEBUG = false;


        // OneSignal Initialization
        /*OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();*/

        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new OneSignalNotificationReceivedHandler(this))
                .setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler(this))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        if (LocaleManager.getLanguage(getApplicationContext()).equals("en")) {
            FontOverride.setDefaultFont(this, "DEFAULT", "font_en/futuramediumbt.ttf");
            FontOverride.setDefaultFont(this, "MONOSPACE", "font_en/futuraboldfont.ttf");
            FontOverride.setDefaultFont(this, "SERIF", "font_en/futuralight.ttf");
            FontOverride.setDefaultFont(this, "SANS_SERIF", "font_en/futuralight.ttf");
        }
        else
        {
            FontOverride.setDefaultFont(this, "DEFAULT", "font_ar/futuramediumbt.ttf");
            FontOverride.setDefaultFont(this, "MONOSPACE", "font_ar/futuraboldfont.ttf");
            FontOverride.setDefaultFont(this, "SERIF", "font_ar/futuralight.ttf");
            FontOverride.setDefaultFont(this, "SANS_SERIF", "font_ar/futuralight.ttf");
        }


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        //MultiDex.install(this);
        Log.d(TAG, "attachBaseContext");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
        Log.d(TAG, "onConfigurationChanged: " + newConfig.locale.getLanguage());
    }
}