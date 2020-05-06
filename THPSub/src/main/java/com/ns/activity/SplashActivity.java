package com.ns.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.main.SuperApp;
import com.netoperation.config.download.Download;
import com.netoperation.config.download.IconDownloadService;
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

    private final int WHAT_CONFIG_UPDATE_CHECK = 1;
    private final int WHAT_CONFIG_FETCH_SERVER = 2;
    private final int WHAT_DOWNLOADING_CHECK = 3;
    private final int WHAT_MP = 4;
    private final int WHAT_SECTION = 5;
    private final int WHAT_SERVER_HOME_CONTENT = 6;
    private final int WHAT_ACTIIVTY_TAB = 7;
    private final int WHAT_ERROR = 8;
    private final int WHAT_TEMP_SECTION = 9;
    private final int WHAT_Server_Section = 10;

    private boolean isErrorOccured = false;
    private int totalSentRequestIcons;
    private int totalReceivedRequestIcons;


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

        registerReceiver();

        sendHandlerMsg(WHAT_CONFIG_UPDATE_CHECK);

        // Reduces Read article table
        DefaultTHApiManager.readArticleDelete(this);
        DefaultPref.getInstance(SuperApp.getAppContext()).setIsFullScreenAdLoaded(false);


    }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_CONFIG_UPDATE_CHECK:
                    showProgressBar("Checking App Configuration.");
                    isConfigurationUpdateAvailable();
                    break;
                case WHAT_CONFIG_FETCH_SERVER:
                    showProgressBar("Initializing App Configuration.");
                    callAppConfigApi();
                    break;
                case WHAT_DOWNLOADING_CHECK:
                    showProgressBar("Downloading App Configurations.");
                    Intent downloadIntent = new Intent(SplashActivity.this, IconDownloadService.class);
                    downloadIntent.putExtra(IconDownloadService.STATE, IconDownloadService.STATE_CHECK);
                    startService(downloadIntent);
                    break;
                case WHAT_MP:
                    showProgressBar("Configuring Articles.");
                    if (DefaultPref.getInstance(SplashActivity.this).getMPStartTimeInMillis() == 0 || DefaultPref.getInstance(SplashActivity.this).isMPDurationExpired()) {
                        callMpApi();
                    }
                    else {
                        routeToAppropriateAction();
                    }
                    break;
                case WHAT_SECTION:
                    showProgressBar("Checking Topics.");
                    break;
                case WHAT_SERVER_HOME_CONTENT:
                    getHomeDataFromServer();
                    showProgressBar("Fetching News.");
                    break;
                case WHAT_ACTIIVTY_TAB:
                    launchTabScreen();
                    break;
                case WHAT_TEMP_SECTION:
                    tempSection();
                    break;
                case WHAT_Server_Section:
                    getSectionDirectFromServer();
                    break;
                case WHAT_ERROR:
                    if(!NetUtils.isConnected(SplashActivity.this)) {
                        Alerts.noConnectionSnackBarInfinite(appIconImg, SplashActivity.this);
                    }
                    else {
                        showProgressBar(getResources().getString(R.string.something_went_wrong));
                        Alerts.showSnackbar(SplashActivity.this, getResources().getString(R.string.something_went_wrong));
                    }
                    isErrorOccured = true;
                    break;
            }
        }
    };

    private void sendHandlerMsg(int what) {
        mHandler.sendEmptyMessage(what);
    }

    private void showProgressBar(String msg) {
        progressBar.setVisibility(View.VISIBLE);
        loadingMsg.setVisibility(View.VISIBLE);
        loadingMsg.setText(msg);
    }

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
                sendHandlerMsg(WHAT_Server_Section);
            }
            // For Moderate Net Speed
            else if(downSpeed > 5000 && downSpeed < 102100) {
                sendHandlerMsg(WHAT_SERVER_HOME_CONTENT);
            }
            // For low net speed, it will launch directly
            else {
                sendHandlerMsg(WHAT_TEMP_SECTION);
            }

        } else {
            Log.i("NSPEED", "downSpeed :: is NULL" );
            sendHandlerMsg(WHAT_TEMP_SECTION);
        }
    }

    private void callMpApi() {
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
                            sendHandlerMsg(WHAT_ERROR);
                        }
                    }

                    @Override
                    public void onComplete(String str) {

                    }
                }));
    }

    private void isConfigurationUpdateAvailable() {
        mDisposable.add(DefaultTHApiManager.isConfigurationUpdateAvailable(this)
                .subscribe(isAvailable->{
                    if(isAvailable) {
                        sendHandlerMsg(WHAT_CONFIG_FETCH_SERVER);
                    } else {
                        sendHandlerMsg(WHAT_MP);
                    }
                }, throwable -> {
                    if(DefaultPref.getInstance(SplashActivity.this).isConfigurationOnceLoaded()) {
                        sendHandlerMsg(WHAT_MP);
                    }
                    else {
                        sendHandlerMsg(WHAT_ERROR);
                    }
                }));
    }



    private void callAppConfigApi() {
        DefaultTHApiManager.appConfigurationFromServer(this, new RequestCallback<TableConfiguration>() {
            @Override
            public void onNext(TableConfiguration configuration) {
                if(configuration != null) {
                    DefaultPref.getInstance(SplashActivity.this).setConfigurationOnceLoaded(true);
                }
            }

            @Override
            public void onError(Throwable t, String str) {
                sendHandlerMsg(WHAT_ERROR);

            }

            @Override
            public void onComplete(String str) {
                sendHandlerMsg(WHAT_DOWNLOADING_CHECK);
            }
        });
    }


    private void tempSection() {
        Log.i("NSPEED", "Direct Launch calling...." );
        startTime = System.currentTimeMillis();
        DefaultTHApiManager.getSectionsFromTempTable(this, System.currentTimeMillis(), new RequestCallback() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t, String str) {
                sendHandlerMsg(WHAT_Server_Section);
            }

            @Override
            public void onComplete(String str) {
                boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(SplashActivity.this).isHomeArticleOptionScreenShown();
                // Opens Main Tab Screen
                if(isHomeArticleOptionScreenShown) {
                    sendHandlerMsg(WHAT_ACTIIVTY_TAB);
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
                    //getHomeDataFromServer();
                    sendHandlerMsg(WHAT_SERVER_HOME_CONTENT);
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
                            sendHandlerMsg(WHAT_ACTIIVTY_TAB);
                        }
                        else {
                            sendHandlerMsg(WHAT_ERROR);
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
                sendHandlerMsg(WHAT_ACTIIVTY_TAB);
                long totalExecutionTime = System.currentTimeMillis() - startTime;
                Log.i("NSPEED", "Home Article Loaded from server, Launched Main Tab Page :: "+totalExecutionTime);
            }

            @Override
            public void onError(Throwable t, String str) {
                Log.i("NSPEED", "ERROR2");
                sendHandlerMsg(WHAT_ACTIIVTY_TAB);
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



    private boolean isBroadCastRegistered = false;


    private void registerReceiver() {
        if(!isBroadCastRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(IconDownloadService.MESSAGE_STATUS);
            intentFilter.addAction(IconDownloadService.MESSAGE_PROGRESS);
            intentFilter.addAction(IconDownloadService.MESSAGE_FAILED);
            intentFilter.addAction(IconDownloadService.MESSAGE_SUCCESS);
            intentFilter.addAction(IconDownloadService.MESSAGE_REQUEST_SENT);
            isBroadCastRegistered = true;
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
        }

    }

    @Override
    public void onBackPressed() {
        if(isErrorOccured) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if(isBroadCastRegistered) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
            isBroadCastRegistered = false;
        }
        super.onDestroy();
    }

    private boolean isMpRequestSentFromBroadcastReceiver;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Download download = intent.getParcelableExtra("download");

            if(intent.getAction().equals(IconDownloadService.MESSAGE_STATUS)) {
                if(download.getStatus() == IconDownloadService.STATUS_NOT_RUNNING) {
                    Intent downloadIntent = new Intent(SplashActivity.this, IconDownloadService.class);
                    downloadIntent.putExtra(IconDownloadService.STATE, IconDownloadService.STATE_START);
                    startService(downloadIntent);
                }
            }
            else if(intent.getAction().equals(IconDownloadService.MESSAGE_PROGRESS)) {

            }
            else if(intent.getAction().equals(IconDownloadService.MESSAGE_REQUEST_SENT)) {
                totalSentRequestIcons = download.getRequestNumber();
            }
            else if(intent.getAction().equals(IconDownloadService.MESSAGE_FAILED)) {
                totalReceivedRequestIcons++;
                if(totalReceivedRequestIcons >= totalSentRequestIcons) {
                    if(!isMpRequestSentFromBroadcastReceiver) {
                        isMpRequestSentFromBroadcastReceiver = true;
                        sendHandlerMsg(WHAT_MP);
                    }
                }

                Log.i("Downloading", "Fail :: "+ totalReceivedRequestIcons);

            }
            else if(intent.getAction().equals(IconDownloadService.MESSAGE_SUCCESS)){
                totalReceivedRequestIcons++;
                if(totalReceivedRequestIcons >= totalSentRequestIcons) {
                    if(!isMpRequestSentFromBroadcastReceiver) {
                        isMpRequestSentFromBroadcastReceiver = true;
                        sendHandlerMsg(WHAT_MP);
                    }
                }
                Log.i("Downloading", "Success :: "+ totalReceivedRequestIcons);
            }
        }
    };
}
