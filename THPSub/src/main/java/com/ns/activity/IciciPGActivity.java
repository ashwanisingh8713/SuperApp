package com.ns.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;

import java.net.URLEncoder;

public class IciciPGActivity extends AppCompatActivity {

    private ProgressDialog progress;
    private WebView webView;
    private long mStartTime = 0L;
    private String JS_OBJECT_NAME = "Android";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void paymentSuccess() {
               //Do Nothing
            }

            @JavascriptInterface
            public void  paymentSuccessNew(String planAmount,String planName,String PlanValidity){
                runOnUiThread(() -> {
                    webView.stopLoading();
                    hideProgressDialog();
                    setResult(RESULT_OK);
                    long mEndTime = System.currentTimeMillis();
                    double d = Double.parseDouble(planAmount)*70;
                    CleverTapUtil.cleverTapEventPaymentStatus(IciciPGActivity.this, "success",(int)d,PlanValidity,planName,mStartTime,mEndTime);
                    THPFirebaseAnalytics.setFirbasePaymentSuccessFailedEvent(IciciPGActivity.this,"Action","success",(int)d,PlanValidity,planName,mStartTime,mEndTime);
                    finish();

                });
            }

            @JavascriptInterface
            public void  paymentFailedNew(String message, String planAmount,String planName,String PlanValidity){

                runOnUiThread(() -> {
                    webView.stopLoading();
                    hideProgressDialog();
                    Intent intent = new Intent();
                    intent.putExtra("message", message == null ? "You have cancelled the transaction." : message);
                    intent.putExtra("status", "failed");
                    setResult(RESULT_CANCELED, intent);
                    long mEndTime = System.currentTimeMillis();

                    double d = Double.parseDouble(planAmount)*70;
                    CleverTapUtil.cleverTapEventPaymentStatus(IciciPGActivity.this, "failed",(int)d,PlanValidity,planName,mStartTime,mEndTime);
                    THPFirebaseAnalytics.setFirbasePaymentSuccessFailedEvent(IciciPGActivity.this,"Action","failed",(int)d,PlanValidity,planName,mStartTime,mEndTime);
                    finish();
                });
            }


            @JavascriptInterface
            public void paymentFailed(String message) {
                //Do Nothing
            }

            @JavascriptInterface
            public void paymentPending(String message) {
                //Toast.makeText(IciciPGActivity.this, "Transaction Status Pending", Toast.LENGTH_LONG).show();
                runOnUiThread(() -> {
                    webView.stopLoading();
                    hideProgressDialog();
                    Intent intent = new Intent();
                    intent.putExtra("message", message == null ? "Your transaction status is pending" : message);
                    intent.putExtra("status", "pending");
                    setResult(RESULT_CANCELED, intent);
                    finish();
                });
            }
        }, JS_OBJECT_NAME);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgressDialog();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                showProgressDialog("Loading page");
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
        String userId = getIntent().getStringExtra("userId");
        String contact = getIntent().getStringExtra("contact");
        String planId = getIntent().getStringExtra("planId");
        String country = getIntent().getStringExtra("country");
        mStartTime = getIntent().getLongExtra("startTime", 0L);
        String postData = "";
        try {
            postData = "userid=" + URLEncoder.encode(userId, "UTF-8") +
                    "&planid=" + URLEncoder.encode(planId, "UTF-8") +
                    "&contact=" + URLEncoder.encode(contact, "UTF-8") +
                    "&country=" + URLEncoder.encode(country, "UTF-8") +
                    "&channel=" + URLEncoder.encode("android", "UTF-8");
            //postData = "CUST_ID=183&Plan_id=8&contact=kalaivanan.r%40thehindu.co.in&country=united%20states&channel=android";
        } catch (Exception e) {
            e.printStackTrace();
        }

        String PAYMENT_REDIRECT = "";
        if(BuildConfig.IS_PRODUCTION) {
            PAYMENT_REDIRECT = BuildConfig.PRODUCTION_PAYMENT_REDIRECT;
        } else {
            PAYMENT_REDIRECT = BuildConfig.STATGGING_PAYMENT_REDIRECT;
        }

        webView.postUrl(PAYMENT_REDIRECT, postData.getBytes());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Transaction Cancellation");
            builder.setMessage("Are you sure to cancel this transaction?");
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                setResult(RESULT_CANCELED);
                finish();
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.create().show();
        }
    }

    public void showProgressDialog(String message) {
        if (progress == null) {
            progress = new ProgressDialog(this);
        }
        progress.setTitle(getString(R.string.please_wait));
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }

    public void hideProgressDialog() {
        if (progress != null) {
            if (progress.isShowing()) {
                progress.hide();
            }
        }
    }

    @Override
    protected void onDestroy() {
        webView.removeJavascriptInterface(JS_OBJECT_NAME);
        super.onDestroy();
        hideProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "IciciPGActivity Screen", IciciPGActivity.class.getSimpleName());
    }
}
