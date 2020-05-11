package com.ns.activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
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
    private final int WHAT_DOWNLOADING_ICONS = 3;
    private final int WHAT_MP = 4;
    private final int WHAT_SECTION = 5;
    private final int WHAT_SERVER_HOME_CONTENT = 6;
    private final int WHAT_ACTIIVTY_TAB = 7;
    private final int WHAT_ERROR = 8;
    private final int WHAT_TEMP_SECTION = 9;
    private final int WHAT_Server_Section = 10;
    private final int WHAT_ROUTE_FOR_SCREEN = 11;
    private final int WHAT_FORCE_UPDATE = 12;

    private boolean isErrorOccured = false;
    private int totalSentRequestIcons;
    private int totalReceivedFailRequestIcons;
    private int totalReceivedSuccessRequestIcons;


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
        sendHandlerMsg(WHAT_FORCE_UPDATE, "Force Update request is sent to server");

        // Reduces Read article table
        DefaultTHApiManager.readArticleDelete(this);
        DefaultPref.getInstance(SuperApp.getAppContext()).setIsFullScreenAdLoaded(false);


    }



    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String from = bundle.getString("from");
            Log.i("SplashPage", from);

            switch (msg.what) {
                case WHAT_FORCE_UPDATE:
                    forceUpdate();
                    break;
                case WHAT_CONFIG_UPDATE_CHECK:
                    showProgressBar("Checking App Configuration.");
                    isConfigurationUpdateAvailable();
                    break;
                case WHAT_CONFIG_FETCH_SERVER:
                    showProgressBar("Initializing App Configuration.");
                    callAppConfigApi();
                    break;
                case WHAT_DOWNLOADING_ICONS:
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
                        sendHandlerMsg(WHAT_ROUTE_FOR_SCREEN, "Metered Paywall is not expired");
                    }
                    break;
                case WHAT_ROUTE_FOR_SCREEN:
                    routeToAppropriateAction();
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

    private void sendHandlerMsg(int what, String from) {
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        message.setData(bundle);
        mHandler.sendMessage(message);
        //mHandler.sendEmptyMessage(what);
    }

    private void showProgressBar(String msg) {
        progressBar.setVisibility(View.VISIBLE);
        loadingMsg.setVisibility(View.VISIBLE);
        loadingMsg.setText(msg);
    }

    private void forceUpdate() {
        mDisposable.add(DefaultTHApiManager.forceUpdate()
                .subscribe(updateModel -> {

                    String severVersionCode = updateModel.getVersion_code();
                    int serverVersionNumber = 0;
                    try {
                        serverVersionNumber = Integer.parseInt(severVersionCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int appVersionCode;

                    appVersionCode = BuildConfig.VERSION_CODE;
                    if ((appVersionCode < serverVersionNumber)) {
                        final boolean isForceUpdate = updateModel.getForce_upgrade();
                        showUpdateDialog(updateModel.getApp_store_url(), updateModel.getMessage(), isForceUpdate, updateModel.getRemind_me());
                    }
                    else {
                        sendHandlerMsg(WHAT_CONFIG_UPDATE_CHECK, "App configuration update check request is sent to server");
                    }

                }, throwable -> {
                        sendHandlerMsg(WHAT_CONFIG_UPDATE_CHECK, "App configuration update check request is sent to server");
                }, ()->{

                }));
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
            // On Good 4G or Good Wifi or Good Network
            if(downSpeed > 102100) {
                sendHandlerMsg(WHAT_Server_Section, "Good Network, going to fetch Section data from server");
            }
            // For Moderate Network Speed
            else if(downSpeed > 5000 && downSpeed < 102100) {
                sendHandlerMsg(WHAT_SERVER_HOME_CONTENT, "Moderate Network, going to fetch Home Page data from server");
            }
            // For Low network speed, it will launch directly
            else {
                sendHandlerMsg(WHAT_TEMP_SECTION, "Low network, going to fetch Temprory Section");
            }

        } else {
            sendHandlerMsg(WHAT_TEMP_SECTION, "NO Network, going to fetch Temprory Section");
        }
    }

    private void callMpApi() {
        mDisposable.add(DefaultTHApiManager.mpCycleDurationAPI(this, BuildConfig.MP_CYCLE_API_URL,
                BuildConfig.MP_CYCLE_CONFIGURATION_API_URL, new RequestCallback() {
                    @Override
                    public void onNext(Object o) {
                        sendHandlerMsg(WHAT_ROUTE_FOR_SCREEN, "Metered Paywall is updated from server");
                    }

                    @Override
                    public void onError(Throwable t, String str) {
                        if(DefaultPref.getInstance(SplashActivity.this).isMpCycleOnceLoaded()) {
                            sendHandlerMsg(WHAT_ROUTE_FOR_SCREEN, "Metered Paywall was expired and trying to update but got ERROR");
                        }
                        else {
                            sendHandlerMsg(WHAT_ERROR, "Metered Paywall is not not loaded yet and trying to fetch but got ERROR");
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
                        sendHandlerMsg(WHAT_CONFIG_FETCH_SERVER, "App Configuration update is available");
                    } else {
                        sendHandlerMsg(WHAT_MP, "NO, App Configuration update is Not available ");
                    }
                }, throwable -> {
                    if(DefaultPref.getInstance(SplashActivity.this).isConfigurationOnceLoaded()) {
                        sendHandlerMsg(WHAT_MP, "App Configuration update check failed but already once loaded");
                    }
                    else {
                        sendHandlerMsg(WHAT_ERROR, "App Configuration update is not loaded yet");
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
                sendHandlerMsg(WHAT_ERROR, "App Configuration data fetch is failed from server");

            }

            @Override
            public void onComplete(String str) {
                sendHandlerMsg(WHAT_DOWNLOADING_ICONS, "App Configuration data is received from server");
            }
        });
    }


    private void tempSection() {
        startTime = System.currentTimeMillis();
        DefaultTHApiManager.getSectionsFromTempTable(this, System.currentTimeMillis(), new RequestCallback() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t, String str) {
                sendHandlerMsg(WHAT_Server_Section, "Temperory Section has thrown ERROR so fetching section data from server");
            }

            @Override
            public void onComplete(String str) {
                boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(SplashActivity.this).isHomeArticleOptionScreenShown();
                // Opens Main Tab Screen
                if(isHomeArticleOptionScreenShown) {
                    sendHandlerMsg(WHAT_ACTIIVTY_TAB, "Temperory Section is launching AppTabActivity");
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
        DefaultTHApiManager.sectionDirectFromServer(this, new RequestCallback() {
            @Override
            public void onNext(Object o) {
                long totalExecutionTime = System.currentTimeMillis() - startTime;
                boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(SplashActivity.this).isHomeArticleOptionScreenShown();
                if(isHomeArticleOptionScreenShown) {
                    // Get Home Article from server
                    sendHandlerMsg(WHAT_SERVER_HOME_CONTENT, "Section data from server, it's send request server to get Home page data");
                }
                else {
                    IntentUtil.openHomeArticleOptionActivity(SplashActivity.this);
                }
            }

            @Override
            public void onError(Throwable t, String str) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        boolean isHomeArticleOptionScreenShown = DefaultPref.getInstance(SplashActivity.this).isHomeArticleOptionScreenShown();
                        if(isHomeArticleOptionScreenShown) {
                            sendHandlerMsg(WHAT_ACTIIVTY_TAB, "Section fetch is failed from server, and OnBoarding Screen is already loaded, So it's launching AppTabActivity");
                        }
                        else {
                            sendHandlerMsg(WHAT_ERROR, "Section fetch is failed from server, and thrown ERROR");
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
        DefaultTHApiManager.homeArticles(SplashActivity.this, "SplashActivity", new RequestCallback() {
            @Override
            public void onNext(Object o) {
                // Opens Main Tab Screen
                sendHandlerMsg(WHAT_ACTIIVTY_TAB, "Received latest data of Home Page from server, launching AppTabActivity");
            }

            @Override
            public void onError(Throwable t, String str) {
                Log.i("NSPEED", "ERROR2");
                sendHandlerMsg(WHAT_ACTIIVTY_TAB, "Failed to received data of Home Page from server, launching AppTabActivity");
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
                totalReceivedFailRequestIcons++;
                if(totalReceivedFailRequestIcons+totalReceivedSuccessRequestIcons >= totalSentRequestIcons) {
                    if(!isMpRequestSentFromBroadcastReceiver) {
                        isMpRequestSentFromBroadcastReceiver = true;
                        sendHandlerMsg(WHAT_MP, totalReceivedFailRequestIcons+" Icons are failed to download, making request for metered paywall");
                    }
                }

                Log.i("Downloading", "Fail :: "+ totalReceivedFailRequestIcons);

            }
            else if(intent.getAction().equals(IconDownloadService.MESSAGE_SUCCESS)){
                totalReceivedSuccessRequestIcons++;
                if(totalReceivedFailRequestIcons+totalReceivedSuccessRequestIcons >= totalSentRequestIcons) {
                    if(!isMpRequestSentFromBroadcastReceiver) {
                        isMpRequestSentFromBroadcastReceiver = true;
                        sendHandlerMsg(WHAT_MP, totalReceivedSuccessRequestIcons+" Icons are downloaded successfully, making request for metered paywall");
                    }
                }
                Log.i("Downloading", "Success :: "+ totalReceivedFailRequestIcons);
            }
        }
    };


    /**
     * It shows Force Update Dialog
     * @param app_store_url
     * @param message
     * @param isForceUpdate
     * @param remindMeTimeInMillies
     */
    private void showUpdateDialog(final String app_store_url, String message, final boolean isForceUpdate, String remindMeTimeInMillies) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            if (app_store_url != null)
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(app_store_url)));
                            finish();
                        } catch (android.content.ActivityNotFoundException anfe) {

                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        if (isForceUpdate) {
                            finish();
                        } else {
                            dialog.cancel();
                            sendHandlerMsg(WHAT_CONFIG_UPDATE_CHECK, "App configuration update check request is sent to server");
                        }
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setCancelable(isForceUpdate);
        builder.setTitle("A new update available");
        builder.setMessage(message);

        builder.setPositiveButton("Download", dialogClickListener);
        if (isForceUpdate) {
            builder.setNegativeButton("Cancel", dialogClickListener);
        } else {
            builder.setNegativeButton("Remind Me Later", dialogClickListener);
        }
        Dialog mDialog = builder.create();
        mDialog.show();
    }
}
