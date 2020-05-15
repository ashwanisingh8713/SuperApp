package com.ns.utils;

public class THPConstants {

    public static final String TnC_URL = "https://www.thehindu.com/app-termsofuse/";
    public static final String FAQ_URL = "https://subscription.thehindu.com/appfaq";

    public static final String FROM_SubscriptionStep_1_Fragment = "SubscriptionStep_1_Fragment";
    public static final String FROM_BecomeMemberActivity = "BecomeMemberActivity";
    public static final String FROM_USER_SignUp = "userSignUp";
    public static final String FROM_START_30_DAYS_TRAIL = "start30DaysTrail";
    public static final String FROM_USER_ACCOUNT_CREATED = "userAccountCreated";
    public static final String FROM_SignUpAndPayment = "SignUp&Payment";
    public static final String FROM_FORGOT_PASSWORD = "FORGOT_PASSWORD";
    public static final String FROM_AccountInfoFragment = "AccountInfoFragment";
    public static final String FROM_PersonalInfoFragment = "PersonalInfoFragment";
    public static final String FROM_SUSPEND_ACCOUNT = "suspendAccount";
    public static final String FROM_DELETE_ACCOUNT = "deleteAccount";
    public static final String FROM_PROFILE_VIEWALL = "profileViewAll";
    public static final String FROM_SUBSCRIPTION_EXPLORE = "subscriptionExplore";
    public static final String FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE = "subscriptionExploreNotification";
    public static final String FROM_USER_PROFILE = "userProfile";
    public static final String FROM_BookmarkListing = "BookmarkListing";
    public static final String FROM_PERSONALISE = "Personalise";
    public static final String FROM_DASHBOARD_SET_PREFERENCE = "DashboardSetPreference";
    public static final String PAYMENT = "Payment";
    public static final String FROM_NOTIFICATION_CLICK = "notificationClick";


    public static final String date_dd_MM_yyyy = "dd-MM-yyyy";

    public static final int DESCRIPTION_SMALL = 1;
    public static final int DESCRIPTION_NORMAL = 2;
    public static final int DESCRIPTION_LARGE = 3;
    public static final int DESCRIPTION_LARGEST = 4;

    public static final int THEME_WHITE = 1;
    public static final int THEME_GREY = 2;

    public static final String ARTICLE_TYPE_ARTICLE = "article";
    public static final String ARTICLE_TYPE_PHOTO = "photo";
    public static final String ARTICLE_TYPE_AUDIO = "audio";
    public static final String ARTICLE_TYPE_VIDEO = "video";
    public static final String ARTICLE_TYPE_YOUTUBE_VIDEO = "embedYoutube";


    // For location
    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;
    /*Set login source - Direct/Facebook/Twitter/Google*/
    public static final String DIRECT_LOGIN = "AppDirect";
    public static final String FACEBOOK_LOGIN = "Facebook";
    public static final String TWITTER_LOGIN = "Twitter";
    public static final String GOOGLE_LOGIN = "Google";

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationaddress";

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_MAX_ADDRESS_KEY = PACKAGE_NAME + ".RESULT_MAX_ADDRESS_KEY";
    public static final String RESULT_MESSAGE_KEY = PACKAGE_NAME + ".RESULT_MESSAGE_KEY";
    public static final String RESULT_PIN_KEY = PACKAGE_NAME + ".RESULT_PIN_KEY";
    public static final String RESULT_ADMIN_AREA_KEY = PACKAGE_NAME + ".RESULT_ADMIN_AREA_KEY";
    public static final String RESULT_CITY_KEY = PACKAGE_NAME + ".RESULT_CITY_KEY";
    public static final String RESULT_SUB_CITY_KEY = PACKAGE_NAME + ".RESULT_SUB_CITY_KEY";
    public static final String RESULT_COUNTRY_CODE_KEY = PACKAGE_NAME + ".RESULT_COUNTRY_CODE_KEY";
    public static final String RESULT_COUNTRY_NAME_KEY = PACKAGE_NAME + ".RESULT_COUNTRY_NAME_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static String FLOW_TAB_CLICK = null;
    public static String TAB_1 = "Briefing";
    public static String TAB_2 = "My Stories";
    public static String TAB_3 = "Suggested";
    public static String TAB_4 = "Profile";

