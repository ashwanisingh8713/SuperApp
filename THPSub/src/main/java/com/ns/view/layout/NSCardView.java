package com.ns.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

public class NSCardView extends CardView {

    public NSCardView(Context context) {
        super(context);
        init(context, null);
    }

    public NSCardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NSCardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attrs) {

        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(isUserThemeDay) {
            setCardBackgroundColor(ResUtil.getColor(getResources(), R.color.color_ffffff)); //ffffff
        }
        else {
            setCardBackgroundColor(ResUtil.getColor(getResources(), R.color.color_191919_light)); // 191919
        }

    }
}
