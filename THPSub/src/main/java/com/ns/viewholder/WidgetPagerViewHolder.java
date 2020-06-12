package com.ns.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.ns.thpremium.R;

public class WidgetPagerViewHolder extends RecyclerView.ViewHolder {

    public View groupHeaderIcon;
    public TextView groupHeaderTxt;
    public ViewPager2 groupViewPager;
    public TextView groupActionLeftText;
    public TextView groupActionCenterText;


    public WidgetPagerViewHolder(@NonNull View itemView) {
        super(itemView);

        groupHeaderIcon = itemView.findViewById(R.id.groupHeaderIcon);
        groupHeaderTxt = itemView.findViewById(R.id.groupHeaderTxt);
        groupViewPager = itemView.findViewById(R.id.groupViewPager);
        groupActionLeftText = itemView.findViewById(R.id.groupActionLeftBottom);
        groupActionCenterText = itemView.findViewById(R.id.groupActionCenterBottom);
    }

}