    //TAP value Constants for CleverTap Event @CT_EVENT_BENEFITS_PAGE
    public static String TAP_CROWN = "Crown";
    public static String TAP_BANNER_SUBSCRIPTION = "Banner";
    public static String TAP_BRIEFING = "Briefing";
    public static String TAP_STORIES = "My Stories";
    public static String TAP_SUGGESTED = "Suggested";
    //CT Keys/Properties
    public static final String CT_KEY_Clicked_Section = "Clicked section";

    public static String BROADCAST_ACTION_KILL_BECOME_MEMBER_PAGE = "action.kill.becomeMember.page";

    public static final String MOBILE_COUNTRY_CODE = "+91";
    public static final String CT_KEY_SubSection = "Sub Section";
    public static final String CT_EVENT_SIGNOUT = "Sign out";
    public static final String CT_TIME_SPENT = "Time Spent";
    public static final String CT_KEY_IS_Article_PREMIUM = "0";
    public static final String CT_KEY_Article_IS_FROM = "1";
    public static final String CT_KEY_Article_Id = "2";
    public static final String CT_KEY_Article_Title = "3";
    public static final String CT_KEY_Article_Link = "4";
    public static final String CT_KEY_Article_Section = "5";
    public static final String CT_KEY_Article_Type = "6";
    public static final String CT_KEY_Article_USER_TIME_SPENT = "7";

    public static final String CT_KEY_Identity = "Identity";
    public static final String CT_KEY_UserId = "User Id";
    public static final String CT_KEY_DOB = "DOB";
    public static final String CT_KEY_Name = "Name";
    public static final String CT_Custom_KEY_Email = "Email Id";
    public static final String CT_KEY_Email = "Email";
    public static final String CT_KEY_Mobile_Number = "Mobile Number";
    public static final String CT_KEY_Phone = "Phone";
    public static final String CT_KEY_Gender = "Gender";
    public static final String CT_KEY_Login_Source = "Loginsource";
    public static final String CT_KEY_isSubscribedUser = "isSubscribedUser";
    public static final String CT_KEY_isFreeUser = "isFreeUser";
    public static final String CT_KEY_Plan_Type = "Plan Type";
    public static final String CT_KEY_Subscription_End_Date = "Subscription_End_Date";
    public static final String CT_KEY_Subscription_Start_Date = "Subscription_Start_Date";
    public static final String CT_KEY_platform = "Platform";
    public static final String CT_KEY_SOURCE = "Status";
    public static final String CT_KEY_Topics = "Topics";
    public static final String CT_KEY_Cities = "Cities";
    public static final String CT_KEY_Authors = "Authors";
    public static final String CT_KEY_Offer_Id = "Offer Id";
    public static final String CT_KEY_Date = "Date";

    public static final String CT_KEY_User_info = "User info";
    public static final String CT_KEY_Subscription_details = "Subscriptin details";
    public static final String CT_KEY_Explore_subscription_plan = "Explore subscription plan";
    public static final String CT_KEY_My_address = "My address";
    public static final String CT_KEY_Transaction_history = "Transaction history";
    public static final String CT_KEY_Change_pasword = "Change pasword";
    public static final String CT_KEY_Feedback = "Feedback";
    public static final String CT_KEY_TimeSpent = "Time Spent";
    public static final String CT_KEY_TimeSpentSeconds = "Time Spent Seconds";
    public static final String CT_KEY_Ediitons = "Editions";
    public static final String CT_KEY_AppSections = "App sections";

    public static final String CT_KEY_Section = "Section";
    public static final String CT_EVENT_HAMBERGER = "Hamberger";

    public static final String CT_FROM_TH_DEFAULT = "TH Default";
    public static final String CT_FROM_TH_PREMIUM = "THP";

    public static final String CT_KEY_KEYWORD = "Keyword";
    public static final String CT_KEY_Article_font = "Article font";
    public static final String CT_KEY_Night_mode = "Night mode";
    public static final String CT_KEY_Push_Notification = "Push Notification";
    public static final String CT_KEY_Read_aloud = "Read aloud";
    public static final String CT_KEY_Article = "Article";

