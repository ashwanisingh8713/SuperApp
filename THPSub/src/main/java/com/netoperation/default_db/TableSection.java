package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.netoperation.model.SectionBean;

import java.util.List;

@Entity(tableName = "TableSection")
public class TableSection {

    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private String secId;
    private SectionBean section;
    private boolean show_on_burger;
    private boolean show_on_explore;

    private String parentId;
    private String overridePriority;
    private String customScreen;
    private String customScreenPri;
    private String secName;
    private String type;
    private boolean isUserPreferred;

    private List<SectionBean> subSections;

    public  TableSection(String secId, String secName,  String type, SectionBean section, boolean show_on_burger, boolean show_on_explore, List<SectionBean> subSections) {
        this.secId = secId;
        this.secName = secName;
        this.type = type;
        this.section = section;
        this.show_on_burger = show_on_burger;
        this.show_on_explore = show_on_explore;
        this.subSections = subSections;
    }

    public boolean isUserPreferred() {
        return isUserPreferred;
    }

    public void setUserPreferred(boolean userPreferred) {
        isUserPreferred = userPreferred;
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

    public SectionBean getSection() {
        return section;
    }

    public void setSection(SectionBean section) {
        this.section = section;
    }

    public List<SectionBean> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<SectionBean> subSections) {
        this.subSections = subSections;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
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


    public String getSecName() {
        return secName;
    }

    public void setSecName(String secName) {
        this.secName = secName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
