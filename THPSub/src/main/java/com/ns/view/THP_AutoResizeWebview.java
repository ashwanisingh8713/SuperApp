
package com.ns.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.netoperation.util.UserPref;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class THP_AutoResizeWebview extends WebView {


    private int currentIndex = -1;
    private Context mContext;

    public THP_AutoResizeWebview(Context context) {
        this(context, null);
        mContext = context;
    }


    public THP_AutoResizeWebview(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        mContext = context;
    }

    public THP_AutoResizeWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;


        // This for page fit
//        getSettings().setJavaScriptEnabled(true);
//        getSettings().setAllowContentAccess(true);
//        getSettings().setLoadWithOverviewMode(true);
//        getSettings().setUseWideViewPort(true);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setSize(int newIndex) {
        currentIndex = newIndex;
        if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH) {
            if(currentIndex == 4) {
                getSettings().setTextZoom(100 + ((currentIndex - 1) * 17));
            } else if(currentIndex == 3) {
                getSettings().setTextZoom(100 + ((currentIndex - 1) * 12));
            }
            else {
                getSettings().setTextZoom(100 + ((currentIndex - 1) * 10));
            }
        } else {
            switch (currentIndex) {
                case THPConstants.DESCRIPTION_SMALL: {
                    getSettings().setTextSize(WebSettings.TextSize.SMALLER);
                    break;
                }
                case THPConstants.DESCRIPTION_NORMAL: {
                    getSettings().setTextSize(WebSettings.TextSize.NORMAL);
                    break;
                }
                case THPConstants.DESCRIPTION_LARGE: {
                    getSettings().setTextSize(WebSettings.TextSize.LARGER);
                    break;
                }
                case THPConstants.DESCRIPTION_LARGEST: {
                    getSettings().setTextSize(WebSettings.TextSize.LARGEST);
                    break;
                }
            }
        }
        invalidate();

    }

    public static String premium_WebTextDescription(Context context, String description, boolean isItalic) {

        if(description == null) {
            description = "";
        }
        final boolean isUserThemeDay = UserPref.getInstance(context).isUserThemeDay();
        String bodyCSS = "color: #000; background-color: #ffffff;";
        String urlColor = "";
        if(isUserThemeDay) {
            bodyCSS = "color: #000; background-color: #ffffff;";
            urlColor = "";
        }
        else {
            bodyCSS = "color: #fff; background-color: #181818;";
            urlColor = "a {color: #ffffff;}a:visited {color: #ffffff;}";
        }

//        String font = "THP_FiraSans-Regular.ttf";
        String font = "THP_TundraOffc.ttf";

        if(isItalic) {
            font = "THP_TundraOffc-Italic.ttf";
        }
        return "<html><head>"
                + "<style type=\"text/css\">body{" +
                bodyCSS  +
                ";}" +
                "@font-face {\n" +
                "   font-family: 'tundra';\n" +
                "   src: url('file:///android_asset/fonts/" +
                font +
                "');" +
                "} " +
                "body {font-family: 'tundra';}"
                +urlColor
                + "</style></head>"
                + "<body>"
                + description
                + "</body></html>";


    }


    public static String defaultgroup_showDescription(Context context, String leadText, String description) {
        if (description != null) {
            description = description.trim();
        }

        String fontPath = context.getResources().getString(R.string.THP_TundraOffc);

        final boolean isUserThemeDay = UserPref.getInstance(context).isUserThemeDay();
        if (leadText != null && TextUtils.isEmpty(leadText)) {
            if (isUserThemeDay) {
                description = "<html><head>"
                        + "<style type=\"text/css\">body{color: #000; background-color: #ffffff;}" +
                        "@font-face {\n" +
                        "   font-family: 'tundra';\n" +
                        "   src: url('file:///android_asset/" +
                        fontPath +
                        "');" +
                        "} " +
                        "body {font-family: 'tundra';}"
                        + "</style></head>"
                        + "<body>"
                        + description
                        + "</body></html>";
            } else {
                description = "<html><head>"
                        + "<style type=\"text/css\">body{color: #fff; background-color: #181818;}" +
                        "@font-face {\n" +
                        "   font-family: 'tundra';\n" +
                        "   src: url('file:///android_asset/" +
                        fontPath +
                        "');" +
                        "} " +
                        "body {font-family: 'tundra';}"
                        + "a {color: #ffffff;}a:visited {color: #ffffff;}</style></head>"
                        + "<body>"
                        + description
                        + "</body></html>";
            }
        } else {
            if (isUserThemeDay) {
                description = "<html><head>"
                        + "<style type=\"text/css\">body{color: #000; background-color: #ffffff;}" +
                        "@font-face {\n" +
                        "   font-family: 'tundra';\n" +
                        "   src: url('file:///android_asset/" +
                        fontPath +
                        "');" +
                        "} " +
                        "body {font-family: 'tundra';}"
                        + "</style></head>"
                        + "<body>"
                        + "<i> " + "<font color=\"#666666\">" + leadText + "</font></i>"
                        + description
                        + "</body></html>";
            } else {
                description = "<html><head>"
                        + "<style type=\"text/css\">body{color: #fff; background-color: #181818;}" +
                        "@font-face {\n" +
                        "   font-family: 'tundra';\n" +
                        "   src: url('file:///android_asset/" +
                        fontPath +
                        "');" +
                        "} " +
                        "body {font-family: 'tundra';}"
                        + "a {color: #ffffff;}a:visited {color: #ffffff;}</style></head>"
                        + "<body>"
                        + "<i> " + "<font color=\"#E8EAEC\">" + leadText + "</font></i>"
                        + description
                        + "</body></html>";
            }
        }
        return description;
    }




}
