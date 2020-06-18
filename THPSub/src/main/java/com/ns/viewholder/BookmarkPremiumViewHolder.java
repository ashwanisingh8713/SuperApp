package com.ns.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.view.roundedimageview.RoundedImageView;
import com.ns.thpremium.R;
import com.ns.view.img.BaseImgView;
import com.ns.view.text.CustomTextView;

public class BookmarkPremiumViewHolder extends RecyclerView.ViewHolder {

    public TextView sectionName;
    public TextView title;
    public TextView authorName_Txt;
    public TextView time_Txt;
    public BaseImgView bookmark_Img;
    public ImageView share_Img;
    public RoundedImageView image;
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
