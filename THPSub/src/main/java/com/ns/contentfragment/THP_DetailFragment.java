package com.ns.contentfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdSize;
import com.main.DFPAds;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoMPReadArticle;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.default_db.TableMPReadArticle;
import com.netoperation.model.AdData;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.PremiumPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.activity.THP_DetailActivity;
import com.ns.adapter.PremiumListingContentAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.FragmentTools;
import com.ns.callbacks.OnDFPAdLoadListener;
import com.ns.callbacks.ToolbarChangeRequired;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.AppTabContentModel;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.tts.TTSCallbacks;
import com.ns.tts.TTSManager;
import com.ns.utils.AppAudioManager;
import com.ns.utils.CommonUtil;
import com.ns.utils.ContentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.RowIds;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.RecyclerViewPullToRefresh;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class THP_DetailFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener, FragmentTools, OnDFPAdLoadListener {

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private PremiumListingContentAdapter mRecyclerAdapter;
    private ArticleBean mArticleBean;
    private String mArticleId;
    private String mFrom;

    private long mPageStartTime = 0L;
    private long mPageEndTime = 0L;


    public static THP_DetailFragment getInstance(ArticleBean articleBean, String articleId, String userId, String from) {
        THP_DetailFragment fragment = new THP_DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("ArticleBean", articleBean);
        bundle.putString("articleId", articleId);
        bundle.putString("userId", userId);
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof THP_DetailActivity) {
            mActivity = (THP_DetailActivity) context;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof THP_DetailActivity) {
            mActivity = (THP_DetailActivity) activity;
        }
    }

    public String getArticleId() {
        return mArticleId;
    }

    public boolean isArticleRestricted() {
        if(mArticleBean != null) {
            return mArticleBean.isArticleRestricted();
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mArticleBean = getArguments().getParcelable("ArticleBean");
            mArticleId = getArguments().getString("articleId");
            mUserId = getArguments().getString("userId");
            mFrom = getArguments().getString("from");
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_detail_thp_1;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPullToRefreshLayout = view.findViewById(R.id.recyclerView);

        if(mRecyclerAdapter == null) {
            mRecyclerAdapter = new PremiumListingContentAdapter(new ArrayList<>(), mFrom, mUserId, mPullToRefreshLayout.getRecyclerView());
        }

        mPullToRefreshLayout.showProgressBar();

        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        mPullToRefreshLayout.setTryAgainBtnClickListener(this:: tryAgainBtnClick);

        mPullToRefreshLayout.enablePullToRefresh(false);

        // Reading User Profile Data
        readUserData();

        // Pull To Refresh Listener
        registerPullToRefresh();

        //loadRecyclerData();

        mPullToRefreshLayout.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int findLastVisibleItemPosition = mPullToRefreshLayout.getLinearLayoutManager().findLastVisibleItemPosition();
                if(isRelatedArticleLoaded) {
                    return;
                }
                AppTabContentModel postCommentBtnModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_POST_COMMENT_BTN_VIEW, RowIds.rowId_postCommentBtn());
                int postCommentIndex = mRecyclerAdapter.indexOf(postCommentBtnModel);
                if(postCommentIndex != -1 && findLastVisibleItemPosition==postCommentIndex && !isRelatedArticleLoaded) {
                    isRelatedArticleLoaded = true;
                    getRelatedArticle();
                }
            }
        });

    }

    private int maintainRefreshStateForOnResume = -1;
    private boolean isRelatedArticleLoaded;

    @Override
    public void onResume() {
        super.onResume();


        mPageStartTime = System.currentTimeMillis();
        mPageEndTime = 0L;
        // Set Toolbar Item Click Listener
        mActivity.setOnFragmentTools(this);

        // Checking Visible Article is bookmarked or not.
        isExistInBookmark(mArticleId);

        // Checking Visible Article is Like and Fav or not.
        isFavOrLike();

        showCommentCount();

        loadRecyclerData();


       //Firebase Analytics
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "THP DetailFragment Screen", THP_DetailFragment.class.getSimpleName());

    }


    private void getRelatedArticle() {
        DefaultTHApiManager.getRelatedArticle(mArticleId)
                .map(tableRelatedArticles -> {
                    Log.i("", "");
                    isRelatedArticleLoaded = true;
                    final ArrayList<AppTabContentModel> uiRowItem = new ArrayList<>();
                    if(tableRelatedArticles.getBeans().size() > 0 ) {
                        final String itemRowId = RowIds.rowId_relatedArticleHeader();
                        AppTabContentModel item = new AppTabContentModel(BaseRecyclerViewAdapter.VT_RELATED_ARTICLE_HEADER, itemRowId);
                        uiRowItem.add(item);
                    }
                    for (ArticleBean bean : tableRelatedArticles.getBeans()) {
                        final String itemRowId = RowIds.rowId_defaultArticle(bean.getAid());
                        AppTabContentModel item = new AppTabContentModel(BaseRecyclerViewAdapter.VT_THD_DEFAULT_ROW, itemRowId);
                        item.setBean(bean);
                        uiRowItem.add(item);
                    }
                    return uiRowItem;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(uiRowItem->{
                    if(uiRowItem.size() > 0) {
                        AppTabContentModel postCommentBtnModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_POST_COMMENT_BTN_VIEW, RowIds.rowId_postCommentBtn());
                        int postCommentIndex = mRecyclerAdapter.indexOf(postCommentBtnModel);
                        mRecyclerAdapter.insertItem(uiRowItem, postCommentIndex + 1);
                    }
                    isRelatedArticleLoaded = true;
                }, throwable -> {
                    Log.i("", "");
                    //isRelatedArticleLoaded = false;
                });
    }

    private void loadRecyclerData() {
        if(mFrom.equals(NetConstants.G_DEFAULT_SECTIONS)
                || ContentUtil.isFromBookmarkPage(mFrom)
                || mFrom.equals(NetConstants.G_BOOKMARK_DEFAULT)
                || mFrom.equals(NetConstants.RECO_TEMP_NOT_EXIST)
                || mFrom.equals(NetConstants.G_NOTIFICATION)
                || mFrom.equals(NetConstants.PS_ADD_ON_SECTION)) {
            THPDB thpdb = THPDB.getInstance(getActivity());
            DaoMPReadArticle daoRead = thpdb.daoMPReadArticle();
            mDisposable.add(daoRead.isArticleRestricted(mArticleId)
                    .subscribeOn(Schedulers.io())
                    .map(tableMPReadArticle->{
                        List<String> allRestrictedArticleIds = daoRead.getAllRestrictedArticleIds();
                        if(tableMPReadArticle != null && tableMPReadArticle.getArticleId().equals(mArticleId)) {
                            tableMPReadArticle.setTotalReadCount(allRestrictedArticleIds.size());
                            return tableMPReadArticle;
                        } else {
                            TableMPReadArticle tableMPReadArticle1 = new TableMPReadArticle();
                            tableMPReadArticle1.setTotalReadCount(allRestrictedArticleIds.size());
                            return tableMPReadArticle1;
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(tableMPReadArticle->{
                        // It sends event in THP_DetailActivity.java => handleEvent(TableMPReadArticle mpReadArticle)
                        EventBus.getDefault().post(tableMPReadArticle);

                        final String groupType = mArticleBean.getGroupType();

                        ToolbarChangeRequired toolbarChangeRequired = ContentUtil.getTopbarFromGroupType(groupType);

                        if(!ContentUtil.shouldShowMeteredPaywall() || !tableMPReadArticle.isArticleRestricted() || (!tableMPReadArticle.isUserCanReRead() && tableMPReadArticle.isArticleRestricted())) {
                            if (!ContentUtil.shouldShowMeteredPaywall() || !tableMPReadArticle.isArticleRestricted()) {
                                // TODO, Noting for now
                            }
                            else {
                                toolbarChangeRequired.setTypeOfToolbar(ToolbarChangeRequired.DEFAULT_RESTRICTED_DETAIL_TOPBAR_CROWN);
                            }
                        }

                        // It sends event in THP_DetailActivity.java => ToolbarChangeRequired toolbarChangeRequired
                        EventBus.getDefault().post(toolbarChangeRequired);

                        if(!ContentUtil.shouldShowMeteredPaywall() || !tableMPReadArticle.isArticleRestricted() || tableMPReadArticle.isUserCanReRead()) {
                            if(maintainRefreshStateForOnResume == 0 || maintainRefreshStateForOnResume == -1 || PremiumPref.getInstance(getActivity()).isRefreshRequired()) {
                                if(maintainRefreshStateForOnResume == 0 || maintainRefreshStateForOnResume == -1) {
                                    maintainRefreshStateForOnResume = 1;
                                }
                                mRecyclerAdapter.clearData();
                                // Creates and Shows Detail Page
                                if(groupType != null && groupType.equals(NetConstants.G_BOOKMARK_PREMIUM)) {
                                    if (mArticleBean.getHasDescription() == 0) {
                                        loadDataPremiumFromServer();
                                    } else {
                                        premium_loadDataFromDB();
                                    }
                                } else {
                                    dgPage(mArticleBean);
                                }
                                if (PremiumPref.getInstance(getActivity()).isRefreshRequired()) {
                                    ApiManager.nowNotRefreshRequired(getActivity());
                                }
                            }
                        } else {
                            if(maintainRefreshStateForOnResume == 1 || maintainRefreshStateForOnResume == -1 || PremiumPref.getInstance(getActivity()).isRefreshRequired()) {
                                if(maintainRefreshStateForOnResume == 1 || maintainRefreshStateForOnResume == -1) {
                                    maintainRefreshStateForOnResume = 0;
                                }
                                mRecyclerAdapter.clearData();
                                // Creates and Shows Detail Page
                                dgRestrictedPage(mArticleBean);

                                if (PremiumPref.getInstance(getActivity()).isRefreshRequired()) {
                                    ApiManager.nowNotRefreshRequired(getActivity());
                                }
                            }
                        }
                    }, throwable -> {
                        Log.i("", "");
                        mPullToRefreshLayout.hideProgressBar();
                    }));
        }
        else if(ContentUtil.isFromBookmarkPage(mFrom)) {

        }
        else {
            if(mRecyclerAdapter == null || mRecyclerAdapter.getItemCount()==0) {
                if (mArticleBean.getHasDescription() == 0) {
                    loadDataPremiumFromServer();
                } else {
                    premium_loadDataFromDB();
                }
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mPageStartTime > 0) {
            mPageEndTime = System.currentTimeMillis();
            sendEventCapture(mPageStartTime, mPageEndTime);
            mPageStartTime = -1L;
            mPageEndTime = -1L;
        }
        //Stop Media Player if Playing
        if (AppAudioManager.getInstance().isPlaying()){
            AppAudioManager.getInstance().pausePlayer();
        }
        //TTS Player UI update.
        if(TTSManager.getInstance().isTTSPlaying()) {
            mActivity.getDetailToolbar().showTTSPlayView(DefaultPref.getInstance(getActivity()).isLanguageSupportTTS());
        }
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


        });
    }


    @Override
    public void tryAgainBtnClick() {
        mPullToRefreshLayout.showProgressBar();

    }



    private void loadDataPremiumFromServer() {

        if (THPConstants.SEARCH_BY_ARTICLE_ID_URL == null) {
            mDisposable.add(THPDB.getInstance(getActivity()).daoConfiguration().getConfigurationSingle()
                    .subscribeOn(Schedulers.io())
                    .subscribe((tableConfiguration, throwable) -> {
                        THPConstants.SEARCH_BY_ARTICLE_ID_URL = tableConfiguration.getSearchOption().getUrlId();
                                Observable<ArticleBean> observable = ApiManager.premiumArticleDetailFromServer(getActivity(), mArticleId, THPConstants.SEARCH_BY_ARTICLE_ID_URL, mFrom);
                                mDisposable.add(
                                        observable.observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(recoBean -> {
                                                            premiumPage(recoBean);
                                                        },
                                                        throwabl -> {
                                                            Log.i("", "");
                                                            mPullToRefreshLayout.hideProgressBar();
                                                        })
                                );
                            }
                    ));
        }
        else {
            Observable<ArticleBean> observable = ApiManager.premiumArticleDetailFromServer(getActivity(), mArticleId, THPConstants.SEARCH_BY_ARTICLE_ID_URL, mFrom);
            mDisposable.add(
                    observable.observeOn(AndroidSchedulers.mainThread())
                            .subscribe(recoBean -> {
                                        premiumPage(recoBean);
                                    },
                                    throwable -> {
                                        Log.i("", "");
                                        mPullToRefreshLayout.hideProgressBar();
                                    })
            );
        }
    }

    private void premium_loadDataFromDB() {
        Observable<ArticleBean> observable = ApiManager.premium_singleArticleFromDB(getActivity(), mArticleId, mFrom);
        mDisposable.add(
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(recoBean->{
                                    premiumPage(recoBean);
                                },
                                throwable->{
                                    Log.i("", "");
                                    mPullToRefreshLayout.hideProgressBar();
                                })
        );
    }

    @Override
    public void onBackClickListener() {

    }

    @Override
    public void onSearchClickListener(ToolbarCallModel toolbarCallModel) {

    }

    @Override
    public void onShareClickListener(ToolbarCallModel toolbarCallModel) {
        CommonUtil.shareArticle(getActivity(), mArticleBean);
    }

    @Override
    public void onCreateBookmarkClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFrom.equals(NetConstants.G_DEFAULT_SECTIONS)
                || mFrom.equals(NetConstants.G_BOOKMARK_DEFAULT)
                || mFrom.equals(NetConstants.RECO_TEMP_NOT_EXIST)
                || mFrom.equals(NetConstants.G_NOTIFICATION)
                || mFrom.equals(NetConstants.PS_ADD_ON_SECTION)) {
            mArticleBean.setGroupType(NetConstants.G_BOOKMARK_DEFAULT);
            mArticleBean.setIsBookmark(1);
            // To Create at App end
            mDisposable.add(ApiManager.createBookmark(getContext(), mArticleBean).subscribe(boole -> {
                isExistInBookmark(mArticleBean.getArticleId());
            }, throwable -> {
                Log.i("", "");
            }));

            Alerts.showToastAtCenter(getActivity(), "Added to Read Later");
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getActivity(), "Action", "Details : Added Read Later : " + mArticleBean.getArticleId(), THP_DetailFragment.class.getSimpleName());
            CleverTapUtil.cleverTapBookmarkFavLike(getActivity(), mArticleId, mFrom, "NetConstants.BOOKMARK_YES");
        }
        else {
            premium_updateBookmarkFavLike(getActivity(), mArticleBean, "bookmark");
        }
    }

    @Override
    public void onRemoveBookmarkClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFrom.equals(NetConstants.G_DEFAULT_SECTIONS)
                || mFrom.equals(NetConstants.G_BOOKMARK_DEFAULT)
                || mFrom.equals(NetConstants.RECO_TEMP_NOT_EXIST)
                || mFrom.equals(NetConstants.G_NOTIFICATION)
                || mFrom.equals(NetConstants.PS_ADD_ON_SECTION)) {
            mArticleBean.setIsBookmark(0);
            // To Remove at App end
            mDisposable.add(ApiManager.createUnBookmark(getActivity(), mArticleBean.getArticleId()).subscribe(boole -> {
                isExistInBookmark(mArticleBean.getArticleId());
            }, throwable -> {
                Log.i("", "");
            }));
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(getActivity(), "Action", "Details : Removed Read Later : " + mArticleBean.getArticleId(), THP_DetailFragment.class.getSimpleName());
            CleverTapUtil.cleverTapBookmarkFavLike(getActivity(), mArticleId, mFrom, "NetConstants.BOOKMARK_NO");
        }
        else {
            premium_updateBookmarkFavLike(getActivity(), mArticleBean, "bookmark");
        }
    }

    @Override
    public void onFavClickListener(ToolbarCallModel toolbarCallModel) {
        premium_updateBookmarkFavLike(getActivity(), mArticleBean, "favourite");
    }

    @Override
    public void onLikeClickListener(ToolbarCallModel toolbarCallModel) {
        premium_updateBookmarkFavLike(getActivity(), mArticleBean, "dislike");
    }

    @Override
    public void onOverflowClickListener(ToolbarCallModel toolbarCallModel) {

    }

    ///////////////////////////////////////////////////////////////////////////////////
    ////////////////// Start For Premium Bookmark, Fav, Like & Dislike ////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    private void premium_updateBookmarkFavLike(final Context context, ArticleBean bean, String from) {

        bean.setGroupType(NetConstants.G_BOOKMARK_PREMIUM);

        if(!BaseAcitivityTHP.sIsOnline) {
            noConnectionSnackBar(getView());
            return;
        }

        int bookmark = bean.getIsBookmark();
        int favourite = bean.getIsFavourite();
        if(from.equals("bookmark")) {
            if(bean.getIsBookmark() == NetConstants.BOOKMARK_YES) {
                bookmark = NetConstants.BOOKMARK_NO;
            }
            else {
                bookmark = NetConstants.BOOKMARK_YES;
            }
        }
        else if(from.equals("favourite")) {
            if(bean.getIsFavourite() == NetConstants.LIKE_NEUTRAL) {
                favourite = NetConstants.LIKE_YES;
            }
            else if(bean.getIsFavourite() == NetConstants.LIKE_NO) {
                favourite = NetConstants.LIKE_YES;
            }
            else {
                favourite = NetConstants.LIKE_NEUTRAL;
            }
        }
        else if(from.equals("dislike")) {
            if(bean.getIsFavourite() == NetConstants.LIKE_NO) {
                favourite = NetConstants.LIKE_NEUTRAL;
            }
            else if(bean.getIsFavourite() == NetConstants.LIKE_NEUTRAL) {
                favourite = NetConstants.LIKE_NO;
            }
            else if(bean.getIsFavourite() == NetConstants.LIKE_YES) {
                favourite = NetConstants.LIKE_NO;
            }
        }

        bean.setGroupType(NetConstants.G_BOOKMARK_PREMIUM);

        final int book = bookmark;
        final int fav = favourite;

        mDisposable.add(ApiManager.getUserProfile(getActivity())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    // To Create and Remove at server end
                    mDisposable.add(ApiManager.createBookmarkFavLike(userProfile.getAuthorization(), mUserId, BuildConfig.SITEID, bean.getArticleId(), book, fav)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(val-> {
                                        if (val) {
                                            bean.setIsFavourite(fav);
                                            bean.setIsBookmark(book);
                                            if(from.equals("bookmark")) {
                                                if(book == NetConstants.BOOKMARK_YES) {
                                                    // To Create at App end

                                                    mDisposable.add(ApiManager.createBookmark(context, bean).subscribe(boole -> {
                                                        mActivity.getDetailToolbar().setIsBookmarked((Boolean)boole);
                                                    }));
                                                    Alerts.showToastAtCenter(context, "Added to Read Later");
                                                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", "Details : Added Read Later : " + bean.getArticleId(), THP_DetailFragment.class.getSimpleName());
                                                    CleverTapUtil.cleverTapBookmarkFavLike(context, mArticleId, mFrom, "NetConstants.BOOKMARK_YES");
                                                }
                                                else if(book == NetConstants.BOOKMARK_NO) {
                                                    // To Remove at App end
                                                    mDisposable.add(ApiManager.createUnBookmark(context, bean.getArticleId()).subscribe(boole -> {
                                                        mActivity.getDetailToolbar().setIsBookmarked(!(Boolean)boole);
                                                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", "Details : Removed Read Later : " + bean.getArticleId(), THP_DetailFragment.class.getSimpleName());
                                                        CleverTapUtil.cleverTapBookmarkFavLike(context, mArticleId, mFrom, "NetConstants.BOOKMARK_NO");
                                                    }));
                                                }
                                            }
                                            else if(from.equals("dislike") || from.equals("favourite")) {
                                                if(book == NetConstants.BOOKMARK_YES) {
                                                    // To Update at App end
                                                    mDisposable.add(ApiManager.updateBookmark(context, bean.getArticleId(), fav).subscribe(boole ->
                                                            Log.i("updateBookmark", "true")
                                                    ));
                                                }
                                                // To Update at App end
                                                mDisposable.add(ApiManager.updateLike(context, bean.getArticleId(), fav).subscribe(boole -> {
                                                    // notifyItemChanged(position);
                                                    Log.i("updateLIKE", "true");
                                                    mActivity.getDetailToolbar().isFavOrLike(context, bean, bean.getArticleId());

                                                    if(fav == NetConstants.LIKE_YES) {
                                                        CleverTapUtil.cleverTapBookmarkFavLike(context, mArticleId, mFrom, "NetConstants.LIKE_YES");
                                                        Alerts.showToastAtCenter(context, "You will see more stories like this.");
                                                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", "Details : Favourite Added: " + bean.getArticleId(), THP_DetailFragment.class.getSimpleName());
                                                    }
                                                    else if(fav == NetConstants.LIKE_NO) {
                                                        CleverTapUtil.cleverTapBookmarkFavLike(context, mArticleId, mFrom, "NetConstants.LIKE_NO");
                                                        Alerts.showToastAtCenter(context, "Show fewer stories like this.");
                                                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", "Details : Dislike Article : " + bean.getArticleId(), THP_DetailFragment.class.getSimpleName());
                                                    }
                                                }));
                                            }

                                        }
                                        else {
                                            // notifyItemChanged(position);
                                        }
                                    },
                                    val-> {
                                        if(getActivity() != null && getView() != null) {
                                            mActivity.getDetailToolbar().isFavOrLike(context, bean, bean.getArticleId());
                                            isExistInBookmark(bean.getArticleId());
                                            Alerts.showSnackbar(getActivity(), getActivity().getResources().getString(R.string.something_went_wrong));
                                        }
                                    }
                            ));
                        }));


    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////End For Premium Bookmark, Fav, Like & Dislike ////////////////////////
    ///////////////////////////////////////////////////////////////////////////////


    @Override
    public void onFontSizeClickListener(ToolbarCallModel toolbarCallModel) {
        final int currentSize = DefaultPref.getInstance(getActivity()).getDescriptionSize();
        switch (currentSize) {
            case THPConstants.DESCRIPTION_SMALL:
                DefaultPref.getInstance(getActivity()).setDescriptionSize(THPConstants.DESCRIPTION_NORMAL);
                break;

            case THPConstants.DESCRIPTION_NORMAL:
                DefaultPref.getInstance(getActivity()).setDescriptionSize(THPConstants.DESCRIPTION_LARGE);
                break;

            case THPConstants.DESCRIPTION_LARGE:
                DefaultPref.getInstance(getActivity()).setDescriptionSize(THPConstants.DESCRIPTION_LARGEST);
                break;

            case THPConstants.DESCRIPTION_LARGEST:
                DefaultPref.getInstance(getActivity()).setDescriptionSize(THPConstants.DESCRIPTION_SMALL);
                break;
        }

        updateDescriptionTextSize();
    }

    @Override
    public void onCommentClickListener(ToolbarCallModel toolbarCallModel) {
        IntentUtil.openCommentActivity(getActivity(), mArticleBean);
    }

    @Override
    public void onTTSPlayClickListener(ToolbarCallModel toolbarCallModel) {
        TTSManager ttsManager2 = TTSManager.getInstance();
        if(ttsManager2.isTTSInitialized()) {
            ttsManager2.speakSpeech(mArticleBean);
            return;
        }
        ttsManager2.init(getActivity(),  new TTSCallbacks() {
            @Override
            public boolean onTTSInitialized() {
                TTSManager.getInstance().speakSpeech(mArticleBean);
                return false;
            }

            @Override
            public boolean isPlaying() {
                return false;
            }

            @Override
            public void onTTSError(int errorCode) {
                switch (errorCode) {
                    case TextToSpeech.LANG_MISSING_DATA:
                        Alerts.showToastAtCenter(getActivity(), getString(R.string.language_not_available));
                        break;
                    case TextToSpeech.LANG_NOT_SUPPORTED:
                        Alerts.showToastAtCenter(getActivity(), getString(R.string.language_not_available));
                        break;
                    case 1000:
                    case 1001:
                        Alerts.showToastAtCenter(getActivity(), getString(R.string.language_missing_data));
                        IntentUtil.installVoiceData(getActivity());
                        break;

                }
                mActivity.getDetailToolbar().showTTSPlayView(DefaultPref.getInstance(getActivity()).isLanguageSupportTTS());
            }

            @Override
            public void onTTSPlayStarted(int loopCount) {
                if(loopCount == 0) {
                    mActivity.getDetailToolbar().showTTSPauseView(DefaultPref.getInstance(getActivity()).isLanguageSupportTTS());
                }
            }
        });
    }

    @Override
    public void onTTSStopClickListener(ToolbarCallModel toolbarCallModel) {
        TTSManager.getInstance().stopTTS();
        mActivity.getDetailToolbar().showTTSPlayView(DefaultPref.getInstance(getActivity()).isLanguageSupportTTS());
    }

    /**
     * Updates Description WebView Text Size
     */
    private void updateDescriptionTextSize() {
        if(mRecyclerAdapter != null) {
            AppTabContentModel description_1 = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_RESTRICTED_DESCRIPTION_WEBVIEW, RowIds.rowId_description_1());
            AppTabContentModel description_2 = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_RESTRICTED_DESCRIPTION_WEBVIEW, RowIds.rowId_description_2());

            int index_1 = mRecyclerAdapter.indexOf(description_1);
            int index_2 = mRecyclerAdapter.indexOf(description_2);

            if(index_1 != -1) {
                mRecyclerAdapter.notifyItemChanged(index_1);
            }
            if(index_2 != -1) {
                mRecyclerAdapter.notifyItemChanged(index_2);
            }

        }
    }


    /**
     * Checks, Visible Article is bookmarked or not.
     * @param aid
     */
    private void isExistInBookmark(String aid) {
        if(isBriefingDetail()) {
            return;
        }
        mDisposable.add(
                ApiManager.isExistInBookmark(getActivity(), aid)
                .subscribe(
                        recoBean-> {
                            mActivity.getDetailToolbar().setIsBookmarked(recoBean.getIsBookmark()==1);
                            if(mArticleBean != null) {
                                mArticleBean.setIsBookmark(recoBean.getIsBookmark());
                            }
                        },
                        throwable->{
                            Log.i("", "");
                        })
        );
    }

    /**
     * Checking Visible Article is Like and Fav or not.
     */
    private void isFavOrLike() {
        mActivity.getDetailToolbar().isFavOrLike(getActivity(), mArticleBean, mArticleId);
    }

    private void showCommentCount() {
        mActivity.getDetailToolbar().showCommentCount(getActivity(), mArticleId);
    }

    private boolean isBriefingDetail() {
        return mFrom != null && ((NetConstants.BREIFING_ALL.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_EVENING.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_NOON.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_MORNING.equalsIgnoreCase(mFrom)));
    }



    private void sendEventCapture(long pageStartTime, long pageEndTime) {
        if(mArticleBean != null) {
            // Torcai Event Capture
            ApiManager.detailEventCapture(ResUtil.getOsReleaseVersion(), ResUtil.getOsVersion(),
                    mArticleBean.getArticleSection(), mUserPhone, mUserEmail, mArticleBean.getArticletitle(),
                    mUserId, ""+System.currentTimeMillis(), mArticleId, mArticleBean.getArticleSection(),
                    ResUtil.getDeviceId(getActivity()), mArticleBean.getArticleUrl(), BuildConfig.SITEID);

            final String totalTime = AppDateUtil.millisToMinAndSec(pageEndTime - pageStartTime);

            // CleverTap Event Capture
            CleverTapUtil.cleverTapDetailPageEvent(getActivity(), isBriefingDetail(), THPConstants.CT_FROM_TH_PREMIUM, mFrom,
                    mArticleBean.getArticleId(), mArticleBean.getArticletitle(), mArticleBean.getArticleLink(),
                    mArticleBean.getSectionName(), mArticleBean.getArticleType(), totalTime);

            CleverTapUtil.cleverTapEventPageVisit(getContext(), THPConstants.CT_PAGE_TYPE_ARTICLE, mArticleBean.getArticleId(),
                    mArticleBean.getSectionName(), mArticleBean.getAuthor() == null ? (ResUtil.isEmpty(mArticleBean.getAu()) ? "No Author" : mArticleBean.getAu()) : TextUtils.join(", ", mArticleBean.getAuthor()), 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPageEndTime == -1 || mPageStartTime == -1) {
            return;
        }
        if (mPageStartTime > 0) {
            mPageEndTime = System.currentTimeMillis();
            sendEventCapture(mPageStartTime, mPageEndTime);
        }
    }

    /**
     * Creates Premium Detail Page
     * @param bean
     */
    private void premiumPage(ArticleBean bean) {
        AppTabContentModel bannerModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_PREMIUM_DETAIL_IMAGE_BANNER);
        bannerModel.setBean(bean);
        mRecyclerAdapter.addData(bannerModel);

        AppTabContentModel descriptionModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_PREMIUM_DETAIL_DESCRIPTION_WEBVIEW);
        descriptionModel.setBean(bean);
        mRecyclerAdapter.addData(descriptionModel);

        mArticleBean = bean;

        mPullToRefreshLayout.hideProgressBar();
    }

    /**
     * Creates Default Group Detail Page
     * @param bean
     */
    private void dgPage(ArticleBean bean) {
        if(!ResUtil.isEmpty(bean.getArticleType()) && bean.getArticleType().equalsIgnoreCase(THPConstants.ARTICLE_TYPE_PHOTO)) {
            /*ArrayList<MeBean> mediaBeans = bean.getMe();
            for(MeBean media : mediaBeans) {
                AppTabContentModel meModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_THD_PHOTO_VIEW, media.getIm_v2());
                meModel.setMeBean(media);
                mRecyclerAdapter.addData(meModel);
            }
            return;*/
            AppTabContentModel bannerModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_THD_PHOTO_VIEW, RowIds.rowId_photoModel());
            bannerModel.setBean(bean);
            mRecyclerAdapter.addData(bannerModel);
        } else if (!ResUtil.isEmpty(bean.getArticleType()) && bean.getArticleType().equalsIgnoreCase(THPConstants.ARTICLE_TYPE_VIDEO)) {
            AppTabContentModel bannerModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_DETAIL_VIDEO_PLAYER, RowIds.rowId_videoModel());
            bannerModel.setBean(bean);
            mRecyclerAdapter.addData(bannerModel);
        } else if (!ResUtil.isEmpty(bean.getArticleType()) && bean.getArticleType().equalsIgnoreCase(THPConstants.ARTICLE_TYPE_AUDIO)) {
            AppTabContentModel bannerModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_DETAIL_AUDIO_PLAYER, RowIds.rowId_audioModel());
            bannerModel.setBean(bean);
            mRecyclerAdapter.addData(bannerModel);
        } else {
            AppTabContentModel bannerModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_IMAGE_BANNER, RowIds.rowId_bannerModel());
            bannerModel.setBean(bean);
            mRecyclerAdapter.addData(bannerModel);
        }

        AppTabContentModel description_1Model = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_DESCRIPTION_WEBVIEW, RowIds.rowId_description_1());
        description_1Model.setBean(mArticleBean);
        mRecyclerAdapter.addData(description_1Model);

        AppTabContentModel description_2Model = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_DESCRIPTION_WEBVIEW, RowIds.rowId_description_2());
        description_2Model.setBean(mArticleBean);
        mRecyclerAdapter.addData(description_2Model);

        AppTabContentModel postCommentBtnModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_POST_COMMENT_BTN_VIEW, RowIds.rowId_postCommentBtn());
        postCommentBtnModel.setBean(bean);
        mRecyclerAdapter.addData(postCommentBtnModel);

        AppTabContentModel taboolaModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_TABOOLA_WIDGET, RowIds.rowId_taboolaModel());
        taboolaModel.setBean(bean);
        mRecyclerAdapter.addData(taboolaModel);

        loadAdvertisiment();

        mPullToRefreshLayout.hideProgressBar();
    }


    private void dgRestrictedPage(ArticleBean bean) {
        AppTabContentModel bannerModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_IMAGE_BANNER, RowIds.rowId_bannerModel());
        bannerModel.setBean(bean);
        mRecyclerAdapter.addData(bannerModel);

        AppTabContentModel description_restricted = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_RESTRICTED_DESCRIPTION_WEBVIEW, RowIds.rowId_description_1());
        description_restricted.setBean(bean);
        mRecyclerAdapter.addData(description_restricted);

        AppTabContentModel taboolaModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_TABOOLA_WIDGET, RowIds.rowId_taboolaModel());
        taboolaModel.setBean(bean);
        mRecyclerAdapter.addData(taboolaModel);

        mPullToRefreshLayout.hideProgressBar();
    }


    private void loadAdvertisiment() {
        if(!NetUtils.isConnected(getActivity())) {
            return;
        }

        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration == null) {
            return;
        }

        ArrayList<AdData> mAdsData = new ArrayList<>();
        ArrayList<String> adsId = new ArrayList<>();
        adsId.add(tableConfiguration.getAds().getDetailPageTopAdId());
        adsId.add(tableConfiguration.getAds().getDetailPageBottomAdId());

        for(int i=0; i<adsId.size(); i++) {
            int index = 0;
            if(i == 0) {
                index = 2;
            }
            else if(i == 1) {
                index = 4;
            }

            AdData adData = new AdData(index, adsId.get(i));
            adData.setAdSize(AdSize.MEDIUM_RECTANGLE);
            mAdsData.add(adData);
        }

        DFPAds DFPAds = new DFPAds(mAdsData, this);
        DFPAds.listingAds();
    }


    @Override
    public void onDFPAdLoadSuccess(AdData adData) {
        AppTabContentModel item = new AppTabContentModel(BaseRecyclerViewAdapter.VT_THD_300X250_ADS, RowIds.adDataUiqueId(adData.getIndex(), adData.getAdId()));
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
    public void onAdClose() {

    }


}
