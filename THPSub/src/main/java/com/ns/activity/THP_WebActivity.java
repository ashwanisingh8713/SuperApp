package com.ns.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

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
    private String mUrl;
    private String mFrom;

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
        getDetailToolbar().getPremiumLogoBtn().setVisibility(View.VISIBLE);

        if(mFrom != null) {
            getDetailToolbar().setTitle(mFrom);
        }



        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);


        new WebViewLinkClick(false).linkClick(mWebView, this, mProgressBar);

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
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
