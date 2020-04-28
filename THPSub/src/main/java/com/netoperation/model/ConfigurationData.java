package com.netoperation.model;

import java.util.List;

public class ConfigurationData {


    /**
     * lastServerUpdateTime : UTC-TIME
     * vokkleId : 1234
     * refreshIntervalInMins : value
     * imprtantMsg : This msg on update of config (not required always, if exist will be shown to user only once)
     * taboola : {"publisher":"value","pageType":"value","mode":"value","placementDark":"value","placementLight":"value","taboolaNativeAd":{"showTaboolaNativAd":"boolean","showTaboolaWidget":"boolean","taboola_placement_home":"value","taboola_placement_home_source_type":"value","taboola_placement_section":"value","taboola_placement_section_source_type":"value","taboola_placement_detail":"value"}}
     * staticItem : [{"title":"Name","urlDark":"value","urlLight":"value"},{"title":"Name","urlDark":"value","urlLight":"value"}]
     * tabs : [{"title":"Home","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_DEFAULT_SECTIONS","pageSource":"GROUP_DEFAULT_SECTIONS"},{"title":"Briefing","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_PREMIUM_SECTIONS","pageSource":"Briefing"},{"title":"My Stories","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_PREMIUM_SECTIONS","pageSource":"My Stories"},{"title":"Suggested","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_PREMIUM_SECTIONS","pageSource":"Suggested"},{"title":"Profile","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_PREMIUM_SECTIONS","pageSource":"Profile"},{"title":"Tab Title","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_DEFAULT_SECTIONS","pageSource":"Url","value":"webLink"},{"title":"Tab Title","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_DEFAULT_SECTIONS","pageSource":"SENSEX"},{"title":"Tab Title","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_DEFAULT_SECTIONS","pageSource":"ADD_ON_SECTION","section":{"parentId":0,"secId":43,"secName":"Top News","link":"","secColorRgb":"255,255,255","type":"TN","priority":1,"overridePriority":0,"explorePriority":1,"overrideExplore":0,"image":"https://www.thehindu.com/news/cities/Vijayawada/uqsi40/article31443448.ece/ALTERNATES/FREE_660/vijayawada-migrant-workersjpg","image_v2":"https://www.thehindu.com/news/cities/Vijayawada/uqsi40/article31443448.ece/ALTERNATES/FREE_660/vijayawada-migrant-workersjpg","show_on_burger":false,"show_on_explore":false,"custom":false,"webLink":"","customScreen":0,"customScreenPri":0,"subSections":[],"staticPageUrl":{"url":"","isEnabled":false,"position":"0","sectionId":0}}}]
     * topBarBackground : {"dark":"#ffffff","light":"#000000"}
     * bottomBarBackground : {"dark":"#ffffff","light":"#000000"}
     * textColors : {"dark":{"XlargeTextColor":"#000000","largeTextColor":"#000000","mediumTextColor":"#000000","smallTextColor":"#000000","microTextColor":"#000000","detailpageBackground":"#ffffff","detailpageText":"#000000"},"light":{"XlargeTextColor":"#ffffff","largeTextColor":"#ffffff","mediumTextColor":"#ffffff","smallTextColor":"#ffffff","microTextColor":"#ffffff","detailpageBackground":"#000000","detailpageText":"#ffffff"}}
     * screenBackgound : {"dark":"#000000","light":"#ffffff"}
     * Ads : {"bottomAdId":"value","fullScreenAdId":"value","detailPageTopAdId":"value","detailPageBottomAdId":"value","bannerAdIds":[{"adId":"Value","index":"4"},{"adId":"Value","index":"8"}],"showBannerAd":"boolean","showDetailPageAd":"boolean","showFullScreenAd":"boolean"}
     * subSectionsIndex : 3
     * otherIconsDownloadUrls : url to get all other icons
     */

