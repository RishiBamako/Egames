package com.rginfotech.egames;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rginfotech.egames.fragment.ArabicFragment;
import com.rginfotech.egames.fragment.EnglishFragment;
import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.localization.LocaleManager;
import com.google.android.material.tabs.TabLayout;
import com.rginfotech.egames.utility.CommanMethod;

import java.util.Locale;

public class CountryActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);
        findViewById(R.id.back_image).setOnClickListener(this);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        //TabLayout.Tab tab = tabLayout.getTabAt(1);
        //tab.select();
       /*LinearLayout tabStrip = ((LinearLayout)tabLayout.getChildAt(0));
        for(int i = 1; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }*/

        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            ((ImageView)findViewById(R.id.back_image)).setImageResource(R.drawable.arrow_30);
        }else{
            ((ImageView)findViewById(R.id.back_image)).setImageResource(R.drawable.arrow_right_30);
        }

        if(Locale.getDefault().getLanguage().equals(LocaleManager.LANGUAGE_ENGLISH)){
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.check_frameLayout, new EnglishFragment());
            transaction.commit();
        }else{
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.check_frameLayout, new ArabicFragment());
            transaction.commit();
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        /*((TextView)findViewById(R.id.language_text)).setText(CommanMethod.getStringByLocal(CountryActivity.this, R.string.select_language, "en"));
                        ((TextView)findViewById(R.id.country_text)).setText(CommanMethod.getStringByLocal(CountryActivity.this, R.string.select_country, "en"));*/
                        ((TextView)findViewById(R.id.language_text)).setText("Please select your language");
                        ((TextView)findViewById(R.id.country_text)).setText("Please select your country");
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.check_frameLayout, new EnglishFragment());
                        transaction.commit();
                        break;
                    case 1:
                        /*((TextView)findViewById(R.id.language_text)).setText(CommanMethod.getStringByLocal(CountryActivity.this, R.string.select_language, "ar"));
                        ((TextView)findViewById(R.id.country_text)).setText(CommanMethod.getStringByLocal(CountryActivity.this, R.string.select_country, "ar"));*/
                        ((TextView)findViewById(R.id.language_text)).setText("اختر لغتك");
                        ((TextView)findViewById(R.id.country_text)).setText("اختر بلدك");
                        FragmentManager fragmentManager1 = getSupportFragmentManager();
                        FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                        transaction1.replace(R.id.check_frameLayout, new ArabicFragment());
                        transaction1.commit();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
