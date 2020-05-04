package com.ns.activity;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.main.SuperApp;
import com.netoperation.config.download.IconDownload;
import com.netoperation.config.model.TabsBean;
import com.netoperation.config.download.FileUtils;
import com.netoperation.config.download.RxDownloader;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.default_db.TableWidget;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.ns.alerts.Alerts;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.view.CustomProgressBar;
import com.ns.view.LogoImgView;
import com.ns.view.text.ArticleTitleTextView;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseAcitivityTHP {

    private static String TAG = NetConstants.TAG_UNIQUE;
    private long startTime;
    private LogoImgView appIconImg;
    private ArticleTitleTextView loadingMsg;
    private CustomProgressBar progressBar;

    private final int WHAT_CONFIG = 1;
    private final int WHAT_DOWNLOADING = 2;
    private final int WHAT_MP = 3;
    private final int WHAT_SECTION = 4;
    private final int WHAT_HOME_CONTENT = 5;
    private final int WHAT_LAUNCH_TAB = 6;

    @Override
    public int layoutRes() {
        return R.layout.activity_splash;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appIconImg = findViewById(R.id.appIconImg);
        loadingMsg = findViewById(R.id.loadingMsg);
        progressBar = findViewById(R.id.progressBar);

        if(!DefaultPref.getInstance(this).isConfigurationOnceLoaded()) {
            sendHandlerMsg(WHAT_CONFIG);
            appConfigApi();
        }
        else if (DefaultPref.getInstance(this).getMPStartTimeInMillis() == 0 || DefaultPref.getInstance(this).isMPDurationExpired()) {
            sendHandlerMsg(WHAT_MP);
            //Metered Paywall Cycle API calls.
            mDisposable.add(DefaultTHApiManager.mpCycleDurationAPI(this, BuildConfig.MP_CYCLE_API_URL,
                    BuildConfig.MP_CYCLE_CONFIGURATION_API_URL, new RequestCallback() {
                        @Override
                        public void onNext(Object o) {
                            routeToAppropriateAction();
                        }

                        @Override
                        public void onError(Throwable t, String str) {
                            if(DefaultPref.getInstance(SplashActivity.this).isMpCycleOnceLoaded()) {
                                routeToAppropriateAction();
                            }
                            else {
                                if(!NetUtils.isConnected(SplashActivity.this)) {
                                    Alerts.noConnectionSnackBarInfinite(appIconImg, SplashActivity.this);
                                } else {
                                    Alerts.showSnackbar(SplashActivity.this, SplashActivity.this.getResources().getString(R.string.something_went_wrong));
                                }
                            }
                        }

                        @Override
                        public void onComplete(String str) {

                        }
                    }));
        }
        else {
            routeToAppropriateAction();

        }

        // Reduces Read article table
        DefaultTHApiManager.readArticleDelete(this);

        DefaultPref.getInstance(SuperApp.getAppContext()).setIsFullScreenAdLoaded(false);



    }

    private void sendHandlerMsg(int what) {
        mHandler.sendEmptyMessage(what);
    }

    private void showProgressBar(String msg) {
        progressBar.setVisibility(View.VISIBLE);
        loadingMsg.setVisibility(View.VISIBLE);
        loadingMsg.setText(msg);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_CONFIG:
                    showProgressBar("Initializing App Configuration.");
                    break;
                case WHAT_DOWNLOADING:
                    showProgressBar("Downloading App Configuration.");
                    break;
                case WHAT_MP:
                    showProgressBar("Configuring Articles.");
                    break;
                case WHAT_SECTION:
                    showProgressBar("Checking Topics.");
                    break;
                case WHAT_HOME_CONTENT:
                    showProgressBar("Updating News.");
                    break;
                case WHAT_LAUNCH_TAB:
                    launchTabScreen();
                    break;
            }
        }
    };

    private void launchTabScreen() {
        IntentUtil.openMainTabPage(SplashActivity.this);
    }



    private void routeToAppropriateAction() {
        boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(this).isHomeArticleOptionScreenShown();

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

        if(nc != null && isHomeArticleOptionScreenShown) {
            int downSpeed = nc.getLinkDownstreamBandwidthKbps();
            startTime = System.currentTimeMillis();
            Log.i("NSPEED", "downSpeed :: " + downSpeed);
            // On Good 4G or Good Wifi
            if(downSpeed > 102100) {
                getSectionDirectFromServer();
            }
            // For Moderate Net Speed
            else if(downSpeed > 5000 && downSpeed < 102100) {
                getHomeDataFromServer();
            }
            // For low net speed, it will launch directly
            else {
                directLaunch();
            }

        } else {
            Log.i("NSPEED", "downSpeed :: is NULL" );
            directLaunch();
        }
    }


    private void appConfigApi() {
        DefaultTHApiManager.appConfiguration(this, new RequestCallback<TableConfiguration>() {
            @Override
            public void onNext(TableConfiguration configuration) {

                if(configuration != null) {
                    // Saves, App configuration is loaded once.
                    DefaultPref.getInstance(SplashActivity.this).setConfigurationOnceLoaded(true);

                    IconDownload iconDownload = new IconDownload(configuration, SplashActivity.this);
                    iconDownload.startDownloading();

                }

            }

            @Override
            public void onError(Throwable t, String str) {

            }

            @Override
            public void onComplete(String str) {

            }
        });
    }


    private void directLaunch() {
        Log.i("NSPEED", "Direct Launch calling...." );
        startTime = System.currentTimeMillis();
        DefaultTHApiManager.getSectionsFromTempTable(this, System.currentTimeMillis(), new RequestCallback() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t, String str) {
                getSectionDirectFromServer();
            }

            @Override
            public void onComplete(String str) {
                boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(SplashActivity.this).isHomeArticleOptionScreenShown();
                // Opens Main Tab Screen
                if(isHomeArticleOptionScreenShown) {
                    sendHandlerMsg(WHAT_LAUNCH_TAB);
                    long totalExecutionTime = System.currentTimeMillis() - startTime;
                    Log.i("NSPEED", "Direct Launched Main Tab Page:: " + totalExecutionTime);
                }
                else {
                    IntentUtil.openHomeArticleOptionActivity(SplashActivity.this);
                }

                //Metered Paywall Configs API calls.
                mDisposable.add(DefaultTHApiManager.mpConfigurationAPI(SplashActivity.this, BuildConfig.MP_CYCLE_CONFIGURATION_API_URL));
            }
        });
    }


    /**
     * Gets Section and Home Data from server, It launches "MainTab" page from getHomeDataFromServer();
     */
    private void getSectionDirectFromServer() {
        Log.i("NSPEED", "Calling Section List API, Next Home Article APIs will be called " );
        DefaultTHApiManager.sectionDirectFromServer(this, new RequestCallback() {
            @Override
            public void onNext(Object o) {
                long totalExecutionTime = System.currentTimeMillis() - startTime;
                Log.i("NSPEED", "Loaded Section Page from Server :: "+totalExecutionTime);
                boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(SplashActivity.this).isHomeArticleOptionScreenShown();
                if(isHomeArticleOptionScreenShown) {
                    // Get Home Article from server
                    getHomeDataFromServer();
                }
                else {
                    IntentUtil.openHomeArticleOptionActivity(SplashActivity.this);
                }
            }

            @Override
            public void onError(Throwable t, String str) {
                Log.i("NSPEED", "ERROR1 :: "+t);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(SplashActivity.this).isHomeArticleOptionScreenShown();
                        if(isHomeArticleOptionScreenShown) {
                            sendHandlerMsg(WHAT_LAUNCH_TAB);
                        }
                        else {
                            if(!NetUtils.isConnected(SplashActivity.this)) {
                                Alerts.noConnectionSnackBarInfinite(appIconImg, SplashActivity.this);
                            } else {
                                Alerts.showSnackbar(SplashActivity.this, SplashActivity.this.getResources().getString(R.string.something_went_wrong));
                            }
                        }
                    }
                });

            }

            @Override
            public void onComplete(String str) {

            }

        }, System.currentTimeMillis());
    }

    /**
     * Gets Home Data from server, launches "MainTab" Page
     */
    private void getHomeDataFromServer() {
        Log.i("NSPEED", "Home Article APIs are called " );
        DefaultTHApiManager.homeArticles(SplashActivity.this, "SplashActivity", new RequestCallback() {
            @Override
            public void onNext(Object o) {
                // Opens Main Tab Screen
                sendHandlerMsg(WHAT_LAUNCH_TAB);
                long totalExecutionTime = System.currentTimeMillis() - startTime;
                Log.i("NSPEED", "Home Article Loaded from server, Launched Main Tab Page :: "+totalExecutionTime);
            }

            @Override
            public void onError(Throwable t, String str) {
                Log.i("NSPEED", "ERROR2");
                sendHandlerMsg(WHAT_LAUNCH_TAB);
            }

            @Override
            public void onComplete(String str) {
                final THPDB thpdb = THPDB.getInstance(SplashActivity.this);
                // Get Widget Ids from server
                DaoWidget daoWidget = thpdb.daoWidget();
                daoWidget.getWidgetsSingle()
                        .subscribeOn(Schedulers.io())
                        .map(widgets -> {
                            final Map<String, String> sections = new HashMap<>();
                            for (TableWidget widget : widgets) {
                                sections.put(widget.getSecId(), widget.getType());
                            }
                            // Get Widget Data From server
                            DefaultTHApiManager.widgetContent(SplashActivity.this, sections);
                            return "";
                        })
                        .subscribe(onSuccess -> {
                            Log.i(TAG, "SplashActivity :: Widget :: Sent Server Request to get latest data");

                            //Metered Paywall Configs API calls.
                            mDisposable.add(DefaultTHApiManager.mpConfigurationAPI(SplashActivity.this, BuildConfig.MP_CYCLE_CONFIGURATION_API_URL));
                        });

            }
        });
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
