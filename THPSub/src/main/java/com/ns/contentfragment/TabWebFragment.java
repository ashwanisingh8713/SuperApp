package com.ns.contentfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ns.callbacks.BackPressCallback;
import com.ns.callbacks.BackPressImpl;
import com.ns.callbacks.ToolbarChangeRequired;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.WebViewClientForArticleDetail;
import com.ns.utils.WebViewClientForWebPage;
import com.ns.view.THP_AutoResizeWebview;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TabWebFragment extends BaseFragmentTHP {

    private THP_AutoResizeWebview mWebView;
    private ProgressBar mProgressBar;

    private String mUrl;
    private int mTabIndex;
    private String mParentSectionName;
    private String mPageSource;

    public static TabWebFragment getInstance(int tabIndex, String pageSource, String url, String parentSectionName) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("pageSource", pageSource);
        bundle.putInt("tabIndex", tabIndex);
        bundle.putString("parentSectionName", parentSectionName);
        TabWebFragment fragment = new TabWebFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_web;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mUrl = getArguments().getString("url");
            mTabIndex = getArguments().getInt("tabIndex");
            mParentSectionName = getArguments().getString("parentSectionName");
            mPageSource = getArguments().getString("pageSource");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = view.findViewById(R.id.webview);
        mProgressBar = view.findViewById(R.id.progressBar);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("handleEvent", "register() ::  "+mPageSource+" :: "+mTabIndex);
        EventBus.getDefault().register(this);
        // ToolbarChangeRequired Event Post, It show Toolbar for Sub-Section
        EventBus.getDefault().post(new ToolbarChangeRequired(mPageSource, false, mTabIndex, mParentSectionName, ToolbarChangeRequired.OTHER_LISTING_TOPBAR));

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        new WebViewClientForWebPage().linkClick(mWebView, getActivity(), mProgressBar);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("handleEvent", "unregister() ::  "+mPageSource+" :: "+mTabIndex);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(BackPressImpl backPress) {
        Log.i("handleEvent", "Back Button Pressed ::  "+mPageSource+" :: "+mTabIndex);

        // ToolbarChangeRequired Event Post, It shows Toolbar for Section
        EventBus.getDefault().post(new ToolbarChangeRequired(mPageSource, true, mTabIndex, null, ToolbarChangeRequired.SECTION_LISTING_TOPBAR));

        // Send Back to AppTabActivity.java => handleEvent(BackPressCallback backPressCallback)
        BackPressCallback backPressCallback = new BackPressImpl(this, mPageSource, mTabIndex).onBackPressed();
        EventBus.getDefault().post(backPressCallback);
    }
}
