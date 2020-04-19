package com.ns.activity;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableWidget;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;
import com.ns.thpremium.R;
import com.ns.utils.IntentUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseAcitivityTHP {

    private static String TAG = NetConstants.UNIQUE_TAG;


    @Override
    public int layoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTHApiManager.writeSectionReponseInTempTable(this, System.currentTimeMillis());

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
                IntentUtil.openMainTabPage(SplashActivity.this);
            }
        });


        // Reduces Read article table
        DefaultTHApiManager.readArticleDelete(this);

    }


    private void getSectionDirectFromServer() {
        DefaultTHApiManager.sectionDirectFromServer(this, new RequestCallback() {
            @Override
            public void onNext(Object o) {
                final THPDB thpdb = THPDB.getInstance(SplashActivity.this);
                DefaultTHApiManager.homeArticles(SplashActivity.this, "SplashActivity", null);
                DaoWidget daoWidget = thpdb.daoWidget();
                daoWidget.getWidgetsSingle()
                        .subscribeOn(Schedulers.io())
                        .map(widgets -> {
                            final Map<String, String> sections = new HashMap<>();
                            for (TableWidget widget : widgets) {
                                sections.put(widget.getSecId(), widget.getType());
                            }
                            DefaultTHApiManager.widgetContent(SplashActivity.this, sections);
                            return "";
                        })
                        .subscribe(onSuccess -> {
                            Log.i(TAG, "SplashActivity :: Widget :: Sent Server Request to get latest data");
                        });

            }

            @Override
            public void onError(Throwable t, String str) {

            }

            @Override
            public void onComplete(String str) {
                IntentUtil.openMainTabPage(SplashActivity.this);
            }

        }, System.currentTimeMillis());
    }
}
