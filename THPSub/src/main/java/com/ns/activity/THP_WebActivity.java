package com.ns.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ns.thpremium.R;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.WebViewLinkClick;
import com.ns.view.THP_AutoResizeWebview;

public class THP_WebActivity extends BaseAcitivityTHP {

    @Override
    public int layoutRes() {
        return R.layout.activity_web_thp;
    }

    private THP_AutoResizeWebview mWebView;
    private ProgressBar mProgressBar;
    private TextView mTitleView;

    private String mUrl;
    private String mFrom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.progressBar);
        mTitleView = findViewById(R.id.titleText);

        if(getIntent().getExtras() != null) {
            mUrl = getIntent().getStringExtra("url");
            mFrom = getIntent().getStringExtra("from");
        }

        if(mUrl == null) {
            return;
        }

        if(mFrom.equalsIgnoreCase(getString(R.string.comments))) {
            mTitleView.setText(mFrom);
        }

        // Back Button Click Listener
        findViewById(R.id.backBtn).setOnClickListener(v->
                finish()
        );


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);


        new WebViewLinkClick().linkClick(mWebView, this, mProgressBar);

    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(THP_WebActivity.this, "THP_WebActivity Screen", THP_WebActivity.class.getSimpleName());

    }

    @Override
    protected void onDestroy() {
        mWebView.clearFormData();
        mWebView.clearHistory();
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
