package com.main;

import android.util.Log;
import android.util.SparseIntArray;

import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.netoperation.model.AdData;
import com.netoperation.util.PremiumPref;
import com.taboola.android.api.TBPlacement;
import com.taboola.android.api.TBPlacementRequest;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.api.TBRecommendationRequestCallback;
import com.taboola.android.api.TBRecommendationsRequest;
import com.taboola.android.api.TBRecommendationsResponse;
import com.taboola.android.api.TaboolaApi;

import java.util.List;

public class SectionListingAds extends AdsBase {

    private int totalLoadedAdCount;
    private int lastAdIndex;

    private int taboolaLoadedCount;
    private int dfpLoadedCount;

    private TBPlacement mTbPlacement;
    private SparseIntArray mAddedPositionList = new SparseIntArray();
    private List<AdData> taboolaAdsBeans;
    private List<AdData> dfpAdsBeans;


    public void createMEDIUM_RECTANGLE() {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        if(dfpLoadedCount>=dfpAdsBeans.size()) {
            dfpLoadedCount = 0;
        }
        AdData adData = dfpAdsBeans.get(dfpLoadedCount);
        adData.createAdDataUiqueId(adData.getIndex(), adData.getAdId());
        final PublisherAdView publisherAdView = new PublisherAdView(SuperApp.getAppContext());
        publisherAdView.setAdUnitId(adData.getAdId());
        publisherAdView.setAdSizes(adData.getAdSize());

        publisherAdView.setAdListener(appAdListener(adData, new InAdLoadListener() {
            @Override
            public void onDFPAdLoadSuccess(AdData adData) {
                totalLoadedAdCount++;
                dfpLoadedCount++;
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



    public void initAndLoadRecommendationsBatch() {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        if(taboolaAdsBeans == null || taboolaAdsBeans.size()<= taboolaLoadedCount) {
            return;
        }
        AdData adData = taboolaAdsBeans.get(taboolaLoadedCount);
        if(isTaboolaAlreadyExecuted(adData.getIndex())) {
            taboolaLoadedCount++;
            loadNextRecommendationsBatch();
            return;
        }
        Log.i("TATA", "Init Sent :: "+adData.getIndex());
        String placementName = "Below Home Stream";
        String sourceType = "home";
        TBPlacementRequest placementRequest = new TBPlacementRequest(placementName, 1);
        TBRecommendationsRequest request = new TBRecommendationsRequest("https://www.thehindu.com", sourceType);
        request.setViewId(""+adData.getIndex());
        request.addPlacementRequest(placementRequest);
        TaboolaApi.getInstance().fetchRecommendations(request, new TBRecommendationRequestCallback() {
            @Override
            public void onRecommendationsFetched(TBRecommendationsResponse response) {
                mTbPlacement = response.getPlacementsMap().get(placementName);
                if (mTbPlacement != null && mTbPlacement.getItems() != null && !mTbPlacement.getItems().isEmpty()) {
                    mTbPlacement.prefetchThumbnails();
                    if (mOnTaboolaAdLoadListener != null) {
                        taboolaLoadedCount++;
                        totalLoadedAdCount++;
                        addTaboolaSuccessPosition(adData.getIndex());
                        TBRecommendationItem item = mTbPlacement.getItems().get(0);
                        adData.createAdDataUiqueId(adData.getIndex(), item.getPublisherId());
                        adData.setTaboolaNativeAdItem(item);
                        mOnTaboolaAdLoadListener.onTaboolaAdLoadSuccess(adData);
                    }
                }
            }

            @Override
            public void onRecommendationsFailed(Throwable throwable) {
                if (mOnTaboolaAdLoadListener != null) {
                    mOnTaboolaAdLoadListener.onTaboolaAdLoadFailure(adData);
                }
            }
        });
    }

    public void loadNextRecommendationsBatch() {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        if (mTbPlacement != null) {
            if(taboolaAdsBeans == null || taboolaAdsBeans.size()<= taboolaLoadedCount) {
                return;
            }
            AdData adData = taboolaAdsBeans.get(taboolaLoadedCount);
            if(isTaboolaAlreadyExecuted(adData.getIndex())) {
                taboolaLoadedCount++;
                loadNextRecommendationsBatch();
                return;
            }
            TaboolaApi.getInstance().getNextBatchForPlacement(mTbPlacement, new TBRecommendationRequestCallback() {
                @Override
                public void onRecommendationsFetched(TBRecommendationsResponse response) {
                    mTbPlacement = response.getPlacementsMap().values().iterator().next();
                    if (mTbPlacement != null && mTbPlacement.getItems() != null && !mTbPlacement.getItems().isEmpty()) {
                        if (mOnTaboolaAdLoadListener != null) {
                            taboolaLoadedCount++;
                            totalLoadedAdCount++;
                            addTaboolaSuccessPosition(adData.getIndex());
                            TBRecommendationItem item = mTbPlacement.getItems().get(0);
                            adData.createAdDataUiqueId(adData.getIndex(), item.getPublisherId());
                            adData.setTaboolaNativeAdItem(item);
                            mOnTaboolaAdLoadListener.onTaboolaAdLoadSuccess(adData);
                        }
                        mTbPlacement.prefetchThumbnails();
                    }
                }

                @Override
                public void onRecommendationsFailed(Throwable throwable) {
                    if (mOnTaboolaAdLoadListener != null) {
                        mOnTaboolaAdLoadListener.onTaboolaAdLoadFailure(adData);
                    }
                }
            });
        }
    }

    private void addTaboolaSuccessPosition(int adIndex) {
        mAddedPositionList.put(adIndex, 1);
    }

    private boolean isTaboolaAlreadyExecuted(int adIndex) {
        return 1==mAddedPositionList.get(adIndex, 0);
    }

    public void setDfpAdsBeans(List<AdData> dfpAdsBeans) {
        this.dfpAdsBeans = dfpAdsBeans;
    }

    public void setTaboolaAdsBeans(List<AdData> taboolaAdsBeans) {
        this.taboolaAdsBeans = taboolaAdsBeans;
    }

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



}
