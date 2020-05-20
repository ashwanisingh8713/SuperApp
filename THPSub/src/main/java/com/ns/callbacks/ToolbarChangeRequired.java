package com.ns.callbacks;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class ToolbarChangeRequired {

    public static final String SECTION_LISTING_TOPBAR = "sectionListingTopbar";
    public static final String SUB_SECTION_LISTING_TOPBAR = "subSectionListingTopbar";
    public static final String PREMIUM_LISTING_TOPBAR = "premiumListingTopbar";
    public static final String OTHER_LISTING_TOPBAR = "otherListingTopbar";

    public static final String BREIFING_DETAIL_TOPBAR_CROWN = "breifingDetailTopbarCrown";
    public static final String BREIFING_DETAIL_TOPBAR = "breifingDetailTopbar";

    public static final String PREMIUM_BOOKMARK_DETAIL_TOPBAR_CROWN = "premiumBookmarkDetailTopbarCrown";
    public static final String PREMIUM_BOOKMARK_DETAIL_TOPBAR = "premiumBookmarkDetailTopbar";

    public static final String PREMIUM_DETAIL_TOPBAR_CROWN = "premiumBookmarkDetailTopbarCrown";
    public static final String PREMIUM_DETAIL_TOPBAR = "premiumBookmarkDetailTopbar";


    public static final String DEFAULT_RESTRICTED_DETAIL_TOPBAR_CROWN = "defaultRestrictedDetailTopbarCrown";
    public static final String DEFAULT_DETAIL_TOPBAR_CROWN = "defaultDetailTopbarCrown";
    public static final String DEFAULT_DETAIL_TOPBAR = "defaultDetailTopbar";


    @Retention(SOURCE)
    @StringDef({
            SECTION_LISTING_TOPBAR, SUB_SECTION_LISTING_TOPBAR, PREMIUM_LISTING_TOPBAR, OTHER_LISTING_TOPBAR,
            PREMIUM_BOOKMARK_DETAIL_TOPBAR_CROWN, PREMIUM_BOOKMARK_DETAIL_TOPBAR,
            DEFAULT_RESTRICTED_DETAIL_TOPBAR_CROWN, BREIFING_DETAIL_TOPBAR_CROWN, BREIFING_DETAIL_TOPBAR,
            PREMIUM_DETAIL_TOPBAR_CROWN, PREMIUM_DETAIL_TOPBAR, DEFAULT_DETAIL_TOPBAR_CROWN, DEFAULT_DETAIL_TOPBAR
    })
    public @interface ChangeType {}


    private String mPageSource;
    private boolean isEnableLeftSlider;
    private int tabIndex;
    private String typeOfToolbar;
    private String title;

    private boolean isArticleRestricted;
    private boolean isUserCanReRead;
    private boolean isBannerCloseClick;

    public ToolbarChangeRequired(String pageSource, boolean isEnableLeftSlider, int tabIndex, String title, @ChangeType String typeOfToolbar) {
        this.mPageSource = pageSource;
        this.isEnableLeftSlider = isEnableLeftSlider;
        this.tabIndex = tabIndex;
        this.typeOfToolbar = typeOfToolbar;
        this.title = title;
    }

    public ToolbarChangeRequired() {

    }

    public void setTypeOfToolbar(@ChangeType String typeOfToolbar) {
        this.typeOfToolbar = typeOfToolbar;
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


    public boolean isArticleRestricted() {
        return isArticleRestricted;
    }

    public void setArticleRestricted(boolean articleRestricted) {
        isArticleRestricted = articleRestricted;
    }

    public boolean isUserCanReRead() {
        return isUserCanReRead;
    }

    public void setUserCanReRead(boolean userCanReRead) {
        isUserCanReRead = userCanReRead;
    }

    public boolean isBannerCloseClick() {
        return isBannerCloseClick;
    }

    public void setBannerCloseClick(boolean bannerCloseClick) {
        isBannerCloseClick = bannerCloseClick;
    }
}
