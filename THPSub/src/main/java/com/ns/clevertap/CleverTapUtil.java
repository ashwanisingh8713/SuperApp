package com.ns.clevertap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import com.clevertap.android.sdk.CleverTapAPI;
import com.netoperation.model.TxnDataBean;
import com.netoperation.model.UserProfile;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class CleverTapUtil {

    private static void pushCleverTapEvent(Context context, String event, HashMap<String, Object>  properties) {
    /*if(1==1) {
        return;
    }*/
        CleverTapAPI.getDefaultInstance(context).pushEvent(event, properties);
    }

    /**
     * Update CleverTap UserProfile
     */
    public static void cleverTapUpdateProfile(Context context, boolean isNewLoggin, UserProfile userProfilee, boolean isHasSubscriptionPlan, boolean isHasFreePlan) {
        if(userProfilee == null) {
            return;
        }
        Observable.just(userProfilee)
                .subscribeOn(Schedulers.newThread())
                .map(userProfile -> {
                    CleverTapAPI clevertap = CleverTapAPI.getDefaultInstance(context);
                    HashMap<String, Object> profileUpdate = new HashMap<String, Object>();

                    profileUpdate.put(THPConstants.CT_KEY_platform,"app");

                    String email = userProfile.getEmailId();
                    String contact = userProfile.getContact();

                    if (!ResUtil.isEmpty(email)) {
                        //email = userProfile.getUserEmailOrContact();
                        profileUpdate.put(THPConstants.CT_Custom_KEY_Email, email);
                        profileUpdate.put(THPConstants.CT_KEY_Email, email);
                    }

                    if (!ResUtil.isEmpty(contact)) {
                        //contact = userProfile.getUserEmailOrContact();
                        profileUpdate.put(THPConstants.CT_KEY_Phone, THPConstants.MOBILE_COUNTRY_CODE + contact);
                        profileUpdate.put(THPConstants.CT_KEY_Mobile_Number, contact);
                    }


                    //Update pre-defined profile properties
                    profileUpdate.put(THPConstants.CT_KEY_Identity, userProfile.getUserId());
                    profileUpdate.put(THPConstants.CT_KEY_UserId, userProfile.getUserId());
                    profileUpdate.put(THPConstants.CT_KEY_DOB, userProfile.getDOB());
                    profileUpdate.put(THPConstants.CT_KEY_Name, userProfile.getFullNameForProfile());


                    profileUpdate.put(THPConstants.CT_KEY_Gender, userProfile.getGender());
                    profileUpdate.put(THPConstants.CT_KEY_Login_Source, PremiumPref.getInstance(context).getLoginSource());
                    profileUpdate.put(THPConstants.CT_KEY_isSubscribedUser, isHasSubscriptionPlan);
                    profileUpdate.put(THPConstants.CT_KEY_isFreeUser, isHasFreePlan);

                    String permission = Manifest.permission.ACCESS_COARSE_LOCATION;
                    int res = context.checkCallingOrSelfPermission(permission);
                    if (res == PackageManager.PERMISSION_GRANTED) {
                        Location location = clevertap.getLocation();
                        // do something with location,
                        // optionally set on CleverTap for use in segmentation etc
                        clevertap.setLocation(location);
                    }

                    //Update custom profile properties
                   if (userProfile.getUserPlanList() != null && userProfile.getUserPlanList().size() > 0) {
                       ArrayList<String> myPlanNames = new ArrayList<>();
                       for (TxnDataBean plan : userProfile.getUserPlanList()) {
                           myPlanNames.add(plan.getPlanName());
                       }
                       profileUpdate.put(THPConstants.CT_KEY_Plan_Type, myPlanNames);
                    } else {
                        profileUpdate.put(THPConstants.CT_KEY_Plan_Type, "Plan Expired");
                    }

                    if (isNewLoggin) {
                        clevertap.onUserLogin(profileUpdate);
                    } else {
                        clevertap.pushProfile(profileUpdate);
                    }
                    return "";
                })
                .subscribe(v -> {
                }, t -> {
                    Log.i("", "");
                });

    }

    public static void cleverTapDetailPageEvent(Context context, String IS_Article_PREMIUM, int articleId, String articleTitle,
                                                String articleLink, String sectionName, String articleType) {
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put(THPConstants.CT_KEY_platform,"app");
        prodViewedAction.put(THPConstants.CT_KEY_UserId, getUserId(context));
        prodViewedAction.put(THPConstants.CT_KEY_IS_Article_PREMIUM, IS_Article_PREMIUM);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Id, articleId);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Title, articleTitle);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Link, articleLink);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Section, sectionName);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Type, articleType);
        //This Event is Replaced with @PageVisit event
        //CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_ARTICLE_READ, prodViewedAction);
    }

    public static void cleverTapDetailPageEvent(Context context, boolean isBriefing, String IS_Article_PREMIUM, String from, String articleId, String articleTitle,
                                                String articleLink, String sectionName, String articleType, String Article_USER_TIME_SPENT) {
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put(THPConstants.CT_KEY_platform,"app");
        prodViewedAction.put(THPConstants.CT_KEY_UserId, getUserId(context));
        prodViewedAction.put(THPConstants.CT_KEY_IS_Article_PREMIUM, IS_Article_PREMIUM);
        if (isBriefing) {
            prodViewedAction.put(THPConstants.CT_KEY_Article_IS_FROM, "Briefing - " + from);
        } else {
            prodViewedAction.put(THPConstants.CT_KEY_Article_IS_FROM, from);
        }
        prodViewedAction.put(THPConstants.CT_KEY_Article_Id, articleId);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Title, articleTitle);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Link, articleLink);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Section, sectionName);
        prodViewedAction.put(THPConstants.CT_KEY_Article_Type, articleType);
        prodViewedAction.put(THPConstants.CT_KEY_Article_USER_TIME_SPENT, Article_USER_TIME_SPENT);
        //This Event is Replaced with @PageVisit event
        //CleverTapAPI.getDefaultInstance(context).pushEvent(THPConstants.CT_EVENT_ARTICLE_READ, prodViewedAction);
    }

    public static void cleverTapTHPTabTimeSpent(Context context, String from, String Article_USER_TIME_SPENT,long timeInSeconds) {
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put(THPConstants.CT_KEY_AppSections, from);
        prodViewedAction.put(THPConstants.CT_KEY_platform,"app");
        prodViewedAction.put(THPConstants.CT_KEY_UserId, getUserId(context));
        prodViewedAction.put(THPConstants.CT_KEY_TimeSpent, Article_USER_TIME_SPENT);
        prodViewedAction.put(THPConstants.CT_KEY_TimeSpentSeconds, timeInSeconds);

        pushCleverTapEvent(context, THPConstants.CT_TIME_SPENT, prodViewedAction);
    }

    public static void cleverTapLogoutEvent(Context context, UserProfile userProfile) {
        if(userProfile == null) {
            return;
        }
        HashMap<String, Object> logoutActions = new HashMap<String, Object>();

        String email = userProfile.getEmailId();
        String contact = userProfile.getContact();

        if (ResUtil.isEmpty(email)) {
            email = userProfile.getUserEmailOrContact();
        }

        if (ResUtil.isEmpty(contact)) {
            contact = userProfile.getUserEmailOrContact();
        }

        logoutActions.put(THPConstants.CT_KEY_Name, userProfile.getFullName());
        logoutActions.put(THPConstants.CT_KEY_platform,"app");
        logoutActions.put(THPConstants.CT_Custom_KEY_Email, email);
        logoutActions.put(THPConstants.CT_KEY_Email, email);
        logoutActions.put(THPConstants.CT_KEY_Phone, contact);
        logoutActions.put(THPConstants.CT_KEY_UserId, userProfile.getUserId());
        logoutActions.put(THPConstants.CT_KEY_Login_Source, userProfile.getLoginSource());
        pushCleverTapEvent(context, THPConstants.CT_EVENT_SIGN_OUT, logoutActions);

    }

    public static void cleverTapEvent(Context context,String eventName,HashMap<String,Object> map){
        if(map==null){
            map = new HashMap<>();
        }
        map.put(THPConstants.CT_KEY_platform,"app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));

        pushCleverTapEvent(context, eventName, map);
    }


    public static void cleverTapEventPayNow(Context context,int packValue,String packDuration, String packName){

        HashMap<String,Object>   map = new HashMap<>();

        map.put(THPConstants.CT_KEY_platform,"app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        map.put(THPConstants.CT_KEY_Pack_value,packValue);
        map.put(THPConstants.CT_KEY_Pack_duration,packDuration);
        map.put(THPConstants.CT_KEY_Pack_Name,packName);
        pushCleverTapEvent(context, THPConstants.CT_EVENT_PAY_NOW, map);
    }
    public static void cleverTapEventProductViewed(Context context,String packValue,String packDuration, String packName){

        HashMap<String,Object>   map = new HashMap<>();

        map.put(THPConstants.CT_KEY_platform,"app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        map.put(THPConstants.CT_KEY_Pack_Name,packName);
        map.put(THPConstants.CT_KEY_Pack_value,packValue);
        map.put(THPConstants.CT_KEY_Pack_duration,packDuration);
        pushCleverTapEvent(context, THPConstants.CT_EVENT_PRODUCT_VIEWED, map);
    }

    public static void cleverTapBookmarkFavLike(Context context, String articleId, String from, String takenAction) {

        if(context == null) {
            return;
        }

        final HashMap<String, Object> map = new HashMap<>();

        map.put("ArticleId", articleId);
        if(from.equalsIgnoreCase(NetConstants.API_Mystories)) {
            from = "My Stories";
        }
        map.put(THPConstants.CT_KEY_AppSections, from);
        map.put(THPConstants.CT_KEY_platform,"app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));

        String eventName = "";
        switch (takenAction) {
            case "NetConstants.BOOKMARK_YES":
                eventName = THPConstants.CT_EVENT_READ_LATER;
                break;
//            case "NetConstants.BOOKMARK_NO":
//                eventName = "Article deleted from bookmark";
//                break;
            case "NetConstants.LIKE_YES":
                eventName = THPConstants.CT_EVENT_FAVOURTING;
                break;
            case "NetConstants.LIKE_NO":
                eventName = "Article Show fewer stories";
                break;
            case "UNDO":
                eventName = "Undo";
                break;
                default:
                    eventName = takenAction;

        }

        pushCleverTapEvent(context, eventName, map);
    }


    public static void CleverTapWidget(Context context, CTWidgetTracking widgetTracking) {
        Observable.just(widgetTracking)
                .subscribeOn(Schedulers.newThread())
                .map(v->{
                    final List<List<String>> widgetArticleIdsList = widgetTracking.widgetArticleIdsList;
                    final List<String> widgetNames = widgetTracking.widgetName;
                    int count = 0;
                    for(String widgetType : widgetNames) {
                        final HashMap<String, Object> map = new HashMap<>();
                        map.put("Section Widget", widgetType);
                        map.put(THPConstants.CT_KEY_platform,"app");
                        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
                        final List<String> articleIdList = widgetArticleIdsList.get(count);
                        String articleIds = "";
                        for(String str : articleIdList) {
                            articleIds +=str+", ";
                        }
                        map.put("ArticleIds", articleIds);
                        map.put("ArticleCount", widgetArticleIdsList.get(count).size());

                        pushCleverTapEvent(context, THPConstants.CT_EVENT_WIDGET, map);
                        count++;
                    }
                return "";
                })
                .subscribe(v->{

                }, t->{

                });



    }

    public static void cleverTapEventSettings(Context context, String propertyName, String value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(propertyName, value);
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        pushCleverTapEvent(context, THPConstants.CT_EVENT_SETTINGS, map);
    }

    public static void cleverTapEventPaymentStatus(Context context,String status,int packValue,String packDuration,String packName,long startTime,long endTime) {



        String timeDuration = AppDateUtil.millisToMinAndSec((endTime >= 1000 ? endTime : 1000) - startTime);

        if(timeDuration.contains("-"))
            timeDuration = "0 minute 1 Sec";

        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_Pack_value,packValue );
        map.put(THPConstants.CT_KEY_Pack_duration,packDuration);
        map.put(THPConstants.CT_KEY_Pack_Name,packName );
        map.put(THPConstants.CT_KEY_Conversion_Time,timeDuration);
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));

        if(status.equals("success")){
            pushCleverTapEvent(context, THPConstants.CT_EVENT_PAYMENT_SUCCESSFUL, map);
        }else{
            pushCleverTapEvent(context, THPConstants.CT_EVENT_PAYMENT_FAILED, map);
        }

    }

    public static void cleverTapEventBenefitsPage(Context context, String propertyName, String value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(propertyName, value);
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        pushCleverTapEvent(context, THPConstants.CT_EVENT_BENEFITS_PAGE, map);
    }

    public static void cleverTapEventHamberger(Context context, String section, String subsection) {
        HashMap<String, Object> map = new HashMap<>();
        if (subsection != null) {
            map.put(THPConstants.CT_KEY_SubSection, section + " --> " + subsection);
        } else {
            map.put(THPConstants.CT_KEY_Section, section);
        }
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        pushCleverTapEvent(context, THPConstants.CT_EVENT_HAMBERGER, map);
    }

    public static void cleverTapEventFreeTrial(Context context) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_Date_Subscription, AppDateUtil.getCurrentDateFormatted("dd/MM/yyyy"));
        map.put(THPConstants.CT_KEY_User_Type, "Free_trial");
        map.put(THPConstants.CT_KEY_Pack_duration, "1 Month");
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        pushCleverTapEvent(context, THPConstants.CT_EVENT_FREE_TRIAL, map);
    }

    /*
     * isArticle is 1 if page type is an Article Detail Page otherwise 0*/
    public static void cleverTapEventPageVisit(Context context, String pageType, String articleId, String section, String authors, int isArticle) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_PAGE_TYPE, pageType);
        map.put(THPConstants.CT_KEY_IS_ARTICLE, isArticle);
        if (section != null) {
            map.put(THPConstants.CT_KEY_SECTIONS, section);
        }
        if (isArticle == 1) {
            map.put(THPConstants.CT_KEY_ID, articleId);
            map.put(THPConstants.CT_KEY_AUTHOR, authors);
        }
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserId(context));
        pushCleverTapEvent(context, THPConstants.CT_EVENT_PAGE_VISITED, map);
    }

    //MP Events Article Count
    public static void cleverTapMP_ArticleCount(Context context, String mpCycle, int articleCount, int allowedCounts) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserIdOrDeviceId(context));
        map.put(THPConstants.MP_Cycle, mpCycle);
        map.put(THPConstants.ArticleCount,articleCount);
        map.put(THPConstants.Allowed_Counts,allowedCounts);
        pushCleverTapEvent(context, THPConstants.MP_Article_Count, map);
    }

    //MP Events Metered Paywall
    public static void cleverTapMetered_Paywall(Context context, String mpCycle, int articleCount, int allowedCounts) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserIdOrDeviceId(context));
        map.put(THPConstants.MP_Cycle, mpCycle);
        map.put(THPConstants.ArticleCount,articleCount);
        map.put(THPConstants.Allowed_Counts,allowedCounts);
        pushCleverTapEvent(context, THPConstants.Metered_Paywall, map);
    }

    //MP Banner Subscribe
    public static void cleverTapMPBannerSubscribe(Context context, String mpCycle) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserIdOrDeviceId(context));
        map.put(THPConstants.MP_Cycle, mpCycle);
        pushCleverTapEvent(context, THPConstants.MP_Banner_Subscribe, map);
    }

    //MP Events Conversion From MP
    public static void cleverTapConversionFromMP(Context context, String mpCycle) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserIdOrDeviceId(context));
        map.put(THPConstants.MP_Cycle, mpCycle);
        pushCleverTapEvent(context, THPConstants.Conversion_From_MP, map);
    }

    //Get Full Access Button Click
    public static void cleverTapGetFullAccessButtonClick(Context context, String mpCycle) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserIdOrDeviceId(context));
        map.put(THPConstants.MP_Cycle, mpCycle);
        pushCleverTapEvent(context, THPConstants.Get_Full_Access_Button_Click, map);
    }

    //MP SignIn
    public static void cleverTapMP_SignIn(Context context, String mpCycle) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserIdOrDeviceId(context));
        map.put(THPConstants.MP_Cycle, mpCycle);
        pushCleverTapEvent(context, THPConstants.MP_SignIn, map);
    }

    //MP SignIn
    public static void cleverTapMP_SignUp(Context context, String mpCycle) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_UserId, getUserIdOrDeviceId(context));
        map.put(THPConstants.MP_Cycle, mpCycle);
        pushCleverTapEvent(context, THPConstants.MP_SignUp, map);
    }

    //Splash From
    public static void cleverTap_Splash_API(Context context, String from) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(THPConstants.CT_KEY_platform, "app");
        map.put(THPConstants.CT_KEY_Device_id, getUserIdOrDeviceId(context));
        map.put(THPConstants.CT_KEY_resolution, ResUtil.resolution(context));
        map.put(THPConstants.CT_KEY_from, from);
        pushCleverTapEvent(context, THPConstants.CT_EVENT_SPLASH_API, map);
    }





    private static String sUserId ="";
    private static String getUserId(Context context){
        if(ResUtil.isEmpty(sUserId)) {
            sUserId = PremiumPref.getInstance(context).getUserId();
        }
        return sUserId;
    }

    public static void clearUserId() {
        sUserId = "";
    }

    private static String getUserIdOrDeviceId(Context context){
        if(ResUtil.isEmpty(getUserId(context))) {
            return ResUtil.getDeviceId(context);
        } else {
            return getUserId(context);
        }
    }
}
