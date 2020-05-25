package com.main;

import android.os.Bundle;
import android.util.Log;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.netoperation.model.AdData;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.PremiumPref;

import java.util.ArrayList;
import java.util.Arrays;

public class DFPAds extends AdsBase {

    private final String TAG = DFPAds.class.getName();

    public static String firstInline = "/22390678/Hindu_Android_HP_Middle";
    public static String secondInline = "/22390678/Hindu_Android_HP_Footer";
    public static String thirdInline = "/22390678/Hindu_Android_HP_Bottom";

    private ArrayList<AdData> mAdsData;

    private int mLoadedCount;

    private static PublisherInterstitialAd mPublisherInterstitialAd;

    /**
     * To Add test devices to show Ads
     */
    private void addTestDevice() {
        RequestConfiguration.Builder requestConfiguration = MobileAds.getRequestConfiguration().toBuilder();
        requestConfiguration.setTestDeviceIds(Arrays.asList("69A27258C3736E220C92E889FD41FB39"));
        requestConfiguration.build();
    }

    public DFPAds() {
        addTestDevice();
    }

    public DFPAds(ArrayList<AdData> adsData, OnDFPAdLoadListener onDFPAdLoadListener) {
        this.mAdsData = adsData;
        mOnDFPAdLoadListener = onDFPAdLoadListener;
        // To Add test devices to show Ads
        addTestDevice();
    }

    public DFPAds(OnDFPAdLoadListener onDFPAdLoadListener) {
        mOnDFPAdLoadListener = onDFPAdLoadListener;
        // To Add test devices to show Ads
        addTestDevice();
    }

    /**
     * Loads Listing and Detail Ads
     */
    public void listingAds() {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        if (mAdsData != null && mAdsData.size() > mLoadedCount) {
            final AdData adData = mAdsData.get(mLoadedCount);

            final PublisherAdView publisherAdView = new PublisherAdView(SuperApp.getAppContext());
            publisherAdView.setAdUnitId(adData.getAdId());
            publisherAdView.setAdSizes(adData.getAdSize());

            publisherAdView.setAdListener(appAdListener(adData, new OnDFPAdLoadListener() {
                @Override
                public void onDFPAdLoadSuccess(AdData adData) {
                    mLoadedCount++;
                    adData.setAdView(publisherAdView);
                    if(mOnDFPAdLoadListener != null) {
                        mOnDFPAdLoadListener.onDFPAdLoadSuccess(adData);
                    }
                    listingAds();
                }

                @Override
                public void onDFPAdLoadFailure(AdData adData) {
                    if(mOnDFPAdLoadListener != null) {
                        mOnDFPAdLoadListener.onDFPAdLoadSuccess(adData);
                    }
                }

                @Override
                public void onAdClose() {

                }

            }));

            // Start loading the ad.
            publisherAdView.loadAd(appAdRequest());

        }
    }


    public void createBannerAdRequest(boolean isHomePage, String homePageAdId, String otherPageAdId) {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        String unitId = "";
        if(isHomePage) {
            //unitId = "/22390678/Hindu_Android_HP_Sticky";
            unitId = homePageAdId;
        }
        else {
            //unitId = "/22390678/Hindu_Android_AT_Sticky";
            unitId = otherPageAdId;
        }

        AdData adData = new AdData(-1, unitId);
        adData.setAdSize(AdSize.BANNER);

        PublisherAdView mBannerPublisherAdView = new PublisherAdView(SuperApp.getAppContext());
        mBannerPublisherAdView.setAdSizes(adData.getAdSize());
        mBannerPublisherAdView.setAdUnitId(adData.getAdId());

        mBannerPublisherAdView.setAdListener(appAdListener(adData, new OnDFPAdLoadListener() {
            @Override
            public void onAdClose() {

            }

            @Override
            public void onDFPAdLoadSuccess(AdData adData) {
                if(mOnDFPAdLoadListener != null) {
                    adData.setAdView(mBannerPublisherAdView);
                    mOnDFPAdLoadListener.onDFPAdLoadSuccess(adData);
                }
            }

            @Override
            public void onDFPAdLoadFailure(AdData adData) {
                if(mOnDFPAdLoadListener != null) {
                    mOnDFPAdLoadListener.onDFPAdLoadFailure(adData);
                }
            }
        }));

        mBannerPublisherAdView.loadAd(appAdRequest());

    }

    /**
     * Shows Full Scree Ads
     */
    public void showFullScreenAds() {
        if(mPublisherInterstitialAd != null) {
            mPublisherInterstitialAd.show();
            DefaultPref.getInstance(SuperApp.getAppContext()).setInterstetial_Ads_Shown(true);
        }
    }

    /**
     * Loads Full Screen Ads
     */
    public void loadFullScreenAds() {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }

        String dfp_interstitial_ad = "/22390678/Hindu_Android_Interstitial";

        if (DefaultPref.getInstance(SuperApp.getAppContext()).isFullScreenAdLoaded()) {
            if(DefaultPref.getInstance(SuperApp.getAppContext()).getInterstetial_Ads_Shown()) {
                return;
            } else {
                if(mPublisherInterstitialAd != null) {
                    mPublisherInterstitialAd.show();
                    DefaultPref.getInstance(SuperApp.getAppContext()).setInterstetial_Ads_Shown(true);
                }
            }
            return;
        }

        mPublisherInterstitialAd = new PublisherInterstitialAd(SuperApp.getAppContext());
        mPublisherInterstitialAd.setAdUnitId(dfp_interstitial_ad);

        AdData adData = new AdData(-1, dfp_interstitial_ad);
        adData.setAdSize(AdSize.INVALID);

        mPublisherInterstitialAd.loadAd(appAdRequest());

        mPublisherInterstitialAd.setAdListener(appAdListener(adData, new OnDFPAdLoadListener() {
            @Override
            public void onAdClose() {
                mPublisherInterstitialAd.setAdListener(null);
                mPublisherInterstitialAd = null;
            }

            @Override
            public void onDFPAdLoadSuccess(AdData adData) {
                DefaultPref.getInstance(SuperApp.getAppContext()).setIsFullScreenAdLoaded(true);
            }

            @Override
            public void onDFPAdLoadFailure(AdData adData) {

            }
        }));


    }






}
