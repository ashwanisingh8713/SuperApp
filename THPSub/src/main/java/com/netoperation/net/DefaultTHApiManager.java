package com.netoperation.net;

import android.content.Context;
import android.util.Log;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableSection;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.BannerBean;
import com.netoperation.model.SectionAndWidget;
import com.netoperation.model.SectionBean;
import com.netoperation.model.WidgetBean;
import com.netoperation.retrofit.ReqBody;
import com.netoperation.retrofit.ServiceFactory;
import com.ns.thpremium.BuildConfig;
import com.ns.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DefaultTHApiManager {


    private DefaultTHApiManager() {

    }

    public static Disposable sectionList(Context context) {
        String url = BuildConfig.DEFAULT_TH_BASE_URL+"sectionList_v4.php";
        Observable<SectionAndWidget> observable = ServiceFactory.getServiceAPIs().sectionList(url, ReqBody.sectionList());
        return observable.subscribeOn(Schedulers.newThread())
                .timeout(15, TimeUnit.SECONDS)
                .map(sectionAndWidget -> {
                            THPDB db = THPDB.getInstance(context);
                            DaoSection daoSection = db.daoSection();
                            DaoBanner daoBanner = db.daoBanner();
                            DaoWidget daoWidget = db.daoWidget();

                            SectionAndWidget.DataBean dataBean = sectionAndWidget.getData();
                            long date = dataBean.getDate();

                            if (daoBanner != null) {
                                TableBanner tableBanner = daoBanner.getBanners();
                                if (tableBanner != null && !ResUtil.isEmpty(tableBanner.getLastUpdatedTime()) && tableBanner.getLastUpdatedTime().equals("" + date)) {

                                    return "";
                                } else {
                                    daoSection.deleteAll();
                                    daoBanner.deleteAll();
                                    daoWidget.deleteAll();

                                    // Table Banner
                                    BannerBean bannerBean = dataBean.getHome().getBanner();
                                    tableBanner = new TableBanner(bannerBean.getSecId(), bannerBean.getSecName(), bannerBean.getType(), ""+date, dataBean.getHome().getStaticPageUrl());
                                    daoBanner.insertWidget(tableBanner);

                                    // Table Widget
                                    List<WidgetBean> widgetBeans = dataBean.getHome().getWidget();
                                    for(WidgetBean widgetBean : widgetBeans) {
                                        TableWidget tableWidget = new TableWidget(widgetBean.getSecId(), widgetBean.getSecName(), widgetBean.getType(), widgetBean.isViewAllCTA());
                                        daoWidget.insertWidget(tableWidget);
                                    }

                                    // Table Section List
                                    List<SectionBean> sections = dataBean.getSection();
                                    for(SectionBean section : sections) {
                                        List<SectionBean> subSections = new ArrayList<>(section.getSubSections());
                                        section.setSubSections(null);
                                        TableSection tableSection = new TableSection(section.getSecId(), section, section.isShow_on_burger(), section.isShow_on_explore(), subSections);
                                        daoSection.insertSection(tableSection);
                                    }


                                }

                            }


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
