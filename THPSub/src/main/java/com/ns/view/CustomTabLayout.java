package com.ns.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.netoperation.util.DefaultPref;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;

/**
 * Created by arvind on 26/1/17.
 */

public class CustomTabLayout extends TabLayout {
    private Typeface mTypeface;

    public CustomTabLayout(Context context) {
        super(context);
        init(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();
        if(isUserThemeDay) {
            setTabTextColors(ResUtil.getColor(getResources(), R.color.text_warm_grey), ResUtil.getColor(getResources(), R.color.color_banner_text));
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_titlestrip_background));
            setSelectedTabIndicatorColor(ResUtil.getColor(getResources(), R.color.text_peacock_blue));
        }
        else {
            setTabTextColors(ResUtil.getColor(getResources(), R.color.dark_text_warm_grey), ResUtil.getColor(getResources(), R.color.dark_color_banner_text));
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_titlestrip_background));
            setSelectedTabIndicatorColor(ResUtil.getColor(getResources(), R.color.dark_text_peacock_blue));
        }
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.THP_FiraSans_Regular));
    }

    @Override
    public void addTab(Tab tab) {
        super.addTab(tab);

        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTypeface(mTypeface, Typeface.NORMAL);
            }
        }
    }

    public void changeTabsFont(TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    TextView viewChild = (TextView) tabViewChild;
                    viewChild.setTypeface(mTypeface);
                    viewChild.setAllCaps(false);
                }
            }
        }
    }
}
