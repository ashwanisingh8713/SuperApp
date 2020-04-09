package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.SectionBean;
import com.netoperation.model.StaticPageUrlBean;

import java.util.List;

@Entity(tableName = "TableSectionList")
public class TableSectionList {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private int secId;
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
    private List<SectionBean> subSections;

    public TableSectionList(int secId, String secName, String type, boolean show_on_burger, boolean show_on_explore) {
        this.secId = secId;
        this.secName = secName;
        this.type = type;
        this.show_on_burger = show_on_burger;
        this.show_on_explore = show_on_explore;
    }

    public int getSecId() {
        return secId;
    }

    public void setSecId(int secId) {
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

    public List<SectionBean> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<SectionBean> subSections) {
        this.subSections = subSections;
    }
}
