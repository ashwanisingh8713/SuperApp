package com.netoperation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.netoperation.default_db.TableConfiguration;
import com.ns.activity.BaseAcitivityTHP;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ashwanisingh on 22/09/18.
 */

public class DefaultPref {


    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private static DefaultPref mUser;
    private static long SYNC_UP_DURATION = 0l;

    private DefaultPref(Context context) {
        try {
            mPreferences = context.getSharedPreferences("DefaultPref.xml", Context.MODE_PRIVATE);
            mEditor = mPreferences.edit();
        } catch (NumberFormatException e) {

        }

    }

    public static final DefaultPref getInstance(Context context) {
        if(mUser == null) {
            try {
                mUser = new DefaultPref(context);
            } catch (Exception e) {

            }
        }

        return mUser;
    }


    public void setIsFullScreenAdLoaded(boolean isFullScreenAdLoaded) {
        mEditor.putBoolean("isFullScreenAdLoaded", isFullScreenAdLoaded);
        mEditor.apply();
    }

    public boolean isFullScreenAdLoaded() {
        return mPreferences.getBoolean("isFullScreenAdLoaded", false);
    }

    public void setInterstetial_Ads_Shown(boolean Interstetial_Ads_Shown) {
        mEditor.putBoolean("Interstetial_Ads_Shown", Interstetial_Ads_Shown);
        mEditor.commit();
    }

    public boolean getInterstetial_Ads_Shown() {
        return mPreferences.getBoolean("Interstetial_Ads_Shown", false);
    }


    public void setNotificationEnable(boolean isNotificationEnable) {
        mEditor.putBoolean("isNotificationEnable", isNotificationEnable);
        mEditor.apply();
    }

