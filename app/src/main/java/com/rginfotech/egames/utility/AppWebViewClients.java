package com.rginfotech.egames.utility;

import android.app.Dialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by user on 22/2/18.
 */

public class AppWebViewClients extends WebViewClient {
    private Dialog progressBar;
    private WebView webView;

    public AppWebViewClients(WebView webView, Dialog progressBar) {
        this.progressBar = progressBar;
        this.webView = webView;
        webView.setVisibility(View.GONE);
        progressBar.show();
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);

        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        progressBar.dismiss();
        webView.setVisibility(View.VISIBLE);

    }
}