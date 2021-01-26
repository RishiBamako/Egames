package com.rginfotech.egames;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.VideoView;

import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.localization.LocaleManager;
import com.rginfotech.egames.utility.CommanMethod;
import com.rginfotech.egames.utility.SessionManager;
import com.rginfotech.egames.utility.SortFilterSessionManager;
import com.splunk.mint.Mint;

import java.util.Locale;

public class MainActivity extends BaseActivity {
    private Animation animation ,zoom;
    private ImageView slide_image;
    //private TextView app_name_textview;
    private SessionManager sessionManager;
    SortFilterSessionManager sortFilterSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sortFilterSessionManager = new SortFilterSessionManager(MainActivity.this);
        sortFilterSessionManager.setFilter_Brands("");

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        Mint.initAndStartSession(this.getApplication(), "e53610c7");

        sessionManager = new SessionManager(this);
        if (TextUtils.isEmpty(sessionManager.getRandomValue())){
            sessionManager.setRandomValue(CommanMethod.getRandomNumber());
        }
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            finishAnimation();
          /*  findViewById(R.id.logos_lay).setVisibility(View.GONE);
            findViewById(R.id.main_logo_lay).setVisibility(View.VISIBLE);

        animateMainLogo();
            animateSecurityLogo();*/
        }, 3000);


        try {
            VideoView videoHolder = findViewById(R.id.video_view);
            //setContentView(videoHolder);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash_video_bg);

            videoHolder.setVideoURI(video);
            videoHolder.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                                // video started; hide the placeholder.
                                View placeholder_view = findViewById(R.id.placeholder_view);
                                placeholder_view.setVisibility(View.GONE);
                                return true;
                            }
                            return false;
                        }
                    });

                }
            });

            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    finishAnimation();
                }
            });

            videoHolder.start();

        } catch (Exception ex) {
            //finishAnimation();
        }

      /*  //animateAllCompaniesLogo();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishAnimation();
                findViewById(R.id.logos_lay).setVisibility(View.GONE);
                findViewById(R.id.main_logo_lay).setVisibility(View.VISIBLE);

                animateMainLogo();
                animateSecurityLogo();
            }
        }, 5000);*/
    }

    private void finishAnimation(){
        // if(!sessionManager.getCountryCode().isEmpty() && !sessionManager.getCurrencyCode().isEmpty()){
                    /*Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                    finish();*/
        System.out.println("lan "+Locale.getDefault().getLanguage());

            /*if(Locale.getDefault().getLanguage().equals("en")){
                setNewLocale(getLanguageNotation(0));
            }else{
                setNewLocale(getLanguageNotation(1));
            }*/
        if(sessionManager.getLanguageSelected().equalsIgnoreCase(""))
            setEnglishByDefault(LocaleManager.LANGUAGE_ENGLISH);


        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
     /*   }else{
            System.out.println("lan "+Locale.getDefault().getLanguage());
            Intent intent = new Intent(MainActivity.this,CountryActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    private void setNewLocale(String language) {
        LocaleManager.setNewLocale(this, language);
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public String getLanguageNotation(int position){
        switch (position){
            case 0:
                return LocaleManager.LANGUAGE_ENGLISH;
            case 1:
                return LocaleManager.LANGUAGE_ARABIC;
            default:
                return LocaleManager.LANGUAGE_ENGLISH;

        }
    }
/*

    public void animateMainLogo(){
        final Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        ImageView slide_image = (ImageView)findViewById(R.id.slide_image);
        slide_image.startAnimation(zoom);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //slide_image.clearAnimation();
                //finishAnimation();
                */
/*final Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
                ImageView slide_image = (ImageView)findViewById(R.id.slide_image);
                slide_image.startAnimation(zoom);*//*

                finishAnimation();
            }
        }, 2000);

    }

    public void animateSecurityLogo(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //slide_image.clearAnimation();
                //finishAnimation();
                findViewById(R.id.security_iv).setVisibility(View.VISIBLE);
                final Animation zoom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in_short);
                ImageView slide_image = (ImageView)findViewById(R.id.security_iv);
                slide_image.startAnimation(zoom);
            }
        }, 1000);

    }


    public void animateAllCompaniesLogo() {
        findViewById(R.id.logos_lay).setVisibility(View.VISIBLE);
        findViewById(R.id.main_logo_lay).setVisibility(View.GONE);
        View[] views = new View[]{findViewById(R.id.freshlook_iv),
                findViewById(R.id.bella_iv),
                findViewById(R.id.john_iv),
                findViewById(R.id.alcon_iv),
                findViewById(R.id.rayban_iv),
                findViewById(R.id.gucci_iv)};

// 100ms delay between Animations
        int delayBetweenAnimations = 500;

        for (int i = 0; i < views.length; i++) {
            final View view = views[i];

            // We calculate the delay for this Animation, each animation starts 100ms
            // after the previous one
            int delay = i * delayBetweenAnimations;

            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in_short);
                    view.startAnimation(animation);
                }
            }, delay);
        }
    }
*/

    private void setEnglishByDefault(String language) {
        LocaleManager.setNewLocale(this, language);
        sessionManager.setLanguageSelected(language);
    }
}
