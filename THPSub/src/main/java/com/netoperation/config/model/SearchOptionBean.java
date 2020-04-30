package com.netoperation.config.model;

import java.util.List;

public class SearchOptionBean {
    /**
     * url : value
     * types : ["article","sensex"]
     */

    private String urlId;
    private String urlText;
    private List<String> types;

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}