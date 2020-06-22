package com.ns.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.ns.thpremium.R;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.WebViewClientForWebPage;

public class THP_WebActivity extends BaseAcitivityTHP {

    @Override
    public int layoutRes() {
        return R.layout.activity_web_thp;
    }

    private WebView mWebView;
    private ProgressBar mProgressBar;
    private String mUrl;
    private String mFrom;

    private WebViewClientForWebPage mWebViewLinkClick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = findViewById(R.id.webview);
        mProgressBar = findViewById(R.id.progressBar);
        if(getIntent().getExtras() != null) {
            mUrl = getIntent().getStringExtra("url");
            mFrom = getIntent().getStringExtra("from");
        }

        if(mUrl == null) {
            return;
        }

        /*if(mFrom.equalsIgnoreCase(getString(R.string.comments))) {
            mTitleView.setText(mFrom);
        }*/

        getDetailToolbar().getBackView().setVisibility(View.VISIBLE);
        //getDetailToolbar().getPremiumLogoBtn().setVisibility(View.VISIBLE);

        if(mFrom != null) {
            getDetailToolbar().setTitle(mFrom);
        }


        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);

        mWebViewLinkClick = new WebViewClientForWebPage();
        mWebViewLinkClick.linkClick(mWebView, this, mProgressBar);

        bottomBannerAds();

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
        if(mWebView.canGoBack() && !mWebViewLinkClick.isErrorPageLoaded()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
