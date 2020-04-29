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

public class AppAds {

    private final String TAG = AppAds.class.getName();

    public static String firstInline = "/22390678/Hindu_Android_HP_Middle";
    public static String secondInline = "/22390678/Hindu_Android_HP_Footer";
    public static String thirdInline = "/22390678/Hindu_Android_HP_Bottom";

    private ArrayList<AdData> mAdsData;

    private int mLoadedCount;

    private static PublisherInterstitialAd mPublisherInterstitialAd;

    public interface OnAppAdLoadListener {
        void onAppAdLoadSuccess(AdData adData);
        void onAppAdLoadFailure(AdData adData);
    }

    private OnAppAdLoadListener mOnAppAdLoadListener;

    private static Bundle nonPersonalizedAdsReqBundle;

    private interface InAdLoadListener extends OnAppAdLoadListener {
        void onAdClose();
    }

    public void setOnAppAdLoadListener(OnAppAdLoadListener onAppAdLoadListener) {
        this.mOnAppAdLoadListener = onAppAdLoadListener;
    }

    /**
     * To Add test devices to show Ads
     */
    private void addTestDevice() {
        RequestConfiguration.Builder requestConfiguration = MobileAds.getRequestConfiguration().toBuilder();
        requestConfiguration.setTestDeviceIds(Arrays.asList("69A27258C3736E220C92E889FD41FB39"));
        requestConfiguration.build();
    }

    public AppAds() {
        addTestDevice();
    }

    public AppAds(ArrayList<AdData> adsData, OnAppAdLoadListener onAppAdLoadListener) {
        this.mAdsData = adsData;
        mOnAppAdLoadListener = onAppAdLoadListener;
        // To Add test devices to show Ads
        addTestDevice();
    }

    /**
     * Loads Listing and Detail Ads
     */
    public void loadAds() {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        if (mAdsData != null && mAdsData.size() > mLoadedCount) {
            final AdData adData = mAdsData.get(mLoadedCount);

            final PublisherAdView publisherAdView = new PublisherAdView(SuperApp.getAppContext());
            publisherAdView.setAdUnitId(adData.getAdId());
            publisherAdView.setAdSizes(adData.getAdSize());

            publisherAdView.setAdListener(appAdListener(adData, new InAdLoadListener() {
                @Override
                public void onAppAdLoadSuccess(AdData adData) {
                    mLoadedCount++;
                    adData.setAdView(publisherAdView);
                    if(mOnAppAdLoadListener != null) {
                        mOnAppAdLoadListener.onAppAdLoadSuccess(adData);
                    }
                    loadAds();
                }

                @Override
                public void onAppAdLoadFailure(AdData adData) {
                    if(mOnAppAdLoadListener != null) {
                        mOnAppAdLoadListener.onAppAdLoadSuccess(adData);
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

    public void createBannerAdRequest(boolean isHomePage) {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        String unitId = "";
        if(isHomePage) {
            unitId = "/22390678/Hindu_Android_HP_Sticky";
        }
        else {
            unitId = "/22390678/Hindu_Android_AT_Sticky";
        }

        AdData adData = new AdData(-1, AdSize.BANNER, unitId, false);

        PublisherAdView mBannerPublisherAdView = new PublisherAdView(SuperApp.getAppContext());
        mBannerPublisherAdView.setAdSizes(adData.getAdSize());
        mBannerPublisherAdView.setAdUnitId(adData.getAdId());

        mBannerPublisherAdView.setAdListener(appAdListener(adData, new InAdLoadListener() {
            @Override
            public void onAdClose() {

            }

            @Override
            public void onAppAdLoadSuccess(AdData adData) {
                if(mOnAppAdLoadListener != null) {
                    adData.setAdView(mBannerPublisherAdView);
                    mOnAppAdLoadListener.onAppAdLoadSuccess(adData);
                }
            }

            @Override
            public void onAppAdLoadFailure(AdData adData) {
                if(mOnAppAdLoadListener != null) {
                    mOnAppAdLoadListener.onAppAdLoadFailure(adData);
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

        AdData adData = new AdData(-1, AdSize.INVALID, dfp_interstitial_ad, false);

        mPublisherInterstitialAd.loadAd(appAdRequest());

        mPublisherInterstitialAd.setAdListener(appAdListener(adData, new InAdLoadListener() {
            @Override
            public void onAdClose() {
                mPublisherInterstitialAd.setAdListener(null);
                mPublisherInterstitialAd = null;
            }

            @Override
            public void onAppAdLoadSuccess(AdData adData) {
                DefaultPref.getInstance(SuperApp.getAppContext()).setIsFullScreenAdLoaded(true);
            }

            @Override
            public void onAppAdLoadFailure(AdData adData) {

            }
        }));


    }

    /**
     * Create an Ad request.
     * @return
     */
    private PublisherAdRequest appAdRequest() {
        if(nonPersonalizedAdsReqBundle == null) {
            nonPersonalizedAdsReqBundle = DFPConsent.GDPRStatusBundle(SuperApp.getAppContext());
        }

        String THE_HINDU_URL = "http://www.thehindu.com";

        PublisherAdRequest request;
        if(nonPersonalizedAdsReqBundle != null) {
            Bundle extras = new FacebookAdapter.FacebookExtrasBundleBuilder()
                    .setNativeAdChoicesIconExpandable(false)
                    .build();
            return new PublisherAdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, nonPersonalizedAdsReqBundle)
                    .addNetworkExtrasBundle(FacebookAdapter.class, extras)
                    .setContentUrl(THE_HINDU_URL).build();

        }
        else {
            return new PublisherAdRequest.Builder().setContentUrl(THE_HINDU_URL).build();

        }
    }

    /**
     * Ads Listener
     * @param adData
     * @param inAdLoadListener
     * @return
     */
    private AdListener appAdListener(AdData adData, InAdLoadListener inAdLoadListener) {
        return new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "onAdLoaded() :: " + adData.toString());
                if(inAdLoadListener != null) {
                    inAdLoadListener.onAppAdLoadSuccess(adData);
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.i(TAG, "onAdFailedToLoad() :: " + adData.toString());
                if(inAdLoadListener != null) {
                    inAdLoadListener.onAppAdLoadSuccess(adData);
                }
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.i(TAG, "onAdImpression() :: " + adData.toString());
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.i(TAG, "onAdLeftApplication() :: " + adData.toString());
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.i(TAG, "onAdOpened() :: " + adData.toString());
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.i(TAG, "onAdClicked() :: " + adData.toString());
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.i(TAG, "onAdClosed() :: " + adData.toString());
                if(inAdLoadListener != null) {
                    inAdLoadListener.onAppAdLoadSuccess(adData);
                }
            }
        };

    }



}
