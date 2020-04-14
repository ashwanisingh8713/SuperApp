package com.netoperation.net;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoHomeArticle;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoSectionArticle;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TableSection;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.BannerBean;
import com.netoperation.model.HomeData;
import com.netoperation.model.SectionAndWidget;
import com.netoperation.model.SectionBean;
import com.netoperation.model.SectionContentFromServer;
import com.netoperation.model.THDefaultPersonalizeBean;
import com.netoperation.model.WidgetBean;
import com.netoperation.retrofit.ReqBody;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.NetConstants;
import com.ns.thpremium.BuildConfig;
import com.ns.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DefaultTHApiManager {

    private static String TAG = NetConstants.UNIQUE_TAG;


    /**
     * Private Constructure
     */
    private DefaultTHApiManager() {

    }

    /**
     * Fetch section and Sub-sections from server and saves in database
     *
     * @param context
     * @return
     */
    public static Disposable sectionList(Context context, RequestCallback callback) {
        String url = BuildConfig.DEFAULT_TH_BASE_URL + "sectionList_v4.php";
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
                                    tableBanner = new TableBanner(bannerBean.getSecId(), bannerBean.getSecName(), bannerBean.getType(), "" + date, dataBean.getHome().getStaticPageUrl());
                                    daoBanner.insertBanner(tableBanner);

                                    // Table Widget
                                    List<WidgetBean> widgetBeans = dataBean.getHome().getWidget();
                                    for (WidgetBean widgetBean : widgetBeans) {
                                        TableWidget tableWidget = new TableWidget(widgetBean.getSecId(), widgetBean.getSecName(), widgetBean.getType(), widgetBean.isViewAllCTA());
                                        daoWidget.insertWidget(tableWidget);
                                    }

                                    // Table Section List
                                    List<SectionBean> sections = dataBean.getSection();

                                    // Home Section,
                                    SectionBean homeBean = new SectionBean();
                                    homeBean.setSecId(NetConstants.RECO_HOME_TAB);
                                    homeBean.setSecName(NetConstants.RECO_HOME_TAB);
                                    homeBean.setLink(NetConstants.RECO_HOME_TAB);
                                    homeBean.setWebLink(NetConstants.RECO_HOME_TAB);
                                    homeBean.setShow_on_burger(true);
                                    homeBean.setShow_on_explore(true);
                                    sections.add(0, homeBean);

                                    final List<THDefaultPersonalizeBean> defaultPersonalizeBeans = dataBean.getHome().getPersonalize();

                                    for (SectionBean section : sections) {
                                        List<SectionBean> subSections = new ArrayList<>(section.getSubSections());
                                        section.setSubSections(null);
                                        TableSection tableSection = new TableSection(section.getSecId(), section.getSecName(), section.getType(), section, section.isShow_on_burger(), section.isShow_on_explore(), subSections);
                                        // Adding Default selected persionlise news feed section
                                        THDefaultPersonalizeBean personalizeBean = new THDefaultPersonalizeBean();
                                        personalizeBean.setSecId(section.getSecId());
                                        if (defaultPersonalizeBeans.contains(personalizeBean)) {
                                            tableSection.setUserPreferred(true);
                                        }
                                        daoSection.insertSection(tableSection);
                                    }
                                }
                            }
                            return sectionAndWidget;
                        }
                )
                .subscribe(value -> {
                    if (callback != null) {
                        callback.onNext(value);
                    }
                    Log.i(TAG, "sectionList :: subscribe");

                }, throwable -> {
                    Log.i(TAG, "");
                    if (callback != null) {
                        callback.onError(throwable, "sectionList");
                    }
                    Log.i(TAG, "sectionList :: throwable "+throwable);
                }, () -> {
                    Log.i(TAG, "");
                    if (callback != null) {
                        callback.onComplete("sectionList");
                    }
                    Log.i(TAG, "sectionList :: completed");
                });
    }


    /**
     * Making request to get Home data.
     * For Example http://getgoingit.blogspot.com/2018/05/passing-value-from-one-observable-to.html
     * For Example https://medium.com/androiddevelopers/room-rxjava-acb0cd4f3757
     *
     * @param context
     * @return
     */
    public static Disposable homeArticles(Context context, String from) {

        if (context != null) {
            Log.i(TAG, from+" :: HomeArticles :: Sent Server Request to get latest data");
            THPDB thpdb = THPDB.getInstance(context);
            Observable<TableBanner> bannerObservable = Observable.just("tableBanner").map(val -> {
                final DaoBanner daoBanner = thpdb.daoBanner();
                TableBanner tableBanner = daoBanner.getBanners();
                return tableBanner;
            }).subscribeOn(Schedulers.io());

            Observable<List<TableSection>> sectionObservable = Observable.just("TableSection").map(val -> {
                final DaoSection daoSection = thpdb.daoSection();
                List<TableSection> tableSections = daoSection.getUserPreferredSection();
                return tableSections;
            }).subscribeOn(Schedulers.io());


            return bannerObservable
                    .switchMap(bannerVal -> {
                        return sectionObservable.map(sectionVal -> {
                            final String url = BuildConfig.DEFAULT_TH_BASE_URL + "newsFeed.php";
                            String bannerId = bannerVal.getSecId();

                            JsonArray personliseSectionIds = new JsonArray();
                            for (TableSection section : sectionVal) {
                                personliseSectionIds.add(section.getSecId());
                            }

                            return ServiceFactory.getServiceAPIs().homeContent(url, ReqBody.homeFeed(personliseSectionIds, bannerId, 0));
                        });

                    })
                    .switchMap(val -> {
                        Log.i("", "");
                        return val.subscribeOn(Schedulers.newThread());
                    })
                    .map(homeData -> {
                        if (context != null) {
                            THPDB db = THPDB.getInstance(context);
                            final DaoBanner daoBanner = thpdb.daoBanner();
                            // Banner Article Update
                            TableBanner tableBanner1 = daoBanner.getBanners();
                            tableBanner1.setBeans(homeData.getNewsFeed().getBanner());
                            daoBanner.deleteAndInsertInBanner(tableBanner1);

                            Log.i(TAG, "homeArticles :: Banner Article Added "+homeData.getNewsFeed().getBanner().size()+" Size");

                            DaoHomeArticle daoHomeArticle = db.daoHomeArticle();
                            // Delete All before Inserting New articles
                            daoHomeArticle.deleteAll();
                            // Insert articles in HomeArticle Table
                            for (HomeData.NewsFeedBean.ArticlesBean articlesBeans : homeData.getNewsFeed().getArticles()) {
                                TableHomeArticle tableHomeArticle = new TableHomeArticle(articlesBeans.getSec_id(), articlesBeans.getData());
                                daoHomeArticle.insertHomeArticle(tableHomeArticle);
                                Log.i(TAG, "homeArticles :: Home Article Added SEC-ID ::"+articlesBeans.getSec_id()+" :: "+articlesBeans.getData().size()+" Size");
                            }
                        }
                        return "";
                    })
                    .subscribe(val -> {
                        Log.i(TAG, "homeArticles :: subscribe");
                    }, throwable -> {
                        Log.i(TAG, "homeArticles :: throwable "+throwable);
                    }, () -> {
                        Log.i(TAG, "homeArticles :: completed");
                    });
        }

        return null;


    }


    /**
     * Making paraller request to get Widgets data.
     * For Example : https://proandroiddev.com/rxjava-2-parallel-multiple-network-call-made-easy-1e1f14163eef
     * @param context
     * @param sections
     * @return
     */
    public static Disposable widgetContent(Context context, Map<String, String> sections) {
        final String url = BuildConfig.DEFAULT_TH_BASE_URL + "section-content.php";
        final Observable[] observables = new Observable[sections.size()];
        int count = 0;
        for (Map.Entry<String, String> entry : sections.entrySet()) {
            observables[count] = ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(entry.getKey(), 1, entry.getValue(), 0))
                    .subscribeOn(Schedulers.newThread());
            count++;
        }
        return Observable.mergeArray(observables)
                .map(value -> {
                    SectionContentFromServer sectionContent = (SectionContentFromServer) value;
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoWidget daoWidget = thpdb.daoWidget();

                    TableWidget tableWidget = daoWidget.getWidget(sectionContent.getData().getSid());
                    tableWidget.setBeans(sectionContent.getData().getArticle());
                    daoWidget.deleteAndInsertWidget(sectionContent.getData().getSid(), tableWidget);

                    return sectionContent.getData().getSid() + "-" + sectionContent.getData().getSname();
                })
                .subscribe(value -> {
                    Log.i(TAG, "widgetContent :: subscribe-" + value);
                }, throwable -> {
                    Log.i(TAG, "widgetContent :: throwable "+throwable);
                }, () -> {
                    Log.i(TAG, "widgetContent :: completed");
                });
    }


    /**
     * Making request to get Section or sub-section articles from server
     *
     * @param context
     * @param secId
     * @param page
     * @param type
     * @param lut
     * @return
     */
    public static Disposable getSectionContent(Context context, String secId, int page, String type, long lut) {
        final String url = BuildConfig.DEFAULT_TH_BASE_URL + "section-content.php";
        return ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(secId, page, type, lut))
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    Log.i("", "");
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
                    TableSectionArticle sectionArticles = new TableSectionArticle(value.getData().getSid(), value.getData().getSname(), page, value.getData().getArticle());
                    daoSectionArticle.insertSectionArticle(sectionArticles);
                    return value;
                })
                .subscribe(value -> {
                    Log.i(TAG, "getSectionContent :: subscribe");
                }, throwable -> {
                    Log.i(TAG, "getSectionContent :: throwable "+throwable);
                }, () -> {
                    Log.i(TAG, "getSectionContent :: completed");
                });
    }


}
