package com.netoperation.config.model;

public class ColorOptionBean {

    /**
     * dark : #313131
     * light : #ffffff
     */

    private String dark = "";
    private String light = "";
    private String darkSelected;
    private String lightSelected = "";

    public String getDark() {
        return dark;
    }

    public void setDark(String dark) {
        this.dark = dark;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getDarkSelected() {
        return darkSelected;
    }

    public void setDarkSelected(String darkSelected) {
        this.darkSelected = darkSelected;
    }

    public String getLightSelected() {
        return lightSelected;
    }

    public void setLightSelected(String lightSelected) {
        this.lightSelected = lightSelected;
    }
}
