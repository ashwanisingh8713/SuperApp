package com.ns.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class W_Item_Text_VH extends RecyclerView.ViewHolder {

    public TextView mTitleTextView;
    public FrameLayout innerParent;
    public CardView cardView;

    public W_Item_Text_VH(View itemView) {
        super(itemView);
        mTitleTextView = itemView.findViewById(R.id.title);
        innerParent = itemView.findViewById(R.id.innerParent);
        cardView = itemView.findViewById(R.id.widgetParentLayout);
    }
}