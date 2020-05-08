package com.ns.loginfragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.MailTo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.netoperation.net.ApiManager;
import com.netoperation.util.PremiumPref;
import com.ns.activity.AppTabActivity;
import com.ns.activity.THPUserProfileActivity;
import com.ns.alerts.Alerts;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.OnBackPressed;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RecoPlansWebViewFragment extends BaseFragmentTHP implements OnBackPressed {

    private WebView mWebView;
    private String JS_OBJECT_NAME = "Android";


    public interface SubsPlanSelectListener {
        void onSubsPlanSelected(String planId, String countryName,int packValue,String packValidity,String packName);
    }

    public static RecoPlansWebViewFragment getInstance(String from) {
        RecoPlansWebViewFragment fragment = new RecoPlansWebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_subs_webview_reco_plans;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if(getArguments() != null) {
            mFrom = getArguments().getString("from");
        }*/
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Shows plan expire dialog
        loadUserData();

        mWebView = view.findViewById(R.id.webViewRecoPlans);

        initiateWebView();
        loadSubsWebViewApi();
        // Back button click listener
        view.findViewById(R.id.backBtn).setOnClickListener(v -> {
            //getActivity().finish();
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                FragmentUtil.clearSingleBackStack((AppCompatActivity) getActivity());
            else {
                getActivity().onBackPressed();
            }
        });

        // mail id click listener
        /*view.findViewById(R.id.tv_mail).setOnClickListener(v->{
            sendEmail();
        });

        // frequently asked questions click listener
        view.findViewById(R.id.tv_frequent_questions).setOnClickListener(v->{
            TCFragment tcFragment = TCFragment.getInstance(THPConstants.FAQ_URL, "arrowBackImg");
            FragmentUtil.replaceFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    tcFragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
        });

        // terms and conditions click listener
        view.findViewById(R.id.tv_terms).setOnClickListener(v->{
            TCFragment tcFragment = TCFragment.getInstance(THPConstants.TnC_URL, "arrowBackImg");
            FragmentUtil.replaceFragmentAnim((AppCompatActivity) getActivity(), R.id.parentLayout,
                    tcFragment, FragmentUtil.FRAGMENT_NO_ANIMATION, false);
        });*/
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initiateWebView() {
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideProgressDialog();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if(getContext()!=null) {
                    if (!NetUtils.isConnected(getContext())) {
                        Alerts.showAlertNoInternetRetry(getContext(), "No internet connection!",
                                getString(R.string.please_check_ur_connectivity), (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    mWebView.reload();
                                });
                    } else {
                        Alerts.showAlertNoInternetRetry(getContext(), "Error loading!",
                                getString(R.string.please_check_ur_connectivity), (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                    mWebView.reload();
                                });
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().startsWith("mailto:")) {
                        MailTo mt = MailTo.parse(request.getUrl().toString());
                        sendEmail(mt.getTo());
                        //mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc()
                        return true;
                    } else {
                        if (!NetUtils.isConnected(getContext())) {
                            Alerts.noConnectionSnackBar(mWebView, (AppCompatActivity) getActivity());
                            return true;
                        }
                        return super.shouldOverrideUrlLoading(view, request);
                    }
                } else {
                    if (!NetUtils.isConnected(getContext())) {
                        Alerts.noConnectionSnackBar(mWebView, (AppCompatActivity) getActivity());
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, request);
                }
            }
        });
        mWebView.addJavascriptInterface(new Object() {
           /* @JavascriptInterface
            public void subsPurchase(String planId, String countryName) {
                //Pass the information to Activity's interface
                if (getView() == null || getActivity() == null) {
                    return;
                }
                if (!NetUtils.isConnected(getContext())) {
                    Alerts.noConnectionSnackBar(mWebView, (AppCompatActivity) getActivity());
                    return;
                }
                getActivity().runOnUiThread(() -> {
                    THPUserProfileActivity thpUserProfileActivity = (THPUserProfileActivity) getActivity();
                    thpUserProfileActivity.onSubsPlanSelected(planId, countryName);

                });
            }*/

            @JavascriptInterface
            public void purchase(String planId, String countryName) {
                /*Do not Delete this method)*/
                //Use purchaseNew() method for further steps.
            }

            /*Use this method instead of purchase()*/
            @JavascriptInterface
            public void purchaseNew(String planId, String countryName,String planAmount,String planName,String planValidity) {
                //Pass the information to Activity's interface
                if (getView() == null || getActivity() == null) {
                    return;
                }
                if (!NetUtils.isConnected(getContext())) {
                    Alerts.noConnectionSnackBar(mWebView, (AppCompatActivity) getActivity());
                    return;
                }
                getActivity().runOnUiThread(() -> {
                    if(getActivity() instanceof THPUserProfileActivity) {
                        int packValue;
                        if (countryName.equalsIgnoreCase("india") || countryName.equalsIgnoreCase("IN")) {
                            packValue = (int)Double.parseDouble(planAmount);
                        } else {
                            packValue = (int)Double.parseDouble(planAmount)*70;
                        }
                        THPUserProfileActivity thpUserProfileActivity = (THPUserProfileActivity) getActivity();
                        thpUserProfileActivity.onSubsPlanSelected(planId, countryName,packValue,planValidity,planName);
                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getContext(), "Action", "Subscribe Button clicked : " + planId, RecoPlansWebViewFragment.class.getSimpleName());

                        //CleverTap Pay Now event
                        CleverTapUtil.cleverTapEventPayNow(getActivity(), packValue,planValidity,planName);
                        //Firebase Pay Now event
                        THPFirebaseAnalytics.cleverTapEventPayNowFirebase(getActivity(), packValue,planValidity,planName);


                    } else if(getActivity() instanceof AppTabActivity) {
                        /*AppTabActivity thpUserProfileActivity = (AppTabActivity) getActivity();
                        thpUserProfileActivity.onSubsPlanSelected(planId, countryName);*/
                    }
                });
            }

            @JavascriptInterface
            public void ProductViewed(String planDetails){
                //Pass the information to Activity's interface
                if (getView() == null || getActivity() == null) {
                    return;
                }
                if (!NetUtils.isConnected(getContext())) {
                    return;
                }
                try {
                    String s = URLDecoder.decode(planDetails,"UTF-8");

                    List<String> planName = new ArrayList<>();
                    List<String> amount = new ArrayList<>();
                    List<String> validity = new ArrayList<>();

                    JSONArray jsonArray = new JSONArray(s);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        planName.add(jsonObject.getString("planName"));
                        amount.add(jsonObject.getString("Amount"));
                        validity.add(jsonObject.getString("validity"));
                    }

                    //CleverTap Product Viewed event
                    CleverTapUtil.cleverTapEventProductViewed(getActivity(),
                            amount.toString().replaceAll("[\\[\\]]", ""),
                            validity.toString().replaceAll("[\\[\\]]", ""),
                            planName.toString().replaceAll("[\\[\\]]", ""));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @SuppressLint("CheckResult")
            @JavascriptInterface
            public void startThirtyDayTrial() {
                //Pass the information to Activity's interface
                if (getView() == null || getActivity() == null) {
                    return;
                }
                if (!NetUtils.isConnected(getContext())) {
                    Alerts.noConnectionSnackBar(mWebView, (AppCompatActivity) getActivity());
                    return;
                }
                getActivity().runOnUiThread(() -> {
                    ApiManager.getUserProfile(getActivity())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(userProfile -> {
                                if (!PremiumPref.getInstance(getContext()).isUserLoggedIn() || userProfile.getUserId() == null ) {
                                    IntentUtil.openSignInOrUpActivity(getContext(), THPConstants.FROM_START_30_DAYS_TRAIL);
                                } else if (userProfile.isHasFreePlan()) {
                                    Alerts.showAlertDialogOKBtn(getContext(), "Already Signed up", "You have already signed up and availing 30 days free trial.");
                                } else {
                                    Alerts.showAlertDialogOKBtn(getContext(), "Already Signed up", "You have already signed up and consumed 30 days free trial.");
                                }
                            });
                });

            }
        }, JS_OBJECT_NAME);
    }

    private void loadSubsWebViewApi() {
        showProgressDialog("Fetching recommended plans");
        String SUBSCRIPTION_WEB_URL = "";
        if(BuildConfig.IS_PRODUCTION) {
            SUBSCRIPTION_WEB_URL = BuildConfig.PRODUCTION_SUBSCRIPTION_WEB_URL;
        } else {
            SUBSCRIPTION_WEB_URL = BuildConfig.STATGGING_SUBSCRIPTION_WEB_URL;
        }
        if (!sIsDayTheme) {
            SUBSCRIPTION_WEB_URL += "true";
        }

        ApiManager.loadSubsWebApi(SUBSCRIPTION_WEB_URL)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(urlForSubsPage -> {
                    if (urlForSubsPage != null) {
                        mWebView.loadUrl(urlForSubsPage);
                    } else {
                        Alerts.showToast(getContext(), "Failed to load recommended plans.");
                    }
                }, throwable -> {
                    hideProgressDialog();
                    //Handle Error and network interruption
                    Log.i("THPUserProfileActivity", throwable.getMessage());
                    if (!NetUtils.isConnected(getContext())) {
                        Alerts.noConnectionSnackBar(mWebView, (AppCompatActivity) getActivity());
                    } else {
                        Alerts.showToast(getContext(), "Failed to load recommended plans.");
                    }
                    getActivity().finish();
                });
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
    public boolean onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return false;
        } else
            return true;
    }


    private void loadUserData() {
        ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    mUserId = userProfile.getUserId();
                    boolean mHasFreePlan = userProfile.isHasFreePlan();
                    boolean mHasSubscriptionPlan = userProfile.isHasSubscribedPlan();
                    if (getActivity() != null) {
                        boolean isUserLoggedIn = PremiumPref.getInstance(getActivity()).isUserLoggedIn();
                        if (isUserLoggedIn && !mHasFreePlan && !mHasSubscriptionPlan) {
                            Alerts.showAlertDialogOKBtn(getActivity(), "Kindly Subscribe", "Your active plans has expired. Please subscribe.");
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Subscription Plans Screen", RecoPlansWebViewFragment.class.getSimpleName());
    }

    @Override
    public void onDestroy() {
        mWebView.removeJavascriptInterface(JS_OBJECT_NAME);
        super.onDestroy();
    }
}
