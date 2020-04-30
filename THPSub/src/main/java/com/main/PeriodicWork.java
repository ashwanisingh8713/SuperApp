package com.main;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableWidget;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

public class PeriodicWork extends Worker {

    private Context mContext;

    public PeriodicWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i("BackgroundWork","Started");
        getSectionData();
        return Result.success();
//        return Result.retry();
    }

    private void getSectionData() {
        DefaultTHApiManager.writeSectionReponseInTempTable(mContext, System.currentTimeMillis(), new RequestCallback() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t, String str) {

            }

            @Override
            public void onComplete(String str) {
                Log.i("BackgroundWork", "Received Section Data from server");
                getHomeDatafromServer();
            }
        }, "PeriodicWork");
    }

    // Get Home Article from server
    private void getHomeDatafromServer() {

        DefaultTHApiManager.homeArticles(mContext, "SplashActivity", new RequestCallback() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t, String str) {

            }

            @Override
            public void onComplete(String str) {
                final THPDB thpdb = THPDB.getInstance(mContext);
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
                            DefaultTHApiManager.widgetContent(mContext, sections);
                            return "";
                        })
                        .subscribe(onSuccess -> {
                            Log.i("BackgroundWork", "Widget :: Sent Server Request to get latest data");
                        });

            }
        });
    }
}
