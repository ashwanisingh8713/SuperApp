package com.ns.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;

import com.netoperation.model.ArticleBean;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;


/**
 * Created by arvind on 22/11/16.
 */

public class SharingArticleUtil {

public static void shareArticle(Context mContext,String mShareTitle,String mShareUrl, String sectionName){


    if (mShareTitle == null) {
        mShareTitle ="Download The Hindu official app.";
    }
    if (mShareUrl == null) {
        mShareUrl ="https://play.google.com/store/apps/details?id=com.mobstac.thehindu";
    }
    if (sectionName == null) {
        sectionName ="";
    }


    // This is from Share Intent
    Intent intent = getSharingIntent(mShareTitle, mShareUrl);
    mContext.startActivity(intent);

//    FlurryAgent.logEvent(mContext.getString(R.string.ga_section_share_button_clicked));
//
//    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, mContext.getString(R.string.ga_action),
//            mContext.getString(R.string.ga_section_share_button_clicked), mContext.getString(R.string.ga_article_detail_lebel));
}


    public static void shareArticle(Context mContext, ArticleBean bean) {


        String mShareTitle = bean.getTi();
        String mShareUrl = bean.getAl();
        String sectionName = bean.getSname();
        String parentName = bean.getParentName();

        if (mShareTitle == null) {
            mShareTitle ="Download The Hindu official app.";
        }
        if (mShareUrl == null) {
            mShareUrl ="https://play.google.com/store/apps/details?id=com.mobstac.thehindu";
        }
        if (sectionName == null) {
            sectionName ="";
        }


        // This is from Share Intent
        Intent intent = getSharingIntent(mShareTitle, mShareUrl);
        mContext.startActivity(intent);

//        FlurryAgent.logEvent(mContext.getString(R.string.ga_section_share_button_clicked));
//        GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, mContext.getString(R.string.ga_action),
//                mContext.getString(R.string.ga_section_share_button_clicked), mContext.getString(R.string.ga_article_detail_lebel));

        //Clever Tap Event
        HashMap<String,Object> map = new HashMap<>();
        if(TextUtils.isEmpty(parentName)){
            map.put(THPConstants.CT_KEY_Section, sectionName);
            //map.put(THPConstants.CT_KEY_SubSection, "");
        }else {
            map.put(THPConstants.CT_KEY_Section, parentName);
            map.put(THPConstants.CT_KEY_SubSection, sectionName);
        }
        CleverTapUtil.cleverTapEvent(mContext, THPConstants.CT_EVENT_SHARE_ARTICLE, map);
        CleverTapUtil.cleverTapEvent(mContext, THPConstants.CT_EVENT_SHARE, null);

    }



    public static Intent getSharingIntent(String mShareTitle,String mShareUrl) {

        if (mShareTitle == null) {
            mShareTitle = "";
        }
        if (mShareUrl == null) {
            mShareUrl = "";
        }
        Intent sharingIntent = new Intent(
                Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        if (mShareUrl != null && !mShareUrl.contains("thehindu.com")) {
            mShareUrl = "https://www.thehindu.com\n\n" + mShareUrl;
        }
        String shareBody = mShareTitle
                + ": " + mShareUrl;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT,
                mShareTitle);
        sharingIntent
                .putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_TITLE,
                mShareTitle);
        return Intent.createChooser(sharingIntent, "Share Via");
    }


    /***** Shared Screenshot Code  Start *******/

    private static final String SHARED_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".myfileprovider";
    private static final String SHARED_FOLDER = "shared";
    public static void getSharedScreenShot(Activity context, Bitmap bitmap){
        // Create a random image and save it in private app folder
        File sharedFile = null;
        try {
            sharedFile = createFile(context,bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get the shared file's Uri
        final Uri uri = FileProvider.getUriForFile(context, SHARED_PROVIDER_AUTHORITY, sharedFile);

        // Create a intent
        final ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(context)
                .setType("image/*")
                .addStream(uri);

        // Start the intent
        final Intent chooserIntent = intentBuilder.createChooserIntent();
        context.startActivity(chooserIntent);
    }

    public static File createFile(Activity activity,Bitmap randomBitmap) throws IOException {

        final File sharedFolder = new File(activity.getFilesDir(), SHARED_FOLDER);
        sharedFolder.mkdirs();

        final File sharedFile = File.createTempFile("picture", ".png", sharedFolder);
        sharedFile.createNewFile();

        writeBitmap(sharedFile, randomBitmap);
        return sharedFile;
    }

    private static void writeBitmap(final File destination,
                                    final Bitmap bitmap) {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(destination);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            close(outputStream);
        }
    }

    private static void close(final Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    /***** Shared Screenshot Code  End *******/

}
