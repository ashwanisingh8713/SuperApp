package com.ns.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.text.ArticleTitleTextView;

public class ExploreViewHolder extends RecyclerView.ViewHolder {

    public RecyclerView mExploreRecyclerView;
    public ArticleTitleTextView exploreTitle;

    public ExploreViewHolder(View itemView) {
        super(itemView);
        mExploreRecyclerView = itemView.findViewById(R.id.recyclerview_explore);
        exploreTitle = itemView.findViewById(R.id.exploreTitle);
    }
}
