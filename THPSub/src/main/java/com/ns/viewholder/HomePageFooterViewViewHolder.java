package com.ns.viewholder;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class HomePageFooterViewViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout mParentLayout;

    public HomePageFooterViewViewHolder(View itemView) {
        super(itemView);
        mParentLayout = itemView.findViewById(R.id.parentLayout);
    }
}