    private String lastServerUpdateTime;
    private String vokkleId;
    private String refreshIntervalInMins;
    private String imprtantMsg;
    private TaboolaBean taboola;
    private TopBarBackgroundBean topBarBackground;
    private BottomBarBackgroundBean bottomBarBackground;
    private TextColorsBean textColors;
    private ScreenBackgoundBean screenBackgound;
    private AdsBean Ads;
    private String subSectionsIndex;
    private String otherIconsDownloadUrls;
    private List<StaticItemBean> staticItem;
    private List<TabsBean> tabs;

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

    public String getImprtantMsg() {
        return imprtantMsg;
    }

    public void setImprtantMsg(String imprtantMsg) {
        this.imprtantMsg = imprtantMsg;
    }

    public TaboolaBean getTaboola() {
        return taboola;
    }

    public void setTaboola(TaboolaBean taboola) {
        this.taboola = taboola;
    }

    public TopBarBackgroundBean getTopBarBackground() {
        return topBarBackground;
    }

    public void setTopBarBackground(TopBarBackgroundBean topBarBackground) {
        this.topBarBackground = topBarBackground;
    }

    public BottomBarBackgroundBean getBottomBarBackground() {
        return bottomBarBackground;
    }

    public void setBottomBarBackground(BottomBarBackgroundBean bottomBarBackground) {
        this.bottomBarBackground = bottomBarBackground;
    }

    public TextColorsBean getTextColors() {
        return textColors;
    }

    public void setTextColors(TextColorsBean textColors) {
        this.textColors = textColors;
    }

    public ScreenBackgoundBean getScreenBackgound() {
        return screenBackgound;
    }

    public void setScreenBackgound(ScreenBackgoundBean screenBackgound) {
        this.screenBackgound = screenBackgound;
    }

    public AdsBean getAds() {
        return Ads;
    }

    public void setAds(AdsBean Ads) {
        this.Ads = Ads;
    }

    public String getSubSectionsIndex() {
        return subSectionsIndex;
    }

    public void setSubSectionsIndex(String subSectionsIndex) {
        this.subSectionsIndex = subSectionsIndex;
    }

    public String getOtherIconsDownloadUrls() {
        return otherIconsDownloadUrls;
    }

    public void setOtherIconsDownloadUrls(String otherIconsDownloadUrls) {
        this.otherIconsDownloadUrls = otherIconsDownloadUrls;
    }

    public List<StaticItemBean> getStaticItem() {
        return staticItem;
    }

    public void setStaticItem(List<StaticItemBean> staticItem) {
        this.staticItem = staticItem;
    }

    public List<TabsBean> getTabs() {
        return tabs;
    }

    public void setTabs(List<TabsBean> tabs) {
        this.tabs = tabs;
    }

    public static class TaboolaBean {
        /**
         * publisher : value
         * pageType : value
         * mode : value
         * placementDark : value
         * placementLight : value
         * taboolaNativeAd : {"showTaboolaNativAd":"boolean","showTaboolaWidget":"boolean","taboola_placement_home":"value","taboola_placement_home_source_type":"value","taboola_placement_section":"value","taboola_placement_section_source_type":"value","taboola_placement_detail":"value"}
         */

        private String publisher;
        private String pageType;
        private String mode;
        private String placementDark;
        private String placementLight;
        private TaboolaNativeAdBean taboolaNativeAd;

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getPageType() {
            return pageType;
        }

