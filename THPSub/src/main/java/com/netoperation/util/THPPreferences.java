package com.netoperation.util;

import android.content.Context;
import android.content.SharedPreferences;

public class THPPreferences {
    Context context;
    private static THPPreferences instance = null;

    private SharedPreferences mPref;
    private SharedPreferences.Editor editor;
    private  final String TPHpref = "tphpref";

    public static final int PERMISSION_ACCEPT_INIT = 0;
    public static final int PERMISSION_ACCEPTED_ALL = 1;
    public static final int PERMISSION_WITHOUT_NEVER_AGAIN = 2;
    public static final int PERMISSION_WITH_NEVER_AGAIN = 3;


    private THPPreferences(Context context) {
        this.context = context;
        mPref = context.getSharedPreferences(TPHpref, Context.MODE_PRIVATE);
        editor = mPref.edit();
    }

    public static THPPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new THPPreferences(context);
        }
        return instance;
    }

    public void clearPref() {
        editor.clear();
        editor.commit();
    }

    /**
     * It saves either email or mobile no.
     */
    public void saveLoginId(String loginId) {
        editor.putString("loginId", loginId);
        editor.commit();
    }

    public String getLoginId() {
        return mPref.getString("loginId", "");
    }

    /**
     * It saves User id which is provided from server
     * @param userId
     */
    public void saveUserId(String userId) {
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getUserId() {
        return mPref.getString("userId", "");
    }


    public void saveLoginPasswd(String loginPasswd) {
        editor.putString("loginPasswd", loginPasswd);
        editor.commit();
    }

    public String getLoginPasswd() {
        return mPref.getString("loginPasswd", "");
    }

    public void setTHPPermissionAccepted(int isAccepted) {
        editor.putInt("permissionAll", isAccepted);
        editor.commit();
    }

    public int getTHPPermissionAccepted() {
        return mPref.getInt("permissionAll", PERMISSION_ACCEPT_INIT);
    }

    public void setIsUserLoggedIn(boolean isUserLoggedIn) {
        editor.putBoolean("isUserLoggedIn", isUserLoggedIn);
        editor.commit();
    }

    public boolean isUserLoggedIn() {
        return mPref.getBoolean("isUserLoggedIn", false);
    }

    public void setIsUserAdsFree(boolean isUserAdsFree) {
        editor.putBoolean("isUserAdsFree", isUserAdsFree);
        editor.commit();
    }

    public boolean isUserAdsFree() {
        return mPref.getBoolean("isUserAdsFree", false);
    }

    public void setUserLoggedName(String userLoggedName) {
        editor.putString("userLoggedName", userLoggedName);
        editor.commit();
    }



    public String getUserLoggedName() {
        return mPref.getString("userLoggedName", "");
    }

    /*
     * This condition is being used, when user is login or signup
     * first time then all ads should be removed.
     */
    public void setIsRefreshRequired(boolean isRefreshRequired) {
        editor.putBoolean("isRefreshRequired", isRefreshRequired);
        editor.commit();
    }

    public boolean isRefreshRequired() {
        return mPref.getBoolean("isRefreshRequired", false);
    }


    public void setIsSubscribeClose(boolean isSubscribeClose) {
        editor.putBoolean("isSubscribeClose", isSubscribeClose);
        editor.commit();
    }

    public boolean isSubscribeClose() {
        return mPref.getBoolean("isSubscribeClose", false);
    }

    public void setIsMPBannerClose(boolean isSubscribeClose) {
        editor.putBoolean("isMPBannerClose", isSubscribeClose);
        editor.commit();
    }

    public boolean isMPBannerClose() {
        return mPref.getBoolean("isMPBannerClose", false);
    }

    public void setIsRelogginSuccess(boolean isRelogginSuccess) {
        editor.putBoolean("IsRelogginSuccess", isRelogginSuccess);
        editor.commit();
    }

    public boolean isRelogginSuccess() {
        return mPref.getBoolean("IsRelogginSuccess", false);
    }

    public void setLoginSource(String loginSource) {
        editor.putString("loginSource", loginSource);
        editor.commit();
    }

    public String getLoginSource() {
        return mPref.getString("loginSource", "AppDirect");
    }




    public void setStatusTest(String value) {
        editor.putString("statusTest", value);
        editor.commit();
    }

    public String getStatus() {
        return mPref.getString("statusTest", "");
    }
}
