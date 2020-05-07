package com.ns.model;

public class IndicesSection {
    private int viewType;
    private String sectionName;

    public IndicesSection(int viewType, String sectionName) {
        this.viewType = viewType;
        this.sectionName = sectionName;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
