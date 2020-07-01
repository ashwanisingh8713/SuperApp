package com.ns.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.config.model.WidgetIndex;
import com.netoperation.model.SectionBean;
import com.ns.activity.AppSettingActivity;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.PicassoUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.view.roundedimageview.RoundedImageView;

import java.util.List;


public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {
    private List<SectionBean> mSubSection;
    private WidgetIndex widgetIndex;
    private String sectionId;

    public WidgetIndex getWidgetIndex() {
        return widgetIndex;
    }

    public void setWidgetIndex(WidgetIndex widgetIndex) {
        this.widgetIndex = widgetIndex;
    }

    public ExploreAdapter(List<SectionBean> mExploreListParam, String sectionId) {
        mSubSection = mExploreListParam;
        this.sectionId = sectionId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View exploreView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cardview_explore_column, parent, false);
        return new ViewHolder(exploreView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String imageUrl = mSubSection.get(position).getImage();
        if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
            imageUrl = ContentUtil.getWidgetUrl(imageUrl);
            PicassoUtil.loadImageWithFilePH(holder.itemView.getContext(), holder.mExploreImageView, ContentUtil.getWidgetUrl(imageUrl));
        }

        String mParentSecctionName = mSubSection.get(position).getSecName();

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentUtil.redirectionOnSectionAndSubSection(view.getContext(), mSubSection.get(position).getSecId());
                //Firebase Analytics event
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent( holder.itemView.getContext() ,
                        "Explore",
                        "Clicked",
                        "Explore - " + mParentSecctionName + " - " + mSubSection.get(position).getSecName());


            }
        });
        holder.mExploreTextView.setText(mSubSection.get(position).getSecName());


        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
            holder.mExploreTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
            holder.mExploreTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
        }

        // Item Outer Line Check
        if(widgetIndex.isItemOuterLineRequired()) {
            holder.innerParent.setBackground(ResUtil.getBackgroundDrawable(holder.itemView.getResources(), R.drawable.cartoon_border));
        } else {
            holder.innerParent.setBackground(null);
        }

        // Setting Radius
        holder.cardView.setRadius((int)ResUtil.pxFromDp(holder.itemView.getContext(), widgetIndex.getItemRadius()));
        // Setting Elevation
        holder.cardView.setElevation((int)ResUtil.pxFromDp(holder.itemView.getContext(), widgetIndex.getItemElevation()));

        // Setting Margin
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        List<Integer> itemMargin = widgetIndex.getItemMargin();
        if(itemMargin != null && itemMargin.size() == 4) {
            params.setMargins((int)ResUtil.pxFromDp(holder.itemView.getContext(), itemMargin.get(0)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), itemMargin.get(1)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), itemMargin.get(2)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), itemMargin.get(3)));
        }


    }

    @Override
    public int getItemCount() {
        return mSubSection.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private RoundedImageView mExploreImageView;
        private TextView mExploreTextView;
        private LinearLayout innerParent;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            mExploreImageView = itemView.findViewById(R.id.imageview_explore_section);
            mExploreTextView = itemView.findViewById(R.id.textview_explore_section);
            innerParent = itemView.findViewById(R.id.innerParent);
        }
    }
}
