package com.netoperation.model;



import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.taboola.android.api.TBRecommendationItem;

public class AdData {
    private String type;
    private String adId;
    private int index;

    private AdSize adSize;
    private String adDataUiqueId;
    private PublisherAdView adView;
    private boolean reloadOnScroll;

    private String secId;
    private TBRecommendationItem taboolaNativeAdItem;

    public AdData(int index, String adId) {
        this.index = index;
        this.adId = adId;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setReloadOnScroll(boolean reloadOnScroll) {
        this.reloadOnScroll = reloadOnScroll;
    }

    public TBRecommendationItem getTaboolaNativeAdItem() {
        return taboolaNativeAdItem;
    }

    public void setTaboolaNativeAdItem(TBRecommendationItem taboolaNativeAdItem) {
        this.taboolaNativeAdItem = taboolaNativeAdItem;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public void setAdSize(AdSize adSize) {
        this.adSize = adSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReloadOnScroll() {
        return reloadOnScroll;
    }



    public void setAdDataUiqueId(String adDataUiqueId) {
        this.adDataUiqueId = adDataUiqueId;
    }

    public String getAdDataUiqueId() {
        return adDataUiqueId;
    }

    public int getIndex() {
        return index;
    }

    public AdSize getAdSize() {
        return adSize;
    }

    public String getAdId() {
        return adId;
    }


    public PublisherAdView getAdView() {
        return adView;
    }

    public void setAdView(PublisherAdView adView) {
        this.adView = adView;
    }

    /*@Override
    public String toString() {
        super.toString();
        return createAdDataUiqueId(getIndex(), getAdId());
    }*/

    @Override
    public boolean equals( Object obj) {
        super.equals(obj);
        if(obj != null && obj instanceof AdData) {
            AdData adData = (AdData) obj;
            return adData.adDataUiqueId.equals(adDataUiqueId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return adDataUiqueId.hashCode();
    }
}
