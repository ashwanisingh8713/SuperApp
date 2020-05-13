package com.ns.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.layout.NSFrameLayout;

public class InlineAdViewHolder extends RecyclerView.ViewHolder {

    public NSFrameLayout frameLayout;
    public TextView indexTxt;

    public InlineAdViewHolder(View itemView) {
        super(itemView);
        frameLayout = itemView.findViewById(R.id.inline_adview);
        indexTxt = itemView.findViewById(R.id.indexTxt);
    }
}
