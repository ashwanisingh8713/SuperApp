package com.ns.viewholder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class SensexViewHolder extends RecyclerView.ViewHolder {
    public TextView mBseTitle;

    public TextView mNseTitle;


    public LinearLayout mBseParentLayout;
    public LinearLayout mNseParentLayout;

    public RelativeLayout bseValParent;
    public RelativeLayout nseValParent;

    public SensexViewHolder(View itemView) {
        super(itemView);
        mBseTitle = itemView.findViewById(R.id.bseSensexTitle);
        mNseTitle = itemView.findViewById(R.id.nseSensexTitle);

        mBseParentLayout = itemView.findViewById(R.id.bseParentLayout);
        mNseParentLayout = itemView.findViewById(R.id.nseParentLayout);

        bseValParent = itemView.findViewById(R.id.bseValLayout);
        nseValParent = itemView.findViewById(R.id.nseValLayout);


    }
}
