package com.main;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.netoperation.model.AdData;

public class AdsBase {

    private final String TAG = AdsBase.class.getName();

    protected OnDFPAdLoadListener mOnDFPAdLoadListener;

    private static Bundle nonPersonalizedAdsReqBundle;

    public interface OnDFPAdLoadListener {
        void onDFPAdLoadSuccess(AdData adData);
        void onDFPAdLoadFailure(AdData adData);
    }

    protected interface InAdLoadListener extends OnDFPAdLoadListener {
        void onAdClose();
    }

    public void setOnAppAdLoadListener(OnDFPAdLoadListener onDFPAdLoadListener) {
        this.mOnDFPAdLoadListener = onDFPAdLoadListener;
    }


    /**
     * Ads Listener
     * @param adData
     * @param inAdLoadListener
     * @return
     */
    public AdListener appAdListener(AdData adData, InAdLoadListener inAdLoadListener) {
        return new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i(TAG, "onAdLoaded() :: " + adData.toString());
                if(inAdLoadListener != null) {
                    inAdLoadListener.onDFPAdLoadSuccess(adData);
                }
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Log.i(TAG, "onAdFailedToLoad() :: " + adData.toString());
                if(inAdLoadListener != null) {
                    inAdLoadListener.onDFPAdLoadSuccess(adData);
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
                    inAdLoadListener.onDFPAdLoadSuccess(adData);
                }
            }
        };

    }
}