    public boolean isNotificationEnable() {
        return mPreferences.getBoolean("isNotificationEnable", true);
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

    public void setDescriptionSize(int size) {
        mEditor.putInt("current_size", size);
        mEditor.commit();
    }

    public int getDescriptionSize() {
        return mPreferences.getInt("current_size", 2);
    }


    public boolean isUserSelectedDfpConsent() {
        return mPreferences.getBoolean("isUserSelectedDfpConsent", false);
    }

    public boolean isDfpConsentExecuted() {
        return mPreferences.getBoolean("isDfpConsentExecuted", false);
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

//    MP Preferences start
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

    /* Store Start time after reading first article*/
    public void setMPStartTimeInMillis(long startTimeInMillis) {
        mEditor.putLong("mpStartTimeInMillis", startTimeInMillis);
        mEditor.apply();
    }
    /*Get Start time in Millis*/
    public long getMPStartTimeInMillis() {
        return mPreferences.getLong("mpStartTimeInMillis", 0);
    }
    /*Store expiry of MP duration in Millis*/
    public void setMPExpiryTimeInMillis(long expiryTimeInMillis) {
        mEditor.putLong("mpExpiryTimeInMillis", expiryTimeInMillis);
        mEditor.apply();
    }
    /*Get expiry time in Millis*/
    private long getMPExpiryTimeInMillis() {
        return mPreferences.getLong("mpExpiryTimeInMillis", 0);
    }

    /*Set Allowed article counts*/
    public void setMPAllowedArticlesCount(int counts) {
        mEditor.putInt("mpAllowedCounts", counts);
        mEditor.apply();
    }

    /*Get Allowed article counts*/
    public int getMPAllowedArticleCounts() {
        return mPreferences.getInt("mpAllowedCounts", -1);
    }

    public boolean isMpCycleOnceLoaded() {
        return mPreferences.getLong("mpExpiryTimeInMillis", 0)>0;
    }
    /*Calculate Time Difference - If Duration of uses Exhausted then stop hitting API for Cycle*/
    public boolean isMPDurationExpired() {
        long startTimeInMillis = getMPStartTimeInMillis();
        long currentTimeInMillis = System.currentTimeMillis();
        long difference = currentTimeInMillis - startTimeInMillis;
        long expiryTimeInMillis = getMPExpiryTimeInMillis();
        return difference >= expiryTimeInMillis;
    }
//    MP Preferences end

    public void setConfigurationOnceLoaded(boolean isConfigurationOnceLoaded) {
        mEditor.putBoolean("isConfigurationOnceLoaded", isConfigurationOnceLoaded);
        mEditor.apply();
    }

    public boolean isConfigurationOnceLoaded() {
        return mPreferences.getBoolean("isConfigurationOnceLoaded", false);
    }

//    Set and get any preferences start
    public void putBoolean(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public boolean getBoolean(String key) {
        return mPreferences.getBoolean(key, false);
    }
//    Set and get any preferences end


    public void setDefaultContentBaseUrl(String defaultContentBaseUrl) {
        mEditor.putString("defaultContentBaseUrl", defaultContentBaseUrl);
        mEditor.commit();
    }

    public String getDefaultContentBaseUrl() {
        return mPreferences.getString("defaultContentBaseUrl", "");
    }

    public void setNewsDigestUrl(String newsDigestUrl) {
        mEditor.putString("newsDigestUrl", newsDigestUrl);
        mEditor.commit();
    }

    public String getNewsDigestUrl() {
        return mPreferences.getString("newsDigestUrl", "");
    }

    public void setOldBookmarkLoaded(boolean isOldBookmarkLoaded) {
        mEditor.putBoolean("isOldBookmarkLoaded", isOldBookmarkLoaded);
        mEditor.apply();
    }

    public boolean isOldBookmarkLoaded() {
        return mPreferences.getBoolean("isOldBookmarkLoaded", false);
    }

    public void setUserJourneyLoaded(boolean isUserJourneyLoaded) {
        mEditor.putBoolean("isUserJourneyLoaded", isUserJourneyLoaded);
        mEditor.apply();
    }

    public boolean isUserJourneyLoaded() {
        return mPreferences.getBoolean("isUserJourneyLoaded", false);
    }

    public void setLocationEnabled(boolean isEnabled) {
        mEditor.putBoolean("LOCATION_ENABLE", isEnabled);
        mEditor.apply();
    }

    public boolean isLocationEnabled() {
        return mPreferences.getBoolean("LOCATION_ENABLE", false);
    }

    public void saveSectionSyncTimePref(String sectionId) {
        mEditor.putLong(sectionId, System.currentTimeMillis());
        mEditor.apply();
    }

    public boolean isSectionNeedToSync(String sectionId) {
        long lastSyncTime = mPreferences.getLong(sectionId, 0);
        long currentTimeInMilli = System.currentTimeMillis();
        long lastSyncDuration = currentTimeInMilli - lastSyncTime;
        if(SYNC_UP_DURATION == 0l) {
            int SYNC_UP_MINUTE = 10;
            try {
                TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
                if(tableConfiguration != null) {
                    SYNC_UP_MINUTE = Integer.parseInt(tableConfiguration.getRefreshIntervalInMins());
                }
            }
            catch (NumberFormatException e) {
                System.out.println("SYNC_UP_MINUTE" + " is not a valid integer number");
            }

            SYNC_UP_DURATION = (SYNC_UP_MINUTE * 1000 * 60);
        }
        return lastSyncDuration > SYNC_UP_DURATION;
    }


    public void saveGuideOverlay(String listing, String detail) {
        String urls = listing+"===="+detail;
        mEditor.putString("guideOverlay", urls);
        mEditor.apply();
    }

    public String getGuideOverlayUrl(boolean isListing) {
        String urlStr = mPreferences.getString("guideOverlay", null);
        if(urlStr == null) {
            return null;
        }
        String urls [] = urlStr.split("====");
        if(isListing && urls.length > 0) {
            return urls[0];
        }
        else if(urls.length > 1){
            return urls[1];
        } else {
            return "";
        }
    }

    public void setConfigLUT(String configLut) {
        mEditor.putString("configLut", configLut);
        mEditor.commit();
    }

    public String getConfigLUT() {
        return mPreferences.getString("configLut", "");
    }
}
