/*
 * Copyright (c) 2014 Mobstac TM
 * All Rights Reserved.
 * @since Sep 23, 2014 
 * @author rajeshcp
 */
package com.ns.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class ProgressView extends RelativeLayout {

    private TextView mTextView;
    private ProgressBar mProgressBar;

    public ProgressView(Context context) {
        this(context, null);
    }


    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addChildren();
    }


    @SuppressLint("NewApi")
    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addChildren();
    }


    private void addChildren() {
        mProgressBar = new ProgressBar(getContext());
        int size = (int) convertDpToPixel(50, getContext());
        LayoutParams params = new LayoutParams(size, size);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        addView(mProgressBar, params);

        LayoutParams textparams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        textparams.addRule(RelativeLayout.CENTER_IN_PARENT);

        mTextView = new TextView(getContext());
        addView(mTextView, textparams);
        mTextView.setVisibility(View.GONE);
    }


    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
    }


    public void setErrorText(final String text) {
        mProgressBar.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText(text);
    }

    private float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}
