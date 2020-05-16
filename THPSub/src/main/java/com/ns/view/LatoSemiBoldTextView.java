package com.ns.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Vicky on 12/10/16.
 */

public class LatoSemiBoldTextView extends AppCompatTextView {

    public LatoSemiBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LatoSemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LatoSemiBoldTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(
                    getContext().getAssets(),
                    "fonts/Lato-SemiBold.ttf");
            setTypeface(tf);
        }
    }

}