    public static final String CT_KEY_Date_Subscription = "Date_Subscription";
    public static final String CT_KEY_User_Type = "User_Type";

    public static final String CT_KEY_Pack_value = "Pack value";
    public static final String CT_KEY_Pack_duration = "Pack duration";
    public static final String CT_KEY_Pack_Name = "Pack Name";
    public static final String CT_KEY_Conversion_Time  = "Conversion Time ";

    public static final String CT_KEY_PlanId = "Plan Id";

    public static final String CT_KEY_PAGE_TYPE = "Page type";
    public static final String CT_KEY_ID = "ID";
    public static final String CT_KEY_SECTIONS = "Sections";
    public static final String CT_KEY_SECTION = "Section";
    public static final String CT_KEY_AUTHOR = "Author";
    public static final String CT_KEY_IS_ARTICLE = "Is Article";


    //CleverTap Event Name (HINDU)
    public static final String CT_EVENT_MODAL_SCREEN_HOME_SCREEN = "Modal Screen - Home screen";
    public static final String CT_EVENT_MODAL_SCREEN_ARTICLE_PAGE = "Modal Screen - Article page";
    public static final String CT_EVENT_ARTICLE_READ = "Article Read";
    public static final String CT_EVENT_PAGE_VISITED = "Page Visited";
    public static final String CT_EVENT_SEARCHED = "Searched";
    public static final String CT_EVENT_NOTIFICATIONS = "Notifications";
    public static final String CT_EVENT_PERSONALIZE_HOME_SCREEN = "Personalise Home screen";
    public static final String CT_EVENT_PERSONALIZE_MY_STORIES = "Personalise My stories";
    public static final String CT_EVENT_SETTINGS = "Settings";
    public static final String CT_EVENT_SHARE_THIS_APP = "Share this app";
    public static final String CT_EVENT_WIDGET = "Widget";
    public static final String CT_EVENT_SHARE_ARTICLE = "Share article";
    public static final String CT_EVENT_BOOKMARK = "Bookmark";
    public static final String CT_EVENT_READLOUD = "Readloud";
    public static final String CT_EVENT_COMMENTS = "Comments";
    public static final String CT_EVENT_FONT = "Font";
    public static final String CT_EVENT_SHARE = "Share";

    //CleverTap Event Name (SUBSCRIPTION)
    public static final String CT_EVENT_SIGN_IN = "Sign in";
    public static String TAP_PROFILE = "Profile";
    public static final String CT_EVENT_FORGOT_PASSWORD = "Forgot Password";
    public static final String CT_EVENT_RESENT_OTP_FP = "Resend OTP_FP";
    public static final String CT_EVENT_SIGN_UP = "Sign Up";
    public static final String CT_EVENT_OTP_VARIFICATION = "OTP verificaiton";
    public static final String CT_EVENT_SUBSCRIPTION_PERSONALIZE = "Subscription Personalize";
    public static final String CT_EVENT_PROFILE = "Profile";
    public static final String CT_EVENT_BRIEFING = "Briefing";
    public static final String CT_EVENT_FREE_TRIAL = "Free Trial";
    public static final String CT_EVENT_SUBSCRIBE_NOW_PACK = "Subscribe Now_Pack";
    public static final String CT_EVENT_PAY_NOW = "Pay Now";
    public static final String CT_EVENT_PRODUCT_VIEWED = "Product viewed";
    public static final String CT_EVENT_PAYMENT_SUCCESSFUL = "Payment Successful";
    public static final String CT_EVENT_PAYMENT_FAILED = "Payment Failed";
    public static final String CT_EVENT_BENEFITS_PAGE = "Benefits Page";
    public static final String CT_EVENT_BENEFITS_FREE_TRIAL = "Benefits Free Trial";
    public static final String CT_EVENT_SIGN_OUT = "Sign out";
    public static final String CT_EVENT_PERSONAL_INFO = "Personal info";
    public static final String CT_EVENT_TIME_SPEND = "Time spent";
    public static final String CT_EVENT_READ_LATER = "Readlater";
    public static final String CT_EVENT_FAVOURTING = "Favourting";
    public static final String CT_EVENT_SHOW_FEWER_STORIES = "Show fewer strories";


