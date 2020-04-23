package com.ns.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;
import com.ns.view.FontCache;

public class CustomEditView extends AppCompatEditText {

    String mFontPath;
    int viewType = -1;
    int textColor = 0;
    int hintColor = 0;

    public CustomEditView(Context context) {
        super(context);
        init(context, null);

    }

    public CustomEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public CustomEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);

    }

    public void applyCustomFont(Context context, String fontName) {
        Typeface customFont = FontCache.getTypeface(fontName, context);
        setTypeface(customFont);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextView);

            mFontPath = typedArray.getString(R.styleable.TextView_font_path);
            if (typedArray.hasValue(R.styleable.TextView_viewType)) {
                viewType = typedArray.getInt(R.styleable.TextView_viewType, 0);
            } else {
                viewType = -1;
            }

            if (typedArray.hasValue(R.styleable.TextView_textColor)) {
                textColor = typedArray.getColor(R.styleable.TextView_textColor, Color.WHITE);
            }

            if (typedArray.hasValue(R.styleable.TextView_hintColor)) {
                hintColor = typedArray.getColor(R.styleable.TextView_hintColor, Color.WHITE);
            }

            if (mFontPath == null || mFontPath.isEmpty()) {
                mFontPath = getResources().getString(R.string.THP_FiraSans_Regular);
            }

            applyCustomFont(context, mFontPath);

            typedArray.recycle();
        }

        if(getTextColor() != 0) {
            setTextColor(getTextColor());
        }

        if(hintColor != 0) {
            setHintTextColor(hintColor);
        }

        // 5 = "color_static", color_static
        if(getViewType() == 5) {
            if (isUserThemeDay) {
                setTextColor(ResUtil.getColor(getResources(), R.color.color_static_text));
            } else {
                setTextColor(ResUtil.getColor(getResources(), R.color.dark_color_static_text));
            }
        }
    }

    public int getViewType() {
        return viewType;
    }

    public int getTextColor() {
        return textColor;
    }
}
