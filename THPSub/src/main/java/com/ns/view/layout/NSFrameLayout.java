package com.ns.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSFrameLayout extends FrameLayout {

    private int viewType;

    public NSFrameLayout(Context context) {
        super(context);
        init(context, null);
    }

    public NSFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {

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
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_ffffff));
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_313131));
            }
        }
        // 6 = Black_Dark
        else if(viewType == 6){
            if(isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_ffffff));
            }
            else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_191919_light));
            }
        }
        else {
            if (isUserThemeDay) {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_ffffff));
            } else {
                setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_313131));
            }
        }
    }
}
