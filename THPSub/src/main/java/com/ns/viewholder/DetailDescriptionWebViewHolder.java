package com.ns.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.THP_AutoResizeWebview;

public class DetailDescriptionWebViewHolder extends RecyclerView.ViewHolder {
    public THP_AutoResizeWebview mLeadTxt;
    public THP_AutoResizeWebview webview;

    public DetailDescriptionWebViewHolder(View itemView) {
        super(itemView);
        webview = itemView.findViewById(R.id.webview);
        mLeadTxt = itemView.findViewById(R.id.leadTxt);
    }
}
