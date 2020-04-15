package com.ns.contentfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoHomeArticle;
import com.netoperation.default_db.DaoSection;
import com.netoperation.default_db.DaoSectionArticle;
import com.netoperation.default_db.DaoSubSectionArticle;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TableSection;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.default_db.TableSubSectionArticle;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.SectionBean;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.adapter.ExploreAdapter;
import com.ns.adapter.SectionContentAdapter;
import com.ns.adapter.WidgetAdapter;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;
import com.ns.view.text.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class SectionFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener {

    private static String TAG = NetConstants.UNIQUE_TAG;

    private String mFrom;
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
    private List<SectionBean> mSubSections;


    public static SectionFragment getInstance(String from, String sectionId, String sectionType, String sectionOrSubsectionName, boolean isSubsection) {
        SectionFragment fragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
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
                    loadMoreItems();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
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

        mRecyclerAdapter = new SectionContentAdapter(mFrom, new ArrayList<>());
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        // Pull To Refresh Listener
        registerPullToRefresh();


        if(mSectionId.equals(NetConstants.RECO_HOME_TAB)) { // Home Page of Section
            // Here we are using observable to get Home Articles and banner, because in Splash screen it is asynchronous call.
            homeAndBannerArticleFromObservable();
            // Widget Observable
            homeWidgetsFromDB();
        }
        else { // Other Sections or Sub-Section
            // Registering Scroll Listener to load more item
            mPullToRefreshLayout.getRecyclerView().addOnScrollListener(mRecyclerViewOnScrollListener);

            // If it is section
            if(!mIsSubsection) {
                getSubsections();
            }

            loadMoreItems();

        }

    }



    private void loadMoreItems() {
        if(mIsSubsection) {
            subSectionDataFromDB();
        } else {
            sectionDataFromDB();
        }
    }

    private void getSubsections() {
        Observable.just(mSectionId)
                .subscribeOn(Schedulers.io())
                .map(value->{
                    THPDB thpdb = THPDB.getInstance(getActivity());
                    DaoSection daoSection = thpdb.daoSection();
                    TableSection section = daoSection.getSubSections(value);
                    if(section != null) {
                        mSubSections = section.getSubSections();
                    }
                    return value;
                })
                .subscribe();
    }




    private void sectionDataFromDB() {
        THPDB thpdb = THPDB.getInstance(getActivity());
        DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
        Maybe<List<TableSectionArticle>> sectionArticlesMaybe = daoSectionArticle.getPageArticlesMaybe(mSectionId, mPage).subscribeOn(Schedulers.io());
        mDisposable.add(sectionArticlesMaybe
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value-> {
                    if(value == null || value.size() == 0 || value.get(0).getBeans() == null || value.get(0).getBeans().size() == 0) {
                        Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: NO Article in DB :: Page - "+mPage);
                        sectionOrSubSectionFromServer(mPage);
                    }
                    else {
                        for (TableSectionArticle sectionArticle : value) {
                            List<ArticleBean> articleBeanList = sectionArticle.getBeans();
                                for (ArticleBean bean : articleBeanList) {
                                    final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.addSingleItem(item);
                                }
                        }
                        Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: Loaded from DB :: Page - "+mPage);
                        incrementPageCount();
                    }

        }, throwable->{
                    Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: throwable from DB :: Page - "+mPage+" :: throwable - "+throwable);
        }, ()->{
                    Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: complete DB :: Page - "+(mPage-1));
        }));

    }

    private void subSectionDataFromDB() {
        final THPDB thpdb = THPDB.getInstance(getActivity());
        DaoSubSectionArticle daoSubSectionArticle = thpdb.daoSubSectionArticle();
        Maybe<List<TableSubSectionArticle>> sectionArticlesMaybe = daoSubSectionArticle.getPageArticlesMaybe(mSectionId, mPage).subscribeOn(Schedulers.io());
        mDisposable.add(sectionArticlesMaybe
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value-> {
                    if(value == null || value.size() == 0 || value.get(0).getBeans() == null || value.get(0).getBeans().size() == 0) {
                        Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: NO Article in DB :: Page - "+mPage);
                        sectionOrSubSectionFromServer(mPage);
                    }
                    else {
                        for (TableSubSectionArticle sectionArticle : value) {
                            List<ArticleBean> articleBeanList = sectionArticle.getBeans();
                            {
                                for (ArticleBean bean : articleBeanList) {
                                    final String itemRowId = "defaultRow_" + bean.getSid() + "_" + bean.getAid();
                                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.addSingleItem(item);
                                }
                            }
                        }
                        Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: Loaded from DB :: Page - "+mPage);
                        incrementPageCount();
                    }

                }, throwable->{
                    Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: throwable from DB :: Page - "+mPage+" :: throwable - "+throwable);
                }, ()->{
                    Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: complete DB :: Page - "+(mPage-1));
                }));

    }



    private void sectionOrSubSectionFromServer(int page) {

        setLoading(true);

        RequestCallback<ArrayList<SectionAdapterItem>> requestCallback = new RequestCallback<ArrayList<SectionAdapterItem>>() {
            @Override
            public void onNext(ArrayList<SectionAdapterItem> articleBeans) {
                if(page == 1) {
                    mRecyclerAdapter.deleteAllItems();
                    resetPageCount();
                }
                if(articleBeans.size() > 0) {
                    Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: Loaded from Server :: Page - " + page);
                    mRecyclerAdapter.addMultiItems(articleBeans);
                    incrementPageCount();
                } else {
                    Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: NO MORE ARTICLE On Server :: Page - " + page);
                    setLastPage(true);
                }
            }

            @Override
            public void onError(Throwable throwable, String str) {
                Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: throwable from Server :: Page - "+page+" :: throwable - "+throwable);
                setLoading(false);
                showEmptyLayout(false);
            }

            @Override
            public void onComplete(String str) {
                Log.i(TAG, "SECTION :: "+sectionOrSubsectionName+"-"+mSectionId+" :: complete Server :: Page - "+(page));
                setLoading(false);
                showEmptyLayout(false);

            }
        };
        if(mIsSubsection) {
            DefaultTHApiManager.getSubSectionContent(getActivity(), requestCallback, mSectionId, page, mSectionType, 0);
        } else {
            DefaultTHApiManager.getSectionContent(getActivity(), requestCallback, mSectionId, page, mSectionType, 0);
        }
    }

    private void addSubsectionUI() {
        if(mSubSections != null) {
            String rowItemId = "subsection_" + mSectionId;
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_SUB_SECTION, rowItemId);
            ExploreAdapter exploreAdapter = new ExploreAdapter(mSubSections, mSectionId);
            item.setExploreAdapter(exploreAdapter);
            mRecyclerAdapter.insertItem(item, 2);
        }
    }


    private void addLoadMoreUI() {
        if(mSubSections != null) {
            String rowItemId = "loadMore_" + mSectionId;
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_LOADMORE, rowItemId);
            int index = mRecyclerAdapter.indexOf(item);
            if(index == -1) {
                mRecyclerAdapter.addSingleItem(item);
            } else {
                mRecyclerAdapter.insertItem(item, mRecyclerAdapter.getItemCount()-1);
            }

        }
    }

    private void removeLoadMoreUI() {
        String rowItemId = "loadMore_" + mSectionId;
        SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_LOADMORE, rowItemId);
        int index = mRecyclerAdapter.indexOf(item);
        if(index != -1) {
            mRecyclerAdapter.deleteItem(item);
            mRecyclerAdapter.notifyItemRemoved(mRecyclerAdapter.getItemCount()-1);
        }
    }


    /**
     * Adding Pull To Refresh Listener
     */
    private void registerPullToRefresh() {
        mPullToRefreshLayout.getSwipeRefreshLayout().setOnRefreshListener(()->{
            if(!mIsOnline) {
                noConnectionSnackBar(getView());
                mPullToRefreshLayout.setRefreshing(false);
                return;
            }

            mPullToRefreshLayout.setRefreshing(true);

            if(mSectionId.equals(NetConstants.RECO_HOME_TAB)) {
                DefaultTHApiManager.homeArticles(getActivity(), "SectionFragment", new RequestCallback() {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t, String str) {
                        setLoading(false);
                    }

                    @Override
                    public void onComplete(String str) {
                        mRecyclerAdapter.deleteAllItems();
                        homeAndBannerArticleFromDB();
                    }
                });

                homeWidgetFromServer();
            }
            else {
                sectionOrSubSectionFromServer(1);
            }
        });
    }



    @Override
    public void tryAgainBtnClick() {

    }


    private void homeWidgetsFromDB() {
        final DaoWidget daoWidget = THPDB.getInstance(getActivity()).daoWidget();
        Observable<List<TableWidget>> widgetObservable = daoWidget.getWidgets().subscribeOn(Schedulers.io());
        mDisposable.add(widgetObservable
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    if(value instanceof ArrayList) {
                        ArrayList tableDatas = ((ArrayList) value);
                        // Widgets
                        for (int i = 0; i < tableDatas.size(); i++) {
                            if (tableDatas.get(i) instanceof TableWidget) {
                                TableWidget widget = (TableWidget) tableDatas.get(i);
                                final String itemRowId = "widget_" + widget.getSecId();
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_WIDGET_DEFAULT, itemRowId);
                                int index = mRecyclerAdapter.indexOf(item);
                                if (index == -1) {
                                    WidgetAdapter widgetAdapter = new WidgetAdapter(widget.getBeans(), Integer.parseInt(widget.getSecId()), widget.getSecName());
                                    item.setWidgetAdapter(widgetAdapter);
                                    mRecyclerAdapter.addSingleItem(item);
                                    Log.i(TAG, "SECTION :: " + mSectionId + " :: UI :: Widget Added :: " + itemRowId);
                                } else {
                                    item = mRecyclerAdapter.getItem(index);
                                    item.getWidgetAdapter().updateArticleList(widget.getBeans());
                                    mRecyclerAdapter.notifyItemChanged(index);
                                    Log.i(TAG, "SECTION :: " + mSectionId + " :: UI :: Widget Updated :: " + itemRowId);
                                }
                            }
                        }
                    }

                }));
    }

    private void homeWidgetFromServer() {
        final THPDB thpdb = THPDB.getInstance(getActivity());
        DaoWidget daoWidget = thpdb.daoWidget();
        daoWidget.getWidgetsSingle()
                .subscribeOn(Schedulers.io())
                .map(widgets-> {
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
     * Home data from db, observable based and in last of statement immediately it is being disposed
     */
    private void homeAndBannerArticleFromObservable() {

        CompositeDisposable disposable = new CompositeDisposable();

        DaoBanner daoBanner = THPDB.getInstance(getActivity()).daoBanner();
        DaoHomeArticle daoHomeArticle = THPDB.getInstance(getActivity()).daoHomeArticle();
        Observable<TableBanner> bannerObservable = daoBanner.getBannersObservable().subscribeOn(Schedulers.io());
        Observable<List<TableHomeArticle>> homeArticleObservable = daoHomeArticle.getArticles().subscribeOn(Schedulers.io());

        disposable.add(Observable.merge(bannerObservable, homeArticleObservable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    if(value instanceof TableBanner) {
                        final TableBanner banner = (TableBanner) value;
                        mStaticPageBean = banner.getStaticPageBean();

                        int count = 0;
                        for(ArticleBean bean : banner.getBeans()) {
                            final String itemRowId = "banner_" + bean.getSid()+"_"+bean.getAid();
                            if(count == 0) {
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_BANNER, itemRowId);
                                int index = mRecyclerAdapter.indexOf(item);
                                if (index == -1) {
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.insertItem(item, count);
                                    Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Banner Added :: " + itemRowId);
                                } else {
                                    item = mRecyclerAdapter.getItem(index);
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.notifyItemChanged(index);
                                    Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Banner Updated :: " + itemRowId);
                                }
                            }
                            else {
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                item.setArticleBean(bean);
                                mRecyclerAdapter.insertItem(item, count);
                                Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Banner Row Added :: " + itemRowId);
                            }
                            count++;
                        }
                    }
                    else if(value instanceof ArrayList) {
                        ArrayList tableDatas = ((ArrayList) value);
                        for(int i=0; i<tableDatas.size(); i++) {
                            // Home Articles
                            if(tableDatas.get(i) instanceof TableHomeArticle) {
                                final TableHomeArticle homeArticle = (TableHomeArticle) tableDatas.get(i);
                                for(ArticleBean bean : homeArticle.getBeans()) {
                                    final String itemRowId = "defaultRow_"+bean.getAid();
                                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                    int index = mRecyclerAdapter.indexOf(item);
                                    if(index == -1) {
                                        item.setArticleBean(bean);
                                        mRecyclerAdapter.addSingleItem(item);
                                        Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Default Row Added :: " + itemRowId);
                                    }
                                }
                            }
                        }

                        disposable.dispose();

                        addStaticPageBean();
                    }

                    mPullToRefreshLayout.setRefreshing(false);
                    hideProgressDialog();

                    Log.i(TAG, "SECTION :: "+mSectionId+"-"+sectionOrSubsectionName+" :: subscribe - DB");

                }, throwable -> {
                    Log.i(TAG, "SECTION :: "+mSectionId+"-"+sectionOrSubsectionName+" :: throwable - DB");
                }, () ->{
                    Log.i(TAG, "SECTION :: "+mSectionId+"-"+sectionOrSubsectionName+" :: completed - DB");

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
                .subscribe(value->{
                    if(value instanceof TableBanner) {
                        final TableBanner banner = (TableBanner) value;
                        mStaticPageBean = banner.getStaticPageBean();
                        int count = 0;
                        for(ArticleBean bean : banner.getBeans()) {
                            final String itemRowId = "banner_" + bean.getSid()+"_"+bean.getAid();
                            if(count == 0) {
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_BANNER, itemRowId);
                                int index = mRecyclerAdapter.indexOf(item);
                                if (index == -1) {
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.insertItem(item, count);
                                    Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Banner Added :: " + itemRowId);
                                } else {
                                    item = mRecyclerAdapter.getItem(index);
                                    item.setArticleBean(bean);
                                    mRecyclerAdapter.notifyItemChanged(index);
                                    Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Banner Updated :: " + itemRowId);
                                }
                            }
                            else {
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                item.setArticleBean(bean);
                                mRecyclerAdapter.insertItem(item, count);
                                Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Banner Row Added :: " + itemRowId);
                            }
                            count++;
                        }
                    }
                    else if(value instanceof ArrayList) {
                        final ArrayList tableDatas = ((ArrayList) value);
                        for(int i=0; i<tableDatas.size(); i++) {
                            // Home Articles
                            if(tableDatas.get(i) instanceof TableHomeArticle) {
                                TableHomeArticle homeArticle = (TableHomeArticle) tableDatas.get(i);

                                for(ArticleBean bean : homeArticle.getBeans()) {
                                    final String itemRowId = "defaultRow_"+bean.getAid();
                                    SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                    int index = mRecyclerAdapter.indexOf(item);
                                    if(index == -1) {
                                        item.setArticleBean(bean);
                                        mRecyclerAdapter.addSingleItem(item);
                                        Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Default Row Added :: " + itemRowId);
                                    }
                                    Log.i(TAG, "SECTION :: " + mSectionId +"-"+sectionOrSubsectionName+" :: UI :: Default Row Added :: " + itemRowId);
                                }
                            }
                        }
                    }

                    mPullToRefreshLayout.setRefreshing(false);
                    hideProgressDialog();
                    Log.i(TAG, "SECTION :: "+mSectionId+"-"+sectionOrSubsectionName+" :: subscribe - DB");

                }, throwable -> {
                    Log.i(TAG, "SECTION :: "+mSectionId+"-"+sectionOrSubsectionName+" :: throwable - DB");
                }, () -> {
                    Log.i(TAG, "SECTION :: "+mSectionId+"-"+sectionOrSubsectionName+" :: completed - DB");
                }));
    }


    private void addStaticPageBean() {
        if(mStaticPageBean != null && mStaticPageBean.getPosition() > -1 && mIsOnline) {
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

    private ImageView emptyIcon;
    private CustomTextView emptyTitleTxt;
    private CustomTextView emptySubTitleTxt;
    private CustomTextView emptyBtnTxt;

    private void showEmptyLayout(boolean isNoContent) {
        if(mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.setVisibility(View.GONE);

            emptyIcon = getView().findViewById(R.id.emptyIcon);
            emptyTitleTxt = getView().findViewById(R.id.emptyTitleTxt);
            emptySubTitleTxt = getView().findViewById(R.id.emptySubTitleTxt);
            emptyBtnTxt = getView().findViewById(R.id.emptyBtnTxt);
            if(isNoContent) {
                    emptyIcon.setImageResource(R.drawable.ic_empty_suggestion);
                    emptyTitleTxt.setVisibility(View.INVISIBLE);
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptySubTitleTxt.setText("No content in Suggestion. Please look \n back after sometime");
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setOnClickListener(v->{
                        loadMoreItems();
                    });
                }
                else {
                    if(!mIsOnline) {
                        noConnectionSnackBar(getView());
                        emptySubTitleTxt.setText(getString(R.string.no_internet_connection));
                    } else {
                        emptySubTitleTxt.setText("Something went wrong");
                    }
                    emptyIcon.setImageResource(R.drawable.ic_empty_something_wrong);
                    emptyTitleTxt.setVisibility(View.VISIBLE);
                    emptyTitleTxt.setText("Oops...");
                    emptySubTitleTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setVisibility(View.VISIBLE);
                    emptyBtnTxt.setText("Refresh");
                    emptyBtnTxt.setEnabled(true);
                    emptyBtnTxt.setOnClickListener(v->{
                        if(!mIsOnline) {
                            noConnectionSnackBar(getView());
                            return;
                        }
                        loadMoreItems();
                    });
                }
            }
         else {
            mPullToRefreshLayout.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }


}
