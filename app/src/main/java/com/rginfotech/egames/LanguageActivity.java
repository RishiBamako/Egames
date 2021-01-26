package com.rginfotech.egames;

import android.os.Bundle;
import android.view.View;

import com.rginfotech.egames.localization.BaseActivity;

public class LanguageActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        findViewById(R.id.back_image).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_image:
                super.onBackPressed();
                break;
        }
    }
}
