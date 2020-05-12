package com.ns.view.layout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSLinearLayoutWindowBG extends LinearLayout {

    public NSLinearLayoutWindowBG(Context context) {
        super(context);
        init(context, null);
    }

    public NSLinearLayoutWindowBG(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSLinearLayoutWindowBG(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(isUserThemeDay) {
            setBackgroundColor(Color.parseColor("#ffffff"));
        }
        else {
            setBackgroundColor(Color.parseColor("#191919"));
        }

    }
}
