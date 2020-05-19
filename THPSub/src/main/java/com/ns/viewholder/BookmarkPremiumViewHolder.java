package com.ns.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.joooonho.SelectableRoundedImageView;
import com.ns.thpremium.R;
import com.ns.view.img.BaseImgView;
import com.ns.view.text.CustomTextView;

public class BookmarkPremiumViewHolder extends RecyclerView.ViewHolder {

    public CustomTextView sectionName;
    public CustomTextView title;
    public CustomTextView authorName_Txt;
    public CustomTextView time_Txt;
    public BaseImgView bookmark_Img;
    public ImageView share_Img;
    public SelectableRoundedImageView image;
    public View horizontalDivider;

    public ProgressBar bookmarkProgressBar;

    public BookmarkPremiumViewHolder(@NonNull View itemView) {
        super(itemView);

        sectionName = itemView.findViewById(R.id.sectionName);
        title = itemView.findViewById(R.id.title);
        authorName_Txt = itemView.findViewById(R.id.authorName_Txt);
        time_Txt = itemView.findViewById(R.id.time_Txt);
        bookmark_Img = itemView.findViewById(R.id.bookmark_Img);
        share_Img = itemView.findViewById(R.id.share_Img);
        image = itemView.findViewById(R.id.image);
        horizontalDivider = itemView.findViewById(R.id.horizontalDivider);

        bookmarkProgressBar = itemView.findViewById(R.id.bookmarkProgressBar);

    }
}
