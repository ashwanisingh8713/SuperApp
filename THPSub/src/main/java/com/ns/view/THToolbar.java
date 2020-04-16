package com.ns.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.netoperation.util.UserPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class THToolbar extends Toolbar {

    private int viewType;
    boolean isUserThemeDay;


    public THToolbar(Context context) {
        super(context);
        init(context, null);
    }

    public THToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public THToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        isUserThemeDay = UserPref.getInstance(context).isUserThemeDay();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSLayout);
            if (typedArray.hasValue(R.styleable.NSLayout_layoutType)) {
                viewType = typedArray.getInt(R.styleable.NSLayout_layoutType, 0);
            } else {
                viewType = -1;
            }
        }

        if (isUserThemeDay) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.toolbar_white));
            setTitleTextColor(ResUtil.getColor(getResources(), R.color.toolbar_dark));
        } else {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.toolbar_dark));
            setTitleTextColor(ResUtil.getColor(getResources(), R.color.toolbar_white));
        }

        setToolbarBackButtonDefault();

        // Setting Custom Font
        for(int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);

            if(view instanceof TextView) {
                TextView textView = (TextView) view;

                Typeface myCustomFont= FontCache.getTypeface(getResources().getString(R.string.THP_FiraSans_Bold), context);
                textView.setTypeface(myCustomFont); }
        }

    }



    public void setToolbarBackButtonDefault() {
        setLogo(null);
        if(isUserThemeDay) {
                setNavigationIcon(R.drawable.arrow_back);
            } else {
                setNavigationIcon(R.drawable.ic_arrow_back_w);
            }
    }

    public void hideToolbarLogo() {
            setLogo(null);
    }

    public void setToolbarBackButton(@DrawableRes int drawableId) {
        setLogo(null);
        setNavigationIcon(drawableId);
    }


}
