package com.netoperation.config.model;

public class WidgetIndex {

    private String secId;
    private int index;
    private UrlBean background;
    private UrlBean text;

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public UrlBean getBackground() {
        return background;
    }

    public void setBackground(UrlBean background) {
        this.background = background;
    }

    public UrlBean getText() {
        return text;
    }

    public void setText(UrlBean text) {
        this.text = text;
    }
}
