package com.netoperation.model;

public class SelectedPrefModel {
    private int sPId;
    private int userId;
    private String siteId;
    private PreferencesModel preferences;
    private String status;

    public int getsPId() {
        return sPId;
    }

    public void setsPId(int sPId) {
        this.sPId = sPId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public PreferencesModel getPreferences() {
        return preferences;
    }

    public void setModel(PreferencesModel preferences) {
        this.preferences = preferences;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