        public void setPageType(String pageType) {
            this.pageType = pageType;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getPlacementDark() {
            return placementDark;
        }

        public void setPlacementDark(String placementDark) {
            this.placementDark = placementDark;
        }

        public String getPlacementLight() {
            return placementLight;
        }

        public void setPlacementLight(String placementLight) {
            this.placementLight = placementLight;
        }

        public TaboolaNativeAdBean getTaboolaNativeAd() {
            return taboolaNativeAd;
        }

        public void setTaboolaNativeAd(TaboolaNativeAdBean taboolaNativeAd) {
            this.taboolaNativeAd = taboolaNativeAd;
        }

        public static class TaboolaNativeAdBean {
            /**
             * showTaboolaNativAd : boolean
             * showTaboolaWidget : boolean
             * taboola_placement_home : value
             * taboola_placement_home_source_type : value
             * taboola_placement_section : value
             * taboola_placement_section_source_type : value
             * taboola_placement_detail : value
             */

            private String showTaboolaNativAd;
            private String showTaboolaWidget;
            private String taboola_placement_home;
            private String taboola_placement_home_source_type;
            private String taboola_placement_section;
            private String taboola_placement_section_source_type;
            private String taboola_placement_detail;

            public String getShowTaboolaNativAd() {
                return showTaboolaNativAd;
            }

            public void setShowTaboolaNativAd(String showTaboolaNativAd) {
                this.showTaboolaNativAd = showTaboolaNativAd;
            }

            public String getShowTaboolaWidget() {
                return showTaboolaWidget;
            }

            public void setShowTaboolaWidget(String showTaboolaWidget) {
                this.showTaboolaWidget = showTaboolaWidget;
            }

            public String getTaboola_placement_home() {
                return taboola_placement_home;
            }

            public void setTaboola_placement_home(String taboola_placement_home) {
                this.taboola_placement_home = taboola_placement_home;
            }

            public String getTaboola_placement_home_source_type() {
                return taboola_placement_home_source_type;
            }

            public void setTaboola_placement_home_source_type(String taboola_placement_home_source_type) {
                this.taboola_placement_home_source_type = taboola_placement_home_source_type;
            }

            public String getTaboola_placement_section() {
                return taboola_placement_section;
            }

            public void setTaboola_placement_section(String taboola_placement_section) {
                this.taboola_placement_section = taboola_placement_section;
            }

            public String getTaboola_placement_section_source_type() {
                return taboola_placement_section_source_type;
            }

            public void setTaboola_placement_section_source_type(String taboola_placement_section_source_type) {
                this.taboola_placement_section_source_type = taboola_placement_section_source_type;
            }

            public String getTaboola_placement_detail() {
                return taboola_placement_detail;
            }

            public void setTaboola_placement_detail(String taboola_placement_detail) {
                this.taboola_placement_detail = taboola_placement_detail;
            }
        }
    }

    public static class TopBarBackgroundBean {
        /**
         * dark : #ffffff
         * light : #000000
         */

        private String dark;
        private String light;

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
    }

    public static class BottomBarBackgroundBean {
        /**
         * dark : #ffffff
         * light : #000000
         */

        private String dark;
        private String light;

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
    }

    public static class TextColorsBean {
        /**
         * dark : {"XlargeTextColor":"#000000","largeTextColor":"#000000","mediumTextColor":"#000000","smallTextColor":"#000000","microTextColor":"#000000","detailpageBackground":"#ffffff","detailpageText":"#000000"}
         * light : {"XlargeTextColor":"#ffffff","largeTextColor":"#ffffff","mediumTextColor":"#ffffff","smallTextColor":"#ffffff","microTextColor":"#ffffff","detailpageBackground":"#000000","detailpageText":"#ffffff"}
         */

        private DarkBean dark;
        private LightBean light;

        public DarkBean getDark() {
            return dark;
        }

        public void setDark(DarkBean dark) {
            this.dark = dark;
        }

        public LightBean getLight() {
            return light;
        }

        public void setLight(LightBean light) {
            this.light = light;
        }

        public static class DarkBean {
            /**
             * XlargeTextColor : #000000
             * largeTextColor : #000000
             * mediumTextColor : #000000
             * smallTextColor : #000000
             * microTextColor : #000000
             * detailpageBackground : #ffffff
             * detailpageText : #000000
             */

            private String XlargeTextColor;
            private String largeTextColor;
            private String mediumTextColor;
            private String smallTextColor;
            private String microTextColor;
            private String detailpageBackground;
            private String detailpageText;

