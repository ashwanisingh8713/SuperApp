package com.ns.viewholder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class LoadMoreViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;
    public TextView textView;

    public LoadMoreViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.progressBar);
        textView = itemView.findViewById(R.id.txt);
    }
}
