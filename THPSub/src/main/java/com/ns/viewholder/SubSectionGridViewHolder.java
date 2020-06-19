package com.ns.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class SubSectionGridViewHolder extends RecyclerView.ViewHolder {

    public ImageView groupHeaderIcon;
    public TextView groupHeaderTxt;
    public RecyclerView groupRecyclerView;
    public CardView cardView;
    public ConstraintLayout innerParent;


    public SubSectionGridViewHolder(@NonNull View itemView) {
        super(itemView);

        groupHeaderIcon = itemView.findViewById(R.id.groupHeaderIcon);
        groupHeaderTxt = itemView.findViewById(R.id.groupHeaderTxt);
        groupRecyclerView = itemView.findViewById(R.id.groupRecyclerView);
        cardView = itemView.findViewById(R.id.groupCardView);
        innerParent = itemView.findViewById(R.id.innerParent);
    }

}
