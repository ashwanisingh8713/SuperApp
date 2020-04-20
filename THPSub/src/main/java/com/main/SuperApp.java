package com.main;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableWidget;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;

public class SuperApp extends Application implements LifecycleObserver {

    private static Context sAppContext;
    public static boolean isInBackground;

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

        startPeriodicWork();
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        // app moved to foreground
        isInBackground =false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        // app moved to background
        isInBackground =true;

        // When app is in background then it will fetch section data from server
        DefaultTHApiManager.writeSectionReponseInTempTable(this, System.currentTimeMillis(), new RequestCallback() {
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
        });

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
                            Log.i(NetConstants.UNIQUE_TAG, "SplashActivity :: Widget :: Sent Server Request to get latest data");
                        });

            }
        });
    }

    /**
     * THis is workmanager implementation to sync Section and Home data from server.
     */
    private void startPeriodicWork() {
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicWork.class, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
                .addTag("periodicWorkRequest").build();

        /*PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(PeriodicWork.class,
                5, TimeUnit.MINUTES)
                .addTag("periodicWorkRequest")
                .build();*/
        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }
}
