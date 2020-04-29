package com.ns.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSConstraintLayout extends ConstraintLayout {

    private int viewType;

    public NSConstraintLayout(Context context) {
        super(context);
        init(context, null);
    }

    public NSConstraintLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSConstraintLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, @Nullable AttributeSet attrs) {

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSLayout);
            if (typedArray.hasValue(R.styleable.NSLayout_layoutType)) {
                viewType = typedArray.getInt(R.styleable.NSLayout_layoutType, 0);
            } else {
                viewType = -1;
            }
        }

        // 5 = Black_Light
        if(viewType == 5){
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_light));
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_dark_313131));
            }
        }
        // 6 = Black_Dark
        else if(viewType == 6){
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_home_banner_background));
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_dark));
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
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_light));
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_dark_313131));
            }
        }
    }
}
