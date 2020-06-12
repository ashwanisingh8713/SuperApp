package com.ns.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class W_Item_Media_VH extends RecyclerView.ViewHolder {

    public ImageView mWidgetImageView;
    public CardView cardView;

    public W_Item_Media_VH(View itemView) {
        super(itemView);
        mWidgetImageView = itemView.findViewById(R.id.imageview_widget_cartoon);
        cardView = itemView.findViewById(R.id.widgetParentLayout);

    }
}
