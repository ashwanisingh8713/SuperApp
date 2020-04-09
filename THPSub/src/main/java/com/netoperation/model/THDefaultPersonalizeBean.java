package com.netoperation.model;

public class THDefaultPersonalizeBean {
    /**
     * secId : 12
     * homePriority : 5
     * overridePriority : 0
     * secName : National
     * type : GN
     */

    private int secId;
    private int homePriority;
    private int overridePriority;
    private String secName;
    private String type;

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
}
