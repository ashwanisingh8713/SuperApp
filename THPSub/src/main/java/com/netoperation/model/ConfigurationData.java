package com.netoperation.model;

import com.netoperation.default_db.TableConfiguration;


public class ConfigurationData {


    /**
     * STATUS : true
     * STATUS_MSG : Success
     * DATA : {"lastServerUpdateTime":"UTC-TIME","vokkleId":"1234","refreshIntervalInMins":"value","imprtantMsg":"This msg on update of config (not required always, if exist will be shown to user only once)","staticItem":[{"title":"Name","urlDark":"value","urlLight":"value"},{"title":"Name","urlDark":"value","urlLight":"value"}],"tabs":[{"title":"Home","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_DEFAULT_SECTIONS","pageSource":"GROUP_DEFAULT_SECTIONS"},{"title":"Briefing","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_PREMIUM_SECTIONS","pageSource":"Briefing"},{"title":"My Stories","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_PREMIUM_SECTIONS","pageSource":"My Stories"},{"title":"Suggested","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_PREMIUM_SECTIONS","pageSource":"Suggested"},{"title":"Profile","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_PREMIUM_SECTIONS","pageSource":"Profile"},{"title":"Tab Title","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_DEFAULT_SECTIONS","pageSource":"Url","value":"webLink"},{"title":"Tab Title","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_DEFAULT_SECTIONS","pageSource":"SENSEX"},{"title":"Top News","iconUrl":{"urlDark":"value","urlLight":"value"},"group":"GROUP_DEFAULT_SECTIONS","pageSource":"ADD_ON_SECTION","section":{"parentId":0,"secId":43,"secName":"Top News","link":"","secColorRgb":"255,255,255","type":"TN","priority":1,"overridePriority":0,"explorePriority":1,"overrideExplore":0,"image":"https://www.thehindu.com/news/cities/Vijayawada/uqsi40/article31443448.ece/ALTERNATES/FREE_660/vijayawada-migrant-workersjpg","image_v2":"https://www.thehindu.com/news/cities/Vijayawada/uqsi40/article31443448.ece/ALTERNATES/FREE_660/vijayawada-migrant-workersjpg","show_on_burger":false,"show_on_explore":false,"custom":false,"webLink":"","customScreen":0,"customScreenPri":0,"subSections":[],"staticPageUrl":{"url":"","isEnabled":false,"position":"0","sectionId":0}}}],"appTheme":{"textColors":{"dark":{"XL":"#000000","L":"#000000","M":"#000000","S":"#818181","XS":"#818181","detailpageText":"#000000","WigetText":"#ffffff","breadcrumSelect":"ffffff","breadcrumUnSelect":"818181"},"light":{"XL":"#ffffff","L":"#ffffff","M":"#ffffff","S":"#818181","XS":"#818181","detailpageText":"#ffffff","WigetText":"#ffffff","breadcrumSelect":"ffffff","breadcrumUnSelect":"818181"}},"tilesBg":{"dark":"#191919","light":"#ffffff"},"screenBg":{"dark":"#191919","light":"#ffffff"},"topBarBg":{"dark":"#313131","light":"#ffffff"},"bottomBarBg":{"dark":"#313131","light":"#ffffff"},"widgetBg":{"background":{"dark":"#191919","light":"#ffffff"}},"breadcrumBg":{"dark":"#313131","light":"#ffffff"}},"Ads":{"bottomAdHomeId":"/22390678/Hindu_Android_HP_Sticky","bottomAdOtherId":"/22390678/Hindu_Android_AT_Sticky","fullScreenAdId":"/22390678/Hindu_Android_Interstitial","detailPageTopAdId":"/22390678/Hindu_Android_AT_Middle","detailPageBottomAdId":"/22390678/Hindu_Android_AT_Bottom","listingPageAds":[{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Middle","index":"4"},{"type":"taboola","index":"6"},{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Footer","index":"8"},{"type":"taboola","index":"10"},{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Bottom","index":"12"},{"type":"taboola","index":"13"},{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Middle","index":"16"},{"type":"taboola","index":"18"},{"type":"DFP","adId":"/22390678/Hindu_Android_HP_Middle","index":"20"}]},"taboola":{"id":"thehindu-hinduappandroid","apikey":"65e50cbad5e7e156f110d188630a733c8054f54b","publisher":"thehindu-hinduappandroid","pageType":"article","mode":"thumbnails-a","placementLight":"Below Article Thumbnails","placementDark":"Below Article Thumbnails Dark","taboolaNativeAd":{"placement_home":"Below Home Stream","placement_home_source_type":"home","placement_section":"Below Section Stream","placement_section_source_type":"section","placement_detail":"Below Article Stream"}},"subSectionsIndex":"3","searchOption":{"urlId":"https://thehindu.com/app/getArticleByIds.json?articleIds=","urlText":"https://www.thehindu.com/app/search.json?term=","types":["article","sensex"]},"otherIconsDownloadUrls":"url tomm get all other icons"}
     */

    private boolean STATUS;
    private String STATUS_MSG;
    private TableConfiguration DATA;

    public boolean isSTATUS() {
        return STATUS;
    }

    public void setSTATUS(boolean STATUS) {
        this.STATUS = STATUS;
    }

    public String getSTATUS_MSG() {
        return STATUS_MSG;
    }

    public void setSTATUS_MSG(String STATUS_MSG) {
        this.STATUS_MSG = STATUS_MSG;
    }

    public TableConfiguration getDATA() {
        return DATA;
    }

    public void setDATA(TableConfiguration DATA) {
        this.DATA = DATA;
    }


}
