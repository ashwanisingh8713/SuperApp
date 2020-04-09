package com.netoperation.model;

import java.util.List;

public class HomeBean {
    /**
     * banner : {"secId":43,"secName":"Top News","type":"TN"}
     * explore : []
     * widget : [{"parentSecId":0,"secId":142,"homePriority":2,"overridePriority":0,"secName":"News In Quotes","type":"GN","viewAllCTA":true},{"parentSecId":0,"secId":88,"homePriority":3,"overridePriority":0,"secName":"Top Picks","type":"GN","viewAllCTA":false},{"parentSecId":0,"secId":5,"homePriority":4,"overridePriority":0,"secName":"Editorials & Opinion","type":"GN","viewAllCTA":true},{"parentSecId":0,"secId":138,"homePriority":5,"overridePriority":0,"secName":"Multimedia","type":"VD","viewAllCTA":true},{"parentSecId":5,"secId":69,"homePriority":6,"overridePriority":0,"secName":"Cartoons","type":"GN","viewAllCTA":true}]
     * personalize : [{"secId":12,"homePriority":5,"overridePriority":0,"secName":"National","type":"GN"},{"secId":13,"homePriority":6,"overridePriority":0,"secName":"International","type":"GN"},{"secId":7,"homePriority":8,"overridePriority":0,"secName":"Business","type":"GN"},{"secId":6,"homePriority":10,"overridePriority":0,"secName":"Sports","type":"GN"},{"secId":52,"homePriority":11,"overridePriority":0,"secName":"Entertainment","type":"GN"}]
     * staticPageUrl : {"isEnabled":false,"url":"","youtubeId":"","position":"0","sectionId":0,"lastUpdatedOn":"2020-02-12 10:40:07"}
     */

    private BannerBean banner;
    private StaticPageUrlBean staticPageUrl;
    private List<WidgetBean> widget;
    private List<THDefaultPersonalizeBean> personalize;

    public BannerBean getBanner() {
        return banner;
    }

    public void setBanner(BannerBean banner) {
        this.banner = banner;
    }

    public StaticPageUrlBean getStaticPageUrl() {
        return staticPageUrl;
    }

    public void setStaticPageUrl(StaticPageUrlBean staticPageUrl) {
        this.staticPageUrl = staticPageUrl;
    }

    public List<WidgetBean> getWidget() {
        return widget;
    }

    public void setWidget(List<WidgetBean> widget) {
        this.widget = widget;
    }

    public List<THDefaultPersonalizeBean> getPersonalize() {
        return personalize;
    }

    public void setPersonalize(List<THDefaultPersonalizeBean> personalize) {
        this.personalize = personalize;
    }


}
