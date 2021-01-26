package com.rginfotech.egames;

import androidx.annotation.RequiresApi;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rginfotech.egames.localization.BaseActivity;
import com.rginfotech.egames.utility.CommanMethod;

public class CommanSocialActivity extends BaseActivity implements View.OnClickListener {

    private TextView title_text_view;
    private String url;
    private String title;
    private WebView webView;
    private ProgressDialog progressDialog;
    private ImageView back_image;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comman_social);
        findViewById(R.id.back_image).setOnClickListener(this);
        back_image = (ImageView)findViewById(R.id.back_image);
        if(getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_LTR){
            back_image.setImageResource(R.drawable.arrow_30);
        }else{
            back_image.setImageResource(R.drawable.arrow_right_30);
        }

        title_text_view = (TextView)findViewById(R.id.title_text_view);
        webView = (WebView)findViewById(R.id.webView);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            url = bundle.getString("url");
            title = bundle.getString("title");
        }
        title_text_view.setText(title);
        startWebView(url);
    }

    private void startWebView(String url) {

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        progressDialog = new ProgressDialog(CommanSocialActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Toast.makeText(CommanSocialActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();
                CommanMethod.getCustomOkAlert(CommanSocialActivity.this, description);

            }
        });
        webView.loadUrl(url);
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
