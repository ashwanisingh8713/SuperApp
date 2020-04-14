package com.netoperation.model;

public class StaticPageUrlBean {
    /**
     * isEnabled : false
     * url :
     * youtubeId :
     * position : 0
     * sectionId : 0
     * lastUpdatedOn : 2020-02-12 10:40:07
     */

    private boolean isEnabled;
    private String url;
    private String youtubeId;
    private int position;
    private String sectionId;
    private String lastUpdatedOn;

    public boolean isIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
