package com.ns.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class TaboolaNativeAdViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout mAdContainer;
    public FrameLayout thumbNailContainer;
    public View mAttributionView;
    public RelativeLayout parentContainer;

    public TaboolaNativeAdViewHolder(@NonNull View itemView) {
        super(itemView);
        mAdContainer = itemView.findViewById(R.id.textLayoutLinear);
        thumbNailContainer = itemView.findViewById(R.id.frameLayoutContainer);
        mAttributionView = itemView.findViewById(R.id.attribution_view);
        parentContainer = itemView.findViewById(R.id.parentContainer);
    }
}
