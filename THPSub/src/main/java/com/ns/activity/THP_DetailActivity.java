package com.ns.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.main.AppAds;
import com.netoperation.db.DaoMP;
import com.netoperation.db.THPDB;
import com.netoperation.default_db.DaoMPReadArticle;
import com.netoperation.default_db.TableMPReadArticle;
import com.netoperation.default_db.TableSection;
import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.alerts.Alerts;
import com.ns.contentfragment.THP_DetailFragment;
import com.ns.contentfragment.THP_DetailPagerFragment;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
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


    @Override
    public int layoutRes() {
        return R.layout.activity_detail_thp;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
            mFrom = getIntent().getStringExtra("from");
            url = getIntent().getStringExtra("url");
            clickedPosition = getIntent().getIntExtra("clickedPosition", 0);
            articleId = getIntent().getStringExtra("articleId");
            mArticleBean = getIntent().getParcelableExtra("ArticleBean");
        }

        if (mFrom != null && ((NetConstants.BREIFING_ALL.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_EVENING.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_NOON.equalsIgnoreCase(mFrom))
                || (NetConstants.BREIFING_MORNING.equalsIgnoreCase(mFrom)))) {
            getDetailToolbar().showBreifingDetailIcons();
        }
        else if (mFrom != null && NetConstants.RECO_bookmarks.equalsIgnoreCase(mFrom)) {
            getDetailToolbar().showBookmarkPremiumDetailIcons(true);
        }
        // (mFrom.equals(NetConstants.GROUP_DEFAULT_SECTIONS) || mFrom.equals(NetConstants.GROUP_DEFAULT_BOOKMARK))
        else {
            ApiManager.getUserProfile(this)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(userProfile -> {
                        boolean hasSubscriptionPlan = userProfile.isHasSubscribedPlan();
                        getDetailToolbar().showNonPremiumDetailIcons(hasSubscriptionPlan);

                    });
        }


        if(mArticleBean != null) {
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

        // To Create Full Screen Ad
        Observable.just("")
                .subscribeOn(Schedulers.newThread())
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val->{
                    new AppAds().loadFullScreenAds();
                });

        createAndShowBannerAds();

        EventBus.getDefault().register(this);

        subsCloseImg_Mp = findViewById(R.id.subsCloseImg_Mp);
        msgCountTxt_Mp = findViewById(R.id.msgCountTxt_Mp);
        subscribeBtn_Txt_Mp = findViewById(R.id.subscribeBtn_Txt_Mp);
        subscribeLayout_Mp = findViewById(R.id.subscribeLayout_Mp);

    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "THP_DetailActivity Screen", THP_DetailActivity.class.getSimpleName());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        new AppAds().showFullScreenAds();
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(TableMPReadArticle mpReadArticle) {
        if(!mpReadArticle.isUserCanReRead() && mpReadArticle.isArticleRestricted()) {
            subscribeLayout_Mp.setVisibility(View.GONE);
            return;
        }
        int totalReadSize = mpReadArticle.getTotalReadCount();
        boolean isNeedToShowMpBanner = true;
        //Calculate Time Difference, check if exceeds
        long startTimeInMillis = DefaultPref.getInstance(THP_DetailActivity.this).getMPStartTimeInMillis();
        if ((totalReadSize >= BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this)) || (startTimeInMillis < 0 && DefaultPref.getInstance(THP_DetailActivity.this).isMPDurationExpired())) {
            isNeedToShowMpBanner = false;
        }
        String bannerMsg = new String(BaseFragmentTHP.getMpBannerMsg());
        if (bannerMsg != null) {
            bannerMsg = bannerMsg.replaceAll("<readCount>", "" + totalReadSize);
            bannerMsg = bannerMsg.replaceAll("<totalCount>", "" + BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this));
            bannerMsg = bannerMsg + (TextUtils.isEmpty(BaseFragmentTHP.getDurationUnit()) ? "" : " for " + BaseFragmentTHP.getDurationUnit());
        } else {
            //You have read 0 articles out of 0 free articles for 0 days/months
            bannerMsg = "You have read "+totalReadSize+" articles out of "+BaseFragmentTHP.getAllowedCount(THP_DetailActivity.this)+" free articles";
            bannerMsg = bannerMsg + (TextUtils.isEmpty(BaseFragmentTHP.getDurationUnit()) ? "" : " for " + BaseFragmentTHP.getDurationUnit());
        }

        if(isNeedToShowMpBanner) {
            msgCountTxt_Mp.setText(bannerMsg);
            subscribeLayout_Mp.setVisibility(View.VISIBLE);
        }
        else {
            subscribeLayout_Mp.setVisibility(View.GONE);
        }
    }


}
