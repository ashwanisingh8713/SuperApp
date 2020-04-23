package com.ns.view.text;

import android.content.Context;
import android.util.AttributeSet;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class ArticleTitleTextView extends CustomTextView {

    public ArticleTitleTextView(Context context) {
        super(context);
        init(context);
    }

    public ArticleTitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArticleTitleTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context) {

        if(getTextColor() != 0) {
            return;
        }

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();
        // 0 = "L", Large
        if(getViewType() == 0) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_banner_text));
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.dark_color_banner_text));
            }
        }
        // 1 = "M", Medium
        else if(getViewType() == 1) {
                if (isUserThemeDay) {
                    setTextColor(ResUtil.getColor(getResources(), R.color.article_list_text));
                } else {
                    setTextColor(ResUtil.getColor(getResources(), R.color.dark_article_list_text));
                }
        }
        // 2 = "S", Small
        else if(getViewType() == 2) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.article_list_text));
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.dark_article_list_text));
            }
        }
        // 3 = "XS", X_Small
        else if(getViewType() == 3) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.article_list_text));
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.dark_article_list_text));
            }
        }
        // 4 = "White" colored text View
        else if(getViewType() == 4) {
            setTextColor(ResUtil.getColor(getResources(), R.color.white));
        }
        // 5 = "color_static", color_static
        else if(getViewType() == 5) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_static_text));
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.dark_color_static_text));
            }
        }
        else {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_static_text));
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.dark_color_static_text));
            }
        }


    }



}