    //Constant Values for CT
    public static final String CT_PAGE_TYPE_HOME = "Home";
    public static final String CT_PAGE_TYPE_SECTION = "Section";
    public static final String CT_PAGE_TYPE_ARTICLE = "Article";
    public static final String CT_PAGE_TYPE_SEARCH = "Search";

    //notification constants
    public static final String INAPP_NOTIFICATION_CLICKED = "InApp_Notification_Clicked";
    public static final String SUBSCRIPTION_PLANS_NOTIFICATION_CLICKED = "Subscription_Plans_Notification_Clicked";
    public static final String SUBSCRIPTION_PLANS_NOTIFICATION_RECEIVED = "Subscription_Plans_Notification_Received";

    public static final String INAPP_NOTIFICATION_CLICKED_CT = "InApp Notification Clicked";
    public static final String SUBSCRIPTION_PLANS_NOTIFICATION_CLICKED_CT = "Subscription Plans Notification Clicked";
    public static final String SUBSCRIPTION_PLANS_NOTIFICATION_RECEIVED_CT = "Subscription Plans Notification Received";

    //Constants for MainActivity
    public static boolean sISMAIN_ACTIVITY_LAUNCHED;
    public static boolean IS_FROM_MP_BLOCKER;

    //Firebase Subscription & in-App notification constants
    public static final String Fb_Offer_Id = "Offer_Id";
    public static final String Fb_User_Id = "User_Id";
    public static final String Fb_Date = "Date";

    //CleverTap Constant Values for MP
    public static final String MP_SignIn = "MP Sign In";
    public static final String MP_SignUp = "MP Sign Up";
    public static final String MP_Article_Count = "MP Article Count";
    public static final String Metered_Paywall = "Metered Paywall";
    public static final String MP_Banner_Subscribe = "MP Banner Subscribe";
    public static final String Conversion_From_MP = "Conversion From MP";
    public static final String Get_Full_Access_Button_Click = "Get Full Access Button Click";

    //Params
    public static final String MP_Cycle = "Cycle Name";
    public static final String ArticleCount = "Article Count";
    public static final String Allowed_Counts = "Allowed Counts";

    //Firebase Constant Values for MP
    public static final String MP_SignIn_Firebase = "MP_Sign_In";
    public static final String MP_SignUp_Firebase = "MP_Sign_Up";
    public static final String MP_Article_Count_Firebase = "MP_Article_Count";
    public static final String Metered_Paywall_Firebase = "Metered_Paywall";
    public static final String MP_Banner_Subscribe_Firebase = "MP_Banner_Subscribe";
    public static final String Conversion_From_MP_Firebase = "Conversion_From_MP";
    public static final String Get_Full_Access_Button_Click_Firebase = "Get_Full_Access_Button_Click";

    //Params
    public static final String MP_Cycle_Firebase = "Cycle_Name";
    public static final String ArticleCount_Firebase = "Article_Count";
    public static final String Allowed_Counts_Firebase = "Allowed_Counts";

    // DEFAULT



    public static final int APP_EXCLUSIVE_SECTION_ID = 142;

    public static final String IS_CITY = "IsCity";

    //Push Notification type
    public static final String ARTICLE = "article";
    public static final String PLANS_PAGE = "planPage";
    public static final String URL = "url";
    public static final String NOTIFICATION_INCOMING_FILTER = "welcome_notification";
    public static final String NEW_NOTIFICATION = "new_notification";
    public static final String LOAD_NOTIFICATIONS = "ns_load_notifications";
    public static final String PLAN_OFFER10 = "10%"; //P1
    public static final String PLAN_OFFER20 = "20%"; //P2
    public static final String PLAN_OFFER25 = "25%"; //P3


    public static final boolean IS_SHOW_INDEX = true;
    public static final boolean IS_USE_SEVER_THEME = false;

}
