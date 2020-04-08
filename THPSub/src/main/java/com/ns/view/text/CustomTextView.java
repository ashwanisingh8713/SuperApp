package com.ns.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.ns.thpremium.R;
import com.ns.view.FontCache;

public class CustomTextView extends AppCompatTextView {

    String mFontPath;
    int viewType = -1;
    int textColor = 0;

    public CustomTextView(Context context) {
        super(context);
        init(context, null);

    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);

    }

    public void applyCustomFont(Context context, String fontName) {
        Typeface customFont = FontCache.getTypeface(fontName, context);
        setTypeface(customFont);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {
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

            if (mFontPath == null || mFontPath.isEmpty()) {
                mFontPath = getResources().getString(R.string.THP_FiraSans_Regular);
            }

            applyCustomFont(context, mFontPath);

            typedArray.recycle();
        }

        if(getTextColor() != 0) {
            setTextColor(getTextColor());
        }
    }

    public int getViewType() {
        return viewType;
    }

    public int getTextColor() {
        return textColor;
    }
}
