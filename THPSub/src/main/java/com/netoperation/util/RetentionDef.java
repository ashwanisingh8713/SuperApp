package com.netoperation.util;


import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static com.netoperation.util.NetConstants.EVENT_CHANGE_ACCOUNT_STATUS;
import static com.netoperation.util.NetConstants.EVENT_FORGOT_PASSWORD;
import static com.netoperation.util.NetConstants.EVENT_SIGNUP;
import static com.netoperation.util.NetConstants.API_bookmarks;
import static com.netoperation.util.NetConstants.API_briefcase;
import static com.netoperation.util.NetConstants.API_Mystories;
import static com.netoperation.util.NetConstants.API_suggested;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface RetentionDef {

    @Retention(SOURCE)
    @StringDef({EVENT_SIGNUP, EVENT_FORGOT_PASSWORD, EVENT_CHANGE_ACCOUNT_STATUS})
    @interface userVerificationMode {}

    @Retention(SOURCE)
    @StringDef({API_Mystories, API_suggested, API_briefcase})
    @interface Recomendation {}
}
