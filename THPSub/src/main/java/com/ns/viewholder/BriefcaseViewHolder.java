package com.ns.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.view.roundedimageview.RoundedImageView;
import com.ns.thpremium.R;

public class BriefcaseViewHolder extends RecyclerView.ViewHolder {

    public TextView sectionName;
    public TextView title;
    public TextView authorName_Txt;
    public TextView description_Txt;
    public TextView time_Txt;
    public RoundedImageView image;
    public View horizontalDivider;

    public BriefcaseViewHolder(@NonNull View itemView) {
        super(itemView);

        sectionName = itemView.findViewById(R.id.sectionName);
        title = itemView.findViewById(R.id.title);
        authorName_Txt = itemView.findViewById(R.id.authorName_Txt);
        description_Txt = itemView.findViewById(R.id.description_Txt);
        time_Txt = itemView.findViewById(R.id.time_Txt);
        image = itemView.findViewById(R.id.image);
        horizontalDivider = itemView.findViewById(R.id.horizontalDivider);

    }
}
