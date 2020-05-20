package com.main;

import android.os.Bundle;
import android.util.Log;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.netoperation.model.AdData;
import com.netoperation.util.PremiumPref;

public class AppAds extends AdsBase {

    private int totalLoadedAdCount;
    private int lastAdIndex;
    private static Bundle nonPersonalizedAdsReqBundle;

    public int getTotalLoadedAdCount() {
        return totalLoadedAdCount;
    }

    public void setTotalLoadedAdCount(int totalLoadedAdCount) {
        this.totalLoadedAdCount = totalLoadedAdCount;
    }

    public int getLastAdIndex() {
        return lastAdIndex;
    }

    public void setLastAdIndex(int lastAdIndex) {
        this.lastAdIndex = lastAdIndex;
    }

    public void createMEDIUM_RECTANGLE(AdData adData) {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        adData.createAdDataUiqueId(adData.getIndex(), adData.getAdId());
        final PublisherAdView publisherAdView = new PublisherAdView(SuperApp.getAppContext());
        publisherAdView.setAdUnitId(adData.getAdId());
        publisherAdView.setAdSizes(adData.getAdSize());

        publisherAdView.setAdListener(appAdListener(adData, new InAdLoadListener() {
            @Override
            public void onDFPAdLoadSuccess(AdData adData) {
                totalLoadedAdCount++;
                adData.setAdView(publisherAdView);
                if(mOnDFPAdLoadListener != null) {
                    mOnDFPAdLoadListener.onDFPAdLoadSuccess(adData);
                }
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


}
