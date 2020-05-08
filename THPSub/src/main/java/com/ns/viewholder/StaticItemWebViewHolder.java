package com.ns.viewholder;

import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;
import com.ns.thpremium.R;
import com.ns.view.THP_AutoResizeWebview;


public class StaticItemWebViewHolder extends RecyclerView.ViewHolder {

    public THP_AutoResizeWebview webView;
    public ProgressBar progressBar;
    public View mDummyView;

    public StaticItemWebViewHolder(View itemView) {
        super(itemView);
        webView = itemView.findViewById(R.id.staticWebView);
        progressBar = itemView.findViewById(R.id.progressBar);
        mDummyView = itemView.findViewById(R.id.dummyView);
    }
}
