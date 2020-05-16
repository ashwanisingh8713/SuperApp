package com.netoperation.config.model;

public class ContentUrl {

    private String baseUrlPremium;
    private String baseUrlDefault;
    private String newsDigest;

    public String getBaseUrlPremium() {
        return baseUrlPremium;
    }

    public void setBaseUrlPremium(String baseUrlPremium) {
        this.baseUrlPremium = baseUrlPremium;
    }

    public String getBaseUrlDefault() {
        return baseUrlDefault;
    }

    public void setBaseUrlDefault(String baseUrlDefault) {
        this.baseUrlDefault = baseUrlDefault;
    }

    public String getNewsDigest() {
        return newsDigest;
    }

    public void setNewsDigest(String newsDigest) {
        this.newsDigest = newsDigest;
    }
}
