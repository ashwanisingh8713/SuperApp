package com.ns.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class BL_WidgetsViewHolder extends RecyclerView.ViewHolder {

    public RecyclerView mWidgetsRecyclerView;
    public TextView mWidgetTitleTextView, mWidgetFooterTextView;
    public LinearLayoutManager layoutManager;

    public BL_WidgetsViewHolder(View itemView) {
        super(itemView);

        mWidgetsRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview_widgets);
        mWidgetTitleTextView = (TextView) itemView.findViewById(R.id.textview_widget_title);
        mWidgetFooterTextView = (TextView) itemView.findViewById(R.id.textview_widget_viewAll);
        layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mWidgetsRecyclerView.setLayoutManager(layoutManager);

    }
}
