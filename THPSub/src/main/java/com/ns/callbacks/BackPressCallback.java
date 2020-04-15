package com.ns.callbacks;


public class BackPressCallback {
    private String from;
    private boolean isPopBack;
    private int tabIndex;

    public BackPressCallback(String from, boolean isPopBack, int tabIndex) {
        this.from = from;
        this.isPopBack = isPopBack;
        this.tabIndex = tabIndex;
    }

    public String getFrom() {
        return from;
    }

    public boolean isPopBack() {
        return isPopBack;
    }

    public int getTabIndex() {
        return tabIndex;
    }
}
