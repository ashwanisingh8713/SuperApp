package com.ns.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.netoperation.util.AppDateUtil;

/**
 * Created by Abhishek 01-Oct-2019.
 */

public class THPFirebaseAnalytics {
    public static final String TAG = "THPFirebaseAnalytics";


    public static String getSectionName(String sectionName) {
        return "Section: " + sectionName;
    }

    public static String getArticleName(String article) {
        return "Article: " + article;
    }


    //OnResume
    public static void setFirbaseAnalyticsScreenRecord(Activity activity, String screenName, String className) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
        mFirebaseAnalytics.setCurrentScreen(activity, screenName, className);

        setFirebaseAnalyticsScreensEvent(activity, screenName);
    }

    public static void setFirebaseAnalyticsScreensEvent(Context context, String screenName) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("screenName", screenName);
        mFirebaseAnalytics.logEvent("Screens", bundle);

    }


    public static void setFirbaseAnalyticsEvent(Context context, String category, String action, String actionLabel) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        bundle.putString("action", action);
        bundle.putString("screen", actionLabel);
        mFirebaseAnalytics.logEvent("click_to_actions", bundle);
    }


    public static void setFirbasePaymentSuccessFailedEvent(Context context,String category,String status,int pack_value, String pack_duration,String pack_name,long startTime,long endTime) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        String timeDuration = AppDateUtil.millisToMinAndSec((endTime >= 1000 ? endTime : 1000) - startTime);

        if(timeDuration.contains("-"))
            timeDuration = "0 minute 1 Sec";

        Bundle bundle = new Bundle();
        bundle.putString("Pack_Value", String.valueOf(pack_value));
        bundle.putString("Pack_Duration", pack_duration);
        bundle.putString("Pack_Name", pack_name);
        bundle.putString("Conversion_Time", timeDuration);

        if(status.equalsIgnoreCase("success"))
            mFirebaseAnalytics.logEvent("Payment_Successful", bundle);
        else
            mFirebaseAnalytics.logEvent("Payment_Failed", bundle);

    }


    public static void setFirbaseFreeTrialEvent(Context context) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("Date_Registration", AppDateUtil.getCurrentDateFormatted("dd/MM/yyyy"));
        bundle.putString("User_Type", "Free_trial");
        bundle.putString("Pack_Duration", "1 Month");
        mFirebaseAnalytics.logEvent("Free_Trial", bundle);
    }

    public static void cleverTapEventPayNowFirebase(Context context,int packValue,String packDuration, String packName) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("Pack_value",""+packValue );
        bundle.putString("Pack_duration", packDuration);
        bundle.putString("Pack_Name",packName );
        mFirebaseAnalytics.logEvent("Pay_Now", bundle);
    }

    //MP Events Conversion From MP
    public static void firebaseConversionFromMP(Context context, String mpCycle) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(THPConstants.MP_Cycle_Firebase, mpCycle);
        mFirebaseAnalytics.logEvent(THPConstants.Conversion_From_MP_Firebase, bundle);
    }

    //MP Events Article Count
    public static void firebaseMP_ArticleCount(Context context, String mpCycle, int articleCount, int allowedArticleCounts) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(THPConstants.MP_Cycle_Firebase, mpCycle);
        bundle.putInt(THPConstants.ArticleCount_Firebase, articleCount);
        bundle.putInt(THPConstants.Allowed_Counts_Firebase, allowedArticleCounts);
        mFirebaseAnalytics.logEvent(THPConstants.MP_Article_Count_Firebase, bundle);
    }

    //MP Events Metered Paywall
    public static void firebaseMetered_Paywall(Context context, String mpCycle, int articleCount, int allowedArticleCounts) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString(THPConstants.MP_Cycle_Firebase, mpCycle);
        bundle.putString(THPConstants.ArticleCount_Firebase,""+articleCount);
        bundle.putString(THPConstants.Allowed_Counts_Firebase,""+allowedArticleCounts);
        mFirebaseAnalytics.logEvent(THPConstants.Metered_Paywall_Firebase, bundle);
    }

    //MP Banner Subscribe
    public static void firebaseMPBannerSubscribe(Context context, String mpCycle) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        if (!ResUtil.isEmpty(mpCycle))
            bundle.putString(THPConstants.MP_Cycle_Firebase, mpCycle);
        mFirebaseAnalytics.logEvent(THPConstants.MP_Banner_Subscribe_Firebase, bundle);
    }



    //Get Full Access Button Click
    public static void firebaseGetFullAccessButtonClick(Context context, String mpCycle) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        if (!ResUtil.isEmpty(mpCycle))
            bundle.putString(THPConstants.MP_Cycle_Firebase, mpCycle);
        mFirebaseAnalytics.logEvent(THPConstants.Get_Full_Access_Button_Click_Firebase, bundle);
    }

    //MP SignIn
    public static void firebaseMP_SignIn(Context context, String mpCycle) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        if (!ResUtil.isEmpty(mpCycle))
            bundle.putString(THPConstants.MP_Cycle_Firebase, mpCycle);
        mFirebaseAnalytics.logEvent(THPConstants.MP_SignIn_Firebase, bundle);
    }

    //MP SignUp
    public static void firebaseMP_SignUp(Context context, String mpCycle) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        if (!ResUtil.isEmpty(mpCycle))
            bundle.putString(THPConstants.MP_Cycle_Firebase, mpCycle);
        mFirebaseAnalytics.logEvent(THPConstants.MP_SignUp_Firebase, bundle);
    }
}
