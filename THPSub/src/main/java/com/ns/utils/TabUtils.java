package com.ns.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.netoperation.config.model.Breadcrumb;
import com.netoperation.config.model.TabsBean;
import com.netoperation.default_db.TableConfiguration;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;

import java.util.List;

public class TabUtils {

    private String [] tabNames;
    private int [] tabUnSelectedIcons;
    private int [] tabSelectedIcons;
    private boolean mIsDayTheme;

    private List<TabsBean> tabsBeans;

    String background = null;
    String indicator = null ;
    String textColor = null ;
    String textSelectedColor = null;

    public TabUtils (String [] tabNames, int[] tabSelectedIcons, int[] tabUnSelectedIcons, boolean isDayTheme, List<TabsBean> tabsBeans) {
        this.tabNames = tabNames;
        this.tabSelectedIcons = tabSelectedIcons;
        this.tabUnSelectedIcons = tabUnSelectedIcons;
        this.mIsDayTheme = isDayTheme;
        this.tabsBeans = tabsBeans;
        TableConfiguration tableConfiguration = BaseAcitivityTHP.getTableConfiguration();

        if(tableConfiguration != null) {
            if (mIsDayTheme) { // Day Mode
                Breadcrumb bottomBar = tableConfiguration.getAppTheme().getBottomBar();
                background = bottomBar.getBg().getLight();
                indicator = bottomBar.getIndicator().getLight();
                textColor = bottomBar.getText().getLight();
                textSelectedColor = bottomBar.getText().getLightSelected();

            } else { // Night Mode
                Breadcrumb bottomBar = tableConfiguration.getAppTheme().getBottomBar();
                background = bottomBar.getBg().getDark();
                indicator = bottomBar.getIndicator().getDark();
                textColor = bottomBar.getText().getDark();
                textSelectedColor = bottomBar.getText().getDarkSelected();

            }
        }
    }

    public View getTabView(int position, Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.apptab_custom_tab, null);
        TextView tv = v.findViewById(R.id.tab_Txt);
        ImageView tabIcon = v.findViewById(R.id.tab_Icon);

        if(THPConstants.IS_USE_SEVER_THEME) {
            PicassoUtil.loadImageFromCache(context, tabIcon, tabsBeans.get(position).getIconUrl().getLocalFilePath());
        }
        else {
            tabIcon.setImageResource(tabUnSelectedIcons[position]);
        }


        tv.setText(tabNames[position]);
        return v;
    }

    public void SetOnSelectView(Context context, TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        if(selected == null) {return;}
        TextView iv_text = selected.findViewById(R.id.tab_Txt);
        ImageView tabIcon = selected.findViewById(R.id.tab_Icon);

        if(THPConstants.IS_USE_SEVER_THEME) {
            iv_text.setTextColor(Color.parseColor(textSelectedColor));
            selected.setBackgroundColor(Color.parseColor(indicator));
            PicassoUtil.loadImageFromCache(context, tabIcon, tabsBeans.get(position).getIconUrl().getLocalFileSelectedPath());
        } else {
            tabIcon.setImageResource(tabSelectedIcons[position]);
            if (mIsDayTheme) {
                iv_text.setTextColor(ResUtil.getColor(context.getResources(), R.color.boldBlackColor));
            } else {
                iv_text.setTextColor(ResUtil.getColor(context.getResources(), R.color.blueColor_1));
            }
        }
    }

    public void SetUnSelectView(Context context, TabLayout tabLayout,int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        if(selected == null) {return;}
        TextView iv_text = selected.findViewById(R.id.tab_Txt);
        ImageView tabIcon = selected.findViewById(R.id.tab_Icon);

        if(THPConstants.IS_USE_SEVER_THEME) {
            iv_text.setTextColor(Color.parseColor(textColor));
            selected.setBackgroundColor(Color.parseColor(background));
            PicassoUtil.loadImageFromCache(context, tabIcon, tabsBeans.get(position).getIconUrl().getLocalFilePath());
        } else {
            iv_text.setTextColor(ResUtil.getColor(context.getResources(), R.color.greyColor_1));
            tabIcon.setImageResource(tabUnSelectedIcons[position]);
        }
    }
}