            public String getXlargeTextColor() {
                return XlargeTextColor;
            }

            public void setXlargeTextColor(String XlargeTextColor) {
                this.XlargeTextColor = XlargeTextColor;
            }

            public String getLargeTextColor() {
                return largeTextColor;
            }

            public void setLargeTextColor(String largeTextColor) {
                this.largeTextColor = largeTextColor;
            }

            public String getMediumTextColor() {
                return mediumTextColor;
            }

            public void setMediumTextColor(String mediumTextColor) {
                this.mediumTextColor = mediumTextColor;
            }

            public String getSmallTextColor() {
                return smallTextColor;
            }

            public void setSmallTextColor(String smallTextColor) {
                this.smallTextColor = smallTextColor;
            }

            public String getMicroTextColor() {
                return microTextColor;
            }

            public void setMicroTextColor(String microTextColor) {
                this.microTextColor = microTextColor;
            }

            public String getDetailpageBackground() {
                return detailpageBackground;
            }

            public void setDetailpageBackground(String detailpageBackground) {
                this.detailpageBackground = detailpageBackground;
            }

            public String getDetailpageText() {
                return detailpageText;
            }

            public void setDetailpageText(String detailpageText) {
                this.detailpageText = detailpageText;
            }
        }

        public static class LightBean {
            /**
             * XlargeTextColor : #ffffff
             * largeTextColor : #ffffff
             * mediumTextColor : #ffffff
             * smallTextColor : #ffffff
             * microTextColor : #ffffff
             * detailpageBackground : #000000
             * detailpageText : #ffffff
             */

            private String XlargeTextColor;
            private String largeTextColor;
            private String mediumTextColor;
            private String smallTextColor;
            private String microTextColor;
            private String detailpageBackground;
            private String detailpageText;

            public String getXlargeTextColor() {
                return XlargeTextColor;
            }

            public void setXlargeTextColor(String XlargeTextColor) {
                this.XlargeTextColor = XlargeTextColor;
            }

            public String getLargeTextColor() {
                return largeTextColor;
            }

            public void setLargeTextColor(String largeTextColor) {
                this.largeTextColor = largeTextColor;
            }

            public String getMediumTextColor() {
                return mediumTextColor;
            }

            public void setMediumTextColor(String mediumTextColor) {
                this.mediumTextColor = mediumTextColor;
            }

            public String getSmallTextColor() {
                return smallTextColor;
            }

            public void setSmallTextColor(String smallTextColor) {
                this.smallTextColor = smallTextColor;
            }

            public String getMicroTextColor() {
                return microTextColor;
            }

            public void setMicroTextColor(String microTextColor) {
                this.microTextColor = microTextColor;
            }

            public String getDetailpageBackground() {
                return detailpageBackground;
            }

            public void setDetailpageBackground(String detailpageBackground) {
                this.detailpageBackground = detailpageBackground;
            }

            public String getDetailpageText() {
                return detailpageText;
            }

            public void setDetailpageText(String detailpageText) {
                this.detailpageText = detailpageText;
            }
        }
    }

    public static class ScreenBackgoundBean {
        /**
         * dark : #000000
         * light : #ffffff
         */

        private String dark;
        private String light;

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
    }

    public static class AdsBean {
        /**
         * bottomAdId : value
         * fullScreenAdId : value
         * detailPageTopAdId : value
         * detailPageBottomAdId : value
         * bannerAdIds : [{"adId":"Value","index":"4"},{"adId":"Value","index":"8"}]
         * showBannerAd : boolean
         * showDetailPageAd : boolean
         * showFullScreenAd : boolean
         */

        private String bottomAdId;
        private String fullScreenAdId;
        private String detailPageTopAdId;
        private String detailPageBottomAdId;
        private String showBannerAd;
        private String showDetailPageAd;
        private String showFullScreenAd;
        private List<BannerAdIdsBean> bannerAdIds;

