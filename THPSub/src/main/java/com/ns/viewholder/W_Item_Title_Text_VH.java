package com.ns.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class W_Item_Title_Text_VH extends RecyclerView.ViewHolder {

    public TextView mWidgetTextView, mWidgetDescripitionTextView;
    public CardView cardView;
    public LinearLayout innerParent;

    public W_Item_Title_Text_VH(View itemView) {
        super(itemView);

        mWidgetTextView = itemView.findViewById(R.id.textview_opinion_title);
        mWidgetDescripitionTextView = itemView.findViewById(R.id.textview_opinion_content);
        cardView = itemView.findViewById(R.id.widgetParentLayout);
        innerParent = itemView.findViewById(R.id.innerParent);
    }
}
