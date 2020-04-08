package com.ns.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joooonho.SelectableRoundedImageView;
import com.ns.thpremium.R;
import com.ns.view.text.CustomTextView;

public class TrendingViewHolder extends RecyclerView.ViewHolder {

    public CustomTextView sectionName;
    public CustomTextView title;
    public CustomTextView authorName_Txt;
    public CustomTextView time_Txt;
    public ImageView toggleBtn_Img;
    public ImageView bookmark_Img;
    public ImageView like_Img;
    public ImageView trendingIcon_Img;
    public SelectableRoundedImageView image;
    public View horizontalDivider;

    public TrendingViewHolder(@NonNull View itemView) {
        super(itemView);

        sectionName = itemView.findViewById(R.id.sectionName);
        title = itemView.findViewById(R.id.title);
        authorName_Txt = itemView.findViewById(R.id.authorName_Txt);
        time_Txt = itemView.findViewById(R.id.time_Txt);
        toggleBtn_Img = itemView.findViewById(R.id.toggleBtn_Img);
        bookmark_Img = itemView.findViewById(R.id.bookmark_Img);
        like_Img = itemView.findViewById(R.id.like_Img);
        trendingIcon_Img = itemView.findViewById(R.id.trendingIcon_Img);
        image = itemView.findViewById(R.id.image);
        horizontalDivider = itemView.findViewById(R.id.horizontalDivider);

    }
}
