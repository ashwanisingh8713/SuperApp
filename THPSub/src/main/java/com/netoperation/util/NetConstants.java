package com.netoperation.util;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public class NetConstants {

    public static final boolean IS_HOLD = true;

    public static final String TAG_UNIQUE = "Ashwani";
    public static final String TAG_ERROR = "ERRORAshwani";

    /** When user does sign up*/
    public static final String EVENT_SIGNUP = "signup";

    /** When user does reset password*/
    public static final String EVENT_FORGOT_PASSWORD = "forgotpassword";

    /** When user wants to suspend or delete account */
    public static final String EVENT_CHANGE_ACCOUNT_STATUS = "changeAccountStatus";

    public static final String TEMP_SECTION_ID = "tempSectionId";

    public static final String API_suggested = "suggested";
    public static final String API_Mystories = "personalised";
    public static final String API_bookmarks = "bookmarks";
    public static final String API_briefcase = "briefcase";


    public static final String BREIFING_ALL = "ALL";
    public static final String BREIFING_EVENING = "EVENIING";
    public static final String BREIFING_MORNING = "MORNING";
    public static final String BREIFING_NOON = "NOON";

    public static final String RECO_TEMP_NOT_EXIST = "TEMP_NOT_EXIST";
    public static final String RECO_HOME_TAB = "HOME";

    public static final String G_DEFAULT_SECTIONS = "GROUP_DEFAULT_SECTIONS";
    public static final String G_PREMIUM_SECTIONS = "GROUP_PREMIUM_SECTIONS";
    public static final String G_BOOKMARK_DEFAULT = "BOOKMARK_GROUP_DEFAULT";
    public static final String G_BOOKMARK_PREMIUM = "BOOKMARK_GROUP_PREMIUM";
    public static final String G_NOTIFICATION = "GROUP_NOTIFICATION";

    public static final String BOOKMARK_IN_ONE = "BOOKMARK_IN_ONE";
    public static final String BOOKMARK_IN_TAB = "BOOKMARK_IN_TAB";

    @Retention(SOURCE)
    @StringDef({
            G_BOOKMARK_DEFAULT,
            G_BOOKMARK_PREMIUM
    })
    public @interface BookmarkType {}

    public static final int BOOKMARK_YES = 1;
    public static final int BOOKMARK_NO = 0;

    public static final int LIKE_YES = 1;
    public static final int LIKE_NEUTRAL = 0;
    public static final int LIKE_NO = -1;

    public static final String SUCCESS = "success";

    public static final String FAILURE = "Fail";

    public static final String PERSONALISE_CATEGORY_CITY = "city";
    public static final String PERSONALISE_CATEGORY_NEWS = "news";

    public static final String PS_GROUP_DEFAULT_SECTIONS = "GROUP_DEFAULT_SECTIONS";
    public static final String PS_Briefing = "Briefing";
    public static final String PS_My_Stories = "My Stories";
    public static final String PS_Suggested = "Suggested";
    public static final String PS_Profile = "Profile";
    public static final String PS_Url = "Url";
    public static final String PS_ADD_ON_SECTION = "ADD_ON_SECTION";
    public static final String PS_SENSEX = "SENSEX";




}
