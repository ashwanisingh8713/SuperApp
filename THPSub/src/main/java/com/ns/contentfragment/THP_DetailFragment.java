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

import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;
import com.netoperation.util.UserPref;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.activity.THP_DetailActivity;
import com.ns.adapter.AppTabContentAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.FragmentTools;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.AppTabContentModel;
import com.ns.model.ToolbarCallModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.tts.TTSCallbacks;
import com.ns.tts.TTSManager;
import com.ns.utils.CommonUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.RecyclerViewPullToRefresh;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class THP_DetailFragment extends BaseFragmentTHP implements RecyclerViewPullToRefresh.TryAgainBtnClickListener, FragmentTools {

    private RecyclerViewPullToRefresh mPullToRefreshLayout;
    private AppTabContentAdapter mRecyclerAdapter;
    private ArticleBean mArticleBean;
    private String mArticleId;
    private String mFrom;

    private long mPageStartTime = 0l;
    private long mPageEndTime = 0l;


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

        mRecyclerAdapter = new AppTabContentAdapter(new ArrayList<>(), mFrom, mUserId, mPullToRefreshLayout.getRecyclerView());

        mPullToRefreshLayout.setDataAdapter(mRecyclerAdapter);

        mPullToRefreshLayout.setTryAgainBtnClickListener(this:: tryAgainBtnClick);

        mPullToRefreshLayout.enablePullToRefresh(false);

        // Reading User Profile Data
        readUserData();

        // Pull To Refresh Listener
        registerPullToRefresh();

        if(mFrom.equals(NetConstants.GROUP_PREMIUM_SECTIONS)) {
            if (mArticleBean.getHasDescription() == 0) {
                loadDataFromServer();
                AppTabContentModel bannerModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_PREMIUM_DETAIL_IMAGE_BANNER);
                bannerModel.setBean(mArticleBean);
                mRecyclerAdapter.addData(bannerModel);

                AppTabContentModel descriptionModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_PREMIUM_DETAIL_DESCRIPTION_WEBVIEW);
                descriptionModel.setBean(mArticleBean);
                mRecyclerAdapter.addData(descriptionModel);

            } else {
                loadDataFromDB();
            }
        }
        else if(mFrom.equals(NetConstants.GROUP_DEFAULT_SECTIONS) || mFrom.equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
            AppTabContentModel bannerModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_IMAGE_BANNER);
            bannerModel.setBean(mArticleBean);
            AppTabContentModel description_1Model = new AppTabContentModel(BaseRecyclerViewAdapter.VT_GROUP_DEFAULT_DETAIL_DESCRIPTION_WEBVIEW);
            description_1Model.setBean(mArticleBean);
            mRecyclerAdapter.addData(bannerModel);
            mRecyclerAdapter.addData(description_1Model);

            AppTabContentModel taboolaModel = new AppTabContentModel(BaseRecyclerViewAdapter.VT_TABOOLA);
            taboolaModel.setBean(mArticleBean);
            mRecyclerAdapter.addData(taboolaModel);

        }

        if(mActivity != null && mIsVisible) {
            mPageStartTime = System.currentTimeMillis();
            // Set Toolbar Item Click Listener
            mActivity.setOnFragmentTools(this);

            // Checking Visible Article is bookmarked or not.
            isExistInBookmark(mArticleId);
            // Checking Visible Article is Like and Fav or not.
            isFavOrLike();

        }


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(mActivity != null && mIsVisible) {
            mPageStartTime = System.currentTimeMillis();
            // Set Toolbar Item Click Listener
            mActivity.setOnFragmentTools(this);

            // Checking Visible Article is bookmarked or not.
            isExistInBookmark(mArticleBean.getArticleId());

            // Checking Visible Article is Like and Fav or not.
            isFavOrLike();

            //sendEventCapture();
        }

        if(!isVisibleToUser) {
            if(mPageStartTime > 1000) {
                mPageEndTime = System.currentTimeMillis();
                sendEventCapture(mPageStartTime, mPageEndTime);
                mPageStartTime = -1l;
                mPageEndTime = -1l;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Adding Pull To Refresh Listener
     */
    private void registerPullToRefresh() {
        mPullToRefreshLayout.getSwipeRefreshLayout().setOnRefreshListener(()->{
            if(!mIsOnline) {
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

    private void loadDataFromServer() {

        String SEARCH_BY_ARTICLE_ID_URL = "";
        if(BuildConfig.IS_PRODUCTION) {
            SEARCH_BY_ARTICLE_ID_URL = BuildConfig.PRODUCTION_SEARCH_BY_ARTICLE_ID_URL;
        } else {
            SEARCH_BY_ARTICLE_ID_URL = BuildConfig.STATGGING_SEARCH_BY_ARTICLE_ID_URL;
        }

        Observable<ArticleBean> observable =  ApiManager.articleDetailFromServer(getActivity(), mArticleId, SEARCH_BY_ARTICLE_ID_URL, mFrom);
        mDisposable.add(
                observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(recoBean->{
                            mRecyclerAdapter.replaceData(recoBean, 0);
                            mRecyclerAdapter.replaceData(recoBean, 1);
                            mArticleBean = recoBean;
                            if(mIsVisible) {
                                //sendEventCapture();
                            }
                        },
                        throwable->{
                            Log.i("", "");
                        })
        );
    }

    private void loadDataFromDB() {
        Observable<ArticleBean> observable =  ApiManager.articleDetailFromPremiumDB(getActivity(), mArticleId, mFrom);
        mDisposable.add(
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(recoBean->{
                                    mRecyclerAdapter.replaceData(recoBean, 0);
                                    mRecyclerAdapter.replaceData(recoBean, 1);
                                    mArticleBean = recoBean;
                                    if(mIsVisible) {
                                        //sendEventCapture();
                                    }
                                },
                                throwable->{
                                    Log.i("", "");
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
        if(mFrom.equals(NetConstants.GROUP_DEFAULT_SECTIONS) || mFrom.equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
            mArticleBean.setGroupType(NetConstants.GROUP_DEFAULT_BOOKMARK);
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
            updateBookmarkFavLike(getActivity(), mArticleBean, "bookmark");
        }
    }

    @Override
    public void onRemoveBookmarkClickListener(ToolbarCallModel toolbarCallModel) {
        if(mFrom.equals(NetConstants.GROUP_DEFAULT_SECTIONS)  || mFrom.equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
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
            updateBookmarkFavLike(getActivity(), mArticleBean, "bookmark");
        }
    }

    @Override
    public void onFavClickListener(ToolbarCallModel toolbarCallModel) {
        updateBookmarkFavLike(getActivity(), mArticleBean, "favourite");
    }

    @Override
    public void onLikeClickListener(ToolbarCallModel toolbarCallModel) {
        updateBookmarkFavLike(getActivity(), mArticleBean, "dislike");
    }

    @Override
    public void onOverflowClickListener(ToolbarCallModel toolbarCallModel) {

    }

    ///////////////////////////////////////////////////////////////////////////////////
    ////////////////// Start For Bookmark, Fav, Like & Dislike ////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    private void updateBookmarkFavLike(final Context context, ArticleBean bean, String from) {

        if(!mIsOnline) {
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

        bean.setGroupType(NetConstants.GROUP_PREMIUM_BOOKMARK);

        final int book = bookmark;
        final int fav = favourite;

        // To Create and Remove at server end
        mDisposable.add(ApiManager.createBookmarkFavLike(mUserId, BuildConfig.SITEID, bean.getArticleId(), bookmark, favourite)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val-> {
                            if (val) {
                                bean.setIsFavourite(fav);
                                bean.setIsBookmark(book);
                                if(from.equals("bookmark")) {
                                    if(book == NetConstants.BOOKMARK_YES) {
                                        // To Create at App end
                                        bean.setGroupType(NetConstants.GROUP_PREMIUM_BOOKMARK);

                                        ApiManager.createBookmark(context, bean).subscribe(boole -> {
                                            mActivity.getDetailToolbar().setIsBookmarked((Boolean)boole);
                                        });
                                        Alerts.showToastAtCenter(context, "Added to Read Later");
                                        THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", "Details : Added Read Later : " + bean.getArticleId(), THP_DetailFragment.class.getSimpleName());
                                        CleverTapUtil.cleverTapBookmarkFavLike(context, mArticleId, mFrom, "NetConstants.BOOKMARK_YES");
                                    }
                                    else if(book == NetConstants.BOOKMARK_NO) {
                                        // To Remove at App end
                                        ApiManager.createUnBookmark(context, bean.getArticleId()).subscribe(boole -> {
                                            mActivity.getDetailToolbar().setIsBookmarked(!(Boolean)boole);
                                            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", "Details : Removed Read Later : " + bean.getArticleId(), THP_DetailFragment.class.getSimpleName());
                                            CleverTapUtil.cleverTapBookmarkFavLike(context, mArticleId, mFrom, "NetConstants.BOOKMARK_NO");
                                        });
                                    }
                                }
                                else if(from.equals("dislike") || from.equals("favourite")) {
                                    if(book == NetConstants.BOOKMARK_YES) {
                                        // To Update at App end
                                        ApiManager.updateBookmark(context, bean.getArticleId(), fav).subscribe(boole ->
                                                Log.i("updateBookmark", "true")
                                        );
                                    }
                                    // To Update at App end
                                    ApiManager.updateLike(context, bean.getArticleId(), fav).subscribe(boole -> {
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
                                    });
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
                                /*Alerts.showAlertDialogOKBtn(getActivity(),
                                        getActivity().getResources().getString(R.string.failed_to_connect),
                                        getActivity().getResources().getString(R.string.please_check_ur_connectivity));*/
                                Alerts.showSnackbar(getActivity(), getActivity().getResources().getString(R.string.something_went_wrong));
                            }
                        }
                ));
    }

    ////////////////////////////////////////////////////////////////////////////////
    //////////////////End For Bookmark, Fav, Like & Dislike ////////////////////////
    ///////////////////////////////////////////////////////////////////////////////






    @Override
    public void onFontSizeClickListener(ToolbarCallModel toolbarCallModel) {
        final int currentSize = UserPref.getInstance(getActivity()).getDescriptionSize();
        switch (currentSize) {
            case THPConstants.DESCRIPTION_SMALL:
                UserPref.getInstance(getActivity()).setDescriptionSize(THPConstants.DESCRIPTION_NORMAL);
                break;

            case THPConstants.DESCRIPTION_NORMAL:
                UserPref.getInstance(getActivity()).setDescriptionSize(THPConstants.DESCRIPTION_LARGE);
                break;

            case THPConstants.DESCRIPTION_LARGE:
                UserPref.getInstance(getActivity()).setDescriptionSize(THPConstants.DESCRIPTION_LARGEST);
                break;

            case THPConstants.DESCRIPTION_LARGEST:
                UserPref.getInstance(getActivity()).setDescriptionSize(THPConstants.DESCRIPTION_SMALL);
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
                mActivity.getDetailToolbar().showTTSPlayView(UserPref.getInstance(getActivity()).isLanguageSupportTTS());
            }

            @Override
            public void onTTSPlayStarted(int loopCount) {
                if(loopCount == 0) {
                    mActivity.getDetailToolbar().showTTSPauseView(UserPref.getInstance(getActivity()).isLanguageSupportTTS());
                }
            }
        });
    }

    @Override
    public void onTTSStopClickListener(ToolbarCallModel toolbarCallModel) {
        TTSManager.getInstance().stopTTS();
        mActivity.getDetailToolbar().showTTSPlayView(UserPref.getInstance(getActivity()).isLanguageSupportTTS());
    }

    /**
     * Updates Description WebView Text Size
     */
    private void updateDescriptionTextSize() {
        if(mRecyclerAdapter != null) {
            int descriptionSize = UserPref.getInstance(getActivity()).getDescriptionSize();
            if(mRecyclerAdapter.getLastDescriptionTextSize() != descriptionSize) {
                mRecyclerAdapter.notifyItemChanged(mRecyclerAdapter.getDescriptionItemPosition());
            }
        }
    }


    /**
     * Checks, Visible Article is bookmarked or not.
     * @param aid
     */
    private void isExistInBookmark(String aid) {
        if(isBriefingDetail() || isTempArticle()) {
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
        if(isBriefingDetail() || isTempArticle() || isBookmarkDetailPage()) {
            return;
        }
        mActivity.getDetailToolbar().isFavOrLike(getActivity(), mArticleBean, mArticleId);

    }

    private boolean isBriefingDetail() {
        return mFrom != null && ((NetConstants.BREIFING_ALL.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_EVENING.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_NOON.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_MORNING.equalsIgnoreCase(mFrom)));
    }

    private boolean isTempArticle() {
        /*if(mFrom != null && (NetConstants.RECO_TEMP_NOT_EXIST.equalsIgnoreCase(mFrom))) {
            return true;
        }*/
        return false;
    }

    private boolean isBookmarkDetailPage() {
        return mFrom != null && (NetConstants.RECO_bookmarks.equalsIgnoreCase(mFrom));
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
                    Integer.parseInt(mArticleBean.getArticleId()), mArticleBean.getArticletitle(), mArticleBean.getArticleLink(),
                    mArticleBean.getSectionName(), mArticleBean.getArticleType(), totalTime);

            CleverTapUtil.cleverTapEventPageVisit(getContext(), THPConstants.CT_PAGE_TYPE_ARTICLE, Integer.parseInt(mArticleBean.getArticleId()),
                    mArticleBean.getSectionName(), mArticleBean.getAuthor() == null ? "No Author" : TextUtils.join(", ", mArticleBean.getAuthor()), 1);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPageEndTime == -1 || mPageStartTime == -1) {
            return;
        }
        mPageEndTime = System.currentTimeMillis();
        sendEventCapture(mPageStartTime, mPageEndTime);
    }

}
