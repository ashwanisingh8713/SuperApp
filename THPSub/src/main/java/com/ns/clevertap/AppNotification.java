package com.ns.clevertap;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.netoperation.db.THPDB;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.ns.activity.NotificationClickActivity;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

/**
 * Created by NS on 5/7/2020.
 */

public class AppNotification {

    private static void customNotificationWithImageUrl(Context context, Map<String, String> arguments, Bitmap bitmap) {

        String title = arguments.get("nt");
        String body = arguments.get("nm");

        int icon = getNotificationIcon();
        long when = System.currentTimeMillis();

        //generating unique ID
        int uniqueID = (int) System.currentTimeMillis();

        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        CharSequence name="APP ss";
        String desc="this is notific";
        int imp= NotificationManager.IMPORTANCE_HIGH;
        final String ChannelID="TheHindu";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(ChannelID, name, imp);
            mChannel.setDescription(desc);
            mChannel.setLightColor(Color.CYAN);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(mChannel);
        }


        final RemoteViews collapsedView = new RemoteViews(context.getPackageName(), R.layout.custom_collapsed_notification);
        if(BuildConfig.IS_BL) {

        } else {
            collapsedView.setImageViewResource(R.id.notificationLogo, R.mipmap.app_launcher);
        }
        collapsedView.setTextViewText(R.id.title_textview, body);
        collapsedView.setTextViewText(R.id.description_textview, title);
        collapsedView.setLong(R.id.time_textview, "setTime", System.currentTimeMillis());

