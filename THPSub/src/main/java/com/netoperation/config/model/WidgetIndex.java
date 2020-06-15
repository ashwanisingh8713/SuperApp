package com.netoperation.config.model;

import java.util.List;

public class WidgetIndex {

    private String secId;
    private int index;
    private int itemCount;
    private String widgetType;
    private String groupLayout;
    private String itemLayout;
    private boolean groupHeaderRequired;
    private boolean actionIconRequired;
    private boolean groupOuterLineRequired;
    private List<Integer> groupMargin;
    private int groupRadius;
    private int groupElevation;
    private String actionTitle;
    private String actionGravity;
    private boolean itemOuterLineRequired;
    private int itemRadius;
    private int itemElevation;
    private List<Integer> itemMargin;
    private DayNightColor itemBackground;
    private DayNightColor description;
    private DayNightColor title;
    private DayNightColor action;

    public int getGroupRadius() {
        return groupRadius;
    }

    public void setGroupRadius(int groupRadius) {
        this.groupRadius = groupRadius;
    }

    public int getGroupElevation() {
        return groupElevation;
    }

    public void setGroupElevation(int groupElevation) {
        this.groupElevation = groupElevation;
    }

    public int getItemRadius() {
        return itemRadius;
    }

    public void setItemRadius(int itemRadius) {
        this.itemRadius = itemRadius;
    }

    public int getItemElevation() {
        return itemElevation;
    }

    public void setItemElevation(int itemElevation) {
        this.itemElevation = itemElevation;
    }

    public String getActionGravity() {
        return actionGravity;
    }

    public void setActionGravity(String actionGravity) {
        this.actionGravity = actionGravity;
    }

    public String getGroupLayout() {
        return groupLayout;
    }

    public void setGroupLayout(String groupLayout) {
        this.groupLayout = groupLayout;
    }

    public String getItemLayout() {
        return itemLayout;
    }

    public void setItemLayout(String itemLayout) {
        this.itemLayout = itemLayout;
    }

    public boolean isGroupHeaderRequired() {
        return groupHeaderRequired;
    }

    public void setGroupHeaderRequired(boolean groupHeaderRequired) {
        this.groupHeaderRequired = groupHeaderRequired;
    }

    public String getActionTitle() {
        return actionTitle;
    }

    public void setActionTitle(String actionTitle) {
        this.actionTitle = actionTitle;
    }

    public boolean isActionIconRequired() {
        return actionIconRequired;
    }

    public void setActionIconRequired(boolean actionIconRequired) {
        this.actionIconRequired = actionIconRequired;
    }

    public boolean isGroupOuterLineRequired() {
        return groupOuterLineRequired;
    }

    public void setGroupOuterLineRequired(boolean groupOuterLineRequired) {
        this.groupOuterLineRequired = groupOuterLineRequired;
    }

    public boolean isItemOuterLineRequired() {
        return itemOuterLineRequired;
    }

    public void setItemOuterLineRequired(boolean itemOuterLineRequired) {
        this.itemOuterLineRequired = itemOuterLineRequired;
    }

    public DayNightColor getItemBackground() {
        return itemBackground;
    }

    public void setItemBackground(DayNightColor itemBackground) {
        this.itemBackground = itemBackground;
    }

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

    public DayNightColor getAction() {
        return action;
    }

    public void setAction(DayNightColor action) {
        this.action = action;
    }

    public List<Integer> getItemMargin() {
        return itemMargin;
    }

    public void setItemMargin(List<Integer> itemMargin) {
        this.itemMargin = itemMargin;
    }

    public List<Integer> getGroupMargin() {
        return groupMargin;
    }

    public void setGroupMargin(List<Integer> groupMargin) {
        this.groupMargin = groupMargin;
    }
}
