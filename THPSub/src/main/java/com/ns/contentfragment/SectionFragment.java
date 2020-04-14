package com.ns.contentfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoHomeArticle;
import com.netoperation.default_db.DaoSectionArticle;
import com.netoperation.default_db.DaoWidget;
import com.netoperation.default_db.TableBanner;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TableWidget;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.net.RequestCallback;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.adapter.SectionContentAdapter;
import com.ns.adapter.WidgetAdapter;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.view.RecyclerViewPullToRefresh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private boolean mIsSubsection;
    private int mPage = 1;

    int PAGE_SIZE = 10;
    private boolean isLoading;
    private boolean isLastPage;

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;

    private SectionContentAdapter mAdapter;

    private StaticPageUrlBean mStaticPageBean;


    public static SectionFragment getInstance(String from, String sectionId, String sectionType, boolean isSubsection) {
        SectionFragment fragment = new SectionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putString("sectionId", sectionId);
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
//            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int firstVisibleItemPosition = mPullToRefreshLayout.getLinearLayoutManager().findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
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

        mAdapter = new SectionContentAdapter(mFrom, new ArrayList<>());
        mPullToRefreshLayout.setDataAdapter(mAdapter);

        // Pull To Refresh Listener
        registerPullToRefresh();


        if(mSectionId.equals(NetConstants.RECO_HOME_TAB)) { // Home Page of Section

            getBannerAndHomeArticleObservable();

            DaoWidget daoWidget = THPDB.getInstance(getActivity()).daoWidget();
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
                                    int index = mAdapter.indexOf(item);
                                    if (index == -1) {
                                        WidgetAdapter widgetAdapter = new WidgetAdapter(widget.getBeans(), Integer.parseInt(widget.getSecId()), widget.getSecName());
                                        item.setWidgetAdapter(widgetAdapter);
                                        mAdapter.addSingleItem(item);
                                        Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Widget Added :: " + itemRowId);
                                    } else {
                                        item = mAdapter.getItem(index);
                                        item.getWidgetAdapter().updateArticleList(widget.getBeans());
                                        mAdapter.notifyItemChanged(index);
                                        Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Widget Updated :: " + itemRowId);
                                    }
                                }
                            }
                        }

                    }));

        }
        else if(mIsSubsection) { // Sub - Sections
            // Registering Scroll Listener to load more item
            mPullToRefreshLayout.getRecyclerView().addOnScrollListener(mRecyclerViewOnScrollListener);
        }
        else { // Other Sections
            // Registering Scroll Listener to load more item
            mPullToRefreshLayout.getRecyclerView().addOnScrollListener(mRecyclerViewOnScrollListener);
            DefaultTHApiManager.getSectionContent(getActivity(), mSectionId, mPage, mSectionType, 0);

        }

    }

    private void getBannerAndHomeArticleObservable() {

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
                                int index = mAdapter.indexOf(item);
                                if (index == -1) {
                                    item.setArticleBean(bean);
                                    mAdapter.insertItem(item, count);
                                    Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Banner Added :: " + itemRowId);
                                } else {
                                    item = mAdapter.getItem(index);
                                    item.setArticleBean(bean);
                                    mAdapter.notifyItemChanged(index);
                                    Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Banner Updated :: " + itemRowId);
                                }
                            }
                            else {
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                item.setArticleBean(bean);
                                mAdapter.insertItem(item, count);
                                Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Banner Row Added :: " + itemRowId);
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
                                    int index = mAdapter.indexOf(item);
                                    if(index == -1) {
                                        item.setArticleBean(bean);
                                        mAdapter.addSingleItem(item);
                                        Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Default Row Added :: " + itemRowId);
                                    }
                                }
                            }
                        }

                        disposable.dispose();

                        addStaticPageBean();
                    }

                    mPullToRefreshLayout.setRefreshing(false);
                    hideProgressDialog();

                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: subscribe - DB");

                }, throwable -> {
                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: throwable - DB");
                }, () ->{
                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: completed - DB");

                }));
    }

    private void getBannerAndHomeArticle() {
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
                                int index = mAdapter.indexOf(item);
                                if (index == -1) {
                                    item.setArticleBean(bean);
                                    mAdapter.insertItem(item, count);
                                    Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Banner Added :: " + itemRowId);
                                } else {
                                    item = mAdapter.getItem(index);
                                    item.setArticleBean(bean);
                                    mAdapter.notifyItemChanged(index);
                                    Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Banner Updated :: " + itemRowId);
                                }
                            }
                            else {
                                SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                                item.setArticleBean(bean);
                                mAdapter.insertItem(item, count);
                                Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Banner Row Added :: " + itemRowId);
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
                                    int index = mAdapter.indexOf(item);
                                    if(index == -1) {
                                        item.setArticleBean(bean);
                                        mAdapter.addSingleItem(item);
                                        Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Default Row Added :: " + itemRowId);
                                    }
                                    Log.i(TAG, "SEC-ID :: " + mSectionId + " :: UI :: Default Row Added :: " + itemRowId);
                                }
                            }
                        }
                    }

                    mPullToRefreshLayout.setRefreshing(false);
                    hideProgressDialog();
                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: subscribe - DB");

                }, throwable -> {
                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: throwable - DB");
                }, () -> {
                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: completed - DB");
                }));
    }


    private void addStaticPageBean() {
        if(mStaticPageBean != null && mStaticPageBean.getPosition() > -1 && mIsOnline) {
            final String itemRowId = "staticWebpage_" + mStaticPageBean.getPosition();
            SectionAdapterItem item = new SectionAdapterItem(BaseRecyclerViewAdapter.VT_WEB_WIDGET, itemRowId);
            int index = mAdapter.indexOf(item);
            if (index == -1) {
                item.setStaticPageUrlBean(mStaticPageBean);
                mAdapter.insertItem(item, mStaticPageBean.getPosition());
            }
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
                Log.i(TAG, "SEC-ID :: "+mSectionId+" :: REFRESH STARTED ");
                DefaultTHApiManager.homeArticles(getActivity(), "SectionFragment", new RequestCallback() {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable t, String str) {

                    }

                    @Override
                    public void onComplete(String str) {
                        mAdapter.deleteAllItems();
                        getBannerAndHomeArticle();
                    }
                });

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
            else {
                loadMoreItems();
            }
        });
    }

    private void loadMoreItems() {
        THPDB thpdb = THPDB.getInstance(getActivity());
        DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
        daoSectionArticle.getPageArticles(mSectionId, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value->{
                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: subscribe - refresh");
                }, throwable -> {
                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: throwable - refresh");
                }, () -> {
                    Log.i(TAG, "SEC-ID :: "+mSectionId+" :: completed - refresh");
                });
    }

    @Override
    public void tryAgainBtnClick() {

    }
}
