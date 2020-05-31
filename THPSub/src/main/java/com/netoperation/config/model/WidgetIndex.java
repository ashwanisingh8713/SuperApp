package com.netoperation.config.model;

public class WidgetIndex {

    private String secId;
    private int index;
    private int itemCount;
    private String widgetType;
    private DayNightColor background;
    private DayNightColor description;
    private DayNightColor title;
    private DayNightColor viewAll;

    public String getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(String widgetType) {
        this.widgetType = widgetType;
    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public DayNightColor getBackground() {
        return background;
    }

    public void setBackground(DayNightColor background) {
        this.background = background;
    }

    public DayNightColor getDescription() {
        return description;
    }

    public void setDescription(DayNightColor description) {
        this.description = description;
    }

    public DayNightColor getTitle() {
        return title;
    }

    public void setTitle(DayNightColor title) {
        this.title = title;
    }

    public DayNightColor getViewAll() {
        return viewAll;
    }

    public void setViewAll(DayNightColor viewAll) {
        this.viewAll = viewAll;
    }
}
