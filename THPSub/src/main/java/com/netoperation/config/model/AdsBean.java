package com.netoperation.config.model;

import com.netoperation.model.AdData;

import java.util.List;

public class AdsBean {
    /**
     * bottomAdHomeId : /22390678/Hindu_Android_HP_Sticky
     * bottomAdOtherId : /22390678/Hindu_Android_AT_Sticky
     * fullScreenAdId : /22390678/Hindu_Android_Interstitial
     * detailPageTopAdId : /22390678/Hindu_Android_AT_Middle
     * detailPageBottomAdId : /22390678/Hindu_Android_AT_Bottom
     * listingPageAds : {"ads":[{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Middle","index":"4"},{"type":"taboola","index":"6"},{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Footer","index":"8"},{"type":"taboola","index":"10"},{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Bottom","index":"12"},{"type":"taboola","index":"13"},{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Middle","index":"16"},{"type":"taboola","index":"18"},{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Middle","index":"20"}]}
     */

    private String bottomAdHomeId;
    private String bottomAdOtherId;
    private String fullScreenAdId;
    private String detailPageTopAdId;
    private String detailPageBottomAdId;
    private List<AdData> listingPageAds;

    public String getBottomAdHomeId() {
        return bottomAdHomeId;
    }

    public void setBottomAdHomeId(String bottomAdHomeId) {
        this.bottomAdHomeId = bottomAdHomeId;
    }

    public String getBottomAdOtherId() {
        return bottomAdOtherId;
    }

    public void setBottomAdOtherId(String bottomAdOtherId) {
        this.bottomAdOtherId = bottomAdOtherId;
    }

    public String getFullScreenAdId() {
        return fullScreenAdId;
    }

    public void setFullScreenAdId(String fullScreenAdId) {
        this.fullScreenAdId = fullScreenAdId;
    }

    public String getDetailPageTopAdId() {
        return detailPageTopAdId;
    }

    public void setDetailPageTopAdId(String detailPageTopAdId) {
        this.detailPageTopAdId = detailPageTopAdId;
    }

    public String getDetailPageBottomAdId() {
        return detailPageBottomAdId;
    }

    public void setDetailPageBottomAdId(String detailPageBottomAdId) {
        this.detailPageBottomAdId = detailPageBottomAdId;
    }

    public List<AdData> getListingPageAds() {
        return listingPageAds;
    }

    public void setListingPageAds(List<AdData> listingPageAds) {
        this.listingPageAds = listingPageAds;
    }


}