package com.netoperation.config.model;

public class TaboolaBean {
    /**
     * publisher : value
     * pageType : value
     * mode : value
     * placementDark : value
     * placementLight : value
     * taboolaNativeAd : {"taboola_placement_home":"value","taboola_placement_home_source_type":"value","taboola_placement_section":"value","taboola_placement_section_source_type":"value","taboola_placement_detail":"value"}
     */

    private String publisher;
    private String pageType;
    private String mode;
    private String placementDark;
    private String placementLight;
    private TaboolaNativeAdBean taboolaNativeAd;

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPlacementDark() {
        return placementDark;
    }

    public void setPlacementDark(String placementDark) {
        this.placementDark = placementDark;
    }

    public String getPlacementLight() {
        return placementLight;
    }

    public void setPlacementLight(String placementLight) {
        this.placementLight = placementLight;
    }

    public TaboolaNativeAdBean getTaboolaNativeAd() {
        return taboolaNativeAd;
    }

    public void setTaboolaNativeAd(TaboolaNativeAdBean taboolaNativeAd) {
        this.taboolaNativeAd = taboolaNativeAd;
    }


}