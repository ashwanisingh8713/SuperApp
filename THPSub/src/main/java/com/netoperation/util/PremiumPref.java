package com.netoperation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PremiumPref {
    Context context;
    private static PremiumPref instance = null;

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private  final String TPHpref = "PremiumPref.xml";

    public static final int PERMISSION_ACCEPT_INIT = 0;
    public static final int PERMISSION_ACCEPTED_ALL = 1;
    public static final int PERMISSION_WITHOUT_NEVER_AGAIN = 2;
    public static final int PERMISSION_WITH_NEVER_AGAIN = 3;


    private PremiumPref(Context context) {
        this.context = context;
        mPreferences = context.getSharedPreferences(TPHpref, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public static PremiumPref getInstance(Context context) {
        if (instance == null) {
            instance = new PremiumPref(context);
        }
        return instance;
    }

    public void clearPref() {
        mEditor.clear();
        mEditor.commit();
        Log.i("AshwaniError", ":: clearPref");
    }

    /**
     * It saves either email or mobile no.
     */
    public void saveLoginTypeId(String loginId) {
        mEditor.putString("loginTypeId", loginId);
        mEditor.commit();
    }

    public String getLoginTypeId() {
        return mPreferences.getString("loginTypeId", "");
    }

    /**
     * It saves User id which is provided from server
     * @param userId
     */
    public void saveUserId(String userId) {
        mEditor.putString("userId", userId);
        mEditor.commit();
    }

    public String getUserId() {
        return mPreferences.getString("userId", "");
    }


    public void saveLoginPasswd(String loginPasswd) {
        mEditor.putString("loginPasswd", loginPasswd);
        mEditor.commit();
    }

    public String getLoginPasswd() {
        return mPreferences.getString("loginPasswd", "");
    }

    public void setTHPPermissionAccepted(int isAccepted) {
        mEditor.putInt("permissionAll", isAccepted);
        mEditor.commit();
    }

    public int getTHPPermissionAccepted() {
        return mPreferences.getInt("permissionAll", PERMISSION_ACCEPT_INIT);
    }

    public void setIsUserLoggedIn(boolean isUserLoggedIn) {
        mEditor.putBoolean("isUserLoggedIn", isUserLoggedIn);
        mEditor.commit();
    }

    public boolean isUserLoggedIn() {
        return mPreferences.getBoolean("isUserLoggedIn", false);
    }

    public void setIsUserAdsFree(boolean isUserAdsFree) {
        mEditor.putBoolean("isUserAdsFree", isUserAdsFree);
        mEditor.commit();
    }

    public boolean isUserAdsFree() {
        return mPreferences.getBoolean("isUserAdsFree", false);
    }

    public void setUserLoggedName(String userLoggedName) {
        mEditor.putString("userLoggedName", userLoggedName);
        mEditor.commit();
    }

    public boolean isHasSubscription() {
        return mPreferences.getBoolean("isHasSubscription", false);
    }

    public void setHasSubscription(boolean isHasSubscription) {
        mEditor.putBoolean("isHasSubscription", isHasSubscription);
        mEditor.commit();
    }



    public String getUserLoggedName() {
        return mPreferences.getString("userLoggedName", "");
    }

    /*
     * This condition is being used, when user is login or signup
     * first time then all ads should be removed.
     */
    public void setIsRefreshRequired(boolean isRefreshRequired) {
        mEditor.putBoolean("isRefreshRequired", isRefreshRequired);
        mEditor.commit();
    }

    public boolean isRefreshRequired() {
        return mPreferences.getBoolean("isRefreshRequired", false);
    }


    public void setIsSubscribeClose(boolean isSubscribeClose) {
        mEditor.putBoolean("isSubscribeClose", isSubscribeClose);
        mEditor.commit();
    }

    public boolean isSubscribeClose() {
        return mPreferences.getBoolean("isSubscribeClose", false);
    }

    public void setIsMPBannerClose(boolean isSubscribeClose) {
        mEditor.putBoolean("isMPBannerClose", isSubscribeClose);
        mEditor.commit();
    }

    public boolean isMPBannerClose() {
        return mPreferences.getBoolean("isMPBannerClose", false);
    }

    public void setIsRelogginSuccess(boolean isRelogginSuccess) {
        mEditor.putBoolean("IsRelogginSuccess", isRelogginSuccess);
        mEditor.commit();
    }

    public boolean isRelogginSuccess() {
        return mPreferences.getBoolean("IsRelogginSuccess", false);
    }

    public void setLoginSource(String loginSource) {
        mEditor.putString("loginSource", loginSource);
        mEditor.commit();
    }

    public String getLoginSource() {
        return mPreferences.getString("loginSource", "AppDirect");
    }

    public void setStatusTest(String value) {
        mEditor.putString("statusTest", value);
        mEditor.commit();
    }

    public String getStatus() {
        return mPreferences.getString("statusTest", "");
    }

    public void setPremiumContentBaseUrl(String premiumContentBaseUrl) {
        mEditor.putString("premiumContentBaseUrl", premiumContentBaseUrl);
        mEditor.commit();
    }

    public String getPremiumContentBaseUrl() {
        return mPreferences.getString("premiumContentBaseUrl", "");
    }
}
