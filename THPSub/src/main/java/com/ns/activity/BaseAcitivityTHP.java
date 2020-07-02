package com.ns.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.InAppNotificationButtonListener;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;
import com.main.DFPAds;
import com.main.SuperApp;
import com.netoperation.config.model.ColorOptionBean;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.model.AdData;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.PremiumPref;
import com.ns.callbacks.FragmentTools;
import com.ns.callbacks.OnDFPAdLoadListener;
import com.ns.callbacks.ToolbarClickListener;
import com.ns.clevertap.CleverTapUtil;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.R;
import com.ns.tts.TTSManager;
import com.ns.utils.CommonUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.Topbar;
import com.ns.view.text.ArticleTitleTextView;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseAcitivityTHP extends AppCompatActivity implements ToolbarClickListener, InAppNotificationButtonListener {

    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    public static boolean sIsDayTheme = true;
    public static boolean sIsOnline;

    public abstract int layoutRes();
    protected FragmentTools mFragmentTools;
    private Topbar mToolbar;


    public Topbar getDetailToolbar() {
        return  mToolbar;
    }
    private ArticleTitleTextView mNoConnectionTabText;

    private Disposable internetDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sIsDayTheme = DefaultPref.getInstance(this).isUserThemeDay();
        TableConfiguration tableConfiguration = getTableConfiguration();
        //Set Navigation Background color
        if(tableConfiguration != null && THPConstants.IS_USE_SEVER_THEME) {
            ColorOptionBean topSystemBarTheme = tableConfiguration.getAppTheme().getSystemTopBarBackground();
            ColorOptionBean bottomSystemBarTheme = tableConfiguration.getAppTheme().getSystemBottomBarBackground();
            if (sIsDayTheme) {
                getWindow().setNavigationBarColor(Color.parseColor(bottomSystemBarTheme.getLight()));
                getWindow().setStatusBarColor(Color.parseColor(topSystemBarTheme.getLight()));
            } else {
                getWindow().setNavigationBarColor(Color.parseColor(bottomSystemBarTheme.getDark()));
                getWindow().setStatusBarColor(Color.parseColor(topSystemBarTheme.getDark()));
            }
        } else {
            if(sIsDayTheme) {
                getWindow().setNavigationBarColor(ResUtil.getColor(getResources(), R.color.topbar_light));
                getWindow().setStatusBarColor(ResUtil.getColor(getResources(), R.color.topbar_light));
            } else {
                getWindow().setNavigationBarColor(ResUtil.getColor(getResources(), R.color.topbar_dark));
                getWindow().setStatusBarColor(ResUtil.getColor(getResources(), R.color.color_status_bar_dark));
            }
        }

        setContentView(layoutRes());

        mNoConnectionTabText = findViewById(R.id.noConnectionTabText);
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(" ");
            mToolbar.setTitle(null);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // Registering Back button click listener
            if(mToolbar instanceof Topbar) {
                Topbar topbar = (Topbar) mToolbar;
                topbar.setToolbarMenuListener(this);
            }

        }
