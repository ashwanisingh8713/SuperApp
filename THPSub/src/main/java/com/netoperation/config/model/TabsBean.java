package com.netoperation.config.model;

import com.netoperation.model.SectionBean;

public class TabsBean {
    /**
     * title : Home
     * iconUrl : {"urlDark":"value","urlLight":"value"}
     * group : GROUP_DEFAULT_SECTIONS
     * pageSource : GROUP_DEFAULT_SECTIONS
     * value : webLink
     * section : {"parentId":0,"secId":43,"secName":"Top News","link":"","secColorRgb":"255,255,255","type":"TN","priority":1,"overridePriority":0,"explorePriority":1,"overrideExplore":0,"image":"https://www.thehindu.com/news/cities/Vijayawada/uqsi40/article31443448.ece/ALTERNATES/FREE_660/vijayawada-migrant-workersjpg","image_v2":"https://www.thehindu.com/news/cities/Vijayawada/uqsi40/article31443448.ece/ALTERNATES/FREE_660/vijayawada-migrant-workersjpg","show_on_burger":false,"show_on_explore":false,"custom":false,"webLink":"","customScreen":0,"customScreenPri":0,"subSections":[],"staticPageUrl":{"url":"","isEnabled":false,"position":"0","sectionId":0}}
     */

    private String title;
    private UrlBean iconUrl;
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

    public UrlBean getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(UrlBean iconUrl) {
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

}
