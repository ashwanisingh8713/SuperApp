package com.ns.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;

public class LogoImgView extends AppCompatImageView {

    private int imgType = -1;

    public LogoImgView(Context context) {
        super(context);
        init(context, null);
    }

    public LogoImgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LogoImgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NSImageLogo);
            if (typedArray.hasValue(R.styleable.NSImageLogo_logoType)) {
                imgType = typedArray.getInt(R.styleable.NSImageLogo_logoType, 0);
            } else {
                imgType = -1;
            }
        }

        // 0 = app:logoType="Splash"
        if(imgType == 0) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.splash_logo);
            } else {
                setImageResource(R.drawable.splash_logo_dark);
            }
        }
        // 1 = app:logoType="Section"
        else if(imgType == 1) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.logo_actionbar);
            } else {
                setImageResource(R.drawable.logo_actionbar_dark);
            }
        }
        // 2 = app:logoType="ToolbarTHP"
        else if(imgType == 2) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.logo_actionbar);
            } else {
                setImageResource(R.drawable.logo_actionbar_dark);
            }
        }

    }


}
