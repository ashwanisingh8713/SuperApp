package com.netoperation.config.model;

public class Breadcrumb {

    private ColorOptionBean text;
    private ColorOptionBean bg;
    private ColorOptionBean indicator;

    public ColorOptionBean getText() {
        return text;
    }

    public void setText(ColorOptionBean text) {
        this.text = text;
    }

    public ColorOptionBean getBg() {
        return bg;
    }

    public void setBg(ColorOptionBean bg) {
        this.bg = bg;
    }

    public ColorOptionBean getIndicator() {
        return indicator;
    }

    public void setIndicator(ColorOptionBean indicator) {
        this.indicator = indicator;
    }
}
