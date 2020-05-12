package com.ns.clevertap;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.FcmMessageListenerService;
import com.clevertap.android.sdk.NotificationInfo;
import com.google.firebase.messaging.RemoteMessage;
import com.netoperation.db.THPDB;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.utils.THPConstants;

import java.util.Map;

import io.reactivex.schedulers.Schedulers;

public class HinduCTPushListenerService extends FcmMessageListenerService {

    final String TAG = "CleverTapPush";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        CleverTapAPI cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this);
        if (cleverTapDefaultInstance != null) {
            cleverTapDefaultInstance.pushFcmRegistrationId(s,true);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();

        boolean notificationEnabled = DefaultPref.getInstance(getApplicationContext()).isNotificationEnable();
        if (!notificationEnabled) {
            Log.i(TAG, "onMessageReceived: Notification not enabled from setting page");
            return;
        }

        if (null == data) {
            return;
        }

        Bundle extras = new Bundle();
        for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
            extras.putString(entry.getKey(), entry.getValue());
        }

        NotificationInfo info = CleverTapAPI.getNotificationInfo(extras);

        if (info.fromCleverTap) {
            //Notification Viewed Event
            CleverTapAPI.getDefaultInstance(this).pushNotificationViewedEvent(extras);
        } else {
            // not from CleverTap handle yourself or pass to another provider
            return;

        }
        /*
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */
        if (data.containsKey("ns_type_PN") && data.get("ns_type_PN").equalsIgnoreCase(THPConstants.ARTICLE)) {
            parseAndDisplayArticleNotification(data);
        } else if (data.containsKey("ns_type_PN") && data.get("ns_type_PN") != null && data.get("ns_type_PN").equalsIgnoreCase(THPConstants.PLANS_PAGE)) {
            parseAndDisplaySubsPlanNotification(data);
        } else if (data.containsKey("ns_type_PN") && data.get("ns_type_PN") != null && data.get("ns_type_PN").equalsIgnoreCase(THPConstants.URL)) {
            parseAndDisplayUrlNotification(data);
        }


    }

    private void parseAndDisplayArticleNotification(Map<String, String> data) {
        DefaultPref.getInstance(getApplicationContext()).putBoolean(THPConstants.NEW_NOTIFICATION, true);
        try {
            final Map<String, String> arguments = data;
            final String actionURL = arguments.get("ns_action");
            final String type = arguments.get("ns_type_PN");
            final String title = arguments.get("gcm_title");
            final String description = arguments.get("gcm_alert");
            final String imageUrl = arguments.get("ns_image");
            final String sectionName = arguments.get("ns_sec_name");
            final String pub_date = arguments.get("ns_push_date");
            final boolean isHasBody = Boolean.parseBoolean(arguments.get("ns_has_body"));
            final String parentId = arguments.get("ns_parent_id");
            final String sectionId = arguments.get("ns_section_id");

            final String articleType;
            if (arguments.containsKey("ns_article_type")) {
                articleType = arguments.get("ns_article_type");
            } else {
                articleType = "article";
            }
            int article_id = 0;
            try {
                article_id = Integer.parseInt(arguments.get("ns_article_id"));
                final int artId = article_id;

                THPDB.getInstance(getApplicationContext()).daoConfiguration().getConfigurationSingle()
                        .subscribeOn(Schedulers.io())
                        .subscribe((tableConfiguration, throwable) -> {
                            DefaultTHApiManager.articleDetailFromServer(getApplicationContext(), ""+artId, tableConfiguration.getSearchOption().getUrlId(), NetConstants.GROUP_NOTIFICATION);
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*NotificationBean bean = new NotificationBean(artId, actionURL, title, description,
                    imageUrl, sectionName, pub_date, type, isHasBody, System.currentTimeMillis(),
                    parentId, sectionId, false, articleType);
            SharedPreferenceHelper.addInJSONArray(this, bean);
            Intent mNotificationReceiver = new Intent(THPConstants.NOTIFICATION_INCOMING_FILTER);
            mNotificationReceiver.putExtra(THPConstants.NEW_NOTIFICATION, true);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(mNotificationReceiver);*/

            AppNotification.customNotificationWithImageUrl(this, arguments);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseAndDisplaySubsPlanNotification(Map<String, String> data) {
        try {
            AppNotification.customNotificationWithImageUrl(this, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseAndDisplayUrlNotification(Map<String, String> data) {
        try {
            AppNotification.customNotificationWithImageUrl(this, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
