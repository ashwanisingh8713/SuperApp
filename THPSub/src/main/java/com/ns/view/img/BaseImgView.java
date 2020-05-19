package com.ns.view.img;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class BaseImgView extends AppCompatImageView {

    public BaseImgView(Context context) {
        super(context);
    }

    public BaseImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateIcon(int iconType){

    }



}
