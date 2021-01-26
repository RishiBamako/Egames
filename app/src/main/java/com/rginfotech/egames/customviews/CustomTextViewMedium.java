package com.rginfotech.egames.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.rginfotech.egames.api.API;

public class CustomTextViewMedium extends TextView {

    public CustomTextViewMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewMedium(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(API.FONT_MEDIUM, context);
        setTypeface(customFont);
    }
}
