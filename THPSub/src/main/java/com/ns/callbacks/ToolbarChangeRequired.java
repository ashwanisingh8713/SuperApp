package com.ns.callbacks;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ToolbarChangeRequired {

    private String mPageSource;
    private boolean isEnableLeftSlider;
    private int tabIndex;
    private String typeOfToolbar;
    private String title;

    public static final String SECTION_TOPBAR = "sectionTopbar";
    public static final String SUB_SECTION_TOPBAR = "subSectionTopbar";
    public static final String PREMIUM_TOPBAR = "premiumTopbar";
    public static final String OTHER_TOPBAR = "otherTopbar";

    @Retention(SOURCE)
    @StringDef({
            SECTION_TOPBAR, SUB_SECTION_TOPBAR, PREMIUM_TOPBAR, OTHER_TOPBAR
    })
    public @interface ChangeType {}

    public ToolbarChangeRequired(String pageSource, boolean isEnableLeftSlider, int tabIndex, String title, @ChangeType String typeOfToolbar) {
        this.mPageSource = pageSource;
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

    public String getPageSource() {
        return mPageSource;
    }

    public boolean isEnableLeftSlider() {
        return isEnableLeftSlider;
    }

    public int getTabIndex() {
        return tabIndex;
    }
}
