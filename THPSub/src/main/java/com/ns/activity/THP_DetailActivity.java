package com.ns.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.main.AppAds;
import com.netoperation.model.AdData;
import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.ns.clevertap.CleverTapUtil;
import com.ns.contentfragment.THP_DetailFragment;
import com.ns.contentfragment.THP_DetailPagerFragment;
import com.ns.thpremium.R;
import com.ns.utils.FragmentUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.text.CustomTextView;

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

        meteredPaywallReadArticleCountUpdate();
        createAndShowBannerAds();

    }

    @Override
    protected void onResume() {
        super.onResume();
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(this, "THP_DetailActivity Screen", THP_DetailActivity.class.getSimpleName());
    }

    @Override
    protected void onDestroy() {
        new AppAds().showFullScreenAds();
        super.onDestroy();
    }


    private void meteredPaywallReadArticleCountUpdate() {

        subsCloseImg_Mp = findViewById(R.id.subsCloseImg_Mp);
        msgCountTxt_Mp = findViewById(R.id.msgCountTxt_Mp);
        subscribeBtn_Txt_Mp = findViewById(R.id.subscribeBtn_Txt_Mp);

        subsCloseImg_Mp.setOnClickListener(v->{

        });

        subscribeBtn_Txt_Mp.setOnClickListener(v->{

        });

        mDisposable.add(DefaultTHApiManager.readArticleCount(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(valueMap->{

                    boolean isAllowedToRead = Boolean.parseBoolean("" + valueMap.get("isAllowedToRead"));
                    if (!isAllowedToRead) {
                        //hideMeteredPaywallCounterBanner();
                    }
                    if (valueMap.get("bannerMsg") != null)
                        msgCountTxt_Mp.setText("" + valueMap.get("bannerMsg"));
                    else
                        msgCountTxt_Mp.setText("You have read this article");
                    //CleverTap and Firebase Events Start
                    int articleCount = (int) valueMap.get("articleCount");
                    /*if (articleCount > 0 ) {
                        String cycleName = (String) valueMap.get("cycleName");
                        int allowedArticleCounts = (int) valueMap.get("allowedArticleCounts");
                        //Article Count
                        AppFirebaseAnalytics.firebaseMP_ArticleCount(AdsBaseActivity.this,cycleName,articleCount, allowedArticleCounts);
                        CleverTapUtil.cleverTapMP_ArticleCount(AdsBaseActivity.this,cycleName, articleCount, allowedArticleCounts);
                        //Metered Paywall
                        AppFirebaseAnalytics.firebaseMetered_Paywall(AdsBaseActivity.this,cycleName,articleCount, allowedArticleCounts);
                        CleverTapUtil.cleverTapMetered_Paywall(AdsBaseActivity.this,cycleName, articleCount, allowedArticleCounts);
                    }*/
                }));
    }

}
