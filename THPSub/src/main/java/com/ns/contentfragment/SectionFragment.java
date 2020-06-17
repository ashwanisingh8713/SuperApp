package com.ns.contentfragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.main.AdsBase;
import com.main.SectionListingAds;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoHomeArticle;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.AdData;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.adapter.ExploreAdapter;
import com.ns.adapter.SectionContentAdapter;
import com.ns.adapter.SuWidgetRecyclerAdapter;
import com.ns.callbacks.OnDFPAdLoadListener;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.RowIds;
import com.ns.utils.SectionSideWork;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.RecyclerViewPullToRefresh;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SectionFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener,
        BaseFragmentTHP.EmptyViewClickListener, OnDFPAdLoadListener, AdsBase.OnTaboolaAdLoadListener {

    private static String TAG = NetConstants.TAG_UNIQUE;

    private String mPageSource;
    private String mSectionId;
    private String mSectionType;
    private String sectionOrSubsectionName;
    private boolean mIsSubsection;

    private SectionListingAds mSectionAds;

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;
    private SectionContentAdapter mRecyclerAdapter;

    private SectionSideWork mSectionSideWork;


    public static SectionFragment getInstance(String pageSource, String sectionId, String sectionType, String sectionOrSubsectionName, boolean isSubsection) {
        SectionFragment fragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pageSource", pageSource);
        bundle.putString("sectionId", sectionId);
        bundle.putString("sectionOrSubsectionName", sectionOrSubsectionName);
        bundle.putString("sectionType", sectionType);
        bundle.putBoolean("isSubsection", isSubsection);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_trending;
    }

    private RecyclerView.OnScrollListener mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mPullToRefreshLayout.getRecyclerView().getLayoutManager().getChildCount();
            int totalItemCount = mPullToRefreshLayout.getLinearLayoutManager().getItemCount();
            int firstVisibleItemPosition = mPullToRefreshLayout.getLinearLayoutManager().findFirstVisibleItemPosition();

            if (!mSectionSideWork.isLoading() && !mSectionSideWork.isLastPage() && BaseAcitivityTHP.sIsOnline) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= mSectionSideWork.PAGE_SIZE) {
                    sectionOrSubSectionDataFromDB();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageSource = getArguments().getString("pageSource");
            mSectionId = getArguments().getString("sectionId");
            mSectionType = getArguments().getString("sectionType");
            sectionOrSubsectionName = getArguments().getString("sectionOrSubsectionName");
            mIsSubsection = getArguments().getBoolean("isSubsection");
        }

        mSectionSideWork = new SectionSideWork(mSectionId);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullToRefreshLayout = view.findViewById(R.id.recyclerView);
        emptyLayout = view.findViewById(R.id.emptyLayout);
        mPullToRefreshLayout.setTryAgainBtnClickListener(this);
        mPullToRefreshLayout.hideProgressBar();

        setEmptyViewClickListener(this);

        if(mRecyclerAdapter == null) {
            mRecyclerAdapter = new SectionContentAdapter(mPageSource, new ArrayList<>(), mIsSubsection, mSectionId, mSectionType);
        }

        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setFetchingDataFromServer(false);

        // Pull To Refresh Listener
        registerPullToRefresh();

        // Checks Adapter count is zero then only insert the data
        if(mRecyclerAdapter.getItemCount() == 0) {
            if (mSectionId.equals("998")) { // Opens News-Digest
                sectionOrSubSectionFromServer(mSectionSideWork.getPage());
            } else if (mSectionId.equals(NetConstants.RECO_HOME_TAB)) { // Home Page of Section
                homeAndBannerArticleFromDB();
            } else { // Other Sections or Sub-Section
                sectionOrSubSectionDataFromDB();
                // Registering Scroll Listener to load more item
                mPullToRefreshLayout.getRecyclerView().addOnScrollListener(mRecyclerViewOnScrollListener);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean isSectionNeedToSync = DefaultPref.getInstance(getActivity()).isSectionNeedToSync(mSectionId);
        if (isSectionNeedToSync && BaseAcitivityTHP.sIsOnline) {
            showLoadingUIForServerData(true, true);
        }

        if (mSectionId.equals("998")) { // Opens News-Digest
            // TODO, NOTHING
        } else if (mSectionId.equals(NetConstants.RECO_HOME_TAB) && isSectionNeedToSync && BaseAcitivityTHP.sIsOnline) { // Home Page of Section
            getHomeDataFromServer();
            return;
        } else if (isSectionNeedToSync && BaseAcitivityTHP.sIsOnline) { // Other Sections or Sub-Section
            sectionOrSubSectionFromServer(1);
            return;
        }

        if (mRecyclerAdapter != null) {
            int findFirstVisibleItemPosition = mPullToRefreshLayout.getLinearLayoutManager().findFirstVisibleItemPosition();
            int findLastVisibleItemPosition = mPullToRefreshLayout.getLinearLayoutManager().findLastVisibleItemPosition();
            //int findFirstCompletelyVisibleItemPosition = mPullToRefreshLayout.getLinearLayoutManager().findFirstCompletelyVisibleItemPosition();
            //int findLastCompletelyVisibleItemPosition = mPullToRefreshLayout.getLinearLayoutManager().findLastCompletelyVisibleItemPosition();
            int itemCount = findLastVisibleItemPosition - findFirstVisibleItemPosition;
            if (itemCount < 3) {
                itemCount = 3;
            }
            mRecyclerAdapter.notifyItemRangeChanged(findFirstVisibleItemPosition, itemCount);

        }

        // Dummy AdData object
        AdData adData = new AdData(-1, "");
        adData.setSecId(mSectionId);
        // It sends event to AppTabFragment.java in handleEvent(AdData adData)
        EventBus.getDefault().post(adData);

        //Firebase Analytics
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Section Fragment Screen", SectionFragment.class.getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onEmptyRefreshBtnClick() {
        mPullToRefreshLayout.showProgressBar();
        showHideLoadingViewCrossFade(mPullToRefreshLayout, emptyLayout);
        new Handler().postDelayed(()->{
            sectionOrSubSectionDataFromDB();
        }, 600);
    }

    @Override
    public void onOtherStuffWork() {

    }

    private void addSubsectionUI() {
        if (mSectionSideWork.getSubSections() != null && mSectionSideWork.getSubSections().size()>0) {
            String rowItemId = RowIds.rowId_subSection(mSectionId);
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_HORIZONTAL_LIST, rowItemId);
            int index = mRecyclerAdapter.indexOf(item);
            if (index != -1) {
                item = mRecyclerAdapter.getItem(index);
                ExploreAdapter exploreAdapter = new ExploreAdapter(mSectionSideWork.getSubSections(), mSectionId);
                item.setExploreAdapter(exploreAdapter);
            }
            mRecyclerAdapter.notifyItemChanged(index);
        }
    }


    private void addStaticWebPage() {
        if (mSectionSideWork.getStaticPageBean() != null) {
            final String itemRowId = RowIds.rowId_staticWebPage(mSectionId, mSectionSideWork.getStaticPageBean().getPosition());
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_WEB_WIDGET, itemRowId);
            int index = mRecyclerAdapter.indexOf(item);
            if (index != -1) {
                item = mRecyclerAdapter.getItem(index);
                item.setStaticPageUrlBean(mSectionSideWork.getStaticPageBean());
                mRecyclerAdapter.notifyItemChanged(index);
            }

        }
    }


    private void sectionOrSubSectionDataFromDB() {
        THPDB thpdb = THPDB.getInstance(getActivity());
        mDisposable.add(thpdb.daoSectionArticle().getPageArticlesMaybe(mSectionId, mSectionSideWork.getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                            if (value == null || value.size() == 0 || value.get(0).getBeans() == null || value.get(0).getBeans().size() == 0) {
                                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: NO Article in DB :: Page - " + mSectionSideWork.getPage());
                                if (mSectionId.equals("998")) {// Opens News-Digest
                                    showEmptyLayout(emptyLayout, false, mRecyclerAdapter, mPullToRefreshLayout, false, mPageSource);
                                } else {
                                    sectionOrSubSectionFromServer(mSectionSideWork.getPage());
                                }
                            } else {
                                for (TableSectionArticle sectionArticle : value) {
                                    List<ArticleBean> articleBeanList = sectionArticle.getBeans();
                                    for (ArticleBean bean : articleBeanList) {
                                        final String itemRowId = RowIds.rowId_defaultArticle(bean.getAid());
                                        SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                        item.setArticleBean(bean);
                                        mRecyclerAdapter.addSingleItem(item);
                                    }
                                }
                                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: Loaded from DB :: Page - " + mSectionSideWork.getPage());
                                if (mSectionSideWork.getPage() == 1) {
                                    loadFirstScrollPageData();
                                }
                                else {
                                    loadOtherScrollPageData();
                                }
                                mSectionSideWork.incrementPageCount();

                                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: complete DB :: Page - " + (mSectionSideWork.getPage() - 1));
                                showLoadingUIForServerData(false, false);
                            }
                        },
                        throwable -> {
                            Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: throwable from DB :: Page - " + mSectionSideWork.getPage() + " :: throwable - " + throwable);
                            Log.i(NetConstants.TAG_ERROR, "sectionOrSubSectionDataFromDB() :: " + throwable);
                            showLoadingUIForServerData(false, false);
                        }));
    }

    private void sectionOrSubSectionFromServer(int page) {
        showLoadingUIForServerData(true, true);
        RequestCallback<ArrayList<SectionAdapterItem>> requestCallback = new RequestCallback<ArrayList<SectionAdapterItem>>() {
            @Override
            public void onNext(ArrayList<SectionAdapterItem> articleBeans) {
                if (page == 1) {
                    mRecyclerAdapter.deleteAllItems();
                    mSectionSideWork.resetPageCount();
                    // Save section last refresh time
                    DefaultPref.getInstance(getActivity()).saveSectionSyncTimePref(mSectionId);
                }
                if (articleBeans.size() > 0) {
                    Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: Loaded from Server :: Page - " + page);
                    mRecyclerAdapter.addMultiItems(articleBeans);
                    if(mSectionSideWork.getPage() == 1) {
                        loadFirstScrollPageData();
                    }
                    else {
                        loadOtherScrollPageData();
                    }
                    mSectionSideWork.incrementPageCount();
                } else {
                    Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: NO MORE ARTICLE On Server :: Page - " + page);
                    mSectionSideWork.setLastPage(true);
                }
            }

            @Override
            public void onError(Throwable throwable, String str) {
                if(mSectionId.equals("998")) {// Opens News-Digest
                    sectionOrSubSectionDataFromDB();
                }
                else {
                    showEmptyLayout(emptyLayout, false, mRecyclerAdapter, mPullToRefreshLayout, false, mPageSource);
                }
                Log.i(NetConstants.TAG_ERROR, "sectionOrSubSectionFromServer() :: " + throwable);
                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: throwable from Server :: Page - " + page + " :: throwable - " + throwable);
                showLoadingUIForServerData(false, false);
            }

            @Override
            public void onComplete(String str) {
                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: complete Server :: Page - " + (page));
                showLoadingUIForServerData(false, false);
                showEmptyLayout(emptyLayout, false, mRecyclerAdapter, mPullToRefreshLayout, false, mPageSource);
            }
        };
        if(mSectionId.equals("998")) {// Opens News-Digest
            DefaultTHApiManager.getNewsDigestContentFromServer(getActivity(), requestCallback, mSectionId, page);
        }
        else if (mIsSubsection) {
            DefaultTHApiManager.getSubSectionContentFromServer(getActivity(), requestCallback, mSectionId, page, mSectionType, 0);
        }
        else {
            DefaultTHApiManager.getSectionContentFromServer(getActivity(), requestCallback, mSectionId, page, mSectionType, 0);
        }
    }




    private void addLoadMoreUI() {
            String rowItemId = RowIds.rowId_loadMore(mSectionId);
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_LOADMORE, rowItemId);
            int index = mRecyclerAdapter.indexOf(item);
            if (index == -1) {
                mRecyclerAdapter.addSingleItem(item);
            } else {
                mRecyclerAdapter.insertItem(item, mRecyclerAdapter.getItemCount() - 1);
            }

        }

    private void removeLoadMoreUI() {
        String rowItemId = RowIds.rowId_loadMore(mSectionId);
        SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_LOADMORE, rowItemId);
        int index = mRecyclerAdapter.indexOf(item);
        if (index != -1) {
            mRecyclerAdapter.deleteItem(item);
            mRecyclerAdapter.notifyItemRemoved(mRecyclerAdapter.getItemCount() - 1);
        }
    }


    /**
     * Adding Pull To Refresh Listener
     */
    private void registerPullToRefresh() {
        mPullToRefreshLayout.getSwipeRefreshLayout().setOnRefreshListener(() -> {
            if (!BaseAcitivityTHP.sIsOnline) {
                noConnectionSnackBar(getView());
                showLoadingUIForServerData(false, false);
                return;
            }
            showLoadingUIForServerData(true, false);

            if (mSectionId.equals(NetConstants.RECO_HOME_TAB)) {
                getHomeDataFromServer();
            } else {
                sectionOrSubSectionFromServer(1);
            }
        });
    }

    private void showLoadingUIForServerData(boolean isFetchingDataFromServer, boolean enableSmoothProgressBar) {
        mPullToRefreshLayout.setScrollEnabled(!isFetchingDataFromServer);
        if(enableSmoothProgressBar) {
            mPullToRefreshLayout.showSmoothProgressBar();
        }
        mPullToRefreshLayout.setRefreshing(isFetchingDataFromServer && !enableSmoothProgressBar);
        mRecyclerAdapter.setFetchingDataFromServer(isFetchingDataFromServer);
        mSectionSideWork.setLoading(isFetchingDataFromServer);
        if(!isFetchingDataFromServer) {
            mPullToRefreshLayout.hideProgressBar();
        }
    }


    @Override
    public void tryAgainBtnClick() {

    }

    private void getHomeDataFromServer() {
        DefaultTHApiManager.homeArticles(getActivity(), "SectionFragment", new RequestCallback() {
            @Override
            public void onNext(Object o) {
                // Save section last refresh time
                DefaultPref.getInstance(getActivity()).saveSectionSyncTimePref(mSectionId);
            }

            @Override
            public void onError(Throwable throwable, String str) {
                Log.i(NetConstants.TAG_ERROR, "getHomeDataFromServer() :: " + throwable);
                showLoadingUIForServerData(false, false);
            }

            @Override
            public void onComplete(String str) {
                int co = mRecyclerAdapter.getItemCount();
                mRecyclerAdapter.deleteAllItems();
                homeAndBannerArticleFromDB();

            }
        });

        // it sends request to server to get Widget Data,
        mSectionSideWork.sendRequestToGetHomeWidgetFromServer();
    }

    /**
     * It shows home page widgets
     */
    private void SU_showHomeWidgetsFromObservable() {
        final DaoWidget daoWidget = THPDB.getInstance(getActivity()).daoWidget();
        Observable<List<TableWidget>> widgetObservable = daoWidget.getWidgets().subscribeOn(Schedulers.io());
        mDisposable.add(widgetObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {

                    for (TableWidget widget : value) {
                        if (widget.getBeans() == null || widget.getBeans().size() == 0) {
                            continue;
                        }
                        final String itemRowId = RowIds.rowId_widget(widget.getSecId());
                        SectionAdapterItem item = new SectionAdapterItem(-1, itemRowId);
                        int index = mRecyclerAdapter.indexOf(item);
                        if(index != -1) {
                            item = mRecyclerAdapter.getItem(index);
                            if(item.getSuWidgetRecyclerAdapter() == null) {
                                SuWidgetRecyclerAdapter su_widgetAdapter = new SuWidgetRecyclerAdapter(widget.getBeans(), widget.getSecName());
                                su_widgetAdapter.setWidgetIndex(mSectionSideWork.getWidgetIndex(itemRowId));
                                item.setSuWidgetRecyclerAdapter(su_widgetAdapter);
                            }
                            item.getSuWidgetRecyclerAdapter().updateArticleList(widget.getBeans());
                            mRecyclerAdapter.notifyItemChanged(index);
                            Log.i(TAG, "Home Page Widget Updated :: " + widget.getSecName() + " :: " + widget.getSecId());
                        }
                    }

                }, throwable -> {
                    Log.i(NetConstants.TAG_ERROR, "showHomeWidgets() :: " + throwable);
                }, ()->{

                }));
    }

    /**
     * Home data from db
     */
    private void homeAndBannerArticleFromDB() {
        DaoBanner daoBanner = THPDB.getInstance(getActivity()).daoBanner();
        DaoHomeArticle daoHomeArticle = THPDB.getInstance(getActivity()).daoHomeArticle();
        Single<TableBanner> bannerObservable = daoBanner.getBannersSingle().subscribeOn(Schedulers.io());
        Single<List<TableHomeArticle>> homeArticleObservable = daoHomeArticle.getArticlesSingle().subscribeOn(Schedulers.io());

        mDisposable.add(Single.merge(bannerObservable, homeArticleObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (value instanceof TableBanner) {
                        final TableBanner banner = (TableBanner) value;
                        mSectionSideWork.setStaticPageBean(banner.getStaticPageBean());
                        int count = 0;
                        for (ArticleBean bean : banner.getBeans()) {
                            if (count == 0) {
                                final String itemRowId = RowIds.rowId_banner(bean.getSid());
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_BANNER, itemRowId);
                                int index = mRecyclerAdapter.indexOf(item);
                                if (index == -1) {
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.insertItem(item, count);
                                    Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: UI :: Banner Added :: " + itemRowId);
                                } else {
                                    item = mRecyclerAdapter.getItem(index);
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.notifyItemChanged(index);
                                    Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: UI :: Banner Updated :: " + itemRowId);
                                }
                            } else {
                                final String itemRowId = RowIds.rowId_defaultArticle(bean.getAid());
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                item.setArticleBean(bean);
                                mRecyclerAdapter.insertItem(item, count);
                                Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: UI :: Banner Row Added :: " + itemRowId);
                            }
                            count++;
                        }
                    } else if (value instanceof ArrayList) {
                        final ArrayList tableDatas = ((ArrayList) value);
                        for (int i = 0; i < tableDatas.size(); i++) {
                            // Home Articles
                            if (tableDatas.get(i) instanceof TableHomeArticle) {
                                TableHomeArticle homeArticle = (TableHomeArticle) tableDatas.get(i);

                                for (ArticleBean bean : homeArticle.getBeans()) {
                                    final String itemRowId = RowIds.rowId_defaultArticle(bean.getAid());
                                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                    int index = mRecyclerAdapter.indexOf(item);
                                    if (index == -1) {
                                        item.setArticleBean(bean);
                                        mRecyclerAdapter.addSingleItem(item);
                                        Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: UI :: Default Row Added :: " + itemRowId);
                                    }
                                    Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: UI :: Default Row Added :: " + itemRowId);
                                }
                            }
                        }
                    }

                    showLoadingUIForServerData(false, false);
                    hideProgressDialog();

                }, throwable -> {
                    Log.i(NetConstants.TAG_ERROR, "homeAndBannerArticleFromDB() :: " + throwable);
                    //Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: throwable - DB");
                }, () -> {
                    Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: completed - DB");
                    loadFirstScrollPageData();

                }));
    }


    private void loadFirstScrollPageData() {
        mSectionSideWork.indexConfig(mRecyclerAdapter, sIsDayTheme);
        SU_showHomeWidgetsFromObservable();
        addStaticWebPage();
        addSubsectionUI();
        adRequest();
    }

    private void loadOtherScrollPageData() {
        mSectionSideWork.loadForAnotherPage(mRecyclerAdapter);
        adRequest();
    }


    private void adRequest() {
        if(mSectionAds == null) {
            mSectionAds = new SectionListingAds();
            mSectionAds.setOnDFPAdLoadListener(this);
            mSectionAds.setOnTaboolaAdLoadListener(this);

            mSectionAds.setTaboolaAdsBeans(mSectionSideWork.getTaboolaAdsBeans());
            mSectionAds.setDfpAdsBeans(mSectionSideWork.getDfpAdsBeans());
            doRunnableWork(runnableTaboolaDelayStart(true), 1000);
        } else {
            doRunnableWork(runnableTaboolaDelayStart(false), 1000);
        }

        mSectionAds.createMEDIUM_RECTANGLE();

    }


    @Override
    public void onDFPAdLoadSuccess(AdData adData) {
        doRunnableWork(runnableAdsData(adData), 300);
    }

    @Override
    public void onDFPAdLoadFailure(AdData adData) {
        mSectionAds.createMEDIUM_RECTANGLE();
    }

    @Override
    public void onAdClose() {

    }

    @Override
    public void onTaboolaAdLoadSuccess(AdData adData) {
        doRunnableWork(runnableAdsData(adData), 500);
    }

    @Override
    public void onTaboolaAdLoadFailure(AdData adData) {
        doRunnableWork(runnableTaboolaDelayStart(true), 1000);
    }

    private Handler mHandler;

    private void doRunnableWork(Runnable runnable, int delayInMillies) {
        if(mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(runnable, delayInMillies);
    }


    private Runnable runnableDFPDelayStart() {
        return new Runnable(){
            @Override
            public void run() {
                if(mSectionAds != null) {
                    mSectionAds.createMEDIUM_RECTANGLE();
                }
            }
        };
    }

    private Runnable runnableTaboolaDelayStart(boolean isInit) {
        return new Runnable(){
            @Override
            public void run() {
                if(mSectionAds != null) {
                    if(isInit) {
                        mSectionAds.initAndLoadRecommendationsBatch();
                    }
                    else {
                        mSectionAds.loadNextRecommendationsBatch();
                    }
                }
            }
        };
    }

    private Runnable runnableAdsData(AdData adData) {
        return new Runnable() {
            @Override
            public void run() {
                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_TABOOLA_LISTING_ADS, adData.getAdDataUiqueId());
                int index = mRecyclerAdapter.indexOf(item);
                if(index != -1) {
                    if (adData.getType().equalsIgnoreCase("DFP")) {
                        mRecyclerAdapter.getItem(index).getAdData().setAdView(adData.getAdView());
                        if (mRecyclerAdapter.getItemCount() > index)
                            doRunnableWork(runnableDFPDelayStart(), 100);
                    } else {
                        mRecyclerAdapter.getItem(index).getAdData().setTaboolaNativeAdItem(adData.getTaboolaNativeAdItem());
                        if (mRecyclerAdapter.getItemCount() > index)
                            doRunnableWork(runnableTaboolaDelayStart(true), 1000);
                    }
                    mRecyclerAdapter.notifyItemChanged(index);
                }
            }
        };
    }


}
