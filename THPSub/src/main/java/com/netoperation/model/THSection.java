package com.netoperation.model;

public class THSection {

    private String secId;
    private String secName;
    private String link;
    private String type;
    private int priority;
    private String image;
    private String image_v2;
    private boolean show_on_burger;
    private boolean show_on_explore;
    private String webLink;

    private String parentId;
    private String overridePriority;
    private String customScreen;
    private String customScreenPri;

    private StaticPageUrlBean staticPageUrl;

    public THSection(String secId, String secName, String link, boolean show_on_burger, boolean show_on_explore) {
        this.secId = secId;
        this.secName = secName;
        this.link = link;
        this.show_on_burger = show_on_burger;
        this.show_on_explore = show_on_explore;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }

    public StaticPageUrlBean getStaticPageUrl() {
        return staticPageUrl;
    }

    public void setStaticPageUrl(StaticPageUrlBean staticPageUrl) {
        this.staticPageUrl = staticPageUrl;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOverridePriority() {
        return overridePriority;
    }

    public void setOverridePriority(String overridePriority) {
        this.overridePriority = overridePriority;
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
}
