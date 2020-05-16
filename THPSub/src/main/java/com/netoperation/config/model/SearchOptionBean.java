package com.netoperation.config.model;

import java.util.List;

public class SearchOptionBean {

    private String urlId;
    private String urlText;
    private List<SearchType> searchItem;

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

    public List<SearchType> getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(List<SearchType> searchItem) {
        this.searchItem = searchItem;
    }
}