package com.ns.contentfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoBanner;
import com.netoperation.default_db.DaoSectionArticle;
import com.netoperation.default_db.TableHomeArticle;
import com.netoperation.default_db.TableSectionArticle;
import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.PremiumPref;
import com.ns.activity.THP_DetailActivity;
import com.ns.adapter.DetailPagerAdapter;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.tts.TTSManager;
import com.ns.utils.ContentUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.ViewPagerScroller;

import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class THP_DetailPagerFragment extends BaseFragmentTHP {

    private static String TAG = NetConstants.TAG_UNIQUE;

    private String mSectionId;
    private String mSectionType;
    private String sectionOrSubsectionName;
    private boolean mIsSubsection;

    private String mFrom;
    private int mClickedPosition;
    private String mArticleId;

    private ViewPager mViewPager;
    private DetailPagerAdapter mSectionsPagerAdapter;

    private THP_DetailActivity mActivity;

    private List<ArticleBean> mHomeArticleList;

    /**
     * This is for Non-Premium
     */
    public static THP_DetailPagerFragment getInstance(String from, String articleId, String sectionId, String sectionType, String sectionOrSubsectionName, boolean isSubsection) {
        THP_DetailPagerFragment fragment = new THP_DetailPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        bundle.putString("articleId", articleId);
        bundle.putString("sectionId", sectionId);
        bundle.putString("sectionOrSubsectionName", sectionOrSubsectionName);
        bundle.putString("sectionType", sectionType);
        bundle.putBoolean("isSubsection", isSubsection);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_pager_detail_thp;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mFrom = getArguments().getString("from");
            mArticleId = getArguments().getString("articleId");
            mSectionId = getArguments().getString("sectionId");
            sectionOrSubsectionName = getArguments().getString("sectionOrSubsectionName");
            mSectionType = getArguments().getString("sectionType");
            mIsSubsection = getArguments().getBoolean("isSubsection");

            mUserId = PremiumPref.getInstance(getActivity()).getUserId();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(mFrom.equals(NetConstants.PS_GROUP_DEFAULT_SECTIONS) || mFrom.equals(NetConstants.PS_ADD_ON_SECTION)) {
            if(mIsSubsection) {
                //subSectionDataFromDB();
                sectionDataFromDB();
            } else {
                if(mSectionId.equals(NetConstants.RECO_HOME_TAB)) {
                    homeAndBannerArticleFromDB();
                } else {
                    sectionDataFromDB();
                }
            }
        }
        else if(mFrom.equals(NetConstants.RECO_TEMP_NOT_EXIST)) {
            loadDataFromTemp();
        }
        else if(ContentUtil.isFromBookmarkPage(mFrom)) {
            loadBookmarkData();
        }
        else if(mFrom.equals(NetConstants.G_NOTIFICATION)) {
            loadNotificationData();
        }
        else {
            loadDataFromPremium();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(getActivity(), "Details Screen", THP_DetailPagerFragment.class.getSimpleName());
    }


    /**
     * Premium all articles from DB
     */
    private void loadDataFromPremium() {
        Observable<List<ArticleBean>> observable = null;
        if(mFrom.equalsIgnoreCase(NetConstants.BREIFING_ALL) || mFrom.equalsIgnoreCase(NetConstants.BREIFING_EVENING)
                || mFrom.equalsIgnoreCase(NetConstants.BREIFING_NOON) || mFrom.equalsIgnoreCase(NetConstants.BREIFING_MORNING)) {
            observable = ApiManager.getBreifingFromDB(getActivity(), mFrom);
        }
        else {
            observable = ApiManager.premium_allArticleFromDB(getActivity(), mFrom);
        }

        mDisposable.add(
                observable.map(value->{
                    return value;
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value -> {
                            if(mFrom != null && mFrom.equalsIgnoreCase(NetConstants.RECO_TEMP_NOT_EXIST) && value.size() > 0) {
                                ArrayList<ArticleBean> singleBean = new ArrayList<>();
                                singleBean.add(value.get(0));
                                viewPagerSetup(singleBean, mFrom);
                            }
                            else {
                                viewPagerSetup(value, mFrom);
                            }

                        }, throwable -> {
                            if (throwable instanceof ConnectException
                                    || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
                                // TODO,
                            }

                        }, () -> {

                        }));
    }


    /**
     * Articles from TableTemperoryArticle DB
     */
    private void loadDataFromTemp() {
        Observable<ArticleBean> observable = ApiManager.getFromTemperoryArticle(getActivity(), mArticleId);

        mDisposable.add(
                observable.map(value->{
                    return value;
                })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(articleBean -> {
                            ArrayList<ArticleBean> singleBean = new ArrayList<>();
                            singleBean.add(articleBean);
                            viewPagerSetup(singleBean, mFrom);

                        }, throwable -> {
                            if (throwable instanceof ConnectException
                                    || throwable instanceof SocketTimeoutException || throwable instanceof TimeoutException) {
                                // TODO,
                            }

                        }, () -> {

                        }));
    }


    /**
     * Section all articles from DB
     */
    private void sectionDataFromDB() {
        THPDB thpdb = THPDB.getInstance(getActivity());
        DaoSectionArticle daoSectionArticle = thpdb.daoSectionArticle();
        Maybe<List<TableSectionArticle>> sectionArticlesMaybe = daoSectionArticle.getArticlesMaybe(mSectionId).subscribeOn(Schedulers.io());
        mDisposable.add(sectionArticlesMaybe
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(value -> {
                    if (value == null || value.size() == 0 || value.get(0).getBeans() == null || value.get(0).getBeans().size() == 0) {
                        Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: NO Article in DB");
                    } else {
                        for (TableSectionArticle sectionArticle : value) {
                            if(mHomeArticleList == null) {
                                mHomeArticleList = new ArrayList<>();
                            }
                            mHomeArticleList.addAll(sectionArticle.getBeans());
                        }
                        viewPagerSetup(mHomeArticleList, mFrom);
                        Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: Loaded from DB");
                    }

                }, throwable -> {
                    Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: throwable from DB :: throwable - " + throwable);
                }, () -> {
                    Log.i(TAG, "SECTION :: " + sectionOrSubsectionName + "-" + mSectionId + " :: complete DB");
                }));
    }

    /**
     * Bookmark all articles from DB
     */
    private void loadBookmarkData() {
        Observable<List<ArticleBean>> observable = null;

        if (mFrom != null && mFrom.equals(NetConstants.G_BOOKMARK_PREMIUM)) {
            observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.G_BOOKMARK_PREMIUM);
        }
        else if (mFrom != null && mFrom.equals(NetConstants.G_BOOKMARK_DEFAULT)) {
            observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.G_BOOKMARK_DEFAULT);
        }
        else {
            observable = ApiManager.getBookmarkGroupType(getActivity(), NetConstants.BOOKMARK_IN_ONE);
        }

        mDisposable.add(
                observable
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(value -> {
                            if (value.size() > 0) {
                                viewPagerSetup(value, mFrom);
                            }
                        }, throwable -> {
                            Log.i("", "");
                        }, () -> {
                        }));
    }


    /**
     * Home data from db
     */
    private void homeAndBannerArticleFromDB() {
//        showProgressDialog("");
        DaoBanner daoBanner = THPDB.getInstance(getActivity()).daoBanner();

        mDisposable.add(daoBanner.getBannersSingle().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(banner -> {
                    if (mHomeArticleList == null) {
                        mHomeArticleList = new ArrayList<>();
                    }
                    mHomeArticleList.addAll(banner.getBeans());

                    mDisposable.add(THPDB.getInstance(getActivity()).daoHomeArticle().getArticlesSingle()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(tableDatas->{
                                for (int i = 0; i < tableDatas.size(); i++) {
                                    // Home Articles
                                    if (tableDatas.get(i) instanceof TableHomeArticle) {
                                        TableHomeArticle homeArticle = (TableHomeArticle) tableDatas.get(i);
                                        if (mHomeArticleList == null) {
                                            mHomeArticleList = new ArrayList<>();
                                        }
                                        mHomeArticleList.addAll(homeArticle.getBeans());
                                    }
                                }

                                viewPagerSetup(mHomeArticleList, mFrom);
                                // Setting current position of ViewPager
                                setCurrentPage(mClickedPosition, false);
                                hideProgressDialog();
                                Log.i(TAG, "Detail Pager SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: subscribe - DB");
                            },
                                    throwable -> {
                                        hideProgressDialog();
                                        Log.i(TAG, "Detail Pager SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: throwable - Home Article");
                            }));

                }, throwable -> {
                    hideProgressDialog();
                    Log.i(TAG, "Detail Pager SECTION :: " + mSectionId + "-" + sectionOrSubsectionName + " :: throwable - Banner");
                }));

    }


    private void loadNotificationData() {
        mDisposable.add(DefaultTHApiManager.getNotificationArticles(getActivity()).map(articleBeanList -> {
            if (mHomeArticleList == null) {
                mHomeArticleList = new ArrayList<>();
            }
            mHomeArticleList.addAll(articleBeanList);
            viewPagerSetup(mHomeArticleList, mFrom);
            // Setting current position of ViewPager
            setCurrentPage(mClickedPosition, false);
            hideProgressDialog();
            return "";
        })
         .subscribe(isEmpty -> {
                    Log.i("", "");
            }, throwable -> {
                    Log.i("", "");
            }));
    }


    /**
     * ViewPager Adapter Initialisation and Assiging
     */
    private void viewPagerSetup(List<ArticleBean> articleBeans, String from) {

        mViewPager = getView().findViewById(R.id.viewPager);
        // This is smooth scroll of ViewPager
        smoothPagerScroll();
        mViewPager.setOffscreenPageLimit(4);

        // To Check the selected article Index
        if (mArticleId != null) {
            ArticleBean bean = new ArticleBean();
            bean.setArticleId(mArticleId);

            int index = articleBeans.indexOf(bean);
            if (index != -1) {
                mClickedPosition = index;
                bean = articleBeans.get(mClickedPosition);
                DefaultTHApiManager.insertMeteredPaywallArticleId(getActivity(), mArticleId, articleBeans.get(mClickedPosition).isArticleRestricted(), getAllowedCount(getActivity()));
                //if article bean group type is not empty, then insert article read with group type
                if (bean.getGroupType() != null) {
                    //insert article read with group type
                    DefaultTHApiManager.readArticleId(getActivity(), bean.getArticleId(), bean.getGroupType());
                } else {
                    DefaultTHApiManager.readArticleId(getActivity(), bean.getArticleId());
                }
            }
        }

        mSectionsPagerAdapter = new DetailPagerAdapter(getActivity().getSupportFragmentManager(), articleBeans, mUserId, from);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Setting current position of ViewPager
        setCurrentPage(mClickedPosition, false);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                // It stops TTS if it's playing.
                TTSManager.getInstance().stopTTS();
                // It shows TTS Play view and hides Stop View
                mActivity.getDetailToolbar().showTTSPlayView(DefaultPref.getInstance(getActivity()).isLanguageSupportTTS());
                ArticleBean bean = mSectionsPagerAdapter.getArticleBean(position);
                DefaultTHApiManager.insertMeteredPaywallArticleId(getActivity(), bean.getArticleId(), bean.isArticleRestricted(), getAllowedCount(getActivity()));
                //if article bean group type is not empty, then insert article read with group type
                if (bean.getGroupType() != null) {
                    //insert article read with group type
                    DefaultTHApiManager.readArticleId(getActivity(), bean.getArticleId(), bean.getGroupType());
                } else {
                    DefaultTHApiManager.readArticleId(getActivity(), bean.getArticleId());
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }



    private void setCurrentPage(int position, boolean smoothScroll) {
        mViewPager.setCurrentItem(position, smoothScroll);
    }


    /**
     * This is ViewPager Page Scroll Animation
     */
    private void smoothPagerScroll() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(mViewPager, new ViewPagerScroller(getActivity(),
                    new LinearInterpolator(), 250));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
