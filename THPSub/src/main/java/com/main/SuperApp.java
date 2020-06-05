package com.main;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.clevertap.android.sdk.Application;
import com.clevertap.android.sdk.CleverTapAPI;
import com.comscore.Analytics;
import com.comscore.PublisherConfiguration;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableWidget;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.RealmSupport;
import com.ns.utils.THPConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;

public class SuperApp extends Application implements LifecycleObserver  {

    private static Context sAppContext;
    public static boolean isInBackground;



    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        startPeriodicWork();
        initCleverTap();

    }

    public static Context getAppContext() {
        return sAppContext;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        // app moved to foreground
        isInBackground =false;

        /*Comscore integration*/
        if(!NetConstants.IS_HOLD) {
            PublisherConfiguration publisher = new PublisherConfiguration.Builder()
                    .publisherSecret(getResources().getString(R.string.comscore_publisher_secret))
                    .publisherId(getResources().getString(R.string.comscore_customer_c2))
                    .build();

            Analytics.getConfiguration().addClient(publisher);
            Analytics.start(getApplicationContext());
        }
        /*Comscore ends*/
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        // app moved to background
        isInBackground =true;

        // When app is in background then it will fetch section data from server
        DefaultTHApiManager.writeSectionReponseInTempTable(this, DefaultPref.getInstance(this).getDefaultContentBaseUrl(),
                System.currentTimeMillis(), new RequestCallback() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t, String str) {

            }

            @Override
            public void onComplete(String str) {
                 getHomeDatafromServer();
            }
        }, "SuperApp");

        // Reduces Read article table
        DefaultTHApiManager.readArticleDelete(this);
        // Reduces Related article table
        DefaultTHApiManager.deleteRelatedArticle();

        THPConstants.SEARCH_BY_ARTICLE_ID_URL = null;

    }

    /**
     * When app is in background then it will fetch home and widget data from server
     */
    private void getHomeDatafromServer() {

        DefaultTHApiManager.homeArticles(SuperApp.this, "SuperApp.java", new RequestCallback() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t, String str) {

            }

            @Override
            public void onComplete(String str) {
                final THPDB thpdb = THPDB.getInstance(SuperApp.this);
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
                            DefaultTHApiManager.widgetContent(SuperApp.this, sections);
                            return "";
                        })
                        .subscribe(onSuccess -> {
                            Log.i(NetConstants.TAG_UNIQUE, "SplashActivity :: Widget :: Sent Server Request to get latest data");
                        });
            }
        });
    }

    /**
     * THis is workmanager implementation to sync Section and Home data from server.
     */
    private void startPeriodicWork() {

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresDeviceIdle(true)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicWork.class, 45, TimeUnit.MINUTES, 10, TimeUnit.MINUTES)
                .addTag("periodicWorkRequest")
                .setConstraints(constraints)
                .build();


        /*PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicWork.class,
                5, TimeUnit.MINUTES)
                .addTag("periodicWorkRequest")
                .build();*/
        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }

    private void initCleverTap() {
        CleverTapAPI cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
        //Set DebugLevel as below - From CleverTap Support
        //CleverTapAPI.setDebugLevel(1277182231);
        if (BuildConfig.DEBUG) {
            CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.DEBUG);
        } else {
            CleverTapAPI.setDebugLevel(CleverTapAPI.LogLevel.OFF);
        }
        CleverTapAPI.createNotificationChannelGroup(getApplicationContext(),"TheHindu","TheHindu");
        CleverTapAPI.createNotificationChannel(getApplicationContext(),"TheHindu",
                "TheHindu","TheHindu",
                NotificationManager.IMPORTANCE_MAX,"TheHindu",true);
    }


}
