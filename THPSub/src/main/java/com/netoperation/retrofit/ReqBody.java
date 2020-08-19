package com.netoperation.retrofit;


import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.netoperation.util.RetentionDef;
import com.ns.thpremium.BuildConfig;


public class ReqBody {

    public static final String REQUEST_SOURCE = "app";

    public static JsonObject ups( String packageName, String resolution) {
        JsonObject object = new JsonObject();
        object.addProperty("packageName", packageName);
        object.addProperty("resolution", resolution);
        return object;
    }

    public static JsonObject configuration(String resolution) {
        JsonObject object = new JsonObject();
        object.addProperty("authKey", BuildConfig.CONFIG_AUTH_KEY);
        object.addProperty("resolution", resolution);
        object.addProperty("id", BuildConfig.CONFIG_PRODUCTION_ID);
        return object;
    }

    public static JsonObject configurationUpdateCheck() {
        JsonObject object = new JsonObject();
        object.addProperty("authKey", BuildConfig.CONFIG_AUTH_KEY);
        object.addProperty("id", BuildConfig.CONFIG_PRODUCTION_ID);
        return object;
    }

    public static JsonObject login(String emailId, String contact, String password, String deviceId, String siteId, String originUrl) {
        JsonObject object = new JsonObject();
        object.addProperty("password", password);
        object.addProperty("emailId", emailId);
        object.addProperty("contact", contact);
        object.addProperty("deviceId", deviceId);
        object.addProperty("siteId", siteId);
        object.addProperty("originUrl", originUrl);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject signUp(String otp, String countryCode, String password, String emailId, String contact, String deviceId, String siteId, String originUrl) {
        JsonObject object = new JsonObject();
        object.addProperty("otp", otp);
        //object.addProperty("countryCode", countryCode);
        object.addProperty("password", password);
        object.addProperty("emailId", emailId);
        object.addProperty("contact", contact);
        object.addProperty("deviceId", deviceId);
        object.addProperty("siteId", siteId);
        object.addProperty("originUrl", originUrl);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject logout(String userId, String siteId, String  deviceId) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("deviceId", deviceId);
        object.addProperty("siteId", siteId);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }


    public static JsonObject userVerification(String emailId, String contact, String siteId, @RetentionDef.userVerificationMode String event) {
        JsonObject object = new JsonObject();
        object.addProperty("emailId", emailId);
        object.addProperty("contact", contact);
        object.addProperty("siteId", siteId);
        object.addProperty("event", event);
        return object;
    }

    public static JsonObject resetPassword(String otp, String password, String countryCode, String emailId,
                                           String siteId, String originUrl, String contact) {
        JsonObject object = new JsonObject();
        object.addProperty("otp", otp);
        object.addProperty("password", password);
        object.addProperty("countryCode", countryCode);
        object.addProperty("emailId", emailId);
        object.addProperty("siteId", siteId);
        object.addProperty("originUrl", originUrl);
        object.addProperty("contact", contact);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject userInfo(String deviceId, String siteId, String userId) {
        JsonObject object = new JsonObject();
        object.addProperty("deviceId", deviceId);
        object.addProperty("siteId", siteId);
        object.addProperty("userId", userId);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject editProfile(String userId, String siteId, String emailId, String contact) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("siteId", siteId);
        object.addProperty("emailId", emailId);
        object.addProperty("contact", contact);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject validateOtp(String otp, String emailOrContact, String siteId) {
        JsonObject object = new JsonObject();
        object.addProperty("otp", otp);
        object.addProperty("userName", emailOrContact);
        object.addProperty("siteId", siteId);
        return object;
    }


    public static JsonObject updatePassword(String userId, String oldPassword, String newPassword, String siteId) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("siteId", siteId);
        object.addProperty("oldPassword", oldPassword);
        object.addProperty("newPassword", newPassword);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject suspendAccount(String userId, String siteId, String deviceId, String emailId, String contact, String otp) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("siteId", siteId);
        object.addProperty("deviceId", deviceId);
        object.addProperty("emailId", emailId);
        object.addProperty("contact", contact);
        object.addProperty("otp", otp);
        object.addProperty("event", "suspendAccount");
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject deleteAccount(@NonNull String userId, @NonNull String siteId, @NonNull String deviceId, @NonNull String emailId, @NonNull String contact, @NonNull String otp) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("siteId", siteId);
        object.addProperty("deviceId", deviceId);
        object.addProperty("emailId", emailId);
        object.addProperty("contact", contact);
        object.addProperty("otp", otp);
        object.addProperty("event", "deleteAccount");
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject setUserPreference(@NonNull String userId, @NonNull String siteId, @NonNull String deviceId, @NonNull JsonObject preferences) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("siteId", siteId);
        object.addProperty("deviceId", deviceId);
        object.addProperty("event", "set");
        object.add("preferences", preferences);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject getUserPreference(@NonNull String userId, @NonNull String siteId, @NonNull String deviceId) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("siteId", siteId);
        object.addProperty("deviceId", deviceId);
        object.addProperty("event", "get");
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject createBookmarkFavLike(@NonNull String userId, @NonNull String siteId,
                                                   @NonNull String contentId, @NonNull int isBookmark,
                                                   @NonNull int isFavourite) {
        JsonObject object = new JsonObject();
        object.addProperty("userId", userId);
        object.addProperty("siteId", siteId);
        object.addProperty("contentId", contentId);
        object.addProperty("isBookmark", isBookmark);
        object.addProperty("isFavourite", isFavourite);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject updateProfile(String emailId, String contact, String siteId,
                                           String userId, String FullName, String DOB,
                                           String Gender, String Profile_Country, String Profile_State) {
        JsonObject object = new JsonObject();
        object.addProperty("emailId", emailId);
        object.addProperty("contact", contact);
        object.addProperty("siteId", siteId);
        object.addProperty("userId", userId);
        object.addProperty("FullName", FullName);
        object.addProperty("DOB", DOB);
        object.addProperty("Gender", Gender);
        object.addProperty("Profile_Country", Profile_Country);
        object.addProperty("Profile_State", Profile_State);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject updateAddress(String emailId, String contact, String siteId, String userId,
                                           String address_house_no, String address_street, String address_landmark,
                                           String address_pincode, String address_state, String address_city,
                                            String address_default_option, String address_fulllname) {
        JsonObject object = new JsonObject();
        object.addProperty("emailId", emailId);
        object.addProperty("contact", contact);
        object.addProperty("siteId", siteId);
        object.addProperty("userId", userId);
        object.addProperty("address_fulllname", address_fulllname);
        object.addProperty("address_house_no", address_house_no);
        object.addProperty("address_street", address_street);
        object.addProperty("address_landmark", address_landmark);
        object.addProperty("address_pincode", address_pincode);
        object.addProperty("address_state", address_state);
        object.addProperty("address_city", address_city);
        object.addProperty("address_default_option", address_default_option);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }

    public static JsonObject createSubscription(String userid,
                                                String trxnid,
                                                String amt,
                                                String channel,
                                                String siteid,
                                                String planid,
                                                String plantype,
                                                String billingchannel,
                                                String validity,
                                                String contact,
                                                String currency,
                                                String tax,
                                                String netAmount) {
        JsonObject object = new JsonObject();
        object.addProperty("userid", userid);
        object.addProperty("trxnid", trxnid);
        object.addProperty("amt", amt);
        object.addProperty("channel", channel);
        object.addProperty("siteid", siteid);
        object.addProperty("planid", planid);
        object.addProperty("plantype", plantype);
        object.addProperty("billingchannel", billingchannel);
        object.addProperty("validity", validity);
        object.addProperty("contact", contact);
        object.addProperty("currency", currency);
        object.addProperty("tax", tax);
        object.addProperty("netAmount", netAmount);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;

    }


    public static JsonObject socialLogin(String deviceId, String originUrl, String provider,
                                         String socialId, String userEmail, String userName) {
        JsonObject object = new JsonObject();
        object.addProperty("deviceId", deviceId);
        object.addProperty("originUrl", originUrl);
        object.addProperty("provider", provider);
        object.addProperty("socialId", socialId);
        object.addProperty("userEmail", userEmail);
        object.addProperty("userName", userName);
        object.addProperty("requestSource", REQUEST_SOURCE);
        return object;
    }


    public static JsonObject checksumHashAPIBody(String userId, int planId, String contact, String country) {
        JsonObject object = new JsonObject();
        object.addProperty("userid", userId);
        object.addProperty("planid", planId);
        object.addProperty("contact", contact);
        object.addProperty("country", country); //Get the country name from reco plan info
        object.addProperty("channel", "android");
        return object;
    }

    public static JsonObject detailEventCapture(String osVersion, String os, String section,
                                                String contact, String email, String pageTitle, String userid,
                                                String eventTime, String aid, String page, String deviceid,
                                                String pageUrl, String siteId) {
        JsonObject object = new JsonObject();
        object.addProperty("OsVersion", osVersion);
        object.addProperty("Os", "Android");
        object.addProperty("section", section);
        object.addProperty("contact", contact);
        object.addProperty("emailId", email);
        object.addProperty("pageTitle", pageTitle);
        object.addProperty("userId", userid);
        object.addProperty("eTime", eventTime);
        object.addProperty("aid", aid);
        object.addProperty("page", page);
        object.addProperty("deviceId", deviceid);
        object.addProperty("pageUrl", pageUrl);
        object.addProperty("siteId", siteId);
        object.addProperty("event", "Detail Page Swipe");
        return object;

    }

    public static JsonObject freePlan(String userId, String contact, String trxnid, String siteid) {
        JsonObject object = new JsonObject();
        object.addProperty("userid", userId);
        object.addProperty("trxnid", trxnid);
        object.addProperty("contact", contact);
        object.addProperty("amt","0.0");
        object.addProperty("channel","android");
        object.addProperty("siteid", siteid);
        object.addProperty("planid","249");
        object.addProperty("plantype","Subscription");
        object.addProperty("billingchannel","paytm");
        object.addProperty("validity","14-days");
        object.addProperty("currency","INR");
        object.addProperty("netAmount","0.0");
        object.addProperty("requestSource", REQUEST_SOURCE);

        return object;
    }

    public static JsonObject forceUpdate() {
        JsonObject object = new JsonObject();
        /*object.addProperty("device", "android");
        object.addProperty("app_version", BuildConfig.VERSION_NAME);
        object.addProperty("os_version", android.os.Build.VERSION.SDK_INT);*/
        object.addProperty("packageName", BuildConfig.APPLICATION_ID);
        return object;
    }

    public static JsonObject sectionList() {
        JsonObject object = new JsonObject();
        object.addProperty("device", "android");
        object.addProperty("api_key", "hindu@9*M");
        object.addProperty("app_version", BuildConfig.VERSION_NAME);
        object.addProperty("os_version", android.os.Build.VERSION.SDK_INT);
        return object;
    }

    public static JsonObject homeFeed(JsonArray personliseSectionIds, String bannerId, long lut) {
        JsonObject object = new JsonObject();
        object.addProperty("device", "android");
        object.addProperty("api_key", "hindu@9*M");
        object.addProperty("app_version", BuildConfig.VERSION_NAME);
        object.addProperty("os_version", android.os.Build.VERSION.SDK_INT);
        object.addProperty("bannerId", bannerId);
        object.addProperty("lut", lut);
        object.add("sec_id", personliseSectionIds);

        return object;
    }

    public static JsonObject sectionContent(final String id, final int page, final String type, long lut) {
        JsonObject object = new JsonObject();
        object.addProperty("device", "android");
        object.addProperty("api_key", "hindu@9*M");
        object.addProperty("app_version", BuildConfig.VERSION_NAME);
        object.addProperty("os_version", android.os.Build.VERSION.SDK_INT);
        object.addProperty("id", id);
        object.addProperty("type", type);
        object.addProperty("lut", lut);
        object.addProperty("page", page);
        return object;
    }


}
