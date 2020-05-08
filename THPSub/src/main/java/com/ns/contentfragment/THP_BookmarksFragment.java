package com.ns.contentfragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.adapter.AppTabContentAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.THP_AppEmptyPageListener;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.AppTabContentModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.RecyclerViewPullToRefresh;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class THP_BookmarksFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener, THP_AppEmptyPageListener {

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private LinearLayout emptyLayout;
    private AppTabContentAdapter mRecyclerAdapter;
    private String mUserId;
    private String mGoupType;

    public static THP_BookmarksFragment getInstance(String userId, String groupType) {
        THP_BookmarksFragment fragment = new THP_BookmarksFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userId",userId);
        bundle.putString("groupType",groupType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.thp_fragment_bookmark;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mUserId = getArguments().getString("userId");
            mGoupType = getArguments().getString("groupType");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullToRefreshLayout = view.findViewById(R.id.recyclerView);
        emptyLayout = view.findViewById(R.id.emptyLayout);

        mRecyclerAdapter = new AppTabContentAdapter(new ArrayList<>(), NetConstants.API_bookmarks, mUserId, mPullToRefreshLayout.getRecyclerView());
        mRecyclerAdapter.setAppEmptyPageListener(this);
        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        mPullToRefreshLayout.setTryAgainBtnClickListener(this);

        mPullToRefreshLayout.showProgressBar();

        // Pull To Refresh Listener
        registerPullToRefresh();

        loadData();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(mRecyclerAdapter != null && mRecyclerAdapter.getItemCount() > 0) {
            loadData(false);
        }

        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Bookmark Screen", THP_BookmarksFragment.class.getSimpleName());

    }

    /**
     * Adding Pull To Refresh Listener
     */
    private void registerPullToRefresh() {
        mPullToRefreshLayout.getSwipeRefreshLayout().setOnRefreshListener(()->{
            if(!BaseAcitivityTHP.sIsOnline) {
                Alerts.showSnackbar(getActivity(), getResources().getString(R.string.please_check_ur_connectivity));
                mPullToRefreshLayout.setRefreshing(false);
                return;
            }
            mPullToRefreshLayout.setRefreshing(true);

            loadData();
        });
    }

    @Override
    public void tryAgainBtnClick() {
        mPullToRefreshLayout.showProgressBar();
        loadData();
    }

    private void loadData() {
        loadData(BaseAcitivityTHP.sIsOnline);
    }

    /**
     * Load bookmarks from respective Group
     * @param isOnline
     */
    private void loadData(boolean isOnline) {



        Observable<List<ArticleBean>> observable = null;

        if (isOnline && mGoupType!=null && mGoupType.equals(NetConstants.GROUP_PREMIUM_SECTIONS)) {
            observable = ApiManager.getRecommendationFromServer(getActivity(), mUserId,
                    NetConstants.API_bookmarks, ""+1000, BuildConfig.SITEID);

        }
        else if(mGoupType!=null && mGoupType.equals(NetConstants.GROUP_PREMIUM_BOOKMARK)) {
            observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.GROUP_PREMIUM_BOOKMARK);
        }
        else if(mGoupType!=null && mGoupType.equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
            observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.GROUP_DEFAULT_BOOKMARK);
        }
        else { //(mGoupType!=null && mGoupType.equals(NetConstants.BOOKMARK_IN_ONE))
            observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.BOOKMARK_IN_ONE);
        }

        mDisposable.add(
                observable
                        .map(value->{
                            List<AppTabContentModel> content = new ArrayList<>();
                            for(ArticleBean bean : value) {
                                int viewType = BaseRecyclerViewAdapter.VT_BOOKMARK_PREMIUM;
                                if(bean.getGroupType() == null ) {
                                    continue;
                                }
                                else if(bean.getGroupType().equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
                                    viewType = BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW;
                                }
                                else if(bean.getGroupType().equals(NetConstants.GROUP_PREMIUM_BOOKMARK)) {
                                    viewType = BaseRecyclerViewAdapter.VT_BOOKMARK_PREMIUM;
                                }
                                AppTabContentModel model = new AppTabContentModel(viewType);
                                model.setBean(bean);
                                content.add(model);
                            }
                            return content;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value -> {
                            if(value.size()>0) {
                                mRecyclerAdapter.clearData();
                                mRecyclerAdapter.addData(value);
                            }
                            else {
                                showEmptyLayout();
                            }
                            mPullToRefreshLayout.hideProgressBar();
                            mPullToRefreshLayout.setRefreshing(false);
                        }, throwable -> {
                            if (throwable instanceof ConnectException
                                    || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException
                                    || throwable instanceof NullPointerException) {
                                loadData(false);
                            } else {
                                showEmptyLayout();
                            }

                            mPullToRefreshLayout.hideProgressBar();
                            mPullToRefreshLayout.setRefreshing(false);

                        }, () -> {



                        }));

    }

    private void showEmptyLayout() {
        if(mRecyclerAdapter == null || mRecyclerAdapter.getItemCount() == 0) {
            if(mGoupType!=null && mGoupType.equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
                TextView emptyTxtMsg = emptyLayout.findViewById(R.id.emptyTitleTxt);
                emptyTxtMsg.setText("You have not added any bookmarks");
            }
            emptyLayout.setVisibility(View.VISIBLE);
            mPullToRefreshLayout.setVisibility(View.GONE);
        } else {
            emptyLayout.setVisibility(View.GONE);
            mPullToRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void checkPageEmpty() {
        showEmptyLayout();
    }


}
