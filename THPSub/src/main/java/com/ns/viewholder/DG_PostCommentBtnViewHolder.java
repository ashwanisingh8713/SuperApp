package com.ns.viewholder;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class DG_PostCommentBtnViewHolder extends RecyclerView.ViewHolder  {

    public Button post_comment_detail;

    public DG_PostCommentBtnViewHolder(@NonNull View itemView) {
        super(itemView);
        post_comment_detail = itemView.findViewById(R.id.post_comment_detail);
    }

}
