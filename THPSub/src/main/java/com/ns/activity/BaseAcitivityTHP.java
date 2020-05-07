package com.ns.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;
import com.google.android.material.snackbar.Snackbar;
import com.main.AppAds;
import com.netoperation.model.AdData;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.PremiumPref;
import com.netoperation.util.DefaultPref;
import com.ns.alerts.Alerts;
import com.ns.callbacks.FragmentTools;
import com.ns.callbacks.ToolbarClickListener;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.tts.TTSManager;
import com.ns.utils.CommonUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.DetailToolbar;
import com.ns.view.text.ArticleTitleTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseAcitivityTHP extends AppCompatActivity implements ToolbarClickListener {

    protected final CompositeDisposable mDisposable = new CompositeDisposable();

    public abstract int layoutRes();

    protected FragmentTools mFragmentTools;
    private DetailToolbar mToolbar;

    public DetailToolbar getDetailToolbar() {
        return  mToolbar;
    }


    protected boolean mIsDayTheme = true;
    protected boolean hasSubscriptionPlan;
    private ArticleTitleTextView mNoConnectionTabText;

    public static boolean sIsOnline;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsDayTheme = DefaultPref.getInstance(this).isUserThemeDay();

        // Dialog Theme change
        if(mIsDayTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //For day mode theme for dialog
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); //For night mode theme for dialog
        }
        //Assigning base url
        if(BuildConfig.IS_PRODUCTION) {
            ServiceFactory.BASE_URL = BuildConfig.PRODUCTION_BASE_URL;
        } else {
            ServiceFactory.BASE_URL = BuildConfig.STATGGING_BASE_URL;
        }

        setContentView(layoutRes());

        mNoConnectionTabText = findViewById(R.id.noConnectionTabText);
        internetAvailibilityCheck();
        mToolbar = findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(" ");
            mToolbar.setTitle(null);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // Registering Back button click listener
            if(mToolbar instanceof DetailToolbar) {
                DetailToolbar detailToolbar = (DetailToolbar) mToolbar;
                detailToolbar.setToolbarMenuListener(this);
            }

        }
    }



    private void internetAvailibilityCheck() {
        ReactiveNetwork
                .observeNetworkConnectivity(this)
                .subscribeOn(Schedulers.io())
                .map(connectivity->{
                    if(connectivity.state() == NetworkInfo.State.CONNECTED) {
                        sIsOnline = true;
                    }
                    else {
                        sIsOnline = false;
                    }
                    return sIsOnline;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sIsOnline -> {
                    if(sIsOnline && mNoConnectionTabText != null) {
                        mNoConnectionTabText.setVisibility(View.GONE);
                    }
                    else if( mNoConnectionTabText != null) {
                        mNoConnectionTabText.setVisibility(View.VISIBLE);
                    }

                    Log.i("", "");
                });
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


    public void setOnFragmentTools(FragmentTools fragmentTools) {
        mFragmentTools = fragmentTools;
    }

    @Override
    public void onBackClickListener() {
        FragmentManager fm = getSupportFragmentManager();
        int fragmentCount = fm.getBackStackEntryCount();
        if (fragmentCount > 0) {
            fm.popBackStack();
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
        }
    }

    @Override
    public void onCreateBookmarkClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onCreateBookmarkClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onFontSizeClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onFontSizeClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onCommentClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onCommentClickListener(toolbarCallModel);
        }
    }

    @Override
    public void onTTSPlayClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFragmentTools != null) {
            mFragmentTools.onTTSPlayClickListener(toolbarCallModel);
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
        Alerts.showToast(this, "TO");
        if(mFragmentTools != null) {
            mFragmentTools.onLikeClickListener(toolbarCallModel);
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


    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "BaseAcitivityTHP Screen", BaseAcitivityTHP.class.getSimpleName());
    }

    protected boolean isUserLoggedIn() {
        return PremiumPref.getInstance(this).isUserLoggedIn();
    }


    protected void createAndShowBannerAds() {
        AppAds appAds = new AppAds();
        appAds.createBannerAdRequest(false);
        appAds.setOnAppAdLoadListener(new AppAds.OnAppAdLoadListener() {
            @Override
            public void onAppAdLoadSuccess(AdData adData) {
                LinearLayout banner_Ad_layout = findViewById(R.id.banner_Ad_layout);
                if(banner_Ad_layout != null) {
                    banner_Ad_layout.setVisibility(View.VISIBLE);
                    banner_Ad_layout.addView(adData.getAdView());
                }
            }

            @Override
            public void onAppAdLoadFailure(AdData adData) {
                Log.i("", "");

            }
        });
    }

    /*
     * Check shall MP should show
     * */
    public boolean shouldShowMeteredPaywall() {
        if (DefaultPref.getInstance(this).isMeteredPaywallEnabled()) {
            if (isUserLoggedIn() && !PremiumPref.getInstance(this).isUserAdsFree())
                return true;
            else if (isUserLoggedIn() && PremiumPref.getInstance(this).isUserAdsFree())
                return false;
            else if (!isUserLoggedIn())
                return true;
            else
                return true;
        } else {
            return false;
        }
    }
}
