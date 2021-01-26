package com.rginfotech.egames.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.rginfotech.egames.api.API;

public class CustomTextViewNormal extends TextView {

    public CustomTextViewNormal(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewNormal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewNormal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(API.FONT_NORMAL, context);
        setTypeface(customFont);
    }

}
