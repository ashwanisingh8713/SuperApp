package com.ns.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSEditText extends AppCompatEditText {

    int textColor = -1;
    int hintColor = -1;

    public NSEditText(Context context) {
        super(context);
        init(context, null);
    }

    public NSEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextView);
            if (typedArray.hasValue(R.styleable.TextView_textColor)) {
                textColor = typedArray.getColor(R.styleable.TextView_textColor, Color.WHITE);
                hintColor = typedArray.getColor(R.styleable.TextView_hintColor, Color.WHITE);
            }

        }

        if(getTextColor() != -1) {
            setTextColor(getTextColor());
        }

        if(getHintColor() != -1) {
            setHintTextColor(getHintColor());
        }

        if(getTextColor() != -1) {
            return;
        }

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if (isUserThemeDay) {
            setTextColor(ResUtil.getColor(getResources(), R.color.color_424242));
            setHintTextColor(ResUtil.getColor(getResources(), R.color.color_424242));
        } else {
            setTextColor(ResUtil.getColor(getResources(), R.color.color_ededed_dark));
            setHintTextColor(ResUtil.getColor(getResources(), R.color.color_ededed_dark));
        }
    }


    public int getTextColor() {
        return textColor;
    }

    public int getHintColor() {
        return hintColor;
    }
}
