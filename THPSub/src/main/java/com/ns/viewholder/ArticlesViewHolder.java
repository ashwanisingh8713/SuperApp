package com.ns.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.img.ListingIconView;

/**
 * Normal Row Item View Holder
 */
public class ArticlesViewHolder extends RecyclerView.ViewHolder {

    public ImageView mArticleImageView;
    public TextView mArticleTextView;
    public TextView mArticleTimeTextView;
    public TextView mArticleSectionName;
    public TextView author_textView;
    public ViewGroup mArticlesLayout;
    public ListingIconView mBookmarkButton;
    public ImageView mShareArticleButton;
    public ImageButton mMultimediaButton;
    public FrameLayout mImageParentLayout;

    public ArticlesViewHolder(View itemView) {
        super(itemView);
        mMultimediaButton = itemView.findViewById(R.id.multimedia_button);
        mImageParentLayout = itemView.findViewById(R.id.imageParentLayout);
        mArticlesLayout = itemView.findViewById(R.id.layout_articles_root);
        mArticleImageView = itemView.findViewById(R.id.imageview_article_list_image);
        mArticleTextView = itemView.findViewById(R.id.textview_article_list_header);
        mShareArticleButton = itemView.findViewById(R.id.button_article_share);
        mArticleTimeTextView = itemView.findViewById(R.id.textview_time);
        mBookmarkButton = itemView.findViewById(R.id.button_bookmark);
        mArticleSectionName = itemView.findViewById(R.id.section_name);
        author_textView = itemView.findViewById(R.id.author_textView);
        mArticleSectionName.setVisibility(View.VISIBLE);
    }
}
