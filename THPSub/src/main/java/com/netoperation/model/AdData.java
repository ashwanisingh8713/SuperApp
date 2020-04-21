package com.netoperation.model;



import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

public class AdData {
    private int index;
    private AdSize adSize;
    private String adId;
    private String adDataUiqueId;
    private PublisherAdView adView;
    private boolean reloadOnScroll;

    public AdData(int index, AdSize adSize, String adId, boolean reloadOnScroll) {
        this.index = index;
        this.adDataUiqueId = createAdDataUiqueId(index, adId);
        this.adSize = adSize;
        this.adId = adId;
        this.reloadOnScroll = reloadOnScroll;
    }

    public boolean isReloadOnScroll() {
        return reloadOnScroll;
    }

    public String createAdDataUiqueId(int index, String adId) {
     return adId+"_"+"_"+index;
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

    @Override
    public String toString() {
        super.toString();
        return createAdDataUiqueId(getIndex(), getAdId());
    }

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
