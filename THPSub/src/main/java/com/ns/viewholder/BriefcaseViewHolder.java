package com.ns.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.view.roundedimageview.RoundedImageView;
import com.ns.thpremium.R;
import com.ns.view.text.CustomTextView;

public class BriefcaseViewHolder extends RecyclerView.ViewHolder {

    public CustomTextView sectionName;
    public CustomTextView title;
    public CustomTextView authorName_Txt;
    public CustomTextView description_Txt;
    public CustomTextView time_Txt;
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
