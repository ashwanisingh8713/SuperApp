package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.config.model.OtherIconsDownloadUrls;
import com.netoperation.config.model.WidgetIndex;
import com.netoperation.config.model.AdsBean;
import com.netoperation.config.model.AppThemeBean;
import com.netoperation.config.model.SearchOptionBean;
import com.netoperation.config.model.TaboolaBean;
import com.netoperation.config.model.TabsBean;
import com.netoperation.config.model.UrlBean;

import java.util.List;

@Entity(tableName ="TableConfiguration")
public class TableConfiguration {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private String lastServerUpdateTime;
    private String vokkleId;
    private String refreshIntervalInMins;
    private AppThemeBean appTheme;
    private AdsBean Ads;
    private TaboolaBean taboola;
    private String subSectionsIndex;
    private SearchOptionBean searchOption;
    private OtherIconsDownloadUrls otherIconsDownloadUrls;
    private List<UrlBean> staticItem;
    private List<TabsBean> tabs;
    private int totalIcons;

    public int getTotalIcons() {
        return totalIcons;
    }

    public void setTotalIcons(int totalIcons) {
        this.totalIcons = totalIcons;
    }

    private WidgetIndex widgetIndex;

    public WidgetIndex getWidgetIndex() {
        return widgetIndex;
    }

    public void setWidgetIndex(WidgetIndex widgetIndex) {
        this.widgetIndex = widgetIndex;
    }

    public String getLastServerUpdateTime() {
        return lastServerUpdateTime;
    }

    public void setLastServerUpdateTime(String lastServerUpdateTime) {
        this.lastServerUpdateTime = lastServerUpdateTime;
    }

    public String getVokkleId() {
        return vokkleId;
    }

    public void setVokkleId(String vokkleId) {
        this.vokkleId = vokkleId;
    }

    public String getRefreshIntervalInMins() {
        return refreshIntervalInMins;
    }

    public void setRefreshIntervalInMins(String refreshIntervalInMins) {
        this.refreshIntervalInMins = refreshIntervalInMins;
    }

    public AppThemeBean getAppTheme() {
        return appTheme;
    }

    public void setAppTheme(AppThemeBean appTheme) {
        this.appTheme = appTheme;
    }

    public AdsBean getAds() {
        return Ads;
    }

    public void setAds(AdsBean Ads) {
        this.Ads = Ads;
    }

    public TaboolaBean getTaboola() {
        return taboola;
    }

    public void setTaboola(TaboolaBean taboola) {
        this.taboola = taboola;
    }

    public String getSubSectionsIndex() {
        return subSectionsIndex;
    }

    public void setSubSectionsIndex(String subSectionsIndex) {
        this.subSectionsIndex = subSectionsIndex;
    }

    public SearchOptionBean getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(SearchOptionBean searchOption) {
        this.searchOption = searchOption;
    }

    public OtherIconsDownloadUrls getOtherIconsDownloadUrls() {
        return otherIconsDownloadUrls;
    }

    public void setOtherIconsDownloadUrls(OtherIconsDownloadUrls otherIconsDownloadUrls) {
        this.otherIconsDownloadUrls = otherIconsDownloadUrls;
    }

    public List<UrlBean> getStaticItem() {
        return staticItem;
    }

    public void setStaticItem(List<UrlBean> staticItem) {
        this.staticItem = staticItem;
    }

    public List<TabsBean> getTabs() {
        return tabs;
    }

    public void setTabs(List<TabsBean> tabs) {
        this.tabs = tabs;
    }








}
