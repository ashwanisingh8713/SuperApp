package com.ns.callbacks;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ToolbarChangeRequired {

    private String from;
    private boolean isEnableLeftSlider;
    private int tabIndex;
    private String typeOfToolbar;
    private String title;

    public static final String SECTION = "section";
    public static final String SUB_SECTION = "subSection";
    public static final String DETAIL = "detail";
    public static final String PREMIUM = "premium";

    @Retention(SOURCE)
    @StringDef({
            SECTION, SUB_SECTION, DETAIL, PREMIUM
    })
    public @interface ChangeType {}

    public ToolbarChangeRequired(String from, boolean isEnableLeftSlider, int tabIndex, String title, @ChangeType String typeOfToolbar) {
        this.from = from;
        this.isEnableLeftSlider = isEnableLeftSlider;
        this.tabIndex = tabIndex;
        this.typeOfToolbar = typeOfToolbar;
        this.title = title;
    }

    public String getTypeOfToolbar() {
        return typeOfToolbar;
    }

    public String getTitle() {
        return title;
    }

    public String getFrom() {
        return from;
    }

    public boolean isEnableLeftSlider() {
        return isEnableLeftSlider;
    }

    public int getTabIndex() {
        return tabIndex;
    }
}