package com.ns.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.config.model.WidgetIndex;
import com.netoperation.model.ArticleBean;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.PicassoUtil;
import com.ns.utils.ResUtil;
import com.ns.viewholder.MultiMediaViewHolder;

import java.util.List;
import java.util.Locale;

public class SuWidgetRecyclerAdapter extends BaseRecyclerViewAdapter {

    private List<ArticleBean> mWidgetList;
    private String sectionName;

    private WidgetIndex widgetIndex;

    public void setWidgetIndex(WidgetIndex widgetIndex) {
        this.widgetIndex = widgetIndex;
    }

    public WidgetIndex getWidgetIndex() {
        return widgetIndex;
    }

    public SuWidgetRecyclerAdapter(List<ArticleBean> widgetList, String sectionName) {
        this.mWidgetList = widgetList;
        this.sectionName = sectionName;
    }

    public void updateArticleList(List<ArticleBean> articleBeans) {
        this.mWidgetList = articleBeans;
        notifyDataSetChanged();
    }

    public String getSectionName() {
        return sectionName;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder mViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA")) {

        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("TEXT")) {

        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA_TEXT")) {

        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("TITLE_TEXT")) {

        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA_TITLE_TIME")) {
            return new MultiMediaViewHolder(inflater.inflate(R.layout.layout_widget_multimedia, parent, false));
        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA_TextOverlay")) {

        }

        return new MultiMediaViewHolder(inflater.inflate(R.layout.layout_widget_multimedia, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MultiMediaViewHolder multiMediaViewHolder = (MultiMediaViewHolder) holder;
        fillMultiMediaData(multiMediaViewHolder, position);
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


    private void fillMultiMediaData(MultiMediaViewHolder holder, final int position) {

        holder.cardView.setRadius((int)ResUtil.pxFromDp(holder.itemView.getContext(), widgetIndex.getItemRadius()));
        holder.cardView.setElevation((int)ResUtil.pxFromDp(holder.itemView.getContext(), widgetIndex.getItemElevation()));

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();

        List<Integer> itemMargin = widgetIndex.getItemMargin();

        if(itemMargin != null && itemMargin.size() == 4) {
            params.setMargins((int)ResUtil.pxFromDp(holder.itemView.getContext(), itemMargin.get(0)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), itemMargin.get(1)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), itemMargin.get(2)),
                    (int)ResUtil.pxFromDp(holder.itemView.getContext(), itemMargin.get(3)));
        }

        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.cardView.setBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.cardView.setBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
        }
        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {
            boolean isDayTheme = DefaultPref.getInstance(holder.itemView.getContext()).isUserThemeDay();
            holder.mWidgetTextView.setText(bean.getTi());
            String publishTime = bean.getGmt();
            String timeDiff = AppDateUtil.getDurationFormattedDate(AppDateUtil.strToMlsForSearchedArticle(publishTime), Locale.ENGLISH);
            holder.mWidgetTime.setText(timeDiff);

            String imageUrl = bean.getIm_thumbnail_v2();
            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = bean.getIm_thumbnail();
            }
            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = "http://";
            }
            PicassoUtil.loadImageWithFilePH(holder.itemView.getContext(), holder.mWidgetImageView, ContentUtil.getMultimediaUrl(imageUrl));
            // Dims Read article given view
            dimReadArticle(holder.mParentView.getContext(), bean.getArticleId(), holder.mParentView);
            articleTypeImage(bean.getArticleType(), bean, holder.mPlayButton);
            holder.mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    */
                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.mParentView);
                    /*if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }*/
                }
            });

            holder.mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    */
                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.mPlayButton);
                    /*if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }*/
                }
            });
        }
    }

}
