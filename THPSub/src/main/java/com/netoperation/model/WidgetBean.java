package com.netoperation.model;

public class WidgetBean {
    /**
     * parentSecId : 0
     * secId : 142
     * homePriority : 2
     * overridePriority : 0
     * secName : News In Quotes
     * type : GN
     * viewAllCTA : true
     */

    private int parentSecId;
    private int secId;
    private int homePriority;
    private int overridePriority;
    private String secName;
    private String type;
    private boolean viewAllCTA;

    public int getParentSecId() {
        return parentSecId;
    }

    public void setParentSecId(int parentSecId) {
        this.parentSecId = parentSecId;
    }

    public int getSecId() {
        return secId;
    }

    public void setSecId(int secId) {
        this.secId = secId;
    }

    public int getHomePriority() {
        return homePriority;
    }

    public void setHomePriority(int homePriority) {
        this.homePriority = homePriority;
    }

    public int getOverridePriority() {
        return overridePriority;
    }

    public void setOverridePriority(int overridePriority) {
        this.overridePriority = overridePriority;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isViewAllCTA() {
        return viewAllCTA;
    }

    public void setViewAllCTA(boolean viewAllCTA) {
        this.viewAllCTA = viewAllCTA;
    }
}
