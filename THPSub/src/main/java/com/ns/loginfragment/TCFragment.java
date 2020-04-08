package com.ns.loginfragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.MailTo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.CustomProgressBar;

import java.util.List;

public class TCFragment extends BaseFragmentTHP {

    private WebView tc_webView;
    private ImageButton backBtn;
    private String mWebUrl;
    private CustomProgressBar mProgressBar;
    private String mFrom;
    AlertDialog alertDialog;

    public static TCFragment getInstance(String url, String from) {
        TCFragment fragment = new TCFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_tc;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mWebUrl = getArguments().getString("url");
            mFrom = getArguments().getString("from");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tc_webView = view.findViewById(R.id.tc_webView);
        mProgressBar = view.findViewById(R.id.progressBar);
        backBtn = view.findViewById(R.id.backBtn);

        if(mFrom != null && mFrom.equalsIgnoreCase("crossBackImg")) {
            if(mIsDayTheme) {
                backBtn.setImageResource(R.drawable.ic_close_ss);
            } else {
                backBtn.setImageResource(R.drawable.ic_close_ss_dark);
            }
        } else if(mFrom != null && mFrom.equalsIgnoreCase("arrowBackImg")) {
            if(mIsDayTheme) {
                backBtn.setImageResource(R.drawable.ic_back_copy_42);
            } else {
                backBtn.setImageResource(R.drawable.ic_back_copy_42_dark);
            }
        }

        // Back Btn click listener
        backBtn.setOnClickListener(v->{
            FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
        });

        tc_webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //javascript support
        tc_webView.getSettings().setJavaScriptEnabled(true);
        //html5 support
        tc_webView.getSettings().setDomStorageEnabled(true);
        tc_webView.getSettings().setAllowContentAccess(true);
        tc_webView.getSettings().setLoadWithOverviewMode(true);

        tc_webView.loadUrl(mWebUrl);


        tc_webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().startsWith("mailto:")) {
                        MailTo mt = MailTo.parse(request.getUrl().toString());
                        sendEmail(mt.getTo());
                        return true;
                    } else {
                        return super.shouldOverrideUrlLoading(view, request);
                    }
                } else {
                    return super.shouldOverrideUrlLoading(view, request);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mProgressBar.setVisibility(View.GONE);
            }

        });

    }


    @Override
    public void onDetach() {
        super.onDetach();
        tc_webView.stopLoading();
    }

    private void sendEmail(String toAddress) {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{toAddress});
            emailIntent.putExtra(Intent.EXTRA_TEXT, "");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            emailIntent.putExtra(Intent.EXTRA_CC, "");
            emailIntent.setType("message/rfc822");
            final PackageManager pm = getActivity().getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
            ResolveInfo best = null;
            for (final ResolveInfo info : matches)
                if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                    best = info;
            if (best != null)
                emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
            getActivity().startActivity(Intent.createChooser(emailIntent, "Send an Email:"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Terms & Conditions Screen", TCFragment.class.getSimpleName());
    }


}
