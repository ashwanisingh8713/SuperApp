package com.ns.viewholder;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.THP_AutoResizeWebview;


public class StaticItemWebViewHolder extends RecyclerView.ViewHolder {

    public THP_AutoResizeWebview webView;
    public ProgressBar progressBar;
    public View mDummyView;

    public StaticItemWebViewHolder(View itemView) {
        super(itemView);
        webView = itemView.findViewById(R.id.staticWebView);
        progressBar = itemView.findViewById(R.id.progressBar);
        mDummyView = itemView.findViewById(R.id.dummyView);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                    webView.loadUrl("about:blank");
                    webView.loadUrl("file:///android_asset/web/web_error.html");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                    webView.loadUrl("about:blank");
                    webView.loadUrl("file:///android_asset/web/web_error.html");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //javascript support
        webView.getSettings().setJavaScriptEnabled(true);
        //html5 support
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void performClick() {
                Toast.makeText(webView.getContext(), "Login clicked", Toast.LENGTH_LONG).show();
            }
        }, "Rajastan");


    }
}
