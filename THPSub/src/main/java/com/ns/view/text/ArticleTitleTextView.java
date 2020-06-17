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
        // 0 = "XL", XLarge
        if(getTextType() == 0) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_424242)); // 424242
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_ededed_dark)); // ededed
            }
        }
        // 1 = "L", Large
        else if(getTextType() == 1) {
                if (isUserThemeDay) {
                    setTextColor(ResUtil.getColor(getResources(), R.color.color_111111_light)); // 111111
                } else {
                    setTextColor(ResUtil.getColor(getResources(), R.color.color_ededed_dark)); // ededed
                }
        }
        // 2 = "S", Small
        else if(getTextType() == 2) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_111111_light)); // 111111
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_ededed_dark)); // ededed
            }
        }
        // 3 = "XS", X_Small
        else if(getTextType() == 3) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_818181_light)); // 818181
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_818181_light)); // 818181
            }
        }
        // 4 = "WigetText" colored text View
        else if(getTextType() == 4) {
            setTextColor(ResUtil.getColor(getResources(), R.color.white));
        }
        // 5 = "M", M
        else if(getTextType() == 5) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_191919_light));  // 191919
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_ededed_dark)); // ededed
            }
        }
        else {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_191919_light)); // 191919
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_ededed_dark));  // ededed
            }
        }


    }



}
