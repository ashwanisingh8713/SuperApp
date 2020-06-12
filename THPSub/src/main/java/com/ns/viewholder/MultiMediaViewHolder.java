package com.ns.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;
import com.ns.view.roundedimageview.RoundedImageView;

public class MultiMediaViewHolder extends RecyclerView.ViewHolder {

    public RoundedImageView mWidgetImageView;
    public TextView mWidgetTextView;
    public TextView mWidgetTime;
    public ImageView mPlayButton;
    public View mParentView;
    public CardView cardView;

    public MultiMediaViewHolder(View itemView) {
        super(itemView);
        mParentView = itemView;
        cardView = itemView.findViewById(R.id.widgetParentLayout);
        mWidgetImageView = itemView.findViewById(R.id.imageview_multimedia_thumbnail);
        mWidgetTextView = itemView.findViewById(R.id.textview_multimedia_title);
        mWidgetTime = itemView.findViewById(R.id.textview_multimedia_time);
        mPlayButton = itemView.findViewById(R.id.button_multimedia_play);
    }
}
