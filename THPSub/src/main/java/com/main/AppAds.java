package com.main;

import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.netoperation.model.AdData;

import java.util.ArrayList;
import java.util.Arrays;

public class AppAds {

    private final String TAG = AppAds.class.getName();

    public static String firstInline = "/22390678/Hindu_Android_HP_Middle";
    public static String secondInline = "/22390678/Hindu_Android_HP_Footer";
    public static String thirdInline = "/22390678/Hindu_Android_HP_Bottom";

    /*public static String firstInline = "ca-app-pub-3940256099942544/1033173712";
    public static String secondInline = "ca-app-pub-3940256099942544/1033173712";
    public static String thirdInline = "ca-app-pub-3940256099942544/1033173712";*/

    public static String firstNative = "/22390678/Hindu_Android_Native01";
    public static String secondNative = "/22390678/Hindu_Android_Native02";
    public static String thirdNative = "/22390678/Hindu_Android_Native03";

    private ArrayList<AdData> mAdsData;

    private int mLoadedCount;

//    private PublisherAdView publisherAdView;

    public interface OnAppAdLoadListener {
        void opnAppAdLoadSuccess(AdData adData);
        void opnAppAdLoadFailure(AdData adData);
    }

    private OnAppAdLoadListener mOnAppAdLoadListener;

    public AppAds(ArrayList<AdData> adsData, OnAppAdLoadListener onAppAdLoadListener) {
        this.mAdsData = adsData;
        mOnAppAdLoadListener = onAppAdLoadListener;
        // To Add test devices to show Ads
        RequestConfiguration.Builder requestConfiguration = MobileAds.getRequestConfiguration().toBuilder();
        requestConfiguration.setTestDeviceIds(Arrays.asList("69A27258C3736E220C92E889FD41FB39"));
        requestConfiguration.build();
    }

    public void loadAds() {
        if (mAdsData != null && mAdsData.size() > mLoadedCount) {
            final AdData adData = mAdsData.get(mLoadedCount);

            final PublisherAdView publisherAdView = new PublisherAdView(SuperApp.getAppContext());
            publisherAdView.setAdUnitId(adData.getAdId());
            publisherAdView.setAdSizes(adData.getAdSize());

            publisherAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Log.i(TAG, "onAdLoaded() :: " + adData.toString());
                    mLoadedCount++;
                    adData.setAdView(publisherAdView);
                    if(mOnAppAdLoadListener != null) {
                        mOnAppAdLoadListener.opnAppAdLoadSuccess(adData);
                    }
                    loadAds();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    loadAds();
                    Log.i(TAG, "onAdFailedToLoad() :: " + adData.toString());
                    if(mOnAppAdLoadListener != null) {
                        mOnAppAdLoadListener.opnAppAdLoadSuccess(adData);
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
                }
            });

            // Create an ad request.
            PublisherAdRequest.Builder publisherAdRequestBuilder = new PublisherAdRequest.Builder();

            // Start loading the ad.
            publisherAdView.loadAd(publisherAdRequestBuilder.build());


        }

    }



}
