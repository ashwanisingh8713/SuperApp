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
    private String position;
    private int sectionId;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(String lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }
}
