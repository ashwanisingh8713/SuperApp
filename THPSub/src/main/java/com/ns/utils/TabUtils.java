package com.ns.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ns.thpremium.R;

public class TabUtils {

    private String [] tabNames;
    private int [] tabUnSelectedIcons;
    private int [] tabSelectedIcons;
    private boolean mIsDayTheme;

    public TabUtils (String [] tabNames, int[] tabSelectedIcons, int[] tabUnSelectedIcons, boolean isDayTheme) {
        this.tabNames = tabNames;
        this.tabSelectedIcons = tabSelectedIcons;
        this.tabUnSelectedIcons = tabUnSelectedIcons;
        this.mIsDayTheme = isDayTheme;
    }

    public View getTabView(int position, Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.apptab_custom_tab, null);
        TextView tv = v.findViewById(R.id.tab_Txt);
        ImageView tabIcon = v.findViewById(R.id.tab_Icon);

        tabIcon.setImageResource(tabUnSelectedIcons[position]);
        tv.setText(tabNames[position]);
        return v;
    }

    public void SetOnSelectView(Context context, TabLayout tabLayout, int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        if(selected == null) {return;}
        TextView iv_text = selected.findViewById(R.id.tab_Txt);
        ImageView tabIcon = selected.findViewById(R.id.tab_Icon);
        tabIcon.setImageResource(tabSelectedIcons[position]);
        if(mIsDayTheme) {
            iv_text.setTextColor(context.getResources().getColor(R.color.boldBlackColor));
        } else {
            iv_text.setTextColor(context.getResources().getColor(R.color.blueColor_1));
        }
    }

    public void SetUnSelectView(Context context, TabLayout tabLayout,int position) {
        TabLayout.Tab tab = tabLayout.getTabAt(position);
        View selected = tab.getCustomView();
        if(selected == null) {return;}
        TextView iv_text = selected.findViewById(R.id.tab_Txt);
        ImageView tabIcon = selected.findViewById(R.id.tab_Icon);
        tabIcon.setImageResource(tabUnSelectedIcons[position]);
        iv_text.setTextColor(context.getResources().getColor(R.color.greyColor_1));
    }
}
