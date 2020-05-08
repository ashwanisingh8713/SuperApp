package com.ns.contentfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.netoperation.config.model.TabsBean;
import com.ns.callbacks.ToolbarChangeRequired;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.WebViewLinkClick;
import com.ns.view.THP_AutoResizeWebview;

import org.greenrobot.eventbus.EventBus;

public class WebFragment extends BaseFragmentTHP {

    private THP_AutoResizeWebview mWebView;
    private ProgressBar mProgressBar;

    private String mUrl;
    private int mTabIndex;

    public static WebFragment getInstance(int tabIndex, TabsBean tabsBean) {
        Bundle bundle = new Bundle();
        bundle.putString("url", tabsBean.getValue());
        bundle.putInt("tabIndex", tabIndex);
        WebFragment fragment = new WebFragment();
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

        // ToolbarChangeRequired Event Post, It show Toolbar for Sub-Section
        EventBus.getDefault().post(new ToolbarChangeRequired("WebFragment", false, mTabIndex, null, ToolbarChangeRequired.Other_Tabs));

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        new WebViewLinkClick(false).linkClick(mWebView, getActivity(), mProgressBar);
    }
}
