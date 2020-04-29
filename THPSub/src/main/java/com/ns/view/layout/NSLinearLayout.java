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

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(isUserThemeDay) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_home_banner_background)); //ffffff
        }
        else {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.background_dark)); // 191919
        }

    }
}
