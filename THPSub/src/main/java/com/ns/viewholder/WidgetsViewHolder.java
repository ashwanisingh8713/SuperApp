package com.ns.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class WidgetsViewHolder extends RecyclerView.ViewHolder {

    public RecyclerView mWidgetsRecyclerView;
    public TextView mWidgetTitleTextView, mWidgetFooterTextView;
    public LinearLayoutManager layoutManager;

    public WidgetsViewHolder(View itemView) {
        super(itemView);

        mWidgetsRecyclerView = itemView.findViewById(R.id.recyclerview_widgets);
        mWidgetTitleTextView = itemView.findViewById(R.id.textview_widget_title);
        mWidgetFooterTextView = itemView.findViewById(R.id.textview_widget_viewAll);
        layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mWidgetsRecyclerView.setLayoutManager(layoutManager);
    }
}
