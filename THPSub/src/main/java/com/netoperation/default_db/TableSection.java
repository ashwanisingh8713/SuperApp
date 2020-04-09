package com.netoperation.default_db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.netoperation.model.THSection;

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
    private THSection section;
    private boolean show_on_burger;
    private boolean show_on_explore;
    private List<THSection> subSections;

    public TableSection(String secId, THSection section, boolean show_on_burger, boolean show_on_explore, List<THSection> subSections) {
        this.secId = secId;
        this.section = section;
        this.show_on_burger = show_on_burger;
        this.show_on_explore = show_on_explore;
        this.subSections = subSections;
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

    public THSection getSection() {
        return section;
    }

    public void setSection(THSection section) {
        this.section = section;
    }

    public List<THSection> getSubSections() {
        return subSections;
    }

    public void setSubSections(List<THSection> subSections) {
        this.subSections = subSections;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }


}
