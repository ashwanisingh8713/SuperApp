package com.ns.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by ashwanisingh on 11/10/18.
 */

public class WebViewClientForWebPage {

    private boolean mIsErrorPageLoaded;



    public void linkClick(WebView webView, Context context, ProgressBar mProgressBar) {
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //javascript support
        webView.getSettings().setJavaScriptEnabled(true);
        //html5 support
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        webView.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(mProgressBar != null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if(mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                }
                loadErrorPage(view);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if(mProgressBar != null) {
                    mProgressBar.setVisibility(View.GONE);
                }
                loadErrorPage(view);
            }


        });
    }




    private void loadErrorPage(WebView view) {
        if(isErrorPageLoaded()) {
            return;
        }
        setIsErrorPageLoaded(true);
        if(!BaseAcitivityTHP.sIsOnline) {
            view.loadUrl("about:blank");
            view.loadUrl("file:///android_asset/web/web_error.html");
        }
    }

    public boolean isErrorPageLoaded() {
        return mIsErrorPageLoaded;
    }

    public void setIsErrorPageLoaded(boolean mIsErrorPageLoaded) {
        this.mIsErrorPageLoaded = mIsErrorPageLoaded;
    }

}
