package com.rginfotech.egames.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

import com.rginfotech.egames.api.API;

public class CustomEditTextNormal extends EditText {

    public CustomEditTextNormal(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public CustomEditTextNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomEditTextNormal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(API.FONT_NORMAL, context);
        setTypeface(customFont);
    }
}
