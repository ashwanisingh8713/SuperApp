package com.ns.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.config.model.WidgetIndex;
import com.netoperation.model.ArticleBean;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.callbacks.WidgetItemClickListener;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.PicassoUtil;
import com.ns.utils.ResUtil;
import com.ns.view.roundedimageview.RoundedImageView;

import java.util.List;

public class BL_WidgetAdapter extends BaseRecyclerViewAdapter {
    private final int BL_VIEW_WIDGET = 1;
    private final int BL_VIEW_OPINION = 4;
    private final int BL_SECTION_OPINION = 26;
    private List<ArticleBean> mWidgetList;
    private int sectionId;
    private String sectionName;

    private WidgetIndex widgetIndex;

    public void setWidgetIndex(WidgetIndex widgetIndex) {
        this.widgetIndex = widgetIndex;
    }

    public WidgetIndex getWidgetIndex() {
        return widgetIndex;
    }

    public BL_WidgetAdapter(List<ArticleBean> mWidgetList, int sectionId, String sectionName) {
        this.mWidgetList = mWidgetList;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
    }

    /*public BL_WidgetAdapter(List<ArticleBean> mWidgetListParam, int sectionId, String sectionName) {
        mWidgetList = mWidgetListParam;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case BL_VIEW_WIDGET:
                View widgetView = inflater.inflate(R.layout.bl_layout_widget_recycler_column, parent, false);
                mViewHolder = new WidgetViewHolder(widgetView);
                break;
            case BL_VIEW_OPINION:
                View opinionView = inflater.inflate(R.layout.bl_layout_widget_opinion, parent, false);
                mViewHolder = new OpinionViewHolder(opinionView);
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case BL_VIEW_WIDGET:
                WidgetViewHolder widgetViewHolder = (WidgetViewHolder) holder;
                fillWidgetData(widgetViewHolder, position);
                break;
            case BL_VIEW_OPINION:
                OpinionViewHolder opinionViewHolder = (OpinionViewHolder) holder;
                fillOpinionData(opinionViewHolder, position);
                break;
        }
    }


    @Override
    public int getItemCount() {
        if (mWidgetList == null) {
            return 0;
        } else if(mWidgetList.size() > getWidgetIndex().getItemCount()) {
            return getWidgetIndex().getItemCount();
        }
        return mWidgetList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (sectionId) {
            case BL_SECTION_OPINION:
                return BL_VIEW_OPINION;
            default:
                return BL_VIEW_WIDGET;
        }
    }


    private void fillWidgetData(final WidgetViewHolder holder, final int position) {
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.widgetParentLayout.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.widgetParentLayout.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
        }
        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {

            String imageUrl = bean.getIm_thumbnail_v2();
            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = bean.getIm_thumbnail();
            }
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                PicassoUtil.loadImageWithFilePH(holder.itemView.getContext(), holder.mWidgetImageView, ContentUtil.getWidgetUrl(imageUrl));
            }
            
            holder.mWidgetTextView.setText(bean.getTi());

            holder.mWidgetLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");*/
                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.mWidgetLayout);

                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }
    }


    private void fillOpinionData(OpinionViewHolder holder, final int position) {
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.widgetParentLayout.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.widgetParentLayout.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
        }
        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {
            holder.mWidgetTextView.setText("OPINION");
            String description = bean.getTi();
            if (description != null) {
                holder.mWidgetDescripitionTextView.setText(ResUtil.htmlText(description));
            } else {
                holder.mWidgetDescripitionTextView.setText("");
            }
            holder.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget Openion", "Widget Openion: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget Openion: " + " Article Clicked");*/
                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.mRootLayout);
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }
    }


    private class WidgetViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mWidgetImageView;
        private TextView mWidgetTextView;
        private LinearLayout mWidgetLayout;
        private CardView widgetParentLayout;

        private WidgetViewHolder(View itemView) {
            super(itemView);
            mWidgetLayout = itemView.findViewById(R.id.layout_widget);
            mWidgetImageView = itemView.findViewById(R.id.imageview_widget_image);
            mWidgetTextView = itemView.findViewById(R.id.textview_widget_text);
            widgetParentLayout = itemView.findViewById(R.id.widgetParentLayout);
        }
    }

    private class OpinionViewHolder extends RecyclerView.ViewHolder {

        private TextView mWidgetTextView;
        private TextView mWidgetDescripitionTextView;
        private LinearLayout mRootLayout;
        private CardView widgetParentLayout;

        private OpinionViewHolder(View itemView) {
            super(itemView);
            widgetParentLayout = itemView.findViewById(R.id.widgetParentLayout);
            mWidgetTextView = itemView.findViewById(R.id.textview_opinion_title);
            mWidgetDescripitionTextView = itemView.findViewById(R.id.textview_opinion_content);
            mRootLayout = itemView.findViewById(R.id.layout_root_opinion);
        }
    }


    private WidgetItemClickListener mWidgetItemClickListener;

    public void setWidgetItemClickListener(WidgetItemClickListener widgetItemClickListener) {
        mWidgetItemClickListener = widgetItemClickListener;
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void updateArticleList(List<ArticleBean> articleBeans) {
        this.mWidgetList = articleBeans;
    }
}
