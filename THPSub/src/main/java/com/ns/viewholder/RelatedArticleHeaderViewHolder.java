package com.ns.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.text.CustomTextView;

public class RelatedArticleHeaderViewHolder extends RecyclerView.ViewHolder {

    public View indicatorView;

    public RelatedArticleHeaderViewHolder(@NonNull View itemView) {
        super(itemView);
        indicatorView = itemView.findViewById(R.id.indicatorView);
    }

}
