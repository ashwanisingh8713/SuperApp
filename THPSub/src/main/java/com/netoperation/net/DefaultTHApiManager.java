package com.netoperation.net;

import android.content.Context;
import android.util.Log;

import com.netoperation.model.SectionAndWidget;
import com.netoperation.retrofit.ReqBody;
import com.netoperation.retrofit.ServiceFactory;
import com.ns.thpremium.BuildConfig;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class DefaultTHApiManager {


    private DefaultTHApiManager() {
    }

    public static void sectionList(Context context) {
        String url = BuildConfig.DEFAULT_TH_BASE_URL+"sectionList_v4.php";
        Observable<SectionAndWidget> observable = ServiceFactory.getServiceAPIs().sectionList(url, ReqBody.sectionList());
        observable.subscribeOn(Schedulers.newThread())
                .map(sectionAndWidget -> {

                            return sectionAndWidget;
                        }
                )
                .subscribe(value -> {
                    Log.i("", "");

                }, throwable -> {
                    Log.i("", "");
                }, () -> {
                    Log.i("", "");
                });
    }
}
