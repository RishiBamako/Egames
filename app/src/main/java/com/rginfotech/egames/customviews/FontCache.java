package com.rginfotech.egames.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.rginfotech.egames.utility.SessionManager;

import java.util.HashMap;

public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = fontCache.get(fontName);

        SessionManager sessionManager = new SessionManager(context);
        if (typeface == null) {
            try {

                if ("ar".equals(sessionManager.getLanguageSelected())) {
                    typeface = Typeface.createFromAsset(context.getAssets(), "font_ar/"+fontName);

                }
                else if ("en".equals(sessionManager.getLanguageSelected())) {
                    typeface = Typeface.createFromAsset(context.getAssets(), "font_en/"+fontName);
                }
                else
                {
                    typeface = Typeface.createFromAsset(context.getAssets(), fontName);
                }

            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontName, typeface);
        }

        return typeface;
    }
}