        public String getBottomAdId() {
            return bottomAdId;
        }

        public void setBottomAdId(String bottomAdId) {
            this.bottomAdId = bottomAdId;
        }

        public String getFullScreenAdId() {
            return fullScreenAdId;
        }

        public void setFullScreenAdId(String fullScreenAdId) {
            this.fullScreenAdId = fullScreenAdId;
        }

        public String getDetailPageTopAdId() {
            return detailPageTopAdId;
        }

        public void setDetailPageTopAdId(String detailPageTopAdId) {
            this.detailPageTopAdId = detailPageTopAdId;
        }

        public String getDetailPageBottomAdId() {
            return detailPageBottomAdId;
        }

        public void setDetailPageBottomAdId(String detailPageBottomAdId) {
            this.detailPageBottomAdId = detailPageBottomAdId;
        }

        public String getShowBannerAd() {
            return showBannerAd;
        }

        public void setShowBannerAd(String showBannerAd) {
            this.showBannerAd = showBannerAd;
        }

        public String getShowDetailPageAd() {
            return showDetailPageAd;
        }

        public void setShowDetailPageAd(String showDetailPageAd) {
            this.showDetailPageAd = showDetailPageAd;
        }

        public String getShowFullScreenAd() {
            return showFullScreenAd;
        }

        public void setShowFullScreenAd(String showFullScreenAd) {
            this.showFullScreenAd = showFullScreenAd;
        }

        public List<BannerAdIdsBean> getBannerAdIds() {
            return bannerAdIds;
        }

        public void setBannerAdIds(List<BannerAdIdsBean> bannerAdIds) {
            this.bannerAdIds = bannerAdIds;
        }

        public static class BannerAdIdsBean {
            /**
             * adId : Value
             * index : 4
             */

            private String adId;
            private String index;

            public String getAdId() {
                return adId;
            }

            public void setAdId(String adId) {
                this.adId = adId;
            }

            public String getIndex() {
                return index;
            }

            public void setIndex(String index) {
                this.index = index;
            }
        }
    }

    public static class StaticItemBean {
        /**
         * title : Name
         * urlDark : value
         * urlLight : value
         */

        private String title;
        private String urlDark;
        private String urlLight;

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
    }

    public static class TabsBean {
        /**
         * title : Home
         * iconUrl : {"urlDark":"value","urlLight":"value"}
         * group : GROUP_DEFAULT_SECTIONS
         * pageSource : GROUP_DEFAULT_SECTIONS
         * value : webLink
         * section : {"parentId":0,"secId":43,"secName":"Top News","link":"","secColorRgb":"255,255,255","type":"TN","priority":1,"overridePriority":0,"explorePriority":1,"overrideExplore":0,"image":"https://www.thehindu.com/news/cities/Vijayawada/uqsi40/article31443448.ece/ALTERNATES/FREE_660/vijayawada-migrant-workersjpg","image_v2":"https://www.thehindu.com/news/cities/Vijayawada/uqsi40/article31443448.ece/ALTERNATES/FREE_660/vijayawada-migrant-workersjpg","show_on_burger":false,"show_on_explore":false,"custom":false,"webLink":"","customScreen":0,"customScreenPri":0,"subSections":[],"staticPageUrl":{"url":"","isEnabled":false,"position":"0","sectionId":0}}
         */

        private String title;
        private IconUrlBean iconUrl;
        private String group;
        private String pageSource;
        private String value;
        private SectionBean section;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public IconUrlBean getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(IconUrlBean iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getPageSource() {
            return pageSource;
        }

        public void setPageSource(String pageSource) {
            this.pageSource = pageSource;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public SectionBean getSection() {
            return section;
        }

        public void setSection(SectionBean section) {
            this.section = section;
        }

        public static class IconUrlBean {
            /**
             * urlDark : value
             * urlLight : value
             */

            private String urlDark;
            private String urlLight;

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
        }


    }
}