// Initializing, CLeverTap In-App Notification Button Click Listener
        CleverTapAPI clevertap = CleverTapAPI.getDefaultInstance(this);
        clevertap.setInAppNotificationButtonListener(this);

        loadConfigurationInstance();
    }

    @Override
    public void onInAppButtonClick(HashMap<String, String> payload) {
        Log.i("", "");
        if (payload.containsKey("ns_type_PN")) {
            String type = payload.get("ns_type_PN");
            if (type != null) {
                if(type.equalsIgnoreCase(THPConstants.PLANS_PAGE)){
                    String planOffer = payload.get("ns_plan_offer");
                    if (planOffer != null && !TextUtils.isEmpty(planOffer)) {
                        IntentUtil.openSubscriptionPageOfferWise(this,
                                THPConstants.FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE, planOffer);
                    } else {
                        IntentUtil.openSubscriptionActivity(this,
                                THPConstants.FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE);
                    }
                } else if (type.equalsIgnoreCase(THPConstants.URL)) {
                    String url = payload.get("ns_url");
                    IntentUtil.openUrlInBrowser(this, url);
                }
            }
        }
    }

    private void internetAvailbilityCheck() {
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isConnected -> {
                            sIsOnline = isConnected;
                            if (sIsOnline && mNoConnectionTabText != null) {
                                mNoConnectionTabText.setVisibility(View.GONE);
                            } else if (mNoConnectionTabText != null) {
                                mNoConnectionTabText.setVisibility(View.VISIBLE);
                            }
                        }
                );
    }

    @Override
    protected void onResume() {
        super.onResume();
        internetAvailbilityCheck();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "BaseAcitivityTHP Screen", BaseAcitivityTHP.class.getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        safelyDispose(internetDisposable);
    }

    @Override
    protected void onStop() {
        TTSManager.getInstance().stopTTS();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        TTSManager.getInstance().release();
        mDisposable.clear();
        mDisposable.dispose();
        super.onDestroy();
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    public void setOnFragmentTools(FragmentTools fragmentTools) {
        mFragmentTools = fragmentTools;
    }

    @Override
    public void onBackClickListener() {
        FragmentManager fm = getSupportFragmentManager();
        int fragmentCount = fm.getBackStackEntryCount();
        if (fragmentCount > 0) {
            fm.popBackStack();
        } if (!THPConstants.sISMAIN_ACTIVITY_LAUNCHED) {
            IntentUtil.openMainTabPage(this);
        } else {
            finish();
//            IntentUtil.exitActivityAnim(this);
        }

        if(mToolbar!=null) {
            CommonUtil.hideKeyboard(mToolbar);
        }

    }


    @Override
    public void onSearchClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onSearchClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onShareClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onShareClickListener(toolbarCallModel);
            //share event
            CleverTapUtil.cleverTapEvent(this, THPConstants.CT_EVENT_SHARE, null);
        }
    }

    @Override
    public void onCreateBookmarkClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onCreateBookmarkClickListener(toolbarCallModel);
            //create bookmark
            CleverTapUtil.cleverTapEvent(this,THPConstants.CT_EVENT_BOOKMARK,null);
        }
    }

    @Override
    public void onFontSizeClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onFontSizeClickListener(toolbarCallModel);
            //font event
            CleverTapUtil.cleverTapEvent(this,THPConstants.CT_EVENT_FONT,null);
        }
    }

    @Override
    public void onCommentClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onCommentClickListener(toolbarCallModel);
            //comment event
            CleverTapUtil.cleverTapEvent(this,THPConstants.CT_EVENT_COMMENTS,null);
        }
    }

    @Override
    public void onTTSPlayClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onTTSPlayClickListener(toolbarCallModel);
            //readloud event
            CleverTapUtil.cleverTapEvent(this,THPConstants.CT_EVENT_READLOUD,null);
        }
    }

    @Override
    public void onTTSStopClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onTTSStopClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onRemoveBookmarkClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onRemoveBookmarkClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onFavClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onFavClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onLikeClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onLikeClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onOverflowClickListener(ToolbarCallModel toolbarCallModel) {
        //Alerts.showToast(this, "TO");
        if(mFragmentTools != null) {
            mFragmentTools.onOverflowClickListener(toolbarCallModel);
        }
    }

    public void noConnectionSnackBar(View view) {
        if(view == null) {
            return;
        }
        Snackbar mSnackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout mSnackbarView = (Snackbar.SnackbarLayout) mSnackbar.getView();
        mSnackbarView.setBackgroundColor(Color.BLACK);
        TextView textView = mSnackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        TextView textView1 = mSnackbarView.findViewById(com.google.android.material.R.id.snackbar_action);
        textView.setVisibility(View.INVISIBLE);
        textView1.setVisibility(View.INVISIBLE);
        View snackView = getLayoutInflater().inflate(R.layout.thp_noconnection_snackbar, null);
        snackView.findViewById(R.id.action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        mSnackbarView.addView(snackView);
        mSnackbar.show();
    }


    protected boolean isUserLoggedIn() {
        return PremiumPref.getInstance(this).isUserLoggedIn();
    }


    protected void bottomBannerAds() {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isHasSubscription()) {
            LinearLayout banner_Ad_layout = findViewById(R.id.banner_Ad_layout);
            if(banner_Ad_layout != null) {
                banner_Ad_layout.setVisibility(View.GONE);
            }
            return;
        }

        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            LinearLayout banner_Ad_layout = findViewById(R.id.banner_Ad_layout);
            if(banner_Ad_layout != null) {
                banner_Ad_layout.setVisibility(View.VISIBLE);
            }
            return;
        }


        TableConfiguration tableConfiguration = getTableConfiguration();
        if(tableConfiguration == null) return;

        DFPAds DFPAds = new DFPAds();
        DFPAds.createBannerAdRequest(false, tableConfiguration.getAds().getBottomAdHomeId(), tableConfiguration.getAds().getBottomAdOtherId());
        DFPAds.setOnDFPAdLoadListener(new OnDFPAdLoadListener() {
            @Override
            public void onDFPAdLoadSuccess(AdData adData) {
                LinearLayout banner_Ad_layout = findViewById(R.id.banner_Ad_layout);
                if(banner_Ad_layout != null) {
                    banner_Ad_layout.setVisibility(View.VISIBLE);
                    int childCount = banner_Ad_layout.getChildCount();
                    if(banner_Ad_layout.getChildCount() > 0) {
                        for(int i=0; i<childCount; i++) {
                            banner_Ad_layout.removeViewAt(i);
                        }
                    }
                    banner_Ad_layout.addView(adData.getAdView());
                }

            }

            @Override
            public void onDFPAdLoadFailure(AdData adData) {
                Log.i("", "");

            }

            @Override
            public void onAdClose() {

            }
        });
    }


    private static TableConfiguration sTableConfiguration;

    private static void loadConfigurationInstance() {
        if(sTableConfiguration == null) {
            refreshConfigurationInstance();
        }
    }

    public static void refreshConfigurationInstance() {
            DefaultTHApiManager.appConfiguration(SuperApp.getAppContext())
                    .subscribe(tableConfiguration -> {
                        sTableConfiguration = tableConfiguration;
                    }, throwable -> {

                    });

    }

    public static TableConfiguration getTableConfiguration() {
        if(sTableConfiguration == null) {
            loadConfigurationInstance();
        }
        return sTableConfiguration;
    }

    @Override
    public void onBackPressed() {
        if (!THPConstants.sISMAIN_ACTIVITY_LAUNCHED && DefaultPref.getInstance(this).isHomeArticleOptionScreenShown()) {
            IntentUtil.openMainTabPage(this);
        } else {
            super.onBackPressed();
        }
    }
}