        RemoteViews expandedView = new RemoteViews(context.getPackageName(), R.layout.custom_expanded_notification);
        try {
            expandedView.setTextViewText(R.id.title_textview, title);
            expandedView.setTextViewText(R.id.description_textview, body);
            expandedView.setLong(R.id.time_textview, "setTime", System.currentTimeMillis());
            if (bitmap != null) {
                expandedView.setImageViewBitmap(R.id.notification_image, bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Notification notification = new NotificationCompat.Builder(context, ChannelID)
                .setSmallIcon(icon)
                .setContentTitle(body)
                .setWhen(when)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .build();

        //Commented because Deprecated warning coming for contentView, bigContentView. Replaced with setCustomContentView, setCustomBigContentView
        //notification.contentView = collapsedView;
        //notification.bigContentView = expandedView;

        Intent notificationIntent = new Intent(context, NotificationClickActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationIntent.putExtra(THPConstants.LOAD_NOTIFICATIONS, true);

        //Add CleverTap Extras
        for (Map.Entry<String, String> entry : arguments.entrySet()) {
            notificationIntent.putExtra(entry.getKey(), entry.getValue());
        }
        final String actionURL = arguments.get("ns_action");
        final String type = arguments.get("ns_type_PN");
        final String titlee = arguments.get("gcm_title");
        final String description = arguments.get("gcm_alert");
        final String imageUrll = arguments.get("ns_image");
        final String sectionName = arguments.get("ns_sec_name");
        final String pub_date = arguments.get("ns_push_date");
        final String ns_has_body = arguments.get("ns_has_body");
        final String parentId = arguments.get("ns_parent_id");
        final String sectionId = arguments.get("ns_section_id");
        final String ns_article_id = arguments.get("ns_article_id");
        final String ns_offer_id = arguments.get("ns_offer_id");
        final String ns_url = arguments.get("ns_url");

        final String articleType;
        if (arguments.containsKey("ns_article_type")) {
            articleType = arguments.get("ns_article_type");
        } else {
            articleType = "article";
        }

        notificationIntent.putExtra("ns_action", actionURL);
        notificationIntent.putExtra("ns_type_PN", type);
        notificationIntent.putExtra("gcm_title", titlee);
        notificationIntent.putExtra("gcm_alert", description);
        notificationIntent.putExtra("ns_image", imageUrll);
        notificationIntent.putExtra("ns_sec_name", sectionName);
        notificationIntent.putExtra("ns_push_date", pub_date);
        notificationIntent.putExtra("ns_has_body", ns_has_body);
        notificationIntent.putExtra("ns_parent_id", parentId);
        notificationIntent.putExtra("ns_section_id", sectionId);
        notificationIntent.putExtra("ns_article_type", articleType);
        notificationIntent.putExtra("ns_article_id", ns_article_id);
        notificationIntent.putExtra("ns_offer_id", ns_offer_id);
        notificationIntent.putExtra("ns_url", ns_url);

        PendingIntent contentIntent = PendingIntent.getActivity(context, uniqueID, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        notification.contentIntent = contentIntent;


        notification.flags |= Notification.FLAG_AUTO_CANCEL; // clear the notification
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration

        mNotificationManager.notify(uniqueID, notification);

        int article_id = 0;
        try {
            article_id = Integer.parseInt(ns_article_id);
            final int artId = article_id;
        } catch (Exception e) {
            e.printStackTrace();
        }

        final int artId = article_id;

        THPDB.getInstance(context).daoConfiguration().getConfigurationSingle()
                .subscribeOn(Schedulers.io())
                .subscribe((tableConfiguration, throwable) -> {
                    DefaultTHApiManager.articleDetailFromServer(context,
                            ""+artId, tableConfiguration.getSearchOption().getUrlId(), NetConstants.G_NOTIFICATION)
                            .subscribe(val->{
                                Log.i("", "");
                            }, throwable1 -> {
                                Log.i("", "");
                            });
                });

    }

    public static void customNotificationWithImageUrl(Context context, Map<String, String> arguments) {
        if(arguments != null) {
            final String imgUrl = arguments.get("ns_image");
            new CustomNotificationWithImgUrlTask(context, arguments).execute(imgUrl);
        }
    }


    public static void customTestNotification(Context context, boolean isArticleType, boolean isSubscriptionPlan, boolean isUrlType) {

        Map<String, String> arguments = new HashMap<>();

        if(isSubscriptionPlan){
            arguments.put("ns_type_PN", "planPage");
            //Plan offer type
            //arguments.put("ns_plan_offer", "10%");
        } else if (isArticleType){
            arguments.put("ns_type_PN", "article");
            arguments.put("ns_action", "https://www.thehindu.com/news/national/other-states/14-migrant-workers-run-over-by-goods-train-in-maharashtra/article31531352.ece?");
            arguments.put("ns_parent_id", "0");
            arguments.put("ns_section_id", "0");
            arguments.put("ns_article_id", "31531352");
        } else if (isUrlType){
            arguments.put("ns_type_PN", "url");
            arguments.put("ns_url", "https://www.thehindu.com/news/national/other-states/14-migrant-workers-run-over-by-goods-train-in-maharashtra/article31531352.ece?");
        } else {
            return;
        }
        arguments.put("nt", context.getString(R.string.APP_NAME));
        arguments.put("gcm_title", context.getString(R.string.APP_NAME));
        arguments.put("nm", arguments.get("ns_type_PN").toUpperCase()+" : 15 migrant workers run over by goods train in Maharashtra");
        arguments.put("gcm_alert", arguments.get("ns_type_PN").toUpperCase()+" : 15 migrant workers run over by goods train in Maharashtra");
        arguments.put("ns_image", "https://www.thehindu.com/news/national/other-states/ir60zu/article31531497.ece/ALTERNATES/FREE_960/aurangabadtrain-police");
        arguments.put("ns_push_date", "2019-04-03 08:08:53");
        arguments.put("ns_has_body", "true");

        final String imgUrl = arguments.get("ns_image");
        new CustomNotificationWithImgUrlTask(context, arguments).execute(imgUrl);
    }


    /**
     * It gives notification small icon.
     * @return
     */
    private static int getNotificationIcon() {
        return R.drawable.icon_notification;
    }

    /**
     * Downloads Bitmap image from URL and shows notification.
     */
    private static class CustomNotificationWithImgUrlTask extends AsyncTask<String, Void, Bitmap> {

        Context ctx;
        Map<String, String> arguments;

        public CustomNotificationWithImgUrlTask(Context context, Map<String, String> arguments) {
            super();
            this.ctx = context;
            this.arguments = arguments;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            String imageUrl = params[0];

            try {
                return BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
                /*Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                int newWidth = 900;
                int newHeight = 500;

                float scaleWidth = ((float) newWidth) / width;
                float scaleHeight = ((float) newHeight) / height;
                // CREATE A MATRIX FOR THE MANIPULATION
                Matrix matrix = new Matrix();
                // RESIZE THE BIT MAP
                matrix.postScale(scaleWidth, scaleHeight);

                // "RECREATE" THE NEW BITMAP
                Bitmap resizedBitmap = Bitmap.createBitmap(
                        bitmap, 0, 0, width, height, matrix, false);
                bitmap.recycle();
                return resizedBitmap;*/
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*InputStream in;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            try {
                customNotificationWithImageUrl(ctx, arguments, result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
