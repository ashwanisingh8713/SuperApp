package com.ns.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.layout.NSFrameLayout;

public class InlineAdViewHolder extends RecyclerView.ViewHolder {

    public NSFrameLayout frameLayout;

    public InlineAdViewHolder(View itemView) {
        super(itemView);
        frameLayout = itemView.findViewById(R.id.inline_adview);
    }
}
