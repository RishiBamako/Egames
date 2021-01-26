package com.rginfotech.egames.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.rginfotech.egames.api.API;

public class CustomTextViewBold extends TextView {

    public CustomTextViewBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(API.FONT_BOLD, context);
        setTypeface(customFont);
    }
}
