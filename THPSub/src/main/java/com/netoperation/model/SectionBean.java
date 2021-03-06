package com.netoperation.model;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SectionBean {

    /**
     * parentId : 0
     * secId : 43
     * secName : Top News
     * link :
     * secColorRgb : 255,255,255
     * type : TN
     * priority : 1
     * overridePriority : 0
     * explorePriority : 1
     * overrideExplore : 0
     * image : https://www.thehindu.com/news/cities/chennai/ei5ezc/article31285827.ece/
     * image_v2 : https://www.thehindu.com/news/cities/chennai/ei5ezc/article31285827.ece/
     * show_on_burger : false
     * show_on_explore : false
     * custom : false
     * webLink :
     * customScreen : 0
     * customScreenPri : 0
     * subSections : []
     * staticPageUrl : {"url":"","isEnabled":false,"position":"0","sectionId":0}
     */

    private String parentSecId;
    private String secId;
    private String parentSecName;
    private String secName;
    private String link;
    private String type;
    private int priority;
    private String image;
    private String image_v2;
    private boolean show_on_burger;
    private boolean show_on_explore;
    private String webLink;
    private StaticPageUrlBean staticPageUrl;
    private List<SectionBean> subSections = new ArrayList<>();

    private int parentId;
    private String secColorRgb;
    private int overridePriority;
    private int explorePriority;
    private int overrideExplore;
    private boolean custom;
    private String customScreen = "";
    private String customScreenPri = "";

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getParentSecId() {
        return parentSecId;
    }

    public void setParentSecId(String parentSecId) {
        this.parentSecId = parentSecId;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getParentSecName() {
        return parentSecName;
    }

    public void setParentSecName(String parentSecName) {
        this.parentSecName = parentSecName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSecColorRgb() {
        return secColorRgb;
    }

    public void setSecColorRgb(String secColorRgb) {
        this.secColorRgb = secColorRgb;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getOverridePriority() {
        return overridePriority;
    }

    public void setOverridePriority(int overridePriority) {
        this.overridePriority = overridePriority;
    }

    public int getExplorePriority() {
        return explorePriority;
    }

    public void setExplorePriority(int explorePriority) {
        this.explorePriority = explorePriority;
    }

    public int getOverrideExplore() {
        return overrideExplore;
    }

    public void setOverrideExplore(int overrideExplore) {
        this.overrideExplore = overrideExplore;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_v2() {
        return image_v2;
    }

    public void setImage_v2(String image_v2) {
        this.image_v2 = image_v2;
    }

    public boolean isShow_on_burger() {
        return show_on_burger;
    }

    public void setShow_on_burger(boolean show_on_burger) {
        this.show_on_burger = show_on_burger;
    }

    public boolean isShow_on_explore() {
        return show_on_explore;
    }

    public void setShow_on_explore(boolean show_on_explore) {
        this.show_on_explore = show_on_explore;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public String getCustomScreen() {
        return customScreen;
    }

    public void setCustomScreen(String customScreen) {
        this.customScreen = customScreen;
    }

    public String getCustomScreenPri() {
        return customScreenPri;
    }

    public void setCustomScreenPri(String customScreenPri) {
        this.customScreenPri = customScreenPri;
    }

    public StaticPageUrlBean getStaticPageUrl() {
        return staticPageUrl;
    }

    public void setStaticPageUrl(StaticPageUrlBean staticPageUrl) {
        this.staticPageUrl = staticPageUrl;
    }

    public List<SectionBean> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<SectionBean> subSections) {
        this.subSections = subSections;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        super.equals(obj);
        if(obj != null && obj instanceof SectionBean) {
            SectionBean bean = (SectionBean)obj;
            return bean.getSecId().equals(getSecId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();

        return secId.hashCode();
    }
}
