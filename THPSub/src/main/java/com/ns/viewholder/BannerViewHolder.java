package com.ns.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.img.ListingIconView;
import com.ns.view.roundedimageview.RoundedImageView;

public class BannerViewHolder extends RecyclerView.ViewHolder {

    public RoundedImageView mBannerImageView;
    public TextView mBannerTextView;
    public ViewGroup mBannerLayout;
    public TextView mArticleUpdateTime;
    public TextView mArticleSectionName;
    public ListingIconView mBookmarkButton;
    public ImageView mShareArticleButton;
    public ImageButton mMultimediaButton;

    public BannerViewHolder(View itemView) {
        super(itemView);
        mBookmarkButton = itemView.findViewById(R.id.button_bookmark);
        mBannerLayout = itemView.findViewById(R.id.layout_banner);
        mBannerImageView = itemView.findViewById(R.id.imageview_banner);
        mBannerTextView = itemView.findViewById(R.id.textview_banner);
        mArticleSectionName = itemView.findViewById(R.id.section_name);
        mArticleUpdateTime = itemView.findViewById(R.id.textview_time);
        mShareArticleButton = itemView.findViewById(R.id.button_article_share);
        mMultimediaButton = itemView.findViewById(R.id.multimedia_button);
        mArticleSectionName.setVisibility(View.VISIBLE);
    }
}