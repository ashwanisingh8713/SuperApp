package com.ns.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;

public class DG_DetailPhotoViewHolder extends RecyclerView.ViewHolder {
    public TextView mTitleTextView;
    public TextView mArticleLocationView;
    public TextView mUpdatedTextView;
    public TextView mCaptionTextView;
    public TextView mAuthorTextView;
    public TextView mCreatedDateTextView;
    public ImageView mHeaderImageView;
    public FrameLayout mHeaderFragmeLayout;
    public View mCaptionDevider;
    public ImageButton mMultiMediaButton;
    public DG_DetailPhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        mTitleTextView = itemView.findViewById(R.id.title);
        mArticleLocationView = itemView.findViewById(R.id.articleLocationTxt);
        mUpdatedTextView = itemView.findViewById(R.id.updated);
        mCreatedDateTextView = itemView.findViewById(R.id.createdate);
        mAuthorTextView = itemView.findViewById(R.id.author);
        mHeaderImageView = itemView.findViewById(R.id.detail_image);
        mHeaderFragmeLayout = itemView.findViewById(R.id.imageContainer);
        mMultiMediaButton = itemView.findViewById(R.id.multimedia_button);
        mCaptionTextView = itemView.findViewById(R.id.caption);
        mCaptionDevider = itemView.findViewById(R.id.captiondevider);
    }
}
