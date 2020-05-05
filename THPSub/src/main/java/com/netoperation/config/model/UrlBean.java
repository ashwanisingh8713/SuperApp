package com.netoperation.config.model;

import android.webkit.URLUtil;

public class UrlBean {

    /**
     * urlDark : value
     * urlLight : value
     */

    private String title;
    private String urlDark;
    private String urlLight;
    private String urlSelectedDark;
    private String urlSelectedLight;

    private String localFilePath;
    private String localFileSelectedPath;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlDark() {
        return urlDark;
    }

    public void setUrlDark(String urlDark) {
        this.urlDark = urlDark;
    }

    public String getUrlLight() {
        return urlLight;
    }

    public void setUrlLight(String urlLight) {
        this.urlLight = urlLight;
    }

    public String getUrlSelectedDark() {
        return urlSelectedDark;
    }

    public void setUrlSelectedDark(String urlSelectedDark) {
        this.urlSelectedDark = urlSelectedDark;
    }

    public String getUrlSelectedLight() {
        return urlSelectedLight;
    }

    public void setUrlSelectedLight(String urlSelectedLight) {
        this.urlSelectedLight = urlSelectedLight;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getLocalFileSelectedPath() {
        return localFileSelectedPath;
    }

    public void setLocalFileSelectedPath(String localFileSelectedPath) {
        this.localFileSelectedPath = localFileSelectedPath;
    }
}
