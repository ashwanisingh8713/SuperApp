package com.ns.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;

public class EmptyImageView extends AppCompatImageView {

    private int imgType = -1;

    public EmptyImageView(Context context) {
        super(context);
        init(context, null);
    }

    public EmptyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public EmptyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();

        if(attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EmptyImageView);
            if (typedArray.hasValue(R.styleable.EmptyImageView_emptyView)) {
                imgType = typedArray.getInt(R.styleable.EmptyImageView_emptyView, 0);
            } else {
                imgType = -1;
            }
        }

        // 0 = TH_Bookmark
        if(imgType == 0) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_bookmark_empty);
            } else {
                setImageResource(R.drawable.ic_bookmark_empty_dark);
            }
        }
        // 1 = THP_Bookmark
        else if(imgType == 1) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_bookmark_empty);
            } else {
                setImageResource(R.drawable.ic_bookmark_empty_dark);
            }
        }
        // 2 = Briefing
        else if(imgType == 2) {

        }
        // 3 = Mystories
        else if(imgType == 3) {

        }
        // 4 = Suggestion
        else if(imgType == 4) {

        }
        // 5 = Crown
        else if(imgType == 5) {
            if (isUserThemeDay) {
                setImageResource(R.drawable.ic_empty_watermark);
            } else {
                setImageResource(R.drawable.ic_empty_watermark);
            }
        }

    }
}
