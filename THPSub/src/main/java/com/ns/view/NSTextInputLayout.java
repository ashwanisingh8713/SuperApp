package com.ns.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;
import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NSTextInputLayout extends TextInputLayout {
    public NSTextInputLayout(Context context) {
        super(context);
        init(context, null);
    }

    public NSTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        boolean isDayTheme = DefaultPref.getInstance(context).isUserThemeDay();


        if(isDayTheme) {
            setHintTextAppearance(R.style.HintLabel);
            // colorControlActivated

        } else {

        }

        setUpperHintColor(Color.RED);

    }

    private void setUpperHintColor(int color) {

        String focusedTextColor = "mFocusedTextColor";
        String defaultHintTextColor = "mDefaultTextColor";

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            focusedTextColor = "focusedTextColor";
            defaultHintTextColor = "defaultHintTextColor";
        }

        try {
            Field field = this.getClass().getDeclaredField(focusedTextColor);
            field.setAccessible(true);
            int[][] states = new int[][]{
                    new int[]{}
            };
            int[] colors = new int[]{
                    color
            };
            ColorStateList myList = new ColorStateList(states, colors);
            field.set(this, myList);

            Method method = this.getClass().getDeclaredMethod("updateLabelState", boolean.class);
            method.setAccessible(true);
            method.invoke(this, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
