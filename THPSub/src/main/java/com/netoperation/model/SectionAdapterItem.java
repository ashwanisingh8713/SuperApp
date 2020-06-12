package com.netoperation.model;

import androidx.annotation.Nullable;

import com.ns.adapter.BL_WidgetAdapter;
import com.ns.adapter.ExploreAdapter;
import com.ns.adapter.SuWidgetRecyclerAdapter;
import com.ns.adapter.TH_WidgetAdapter;
import com.ns.model.SensexData;

public class SectionAdapterItem {

    private final int viewType;
    private final String itemRowId;
    private ArticleBean articleBean;
    private TH_WidgetAdapter THWidgetAdapter;
    private BL_WidgetAdapter BLWidgetAdapter;
    private ExploreAdapter exploreAdapter;
    private StaticPageUrlBean staticPageUrlBean;
    private AdData adData;
    private SensexData sensexData;
    private SuWidgetRecyclerAdapter suWidgetRecyclerAdapter;


    public SectionAdapterItem(int viewType, String itemRowId) {
        this.viewType = viewType;
        this.itemRowId = itemRowId;
    }

    public SuWidgetRecyclerAdapter getSuWidgetRecyclerAdapter() {
        return suWidgetRecyclerAdapter;
    }

    public void setSuWidgetRecyclerAdapter(SuWidgetRecyclerAdapter suWidgetRecyclerAdapter) {
        this.suWidgetRecyclerAdapter = suWidgetRecyclerAdapter;
    }

    public SensexData getSensexData() {
        return sensexData;
    }

    public void setSensexData(SensexData sensexData) {
        this.sensexData = sensexData;
    }

    public AdData getAdData() {
        return adData;
    }

    public void setAdData(AdData adData) {
        this.adData = adData;
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

    public String getItemRowId() {
        return itemRowId;
    }

    public TH_WidgetAdapter getTHWidgetAdapter() {
        return THWidgetAdapter;
    }

    public void setTHWidgetAdapter(TH_WidgetAdapter THWidgetAdapter) {
        this.THWidgetAdapter = THWidgetAdapter;
    }

    public BL_WidgetAdapter getBLWidgetAdapter() {
        return BLWidgetAdapter;
    }

    public void setBLWidgetAdapter(BL_WidgetAdapter BLWidgetAdapter) {
        this.BLWidgetAdapter = BLWidgetAdapter;
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
