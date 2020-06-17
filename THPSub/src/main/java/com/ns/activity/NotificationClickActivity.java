package com.ns.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.clevertap.android.sdk.CleverTapAPI;
import com.netoperation.util.NetConstants;
import com.ns.utils.IntentUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;

public class NotificationClickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {

            //For notification clicked event
            CleverTapAPI.setAppForeground(true);
            try {
                CleverTapAPI.getDefaultInstance(this).pushNotificationClickedEvent(getIntent().getExtras());
            } catch (Throwable t) {
                Log.e("ERROR",""+t);
            }
            String actionURL = getIntent().getExtras().getString("ns_action");
            String type = getIntent().getExtras().getString("ns_type_PN");

            if(type.equalsIgnoreCase(THPConstants.URL)) {
                String ns_url = getIntent().getExtras().getString("ns_url");
                if (ns_url != null) {
                    //Opening the url in Web Browser
                    IntentUtil.openUrlInBrowser(this, ns_url);
                } else {
                    if (THPConstants.sISMAIN_ACTIVITY_LAUNCHED) {
                        IntentUtil.openMainTabPage(this);
                    } else {
                        IntentUtil.callToLaunchTheApp(this);
                    }
                }
            } else if(type.equalsIgnoreCase(THPConstants.PLANS_PAGE)){
                String planOffer = getIntent().getExtras().getString("ns_plan_offer");
                if (planOffer != null && !TextUtils.isEmpty(planOffer)) {
                    IntentUtil.openSubscriptionPageOfferWise(this,
                            THPConstants.FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE, planOffer);
                } else {
                    IntentUtil.openSubscriptionActivity(NotificationClickActivity.this,
                            THPConstants.FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE);
                }
            }else{
                //Article Type
                String articleId = getIntent().getExtras().getString("ns_article_id");
                IntentUtil.openDetailAfterSearchInActivity(this, articleId, actionURL, NetConstants.G_NOTIFICATION);
            }

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String actionId = extras.getString("actionId");
                if (actionId != null) {
                    Log.d("ACTION_ID", actionId);
                    boolean autoCancel = extras.getBoolean("autoCancel", true);
                    int notificationId = extras.getInt("notificationId", -1);
                    if (autoCancel && notificationId > -1) {
                        NotificationManager notifyMgr =
                                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notifyMgr.cancel(notificationId);  // the bit that cancels the notification
                    }
                    //Toast.makeText(getBaseContext(),"Action ID is: "+actionId, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Log.i("NotificationActivity", "Bundle or Intent is Empty");
            if (THPConstants.sISMAIN_ACTIVITY_LAUNCHED) {
                IntentUtil.openMainTabPage(this);
            } else {
                IntentUtil.callToLaunchTheApp(this);
            }
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        if (intent != null && intent.getExtras() != null) {
            //For notification clicked event
            CleverTapAPI.setAppForeground(true);
            try {
                CleverTapAPI.getDefaultInstance(NotificationClickActivity.this).pushNotificationClickedEvent(intent.getExtras());
            } catch (Throwable t) {
                // Ignore
            }

            String actionURL = intent.getExtras().getString("ns_action");
            String type = intent.getExtras().getString("ns_type_PN");

            if(type.equalsIgnoreCase(THPConstants.URL)) {
                String ns_url = getIntent().getExtras().getString("ns_url");
                if (ns_url != null) {
                    //Opening the url in Web Browser
                    IntentUtil.openUrlInBrowser(this, ns_url);
                } else {
                    if (THPConstants.sISMAIN_ACTIVITY_LAUNCHED) {
                        IntentUtil.openMainTabPage(this);
                    } else {
                        IntentUtil.callToLaunchTheApp(this);
                    }
                }
            } else if(type.equalsIgnoreCase(THPConstants.PLANS_PAGE)){
                String planOffer = intent.getExtras().getString("ns_plan_offer");
                if (planOffer != null && !TextUtils.isEmpty(planOffer)) {
                    IntentUtil.openSubscriptionPageOfferWise(this,
                            THPConstants.FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE, planOffer);
                } else {
                    IntentUtil.openSubscriptionActivity(NotificationClickActivity.this,
                            THPConstants.FROM_NOTIFICATION_SUBSCRIPTION_EXPLORE);
                }
            } else {
                //Article Type
                String articleId = intent.getExtras().getString("ns_article_id");
                IntentUtil.openDetailAfterSearchInActivity(this, articleId, actionURL, NetConstants.G_NOTIFICATION);
            }
            Bundle extras = intent.getExtras();
            if (extras != null) {
                String actionId = extras.getString("actionId");
                if (actionId != null) {
                    Log.d("ACTION_ID", actionId);
                    boolean autoCancel = extras.getBoolean("autoCancel", true);
                    int notificationId = extras.getInt("notificationId", -1);
                    if (autoCancel && notificationId > -1) {
                        NotificationManager notifyMgr =
                                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notifyMgr.cancel(notificationId);  // the bit that cancels the notification
                    }
                    //Toast.makeText(getBaseContext(),"Action ID is: "+actionId, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Log.i("NotificationActivity", "Bundle or Intent is Empty");
            if (THPConstants.sISMAIN_ACTIVITY_LAUNCHED) {
                IntentUtil.openMainTabPage(this);
            } else {
                IntentUtil.callToLaunchTheApp(this);
            }
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Firebase Analytics
        THPFirebaseAnalytics.setFirbaseAnalyticsScreenRecord(NotificationClickActivity.this, "Notification Click Screen", NotificationClickActivity.class.getSimpleName());
    }
}
