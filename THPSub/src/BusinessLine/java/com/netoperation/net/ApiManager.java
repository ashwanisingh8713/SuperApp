package com.netoperation.net;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.netoperation.db.BookmarkTable;
import com.netoperation.db.BreifingTable;
import com.netoperation.db.DashboardTable;
import com.netoperation.db.MPTable;
import com.netoperation.db.MPTableDao;
import com.netoperation.db.THPDB;
import com.netoperation.db.UserProfileDao;
import com.netoperation.db.UserProfileTable;
import com.netoperation.model.BreifingModelNew;
import com.netoperation.model.KeyValueModel;
import com.netoperation.model.MPConfigurationModel;
import com.netoperation.model.MPCycleDurationModel;
import com.netoperation.model.MorningBean;
import com.netoperation.model.PaytmModel;
import com.netoperation.model.PaytmTransactionStatus;
import com.netoperation.model.PersonaliseDetails;
import com.netoperation.model.PersonaliseModel;
import com.netoperation.model.PrefListModel;
import com.netoperation.model.RecoBean;
import com.netoperation.model.RecomendationData;
import com.netoperation.model.SearchedArticleModel;
import com.netoperation.model.TransactionHistoryModel;
import com.netoperation.model.TxnDataBean;
import com.netoperation.model.UserProfile;
import com.netoperation.retrofit.ReqBody;
import com.netoperation.retrofit.ServiceFactory;
import com.netoperation.util.NetConstants;
import com.netoperation.util.RetentionDef;
import com.netoperation.util.THPPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ApiManager {

    private static final String TAG = ApiManager.class.getCanonicalName();

    private ApiManager() {
    }

    public static void userVerification(RequestCallback<KeyValueModel> callback,
                                        String email, String contact, String siteId, @RetentionDef.userVerificationMode String event) {
        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().userVerification(ReqBody.userVerification(email, contact, siteId, event));
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(value -> {
                            KeyValueModel keyValueModel = new KeyValueModel();

                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                keyValueModel.setState(status);
                                if (status.equalsIgnoreCase("success")) {

                                } else {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                }
                            }
                            return keyValueModel;
                        }
                )
                .subscribe(value -> {
                    if (callback != null) {
                        callback.onNext(value);
                    }

                }, throwable -> {
                    if (callback != null) {
                        callback.onError(throwable, NetConstants.EVENT_SIGNUP);
                    }
                }, () -> {
                    if (callback != null) {
                        callback.onComplete(NetConstants.EVENT_SIGNUP);
                    }
                });
    }


    public static void resetPassword(RequestCallback<KeyValueModel> callback, String otp, String password, String countryCode, String emailId, String siteId, String originUrl, String contact) {
        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().resetPassword(ReqBody.resetPassword(otp, password, countryCode, emailId, siteId, originUrl, contact));
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(value -> {
                            KeyValueModel keyValueModel = new KeyValueModel();
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                keyValueModel.setState(status);
                                if (status.equalsIgnoreCase("success")) {

                                } else {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                }
                            }
                            return keyValueModel;
                        }
                )
                .subscribe(value -> {
                    if (callback != null) {
                        callback.onNext(value);
                    }

                }, throwable -> {
                    if (callback != null) {
                        callback.onError(throwable, NetConstants.EVENT_SIGNUP);
                    }
                }, () -> {
                    if (callback != null) {
                        callback.onComplete(NetConstants.EVENT_SIGNUP);
                    }
                });
    }

    public static Observable<Boolean> generateOtp(String email, String contact, String siteId, String otpEventType) {

        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().userVerification(ReqBody.userVerification(email, contact, siteId, otpEventType));
        return observable.subscribeOn(Schedulers.newThread())
                .map(value -> {
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                return status.equalsIgnoreCase("success");
                            }
                            return false;
                        }
                );
    }

    public static void validateOTP(RequestCallback<Boolean> callback, String otp, String emailOrContact) {

        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().validateOtp(ReqBody.validateOtp(otp, emailOrContact));
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(value -> {
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                return status.equalsIgnoreCase("success");
                            }
                            return false;
                        }
                )
                .subscribe(value -> {
                    if (callback != null) {
                        callback.onNext(value);
                    }

                }, throwable -> {
                    if (callback != null) {
                        callback.onError(throwable, NetConstants.EVENT_SIGNUP);
                    }
                }, () -> {
                    if (callback != null) {
                        callback.onComplete(NetConstants.EVENT_SIGNUP);
                    }
                });
    }



    public static Observable<KeyValueModel> userSignUp(Context context, String otp, String countryCode, String password, String email, String contact, String deviceId, String siteId, String originUrl) {
        return ServiceFactory.getServiceAPIs().signup(ReqBody.signUp(otp, countryCode, password, email, contact, deviceId, siteId, originUrl))
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                            KeyValueModel keyValueModel = new KeyValueModel();
                            if (context != null) {

                                THPDB db = THPDB.getInstance(context);
                                // Clearing All Database
                                clearDB(db, context);

                                if (((JsonObject) value).has("status")) {
                                    String status = ((JsonObject) value).get("status").getAsString();
                                    keyValueModel.setState(status);
                                    if (status.equalsIgnoreCase("success")) {

                                        String userInfo = ((JsonObject) value).get("userInfo").getAsString();

                                        JSONObject obj = new JSONObject(userInfo);

                                        String emailId = ((JsonObject) value).get("emailId").getAsString();
                                        String contact_ = ((JsonObject) value).get("contact").getAsString();
                                        String redirectUrl = ((JsonObject) value).get("redirectUrl").getAsString();
                                        String userId = ((JsonObject) value).get("userId").getAsString();
                                        String reason = ((JsonObject) value).get("reason").getAsString();
                                        String token = ((JsonObject) value).get("token").getAsString();

                                        if(context != null) {
                                            String loginId = "";
                                            if(email != null && !TextUtils.isEmpty(email)) {
                                                loginId = email;
                                            }
                                            else if(contact != null && !TextUtils.isEmpty(contact)) {
                                                loginId = contact;
                                            }
                                            THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                                            THPPreferences.getInstance(context).saveLoginId(loginId);
                                            THPPreferences.getInstance(context).saveLoginPasswd(password);
                                            THPPreferences.getInstance(context).setIsRefreshRequired(true);
                                            THPPreferences.getInstance(context).setIsUserAdsFree(true);
                                            THPPreferences.getInstance(context).saveUserId(userId);

                                            keyValueModel.setContact(loginId);
                                        }

                                        keyValueModel.setUserId(userId);
                                        keyValueModel.setToken(token);

                                        String authors_preference = obj.getString("authors_preference");
                                        String cities_preference = obj.getString("cities_preference");
                                        String topics_preference = obj.getString("topics_preference");

                                        String address_state = obj.getString("address_state");
                                        String address_pincode = obj.getString("address_pincode");
                                        String address_house_no = obj.getString("address_house_no");
                                        String address_city = obj.getString("address_city");
                                        String address_street = obj.getString("address_street");
                                        String address_fulllname = obj.getString("address_fulllname");
                                        String address_landmark = obj.getString("address_landmark");
                                        String address_default_option = obj.getString("address_default_option");
                                        String address_location = obj.getString("address_location");

                                        String Profile_Country = obj.getString("Profile_Country");
                                        String Profile_State = obj.getString("Profile_State");

                                        String FullName = obj.getString("FullName");
                                        String Gender = obj.getString("Gender");
                                        String DOB = obj.getString("DOB");

                                        String isNew = ((JsonObject) value).get("isNew").getAsString();
                                        String fid = ((JsonObject) value).get("fid").getAsString();
                                        String tid = ((JsonObject) value).get("tid").getAsString();
                                        String gid = ((JsonObject) value).get("gid").getAsString();


                                        UserProfile userProfile = new UserProfile();

                                        userProfile.setEmailId(emailId);
                                        userProfile.setContact(contact_);
                                        userProfile.setRedirectUrl(redirectUrl);
                                        userProfile.setUserId(userId);
                                        userProfile.setReason(reason);

                                        userProfile.setAuthors_preference(authors_preference);
                                        userProfile.setCities_preference(cities_preference);
                                        userProfile.setTopics_preference(topics_preference);

                                        userProfile.setAddress_state(address_state);
                                        userProfile.setAddress_pincode(address_pincode);
                                        userProfile.setAddress_house_no(address_house_no);
                                        userProfile.setAddress_city(address_city);
                                        userProfile.setAddress_street(address_street);
                                        userProfile.setAddress_fulllname(address_fulllname);
                                        userProfile.setAddress_landmark(address_landmark);
                                        userProfile.setAddress_default_option(address_default_option);
                                        userProfile.setAddress_location(address_location);

                                        userProfile.setProfile_Country(Profile_Country);
                                        userProfile.setProfile_State(Profile_State);

                                        userProfile.setFullName(FullName);
                                        userProfile.setGender(Gender);
                                        userProfile.setDOB(DOB);

                                        userProfile.setIsNew(isNew);
                                        userProfile.setFid(fid);
                                        userProfile.setTid(tid);
                                        userProfile.setGid(gid);
                                        userProfile.setAuthorization(token);

                                        userProfile.setHasFreePlan(true);

                                        UserProfileTable userProfileTable = new UserProfileTable(userId, userProfile);

                                        db.userProfileDao().insertUserProfile(userProfileTable);

                                        String email_Contact = emailId;
                                        if (email_Contact == null || TextUtils.isEmpty(email_Contact)) {
                                            email_Contact = contact_;
                                        }

                                    }

                                } else {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                }

                            }
                            return keyValueModel;
                        }
                )
                .delay(2, TimeUnit.SECONDS);
    }



    public static Observable<KeyValueModel> userLogin(Context context, String email, String contact,
                                 String siteId, String password, String deviceId, String originUrl, boolean isClearDB) {

        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().login(ReqBody.login(email, contact, password, deviceId, siteId, originUrl));
        return observable.subscribeOn(Schedulers.newThread())
                .map(value -> {
                                KeyValueModel keyValueModel = new KeyValueModel();
                                if (((JsonObject) value).has("status")) {
                                    String status = ((JsonObject) value).get("status").getAsString();
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    if (((JsonObject) value).has("userId")) {

                                        if(isClearDB && context != null) {
                                            // Clearing All Database
                                            THPDB db = THPDB.getInstance(context);
                                            clearDB(db, context);
                                        }
                                        String userId = ((JsonObject) value).get("userId").getAsString();
                                        keyValueModel.setCode(userId);
                                        String token = ((JsonObject) value).get("token").getAsString();
                                        keyValueModel.setToken(token);
                                    }
                                    keyValueModel.setState(status);
                                    keyValueModel.setName(reason);
                            }
                            return keyValueModel;
                        }
                );

    }


    /**
     * Get User info from server
     * @param context
     * @param siteId
     * @param deviceId
     * @param usrId
     * @return
     */
    public static Observable<Boolean> getUserInfo(Context context, String authorization, String siteId, String deviceId, String usrId, String loginId, String loginPasswd) {

        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().userInfo(authorization, ReqBody.userInfo(deviceId, siteId, usrId));
        return observable.subscribeOn(Schedulers.newThread())
                .map(responseFromServer -> {
                            if (((JsonObject) responseFromServer).has("status")) {
                                String status = ((JsonObject) responseFromServer).get("status").getAsString();
                                if (status.equalsIgnoreCase("success")) {

                                    if(context != null) {
                                        THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                                        THPPreferences.getInstance(context).saveLoginId(loginId);
                                        THPPreferences.getInstance(context).saveLoginPasswd(loginPasswd);
                                    }

                                    String userInfo = ((JsonObject) responseFromServer).get("userInfo").getAsString();

                                    JSONObject userInfoJsonObj = new JSONObject(userInfo);

                                    String emailId = ((JsonObject) responseFromServer).get("emailId").getAsString();
                                    String contact_ = ((JsonObject) responseFromServer).get("contact").getAsString();
                                    String redirectUrl = "";
                                    String reason = "";
                                    if(((JsonObject) responseFromServer).has("redirectUrl")) {
                                        redirectUrl = ((JsonObject) responseFromServer).get("redirectUrl").getAsString();
                                    }
                                    String userId = ((JsonObject) responseFromServer).get("userId").getAsString();
                                    if(((JsonObject) responseFromServer).has("reason")) {
                                        reason = ((JsonObject) responseFromServer).get("reason").getAsString();
                                    }

                                    String authors_preference = userInfoJsonObj.getString("authors_preference");
                                    String cities_preference = userInfoJsonObj.getString("cities_preference");
                                    String topics_preference = userInfoJsonObj.getString("topics_preference");

                                    String address_state = userInfoJsonObj.getString("address_state");
                                    String address_pincode = userInfoJsonObj.getString("address_pincode");
                                    String address_house_no = userInfoJsonObj.getString("address_house_no");
                                    String address_city = userInfoJsonObj.getString("address_city");
                                    String address_street = userInfoJsonObj.getString("address_street");
                                    String address_fulllname = userInfoJsonObj.getString("address_fulllname");
                                    String address_landmark = userInfoJsonObj.getString("address_landmark");
                                    String address_default_option = userInfoJsonObj.getString("address_default_option");
                                    String address_location = userInfoJsonObj.getString("address_location");


                                    String Profile_Country;
                                    if (userInfoJsonObj.has("Profile_Country"))
                                    Profile_Country = userInfoJsonObj.getString("Profile_Country");
                                    else
                                        Profile_Country = "";
                                    String Profile_State;
                                    if (userInfoJsonObj.has("Profile_State"))
                                    Profile_State = userInfoJsonObj.getString("Profile_State");
                                    else
                                        Profile_State = "";

                                    String FullName;
                                    if (userInfoJsonObj.has("FullName"))
                                        FullName = userInfoJsonObj.getString("FullName");
                                    else
                                        FullName = "";
                                    String Gender;
                                    if (userInfoJsonObj.has("Gender"))
                                        Gender = userInfoJsonObj.getString("Gender");
                                    else
                                        Gender = "";

                                    String DOB;
                                    if (userInfoJsonObj.has("DOB"))
                                        DOB = userInfoJsonObj.getString("DOB");
                                    else
                                        DOB = "";

                                    String isNew = "";
                                    if(((JsonObject) responseFromServer).has("isNew")) {
                                        isNew = ((JsonObject) responseFromServer).get("isNew").getAsString();
                                    }

                                    String fid = ((JsonObject) responseFromServer).get("fid").getAsString();
                                    String tid = ((JsonObject) responseFromServer).get("tid").getAsString();
                                    String gid = ((JsonObject) responseFromServer).get("gid").getAsString();
                                    String loginSource = ((JsonObject) responseFromServer).get("loginSource").getAsString();
                                    String userMigrated = ((JsonObject) responseFromServer).get("userMigrated").getAsString();

                                    String nextRenewalDate = null;
                                    SimpleDateFormat dateFormat = null;
                                    Date nextRenewalDateObj = null;
                                    if(((JsonObject) responseFromServer).has("nextRenewalDate")) {
                                        nextRenewalDate = ((JsonObject) responseFromServer).get("nextRenewalDate").getAsString();
                                        try {
                                            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            nextRenewalDateObj = dateFormat.parse(nextRenewalDate);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    final UserProfile userProfile = new UserProfile();


                                    if(((JsonObject) responseFromServer).has("userPlanList")) {
                                        String jsonArray = ((JsonObject) responseFromServer).get("userPlanList").toString();
                                        JSONArray userPlanList = new JSONArray(jsonArray);
                                        int planSize = userPlanList.length();
                                        for (int i = 0; i < planSize; i++) {
                                            JSONObject object = (JSONObject) userPlanList.get(i);
                                            String planId = object.getString("planId");
                                            String planName = object.getString("planName");
                                            double amount = object.getDouble("amount");
                                            String statusPlan = object.getString("status");
                                            String validity = object.getString("validity");
                                            String nextRenewal = object.getString("nextRenewal");
                                            String sDate = object.getString("sDate");
                                            String eDate = object.getString("eDate");
                                            int isActive = object.getInt("isActive");


                                            if(nextRenewalDateObj != null && dateFormat != null) {
                                                final Date nextRenewalPlanDateObj = dateFormat.parse(nextRenewal);
                                                if (nextRenewalDateObj.after(nextRenewalPlanDateObj)) {
                                                    userProfile.setHasSubscribedPlan(true);
                                                }
                                            }

                                            final String freeTrialPlanId = "10";
                                            // PlanId = 10, has "30 days free trial"
                                            if(planId.equals(freeTrialPlanId) && isActive == 1) {
                                                userProfile.setHasFreePlan(true);
                                            }

                                            // If user free plan is not available then this will check for subscribed user
                                            if( !planId.equals(freeTrialPlanId) && (isActive == 1)) {
                                                userProfile.setHasSubscribedPlan(true);
                                            }

                                            TxnDataBean bean = new TxnDataBean();
                                            bean.setAmount(amount);
                                            bean.setPlanId(planId);
                                            bean.setPlanName(planName);
                                            bean.setTrxnstatus(statusPlan);
                                            bean.setValidity(validity);
                                            bean.setNextRenewal(nextRenewal);
                                            bean.setsDate(sDate);
                                            bean.seteDate(eDate);
                                            bean.setIsActive(isActive);

                                            userProfile.addUserPlanList(bean);
                                        }
                                    }

                                    THPDB thpdb = THPDB.getInstance(context);
                                    // Deleting Previous Profile DB
                                    thpdb.userProfileDao().deleteAll();

                                    userProfile.setAuthorization(authorization);
                                    userProfile.setEmailId(emailId);
                                    userProfile.setContact(contact_);
                                    userProfile.setRedirectUrl(redirectUrl);
                                    userProfile.setUserId(userId);
                                    userProfile.setReason(reason);

                                    userProfile.setAuthors_preference(authors_preference);
                                    userProfile.setCities_preference(cities_preference);
                                    userProfile.setTopics_preference(topics_preference);

                                    userProfile.setAddress_state(address_state);
                                    userProfile.setAddress_pincode(address_pincode);
                                    userProfile.setAddress_house_no(address_house_no);
                                    userProfile.setAddress_city(address_city);
                                    userProfile.setAddress_street(address_street);
                                    userProfile.setAddress_fulllname(address_fulllname);
                                    userProfile.setAddress_landmark(address_landmark);
                                    userProfile.setAddress_default_option(address_default_option);
                                    userProfile.setAddress_location(address_location);

                                    userProfile.setProfile_Country(Profile_Country);
                                    userProfile.setProfile_State(Profile_State);

                                    userProfile.setFullName(FullName);
                                    userProfile.setGender(Gender);
                                    userProfile.setDOB(DOB);

                                    userProfile.setIsNew(isNew);
                                    userProfile.setFid(fid);
                                    userProfile.setTid(tid);
                                    userProfile.setGid(gid);
                                    userProfile.setLoginSource(loginSource);
                                    userProfile.setNextRenewalDate(nextRenewalDate);
                                    userProfile.setUserMigrated(userMigrated);

                                    UserProfileTable userProfileTable = new UserProfileTable(userId, userProfile);

                                    thpdb.userProfileDao().insertUserProfile(userProfileTable);

                                    String mUserLoggedName = null;
                                    if(userProfile != null && !TextUtils.isEmpty(userProfile.getFullName())) {
                                        mUserLoggedName = userProfile.getFullName().toUpperCase();

                                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getEmailId())) {
                                        mUserLoggedName = userProfile.getEmailId().toUpperCase();

                                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getContact())) {
                                        mUserLoggedName = userProfile.getContact().toUpperCase();
                                    }

                                    if(context != null) {
                                        boolean hasFreePlan = userProfile.isHasFreePlan();
                                        boolean hasSubscriptionPlan = userProfile.isHasSubscribedPlan();
                                        THPPreferences.getInstance(context).setUserLoggedName(mUserLoggedName);
                                        THPPreferences.getInstance(context).setIsUserAdsFree(hasFreePlan||hasSubscriptionPlan);
                                        THPPreferences.getInstance(context).saveUserId(userId);
                                    }

                                    return true;

                                }
                                else if (status.equalsIgnoreCase("Fail")) {
                                    if(context != null) {
                                        THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                                    }
                                }

                            }
                            return false;
                        }
                );

    }


    /**
     * Get User info from server
     * @param context
     * @param siteId
     * @param deviceId
     * @param usrId
     * @return
     */
    public static Observable<UserProfile> getUserInfoObject(Context context, final String authorization, String siteId, String deviceId, String usrId, String loginId, String loginPasswd) {

        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().userInfo(authorization, ReqBody.userInfo(deviceId, siteId, usrId));
        return observable.subscribeOn(Schedulers.newThread())
                .map(responseFromServer -> {
                            UserProfile userProfile = new UserProfile();
                            if (((JsonObject) responseFromServer).has("status")) {
                                String status = ((JsonObject) responseFromServer).get("status").getAsString();
                                if (status.equalsIgnoreCase("success")) {

                                    if(context != null) {
                                        THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                                        THPPreferences.getInstance(context).saveLoginId(loginId);
                                        THPPreferences.getInstance(context).saveLoginPasswd(loginPasswd);
                                    }

                                    String userInfo = ((JsonObject) responseFromServer).get("userInfo").getAsString();

                                    JSONObject userInfoJsonObj = new JSONObject(userInfo);

                                    String emailId = ((JsonObject) responseFromServer).get("emailId").getAsString();
                                    String contact_ = ((JsonObject) responseFromServer).get("contact").getAsString();
                                    String redirectUrl = "";
                                    String reason = "";
                                    if(((JsonObject) responseFromServer).has("redirectUrl")) {
                                        redirectUrl = ((JsonObject) responseFromServer).get("redirectUrl").getAsString();
                                    }
                                    String userId = ((JsonObject) responseFromServer).get("userId").getAsString();
                                    if(((JsonObject) responseFromServer).has("reason")) {
                                        reason = ((JsonObject) responseFromServer).get("reason").getAsString();
                                    }

                                    String authors_preference = userInfoJsonObj.getString("authors_preference");
                                    String cities_preference = userInfoJsonObj.getString("cities_preference");
                                    String topics_preference = userInfoJsonObj.getString("topics_preference");

                                    String address_state = userInfoJsonObj.getString("address_state");
                                    String address_pincode = userInfoJsonObj.getString("address_pincode");
                                    String address_house_no = userInfoJsonObj.getString("address_house_no");
                                    String address_city = userInfoJsonObj.getString("address_city");
                                    String address_street = userInfoJsonObj.getString("address_street");
                                    String address_fulllname = userInfoJsonObj.getString("address_fulllname");
                                    String address_landmark = userInfoJsonObj.getString("address_landmark");
                                    String address_default_option = userInfoJsonObj.getString("address_default_option");
                                    String address_location = userInfoJsonObj.getString("address_location");


                                    String Profile_Country;
                                    if (userInfoJsonObj.has("Profile_Country"))
                                        Profile_Country = userInfoJsonObj.getString("Profile_Country");
                                    else
                                        Profile_Country = "";
                                    String Profile_State;
                                    if (userInfoJsonObj.has("Profile_State"))
                                        Profile_State = userInfoJsonObj.getString("Profile_State");
                                    else
                                        Profile_State = "";

                                    String FullName;
                                    if (userInfoJsonObj.has("FullName"))
                                        FullName = userInfoJsonObj.getString("FullName");
                                    else
                                        FullName = "";
                                    String Gender;
                                    if (userInfoJsonObj.has("Gender"))
                                        Gender = userInfoJsonObj.getString("Gender");
                                    else
                                        Gender = "";

                                    String DOB;
                                    if (userInfoJsonObj.has("DOB"))
                                        DOB = userInfoJsonObj.getString("DOB");
                                    else
                                        DOB = "";

                                    String isNew = "";
                                    if(((JsonObject) responseFromServer).has("isNew")) {
                                        isNew = ((JsonObject) responseFromServer).get("isNew").getAsString();
                                    }

                                    String fid = ((JsonObject) responseFromServer).get("fid").getAsString();
                                    String tid = ((JsonObject) responseFromServer).get("tid").getAsString();
                                    String gid = ((JsonObject) responseFromServer).get("gid").getAsString();
                                    String loginSource = ((JsonObject) responseFromServer).get("loginSource").getAsString();

                                    String nextRenewalDate = null;
                                    SimpleDateFormat dateFormat = null;
                                    Date nextRenewalDateObj = null;
                                    if(((JsonObject) responseFromServer).has("nextRenewalDate")) {
                                        nextRenewalDate = ((JsonObject) responseFromServer).get("nextRenewalDate").getAsString();
                                        try {
                                            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            nextRenewalDateObj = dateFormat.parse(nextRenewalDate);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    String userMigrated = ((JsonObject) responseFromServer).get("userMigrated").getAsString();

                                    if(((JsonObject) responseFromServer).has("userPlanList")) {
                                        String jsonArray = ((JsonObject) responseFromServer).get("userPlanList").toString();
                                        JSONArray userPlanList = new JSONArray(jsonArray);
                                        int planSize = userPlanList.length();
                                        for (int i = 0; i < planSize; i++) {
                                            JSONObject object = (JSONObject) userPlanList.get(i);
                                            String planId = object.getString("planId");
                                            String planName = object.getString("planName");
                                            double amount = object.getDouble("amount");
                                            String statusPlan = object.getString("status");
                                            String validity = object.getString("validity");
                                            String nextRenewal = object.getString("nextRenewal");
                                            String sDate = object.getString("sDate");
                                            String eDate = object.getString("eDate");
                                            int isActive = object.getInt("isActive");


                                            if(nextRenewalDateObj != null && dateFormat != null) {
                                                final Date nextRenewalPlanDateObj = dateFormat.parse(nextRenewal);
                                                if (nextRenewalDateObj.after(nextRenewalPlanDateObj)) {
                                                    userProfile.setHasSubscribedPlan(true);
                                                }
                                            }

                                            final String freeTrialPlanId = "10";
                                            // PlanId = 10, has "30 days free trial"
                                            if(planId.equals(freeTrialPlanId) && isActive == 1) {
                                                userProfile.setHasFreePlan(true);
                                            }

                                            // If user free plan is not available then this will check for subscribed user
                                            if( !planId.equals(freeTrialPlanId) && (isActive == 1)) {
                                                userProfile.setHasSubscribedPlan(true);
                                            }

                                            TxnDataBean bean = new TxnDataBean();
                                            bean.setAmount(amount);
                                            bean.setPlanId(planId);
                                            bean.setPlanName(planName);
                                            bean.setTrxnstatus(statusPlan);
                                            bean.setValidity(validity);
                                            bean.setNextRenewal(nextRenewal);
                                            bean.setsDate(sDate);
                                            bean.seteDate(eDate);
                                            bean.setIsActive(isActive);

                                            userProfile.addUserPlanList(bean);
                                        }
                                    }

                                    THPDB thpdb = THPDB.getInstance(context);
                                    // Deleting Previous Profile DB
                                    thpdb.userProfileDao().deleteAll();

                                    userProfile.setAuthorization(authorization);
                                    userProfile.setEmailId(emailId);
                                    userProfile.setContact(contact_);
                                    userProfile.setRedirectUrl(redirectUrl);
                                    userProfile.setUserId(userId);
                                    userProfile.setReason(reason);

                                    userProfile.setAuthors_preference(authors_preference);
                                    userProfile.setCities_preference(cities_preference);
                                    userProfile.setTopics_preference(topics_preference);

                                    userProfile.setAddress_state(address_state);
                                    userProfile.setAddress_pincode(address_pincode);
                                    userProfile.setAddress_house_no(address_house_no);
                                    userProfile.setAddress_city(address_city);
                                    userProfile.setAddress_street(address_street);
                                    userProfile.setAddress_fulllname(address_fulllname);
                                    userProfile.setAddress_landmark(address_landmark);
                                    userProfile.setAddress_default_option(address_default_option);
                                    userProfile.setAddress_location(address_location);

                                    userProfile.setProfile_Country(Profile_Country);
                                    userProfile.setProfile_State(Profile_State);

                                    userProfile.setFullName(FullName);
                                    userProfile.setGender(Gender);
                                    userProfile.setDOB(DOB);

                                    userProfile.setIsNew(isNew);
                                    userProfile.setFid(fid);
                                    userProfile.setTid(tid);
                                    userProfile.setGid(gid);
                                    userProfile.setLoginSource(loginSource);
                                    userProfile.setNextRenewalDate(nextRenewalDate);
                                    userProfile.setUserMigrated(userMigrated);

                                    UserProfileTable userProfileTable = new UserProfileTable(userId, userProfile);

                                    thpdb.userProfileDao().insertUserProfile(userProfileTable);

                                    String mUserLoggedName = null;
                                    if(userProfile != null && !TextUtils.isEmpty(userProfile.getFullName())) {
                                        mUserLoggedName = userProfile.getFullName().toUpperCase();

                                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getEmailId())) {
                                        mUserLoggedName = userProfile.getEmailId().toUpperCase();

                                    } else if(userProfile != null && !TextUtils.isEmpty(userProfile.getContact())) {
                                        mUserLoggedName = userProfile.getContact().toUpperCase();
                                    }

                                    if(context != null) {
                                        boolean hasFreePlan = userProfile.isHasFreePlan();
                                        boolean hasSubscriptionPlan = userProfile.isHasSubscribedPlan();
                                        THPPreferences.getInstance(context).setUserLoggedName(mUserLoggedName);
                                        THPPreferences.getInstance(context).setIsUserAdsFree(hasFreePlan||hasSubscriptionPlan);
                                        THPPreferences.getInstance(context).saveUserId(userId);

                                    }

                                    return userProfile;

                                }
                                else if (status.equalsIgnoreCase("Fail")) {
                                    if(context != null) {
                                        THPPreferences.getInstance(context).setIsUserLoggedIn(false);
                                    }
                                }

                            }
                            return userProfile;
                        }
                );

    }

    public static Observable<List<RecoBean>> getRecommendationFromServer(final Context context, String userid,
                                                                         final @RetentionDef.Recomendation String recotype,
                                                                         String size, String siteid) {
        Observable<RecomendationData> observable = ServiceFactory.getServiceAPIs().getRecommendation(userid, recotype,
                size, siteid, ReqBody.REQUEST_SOURCE);
        return observable.subscribeOn(Schedulers.newThread())
                .map(value -> {
                            List<RecoBean> beans = new ArrayList<>();
                            if (value == null) {
                                return beans;
                            }
                            beans = value.getReco();
                            if (context == null) {
                                return beans;
                            }
                            THPDB thp = THPDB.getInstance(context);
                            if (beans != null && beans.size() > 0) {
                                if (!recotype.equalsIgnoreCase(NetConstants.RECO_TEMP_NOT_EXIST)) {
                                    thp.dashboardDao().deleteAll(recotype);
                                }
                                for (RecoBean bean : beans) {

                                    if (recotype.equalsIgnoreCase(NetConstants.RECO_bookmarks)) {
                                        BookmarkTable bookmarkTable = new BookmarkTable(bean.getArticleId(), bean);
                                        thp.bookmarkTableDao().insertBookmark(bookmarkTable);
                                    } else {
                                        BookmarkTable bookmarkTable = thp.bookmarkTableDao().getBookmarkArticle(bean.getArticleId());
                                        if (bookmarkTable != null && bean.getIsBookmark() == 1) {
                                            thp.bookmarkTableDao().updateBookmark(bean.getArticleId(), bean);
                                        } else if (bean.getIsBookmark() == 1) {
                                            BookmarkTable bookmarkkTable = new BookmarkTable(bean.getArticleId(), bean);
                                            thp.bookmarkTableDao().insertBookmark(bookmarkkTable);
                                        } else if (bean.getIsBookmark() == 0) {
                                            thp.bookmarkTableDao().deleteBookmarkArticle(bean.getArticleId());
                                        }

                                        DashboardTable dashboardTable = new DashboardTable(bean.getArticleId(), recotype, bean);
                                        thp.dashboardDao().insertDashboard(dashboardTable);
                                    }
                                }
                            }

                            if (beans == null) {
                                beans = new ArrayList<>();
                            }

                            return beans;
                        }
                );

    }


    public static Observable<List<RecoBean>> getRecommendationFromDB(final Context context,
                                                                     final @RetentionDef.Recomendation String recotype, String aid) {
        Observable<RecomendationData> observable = Observable.just(new RecomendationData());
        return observable.subscribeOn(Schedulers.newThread())
                .map(value -> {
                            List<RecoBean> beans = new ArrayList<>();
                            if (context == null) {
                                return beans;
                            }
                            THPDB thp = THPDB.getInstance(context);
                            if(recotype.equalsIgnoreCase(NetConstants.RECO_bookmarks)) {
                                List<BookmarkTable> bookmarkTable = thp.bookmarkTableDao().getAllBookmark();
                                if (bookmarkTable != null) {
                                    for (BookmarkTable dash : bookmarkTable) {
                                        beans.add(dash.getBean());
                                    }
                                }
                            } else if(aid != null && recotype.equalsIgnoreCase(NetConstants.RECO_TEMP_NOT_EXIST)) {
                                DashboardTable dashboardTable = thp.dashboardDao().getSingleDashboardBean(aid);
                                if (dashboardTable != null) {
                                    List<RecoBean> tempRecoBean = new ArrayList<>();
                                    tempRecoBean.add(dashboardTable.getBean());
                                    return tempRecoBean;
                                }
                            }
                            else {
                                List<DashboardTable> dashboardTable = thp.dashboardDao().getAllDashboardBean(recotype);
                                if (dashboardTable != null) {
                                    for (DashboardTable dash : dashboardTable) {
                                        beans.add(dash.getBean());
                                    }
                                }
                            }
                            return beans;
                        }
                );

    }


    public static Observable<RecoBean> isExistInBookmark(Context context, final String aid) {
        return Observable.just(aid)
                .subscribeOn(Schedulers.io())
                .map(articleId -> {
                    List<BookmarkTable> bookmarkTable = THPDB.getInstance(context).bookmarkTableDao().getBookmarkArticles(articleId);
                    if (bookmarkTable != null && bookmarkTable.size() > 0) {
                        return bookmarkTable.get(0).getBean();
                    }
                    return new RecoBean();
                })
                .observeOn(AndroidSchedulers.mainThread());

    }

    public static Observable createBookmark(Context context, final RecoBean articleBean) {
        return Observable.just(articleBean)
                .subscribeOn(Schedulers.io())
                .map(new Function<RecoBean, Boolean>() {
                    @Override
                    public Boolean apply(RecoBean articleBean) {
                        RecoBean bean = new RecoBean();
                        bean.setArticleId(articleBean.getArticleId());
                        bean.setArticleSection(articleBean.getArticleSection());
                        bean.setArticletitle(articleBean.getArticletitle());
                        bean.setArticletype(articleBean.getArticletype());
                        bean.setAuthor(articleBean.getAuthor());
                        bean.setThumbnailUrl(articleBean.getThumbnailUrl());
                        bean.setPubDate(articleBean.getPubDate());
                        bean.setPubDateTime(articleBean.getPubDateTime());
                        bean.setRecotype(articleBean.getRecotype());
                        bean.setRank(articleBean.getRank());
                        bean.setIsBookmark(articleBean.getIsBookmark());
                        bean.setIsFavourite(articleBean.getIsFavourite());
                        bean.setDescription(articleBean.getDescription());
                        bean.setShortDescription(articleBean.getShortDescription());
                        bean.setHasDescription(articleBean.getHasDescription());
                        bean.setMedia(articleBean.getMedia());

                        THPDB thpdb = THPDB.getInstance(context);
                        BookmarkTable bookmarkTable = new BookmarkTable(articleBean.getArticleId(), bean);
                        thpdb.bookmarkTableDao().insertBookmark(bookmarkTable);

                        DashboardTable dashboardTable = thpdb.dashboardDao().getSingleDashboardBean(articleBean.getArticleId());
                        if (dashboardTable != null) {
                            RecoBean recoBean = dashboardTable.getBean();
                            if(recoBean != null) {
                                recoBean.setIsBookmark(articleBean.getIsBookmark());
                                thpdb.dashboardDao().updateRecobean(articleBean.getArticleId(), recoBean);
                            }
                        }

                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<RecoBean> updateBookmark(Context context, String aid, int like) {
        Observable<String> observable = Observable.just(aid);
        return observable.subscribeOn(Schedulers.newThread())
                .map(new Function<String, RecoBean>() {
                    @Override
                    public RecoBean apply(String model) {

                        THPDB thp = THPDB.getInstance(context);

                        BookmarkTable bookmarkTable = thp.bookmarkTableDao().getBookmarkArticle(model);

                        if (bookmarkTable != null) {
                            RecoBean recoBean = bookmarkTable.getBean();
                            recoBean.setIsFavourite(like);
                            thp.bookmarkTableDao().updateBookmark(aid, recoBean);
                            return recoBean;
                        }

                        Log.i("", "");
                        return new RecoBean();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable createUnBookmark(Context context, final String aid) {
        return Observable.just(aid)
                .subscribeOn(Schedulers.io())
                .map(value -> {
                    return THPDB.getInstance(context).bookmarkTableDao().deleteBookmarkArticle(aid) == 1;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Observable<Boolean> createBookmarkFavLike(@NonNull String userId, @NonNull String siteId,
                                                            @NonNull String contentId, @NonNull int bookmarkVal,
                                                            @NonNull int favoriteVal) {
        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs()
                .createBookmarkFavLike(ReqBody.createBookmarkFavLike(userId, siteId, contentId, bookmarkVal, favoriteVal));
        return observable.subscribeOn(Schedulers.newThread())
                .map(value -> {
                    if (((JsonObject) value).has("status")) {
                        String status = ((JsonObject) value).get("status").getAsString();
                        if (status.equalsIgnoreCase("success")) {
                            return true;
                        } else if (status.equalsIgnoreCase("Fail")) {
                            return false;
                        }

                    }
                    return false;
                });
    }

    public static Observable isExistFavNdLike(Context context, final String aid) {
        return Observable.just(aid)
                .subscribeOn(Schedulers.io())
                .map(articleId -> {
                    THPDB thp = THPDB.getInstance(context);
                    DashboardTable dashboardTable = thp.dashboardDao().getSingleDashboardBean(aid);
                    if (dashboardTable != null) {
                        return dashboardTable.getBean().getIsFavourite();
                    }
                    return 0;
                })
                .observeOn(AndroidSchedulers.mainThread());

    }


    public static Observable isExistInAllArticle(Context context, final String aid) {
        return Observable.just(aid)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String aid) {
                        boolean isContain = false;

                        THPDB thp = THPDB.getInstance(context);

                        RecoBean recoBean = new RecoBean();
                        recoBean.setArticleId(aid);

                        DashboardTable dashboardTable = thp.dashboardDao().getSingleDashboardBean(aid);
                        if (dashboardTable != null) {
                            if (dashboardTable.getAid().equals(aid)) {
                                isContain = true;
                            }
                        }

                        if (!isContain) {

                        }

                        return isContain;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static final Observable<RecoBean> articleDetailFromServer(Context context, String aid, String url, String recoType) {
        url = url + aid;
        Observable<SearchedArticleModel> observable = ServiceFactory.getServiceAPIs().searchArticleByIDFromServer(url);
        return observable.subscribeOn(Schedulers.newThread())
                .map(new Function<SearchedArticleModel, RecoBean>() {
                    @Override
                    public RecoBean apply(SearchedArticleModel model) {

                        THPDB thp = THPDB.getInstance(context);

                        if(recoType != null && recoType.equalsIgnoreCase(NetConstants.RECO_bookmarks)) {
                            BookmarkTable bookmarkTable = thp.bookmarkTableDao().getBookmarkArticle(aid);
                            if (bookmarkTable != null) {
                                RecoBean recoBean = bookmarkTable.getBean();
                                if (model.getData().size() > 0) {
                                    recoBean.setDescription(model.getData().get(0).getDe());
                                    recoBean.setLeadText(model.getData().get(0).getAl());
                                    recoBean.setIMAGES(model.getData().get(0).getMe());
                                    recoBean.setYoutubeVideoId(model.getData().get(0).getYoutube_video_id());
                                    recoBean.setHasDescription(1);
                                    String thumbUrl = model.getData().get(0).getIm_thumbnail();
                                    if (thumbUrl == null || TextUtils.isEmpty(thumbUrl)) {
                                        thumbUrl = model.getData().get(0).getIm_thumbnail_v2();
                                    }
                                    ArrayList<String> tu = new ArrayList<>();
                                    tu.add(thumbUrl);
                                    recoBean.setThumbnailUrl(tu);

                                    thp.bookmarkTableDao().updateBookmark(aid, recoBean);
                                }
                                return recoBean;
                            }
                        } // End Bookmark
                        else {
                            DashboardTable dashboardTable = thp.dashboardDao().getSingleDashboardBean(aid);
                            if (dashboardTable != null) {
                                RecoBean recoBean = dashboardTable.getBean();
                                if (model.getData().size() > 0) {
                                    recoBean.setDescription(model.getData().get(0).getDe());
                                    recoBean.setLeadText(model.getData().get(0).getAl());
                                    recoBean.setIMAGES(model.getData().get(0).getMe());
                                    recoBean.setYoutubeVideoId(model.getData().get(0).getYoutube_video_id());
                                    recoBean.setHasDescription(1);
                                    String thumbUrl = model.getData().get(0).getIm_thumbnail();
                                    if(thumbUrl == null || TextUtils.isEmpty(thumbUrl)) {
                                        thumbUrl = model.getData().get(0).getIm_thumbnail_v2();
                                    }
                                    ArrayList<String> tu = new ArrayList<>();
                                    tu.add(thumbUrl);
                                    recoBean.setThumbnailUrl(tu);

                                    thp.dashboardDao().updateRecobean(aid, recoBean);

                                }
                                return recoBean;
                            }
                            else { // dashboardTable == null
                                RecoBean recoBean = new RecoBean();
                                if (model.getData().size() > 0) {
                                    recoBean.setDescription(model.getData().get(0).getDe());
                                    recoBean.setLeadText(model.getData().get(0).getAl());
                                    recoBean.setIMAGES(model.getData().get(0).getMe());
                                    recoBean.setYoutubeVideoId(model.getData().get(0).getYoutube_video_id());
                                    recoBean.setHasDescription(1);
                                    String thumbUrl = model.getData().get(0).getIm_thumbnail();
                                    if(thumbUrl == null || TextUtils.isEmpty(thumbUrl)) {
                                        thumbUrl = model.getData().get(0).getIm_thumbnail_v2();
                                    }
                                    ArrayList<String> tu = new ArrayList<>();
                                    tu.add(thumbUrl);
                                    recoBean.setThumbnailUrl(tu);
                                    recoBean.setTitle(model.getData().get(0).getTi());
                                    recoBean.setArticletitle(model.getData().get(0).getTi());
                                    recoBean.setSectionName(model.getData().get(0).getSname());
                                    recoBean.setArticleSection(model.getData().get(0).getSname());
                                    recoBean.setPubDateTime(model.getData().get(0).getGmt());
                                    recoBean.setArticleLink(model.getData().get(0).getAl());
                                    recoBean.setArticleId(""+model.getData().get(0).getAid());
                                    recoBean.setShortDescription(model.getData().get(0).getLe());

                                    DashboardTable dashboardTable1 = new DashboardTable(aid, NetConstants.RECO_TEMP_NOT_EXIST, recoBean);
                                    thp.dashboardDao().insertDashboard(dashboardTable1);
                                }
                                return recoBean;
                            }
                        }

                        Log.i("", "");
                        return new RecoBean();
                    }
                });

    }


    public static final Observable<RecoBean> articleDetailFromDB(Context context, String aid, String recoType) {
        return Observable.just("articleDetailFromDB")
        .subscribeOn(Schedulers.newThread())
                .map(new Function<String, RecoBean>() {
                    @Override
                    public RecoBean apply(String str) {

                        THPDB thp = THPDB.getInstance(context);

                        if(recoType != null && recoType.equalsIgnoreCase(NetConstants.RECO_bookmarks)) {
                            BookmarkTable bookmarkTable = thp.bookmarkTableDao().getBookmarkArticle(aid);
                            if(bookmarkTable != null) {
                                RecoBean recoBean = bookmarkTable.getBean();
                                return recoBean;
                            } else {
                                return new RecoBean();
                            }
                        }
                        else if(recoType != null && (recoType.equalsIgnoreCase(NetConstants.BREIFING_ALL)
                        || recoType.equalsIgnoreCase(NetConstants.BREIFING_MORNING)
                        || recoType.equalsIgnoreCase(NetConstants.BREIFING_NOON)
                        || recoType.equalsIgnoreCase(NetConstants.BREIFING_EVENING))) {
                            BreifingTable breifingTable = thp.breifingDao().getBreifingTable();
                            if(breifingTable != null) {
                                List<RecoBean> morning = null;
                                List<RecoBean> noon = null;
                                List<RecoBean> evening = null;

                                if(recoType.equalsIgnoreCase(NetConstants.BREIFING_ALL)) {
                                    morning = breifingTable.getMorning();
                                    noon = breifingTable.getNoon();
                                    evening = breifingTable.getEvening();
                                } else if(recoType.equalsIgnoreCase(NetConstants.BREIFING_MORNING)) {
                                    morning = breifingTable.getMorning();
                                } else if(recoType.equalsIgnoreCase(NetConstants.BREIFING_NOON)) {
                                    noon = breifingTable.getNoon();
                                } else if(recoType.equalsIgnoreCase(NetConstants.BREIFING_EVENING)) {
                                    evening = breifingTable.getEvening();
                                }

                                final List<RecoBean> allBreifing = new ArrayList<>();
                                if(morning != null && morning.size()>0) {
                                    allBreifing.addAll(morning);
                                }
                                if(noon != null && noon.size()>0) {
                                    allBreifing.addAll(noon);
                                }
                                if(evening != null && evening.size()>0) {
                                    allBreifing.addAll(evening);
                                }

                                for(RecoBean bean : allBreifing) {
                                    if(bean.getArticleId().equalsIgnoreCase(aid)) {
                                        return bean;
                                    }
                                }
                            }
                            return new RecoBean();
                        }
                        else {
                            DashboardTable dashboardTable = thp.dashboardDao().getSingleDashboardBean(aid);
                            if (dashboardTable != null) {
                                RecoBean recoBean = dashboardTable.getBean();
                                return recoBean;
                            } else {
                                return new RecoBean();
                            }
                        }

                    }
                });

    }


    public static Observable<RecoBean> updateLike(Context context, String aid, int like) {
        Observable<String> observable = Observable.just(aid);
        return observable.subscribeOn(Schedulers.newThread())
                .map(new Function<String, RecoBean>() {
                    @Override
                    public RecoBean apply(String model) {

                        THPDB thp = THPDB.getInstance(context);

                        DashboardTable dashboardTable = thp.dashboardDao().getSingleDashboardBean(aid);

                        if (dashboardTable != null) {
                            RecoBean recoBean = dashboardTable.getBean();
                            recoBean.setIsFavourite(like);
                            thp.dashboardDao().updateRecobean(aid, recoBean);
                            return recoBean;
                        }
                        Log.i("", "");
                        return new RecoBean();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * Gets Breifing Data from server
     * @param context
     * @param breifingUrl
     * @return
     */
    public static Observable<List<RecoBean>> getBreifingFromServer(final Context context, String breifingUrl, final String breifingType) {
        Observable<BreifingModelNew> observable = ServiceFactory.getServiceAPIs().getBriefing(breifingUrl);
        return observable.subscribeOn(Schedulers.newThread())
                .map(value -> {

                            final List<RecoBean> allBriefing = new ArrayList<>();

                            if (value == null) {
                                return allBriefing;
                            }

                            List<MorningBean> morningBeans = value.getMorning().getData();
                            List<MorningBean> noonBeans = value.getNoon().getData();
                            List<MorningBean> eveningBeans = value.getEvening().getData();

                            String morningTime = value.getMorning().getTime();
                            String noonTime = value.getNoon().getTime();
                            String eveningTime = value.getEvening().getTime();

                            /*List<MorningBean> morningBeans = value.getMorning();
                            List<MorningBean> noonBeans = value.getNoon();
                            List<MorningBean> eveningBeans = value.getEvening();*/


                            List<RecoBean> morningBriefing = new ArrayList<>();
                            List<RecoBean> noonBriefing = new ArrayList<>();
                            List<RecoBean> eveningBriefing = new ArrayList<>();

                            for (MorningBean bean : morningBeans) {
                                RecoBean reco = new RecoBean();
                                reco.setTimeForBriefing(morningTime);
                                reco.setArticleId(bean.getArticleId());
                                reco.setArticleSection(bean.getSectionName());
                                reco.setPubDate(bean.getOriginalDate());
                                reco.setPubDateTime(bean.getPublishedDate());
                                reco.setLocation(bean.getLocation());
                                reco.setTitle(bean.getTitle());
                                reco.setArticletitle(bean.getTitle());
                                reco.setArticleLink(bean.getArticleLink());
                                reco.setGmt(bean.getGmt());
                                reco.setYoutubeVideoId(bean.getYoutubeVideoId());
                                reco.setShortDescription(bean.getShortDescription());
                                reco.setDescription(bean.getDescription());
                                reco.setHasDescription(1);
                                reco.setVideoId(bean.getVideoId());
                                reco.setArticleType(bean.getArticleType());

                                String thumbUrl = bean.getThumbnailUrl();
                                ArrayList<String> tu = new ArrayList<>();
                                tu.add(thumbUrl);
                                reco.setThumbnailUrl(tu);

                                reco.setTimeToRead(bean.getTimeToRead());
                                reco.setAuthor(bean.getAuthor());
                                reco.setMedia(bean.getMedia());

                                reco.setLeadText(bean.getLeadText());

                                morningBriefing.add(reco);
                            }

                            for (MorningBean bean : noonBeans) {
                                RecoBean reco = new RecoBean();
                                reco.setTimeForBriefing(noonTime);
                                reco.setArticleId(bean.getArticleId());
                                reco.setArticleSection(bean.getSectionName());
                                reco.setPubDate(bean.getOriginalDate());
                                reco.setPubDateTime(bean.getPublishedDate());
                                reco.setLocation(bean.getLocation());
                                reco.setTitle(bean.getTitle());
                                reco.setArticletitle(bean.getTitle());
                                reco.setArticleLink(bean.getArticleLink());
                                reco.setGmt(bean.getGmt());
                                reco.setYoutubeVideoId(bean.getYoutubeVideoId());
                                reco.setShortDescription(bean.getShortDescription());
                                reco.setDescription(bean.getDescription());
                                reco.setHasDescription(1);
                                reco.setVideoId(bean.getVideoId());
                                reco.setArticleType(bean.getArticleType());

                                String thumbUrl = bean.getThumbnailUrl();
                                ArrayList<String> tu = new ArrayList<>();
                                tu.add(thumbUrl);
                                reco.setThumbnailUrl(tu);

                                reco.setTimeToRead(bean.getTimeToRead());
                                reco.setAuthor(bean.getAuthor());
                                reco.setMedia(bean.getMedia());

                                reco.setLeadText(bean.getLeadText());

                                noonBriefing.add(reco);
                            }

                            for (MorningBean bean : eveningBeans) {
                                RecoBean reco = new RecoBean();
                                reco.setTimeForBriefing(eveningTime);
                                reco.setArticleId(bean.getArticleId());
                                reco.setArticleSection(bean.getSectionName());
                                reco.setPubDate(bean.getOriginalDate());
                                reco.setPubDateTime(bean.getPublishedDate());
                                reco.setLocation(bean.getLocation());
                                reco.setTitle(bean.getTitle());
                                reco.setArticletitle(bean.getTitle());
                                reco.setArticleLink(bean.getArticleLink());
                                reco.setGmt(bean.getGmt());
                                reco.setYoutubeVideoId(bean.getYoutubeVideoId());
                                reco.setShortDescription(bean.getShortDescription());
                                reco.setDescription(bean.getDescription());
                                reco.setHasDescription(1);
                                reco.setVideoId(bean.getVideoId());
                                reco.setArticleType(bean.getArticleType());

                                String thumbUrl = bean.getThumbnailUrl();
                                ArrayList<String> tu = new ArrayList<>();
                                tu.add(thumbUrl);
                                reco.setThumbnailUrl(tu);

                                reco.setTimeToRead(bean.getTimeToRead());
                                reco.setAuthor(bean.getAuthor());
                                reco.setMedia(bean.getMedia());

                                reco.setLeadText(bean.getLeadText());

                                eveningBriefing.add(reco);
                            }

                            allBriefing.addAll(morningBriefing);
                            allBriefing.addAll(noonBriefing);
                            allBriefing.addAll(eveningBriefing);
                            if (context == null) {
                                return allBriefing;
                            }
                            THPDB thp = THPDB.getInstance(context);
                            thp.breifingDao().deleteAll();

                            BreifingTable breifingTable = new BreifingTable();
                            breifingTable.setEvening(eveningBriefing);
                            breifingTable.setNoon(noonBriefing);
                            breifingTable.setMorning(morningBriefing);
                            breifingTable.setMorningTime(morningTime);
                            breifingTable.setNoonTime(noonTime);
                            breifingTable.setEveningTime(eveningTime);
                            thp.breifingDao().insertBreifing(breifingTable);
                            if(breifingType.equals(NetConstants.BREIFING_ALL)) {
                                return allBriefing;
                            } else if(breifingType.equals(NetConstants.BREIFING_MORNING)) {
                                return morningBriefing;
                            } else if(breifingType.equals(NetConstants.BREIFING_NOON)) {
                                return noonBriefing;
                            } else if(breifingType.equals(NetConstants.BREIFING_EVENING)) {
                                return eveningBriefing;
                            } else {
                                return allBriefing;
                            }
                        }
                );

    }

    /**
     * Gets Breifing Data from local database
     * @param context
     * @param breifingType
     * @return
     */
    public static Observable<List<RecoBean>> getBreifingFromDB(final Context context, final String breifingType) {
        return Observable.just("BreifingItem")
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                            List<RecoBean> briefingItems = new ArrayList<>();

                            THPDB thp = THPDB.getInstance(context);

                            BreifingTable breifingTable = thp.breifingDao().getBreifingTable();

                            if(breifingTable == null) {
                                return briefingItems;
                            }
                            if(breifingType.equals(NetConstants.BREIFING_ALL)) {
                                briefingItems.addAll(breifingTable.getMorning());
                                briefingItems.addAll(breifingTable.getNoon());
                                briefingItems.addAll(breifingTable.getEvening());
                            } else if(breifingType.equals(NetConstants.BREIFING_MORNING)) {
                                briefingItems.addAll(breifingTable.getMorning());
                            } else if(breifingType.equals(NetConstants.BREIFING_NOON)) {
                                briefingItems.addAll(breifingTable.getNoon());
                            } else if(breifingType.equals(NetConstants.BREIFING_EVENING)) {
                                briefingItems.addAll(breifingTable.getEvening());
                            }

                            return briefingItems;
                        }
                );

    }

    /**
     * Gets Breifing Data from local database
     * @param context
     * @return
     */
    public static Observable<Map<String, String>> getBreifingTimeFromDB(final Context context) {
        return Observable.just("BreifingTime")
                .subscribeOn(Schedulers.newThread())
                .map(value -> {
                            Map<String, String> timeMap = new HashMap<>();

                            THPDB thp = THPDB.getInstance(context);

                            BreifingTable breifingTable = thp.breifingDao().getBreifingTable();

                            if (breifingTable == null) {
                                return timeMap;
                            }

                            String morningTime = breifingTable.getMorningTime();
                            String noonTime = breifingTable.getNoonTime();
                            String eveningTime = breifingTable.getEveningTime();

                            if (morningTime == null) {
                                morningTime = "";
                            }
                            if (noonTime == null) {
                                noonTime = "";
                            }
                            if (eveningTime == null) {
                                eveningTime = "";
                            }

                            timeMap.put("morningTime", morningTime);
                            timeMap.put("noonTime", noonTime);
                            timeMap.put("eveningTime", eveningTime);

                            if (breifingTable.getMorning() != null && breifingTable.getMorning().size() > 0) {
                                timeMap.put("morningEnable", "1");
                            }
                            if (breifingTable.getNoon() != null && breifingTable.getNoon().size() > 0) {
                                timeMap.put("noonEnable", "1");
                            }

                            if (breifingTable.getEvening() != null && breifingTable.getEvening().size() > 0) {
                                timeMap.put("eveningEnable", "1");
                            }

                            return timeMap;
                        }
                );

    }


    public static Observable<UserProfile> getUserProfile(Context context) {
        return Observable.just("userProfile")
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                            UserProfileDao dao = THPDB.getInstance(context).userProfileDao();
                            if (dao.getUserProfileTable() == null) {
                                return new UserProfile();
                            }
                            return dao.getUserProfileTable().getUserProfile();
                        }
                );
    }

    public static Observable<ArrayList<KeyValueModel>> getCountry() {
        return ServiceFactory.getServiceAPIs().getCountry("country")
                .subscribeOn(Schedulers.newThread())
                .map(countryModel->
                        countryModel
                );
    }

    public static Observable<ArrayList<KeyValueModel>> getState(String country) {
        return ServiceFactory.getServiceAPIs().getState("state", country)
                .subscribeOn(Schedulers.newThread())
                .map(stateModel->
                        stateModel
                );
    }

    public static Observable<KeyValueModel> updateProfile(Context context, UserProfile userProfile, String siteId,
                                                        String FullName, String DOB,
                                                        String Gender, String Profile_Country, String Profile_State) {
        return ServiceFactory.getServiceAPIs().updateProfile(ReqBody.updateProfile(userProfile.getEmailId(), userProfile.getContact(),
                siteId, userProfile.getUserId(), FullName, DOB, Gender, Profile_Country, Profile_State))
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                            KeyValueModel keyValueModel = new KeyValueModel();
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                keyValueModel.setState(status);
                                if (status.equalsIgnoreCase(NetConstants.SUCCESS)) {
                                    userProfile.setFullName(FullName);
                                    userProfile.setDOB(DOB);
                                    userProfile.setGender(Gender);
                                    userProfile.setProfile_Country(Profile_Country);
                                    userProfile.setProfile_State(Profile_State);
                                    THPDB thpdb = THPDB.getInstance(context);
                                    thpdb.userProfileDao().updateUserProfile(userProfile.getUserId(), userProfile);
                                } else {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                }
                            }
                            return keyValueModel;
                        }
                );
    }

    public static Observable<KeyValueModel> updateAddress(Context context, UserProfile userProfile, String siteId,
                                                    String address_house_no, String address_street, String address_landmark,
                                                    String address_pincode, String address_state, String address_city,
                                                     String address_default_option, String address_fulllname) {
        return ServiceFactory.getServiceAPIs().updateAddress(ReqBody.updateAddress(userProfile.getEmailId(),
                userProfile.getContact(), siteId, userProfile.getUserId(), address_house_no,  address_street, address_landmark,
                address_pincode, address_state, address_city, address_default_option, address_fulllname))
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                    KeyValueModel keyValueModel = new KeyValueModel();
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                keyValueModel.setState(status);
                                if (status.equalsIgnoreCase(NetConstants.SUCCESS)) {
                                    userProfile.setAddress_house_no(address_house_no);
                                    userProfile.setAddress_street(address_street);
                                    userProfile.setAddress_landmark(address_landmark);
                                    userProfile.setAddress_pincode(address_pincode);
                                    userProfile.setAddress_state(address_state);
                                    userProfile.setAddress_city(address_city);
                                    userProfile.setAddress_default_option(address_default_option);
                                    THPDB thpdb = THPDB.getInstance(context);
                                    thpdb.userProfileDao().updateUserProfile(userProfile.getUserId(), userProfile);
                                }
                                else {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                }
                            }
                            return keyValueModel;
                        }
                );
    }


    /**
     * To get transaction history
     * @param userId
     * @return
     */
    public static Observable<List<TxnDataBean>> getTxnHistory(String userId, String siteId) {
        return ServiceFactory.getServiceAPIs().getTxnHistory(userId, "0", siteId, "app")
                .subscribeOn(Schedulers.newThread())
                .map(jsonElement -> {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            TransactionHistoryModel txnModel = gson.fromJson(jsonElement.toString(), TransactionHistoryModel.class);
                            List<TxnDataBean> txnDataBeans = txnModel.getTxnData();
                            if (txnDataBeans == null) {
                                txnDataBeans = new ArrayList<>();
                            }
                            return txnDataBeans;
                        }
                );
    }

    /**
     * To update Password
     * @param userId
     * @param oldPasswd
     * @param newPasswd
     * @return
     */
    public static Observable<KeyValueModel> updatePassword(String userId, String oldPasswd, String newPasswd) {
        return ServiceFactory.getServiceAPIs().updatePassword(ReqBody.updatePassword(userId, oldPasswd, newPasswd))
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                            KeyValueModel keyValueModel = new KeyValueModel();
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                if (((JsonObject) value).has("reason")) {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                }
                                else if (((JsonObject) value).has("description")) {
                                    String description = ((JsonObject) value).get("description").getAsString();
                                    keyValueModel.setName(description);
                                }
                                keyValueModel.setState(status);

                            }

                            return keyValueModel;
                        }
                );
    }

    /**
     * To suspend user account
     * @param userId
     * @param siteId
     * @param deviceId
     * @param emailId
     * @param contact
     * @param otp
     * @return
     */
    public static Observable<KeyValueModel> suspendAccount(String userId, String siteId, String deviceId, String emailId, String contact, String otp) {
        return ServiceFactory.getServiceAPIs().suspendAccount(ReqBody.suspendAccount(userId, siteId, deviceId, emailId, contact, otp))
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                            KeyValueModel keyValueModel = new KeyValueModel();
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                //String reason = ((JsonObject) value).get("reason").getAsString();
                                //keyValueModel.setName(reason);
                                keyValueModel.setState(status);
                                if (((JsonObject) value).has("reason")) {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                } else if (((JsonObject) value).has("Description")) {
                                    String description = ((JsonObject) value).get("Description").getAsString();
                                    keyValueModel.setName(description);
                                }
                            }

                            return keyValueModel;
                        }
                );
    }

    /**
     * To Delete user account
     * @param userId
     * @param siteId
     * @param deviceId
     * @param emailId
     * @param contact
     * @param otp
     * @return
     */
    public static Observable<KeyValueModel> deleteAccount(String userId, String siteId, String deviceId, String emailId, String contact, String otp) {
        return ServiceFactory.getServiceAPIs().deleteAccount(ReqBody.deleteAccount(userId, siteId, deviceId, emailId, contact, otp))
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                            KeyValueModel keyValueModel = new KeyValueModel();
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                keyValueModel.setState(status);
                                //String reason = ((JsonObject) value).get("reason").getAsString();
                                //keyValueModel.setName(reason);
                                if (((JsonObject) value).has("reason")) {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                } else if (((JsonObject) value).has("Description")) {
                                    String description = ((JsonObject) value).get("Description").getAsString();
                                    keyValueModel.setName(description);
                                }
                            }

                            return keyValueModel;
                        }
                );
    }

    /**
     * To logout user
     * @param userId
     * @param siteId
     * @param deviceId
     * @return
     */
    public static Observable<KeyValueModel> logout(Context context, String authorization, String userId, String siteId, String deviceId) {
        return ServiceFactory.getServiceAPIs().logout(authorization, ReqBody.logout(userId, siteId, deviceId))
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                    KeyValueModel keyValueModel = new KeyValueModel();
                    if (((JsonObject) value).has("status")) {
                        String status = ((JsonObject) value).get("status").getAsString();
                        //Status is not in response as the Api Doc
                        if (((JsonObject) value).has("reason")) {
                            String reason = ((JsonObject) value).get("reason").getAsString();
                            keyValueModel.setName(reason);
                        }
                        keyValueModel.setState(status);
                    }

                    if (keyValueModel.getState() != null &&
                            keyValueModel.getState().equalsIgnoreCase("success")) {
                        THPDB db = THPDB.getInstance(context);
                        db.userProfileDao().deleteAll();
                    }

                    return keyValueModel;
                });
    }


    /**
     * To get user plan info
     * @param userId
     * @param siteId
     * @return
     */
    public static Observable<List<TxnDataBean>> getUserPlanInfo(String userId, String siteId) {
        return ServiceFactory.getServiceAPIs().getUserPlanInfo(userId, siteId, ReqBody.REQUEST_SOURCE)
                .subscribeOn(Schedulers.newThread())
                .map(value->
                        {
                            List<TxnDataBean> userPlanList = new ArrayList<>();
                            //Filter only Success response status
                            for (TxnDataBean userplan : value.getUserPlanList()) {
                                if (userplan.getStatus().equalsIgnoreCase("success")){
                                    userPlanList.add(userplan);
                                }
                            }
                            return userPlanList;
                        }
                );
    }

    /**
     * To get user plan info
     * @param userId
     * @param siteId
     * @return
     */
    public static Observable<List<TxnDataBean>> getRecommendedPlan(String userId, String siteId) {
        return ServiceFactory.getServiceAPIs().getRecommendedPlan(siteId, "25", "1", "1")
                .subscribeOn(Schedulers.newThread())
                .map(value->
                        value.getCamapignList()
                );
    }


    public static Observable<PrefListModel> getAllPreferences(String THP_PERSONALISE_URL) {
        Observable<PrefListModel> observable = ServiceFactory.getServiceAPIs().getAllPreferences(THP_PERSONALISE_URL);
        return observable.subscribeOn(Schedulers.newThread())
                .timeout(10000, TimeUnit.MILLISECONDS)
                .map(value -> {
                    // For topics
                    PersonaliseDetails topics = value.getTopics();
                    topics.setName(topics.getName());
                    value.addTopics(topics);

                    // For cities
                    PersonaliseDetails cities = value.getCities();
                    cities.setName(cities.getName());
                    value.addCities(cities);


                    // For authors
                    PersonaliseDetails authors = value.getAuthors();
                    authors.setName(authors.getName());
                    value.addAuthors(authors);

                    return value;

                });
    }

    public static Observable<KeyValueModel> setPersonalise(@NonNull String authorization, @NonNull String userId, @NonNull String siteId, @NonNull String deviceId, @NonNull ArrayList<String> topics,
                                                     @NonNull ArrayList<String> cities, @NonNull ArrayList<String> authors) {
        JsonObject personaliseObj = new JsonObject();

        JsonArray ja = new JsonArray();
        for(String topic : topics) {
            ja.add(topic);
        }
        personaliseObj.add("topics", ja);

        ja = new JsonArray();
        for(String city : cities) {
            ja.add(city);
        }
        personaliseObj.add("city", ja);

        ja = new JsonArray();
        for(String author : authors) {
            ja.add(author);
        }
        personaliseObj.add("author", ja);

        return ServiceFactory.getServiceAPIs().setPersonalise(authorization, ReqBody.setUserPreference(userId, siteId, deviceId, personaliseObj))
                .subscribeOn(Schedulers.newThread())
                .map(value-> {
                            KeyValueModel keyValueModel = new KeyValueModel();
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                keyValueModel.setState(status);
                                //Reason is not coming in response
                                if (((JsonObject) value).has("reason") && !((JsonObject) value).get("reason").isJsonNull()) {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                } else {
                                    keyValueModel.setName("");
                                }
                            }

                            return keyValueModel;
                        }
                );
    }

    public static Observable<PrefListModel> getUserSavedPersonalise(String userId, String siteId, String deviceId) {
        return ServiceFactory.getServiceAPIs().getPersonalise(ReqBody.getUserPreference(userId, siteId, deviceId))
                .subscribeOn(Schedulers.newThread())
                .map(selectedPrefModel -> {

//                    ResponseBody errorBody = selectedPrefMode.response().errorBody();
//                    SelectedPrefModel selectedPrefModel = new SelectedPrefModel();

                    ArrayList<String> cities = new ArrayList<>();
                    ArrayList<String> authors = new ArrayList<>();
                    ArrayList<String> topics = new ArrayList<>();

                    if(selectedPrefModel.getPreferences() != null && selectedPrefModel.getPreferences().getCity() != null) {
                        cities = selectedPrefModel.getPreferences().getCity();
                    }
                    if(selectedPrefModel.getPreferences() != null && selectedPrefModel.getPreferences().getAuthor() != null) {
                        authors = selectedPrefModel.getPreferences().getAuthor();
                    }
                    if(selectedPrefModel.getPreferences() != null && selectedPrefModel.getPreferences().getTopics() != null) {
                        topics = selectedPrefModel.getPreferences().getTopics();
                    }


                    PersonaliseDetails citiesDetails = new PersonaliseDetails();
                    PersonaliseDetails authorsDetails = new PersonaliseDetails();
                    PersonaliseDetails topicsDetails = new PersonaliseDetails();

                    if(cities != null) {
                        for (String city : cities) {
                            PersonaliseModel model = new PersonaliseModel();
                            model.setTitle(city);
                            model.setValue(city);
                            citiesDetails.addPersonalise(model);
                        }
                    }

                    if(authors != null) {
                        for (String author : authors) {
                            PersonaliseModel model = new PersonaliseModel();
                            model.setTitle(author);
                            model.setValue(author);
                            authorsDetails.addPersonalise(model);
                        }
                    }

                    if(topics != null) {
                        for (String topic : topics) {
                            PersonaliseModel model = new PersonaliseModel();
                            model.setTitle(topic);
                            model.setValue(topic);
                            topicsDetails.addPersonalise(model);
                        }
                    }

                    PrefListModel prefListModel = new PrefListModel();
                    prefListModel.addAuthors(authorsDetails);
                    prefListModel.addTopics(topicsDetails);
                    prefListModel.addCities(citiesDetails);

                    return prefListModel;

                });

    }


    public static Observable<KeyValueModel> createSubscription(String userid,
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
        return ServiceFactory.getServiceAPIs().createSubscription(ReqBody.createSubscription(userid, trxnid,
                amt, channel, siteid, planid, plantype, billingchannel, validity, contact, currency, tax, netAmount))
                .subscribeOn(Schedulers.newThread())
                .map(jsonElement -> {
                    KeyValueModel keyValueModel = new KeyValueModel();
                    if (((JsonObject) jsonElement).has("status")) {
                        String status = ((JsonObject) jsonElement).get("status").getAsString();
                        String reason = ((JsonObject) jsonElement).get("msg").getAsString();
                        keyValueModel.setState(status);
                        keyValueModel.setName(reason);
                    }
                    return keyValueModel;
                });


    }


    public static Observable<KeyValueModel> socialLogin(Context context, String deviceId, String originUrl, String provider,
                                                 String socialId, String userEmail, String userName, String siteId) {

        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().socialLogin(ReqBody.socialLogin(deviceId,
                originUrl, provider, socialId, userEmail, userName));
        return observable.subscribeOn(Schedulers.newThread())
                .map(value -> {
                    KeyValueModel keyValueModel = new KeyValueModel();
                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                if (status.equalsIgnoreCase("success")) {
                                    String userId = ((JsonObject) value).get("userId").getAsString();

                                    String isNew = "";
                                    if(((JsonObject) value).has("isNew")) {
                                        isNew = ((JsonObject) value).get("isNew").getAsString();
                                    }

                                    if(context != null) {
                                        THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                                        THPPreferences.getInstance(context).saveLoginId(userEmail);
                                        THPPreferences.getInstance(context).saveLoginPasswd(socialId);
                                        THPPreferences.getInstance(context).setUserLoggedName(userName);
                                        THPPreferences.getInstance(context).saveUserId(userId);
                                    }

                                    if(isNew.equalsIgnoreCase("1")) {
                                        keyValueModel.setNewAccount(true);
                                    }
                                    keyValueModel.setUserId(userId);
                                    String token = ((JsonObject) value).get("token").getAsString();
                                    keyValueModel.setToken(token);

                                    // If account is new then we need to send free plan API
                                    if(isNew.equals("1")) {
                                        ApiManager.freePlan(token, userId, userEmail, siteId);
                                    }

                                    return keyValueModel;
                                } else if (status.equalsIgnoreCase("Fail")) {
                                    String reason = ((JsonObject) value).get("reason").getAsString();
                                    keyValueModel.setName(reason);
                                }

                            }
                            return keyValueModel;
                        }
                );

    }


    public static Observable<KeyValueModel> socialLoginNewImp(Context context, String deviceId, String originUrl, String provider,
                                                 String socialId, String userEmail, String userName, String siteId) {

        Observable<JsonElement> observable = ServiceFactory.getServiceAPIs().socialLogin(ReqBody.socialLogin(deviceId,
                originUrl, provider, socialId, userEmail, userName));
        return observable.subscribeOn(Schedulers.newThread())
                .map(value -> {
                            KeyValueModel keyValueModel = new KeyValueModel();

                            if (((JsonObject) value).has("status")) {
                                String status = ((JsonObject) value).get("status").getAsString();
                                if (status.equalsIgnoreCase("success")) {
                                    String userId = ((JsonObject) value).get("userId").getAsString();
                                    keyValueModel.setUserId(userId);
                                    String isNew = "";
                                    if(((JsonObject) value).has("isNew")) {
                                        isNew = ((JsonObject) value).get("isNew").getAsString();
                                    }

                                    String email = ((JsonObject) value).get("emailId").getAsString();
                                    String contact = ((JsonObject) value).get("contact").getAsString();


                                    keyValueModel.setState(NetConstants.SUCCESS);

                                    String loginId = "";
                                    if(email != null && !TextUtils.isEmpty(email)) {
                                        loginId = email;
                                    }
                                    else if(contact != null && !TextUtils.isEmpty(contact)) {
                                        loginId = contact;
                                    }

                                    if(context != null) {
                                        THPPreferences.getInstance(context).setIsUserLoggedIn(true);
                                        THPPreferences.getInstance(context).saveLoginId(loginId);
                                        THPPreferences.getInstance(context).setIsRefreshRequired(true);
                                        THPPreferences.getInstance(context).setIsUserAdsFree(true);
                                        THPPreferences.getInstance(context).saveUserId(userId);
                                    }
                                    keyValueModel.setContact(loginId);

                                    // If account is new then we need to send free plan API
                                    if(isNew.equals("1")) {
                                        keyValueModel.setNewAccount(true);

                                        /*String email_Contact = "";
                                        if(email_Contact == null || TextUtils.isEmpty(email_Contact)) {
                                            email_Contact = "";
                                        }
                                        ApiManager.freePlan(userId, email_Contact, siteId);*/
                                    }

                                    return keyValueModel;
                                } else if (status.equalsIgnoreCase("Fail")) {
                                    keyValueModel.setState(NetConstants.FAILURE);
                                }

                            }
                            return keyValueModel;
                        }
                );

    }


    public static Observable<PaytmModel> generateChecksumHash(String url, String userId, int planId, String contact, String countryCode) {
        return ServiceFactory.getServiceAPIs().getChecksumHash(url,ReqBody.checksumHashAPIBody(userId,planId,contact, countryCode))
                .subscribeOn(Schedulers.newThread())
                .map(jsonElement -> {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    PaytmModel paytmModel = gson.fromJson(jsonElement.toString(), PaytmModel.class);
                    return paytmModel;
                });
    }

    public static Observable<PaytmTransactionStatus> verifyPaytmTransactionStatus(String url, Bundle bundle) {
        String ORDERID = bundle.getString("ORDERID");
        String MID = bundle.getString("MID");
        String TXNID = bundle.getString("TXNID");
        String TXNAMOUNT = bundle.getString("TXNAMOUNT");
        String PAYMENTMODE = bundle.getString("PAYMENTMODE");
        String CURRENCY = bundle.getString("CURRENCY");
        String TXNDATE = bundle.getString("TXNDATE");
        String STATUS = bundle.getString("STATUS");
        String RESPCODE = bundle.getString("RESPCODE");
        String RESPMSG = bundle.getString("RESPMSG");
        String GATEWAYNAME = bundle.getString("GATEWAYNAME");
        String BANKTXNID = bundle.getString("BANKTXNID");
        String BANKNAME = bundle.getString("BANKNAME");
        String CHECKSUMHASH = bundle.getString("CHECKSUMHASH");
        return ServiceFactory.getServiceAPIs().verifyPaytmTransactionStatusAPI(url,
                ORDERID,MID, TXNID, TXNAMOUNT, PAYMENTMODE, CURRENCY, TXNDATE, STATUS, RESPCODE, RESPMSG, GATEWAYNAME, BANKTXNID, BANKNAME, CHECKSUMHASH)
                .subscribeOn(Schedulers.newThread())
                .map(jsonElement -> {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    PaytmTransactionStatus paytmModel = gson.fromJson(jsonElement.getAsJsonObject().getAsJsonObject("paytm").toString(), PaytmTransactionStatus.class);
                    return paytmModel;
                });
    }


    public static Observable<String> loadSubsWebApi(String webUrl) {
        return ServiceFactory.getServiceAPIs().getSubsWebviewUrl(webUrl)
                .subscribeOn(Schedulers.newThread())
                .map(jsonElement -> {
                    String url = null;
                    if (jsonElement.getAsJsonObject().has("data")) {
                        url = jsonElement.getAsJsonObject().getAsJsonObject("data").get("url").getAsString();
                    }
                    return url;
                });
    }

    public static void detailEventCapture(String systemVersion, String systemName, String section,
                                      String phone, String email, String title, String userid,
                                      String eventTime, String aid, String page, String DeviceId,
                                      String link, String Sited) {
        JsonObject jsonObject = ReqBody.detailEventCapture(systemVersion, systemName, section, phone, email,
                title, userid, eventTime, aid, page, DeviceId, link, Sited);
        ServiceFactory.getServiceAPIs().eventCapture(jsonObject)
                .subscribeOn(Schedulers.newThread())
                .subscribe(v->{
                    Log.i("", "");
                }, t->{
                    Log.i("", "");
                });
    }

    /**
     * This condition is being used, when user is login or signup first time then all ads should be removed.
     * This refresh default TH listing pages only once.
     * ONce this is false, then I hope not required, let check on this
     * @param context
     */
    public static void nowNotRefreshRequired(final Context context) {
        Observable.just("nowNotRefreshRequired")
                .delay(3000, TimeUnit.MILLISECONDS)
                .map(v->{
                    THPPreferences.getInstance(context).setIsRefreshRequired(false);
                    return "";
                })
                .subscribe();
    }

    public static Observable clearDatabaseAsync(Context context) {
        return Observable.just("clear")
                .subscribeOn(Schedulers.newThread())
                .map(v->{
                    THPDB db = THPDB.getInstance(context);
                    clearDB(db, context);
                    return "";
                });

    }

    private static void clearDB(THPDB db, Context context) {
        db.bookmarkTableDao().deleteAll();
        db.breifingDao().deleteAll();
        db.userProfileDao().deleteAll();
        db.dashboardDao().deleteAll();
        THPPreferences.getInstance(context).setIsRefreshRequired(true);
    }

    public static void freePlan(String authorization, String userId, String contact, String siteid) {

        String trxnid = "A";
        Calendar calendar = Calendar.getInstance();

        int thisYear = calendar.get(Calendar.YEAR);
        String thisMonth = String.format("%02d", calendar.get(Calendar.MONTH)+1);
        String thisDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
        String second = String.format("%02d", calendar.get(Calendar.SECOND));

        trxnid +=thisYear+thisMonth+thisDay+"KCaFREE"+hour+minute+second;
        JsonObject jsonObject = ReqBody.freePlan(userId, contact, trxnid, siteid);

        ServiceFactory.getServiceAPIs().freePlan(authorization, jsonObject)
                .subscribeOn(Schedulers.newThread())
                .repeatWhen(value->{
                    /**
                     * This is called only once.
                     * 1 means each repeated call will be delayed by 5 seconds
                     */
                    return value.delay(1, TimeUnit.SECONDS);
                })
                .takeUntil(value->{
                    /** Here we can check if the responce is correct and if we should
                     *  finish polling
                     *  We finish polling when job is done.
                     *  In other words : "We stop taking when job is done"
                     */
                     if (((JsonObject) value).has("status")) {
                        String status = ((JsonObject) value).get("status").getAsString();
                         return status.equalsIgnoreCase(NetConstants.SUCCESS);
                    }
                    return false;
                })
                .subscribe();
    }


    public static Observable<Boolean> freePlanF(String authorization, String userId, String contact, String siteid) {

        String trxnid = "A";
        Calendar calendar = Calendar.getInstance();

        int thisYear = calendar.get(Calendar.YEAR);
        String thisMonth = String.format("%02d", calendar.get(Calendar.MONTH)+1);
        String thisDay = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
        String second = String.format("%02d", calendar.get(Calendar.SECOND));

        trxnid +=thisYear+thisMonth+thisDay+"KCaFREE"+hour+minute+second;
        JsonObject jsonObject = ReqBody.freePlan(userId, contact, trxnid, siteid);

        return ServiceFactory.getServiceAPIs().freePlan(authorization, jsonObject)
                .subscribeOn(Schedulers.newThread())
                .map(value->{
                    if (((JsonObject) value).has("status")) {
                        String status = ((JsonObject) value).get("status").getAsString();
                        return status.equalsIgnoreCase(NetConstants.SUCCESS);
                    }
                    return false;
                });
    }

    public static void mpConfigurationAPI(Context context) {
        Observable<MPConfigurationModel> observable = ServiceFactory.getServiceAPIs().mpConfigurationAPI("http://46.137.215.226/admin/meterPaywall/configuration");

        observable.subscribeOn(Schedulers.newThread())
                .map(configurationModel -> {
                    THPDB db = THPDB.getInstance(context);
                    MPTableDao mpTableDao = db.mpTableDao();
                    MPTable mpTable = mpTableDao.getMPTable();

                    long MPServerTimeInMillis = configurationModel.getDATA().getGmtInMillis();

                    boolean isTaboolaNeeded = configurationModel.getDATA().getConfigs().isIsTaboolaNeeded();
                    boolean isMpBannerNeeded = configurationModel.getDATA().getConfigs().isIsMpBannerNeeded();

                    String mpBannerMsg = configurationModel.getDATA().getConfigs().getMpBannerMsg();

                    String fullAccessBtnName = configurationModel.getDATA().getConfigs().getFullAccessBtnName();
                    boolean showFullAccessBtn = configurationModel.getDATA().getConfigs().isShowFullAccessBtn();

                    boolean showSignInBtn = configurationModel.getDATA().getConfigs().isShowSignInBtn();
                    String signInBtnName = configurationModel.getDATA().getConfigs().getSignInBtnName();
                    String signInBtnNameBoldWord = configurationModel.getDATA().getConfigs().getSignInBtnNameBoldWord();

                    boolean showSignUpBtn = configurationModel.getDATA().getConfigs().isShowSignUpBtn();
                    String signUpBtnName = configurationModel.getDATA().getConfigs().getSignUpBtnName();
                    String signUpBtnNameBoldWord = configurationModel.getDATA().getConfigs().getSignUpBtnNameBoldWord();

                    String nonSignInBlockerTitle = configurationModel.getDATA().getConfigs().getNonSignInBlockerTitle();
                    String nonSignInBlockerDescription = configurationModel.getDATA().getConfigs().getNonSignInBlockerDescription();

                    String expiredUserBlockerTitle = configurationModel.getDATA().getConfigs().getExpiredUserBlockerTitle();
                    String expiredUserBlockerDescription = configurationModel.getDATA().getConfigs().getExpiredUserBlockerDescription();

                    long allowedTimeInMillis = mpTable.getAllowedTimeInSecs()*1000;

                    // It will trigger cycle api when "MPServerTimeInMillis" is greater than "MPServerTimeInMillis+allowedTimeInMillis"
                    mpTable.setExpiryTimeInMillis(MPServerTimeInMillis+allowedTimeInMillis);

                    mpTable.setTaboolaNeeded(isTaboolaNeeded);
                    mpTable.setMpBannerNeeded(isMpBannerNeeded);

                    mpTable.setMpBannerMsg(mpBannerMsg);

                    mpTable.setFullAccessBtnName(fullAccessBtnName);
                    mpTable.setShowFullAccessBtn(showFullAccessBtn);

                    mpTable.setShowSignInBtn(showSignInBtn);
                    mpTable.setSignInBtnName(signInBtnName);
                    mpTable.setSignInBtnNameBoldWord(signInBtnNameBoldWord);

                    mpTable.setShowSignUpBtn(showSignUpBtn);
                    mpTable.setSignUpBtnName(signUpBtnName);
                    mpTable.setSignUpBtnNameBoldWord(signUpBtnNameBoldWord);

                    mpTable.setNonSignInBlockerTitle(nonSignInBlockerTitle);
                    mpTable.setNonSignInBlockerDescription(nonSignInBlockerDescription);
                    mpTable.setExpiredUserBlockerTitle(expiredUserBlockerTitle);
                    mpTable.setExpiredUserBlockerDescription(expiredUserBlockerDescription);

                    mpTableDao.updateMPTable(mpTable);

                  return "";
                }).subscribe(val->{

        }, throwable -> {
                    Log.i("", "");
        });
    }

    public static void mpCycleDurationAPI(Context context) {
        Observable<MPCycleDurationModel> observable = ServiceFactory.getServiceAPIs().mpCycleDurationAPI("http://46.137.215.226/admin/meterPaywall/cycle");
        observable.subscribeOn(Schedulers.newThread())
                .map(cycleDurationModel -> {
                    THPDB db = THPDB.getInstance(context);
                    MPTableDao mpTableDao = db.mpTableDao();

                    String cycleName = cycleDurationModel.getDATA().getCycleName();
                    int numOfAllowedArticles = cycleDurationModel.getDATA().getNumOfAllowedArticles();
                    long totalAllowedTimeInSec = cycleDurationModel.getDATA().getExpiryInSeconds();
                    String uniqueId = cycleDurationModel.getDATA().getUniqueId();

                    MPTable table = mpTableDao.getMPTable();

                    table.setAllowedArticleCounts(numOfAllowedArticles);
                    table.setCycleName(cycleName);
                    table.setAllowedTimeInSecs(totalAllowedTimeInSec);
                    table.setCycleUniqueId(uniqueId);

                    table.addReadArticleId("100");

                    mpTableDao.insertMpTableData(table);

                    // It calls configuration api, whenever cycle api is called.
                    mpConfigurationAPI(context);

                    return "";
                }).subscribe(val->{

        }, throwable -> {
            Log.i("", "");
        });
    }

    public static Flowable<HashMap> readArticleCount(Context context) {
        THPDB thpdb = THPDB.getInstance(context);
        MPTableDao mpTableDao = thpdb.mpTableDao();
        return mpTableDao.getArticleIdsFlowable()
                .subscribeOn(Schedulers.newThread())
                .map(value->{
                    Log.i("", "");

                    String mpBannerMsg = mpTableDao.getMpBannerMsg();
                    int allowedArticleCounts = mpTableDao.getAllowedArticleCounts();
                    long allowedTimeInSecs = mpTableDao.getAllowedArticleTimesInSecs();
                    int totalReadSize = value.size();

                    boolean isAllowedToRead = allowedArticleCounts >= totalReadSize;

                    String bannerMsg = mpBannerMsg.replaceAll("<readCount>", ""+totalReadSize);
                    bannerMsg = bannerMsg.replaceAll("<totalCount>", ""+allowedArticleCounts);

                    HashMap<String, Object> valueMap = new HashMap<>();
                    valueMap.put("bannerMsg", bannerMsg);
                    valueMap.put("isAllowedToRead", ""+isAllowedToRead);

                    return valueMap;
                });

    }

    public static Observable insertReadArticleId(Context context, String readArticleId) {
        return Observable.just("readCount")
                .subscribeOn(Schedulers.newThread())
                .map(value->{
                    THPDB thpdb = THPDB.getInstance(context);
                    MPTableDao mpTableDao = thpdb.mpTableDao();
                    MPTable mpTable = mpTableDao.getMPTable();
                    mpTable.addReadArticleId(readArticleId);
                    mpTableDao.updateMPTable(mpTable);
                    int allowedArticleCount = mpTable.getAllowedArticleCounts();

                    if(allowedArticleCount>=mpTable.getReadArticleIds().size()) {
                        return true;
                    }
                    return false;
                });

    }
}
