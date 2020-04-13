package com.netoperation.net;

import android.content.Context;
import android.util.Log;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoPersonaliseDefault;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoSectionArticle;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableSection;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.BannerBean;
import com.netoperation.model.SectionAndWidget;
import com.netoperation.model.SectionBean;
import com.netoperation.model.SectionContentFromServer;
import com.netoperation.model.WidgetBean;
import com.netoperation.retrofit.ReqBody;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.NetConstants;
import com.ns.thpremium.BuildConfig;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DefaultTHApiManager {


    /**
     * Private Constructure
     */
    private DefaultTHApiManager() {

    }

    /**
     * Fetch section and Sub-sections from server and saves in database
     * @param context
     * @return
     */
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

                                    // Home Section,
                                    SectionBean homeBean = new SectionBean();
                                    homeBean.setSecId(NetConstants.RECO_HOME);
                                    homeBean.setSecName(NetConstants.RECO_HOME);
                                    homeBean.setLink(NetConstants.RECO_HOME);
                                    homeBean.setWebLink(NetConstants.RECO_HOME);
                                    homeBean.setShow_on_burger(true);
                                    homeBean.setShow_on_explore(true);

                                    sections.add(0, homeBean);
                                    for(SectionBean section : sections) {
                                        List<SectionBean> subSections = new ArrayList<>(section.getSubSections());
                                        section.setSubSections(null);
                                        TableSection tableSection = new TableSection(section.getSecId(), section.getSecName(), section.getType(), section, section.isShow_on_burger(), section.isShow_on_explore(), subSections);
                                        tableSection.setUserPreferred(false);
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

    /**
     * Making paraller request to get Widgets data.
     * @param context
     * @return
     */
    public static Disposable getHomeBannerContent(Context context, String secId, String secName, String type) {
        final String url = BuildConfig.DEFAULT_TH_BASE_URL+"section-content.php";
        return ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(secId, 1, type, 0))
                .subscribeOn(Schedulers.newThread())
                .subscribe(value-> {
                    Log.i("", "");
                    if(context != null) {
                        THPDB thpdb = THPDB.getInstance(context);
                        // Updating Home Banner Article
                        DaoBanner daoBanner = thpdb.daoBanner();
                        daoBanner.updateBannerArticles(secId, value.getData().getArticle());

                        // Deleting all article from TableSectionArticle  = "secId"
                        DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
                        daoSectionArticle.deleteSection(secId);

                        // Inserting new article in TableSectionArticle  = "secId"
                        TableSectionArticle tableSectionArticle = new TableSectionArticle(secId, secName, 1, value.getData().getArticle());
                        daoSectionArticle.insertSectionArticle(tableSectionArticle);
                    }
                }, throwable -> {
                    Log.i("", "");
                }, ()->{
                    Log.i("", "");
                });
    }


    /**
     * Making paraller request to get Widgets data.
     * @param context
     * @return
     */
//    public static Disposable getHomeArticle(Context context, String secId, String secName, String type) {
//
//        if(context != null) {
//            THPDB thpdb = THPDB.getInstance(context);
//            DaoBanner daoBanner = thpdb.daoBanner();
//            DaoSection daoSection = thpdb.daoSection();
//            daoBanner.getBanners().flatMap(tableBanner -> {
//                return daoSection.getSections()
//            });
//        }
//
//        final String url = BuildConfig.DEFAULT_TH_BASE_URL+"newsFeed.php";
//        return ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.homeFeed(secId, 1, type, 0))
//                .subscribeOn(Schedulers.newThread())
//                .subscribe(value-> {
//                    Log.i("", "");
//                    if(context != null) {
//                        THPDB thpdb = THPDB.getInstance(context);
//                        // Updating Home Banner Article
//                        DaoBanner daoBanner = thpdb.daoBanner();
//                        daoBanner.updateBannerArticles(secId, value.getData().getArticle());
//
//                        // Deleting all article from TableSectionArticle  = "secId"
//                        DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
//                        daoSectionArticle.deleteSection(secId);
//
//                        // Inserting new article in TableSectionArticle  = "secId"
//                        TableSectionArticle tableSectionArticle = new TableSectionArticle(secId, secName, 1, value.getData().getArticle());
//                        daoSectionArticle.insertSectionArticle(tableSectionArticle);
//                    }
//                }, throwable -> {
//                    Log.i("", "");
//                }, ()->{
//                    Log.i("", "");
//                });
//    }


    /**
     * Making paraller request to get Widgets data.
     * For Example : https://proandroiddev.com/rxjava-2-parallel-multiple-network-call-made-easy-1e1f14163eef
     * @param context
     * @param sections
     * @return
     */
    public static Disposable getWidgetContent(Context context, Map<String, String> sections) {
        final String url = BuildConfig.DEFAULT_TH_BASE_URL+"section-content.php";
        final Observable[] observables = new Observable[sections.size()];
        int count = 0;
        for (Map.Entry<String,String> entry : sections.entrySet()) {

            observables[count] =  ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(entry.getKey(), 1, entry.getValue(), 0))
                    .subscribeOn(Schedulers.newThread());
            count++;

        }
        return Observable.mergeArray(observables)
        .subscribe(value-> {
            Log.i("", "");
        }, throwable -> {
            Log.i("", "");
        }, ()->{
           Log.i("", "");
        });
    }




    public static Disposable getSectionContent(Context context, String secId, int page, String type, long lut) {
        final String url = BuildConfig.DEFAULT_TH_BASE_URL+"section-content.php";
        return ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(secId, page, type, lut))
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                    Log.i("", "");
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
                    TableSectionArticle sectionArticles = new TableSectionArticle(value.getData().getSid(), value.getData().getSname(), page, value.getData().getArticle());
                    daoSectionArticle.insertSectionArticle(sectionArticles);
                    return value;
                })
                .subscribe(value-> {
                    Log.i("", "");
                }, throwable -> {
                    Log.i("", "");
                });
    }




}
