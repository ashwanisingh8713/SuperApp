package com.ns.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSLinearLayout2 extends LinearLayout {

    private int viewType;

    public NSLinearLayout2(Context context) {
        super(context);
        init(context, null);
    }

    public NSLinearLayout2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSLinearLayout2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        // 0 = Opinion
        if(viewType == 0) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.widget_opinion_background)); //133b5a
        }
        // 1 = Multimedia
        else if(viewType == 1) {
            if(isUserThemeDay) {
                setBackground(getResources().getDrawable(R.drawable.drawable_explore_light)); // ffffff
            }
            else {
                setBackground(getResources().getDrawable(R.drawable.drawable_explore_dark)); // //313131
            }
        }
        // 2 = Cartoon
        else if(viewType == 2) {
            setBackground(getResources().getDrawable(R.drawable.cartoon_border)); // d4d4d4
        }
        // 3 = APPEXCLUSIVE
        else if(viewType == 3) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.widget_app_exclusive_background)); //CF4939
        }
        // 4 = Normal Widget
        else if(viewType == 4) {
            if(isUserThemeDay) {
                setBackground(getResources().getDrawable(R.drawable.drawable_explore_light)); // ffffff
            }
            else {
                setBackground(getResources().getDrawable(R.drawable.drawable_explore_dark)); //313131
            }
        }
        // 5 = Black_Light
        else if(viewType == 5){
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_light)); // ffffff
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_dark_313131)); //313131
            }
        }
        // 6 = Black_Dark
        else if(viewType == 6){
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_home_banner_background)); //ffffff
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_dark)); // 191919
            }
        }
        // 7 = Black_Full
        else if(viewType == 7) {
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.toolbar_white)); // ffffff
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.toolbar_dark));  // 000000
            }
        }
        else {
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_light)); // ffffff
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_dark_313131)); //313131
            }
        }

    }
}
