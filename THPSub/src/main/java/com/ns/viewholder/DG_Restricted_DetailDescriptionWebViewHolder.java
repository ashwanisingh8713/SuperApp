package com.ns.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.THP_AutoResizeWebview;
import com.ns.view.text.ArticleTitleTextView;
import com.ns.view.text.CustomTextView;

public class DG_Restricted_DetailDescriptionWebViewHolder extends RecyclerView.ViewHolder {
    public THP_AutoResizeWebview webview;
    public View shadowView_Mp;
    public ArticleTitleTextView textMpBlockerTitle;
    public ArticleTitleTextView textMPBlockerDescription;
    public ArticleTitleTextView signIn_Txt;
    public ArticleTitleTextView signUp_Txt;
    public CustomTextView getFullAccess_Txt;

    public DG_Restricted_DetailDescriptionWebViewHolder(View itemView) {
        super(itemView);
        webview = itemView.findViewById(R.id.webview);
        shadowView_Mp = itemView.findViewById(R.id.shadowView_Mp);
        textMpBlockerTitle = itemView.findViewById(R.id.textMpBlockerTitle);
        textMPBlockerDescription = itemView.findViewById(R.id.textMPBlockerDescription);
        signIn_Txt = itemView.findViewById(R.id.signIn_Txt);
        signUp_Txt = itemView.findViewById(R.id.signUp_Txt);
        getFullAccess_Txt = itemView.findViewById(R.id.getFullAccess_Txt);
    }
}
