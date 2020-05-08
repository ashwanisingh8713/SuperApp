package com.ns.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.SectionBean;
import com.ns.alerts.Alerts;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.GlideUtil;

import java.util.List;


public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {
    private List<SectionBean> mSubSection;
    private String sectionId;

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
            GlideUtil.loadImage(holder.itemView.getContext(), holder.mExploreImageView, ContentUtil.getWidgetUrl(imageUrl), R.drawable.ph_exploresections_th);
        } else {
            holder.mExploreImageView.setImageResource(R.drawable.ph_exploresections_th);
        }

        String mParentSecctionName = mSubSection.get(position).getSecName();

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentUtil.redirectionOnSectionAndSubSection(view.getContext(), mSubSection.get(position).getSecId());

                /*SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_EXPLORE, position, true, sectionId, mParentSecctionName);
                ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();

                GoogleAnalyticsTracker
                        .setGoogleAnalyticsEvent(
                                mContext,
                                "Explore",
                                "Clicked",
                                "Explore - " + mParentSecctionName + " - " + mSubSection.get(position).getSecName());
                FlurryAgent.logEvent("Explore - " + mParentSecctionName + " - " + mSubSection.get(position).getSecName());*/
                Alerts.showToast(view.getContext(), "Explore");

            }
        });
        holder.mExploreTextView.setText(mSubSection.get(position).getSecName());

    }

    @Override
    public int getItemCount() {
        return mSubSection.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout parentView;
        private ImageView mExploreImageView;
        private TextView mExploreTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            parentView = itemView.findViewById(R.id.layout_explore_root);
            mExploreImageView = itemView.findViewById(R.id.imageview_explore_section);
            mExploreTextView = itemView.findViewById(R.id.textview_explore_section);
        }
    }
}
