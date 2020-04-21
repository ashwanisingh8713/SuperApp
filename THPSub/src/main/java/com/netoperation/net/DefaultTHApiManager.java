package com.netoperation.net;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoHomeArticle;
import com.netoperation.default_db.DaoPersonaliseDefault;
import com.netoperation.default_db.DaoRead;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoSectionArticle;
import com.netoperation.default_db.DaoSubSectionArticle;
import com.netoperation.default_db.DaoTempWork;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TablePersonaliseDefault;
import com.netoperation.default_db.TableRead;
import com.netoperation.default_db.TableSection;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.default_db.TableSubSectionArticle;
import com.netoperation.default_db.TableTempWork;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.BannerBean;
import com.netoperation.model.HomeData;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.SectionAndWidget;
import com.netoperation.model.SectionBean;
import com.netoperation.model.SectionContentFromServer;
import com.netoperation.model.THDefaultPersonalizeBean;
import com.netoperation.model.WidgetBean;
import com.netoperation.retrofit.ReqBody;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.BuildConfig;
import com.ns.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DefaultTHApiManager {

    private static String TAG = NetConstants.TAG_UNIQUE;


    /**
     * Private Constructure
     */
    private DefaultTHApiManager() {

    }

    /**
     * Inserts SectionAndWedget object response into respective table.
     * @param context
     * @param sectionAndWidget
     * @return
     */
    private static SectionAndWidget insertSectionResponseInDB(Context context, SectionAndWidget sectionAndWidget, boolean isFromTempTable) {
        THPDB db = THPDB.getInstance(context);
        DaoSection daoSection = db.daoSection();
        DaoBanner daoBanner = db.daoBanner();
        DaoWidget daoWidget = db.daoWidget();

        SectionAndWidget.DataBean dataBean = sectionAndWidget.getData();
        long date = dataBean.getDate();

        if (daoBanner != null) {
            TableBanner tableBanner = daoBanner.getBanners();

            if (tableBanner != null && !ResUtil.isEmpty(tableBanner.getLastUpdatedTime()) && tableBanner.getLastUpdatedTime().equals("" + date)) {
                return sectionAndWidget;
            }
            else {
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
        String url = BuildConfig.DEFAULT_TH_BASE_URL + "sectionList_v4.php";
        Observable<SectionAndWidget> observable = ServiceFactory.getServiceAPIs().sectionList(url, ReqBody.sectionList());
        return observable.subscribeOn(Schedulers.newThread())
                .timeout(15, TimeUnit.SECONDS)
                .map(sectionAndWidget -> {
                            return insertSectionResponseInDB(context, sectionAndWidget, false);
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
                    return insertSectionResponseInDB(context, sectionAndWidget, true);
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
    public static Disposable writeSectionReponseInTempTable(Context context, final long executionTime, RequestCallback callback, String from) {
        String url = BuildConfig.DEFAULT_TH_BASE_URL + "sectionList_v4.php";
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
                            if(tableBanner == null) {
                                return null;
                            }
                            final String url = BuildConfig.DEFAULT_TH_BASE_URL + "newsFeed.php";
                            String bannerId = tableBanner.getSecId();

                            final JsonArray personliseSectionIds = new JsonArray();
                            for (TablePersonaliseDefault personaliseDefault : personaliseIdsTable) {
                                personliseSectionIds.add(personaliseDefault.getPersonaliseSecId());
                            }

                            /*long dataInsertTimeOfTable = bannerVal.getDataInsertTimeOfTable();
                            long currentTime = System.currentTimeMillis();
                            long fiveMins = 1000*60*5;
                            if(dataInsertTimeOfTable+fiveMins > currentTime) {
                                return null;
                            }*/

                            return ServiceFactory.getServiceAPIs().homeContent(url, ReqBody.homeFeed(personliseSectionIds, bannerId, 0));
                        });

                    })
                    .switchMap(val -> {
                        return val.subscribeOn(Schedulers.newThread());
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
                            }
                        }
                        return "";
                    })
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
    public static Disposable widgetContent(Context context, Map<String, String> sections) {
        final String url = BuildConfig.DEFAULT_TH_BASE_URL + "section-content.php";
        final Observable[] observables = new Observable[sections.size()];
        int count = 0;
        for (Map.Entry<String, String> entry : sections.entrySet()) {
            observables[count] = ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(entry.getKey(), 1, entry.getValue(), 0))
                    .subscribeOn(Schedulers.newThread());
            count++;
        }
        if (observables.length > 0) {
            return Observable.mergeArray(observables)
                    .map(value -> {
                        SectionContentFromServer sectionContent = (SectionContentFromServer) value;
                        THPDB thpdb = THPDB.getInstance(context);
                        DaoWidget daoWidget = thpdb.daoWidget();
                        if(sectionContent.getData().getArticle().size() > 0) {
                            TableWidget tableWidget = daoWidget.getWidget(sectionContent.getData().getSid());
                            tableWidget.setBeans(sectionContent.getData().getArticle());
                            daoWidget.deleteAndInsertWidget(sectionContent.getData().getSid(), tableWidget);

                            Log.i("HomeData", "widgetContent :: Inserted");
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
        return Observable.just("").subscribe();
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
    public static Disposable getSectionContent(Context context, RequestCallback<ArrayList<SectionAdapterItem>> requestCallback, String secId, int page, String type, long lut) {
        final String url = BuildConfig.DEFAULT_TH_BASE_URL + "section-content.php";
        return ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(secId, page, type, lut))
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    final ArrayList<SectionAdapterItem> uiRowItem = new ArrayList<>();
                    if (value.getData().getArticle() != null && value.getData().getArticle().size() > 0) {
                        THPDB thpdb = THPDB.getInstance(context);
                        DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
                        if (page == 1) {
                            daoSectionArticle.deleteSection(secId);
                            Log.i(TAG, "getSectionContent :: DELETED ALL ARTICLES OF SecId :: " + secId);
                        }
                        TableSectionArticle sectionArticles = new TableSectionArticle(value.getData().getSid(), value.getData().getSname(), page, value.getData().getArticle());
                        daoSectionArticle.insertSectionArticle(sectionArticles);

                        for (ArticleBean bean : value.getData().getArticle()) {
                            final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                            item.setArticleBean(bean);
                            uiRowItem.add(item);
                        }
                    }

                    return uiRowItem;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (requestCallback != null) {
                        requestCallback.onNext(value);
                    }
                    Log.i(TAG, "getSectionContent :: subscribe");
                },
                        throwable -> {
                    if (requestCallback != null) {
                        requestCallback.onError(throwable, "getSectionContent");
                    }
                            Log.i(NetConstants.TAG_ERROR, "getSectionContent() :: " + throwable);
                }, () -> {
                    Log.i(TAG, "getSectionContent :: completed");
                    if (requestCallback != null) {
                        requestCallback.onComplete("getSectionContent");
                    }
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
    public static Disposable getSubSectionContent(Context context, RequestCallback<ArrayList<SectionAdapterItem>> requestCallback, String secId, int page, String type, long lut) {
        final String url = BuildConfig.DEFAULT_TH_BASE_URL + "section-content.php";
        return ServiceFactory.getServiceAPIs().sectionContent(url, ReqBody.sectionContent(secId, page, type, lut))
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                    final ArrayList<SectionAdapterItem> uiRowItem = new ArrayList<>();
                    if (value.getData().getArticle() != null && value.getData().getArticle().size() > 0) {
                        THPDB thpdb = THPDB.getInstance(context);
                        DaoSubSectionArticle daoSubSectionArticle = thpdb.daoSubSectionArticle();
                        if (page == 1) {
                            daoSubSectionArticle.deleteSection(secId);
                            Log.i(TAG, "getSubSectionContent :: DELETED ALL ARTICLES OF SecId :: " + secId);
                        }
                        TableSubSectionArticle subSectionArticles = new TableSubSectionArticle(value.getData().getSid(), value.getData().getSname(), page, value.getData().getArticle());
                        daoSubSectionArticle.insertSubSectionArticle(subSectionArticles);

                        for (ArticleBean bean : value.getData().getArticle()) {
                            final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                            item.setArticleBean(bean);
                            uiRowItem.add(item);
                        }
                    }

                    return uiRowItem;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (requestCallback != null) {
                        requestCallback.onNext(value);
                    }
                    Log.i(TAG, "getSubSectionContent :: subscribe");
                }, throwable -> {
                    if (requestCallback != null) {
                        requestCallback.onError(throwable, "getSubSectionContent");
                    }
                    Log.i(NetConstants.TAG_ERROR, "getSubSectionContent() :: " + throwable);
                }, () -> {
                    Log.i(TAG, "getSubSectionContent :: completed");
                    if (requestCallback != null) {
                        requestCallback.onComplete("getSubSectionContent");
                    }
                });
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
                    TableRead read = new TableRead(id);
                    daoRead.insertReadArticle(read);
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


}
