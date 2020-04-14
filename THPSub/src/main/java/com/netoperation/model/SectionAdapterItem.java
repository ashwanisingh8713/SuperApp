package com.netoperation.model;

import androidx.annotation.Nullable;

import com.ns.adapter.ExploreAdapter;
import com.ns.adapter.WidgetAdapter;

public class SectionAdapterItem {

    private int viewType;
    private ArticleBean articleBean;
    private String ADID_300X250;
    private String itemRowId;
    private WidgetAdapter widgetAdapter;
    private ExploreAdapter exploreAdapter;
    private StaticPageUrlBean staticPageUrlBean;



    public SectionAdapterItem(int viewType, String itemRowId) {
        this.viewType = viewType;
        this.itemRowId = itemRowId;
    }

    public ExploreAdapter getExploreAdapter() {
        return exploreAdapter;
    }

    public void setExploreAdapter(ExploreAdapter exploreAdapter) {
        this.exploreAdapter = exploreAdapter;
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

    public StaticPageUrlBean getStaticPageUrlBean() {
        return staticPageUrlBean;
    }

    public void setStaticPageUrlBean(StaticPageUrlBean staticPageUrlBean) {
        this.staticPageUrlBean = staticPageUrlBean;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        super.equals(obj);
        if(obj != null && obj instanceof SectionAdapterItem) {
            SectionAdapterItem item = (SectionAdapterItem) obj;
            return item.itemRowId.equals(itemRowId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return itemRowId.hashCode();
    }
}
