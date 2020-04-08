package com.ns.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.netoperation.util.UserPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSLinearLayout extends LinearLayout {

    private int viewType;

    public NSLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public NSLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSLayout);
            if (typedArray.hasValue(R.styleable.NSLayout_layoutType)) {
                viewType = typedArray.getInt(R.styleable.NSLayout_layoutType, 0);
            } else {
                viewType = -1;
            }
        }

        boolean isUserThemeDay = UserPref.getInstance(context).isUserThemeDay();

        // 0 = Opinion
        if(viewType == 0) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.widget_opinion_background));
        }
        // 1 = Multimedia
        else if(viewType == 1) {
            if(isUserThemeDay) {
                setBackground(getResources().getDrawable(R.drawable.drawable_explore_border));
            }
            else {
                setBackground(getResources().getDrawable(R.drawable.dark_drawable_explore_border));
            }
        }
        // 2 = Cartoon
        else if(viewType == 2) {
            setBackground(getResources().getDrawable(R.drawable.cartoon_border));
        }
        // 3 = APPEXCLUSIVE
        else if(viewType == 3) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.widget_app_exclusive_background));
        }
        // 4 = Normal Widget
        else if(viewType == 4) {
            if(isUserThemeDay) {
                setBackground(getResources().getDrawable(R.drawable.drawable_explore_border));
            }
            else {
                setBackground(getResources().getDrawable(R.drawable.dark_drawable_explore_border));
            }
        }
        // 5 = Black_Light
        else if(viewType == 5){
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_background));
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_background));
            }
        }
        // 6 = Black_Dark
        else if(viewType == 6){
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_home_banner_background));
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_home_banner_background));
            }
        }
        // 7 = Black_Full
        else if(viewType == 7) {
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.toolbar_white));
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.toolbar_dark));
            }
        }
        else {
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_background));
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_background));
            }
        }

    }
}
