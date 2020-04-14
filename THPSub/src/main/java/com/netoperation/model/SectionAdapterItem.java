package com.netoperation.model;

import androidx.annotation.Nullable;

import com.ns.adapter.WidgetAdapter;

public class SectionAdapterItem {

    private int viewType;
    private ArticleBean articleBean;
    private String ADID_300X250;
    private String itemRowId;
    private WidgetAdapter widgetAdapter;
    private boolean isItemRowId;

    public SectionAdapterItem(int viewType, String itemRowId) {
        this.viewType = viewType;
        this.itemRowId = itemRowId;
    }

    public boolean isItemRowId() {
        return isItemRowId;
    }

    public void setItemRowId(boolean itemRowId) {
        isItemRowId = itemRowId;
    }

    public int getViewType() {
        return viewType;
    }

    public ArticleBean getArticleBean() {
        return articleBean;
    }

    public void setArticleBean(ArticleBean articleBean) {
        this.articleBean = articleBean;
    }

    public String getADID_300X250() {
        return ADID_300X250;
    }

    public void setADID_300X250(String ADID_300X250) {
        this.ADID_300X250 = ADID_300X250;
    }

    public String getItemRowId() {
        return itemRowId;
    }

    public void setItemRowId(String itemRowId) {
        this.itemRowId = itemRowId;
    }

    public WidgetAdapter getWidgetAdapter() {
        return widgetAdapter;
    }

    public void setWidgetAdapter(WidgetAdapter widgetAdapter) {
        this.widgetAdapter = widgetAdapter;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        super.equals(obj);
        if(obj != null && obj instanceof SectionAdapterItem) {
            SectionAdapterItem item = (SectionAdapterItem) obj;
            if(item.isItemRowId()) {
                return item.itemRowId.equals(itemRowId);
            }
            else {
                return item.viewType==viewType;
            }

        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return itemRowId.hashCode();
    }
}
