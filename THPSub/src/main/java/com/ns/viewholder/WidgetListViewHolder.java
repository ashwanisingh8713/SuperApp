package com.ns.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class WidgetListViewHolder extends RecyclerView.ViewHolder {

    public ImageView groupHeaderIcon;
    public TextView groupHeaderTxt;
    public RecyclerView groupRecyclerView;
    public TextView groupActionLeft;
    public TextView groupActionCenter;
    public TextView groupActionRight;
    public CardView cardView;


    public WidgetListViewHolder(@NonNull View itemView) {
        super(itemView);

        groupHeaderIcon = itemView.findViewById(R.id.groupHeaderIcon);
        groupHeaderTxt = itemView.findViewById(R.id.groupHeaderTxt);
        groupRecyclerView = itemView.findViewById(R.id.groupRecyclerView);
        groupActionLeft = itemView.findViewById(R.id.groupActionLeftBottom);
        groupActionCenter = itemView.findViewById(R.id.groupActionCenterBottom);
        groupActionRight = itemView.findViewById(R.id.groupActionRightBottom);
        cardView = itemView.findViewById(R.id.groupCardView);
    }

}
