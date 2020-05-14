package com.ns.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class BannerViewHolder extends RecyclerView.ViewHolder {

    public ImageView mBannerImageView;
    public TextView mBannerTextView;
    public LinearLayout mBannerLayout;
    public TextView mArticleUpdateTime;
    public TextView mArticleSectionName;
    public ImageView mBookmarkButton;
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