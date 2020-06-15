package com.ns.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.roundedimageview.RoundedImageView;

public class W_Item_Media_Text extends RecyclerView.ViewHolder {
    public RoundedImageView mWidgetImageView;
    public TextView mWidgetTextView;
    public CardView cardView;
    public LinearLayout innerParent;

    public W_Item_Media_Text(View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.widgetParentLayout);
        mWidgetImageView = itemView.findViewById(R.id.imageview_widget_image);
        mWidgetTextView = itemView.findViewById(R.id.textview_widget_text);
        innerParent = itemView.findViewById(R.id.innerParent);
    }
}