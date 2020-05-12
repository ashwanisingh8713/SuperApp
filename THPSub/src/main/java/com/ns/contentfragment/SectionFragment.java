package com.ns.contentfragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdSize;
import com.main.DFPAds;
import com.main.TaboolaAds;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoHomeArticle;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.AdData;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.SectionBean;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.adapter.ExploreAdapter;
import com.ns.adapter.SectionContentAdapter;
import com.ns.adapter.WidgetAdapter;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class SectionFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener,
        BaseFragmentTHP.EmptyViewClickListener, DFPAds.OnDFPAdLoadListener, TaboolaAds.OnTaboolaAdLoadListener {

    private static String TAG = NetConstants.TAG_UNIQUE;

    private String mPageSource;
    private String mSectionId;
    private String mSectionType;
    private String sectionOrSubsectionName;
    private boolean mIsSubsection;
    private int mPage = 1;

    int PAGE_SIZE = 10;
    private boolean isLoading;
    private boolean isLastPage;

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;
    private SectionContentAdapter mRecyclerAdapter;

    private StaticPageUrlBean mStaticPageBean;


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

            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
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

            if(mSectionId.equals("998")) { // Opens News-Digest
                sectionOrSubSectionFromServer(mPage);
            }
            else if (mSectionId.equals(NetConstants.RECO_HOME_TAB)) { // Home Page of Section
                // Here we are using observable to get Home Articles and banner, because in Splash screen it is asynchronous call.
                homeAndBannerArticleFromDB();
            }
            else { // Other Sections or Sub-Section
                // Registering Scroll Listener to load more item
                mPullToRefreshLayout.getRecyclerView().addOnScrollListener(mRecyclerViewOnScrollListener);
                sectionOrSubSectionDataFromDB();
            }

        }

        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        // Pull To Refresh Listener
        registerPullToRefresh();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(mRecyclerAdapter != null) {
            mRecyclerAdapter.notifyDataSetChanged();
        }

        AdData adData = new AdData(-1, "");
        adData.setSecId(mSectionId);
        // It sends event to AppTabFragment.java in handleEvent(AdData adData)
        EventBus.getDefault().post(adData);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onEmptyRefreshBtnClick() {
        mPullToRefreshLayout.showProgressBar();
        //mPullToRefreshLayout.setVisibility(View.VISIBLE);
        //hideEmptyLayout(emptyLayout);
        hideLoadingViewCrossFade(mPullToRefreshLayout, emptyLayout);
        new Handler().postDelayed(()->{
            sectionOrSubSectionDataFromDB();
        }, 600);

    }

    @Override
    public void onOtherStuffWork() {

    }

    private void getSubsections() {
        mDisposable.add(Observable.just(mSectionId)
                .subscribeOn(Schedulers.io())
                .map(secId -> {
                    THPDB thpdb = THPDB.getInstance(getActivity());
                    DaoSection daoSection = thpdb.daoSection();
                    return daoSection.getSubSections(secId).getSubSections();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val->{
                    addSubsectionUI(val);
                }, throwable -> {

                }));
    }

    private void getStaticPage() {
            THPDB.getInstance(getActivity())
                    .daoSection().getStaticPage(mSectionId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(staticPageUrlBean -> {
                        mStaticPageBean = staticPageUrlBean;
                        addWebPagePageBean();
                    }, throwable -> {
                        Log.i("", "");
                    });
    }


    private void sectionOrSubSectionDataFromDB() {
        THPDB thpdb = THPDB.getInstance(getActivity());
        mDisposable.add(thpdb.daoSectionArticle().getPageArticlesMaybe(mSectionId, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                            if (value == null || value.size() == 0 || value.get(0).getBeans() == null || value.get(0).getBeans().size() == 0) {
                                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: NO Article in DB :: Page - " + mPage);
                                if (mSectionId.equals("998")) {// Opens News-Digest
                                    showEmptyLayout(emptyLayout, false, mRecyclerAdapter, mPullToRefreshLayout, false, mPageSource);
                                } else {
                                    sectionOrSubSectionFromServer(mPage);
                                }
                            } else {
                                for (TableSectionArticle sectionArticle : value) {
                                    List<ArticleBean> articleBeanList = sectionArticle.getBeans();
                                    for (ArticleBean bean : articleBeanList) {
                                        final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                                        SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                        item.setArticleBean(bean);
                                        mRecyclerAdapter.addSingleItem(item);
                                    }
                                }
                                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: Loaded from DB :: Page - " + mPage);
                                if (mPage == 1) {
                                    getSubsections();
                                    getStaticPage();
                                }
                                incrementPageCount();

                                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: complete DB :: Page - " + (mPage - 1));
                                setLoading(false);
                                mPullToRefreshLayout.setRefreshing(false);
                                loadAdvertisment();
                            }
                        },
                        throwable -> {
                            Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: throwable from DB :: Page - " + mPage + " :: throwable - " + throwable);
                            Log.i(NetConstants.TAG_ERROR, "sectionOrSubSectionDataFromDB() :: " + throwable);
                            setLoading(false);
                            mPullToRefreshLayout.setRefreshing(false);
                        }));
    }

    private void sectionOrSubSectionFromServer(int page) {
        mPullToRefreshLayout.showSmoothProgressBar();
        setLoading(true);
        RequestCallback<ArrayList<SectionAdapterItem>> requestCallback = new RequestCallback<ArrayList<SectionAdapterItem>>() {
            @Override
            public void onNext(ArrayList<SectionAdapterItem> articleBeans) {
                if (page == 1) {
                    mRecyclerAdapter.deleteAllItems();
                    resetPageCount();
                }
                if (articleBeans.size() > 0) {
                    Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: Loaded from Server :: Page - " + page);
                    mRecyclerAdapter.addMultiItems(articleBeans);
                    if(mPage == 1) {
                        getSubsections();
                    }
                    incrementPageCount();
                } else {
                    Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: NO MORE ARTICLE On Server :: Page - " + page);
                    setLastPage(true);
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
                setLoading(false);
                mPullToRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onComplete(String str) {
                Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: complete Server :: Page - " + (page));
                setLoading(false);
                mPullToRefreshLayout.hideProgressBar();
                mPullToRefreshLayout.setRefreshing(false);
                showEmptyLayout(emptyLayout, false, mRecyclerAdapter, mPullToRefreshLayout, false, mPageSource);
                loadAdvertisment();
            }
        };
        if(mSectionId.equals("998")) {// Opens News-Digest
            DefaultTHApiManager.getNewsDigestContentFromServer(getActivity(), requestCallback, mSectionId, page);
        }
        else if (mIsSubsection) {
            DefaultTHApiManager.getSubSectionContentFromServer(getActivity(), requestCallback, mSectionId, page, mSectionType, 0);
        } else {
            DefaultTHApiManager.getSectionContentFromServer(getActivity(), requestCallback, mSectionId, page, mSectionType, 0);
        }
    }

    private void addSubsectionUI(List<SectionBean> mSubSections) {
        if (mSubSections != null && mSubSections.size()>0) {
            String rowItemId = "subsection_" + mSectionId;
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_HORIZONTAL_LIST, rowItemId);
            ExploreAdapter exploreAdapter = new ExploreAdapter(mSubSections, mSectionId);
            item.setExploreAdapter(exploreAdapter);
            mRecyclerAdapter.insertItem(item, 2);

        }
    }


    private void addLoadMoreUI() {
            String rowItemId = "loadMore_" + mSectionId;
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_LOADMORE, rowItemId);
            int index = mRecyclerAdapter.indexOf(item);
            if (index == -1) {
                mRecyclerAdapter.addSingleItem(item);
            } else {
                mRecyclerAdapter.insertItem(item, mRecyclerAdapter.getItemCount() - 1);
            }

        }

    private void removeLoadMoreUI() {
        String rowItemId = "loadMore_" + mSectionId;
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
                mPullToRefreshLayout.setRefreshing(false);
                return;
            }

            mPullToRefreshLayout.setRefreshing(true);

            if (mSectionId.equals(NetConstants.RECO_HOME_TAB)) {
                getHomeDataFromServer();
            } else {
                sectionOrSubSectionFromServer(1);
            }
        });
    }


    @Override
    public void tryAgainBtnClick() {

    }

    private void getHomeDataFromServer() {
        DefaultTHApiManager.homeArticles(getActivity(), "SectionFragment", new RequestCallback() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable throwable, String str) {
                Log.i(NetConstants.TAG_ERROR, "getHomeDataFromServer() :: " + throwable);
                setLoading(false);
            }

            @Override
            public void onComplete(String str) {
                mRecyclerAdapter.deleteAllItems();
                homeAndBannerArticleFromDB();
            }
        });

        getHomeWidgetFromServer();
    }


    private void showHomeWidgets() {
        final DaoWidget daoWidget = THPDB.getInstance(getActivity()).daoWidget();
        Observable<List<TableWidget>> widgetObservable = daoWidget.getWidgets().subscribeOn(Schedulers.io());
        mDisposable.add(widgetObservable
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (value instanceof ArrayList) {
                        ArrayList tableDatas = ((ArrayList) value);
                        // Widgets
                        for (int i = 0; i < tableDatas.size(); i++) {
                            if (tableDatas.get(i) instanceof TableWidget) {
                                TableWidget widget = (TableWidget) tableDatas.get(i);

                                if(widget.getBeans() == null || widget.getBeans().size() == 0) {
                                    continue;
                                }

                                final String itemRowId = "widget_" + widget.getSecId();
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_WIDGET_DEFAULT, itemRowId);
                                int index = mRecyclerAdapter.indexOf(item);
                                if (index == -1) {
                                    WidgetAdapter widgetAdapter = new WidgetAdapter(widget.getBeans(), Integer.parseInt(widget.getSecId()), widget.getSecName());
                                    item.setWidgetAdapter(widgetAdapter);
                                    mRecyclerAdapter.addSingleItem(item);
                                    Log.i(TAG, "Home Page Widget Added :: " + widget.getSecName() + " :: " + widget.getSecId());
                                } else {
                                    item = mRecyclerAdapter.getItem(index);
                                    item.getWidgetAdapter().updateArticleList(widget.getBeans());
                                    mRecyclerAdapter.notifyItemChanged(index);
                                    Log.i(TAG, "Home Page Widget Updated :: " + widget.getSecName() + " :: " + widget.getSecId());
                                }
                            }
                        }
                    }
                }, throwable -> {
                    Log.i(NetConstants.TAG_ERROR, "showHomeWidgets() :: " + throwable);
                }));
    }

    private void getHomeWidgetFromServer() {
        final THPDB thpdb = THPDB.getInstance(getActivity());
        DaoWidget daoWidget = thpdb.daoWidget();
        daoWidget.getWidgetsSingle()
                .subscribeOn(Schedulers.io())
                .map(widgets -> {
                    final Map<String, String> sections = new HashMap<>();
                    for (TableWidget widget : widgets) {
                        sections.put(widget.getSecId(), widget.getType());
                    }
                    DefaultTHApiManager.widgetContent(getActivity(), sections);
                    return "";
                })
                .subscribe();
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
                        mStaticPageBean = banner.getStaticPageBean();
                        int count = 0;
                        for (ArticleBean bean : banner.getBeans()) {
                            final String itemRowId = "banner_" + bean.getSid() + "_" + bean.getAid();
                            if (count == 0) {
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
                                    final String itemRowId = "defaultRow_" + bean.getAid();
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

                    mPullToRefreshLayout.setRefreshing(false);
                    hideProgressDialog();

                }, throwable -> {
                    Log.i(NetConstants.TAG_ERROR, "homeAndBannerArticleFromDB() :: " + throwable);
                    //Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: throwable - DB");
                }, () -> {
                    Log.i(TAG, "SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: completed - DB");
                    // Widget Observable
                    showHomeWidgets();
                    addWebPagePageBean();
                    loadAdvertisment();
                }));
    }


    private void addWebPagePageBean() {
        if (mStaticPageBean != null && mStaticPageBean.isIsEnabled()
                && mStaticPageBean.getPosition() > -1 && BaseAcitivityTHP.sIsOnline) {
            final String itemRowId = "staticWebpage_" + mStaticPageBean.getPosition();
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_WEB_WIDGET, itemRowId);
            int index = mRecyclerAdapter.indexOf(item);
            if (index == -1) {
                item.setStaticPageUrlBean(mStaticPageBean);
                mRecyclerAdapter.insertItem(item, mStaticPageBean.getPosition());
            }
        }
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    private void incrementPageCount() {
        mPage++;
    }

    private void resetPageCount() {
        mPage = 1;
    }

    private TaboolaAds taboolaAds;
    private DFPAds dfpAds;

    private void loadAdvertisment() {
        if(!BaseAcitivityTHP.sIsOnline) {
            return;
        }

        Log.i("TATA", "loadAdvertisment :: ");

        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration == null) {
            return;
        }
        List<AdData> listingAdsBeans = tableConfiguration.getAds().getListingPageAds();
        List<AdData> taboolaAdsBeans = new ArrayList<>();
        List<AdData> taboolaAdsBeans1 = new ArrayList<>();

        if(dfpAds == null) {
            dfpAds = new DFPAds(this);
        }

        for(AdData adsBean : listingAdsBeans) {
            adsBean.setSecId(mSectionId);
            if(adsBean.getType().equalsIgnoreCase("DFP")) {
                adsBean.setAdSize(AdSize.MEDIUM_RECTANGLE);
                dfpAds.createMEDIUM_RECTANGLE(adsBean);
            }
            else {
                taboolaAdsBeans.add(adsBean);
            }
        }

        //taboolaAdsBeans1.add(taboolaAdsBeans.get(0));
        dfpAds.listingAds();

        if(taboolaAds == null && taboolaAdsBeans.size() > 0) {
                taboolaAds = new TaboolaAds(this, taboolaAdsBeans);
                taboolaAds.initAndLoadRecommendationsBatch();
        }
    }


    @Override
    public void onDFPAdLoadSuccess(AdData adData) {
        SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_300X250_ADS, adData.getAdDataUiqueId());
        item.setAdData(adData);
        int index = mRecyclerAdapter.indexOf(item);
        if(index == -1) {
            if(adData.getAdView() != null) {
                int updateIndex = mRecyclerAdapter.insertItem(item, adData.getIndex());
                mRecyclerAdapter.notifyItemChanged(updateIndex);
            }
        }
    }

    @Override
    public void onDFPAdLoadFailure(AdData adData) {

    }


    @Override
    public void onTaboolaAdLoadSuccess(AdData adData) {
        SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_TABOOLA_LISTING_ADS, adData.getAdDataUiqueId());
        item.setAdData(adData);
        int index = mRecyclerAdapter.indexOf(item);

        if(index == -1) {
            Log.i("TATA", "Received :: "+adData.getIndex());
            Log.i("TATA", "SecId :: "+adData.getSecId());
            taboolaAds.loadNextRecommendationsBatch();
                int updateIndex = mRecyclerAdapter.insertItem(item, adData.getIndex());
                mRecyclerAdapter.notifyItemChanged(updateIndex);
        }
    }

    @Override
    public void onTaboolaAdLoadFailure(AdData adData) {

    }

}
