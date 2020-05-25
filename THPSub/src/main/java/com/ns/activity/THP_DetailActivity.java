package com.ns.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.main.DFPAds;
import com.netoperation.default_db.TableMPReadArticle;
import com.netoperation.model.ArticleBean;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.ns.alerts.Alerts;
import com.ns.callbacks.ToolbarChangeRequired;
import com.ns.clevertap.CleverTapUtil;
import com.ns.contentfragment.THP_DetailFragment;
import com.ns.contentfragment.THP_DetailPagerFragment;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class THP_DetailActivity extends BaseAcitivityTHP {

    String mFrom;
    private int clickedPosition;
    private String articleId;
    private String url;
    private ArticleBean mArticleBean;
    private CustomTextView msgCountTxt_Mp;
    private CustomTextView subscribeBtn_Txt_Mp;
    private ImageView subsCloseImg_Mp;
    private ConstraintLayout subscribeLayout_Mp;
    private String mReadingArticleId;


    @Override
    public int layoutRes() {
        return R.layout.activity_detail_thp;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("TabFragment", "onCreate() In THP_DetailActivity EventBus Registered");
//        EventBus.getDefault().register(this);

        subsCloseImg_Mp = findViewById(R.id.subsCloseImg_Mp);
        msgCountTxt_Mp = findViewById(R.id.msgCountTxt_Mp);
        subscribeBtn_Txt_Mp = findViewById(R.id.subscribeBtn_Txt_Mp);
        subscribeLayout_Mp = findViewById(R.id.subscribeLayout_Mp);

        if (getIntent().getExtras() != null) {
            mFrom = getIntent().getStringExtra("from");
            url = getIntent().getStringExtra("url");
            clickedPosition = getIntent().getIntExtra("clickedPosition", 0);
            articleId = getIntent().getStringExtra("articleId");
            mArticleBean = getIntent().getParcelableExtra("ArticleBean");
        }


        // Breifing Detail Page Toolbar
        if (mFrom != null && ((NetConstants.BREIFING_ALL.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_EVENING.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_NOON.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_MORNING.equalsIgnoreCase(mFrom)))) {
            if(PremiumPref.getInstance(this).isHasSubscription()) {
                getDetailToolbar().BREIFING_DETAIL_TOPBAR();
            } else {
                getDetailToolbar().BREIFING_DETAIL_TOPBAR_CROWN();
            }
        }
        // Premium Detail Page Toolbar
        else if (mFrom != null && (((NetConstants.PS_My_Stories.equalsIgnoreCase(mFrom))
                || (NetConstants.PS_Suggested.equalsIgnoreCase(mFrom))))) {
            if(PremiumPref.getInstance(this).isHasSubscription()) {
                getDetailToolbar().PREMIUM_DETAIL_TOPBAR();
            } else {
                getDetailToolbar().PREMIUM_DETAIL_TOPBAR_CROWN();
            }
        }
        // Bookmark Detail Page Toolbar
        else if (ContentUtil.isFromPremiumBookmark(mFrom)) {
            if(PremiumPref.getInstance(this).isHasSubscription()) {
                getDetailToolbar().PREMIUM_BOOKMARK_DETAIL_TOPBAR();
            } else {
                getDetailToolbar().PREMIUM_BOOKMARK_DETAIL_TOPBAR_CROWN();
            }
        }
        else {
            if(PremiumPref.getInstance(this).isHasSubscription()) {
                getDetailToolbar().DEFAULT_DETAIL_TOPBAR();
            }
            else {
                getDetailToolbar().DEFAULT_DETAIL_TOPBAR_CROWN();
            }
        }


        if(mArticleBean != null) {
            DefaultTHApiManager.insertMeteredPaywallArticleId(this, mArticleBean.getArticleId(), mArticleBean.isArticleRestricted(), BaseFragmentTHP.getAllowedCount(this));
            THP_DetailFragment fragment = THP_DetailFragment.getInstance(mArticleBean, mArticleBean.getArticleId(), "", mFrom);
            FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);
        } else {
            String sectionId = getIntent().getStringExtra("sectionId");
            String sectionType = getIntent().getStringExtra("sectionType");
            String sectionOrSubsectionName = getIntent().getStringExtra("sectionOrSubsectionName");
            boolean isSubsection = getIntent().getBooleanExtra("isSubsection", false);
            THP_DetailPagerFragment fragment = THP_DetailPagerFragment.getInstance(mFrom, articleId, sectionId, sectionType, sectionOrSubsectionName, isSubsection);
            FragmentUtil.replaceFragmentAnim(this, R.id.parentLayout, fragment, FragmentUtil.FRAGMENT_NO_ANIMATION, true);
        }


        subscribeBtn_Txt_Mp.setOnClickListener(view -> {
            if(NetUtils.isConnected(THP_DetailActivity.this)) {
                //Redirect on subscription plan page
                IntentUtil.openSubscriptionActivity(THP_DetailActivity.this, THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                THPConstants.IS_FROM_MP_BLOCKER = true;
                //CT and Firebase MP Banner Events
                THPFirebaseAnalytics.firebaseMPBannerSubscribe(THP_DetailActivity.this, BaseFragmentTHP.getCycleName());
                CleverTapUtil.cleverTapMPBannerSubscribe(THP_DetailActivity.this, BaseFragmentTHP.getCycleName());
            } else {
                Alerts.noConnectionSnackBar(subscribeLayout_Mp, THP_DetailActivity.this);
            }
        });
        subsCloseImg_Mp.setOnClickListener(view -> {
            //Save close click for article
            DefaultTHApiManager.insertCloseBannerClick(THP_DetailActivity.this, mReadingArticleId, true);
            subscribeLayout_Mp.setVisibility(View.GONE);
        });


        // To Create Full Screen Ad
        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val->{
                    new DFPAds().loadFullScreenAds();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("TabFragment", "onResume() In THP_DetailActivity EventBus Registered");
        EventBus.getDefault().register(this);
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "THP_DetailActivity Screen", THP_DetailActivity.class.getSimpleName());
        bottomBannerAds();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("TabFragment", "onPause() In THP_DetailActivity EventBus UnRegistered");
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        new DFPAds().showFullScreenAds();
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void handleEvent(ToolbarChangeRequired toolbarChangeRequired) {

        if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.BREIFING_DETAIL_TOPBAR_CROWN)) {
            getDetailToolbar().BREIFING_DETAIL_TOPBAR_CROWN();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.BREIFING_DETAIL_TOPBAR)) {
            getDetailToolbar().BREIFING_DETAIL_TOPBAR();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.PREMIUM_BOOKMARK_DETAIL_TOPBAR_CROWN)) {
            getDetailToolbar().PREMIUM_BOOKMARK_DETAIL_TOPBAR_CROWN();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.PREMIUM_BOOKMARK_DETAIL_TOPBAR)) {
            getDetailToolbar().PREMIUM_BOOKMARK_DETAIL_TOPBAR();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.PREMIUM_DETAIL_TOPBAR_CROWN)) {
            getDetailToolbar().PREMIUM_DETAIL_TOPBAR_CROWN();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.PREMIUM_DETAIL_TOPBAR)) {
            getDetailToolbar().PREMIUM_DETAIL_TOPBAR();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.DEFAULT_RESTRICTED_DETAIL_TOPBAR_CROWN)) {
            getDetailToolbar().DEFAULT_RESTRICTED_DETAIL_TOPBAR_CROWN();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.DEFAULT_DETAIL_TOPBAR_CROWN)) {
            getDetailToolbar().DEFAULT_DETAIL_TOPBAR_CROWN();
        }
        else if(toolbarChangeRequired.getTypeOfToolbar().equals(ToolbarChangeRequired.DEFAULT_DETAIL_TOPBAR)) {
            getDetailToolbar().DEFAULT_DETAIL_TOPBAR();
        }
    }

    /**
     * It receives event from THP_DetailFragment.java => loadRecyclerData() => subscribe(tableMPReadArticle)
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(TableMPReadArticle tableMPReadArticle) {
        if(!ContentUtil.shouldShowMeteredPaywall() || !tableMPReadArticle.isArticleRestricted() || (!tableMPReadArticle.isUserCanReRead() && tableMPReadArticle.isArticleRestricted())) {
            subscribeLayout_Mp.setVisibility(View.GONE);
            return;
        }
        mReadingArticleId = tableMPReadArticle.getArticleId();
        int totalReadSize = tableMPReadArticle.getTotalReadCount();
        boolean isNeedToShowMpBanner = (DefaultPref.getInstance(THP_DetailActivity.this).isMPDurationExpired() && tableMPReadArticle.isUserCanReRead())
                || totalReadSize <= BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this)
                || (totalReadSize > BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this) && tableMPReadArticle.isUserCanReRead());
        String bannerMsg = BaseFragmentTHP.getMpBannerMsg();
        //For Display in UI, total read size should not exceed allowed counts.
        if (totalReadSize > BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this)) {
            totalReadSize = BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this);
        }
        if (bannerMsg != null) {
            bannerMsg = bannerMsg.replaceAll("<readCount>", "" + totalReadSize);
            bannerMsg = bannerMsg.replaceAll("<totalCount>", "" + BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this));
            bannerMsg = bannerMsg + (TextUtils.isEmpty(BaseFragmentTHP.getDurationUnit()) ? "" : " for " + BaseFragmentTHP.getDurationUnit());
        } else {
            //You have read 0 articles out of 0 free articles for 0 days/months
            bannerMsg = "You have read "+totalReadSize+" articles out of "+BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this)+" free articles";
            bannerMsg = bannerMsg + (TextUtils.isEmpty(BaseFragmentTHP.getDurationUnit()) ? "" : " for " + BaseFragmentTHP.getDurationUnit());
        }

        //Check Is Banner close button click TRUE/FALSE
        boolean isBannerCloseClick = tableMPReadArticle.isBannerCloseClick();
        if(isNeedToShowMpBanner && !isBannerCloseClick) {
            msgCountTxt_Mp.setText(bannerMsg);
            subscribeLayout_Mp.setVisibility(View.VISIBLE);
        }
        else {
            subscribeLayout_Mp.setVisibility(View.GONE);
        }
        if (isNeedToShowMpBanner) {
            //Show Menu Icons
            if(PremiumPref.getInstance(this).isHasSubscription()) {
                getDetailToolbar().DEFAULT_DETAIL_TOPBAR();
            }
            else {
                getDetailToolbar().DEFAULT_DETAIL_TOPBAR_CROWN();
            }
        } else {
            //Hide Some Menu Icons
            getDetailToolbar().DEFAULT_RESTRICTED_DETAIL_TOPBAR_CROWN();
        }
    }


}
