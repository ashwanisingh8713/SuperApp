package com.main;

import android.os.Bundle;
import android.util.Log;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.ads.mediation.facebook.FacebookAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.netoperation.model.AdData;

import java.util.List;

public class AdsBase {

    private final String TAG = AdsBase.class.getName();

    protected OnDFPAdLoadListener mOnDFPAdLoadListener;

    private static Bundle nonPersonalizedAdsReqBundle;

    public interface OnTaboolaAdLoadListener {
        void onTaboolaAdLoadSuccess(AdData adData);
        void onTaboolaAdLoadFailure(AdData adData);
    }

    protected OnTaboolaAdLoadListener mOnTaboolaAdLoadListener;

    public void setOnTaboolaAdLoadListener(OnTaboolaAdLoadListener mOnTaboolaAdLoadListener) {
        this.mOnTaboolaAdLoadListener = mOnTaboolaAdLoadListener;
    }

    public interface OnDFPAdLoadListener {
        void onDFPAdLoadSuccess(AdData adData);
        void onDFPAdLoadFailure(AdData adData);
        void onAdClose();
    }


    public void setOnDFPAdLoadListener(OnDFPAdLoadListener onDFPAdLoadListener) {
        this.mOnDFPAdLoadListener = onDFPAdLoadListener;
    }

    /**
     * Create an Ad request.
     * @return
     */
    protected PublisherAdRequest appAdRequest() {
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
    public AdListener appAdListener(AdData adData, OnDFPAdLoadListener inAdLoadListener) {
        return new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(inAdLoadListener != null) {
                    inAdLoadListener.onDFPAdLoadSuccess(adData);
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if(inAdLoadListener != null) {
                    inAdLoadListener.onDFPAdLoadSuccess(adData);
                }
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if(inAdLoadListener != null) {
                    inAdLoadListener.onDFPAdLoadSuccess(adData);
                }
            }
        };

    }
}
