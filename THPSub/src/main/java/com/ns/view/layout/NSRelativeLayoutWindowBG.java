package com.ns.view.layout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.netoperation.util.DefaultPref;

public class NSRelativeLayoutWindowBG extends RelativeLayout {

    public NSRelativeLayoutWindowBG(Context context) {
        super(context);
        init(context, null);
    }

    public NSRelativeLayoutWindowBG(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSRelativeLayoutWindowBG(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
