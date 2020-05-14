package com.ns.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.netoperation.config.model.Breadcrumb;
import com.netoperation.default_db.TableConfiguration;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;


public class CustomTabLayout extends TabLayout {
    private Typeface mTypeface;

    public CustomTabLayout(Context context) {
        super(context);
        init(context, null);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        boolean isUserThemeDay = DefaultPref.getInstance(context).isUserThemeDay();
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();

        int tabType = 0; // defaultTab
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTabLayout);
            tabType = typedArray.getInt(R.styleable.CustomTabLayout_tabType, 0);
        }

        String background = null;
        String indicator = null ;
        String textColor = null ;
        String textSelectedColor = null;

        if (tableConfiguration != null && THPConstants.IS_USE_SEVER_THEME) {
            if (isUserThemeDay) { // Day Mode
                if (tabType == 1) { //sectionTop
                    Breadcrumb breadcrumb = tableConfiguration.getAppTheme().getBreadcrumb();
                    background = breadcrumb.getBg().getLight();
                    indicator = breadcrumb.getIndicator().getLight();
                    textColor = breadcrumb.getText().getLight();
                    textSelectedColor = breadcrumb.getText().getLightSelected();
                }
                else if (tabType == 2) { //bottomTab
                    Breadcrumb bottomBar = tableConfiguration.getAppTheme().getBottomBar();
                    background = bottomBar.getBg().getLight();
                    indicator = bottomBar.getIndicator().getLight();
                    textColor = bottomBar.getText().getLight();
                    textSelectedColor = bottomBar.getText().getLightSelected();
                }
            }
            else { // Night Mode
                if (tabType == 1) { //sectionTop
                    Breadcrumb breadcrumb = tableConfiguration.getAppTheme().getBreadcrumb();
                    background = breadcrumb.getBg().getDark();
                    indicator = breadcrumb.getIndicator().getDark();
                    textColor = breadcrumb.getText().getDark();
                    textSelectedColor = breadcrumb.getText().getDarkSelected();
                }
                else if (tabType == 2) { //bottomTab
                    Breadcrumb bottomBar = tableConfiguration.getAppTheme().getBottomBar();
                    background = bottomBar.getBg().getDark();
                    indicator = bottomBar.getIndicator().getDark();
                    textColor = bottomBar.getText().getDark();
                    textSelectedColor = bottomBar.getText().getDarkSelected();
                }
            }

            if(!ResUtil.isEmpty(background) && !ResUtil.isEmpty(indicator) && !ResUtil.isEmpty(textColor)) {
                setBackgroundColor(Color.parseColor(background));
                setSelectedTabIndicatorColor(Color.parseColor(indicator));
                setTabTextColors(Color.parseColor(textColor), Color.parseColor(textSelectedColor));
            }
            else {
                setDefaultColors(isUserThemeDay);
            }
        }
        else {
            setDefaultColors(isUserThemeDay);
        }
        mTypeface = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.THP_FiraSans_Regular));
    }

    private void setDefaultColors(boolean isUserThemeDay) {
        if (isUserThemeDay) {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.color_titlestrip_background));
            setTabTextColors(ResUtil.getColor(getResources(), R.color.text_warm_grey), ResUtil.getColor(getResources(), R.color.color_banner_text));
            setSelectedTabIndicatorColor(ResUtil.getColor(getResources(), R.color.text_peacock_blue));

        } else {
            setBackgroundColor(ResUtil.getColor(getResources(), R.color.dark_color_titlestrip_background));
            setTabTextColors(ResUtil.getColor(getResources(), R.color.dark_text_warm_grey), ResUtil.getColor(getResources(), R.color.dark_color_banner_text));
            setSelectedTabIndicatorColor(ResUtil.getColor(getResources(), R.color.dark_text_peacock_blue));
        }
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

}
