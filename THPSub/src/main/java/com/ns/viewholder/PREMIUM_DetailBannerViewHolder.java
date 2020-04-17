package com.ns.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ns.thpremium.R;


/**
 * Created by ashwanisingh on 25/09/18.
 */

public class PREMIUM_DetailBannerViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_section;
    public TextView tv_time;
    public TextView tv_title;
    public TextView tv_author_name;
    public TextView tv_city_name;
    public TextView tv_updated_time;
    public ImageView imageView;
    public ImageView articleTypeimageView;
    public TextView tv_caption;
    public View shadowOverlay;

    public PREMIUM_DetailBannerViewHolder(View itemView) {
        super(itemView);
        tv_section = itemView.findViewById(R.id.tv_section);
        tv_time = itemView.findViewById(R.id.tv_time);
        tv_title = itemView.findViewById(R.id.tv_title);
        tv_author_name = itemView.findViewById(R.id.tv_author_name);
        tv_city_name = itemView.findViewById(R.id.tv_city_name);
        tv_updated_time = itemView.findViewById(R.id.tv_updated_time);
        tv_caption = itemView.findViewById(R.id.tv_caption);
        imageView = itemView.findViewById(R.id.imageView);
        articleTypeimageView = itemView.findViewById(R.id.articleTypeimageView);
        shadowOverlay = itemView.findViewById(R.id.shadowOverlay);
    }
}
