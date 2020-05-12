package com.main;

import android.util.Log;
import android.util.SparseIntArray;

import com.netoperation.model.AdData;
import com.netoperation.util.PremiumPref;
import com.ns.thpremium.R;
import com.taboola.android.api.TBPlacement;
import com.taboola.android.api.TBPlacementRequest;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.api.TBRecommendationRequestCallback;
import com.taboola.android.api.TBRecommendationsRequest;
import com.taboola.android.api.TBRecommendationsResponse;
import com.taboola.android.api.TaboolaApi;

import java.util.List;

public class TaboolaAds {

    private TBPlacement mTbPlacement;
    private List<AdData> taboolaAdsBeans;
    private int loadedCount;
    private SparseIntArray mAddedPositionList = new SparseIntArray();

    public interface OnTaboolaAdLoadListener {
        void onTaboolaAdLoadSuccess(AdData adData);
        void onTaboolaAdLoadFailure(AdData adData);
    }

    private OnTaboolaAdLoadListener mOnTaboolaAdLoadListener;

    public TaboolaAds(OnTaboolaAdLoadListener mOnTaboolaAdLoadListener, List<AdData> taboolaAdsBeans) {
        this.mOnTaboolaAdLoadListener = mOnTaboolaAdLoadListener;
        this.taboolaAdsBeans = taboolaAdsBeans;
    }

    public void initAndLoadRecommendationsBatch() {
        if(PremiumPref.getInstance(SuperApp.getAppContext()).isUserAdsFree()) {
            return;
        }
        if(taboolaAdsBeans == null || taboolaAdsBeans.size()<=loadedCount) {
            return;
        }
        AdData adData = taboolaAdsBeans.get(loadedCount);
        if(isAlreadyExecuted(adData.getIndex())) {
            loadedCount++;
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
                        loadedCount++;
                        addSuccessPosition(adData.getIndex());
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
            if(taboolaAdsBeans == null || taboolaAdsBeans.size()<=loadedCount) {
                return;
            }
            AdData adData = taboolaAdsBeans.get(loadedCount);
            if(isAlreadyExecuted(adData.getIndex())) {
                loadedCount++;
                loadNextRecommendationsBatch();
                return;
            }
            TaboolaApi.getInstance().getNextBatchForPlacement(mTbPlacement, new TBRecommendationRequestCallback() {
                @Override
                public void onRecommendationsFetched(TBRecommendationsResponse response) {
                    mTbPlacement = response.getPlacementsMap().values().iterator().next();
                    if (mTbPlacement != null && mTbPlacement.getItems() != null && !mTbPlacement.getItems().isEmpty()) {
                        if (mOnTaboolaAdLoadListener != null) {
                            loadedCount++;
                            addSuccessPosition(adData.getIndex());
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

    public void addSuccessPosition(int adIndex) {
        mAddedPositionList.put(adIndex, 1);
    }

    public boolean isAlreadyExecuted(int adIndex) {
        return 1==mAddedPositionList.get(adIndex, 0);
    }


}
