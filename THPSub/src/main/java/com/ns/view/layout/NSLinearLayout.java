package com.ns.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSLinearLayout extends LinearLayout {

    private int layoutType;

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
                layoutType = typedArray.getInt(R.styleable.NSLayout_layoutType, 0);
            } else {
                layoutType = -1;
            }

            typedArray.recycle();
        }

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        // 0 = Opinion
        if(layoutType == 0) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.widget_opinion_background)); //133b5a
        }
        // 1 = Multimedia
        else if(layoutType == 1) {
            if(isUserThemeDay) {
                setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.drawable_explore_light)); // ffffff
            }
            else {
                setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.drawable_explore_dark)); // //313131
            }
        }
        // 2 = Cartoon
        else if(layoutType == 2) {
            setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.cartoon_border)); // d4d4d4
        }
        // 3 = APPEXCLUSIVE
        else if(layoutType == 3) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.red)); //CF4939
        }
        // 4 = Normal Widget
        else if(layoutType == 4) {
            if(isUserThemeDay) {
                setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.drawable_explore_light)); // ffffff
            }
            else {
                setBackground(ResUtil.getBackgroundDrawable(getResources(), R.drawable.drawable_explore_dark)); //313131
            }
        }
        // 5 = Black_Light
        else if(layoutType == 5){
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_ffffff)); // ffffff
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_313131)); //313131
            }
        }
        // 6 = Black_Dark
        else if(layoutType == 6){
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_ffffff)); //ffffff
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_191919_light)); // 191919
            }
        }
        // 7 = Black_Full
        else if(layoutType == 7) {
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_ffffff)); // ffffff
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_000000));  // 000000
            }
        }
        else {
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_ffffff)); // ffffff
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_313131)); //313131
            }
        }

    }
}
