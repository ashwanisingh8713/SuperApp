package com.ns.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class SearchRecyclerHolder extends RecyclerView.ViewHolder {
    public View mParentLayout;
    public TextView title;
    public TextView sname;
    public TextView publishDate;

    public SearchRecyclerHolder(View itemView) {
        super(itemView);
        mParentLayout = itemView;
        title = itemView.findViewById(R.id.title);
        sname = itemView.findViewById(R.id.sectionName);
        publishDate = itemView.findViewById(R.id.publish_date);
    }
}
