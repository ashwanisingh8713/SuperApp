package com.ns.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.netoperation.model.RecoBean;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ashwanisingh on 03/03/18.
 */

public class CommonUtil {

    public static Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }


    public static int getDeviceHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static String getDateFormat(String date_updated) {
        String result = "";
        try {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(date_updated);
            result = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static int getDeviceWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    /**
     * Hides the keyboard displayed for the SearchEditText.
     * @param view The view to detach the keyboard from.
     */
    public static void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }




    public static int getArticleIdFromArticleUrl(String url) {
        Pattern p = Pattern.compile("article(\\d+)");
        Matcher m = p.matcher(url);
        int mArticleId = 0;
        if (m.find()) {
            try {
                mArticleId = Integer.parseInt(m.group(1));
            } catch (Exception e) {
                return 0;
            }
        }

        return mArticleId;
    }

    public static List<String> getFolderImageList(){
        List<String> mList = new ArrayList<>();
        File sdCardRoot = new File(Environment.getExternalStorageDirectory().getPath() , "NewsDXImgFolder");
        for (File f : sdCardRoot.listFiles()) {
            String name =null;
            if (f.isFile()) {
                name = f.getName();
            }
            mList.add(name);
        }
        return mList;
    }


    public static void shareArticle(Context mContext, RecoBean bean) {
        String mShareTitle = bean.getArticletitle();
        String mShareUrl = bean.getArticleUrl();
        String sectionName = bean.getArticleSection();


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


    }


    public static Intent getSharingIntent(String mShareTitle,String mShareUrl) {

        if (mShareTitle == null) {
            mShareTitle = "";
        }
        if (mShareUrl == null) {
            mShareUrl = "";
        }
        Intent sharingIntent = new Intent(
                android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        if (mShareUrl != null && !mShareUrl.contains("thehindu.com")) {
            mShareUrl = "http://thehindu.com" + mShareUrl;
        }
        String shareBody = mShareTitle
                + ": " + mShareUrl;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                mShareTitle);
        sharingIntent
                .putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TITLE,
                mShareTitle);
        return Intent.createChooser(sharingIntent, "Share Via");
    }

    public static String getAutors(List<String> authors) {
        if(authors == null || authors.size() == 0) {
            return null;
        }
        String auth = "";
        int size = authors.size();
        int count = 1;
        for(String author : authors) {

            if(size == count) {
                auth += author;
            }else {
                auth += author +", ";
            }

        }
        return auth;
    }

    public static String fomatedDate(String publishDate, String from) {
        String formatedPubDt = "";
        if(from.equalsIgnoreCase(NetConstants.BREIFING_ALL) || from.equalsIgnoreCase(NetConstants.BREIFING_EVENING)
                || from.equalsIgnoreCase(NetConstants.BREIFING_NOON) || from.equalsIgnoreCase(NetConstants.BREIFING_MORNING)) {
            formatedPubDt = AppDateUtil.getDurationFormattedDate(
                    AppDateUtil.strToMlsForBriefing(publishDate), Locale.ENGLISH);
        } else if(from.equalsIgnoreCase(NetConstants.RECO_TEMP_NOT_EXIST)) {
            formatedPubDt = AppDateUtil.getDurationFormattedDate(
                    AppDateUtil.strToMlsForSearchedArticle(publishDate), Locale.ENGLISH);
        }
        else {
            formatedPubDt = AppDateUtil.getDurationFormattedDate(
                    AppDateUtil.strToMlsForNonBriefing(publishDate), Locale.ENGLISH);
        }
        return formatedPubDt;
    }

    public static int numDaysFrmCurrentDate(String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
            Log.d("Current Date", dateFormat.format(new Date()));
            Date enDate = dateFormat.parse(endDate);
            long diff = enDate.getTime() - currentDate.getTime();
            Log.d("Days", ""+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

            return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isStartDateFutureDate(String startDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
            //Log.d("Current Date", dateFormat.format(new Date()));
            Date sDate = dateFormat.parse(startDate);
            return sDate.getTime() > currentDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void sendBroadcastToKillBecomeAMemberScreen(Activity activityContext){
        Intent intent = new Intent(THPConstants.BROADCAST_ACTION_KILL_BECOME_MEMBER_PAGE);
        activityContext.sendBroadcast(intent);
    }

    public static boolean launchWebBrowser(Context context, String webLink) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webLink));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        } else {
            //Toast.makeText(context, "Please Install a Web Browser", Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
