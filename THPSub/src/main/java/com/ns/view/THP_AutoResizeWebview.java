
package com.ns.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.netoperation.config.model.ArticleTextColor;
import com.netoperation.config.model.ColorOptionBean;
import com.netoperation.default_db.TableConfiguration;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;

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
        String bgColor = "#ffffff";
        String descriptionTextColor = "#181818";
        String leadColor = "#181818";
        String linkColor = "#2435E7";
        String linkVisitedColor = "#2435E7";  // green
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null) {
            ColorOptionBean screenBg = tableConfiguration.getAppTheme().getScreenBg();
            ArticleTextColor articleTextColor = tableConfiguration.getAppTheme().getArticleText();
            if(BaseAcitivityTHP.sIsDayTheme) {
                descriptionTextColor = articleTextColor.getLight().getDetail();
                linkColor = articleTextColor.getLight().getLink();
                leadColor = articleTextColor.getLight().getLead();
                bgColor = screenBg.getLight();
            } else {
                descriptionTextColor = articleTextColor.getDark().getDetail();
                linkColor = articleTextColor.getDark().getLink();
                leadColor = articleTextColor.getDark().getLead();
                bgColor = screenBg.getDark();
            }
        }

        String fontPath = context.getResources().getString(R.string.THP_TundraOffc);

        String leadText = "";

        return description = "<html><head>"
                + "<style type=\"text/css\">body{color: " +
                descriptionTextColor +
                "; background-color: " +
                bgColor +
                ";}" +
                "@font-face {\n" +
                "   font-family: 'tundra';\n" +
                "   src: url('file:///android_asset/" +
                fontPath +
                "');" +
                "} " +
                "body {font-family: 'tundra';}"
                + "a {color: " +
                linkColor +
                ";}a:visited {color: " +
                linkVisitedColor +
                ";}</style></head>"
                + "<body>"
                + "<i> " + "<font color=\"" +
                leadColor +
                "\">" + leadText + "</font></i>"
                + description
                + "</body></html>";


    }


    public static String defaultgroup_showDescription(Context context, String leadText, String description) {
        if (description != null) {
            description = description.trim();
        }

        String bgColor = "#ffffff";
        String descriptionTextColor = "#181818";
        String leadColor = "#ffffff";
        String linkColor = "#2435E7";
        String linkVisitedColor = "#2435E7";  // blue
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();
        if(tableConfiguration != null) {
            ColorOptionBean screenBg = tableConfiguration.getAppTheme().getScreenBg();
            ArticleTextColor articleTextColor = tableConfiguration.getAppTheme().getArticleText();
            if(articleTextColor != null) {
                if (BaseAcitivityTHP.sIsDayTheme) {
                    descriptionTextColor = articleTextColor.getLight().getDetail();
                    linkColor = articleTextColor.getLight().getLink();
                    leadColor = articleTextColor.getLight().getLead();
                    bgColor = screenBg.getLight();
                } else {
                    descriptionTextColor = articleTextColor.getDark().getDetail();
                    linkColor = articleTextColor.getDark().getLink();
                    leadColor = articleTextColor.getDark().getLead();
                    bgColor = screenBg.getDark();
                }
            }
        }

        String fontPath = context.getResources().getString(R.string.Detail_Description_Font);

        description = "<html><head>"
                + "<style type=\"text/css\">body{color: " +
                descriptionTextColor +
                "; background-color: " +
                bgColor +
                ";}" +
                "@font-face {\n" +
                "   font-family: 'tundra';\n" +
                "   src: url('file:///android_asset/" +
                fontPath +
                "');" +
                "} " +
                "body {font-family: 'tundra';}"
                + "a {color: " +
                linkColor +
                ";}a:visited {color: " +
                linkVisitedColor +
                ";}</style></head>"
                + "<body>"
                + "<i> " + "<font color=\"" +
                leadColor +
                "\">" + leadText + "</font></i>"
                + description
                + "</body></html>";

        /*final boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();
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
        }*/
        return description;
    }




}
