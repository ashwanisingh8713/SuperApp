package com.netoperation.net;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.main.SuperApp;
import com.netoperation.config.download.FileUtils;
import com.netoperation.config.model.TabsBean;
import com.netoperation.db.DaoMP;
import com.netoperation.db.THPDB;
import com.netoperation.db.TableBookmark;
import com.netoperation.db.TableMP;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoConfiguration;
import com.netoperation.default_db.DaoHomeArticle;
import com.netoperation.default_db.DaoMPReadArticle;
import com.netoperation.default_db.DaoPersonaliseDefault;
import com.netoperation.default_db.DaoRead;
import com.netoperation.default_db.DaoRelatedArticle;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoSectionArticle;
import com.netoperation.default_db.DaoTempWork;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TableMPReadArticle;
import com.netoperation.default_db.TableOptional;
import com.netoperation.default_db.TablePersonaliseDefault;
import com.netoperation.default_db.TableRead;
import com.netoperation.default_db.TableRelatedArticle;
import com.netoperation.default_db.TableSection;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.default_db.TableTempWork;
import com.netoperation.default_db.TableTemperoryArticle;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.BannerBean;
import com.netoperation.model.BookmarkBean;
import com.netoperation.model.HomeData;
import com.netoperation.model.MPConfigurationModel;
import com.netoperation.model.MPCycleDurationModel;
import com.netoperation.model.NSE_BSE_Data;
import com.netoperation.model.SearchedArticleModel;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.SectionAndWidget;
import com.netoperation.model.SectionBean;
import com.netoperation.model.SectionContentFromServer;
import com.netoperation.model.THDefaultPersonalizeBean;
import com.netoperation.model.USPData;
import com.netoperation.model.UpdateModel;
import com.netoperation.model.WidgetBean;
import com.netoperation.retrofit.ReqBody;
import com.netoperation.retrofit.ServiceAPIs;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.model.BSEData;
import com.ns.model.NSEData;
import com.ns.model.SensexStatus;
import com.ns.thpremium.BuildConfig;
import com.ns.utils.BLConstants;
import com.ns.utils.RealmSupport;
import com.ns.utils.ResUtil;
import com.ns.utils.RowIds;
import com.ns.utils.THPConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.Sort;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefaultTHApiManager {

    private static String TAG = NetConstants.TAG_UNIQUE;


    /**
     * Private Constructure
     */
    private DefaultTHApiManager() {

    }

    /**
     * Inserts SectionAndWedget object response into respective table.
     * @param sectionAndWidget
     * @return
     */
    private static SectionAndWidget insertSectionResponseInDB(SectionAndWidget sectionAndWidget, boolean isFromTempTable) {
        THPDB db = THPDB.getInstance(SuperApp.getAppContext());
        DaoSection daoSection = db.daoSection();
        DaoBanner daoBanner = db.daoBanner();
        DaoWidget daoWidget = db.daoWidget();

        SectionAndWidget.DataBean dataBean = sectionAndWidget.getData();
        long date = dataBean.getDate();

        if (daoBanner != null) {
            TableBanner tableBanner = daoBanner.getBanners();

            /*if (tableBanner != null && !ResUtil.isEmpty(tableBanner.getLastUpdatedTime()) && tableBanner.getLastUpdatedTime().equals("" + date)) {
                return sectionAndWidget;
            }
            else */{
                daoSection.deleteAll();

                if(!isFromTempTable) {
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
                }

                // Table Section List
                List<SectionBean> sections = dataBean.getSection();

                // Home Section,
                SectionBean homeBean = new SectionBean();
                homeBean.setSecId(NetConstants.RECO_HOME_TAB);
                homeBean.setSecName(NetConstants.RECO_HOME_TAB);
                homeBean.setLink(NetConstants.RECO_HOME_TAB);
                homeBean.setWebLink(NetConstants.RECO_HOME_TAB);
                homeBean.setType("GN");

                homeBean.setOverridePriority(0);
                homeBean.setOverridePriority(0);
                homeBean.setShow_on_burger(true);
                homeBean.setShow_on_explore(true);
                sections.add(0, homeBean);


                DaoPersonaliseDefault daoPersonaliseDefault = db.daoPersonaliseDefault();
                List<TablePersonaliseDefault> tablePersonaliseDefaults = daoPersonaliseDefault.getAllPersonalise();
                if (tablePersonaliseDefaults == null || tablePersonaliseDefaults.size() == 0) {
                    final List<THDefaultPersonalizeBean> defaultPersonalizeBeans = dataBean.getHome().getPersonalize();
                    for (THDefaultPersonalizeBean personalizeBean : defaultPersonalizeBeans) {
                        TablePersonaliseDefault personaliseDefault = new TablePersonaliseDefault(NetConstants.PERSONALISE_CATEGORY_NEWS, "0",
                                personalizeBean.getSecId(), null, personalizeBean.getSecName(), false, true);
                        daoPersonaliseDefault.insertDefaultPersonalise(personaliseDefault);
                    }
                }

                for (SectionBean section : sections) {
                    List<SectionBean> subSections = new ArrayList<>(section.getSubSections());
                    section.setSubSections(null);
                    TableSection tableSection = new TableSection(section.getSecId(),
                            section.getSecName(), section.getType(), section,
                            section.isShow_on_burger(), section.isShow_on_explore(),
                            subSections, section.getStaticPageUrl(), section.getCustomScreen(), section.getCustomScreenPri());

                    // Adding Default selected persionlise news feed section
                    THDefaultPersonalizeBean personalizeBean = new THDefaultPersonalizeBean();
                    personalizeBean.setSecId(section.getSecId());
                    daoSection.insertSection(tableSection);
                }
            }
        }

        return sectionAndWidget;
    }

    /**
     * Fetch section and Sub-sections from server and saves in database
     *
     * @param context
     * @return
     */
    public static Disposable sectionDirectFromServer(Context context, RequestCallback callback, final long executionTime) {
        String url = DefaultPref.getInstance(context).getDefaultContentBaseUrl() + "sectionList_v4.php";
        Observable<SectionAndWidget> observable = ServiceFactory.getServiceAPIs().sectionList(url, ReqBody.sectionList());
        return observable.subscribeOn(Schedulers.newThread())
                .timeout(10, TimeUnit.SECONDS)
                .map(sectionAndWidget -> {
                            return insertSectionResponseInDB(sectionAndWidget, false);
                        }
                )
                .subscribe(value -> {
                    if (callback != null) {
                        callback.onNext(value);
                    }
                    long totalExecutionTime = System.currentTimeMillis() - executionTime;
                    Log.i("TotalExec", "sectionDirectFromServer() :: Get From Server, Insert into respective tables:: " + totalExecutionTime);

                }, throwable -> {
                    if (callback != null) {
                        callback.onError(throwable, "sectionDirectFromServer");
                    }
                    Log.i(NetConstants.TAG_ERROR, "sectionDirectFromServer() :: " + throwable);
                }, () -> {
                    if (callback != null) {
                        callback.onComplete("sectionDirectFromServer");
                    }
                    Log.i(TAG, "sectionDirectFromServer() :: completed");
                });
    }

    public static Disposable getSectionsFromTempTable(Context context, final long executionTime, RequestCallback callback) {
        return Observable.just("sectionAndWidget")
                .subscribeOn(Schedulers.io())
                .map(val -> {
                    THPDB db = THPDB.getInstance(context);
                    DaoTempWork daoTempWork = db.daoTempWork();
                    TableTempWork tempSection = daoTempWork.getTableTempWork(NetConstants.TEMP_SECTION_ID);
                    Gson gson = new Gson();
                    SectionAndWidget sectionAndWidget = gson.fromJson(tempSection.getJsonString(), SectionAndWidget.class);
                    return insertSectionResponseInDB(sectionAndWidget, true);
                })
                .subscribe(value -> {
                    if (callback != null) {
                        callback.onNext(value);
                    }
                    long totalExecutionTime = System.currentTimeMillis() - executionTime;
                    Log.i("TotalExec", "Read from Temp, Json String:: " + totalExecutionTime);

                }, throwable -> {
                    if (callback != null) {
                        callback.onError(throwable, "getSectionsFromTempTable");
                    }
                    Log.i(NetConstants.TAG_ERROR, "getSectionsFromTempTable() :: " + throwable);
                }, () ->{
                    if (callback != null) {
                        callback.onComplete("getSectionsFromTempTable");
                    }
                });
    }

    /**
     * Fetch section and Sub-sections from server and saves in Temp-Folder
     * @param context
     * @return
     */
    public static Disposable writeSectionReponseInTempTable(Context context, String defaultContentBaseUrl, final long executionTime, RequestCallback callback, String from) {
        String url = defaultContentBaseUrl + "sectionList_v4.php";
        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().sectionListForJson(url, ReqBody.sectionList());
        return observable.subscribeOn(Schedulers.newThread())
                .timeout(15, TimeUnit.SECONDS)
                .map(sectionAndWidget -> {
                            THPDB db = THPDB.getInstance(context);
                            TableTempWork sectionTemp = new TableTempWork(NetConstants.TEMP_SECTION_ID, sectionAndWidget.toString());
                            db.daoTempWork().insertTempWork(sectionTemp);
                            return "";
                        }
                )
                .subscribe(value -> {
                    long totalExecutionTime = System.currentTimeMillis() - executionTime;
                    Log.i("TotalExec", "Write Section String In Temp DB :: " + totalExecutionTime);
                    if (callback != null) {
                        callback.onNext(value);
                    }
                }, throwable -> {
                    if (callback != null) {
                        callback.onError(throwable, "writeSectionReponseInTempTable");
                    }
                    Log.i(NetConstants.TAG_ERROR, from+" :: writeSectionReponseInTempTable() :: " + throwable);
                }, () -> {

                    if (callback != null) {
                        callback.onComplete("writeSectionReponseInTempTable()");
                    }
                });
    }

    public static void test() {
        String [] ids = {"12","13","7","6","52"};

        final JsonArray personliseSectionIds = new JsonArray();
        for (String personaliseDefault : ids) {
            personliseSectionIds.add(personaliseDefault);
        }
        String url = "https://app.thehindu.com/hindu/service/api_v1.004/newsFeed.php";

        ServiceFactory.getServiceAPIs().homeContent(url, ReqBody.homeFeed(personliseSectionIds, "43", System.currentTimeMillis()))
                .subscribeOn(Schedulers.newThread())
                .map(val->{
                    Log.i("", "");
                    return "";
                })
        .subscribe(val->{

        }, throwable -> {

        });

    }


    /**
     * Making request to get Home data.
     * For Example http://getgoingit.blogspot.com/2018/05/passing-value-from-one-observable-to.html
     * For Example https://medium.com/androiddevelopers/room-rxjava-acb0cd4f3757
     * @param context
     * @return
     */
    public static Disposable homeArticles(Context context, String from, RequestCallback callback) {
        if (context != null) {
            Log.i(TAG, from + " :: HomeArticles :: Sent Server Request to get latest data");
            THPDB thpdb = THPDB.getInstance(context);
            Observable<TableBanner> bannerObservable = Observable.just("tableBanner").map(val -> {
                final DaoBanner daoBanner = thpdb.daoBanner();
                TableBanner tableBanner = daoBanner.getBanners();
                return tableBanner;
            }).subscribeOn(Schedulers.io());

            Observable<List<TablePersonaliseDefault>> sectionObservable = Observable.just("TablePersonaliseDefault").map(val -> {
                final DaoPersonaliseDefault daoSection = thpdb.daoPersonaliseDefault();
                List<TablePersonaliseDefault> personaliseIdsTable = daoSection.getAllPersonalise();
                return personaliseIdsTable;
            }).subscribeOn(Schedulers.io());

            return bannerObservable
                    .switchMap(tableBanner -> {
                        return sectionObservable.map(personaliseIdsTable -> {
                            DaoConfiguration daoConfiguration = THPDB.getInstance(SuperApp.getAppContext()).daoConfiguration();
                            String contentDefaultUrl = daoConfiguration.getConfiguration().getContentUrl().getBaseUrlDefault();
                            final String url = contentDefaultUrl + "newsFeed.php";
                            String bannerId = tableBanner.getSecId();

                            final JsonArray personliseSectionIds = new JsonArray();
                            for (TablePersonaliseDefault personaliseDefault : personaliseIdsTable) {
                                personliseSectionIds.add(personaliseDefault.getPersonaliseSecId());
                            }

                            return ServiceFactory.getServiceAPIs().homeContent(url, ReqBody.homeFeed(personliseSectionIds, bannerId, 0));
                        });

                    })
                    .switchMap(homeDataObservable -> {
                        return homeDataObservable.subscribeOn(Schedulers.io()).timeout(10, TimeUnit.SECONDS);
                    })
                    .map(homeData -> {
                        if (context != null) {
                            THPDB db = THPDB.getInstance(context);
                            final DaoBanner daoBanner = thpdb.daoBanner();
                            // Banner Article Update
                            TableBanner banner = daoBanner.getBanners();
                            List<ArticleBean> bannerArticles = homeData.getNewsFeed().getBanner();
                            if(bannerArticles.size() > 0) {
                                daoBanner.deleteAll();
                                TableBanner tableBanner = new TableBanner(banner.getSecId(), banner.getSecName(), banner.getType(), banner.getLastUpdatedTime(), banner.getStaticPageBean());
                                tableBanner.setBeans(bannerArticles);
                                daoBanner.insertBanner(tableBanner);
                                Log.i("HomeData", "Banner :: Inserted");
                                // Insert Related Article
                                DefaultTHApiManager.insertRelatedArticle(bannerArticles);
                            } else {
                                Log.i("HomeData", "Banner :: Not Valid");
                            }

                            Log.i(TAG, "homeArticles :: Banner Article Added " + homeData.getNewsFeed().getBanner().size() + " Size");

                            DaoHomeArticle daoHomeArticle = db.daoHomeArticle();

                            // Delete All before Inserting New articles, But it is conditional because sometimes getting empty list
                            boolean needToDeleteAllHomeArticles = true;
                            if(homeData.getNewsFeed().getArticles().size() > 0) {
                                for (HomeData.NewsFeedBean.ArticlesBean articlesBeans : homeData.getNewsFeed().getArticles()) {
                                    if (articlesBeans.getData() == null || articlesBeans.getData().size() == 0) {
                                        needToDeleteAllHomeArticles = false;
                                        break;
                                    }
                                }
                            }
                            if(needToDeleteAllHomeArticles) {
                                daoHomeArticle.deleteAll();
                            }
                            // Insert articles in HomeArticle Table
                            for (HomeData.NewsFeedBean.ArticlesBean articlesBeans : homeData.getNewsFeed().getArticles()) {
                                if(articlesBeans.getData() == null || articlesBeans.getData().size() == 0) {
                                    Log.i("HomeData", "Article :: Not Valid");
                                    continue;
                                }
                                // If DeleteAll is not done, then before inserting we have to deleting existing secId's articles
                                if(!needToDeleteAllHomeArticles) {
                                    daoHomeArticle.delete(articlesBeans.getSec_id());
                                }
                                TableHomeArticle tableHomeArticle = new TableHomeArticle(articlesBeans.getSec_id(), articlesBeans.getData());
                                daoHomeArticle.insertHomeArticle(tableHomeArticle);
                                Log.i("HomeData", "Article :: Inserted");
                                Log.i(TAG, "homeArticles :: Home Article Added SEC-ID ::" + articlesBeans.getSec_id() + " :: " + articlesBeans.getData().size() + " Size");
                                // Insert Related Article
                                DefaultTHApiManager.insertRelatedArticle(articlesBeans.getData());
                            }
                        }
                        return "";
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(val -> {
                        if (callback != null) {
                            callback.onNext("homeArticles");
                        }
                        Log.i(TAG, "homeArticles() :: subscribe");
                    }, throwable -> {
                        if (callback != null) {
                            callback.onError(throwable, "homeArticles");
                        }
                        Log.i(NetConstants.TAG_ERROR, "homeArticles() :: " + throwable);
                    }, () -> {
                        if (callback != null) {
                            callback.onComplete("homeArticles");
                        }
                        Log.i(TAG, "homeArticles() :: completed");
                    });
        }

        return Observable.just("").subscribe();

    }


    /**
     * Making paraller request to get Widgets data.
     * For Example : https://proandroiddev.com/rxjava-2-parallel-multiple-network-call-made-easy-1e1f14163eef
     * @param context
     * @param sections
     * @return
     */
    public static void widgetContent(Context context, Map<String, String> sections) {

        Observable.just("Configuration")
                .subscribeOn(Schedulers.io())
                .map(val->{
                    DaoConfiguration daoConfiguration = THPDB.getInstance(context).daoConfiguration();
                    String contentDefaultUrl = daoConfiguration.getConfiguration().getContentUrl().getBaseUrlDefault();
                    return contentDefaultUrl;
                })
                .subscribe(contentDefaultUrl->{

                    final String url = contentDefaultUrl + "section-content.php";
                    final Observable[] observables = new Observable[sections.size()];
                    int count = 0;
                    for (Map.Entry<String, String> entry : sections.entrySet()) {
                        observables[count] = ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(entry.getKey(), 1, entry.getValue(), 0))
                                .subscribeOn(Schedulers.io());
                        count++;
                    }
                    if (observables.length > 0) {
                        Observable.mergeArray(observables)
                                .map(value -> {
                                    SectionContentFromServer sectionContent = (SectionContentFromServer) value;
                                    THPDB thpdb = THPDB.getInstance(context);
                                    DaoWidget daoWidget = thpdb.daoWidget();
                                    DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
                                    if(sectionContent.getData().getArticle().size() > 0) {
                                        TableWidget tableWidget = daoWidget.getWidget(sectionContent.getData().getSid());
                                        tableWidget.setBeans(sectionContent.getData().getArticle());
                                        daoWidget.deleteAndInsertWidget(sectionContent.getData().getSid(), tableWidget);

                                        // Delete Articles from respective TableSectionArticle
                                        daoSectionArticle.deleteSectionAllArticle(sectionContent.getData().getSid());
                                        // Insert Articles in respective TableSectionArticle
                                        TableSectionArticle tableSectionArticle = new TableSectionArticle(sectionContent.getData().getSid(), sectionContent.getData().getSname(), 1, sectionContent.getData().getArticle());
                                        daoSectionArticle.insertSectionArticle(tableSectionArticle);

                                        Log.i("HomeData", "widgetContent :: Inserted");

                                        // Insert Related Article
                                        DefaultTHApiManager.insertRelatedArticle(sectionContent.getData().getArticle());
                                    }
                                    else {
                                        Log.i("HomeData", "widgetContent :: Not Valid");
                                    }

                                    return sectionContent.getData().getSid() + "-" + sectionContent.getData().getSname();
                                })
                                .subscribe(value -> {
                                    Log.i(TAG, "widgetContent :: subscribe-" + value);
                                }, throwable -> {
                                    Log.i(NetConstants.TAG_ERROR, "writeContent() :: " + throwable);
                                }, () -> {
                                    Log.i(TAG, "widgetContent :: completed");
                                });
                    }

                }, throwable -> {

                });

    }


    /**
     * Making request to get Section Articles from server
     *
     * @param context
     * @param secId
     * @param page
     * @param type
     * @param lut
     * @return
     */
    public static Disposable getSectionContentFromServer(Context context, RequestCallback<ArrayList<SectionAdapterItem>> requestCallback, String secId, int page, String type, long lut) {
        String defaultContentBaseUrl = DefaultPref.getInstance(context).getDefaultContentBaseUrl();
        final String url = defaultContentBaseUrl + "section-content.php";
        return ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(secId, page, type, lut))
                .timeout(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    final ArrayList<SectionAdapterItem> uiRowItem = new ArrayList<>();
                    if (value.getData().getArticle() != null && value.getData().getArticle().size() > 0) {
                        THPDB thpdb = THPDB.getInstance(context);
                        DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
                        if (page == 1) {
                            daoSectionArticle.deleteSectionAllArticle(secId);
                            Log.i(TAG, "getSectionContentFromServer :: DELETED ALL ARTICLES OF SecId :: " + secId);
                        }
                        TableSectionArticle sectionArticles = new TableSectionArticle(value.getData().getSid(), value.getData().getSname(), page, value.getData().getArticle());
                        daoSectionArticle.insertSectionArticle(sectionArticles);

                        for (ArticleBean bean : value.getData().getArticle()) {
                            final String itemRowId = RowIds.rowId_defaultArticle(bean.getAid());
                            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                            item.setArticleBean(bean);
                            uiRowItem.add(item);
                        }

                        // Insert Related Article
                        DefaultTHApiManager.insertRelatedArticle(value.getData().getArticle());
                    }

                    return uiRowItem;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (requestCallback != null) {
                        requestCallback.onNext(value);
                    }
                    Log.i(TAG, "getSectionContentFromServer :: subscribe");
                },
                        throwable -> {
                    if (requestCallback != null) {
                        requestCallback.onError(throwable, "getSectionContentFromServer");
                    }
                            Log.i(NetConstants.TAG_ERROR, "getSectionContentFromServer() :: " + throwable);
                }, () -> {
                    Log.i(TAG, "getSectionContentFromServer :: completed");
                    if (requestCallback != null) {
                        requestCallback.onComplete("getSectionContentFromServer");
                    }
                });
    }

    /**
     * Making request to get Sub-section articles from server
     *
     * @param context
     * @param secId
     * @param page
     * @param type
     * @param lut
     * @return
     */
    public static Disposable getSubSectionContentFromServer(Context context, RequestCallback<ArrayList<SectionAdapterItem>> requestCallback, String secId, int page, String type, long lut) {
        String defaultContentBaseUrl = DefaultPref.getInstance(context).getDefaultContentBaseUrl();
        final String url = defaultContentBaseUrl + "section-content.php";
        return ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(secId, page, type, lut))
                .timeout(15, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    final ArrayList<SectionAdapterItem> uiRowItem = new ArrayList<>();
                    if (value.getData().getArticle() != null && value.getData().getArticle().size() > 0) {
                        THPDB thpdb = THPDB.getInstance(context);
                        /*DaoSubSectionArticle daoSubSectionArticle = thpdb.daoSubSectionArticle();
                        if (page == 1) {
                            daoSubSectionArticle.deleteSection(secId);
                            Log.i(TAG, "getSubSectionContentFromServer :: DELETED ALL ARTICLES OF SecId :: " + secId);
                        }
                        TableSubSectionArticle subSectionArticles = new TableSubSectionArticle(secId, value.getData().getSname(), page, value.getData().getArticle());
                        daoSubSectionArticle.insertSubSectionArticle(subSectionArticles);*/

                        DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
                        if (page == 1) {
                            daoSectionArticle.deleteSectionAllArticle(secId);
                            Log.i(TAG, "getSubSectionContentFromServer :: DELETED ALL ARTICLES OF SecId :: " + secId);
                        }
                        TableSectionArticle sectionArticles = new TableSectionArticle(value.getData().getSid(), value.getData().getSname(), page, value.getData().getArticle());
                        daoSectionArticle.insertSectionArticle(sectionArticles);

                        for (ArticleBean bean : value.getData().getArticle()) {
                            final String itemRowId = RowIds.rowId_defaultArticle(bean.getAid());
                            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                            item.setArticleBean(bean);
                            uiRowItem.add(item);
                        }

                        // Insert Related Article
                        DefaultTHApiManager.insertRelatedArticle(value.getData().getArticle());
                    }

                    return uiRowItem;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (requestCallback != null) {
                        requestCallback.onNext(value);
                    }
                    Log.i(TAG, "getSubSectionContentFromServer :: subscribe");
                }, throwable -> {
                    if (requestCallback != null) {
                        requestCallback.onError(throwable, "getSubSectionContentFromServer");
                    }
                    Log.i(NetConstants.TAG_ERROR, "getSubSectionContentFromServer() :: " + throwable);
                }, () -> {
                    Log.i(TAG, "getSubSectionContentFromServer :: completed");
                    if (requestCallback != null) {
                        requestCallback.onComplete("getSubSectionContentFromServer");
                    }
                });
    }

    /**
     * Making request to get News Digest articles from server
     *
     * @param context
     * @param secId
     * @param page
     * @return
     */
    public static Disposable getNewsDigestContentFromServer(Context context, RequestCallback<ArrayList<SectionAdapterItem>> requestCallback, String secId, int page) {
        String newsDigestUrl = DefaultPref.getInstance(context).getNewsDigestUrl();
        return ServiceFactory.getServiceAPIs().newsDigest(newsDigestUrl)
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    final ArrayList<SectionAdapterItem> uiRowItem = new ArrayList<>();
                    if (value.getData().getArticle() != null && value.getData().getArticle().size() > 0) {
                        THPDB thpdb = THPDB.getInstance(context);
                        DaoSectionArticle daoSubSectionArticle = thpdb.daoSectionArticle();
                        daoSubSectionArticle.deleteSectionAllArticle(secId);
                        Log.i(TAG, "getNewsDigestContentFromServer :: DELETED ALL ARTICLES OF SecId :: " + secId);
                        TableSectionArticle subSectionArticles = new TableSectionArticle(secId, value.getData().getSname(), page, value.getData().getArticle());
                        daoSubSectionArticle.insertSectionArticle(subSectionArticles);

                        for (ArticleBean bean : value.getData().getArticle()) {
                            final String itemRowId = RowIds.rowId_defaultArticle(bean.getAid());
                            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                            item.setArticleBean(bean);
                            uiRowItem.add(item);
                        }

                        // Insert Related Article
                        DefaultTHApiManager.insertRelatedArticle(value.getData().getArticle());
                    }

                    return uiRowItem;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (requestCallback != null) {
                        requestCallback.onNext(value);
                    }
                    Log.i(TAG, "getNewsDigestContentFromServer :: subscribe");
                }, throwable -> {
                    if (requestCallback != null) {
                        requestCallback.onError(throwable, "getNewsDigestContentFromServer");
                    }
                    Log.i(NetConstants.TAG_ERROR, "getNewsDigestContentFromServer() :: " + throwable);
                }, () -> {
                    Log.i(TAG, "getNewsDigestContentFromServer :: completed");
                    if (requestCallback != null) {
                        requestCallback.onComplete("getNewsDigestContentFromServer");
                    }
                });
    }


    public static void insertRelatedArticle(List<ArticleBean> articleBeans) {
        Observable.just(articleBeans)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(articleBeanList -> {
                    return articleBeanList;
                })
                .subscribeOn(Schedulers.io())
                .map(article->{
                    if(article.getRn() != null && article.getRn().size() > 0) {
                        THPDB thpdb = THPDB.getInstance(SuperApp.getAppContext());
                        DaoRelatedArticle daoRelatedArticle = thpdb.daoRelatedArticle();
                        daoRelatedArticle.deleteRelatedAllArticle(article.getArticleId());
                        TableRelatedArticle relatedArticle = new TableRelatedArticle(article.getArticleId(), article.getRn());
                        daoRelatedArticle.insertSectionArticle(relatedArticle);
                    }
                    return "";
                })
                .subscribe();
    }

    public static Maybe<TableRelatedArticle>  getRelatedArticle(String articleId) {
        THPDB thpdb = THPDB.getInstance(SuperApp.getAppContext());
        DaoRelatedArticle daoRelatedArticle = thpdb.daoRelatedArticle();
        return daoRelatedArticle.getAllArticles(articleId).subscribeOn(Schedulers.io());
    }

    public static void deleteRelatedArticle() {
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(val->{
                    THPDB thpdb = THPDB.getInstance(SuperApp.getAppContext());
                    DaoRelatedArticle daoRelatedArticle = thpdb.daoRelatedArticle();
                    List<TableRelatedArticle> allRelatedArticle = daoRelatedArticle.getAllArticles();


                    return allRelatedArticle;
                })
                .flatMapIterable(allRelatedArticle->{
                    if(allRelatedArticle.size() > 40) {
                        return allRelatedArticle.subList(29, allRelatedArticle.size() - 1);
                    }
                    return new ArrayList<TableRelatedArticle>();
                })
                .map(tableRelatedArticle -> {
                    if(tableRelatedArticle.getArticleId() != null) {
                        THPDB.getInstance(SuperApp.getAppContext()).daoRelatedArticle().deleteRelatedAllArticle(tableRelatedArticle.getArticleId());
                        return true;
                    }
                    return false;
                })
                .subscribe(val->{
                    Log.i("", "");
                }, throwable -> {
                    Log.i("", "");
                });

    }

    public static void readArticleDelete(Context context) {
        if (context == null) {
            return;
        }

        THPDB thpdb = THPDB.getInstance(context);
        DaoRead daoRead = thpdb.daoRead();
        daoRead.getAllReadArticleId()
                .subscribeOn(Schedulers.io())
                .map(ids -> {
                    if (ids.size() > 200) {
                        List<TableRead> subList = ids.subList(199, ids.size() - 1);
                        List<String> subListId = new ArrayList<>();
                        for (TableRead tableRead : subList) {
                            subListId.add(tableRead.getArticleId());
                        }

                        if (context != null) {
                            THPDB thpdbb = THPDB.getInstance(context);
                            DaoRead daoReadd = thpdbb.daoRead();
                            daoReadd.deleteMultiArticleId(subListId);
                        }
                    }

                    return "";
                }).subscribe();
    }

    public static void readArticleId(Context context, final String articleId, String groupType) {
        if (context == null || articleId == null || groupType == null) {
            return;
        }
        Maybe.just(articleId)
                .subscribeOn(Schedulers.io())
                .map(id -> {
                    if (context == null) {
                        return "";
                    }
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoRead daoRead = thpdb.daoRead();
                    TableRead read = daoRead.getReadArticleIdByGroupType(articleId, groupType);
                    if (read == null) {
                        read = new TableRead(id);
                        read.setGroupType(groupType);
                        daoRead.insertReadArticle(read);
                        DefaultTHApiManager.getCommentCount(articleId, context);
                    } else {
                        long lut = read.getLutOfCommentCount();
                        long interval = 1000 * 60 * 10;
                        if (lut + interval < System.currentTimeMillis()) {
                            DefaultTHApiManager.getCommentCount(articleId, context);
                        }
                    }
                    return "";
                }).subscribe();

    }

    public static void readArticleId(Context context, final String articleId) {
        if (context == null || articleId == null) {
            return;
        }
        Maybe.just(articleId)
                .subscribeOn(Schedulers.io())
                .map(id -> {
                    if (context == null) {
                        return "";
                    }
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoRead daoRead = thpdb.daoRead();
                    TableRead read = daoRead.getReadArticleId(articleId);
                    if(read == null) {
                        read = new TableRead(id);
                        daoRead.insertReadArticle(read);
                        DefaultTHApiManager.getCommentCount(articleId, context);
                    }
                    else {
                        long lut = read.getLutOfCommentCount();
                        long interval = 1000*60*10;
                        if(lut+interval < System.currentTimeMillis()) {
                            DefaultTHApiManager.getCommentCount(articleId, context);
                        }
                    }
                    return "";
                }).subscribe();

    }

    public static Maybe<TableRead> isReadArticleId(Context context, final String articleId) {
        if (context == null) {
            return null;
        }
        return Maybe.just(articleId)
                .subscribeOn(Schedulers.io())
                .map(id -> {
                    if (context == null) {
                        return null;
                    }
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoRead daoRead = thpdb.daoRead();
                    TableRead read = daoRead.getReadArticleId(id);
                    return read;
                })
                .observeOn(AndroidSchedulers.mainThread());

    }



    public static Observable<ArticleBean> isExistInDGnArticle(Context context, final String aid) {
        return Observable.just(aid)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, ArticleBean>() {
                    @Override
                    public ArticleBean apply(String aid) {
                        ArticleBean requiredBean = new ArticleBean();
                        requiredBean.setArticleId(aid);

                        THPDB thp = THPDB.getInstance(context);

                        // Check in Sections
                        DaoSectionArticle daoSectionArticle = thp.daoSectionArticle();
                        List<TableSectionArticle> getAllArticles = daoSectionArticle.getAllArticles();
                        List<ArticleBean> allArticle = new ArrayList<>();

                        for(TableSectionArticle sectionArticle : getAllArticles) {
                            allArticle.addAll(sectionArticle.getBeans());
                        }
                        int index = allArticle.indexOf(requiredBean);

                        if (index != -1) {
                            return   allArticle.get(index);
                        }
                        return new ArticleBean();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<ArticleBean> isExistInTempArticleArticle(Context context, final String aid) {
        return Observable.just(aid)
                .subscribeOn(Schedulers.io())
                .map(articleId->{
                    ArticleBean requiredBean = new ArticleBean();
                    requiredBean.setArticleId(aid);
                    // Check in TableTemperoryArticle
                    TableTemperoryArticle temperoryArticle = THPDB.getInstance(context).daoTemperoryArticle().getSingleTemperoryBean(aid);
                    if(temperoryArticle != null) {
                        return temperoryArticle.getBean();
                    }
                    return new ArticleBean();
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<List<ArticleBean>> getNotificationArticles(Context context) {
        // Check in TableTemperoryArticle
        return THPDB.getInstance(context).daoTemperoryArticle().getAllNotification(NetConstants.G_NOTIFICATION)
                .subscribeOn(Schedulers.io())
                .map(temperoryTablesArticles -> {
                    List<ArticleBean> allArticle = new ArrayList<>();
                    for (TableTemperoryArticle tempArticle : temperoryTablesArticles) {
                        allArticle.add(tempArticle.getBean());
                    }
                    return allArticle;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<Integer> getUnreadNotificationArticlesCount(Context context) {
        // Check in Table TemporaryArticle
        return THPDB.getInstance(context).daoTemperoryArticle().getAllNotification(NetConstants.G_NOTIFICATION)
                .subscribeOn(Schedulers.io())
                .map(temperoryTablesArticles -> {
                    int totalCounts = temperoryTablesArticles.size();
                    int readCounts = THPDB.getInstance(context).daoRead().getAllReadArticlesCount(NetConstants.G_NOTIFICATION);
                    int leftReadCounts = totalCounts - readCounts;
                    return leftReadCounts;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Single<Boolean> deleteAllNotification(Context context) {
        return Single.just(NetConstants.G_NOTIFICATION)
                .subscribeOn(Schedulers.io())
                .map(val->{
                    THPDB thpdb = THPDB.getInstance(context);
                    int count = thpdb.daoTemperoryArticle().deleteAllNotification(val);
                    int c = thpdb.daoRead().deleteReadArticleByGroupType(val);
                    return count > 0;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static final Observable<ArticleBean> articleDetailFromServer(Context context, String aid, String url, String entryFrom) {
        url = url + aid;
        Observable<SearchedArticleModel> observable = ServiceFactory.getServiceAPIs().searchArticleByIDFromServer(url);
        return observable.subscribeOn(Schedulers.io())
                .map(model->{
                    THPDB thp = THPDB.getInstance(context);
                    if (model.getData().size() > 0) {
                        ArticleBean bean = model.getData().get(0);
                        bean.setGroupType(entryFrom);
                        TableTemperoryArticle temperoryArticle = new TableTemperoryArticle(aid, bean, entryFrom);
                        thp.daoTemperoryArticle().insertTemperoryArticle(temperoryArticle);
                        return model.getData().get(0);
                    }
                    return new ArticleBean();
                });
    }

    public static final Observable<List<ArticleBean>> articleFromServer(String aid, String url) {
        url = url + aid;
        Observable<SearchedArticleModel> observable = ServiceFactory.getServiceAPIs().searchArticleByIDFromServer(url);
        return observable.subscribeOn(Schedulers.newThread())
                .map(searchedArticleModel -> {
                    if (searchedArticleModel.getData().size() > 0) {
                        return searchedArticleModel.getData();
                    }
                    return new ArrayList<ArticleBean>();
                });

    }



    public static Disposable mpConfigurationAPI(Context context, String urlConfigAPI) {
        Observable<MPConfigurationModel> observable = ServiceFactory.getServiceAPIs().mpConfigurationAPI(urlConfigAPI);
        return observable
                .subscribeOn(Schedulers.newThread())
                .map(configurationModel -> {
                    THPDB db = THPDB.getInstance(context);
                    DaoMP mpTableDao = db.daoMp();
                    TableMP mpTable = mpTableDao.getMPTable();
                    if (mpTable == null) {
                        mpTable = new TableMP();
                    }
                    boolean isMpFeatureEnabled = configurationModel.isSTATUS();
                    mpTable.setMpFeatureEnabled(isMpFeatureEnabled);
                    //Update MP preferences
                    DefaultPref.getInstance(context).setMeteredPaywallEnabled(isMpFeatureEnabled);
                    if (isMpFeatureEnabled) {
                        boolean isTaboolaNeeded = configurationModel.getDATA().getConfigs().isIsTaboolaNeeded();
                        boolean isMpBannerNeeded = configurationModel.getDATA().getConfigs().isIsMpBannerNeeded();

                        String mpBannerMsg = configurationModel.getDATA().getConfigs().getMpBannerMsg();

                        String fullAccessBtnName = configurationModel.getDATA().getConfigs().getFullAccessBtnName();
                        boolean showFullAccessBtn = configurationModel.getDATA().getConfigs().isShowFullAccessBtn();

                        boolean showSignInBtn = configurationModel.getDATA().getConfigs().isShowSignInBtn();
                        String signInBtnName = configurationModel.getDATA().getConfigs().getSignInBtnName();
                        String signInBtnNameBoldWord = configurationModel.getDATA().getConfigs().getSignInBtnNameBoldWord();

                        boolean showSignUpBtn = configurationModel.getDATA().getConfigs().isShowSignUpBtn();
                        String signUpBtnName = configurationModel.getDATA().getConfigs().getSignUpBtnName();
                        String signUpBtnNameBoldWord = configurationModel.getDATA().getConfigs().getSignUpBtnNameBoldWord();

                        String nonSignInBlockerTitle = configurationModel.getDATA().getConfigs().getNonSignInBlockerTitle();
                        String nonSignInBlockerDescription = configurationModel.getDATA().getConfigs().getNonSignInBlockerDescription();

                        String expiredUserBlockerTitle = configurationModel.getDATA().getConfigs().getExpiredUserBlockerTitle();
                        String expiredUserBlockerDescription = configurationModel.getDATA().getConfigs().getExpiredUserBlockerDescription();

                        mpTable.setTaboolaNeeded(isTaboolaNeeded);
                        mpTable.setMpBannerNeeded(isMpBannerNeeded);

                        mpTable.setMpBannerMsg(mpBannerMsg);

                        mpTable.setFullAccessBtnName(fullAccessBtnName);
                        mpTable.setShowFullAccessBtn(showFullAccessBtn);

                        mpTable.setShowSignInBtn(showSignInBtn);
                        mpTable.setSignInBtnName(signInBtnName);
                        mpTable.setSignInBtnNameBoldWord(signInBtnNameBoldWord);

                        mpTable.setShowSignUpBtn(showSignUpBtn);
                        mpTable.setSignUpBtnName(signUpBtnName);
                        mpTable.setSignUpBtnNameBoldWord(signUpBtnNameBoldWord);

                        mpTable.setNonSignInBlockerTitle(nonSignInBlockerTitle);
                        mpTable.setNonSignInBlockerDescription(nonSignInBlockerDescription);
                        mpTable.setExpiredUserBlockerTitle(expiredUserBlockerTitle);
                        mpTable.setExpiredUserBlockerDescription(expiredUserBlockerDescription);
                    }
                    if (mpTable.getId() > 0) {
                        mpTableDao.updateMPTable(mpTable);
                    } else {
                        mpTableDao.insertMpTableData(mpTable);
                    }
                    Log.i("ApiManager", "MP Cyle END "+System.currentTimeMillis());
                    return "";
                }).subscribe(val -> {

        }, throwable -> {
            Log.i("", "");
        });
    }

    public static Disposable mpCycleDurationAPI(Context context, String urlCycleAPI, String urlConfigAPI, RequestCallback requestCallback) {
        Observable<MPCycleDurationModel> observable = ServiceFactory.getServiceAPIs().mpCycleDurationAPI(urlCycleAPI);
        return observable
                .timeout(30, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(cycleDurationModel -> {
                    Log.i("ApiManager", "MP Cyle START " + System.currentTimeMillis());
                    THPDB db = THPDB.getInstance(context);
                    DaoMP mpTableDao = db.daoMp();
                    boolean isMpFeatureEnabled = cycleDurationModel.isSTATUS();
                    TableMP table = new TableMP();
                    table.setMpFeatureEnabled(isMpFeatureEnabled);
                    DefaultPref.getInstance(context).setMeteredPaywallEnabled(isMpFeatureEnabled);
                    if (isMpFeatureEnabled) {
                        String cycleName = cycleDurationModel.getDATA().getCycleName();
                        int numOfAllowedArticles = cycleDurationModel.getDATA().getNumOfAllowedArticles();
                        long totalAllowedTimeInSec = cycleDurationModel.getDATA().getExpiryInSeconds();
                        long mpServerTimeInMillis = cycleDurationModel.getDATA().getGmtInMillis();
                        String uniqueId = cycleDurationModel.getDATA().getUniqueId();
                        table.setAllowedArticleCounts(numOfAllowedArticles);
                        table.setCycleName(cycleName);
                        table.setAllowedTimeInSecs(totalAllowedTimeInSec);
                        long allowedTimeInMillis = TimeUnit.SECONDS.toMillis(totalAllowedTimeInSec);
                        table.setExpiryTimeInMillis(allowedTimeInMillis);
                        //Save in Preferences
                        DefaultPref.getInstance(context).setMPExpiryTimeInMillis(allowedTimeInMillis);
                        DefaultPref.getInstance(context).setMPAllowedArticlesCount(numOfAllowedArticles);
                        table.setCycleUniqueId(uniqueId);
                        table.setNetworkCurrentTimeInMilli(mpServerTimeInMillis);
                    }
                    //Insert new record into Table in this case, when any Cycle name is found
                    mpTableDao.deleteAll();
                    //Delete startTime
                    //DefaultPref.getInstance(context).setMPStartTimeInMillis(0);
                    //Delete TableMPReadArticle MP
                    if (db.daoMPReadArticle() != null) {
                        db.daoMPReadArticle().DELETE();
                    }
                    mpTableDao.insertMpTableData(table);

                    // It calls configuration api, whenever cycle api is called.
                    mpConfigurationAPI(context, urlConfigAPI);

                    return "";
                })
                .subscribe(val -> {
                    if(requestCallback != null) {
                        requestCallback.onNext("mpCycleDurationAPI");
                    }
                }, throwable -> {
                    Log.i("ApiManager", throwable.getMessage());
                    if(requestCallback != null) {
                        requestCallback.onError(throwable, "mpCycleDurationAPI");
                    }
                });
    }


    public static Observable isSectionOrSubsection(Context context, String sectionId) {
        return Observable.just(sectionId)
                .subscribeOn(Schedulers.io())
                .map(secId->{
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoSection daoSection = thpdb.daoSection();
                    SectionBean subSectionBean = new SectionBean();
                    subSectionBean.setSecId(secId);

                    TableSection tableSectionNew = null;


                    List<TableSection> sectionList = daoSection.getSections();

                    for(TableSection tableSection : sectionList) {
                        if(sectionId.equals(tableSection.getSecId())) {
                            tableSectionNew = tableSection;
                            return tableSectionNew;
                        }
                        List<SectionBean> subSectionList = tableSection.getSubSections();
                        int mSelectedPagerIndex = subSectionList.indexOf(subSectionBean);
                        if(mSelectedPagerIndex != -1) {
                            // Setting Group Section name, It will be used to show in Toolbar title
                            SectionBean subSection = subSectionList.get(mSelectedPagerIndex);
                            subSection.setParentSecName(tableSection.getSecName());

                            subSection.setParentSecId(tableSection.getSecId());
                            return subSection;
                        }
                    }
                    if(tableSectionNew == null) {
                        tableSectionNew = new TableSection();
                    }
                    return tableSectionNew;

                });
    }




    public static void getCommentCount(final String articleId, Context context) {
        String url = "https://api.vuukle.com/api/v1/Comments/getCommentCountListByHost?host=thehindu.com&articleIds="+articleId;
        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().getCommentCount(url);
        observable.timeout(5, TimeUnit.SECONDS);
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<JsonElement, JsonElement>() {
                    @Override
                    public JsonElement apply(JsonElement jsonElement) {
                        return jsonElement;
                    }
                })
                .map(new Function<JsonElement, String>() {
                    @Override
                    public String apply(JsonElement jsonElement) {
                        JsonObject object = jsonElement.getAsJsonObject();
                        boolean isSuccess = object.get("success").getAsBoolean();
                        if(isSuccess) {
                            JsonObject data = object.get("data").getAsJsonObject();
                            int commentCount = data.get("" + articleId).getAsInt();
                            String countStr = ""+commentCount;
                            THPDB thpdb = THPDB.getInstance(context);
                            DaoRead daoRead = thpdb.daoRead();
                            if(daoRead != null) {
                                daoRead.updateReadTable(articleId, countStr, System.currentTimeMillis());
                            }
                            return countStr;
                        }
                        return "0";
                    }
                })
                .subscribe(val->{

                }, throwable -> {

                });
    }


    /**
     * It is App Configuration, gets configuration data from server
     * @param context
     * @param requestCallback
     */
    public static Disposable appConfigurationFromServer(Context context, RequestCallback<TableConfiguration> requestCallback) {
        return ServiceFactory.getServiceAPIs().config(BuildConfig.CONFIG_URL, BuildConfig.CONFIG_AUTH_KEY, BuildConfig.CONFIG_PRODUCTION_ID, ResUtil.resolution(context))
        .subscribeOn(Schedulers.io())
                .map(config->{
                    if(config.isSTATUS()) {
                        THPDB thpdb = THPDB.getInstance(context);
                        DaoConfiguration daoConfiguration = thpdb.daoConfiguration();
                        daoConfiguration.deleteAll();
                        daoConfiguration.insertConfiguration(config.getDATA());
                    }

                    return config.getDATA();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    if(requestCallback != null) {
                        requestCallback.onNext(value);
                    }
                }, throwable -> {
                    if(requestCallback != null) {
                        requestCallback.onError(throwable, "appConfigurationFromServer");
                    }
                }, ()->{
                    if(requestCallback != null) {
                        requestCallback.onComplete("appConfigurationFromServer");
                    }
                });

    }

    public static Observable<Boolean> isConfigurationUpdateAvailable(Context context) {
        return ServiceFactory.getServiceAPIs().configUpdateCheck(BuildConfig.CONFIG_UPDATE_CHECK_URL, BuildConfig.CONFIG_AUTH_KEY, BuildConfig.CONFIG_PRODUCTION_ID)
                .subscribeOn(Schedulers.io())
                .timeout(30, TimeUnit.SECONDS)
                .map(jsonElement -> {
                    if (((JsonObject) jsonElement).has("STATUS_MSG")) {
                        String STATUS_MSG = ((JsonObject) jsonElement).get("STATUS_MSG").getAsString();
                        if(STATUS_MSG.equalsIgnoreCase("true")) {
                            JsonObject jsonObject = ((JsonObject) jsonElement).get("DATA").getAsJsonObject();
                            if(jsonObject.has("lastUpdatedTime")) {
                                String lastUpdatedTime = jsonObject.get("lastUpdatedTime").getAsString();
                                THPDB thpdb = THPDB.getInstance(context);
                                DaoConfiguration daoConfiguration = thpdb.daoConfiguration();
                                TableConfiguration tableConfiguration = daoConfiguration.getConfiguration();
                                if(tableConfiguration == null) {
                                    return true;
                                }
                                String dbLut = tableConfiguration.getLastServerUpdateTime();
                                if (lastUpdatedTime.equalsIgnoreCase(dbLut)) {
                                    return false;
                                }
                            }
                        }
                    }

                    /*if (((JsonObject) jsonElement).has("lastUpdatedTime")) {
                        String lastUpdatedTime = ((JsonObject) jsonElement).get("lastUpdatedTime").getAsString();
                        THPDB thpdb = THPDB.getInstance(context);
                        DaoConfiguration daoConfiguration = thpdb.daoConfiguration();
                        TableConfiguration tableConfiguration = daoConfiguration.getConfiguration();
                        if(tableConfiguration == null) {
                            return true;
                        }
                        String dbLut = tableConfiguration.getLastServerUpdateTime();
                        if (lastUpdatedTime.equalsIgnoreCase(dbLut)) {
                            return false;
                        }
                    }*/
                    return true;
                });

    }

    public static Single<List<TabsBean>> appConfigurationTabs(Context context, boolean isDayTheme) {
        return THPDB.getInstance(context).daoConfiguration().getConfigurationSingle()
                .subscribeOn(Schedulers.io())
                .map(tableConfiguration -> {
                    List<TabsBean> tabsBeans = tableConfiguration.getTabs();
                    int limit;
                    if(tabsBeans.size() > 5) {
                        limit = 5;
                    } else {
                        limit = tabsBeans.size();
                    }

                    String destinationFolderPath;
                    if (isDayTheme) {
                        destinationFolderPath = FileUtils.destinationFolder(context, FileUtils.TAB_ICONs_LIGHT).getPath();
                    } else {
                        destinationFolderPath = FileUtils.destinationFolder(context, FileUtils.TAB_ICONs_DARK).getPath();
                    }

                    List<TabsBean> tabsBeanList = tabsBeans.stream().limit(limit).collect(Collectors.toList());
                    for(TabsBean tab : tabsBeanList) {
                        if (isDayTheme) {
                            tab.getIconUrl().setLocalFilePath(FileUtils.getFilePathFromUrl(destinationFolderPath, tab.getIconUrl().getUrlLight()));
                            tab.getIconUrl().setLocalFileSelectedPath(FileUtils.getFilePathFromUrl(destinationFolderPath, tab.getIconUrl().getUrlSelectedLight()));
                        } else {
                            tab.getIconUrl().setLocalFilePath(FileUtils.getFilePathFromUrl(destinationFolderPath, tab.getIconUrl().getUrlDark()));
                            tab.getIconUrl().setLocalFileSelectedPath(FileUtils.getFilePathFromUrl(destinationFolderPath, tab.getIconUrl().getUrlSelectedDark()));
                        }
                    }
                    return tabsBeanList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                ;

    }

    public static Single<TableConfiguration> appConfiguration(Context context) {
        return THPDB.getInstance(context).daoConfiguration()
                .getConfigurationSingle()
                .subscribeOn(Schedulers.io());
    }

    public static Flowable<HashMap> meteredPaywallReadArticleCount(Context context) {
        THPDB thpdb = THPDB.getInstance(context);
        DaoMP daoMP = thpdb.daoMp();
        return daoMP.getArticleIdsFlowable()
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    Log.i("", "");
                    HashMap<String, Object> valueMap = new HashMap<>();
                    String mpBannerMsg = daoMP.getMpBannerMsg();
                    int allowedArticleCounts = daoMP.getAllowedArticleCounts();
                    long allowedTimeInSecs = daoMP.getAllowedArticleTimesInSecs();
                    String cycleName = daoMP.getCycleName();
                    int totalReadSize = value.size();
                    boolean isAllowedToRead = totalReadSize <= allowedArticleCounts;
                    //Calculate Time Difference, check if exceeds
                    long startTimeInMillis = DefaultPref.getInstance(context).getMPStartTimeInMillis();
                    if (startTimeInMillis > 0 && DefaultPref.getInstance(context).isMPDurationExpired()) {
                        isAllowedToRead = false;
                    }
                    String durationUnit = AppDateUtil.calculateDurationAndUnit(allowedTimeInSecs);
                    if (mpBannerMsg != null) {
                        String bannerMsg = mpBannerMsg.replaceAll("<readCount>", "" + totalReadSize);
                        bannerMsg = bannerMsg.replaceAll("<totalCount>", "" + allowedArticleCounts);
                        bannerMsg = bannerMsg + (TextUtils.isEmpty(durationUnit) ? "" : " for " + durationUnit);
                        valueMap.put("bannerMsg", bannerMsg);
                    } else {
                        //You have read 0 articles out of 0 free articles for 0 days/months
                        String bannerMsg = "You have read "+totalReadSize+" articles out of "+allowedArticleCounts+" free articles";
                        bannerMsg = bannerMsg + (TextUtils.isEmpty(durationUnit) ? "" : " for " + durationUnit);
                        valueMap.put("bannerMsg", bannerMsg);
                    }
                    valueMap.put("isAllowedToRead", "" + isAllowedToRead);
                    valueMap.put("articleCount", totalReadSize);
                    valueMap.put("cycleName", "" + cycleName);
                    valueMap.put("allowedArticleCounts", allowedArticleCounts);
                    return valueMap;
                });

    }

    /**
     * Returns boolean to show/hide Content Blocker
     * @param context
     * @param readArticleId
     * @return
     */
    public static Observable meteredPaywallInsertReadArticleId(Context context, String readArticleId) {
        return Observable.just("readCount")
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoMP daoMP = thpdb.daoMp();
                    TableMP tableMP = daoMP.getMPTable();
                    int allowedArticleCounts = tableMP.getAllowedArticleCounts();
                    //Get readArticles Set size, if it's 0, then save currentTimeInMillis as startTimeInMillis
                    int size = tableMP.getReadArticleIds().size();
                    if (size == 0) {
                        long currentTimeInMillis = System.currentTimeMillis();
                        tableMP.setStartTimeInMillis(currentTimeInMillis);
                        //Save startTime in DefaultPref
                        DefaultPref.getInstance(context).setMPStartTimeInMillis(currentTimeInMillis);
                    }
                    tableMP.addReadArticleId(readArticleId);
                    //Calculate Time Difference
                    long startTimeInMillis = DefaultPref.getInstance(context).getMPStartTimeInMillis();
                    if (startTimeInMillis > 0 && DefaultPref.getInstance(context).isMPDurationExpired()) {
                        Set readArticleIds = daoMP.getArticleIds();
                        return !readArticleIds.contains(readArticleId);
                    }
                    if (tableMP.getReadArticleIds().size() < allowedArticleCounts) {
                        daoMP.updateMPTable(tableMP);
                        return false;
                    } else if (tableMP.getReadArticleIds().size() == allowedArticleCounts) {
                        daoMP.updateMPTable(tableMP);
                        return false;
                    } else {
                        return true;
                    }
                });

    }





    public static Observable clearMPUserRecords(Context context) {
        return Observable.just("Clear")
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    THPDB thpdb = THPDB.getInstance(context);
                    //Delete startTime
                    //DefaultPref.getInstance(context).setMPStartTimeInMillis(0);
                    //Delete TableMPReadArticle MP
                    if (thpdb.daoMPReadArticle() != null) {
                        thpdb.daoMPReadArticle().DELETE();
                    }
                    return true;
                });

    }

    public static Observable<TableMP> getMPTableObject(Context context) {
        return Observable.just("mpTable")
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoMP daoMP = thpdb.daoMp();
                    return daoMP.getMPTable();
                });

    }

    public static void insertMeteredPaywallArticleId(Context context, final String articleId, final boolean isRestricted, final int allowedCount) {
        if (context == null || articleId == null) {
            return;
        }
        Maybe.just(articleId)
                .subscribeOn(Schedulers.io())
                .map(id -> {
                    if (context == null || allowedCount == -1) {
                        return "";
                    }
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoMPReadArticle daoRead = thpdb.daoMPReadArticle();
                    String existingId = daoRead.getMPReadArticleId(id);
                    List<String> allRestrictedArticleIds = daoRead.getAllRestrictedArticleIds();
                    if(existingId == null) {
                        boolean isUserCanReRead = false; //allRestrictedArticleIds.size() >= allowedCount
                        if(allRestrictedArticleIds.size() < allowedCount) {
                            isUserCanReRead = true;
                        }
                        int size = allRestrictedArticleIds.size();
                        if (size == 0) {
                            long currentTimeInMillis = System.currentTimeMillis();
                            //Save startTime in DefaultPref
                            DefaultPref.getInstance(context).setMPStartTimeInMillis(currentTimeInMillis);
                        }
                        TableMPReadArticle mpReadArticle = new TableMPReadArticle(id, isRestricted, isUserCanReRead);
                        daoRead.insertReadArticle(mpReadArticle);
                    }
                    else {
                        daoRead.updateMpReadArticle(id, isRestricted);
                    }
                    return "";
                }).subscribe();

    }

    public static void insertCloseBannerClick(Context context, final String articleId, final boolean isClosed) {
        if (context == null || articleId == null) {
            return;
        }
        Observable.just(articleId)
                .subscribeOn(Schedulers.io())
                .map(id -> {
                    THPDB thpdb = THPDB.getInstance(context);
                    DaoMPReadArticle daoRead = thpdb.daoMPReadArticle();
                    String existingId = daoRead.getMPReadArticleId(id);
                    if(existingId != null) {
                        daoRead.updateIsBannerCloseForArticle(id, isClosed);
                    }
                    return "";
                }).subscribe();

    }

    public static Observable<UpdateModel> forceUpdate() {
        String url = BuildConfig.FORCE_UPDATE_URL;
        if (!BuildConfig.IS_PRODUCTION) {
            url = BuildConfig.FORCE_UPDATE_URL;
        }
        return ServiceFactory.getServiceAPIs()
                .forceUpdate(url, BuildConfig.CONFIG_AUTH_KEY, BuildConfig.CONFIG_PRODUCTION_ID)
                .timeout(10, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .map(responseModel -> responseModel.getDATA().getAndroid())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void bl_sensexWidget(RequestCallback requestCallback) {
        String [] urls = {BLConstants.NSE_URL, BLConstants.BSE_URL};
        final Observable[] observables = new Observable[urls.length];
        for(int i=0; i<urls.length; i++) {
            observables[i] = ServiceFactory.getServiceAPIs().bl_sensexWidget(urls[i]).subscribeOn(Schedulers.io());
        }

        Observable.mergeArray(observables)
                .map(value -> {
                    if(value instanceof NSE_BSE_Data) {
                        NSE_BSE_Data s = (NSE_BSE_Data) value;
                        String scn = s.getSnapshot().getScn();
                        if(scn.equalsIgnoreCase("Nifty 50")) {
                            NSEData nseData = new NSEData();
                            nseData.setStatus(SensexStatus.SUCCESS);
                            nseData.setCh(s.getCh());
                            nseData.setCp(s.getCp());
                            nseData.setPer(s.getPer());
                            nseData.setDa(s.getDa());
                            nseData.setSnapShot(s.getSnapshot());
                            return nseData;
                        } else if(scn.equalsIgnoreCase("BSE Sensex")) {
                            BSEData bseData = new BSEData();
                            bseData.setStatus(SensexStatus.SUCCESS);
                            bseData.setCh(s.getCh());
                            bseData.setCp(s.getCp());
                            bseData.setPer(s.getPer());
                            bseData.setDa(s.getDa());
                            bseData.setSnapShot(s.getSnapshot());
                            return bseData;
                        }
                    }
                    return "";
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valu->{
                    if(requestCallback != null) {
                        requestCallback.onNext(valu);
                    }
                    Log.i("", "");
                }, throwable -> {
                    Log.i("", "");
                });
    }

    public static void mergeOldBookmark() {
        if(DefaultPref.getInstance(SuperApp.getAppContext()).isOldBookmarkLoaded()) {
            return;
        }
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(val->{
                    THPDB thp = THPDB.getInstance(SuperApp.getAppContext());
                    Realm mRealm = new RealmSupport().getRealmInstance();
                    OrderedRealmCollection<BookmarkBean> oldBookmarkArticlesList = mRealm.where(BookmarkBean.class).findAllSorted("bookmarkDate", Sort.DESCENDING);
                    for(BookmarkBean articleBean : oldBookmarkArticlesList) {
                        ArticleBean bean = new ArticleBean();
                        bean.setArticleId(""+articleBean.getAid());
                        bean.setIm_thumbnail_v2(articleBean.getIm_thumbnail_v2());
                        bean.setAid(""+articleBean.getAid());
                        bean.setArticleSection(articleBean.getSname());
                        bean.setArticletitle(articleBean.getTi());
                        bean.setTi(articleBean.getTi());
                        bean.setArticletype(articleBean.getArticleType());
                        bean.setAu(articleBean.getAu());
                        bean.setPubDate(articleBean.getPd());
                        bean.setPubDateTime(articleBean.getPd());
                        bean.setPd(articleBean.getPd());
                        bean.setOd(articleBean.getOd());
                        bean.setGmt(articleBean.getGmt());
                        bean.setIsBookmark(1);
                        bean.setIsFavourite(1);
                        bean.setDescription(articleBean.getDe());
                        bean.setShortDescription(articleBean.getLe());
                        bean.setHasDescription(1);
                        //bean.setMedia(articleBean.getMe());
                        bean.setGroupType(NetConstants.G_BOOKMARK_DEFAULT);
                        bean.setSid(articleBean.getSid());
                        bean.setSectionName(articleBean.getSname());
                        bean.setYoutube_video_id(articleBean.getVid());
                        bean.setYoutubeVideoId(articleBean.getVid());
                        bean.setLe(articleBean.getLe());
                        bean.setLeadText(articleBean.getLe());
                        bean.setAdd_pos(articleBean.getAdd_pos());
                        TableBookmark tableBookmark = new TableBookmark(bean.getArticleId(), bean, bean.getGroupType());
                        thp.bookmarkTableDao().insertBookmark(tableBookmark);
                    }
                    //DefaultPref.getInstance(SuperApp.getAppContext()).setOldBookmarkLoaded(true);
                    return "";
                })
                .subscribe(val->{
                    Log.i("", "");
                }, throwable -> {
                    Log.i("", "");
                });
    }

    public static Single<List<TableOptional.OptionsBean>> getOptionsListApi(Context context) {
        return ServiceFactory.getServiceAPIs().getMenuSequence(BuildConfig.PRODUCTION_MENU_API, BuildConfig.CONFIG_AUTH_KEY, BuildConfig.CONFIG_PRODUCTION_ID)
                .subscribeOn(Schedulers.io())
                .map(jsonElement -> {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    Gson gson = new Gson();
                    TableOptional tableOptional = gson.fromJson(jsonObject.get("DATA"), TableOptional.class);
                    THPDB thpdb = THPDB.getInstance(context);
                    thpdb.daoTableOptional().insertTableOptional(tableOptional);
                    return tableOptional.getOptions();
                });
    }

    public static Single<List<TableOptional.OptionsBean>> getOptionsListDB(Context context) {
        return THPDB.getInstance(context).daoTableOptional().getItemTableOptional()
                .subscribeOn(Schedulers.io())
                .map(tableOptional -> {
                    if (tableOptional == null) {
                        return null;
                    } else {
                        return tableOptional.getOptions();
                    }
                });
    }

    public static void deleteTableOptions(Context context) {
        Single.just("Delete").subscribeOn(Schedulers.io())
        .map(val->{
            THPDB.getInstance(context).daoTableOptional().deleteTableOptional();
            return "";
        }).subscribe();
    }


    /**
     * It is App Configuration, gets configuration data from server
     * @param context
     * @param requestCallback
     */
    public static void getUPS(Context context, RequestCallback<List<String>> requestCallback) {
         ServiceFactory.getServiceAPIs().getUSP(BuildConfig.UPS_URL, BuildConfig.CONFIG_AUTH_KEY, BuildConfig.CONFIG_PRODUCTION_ID, ResUtil.resolution(context))
                .subscribeOn(Schedulers.io())
                .map(uspData->{
                    boolean isDayTheme = DefaultPref.getInstance(context).isUserThemeDay();
                    if(isDayTheme) {
                        if(uspData.getDATA()!=null) {
                            return uspData.getDATA().getLight().getUrls();
                        }
                    }
                    else {
                        if(uspData.getDATA()!=null) {
                            return uspData.getDATA().getDark().getUrls();
                        }
                    }
                    return new ArrayList<String>();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    if(requestCallback != null) {
                        requestCallback.onNext(value);
                    }
                }, throwable -> {
                    if(requestCallback != null) {
                        requestCallback.onError(throwable, "getUPS");
                    }
                }, ()->{
                    if(requestCallback != null) {
                        requestCallback.onComplete("getUPS");
                    }
                });

    }

    public static void getGuideOverlay(Context context, RequestCallback<USPData.DATABean.GuideOverlay> requestCallback) {
        ServiceFactory.getServiceAPIs().getUSP(BuildConfig.GUIDE_OVERLAY_URL, BuildConfig.CONFIG_AUTH_KEY, BuildConfig.CONFIG_PRODUCTION_ID, ResUtil.resolution(context))
                .subscribeOn(Schedulers.io())
                .map(uspData->{
                    USPData.DATABean.GuideOverlay guideOverlay = uspData.getDATA().getAndroid();
                    return guideOverlay;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    if(requestCallback != null) {
                        requestCallback.onNext(value);
                    }
                }, throwable -> {
                    if(requestCallback != null) {
                        requestCallback.onError(throwable, "getGuideOverlay");
                    }
                }, ()->{
                    if(requestCallback != null) {
                        requestCallback.onComplete("getGuideOverlay");
                    }
                });

    }

}
