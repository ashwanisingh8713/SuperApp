package com.netoperation.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by ashwanisingh on 22/09/18.
 */

public class UserPref {


    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private static UserPref mUser;


    private UserPref(Context context) {
        try {
            mPreferences = context.getSharedPreferences("userPref.xml", Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
        } catch (NumberFormatException e) {

        }

    }

    public static final UserPref getInstance(Context context) {
        if(mUser == null) {
            try {
                mUser = new UserPref(context);
            } catch (Exception e) {

            }
        }

        return mUser;
    }

    public void setHomeArticleOptionScreenShown(boolean isHomeArticleOptionScreenShown) {
        mEditor.putBoolean("isHomeArticleOptionScreenShown", isHomeArticleOptionScreenShown);
        mEditor.apply();
    }

    public boolean isHomeArticleOptionScreenShown() {
        return mPreferences.getBoolean("isHomeArticleOptionScreenShown", false);
    }

    public void setLastUpdateTime(String sectionName) {
        mEditor.putLong(sectionName, System.currentTimeMillis());
        mEditor.apply();
    }


    public long getLastUpdateTime(String sectionName) {
        return mPreferences.getLong(sectionName, 1484567729999l);
    }

    public void clearLastUpdateTime(String sectionName) {
        mEditor.putLong(sectionName, 1484567729999l);
        mEditor.commit();
    }

    public boolean isRequiredToRefresh(String sectionName) {
        long currentTimeInMilli = System.currentTimeMillis();
        long lastUpdatedTimeInMilli = getLastUpdateTime(sectionName);

        long differenceTimeInMilli = currentTimeInMilli-lastUpdatedTimeInMilli;
        int minuts = 10;
        return differenceTimeInMilli > 1 * 1000 * 60 * minuts;
    }

    public void setBannerId(String bannerId) {
        mEditor.putString("bannerId", bannerId);
        mEditor.apply();
    }

    public int getBannerId() {
        return mPreferences.getInt("bannerId", 43);
    }

    public void setDescriptionSize(int size) {
        mEditor.putInt("current_size", size);
        mEditor.commit();
    }

    public int getDescriptionSize() {
        return mPreferences.getInt("current_size", 2);
    }

    public void setTheme(int themeVal) {
        mEditor.putInt("themeVal", themeVal);
        mEditor.commit();
    }


    public int getThemeVal() {
        return mPreferences.getInt("themeVal", 1);
    }


    public boolean isUserSelectedDfpConsent() {
        return mPreferences.getBoolean("isUserSelectedDfpConsent", false);
    }

    public boolean isDfpConsentExecuted() {
        return mPreferences.getBoolean("isDfpConsentExecuted", false);
    }

    public boolean isUserPreferAdsFree() {
        return mPreferences.getBoolean("isUserPreferAdsFree", false) && mPreferences.getBoolean("isUserFromEurope", false);
    }

    public boolean isUserFromEurope() {
        return mPreferences.getBoolean("isUserFromEurope", false);
    }


    public void setUserSelectedDfpConsent(boolean isUserSelectedDfpConsent) {
        mEditor.putBoolean("isUserSelectedDfpConsent", isUserSelectedDfpConsent);
        mEditor.apply();
    }

    public void setDfpConsentExecuted(boolean isDfpConsentExecuted) {
        mEditor.putBoolean("isDfpConsentExecuted", isDfpConsentExecuted);
        mEditor.apply();
    }

    public void setUserPreferAdsFree(boolean isUserPreferAdsFree) {
        mEditor.putBoolean("isUserPreferAdsFree", isUserPreferAdsFree);
        mEditor.apply();
    }

    public void setUserFromEurope(boolean isUserFromEurope) {
        mEditor.putBoolean("isUserFromEurope", isUserFromEurope);
        mEditor.apply();
    }

    public void enableDeviceType() {
        mEditor.putBoolean("deviceType", true);
        mEditor.apply();
    }

    public void disableDeviceType() {
        mEditor.putBoolean("deviceType", false);
        mEditor.apply();
    }

    public boolean isDeviceTypeEnabled() {
        return mPreferences.getBoolean("deviceType", false);
    }


    public void setSelectedLocale(String locale) {
        mEditor.putString("locale", locale);
        mEditor.commit();
    }

    public String getSelectedLocale() {
        return mPreferences.getString("locale", "en");
    }

    public void setLanguageSupportTTS(int isLanguageSupportTTS) {
        mEditor.putInt("isLanguageSupportTTS", isLanguageSupportTTS);
        mEditor.commit();
    }

    public int isLanguageSupportTTS() {
        return mPreferences.getInt("isLanguageSupportTTS", -1);
    }

    public void setUserTheme(boolean isDayTheme) {
        mEditor.putBoolean("isDayTheme", isDayTheme);
        mEditor.commit();
    }

    public boolean isUserThemeDay() {
        return mPreferences.getBoolean("isDayTheme", true);
    }


     /*
    save permission dialog cancel
     */
    public void savePermissionDialogPreference(boolean value) {
        mEditor.putBoolean("userDialogPreference", value);
        mEditor.commit();
    }

    public boolean getPermissionDialogPreference() {
        return mPreferences.getBoolean("userDialogPreference",false );
    }

    /*MP Preferences start*/
    public void setMeteredPaywallEnabled(boolean isMpFeatureEnabled) {
        mEditor.putBoolean("isMeteredPaywallEnabled", isMpFeatureEnabled);
        mEditor.commit();
    }

    public boolean isMeteredPaywallEnabled() {
        return mPreferences.getBoolean("isMeteredPaywallEnabled",false );
    }

    public void setMPBannerCloseIdsPrefs(Set<String> bannerCloseIds){
        mEditor.putStringSet("mpBannerCloseIds", bannerCloseIds);
        mEditor.apply();
    }

    public Set<String> getMPBannerClosedIdsPrefs(){
        return mPreferences.getStringSet("mpBannerCloseIds",new HashSet<>());
    }
    /*MP Preferences end*/
}
