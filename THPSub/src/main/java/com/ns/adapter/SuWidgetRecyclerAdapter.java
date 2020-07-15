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
import com.netoperation.model.ArticleSection;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.callbacks.WidgetItemClickListener;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.PicassoUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.SharingArticleUtil;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.viewholder.W_Item_MediaTitleTime_VH;
import com.ns.viewholder.W_Item_Media_Text;
import com.ns.viewholder.W_Item_Media_VH;
import com.ns.viewholder.W_Item_Text_VH;
import com.ns.viewholder.W_Item_Title_Text_VH;

import java.util.List;
import java.util.Locale;

public class SuWidgetRecyclerAdapter extends BaseRecyclerViewAdapter {

    private List<ArticleBean> mWidgetList;
    private String sectionName;

    private WidgetIndex widgetIndex;

    private WidgetItemClickListener mWidgetItemClickListener;

    public void setWidgetItemClickListener(WidgetItemClickListener widgetItemClickListener) {
        mWidgetItemClickListener = widgetItemClickListener;
    }

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

        if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA_INNER_PADDING")) {
            return new W_Item_Media_VH(inflater.inflate(R.layout.widget_item_media_inner_padding, parent, false));
        }

        if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA_FULL_SCR")) {
            return new W_Item_Media_VH(inflater.inflate(R.layout.widget_item_media_full_scr, parent, false));
        }

        else if(widgetIndex.getWidgetType().equalsIgnoreCase("TEXT")) {
            return new W_Item_Text_VH(inflater.inflate(R.layout.widget_item_text, parent, false));
        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA_TEXT")) {
            return new W_Item_Media_Text(inflater.inflate(R.layout.widget_item_media_text, parent, false));
        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("TITLE_TEXT")) {
            return new W_Item_Title_Text_VH(inflater.inflate(R.layout.widget_item_title_text, parent, false));
        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA_TEXT_TIME")) {
            return new W_Item_MediaTitleTime_VH(inflater.inflate(R.layout.widget_item_media_title_time, parent, false));
        }
        else if(widgetIndex.getWidgetType().equalsIgnoreCase("MEDIA_TextOverlay")) {

        }

        return new W_Item_MediaTitleTime_VH(inflater.inflate(R.layout.widget_item_media_title_time, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof W_Item_MediaTitleTime_VH) {
            W_Item_MediaTitleTime_VH WItemMediaTitleTimeVH = (W_Item_MediaTitleTime_VH) holder;
            mediaTitleTime(WItemMediaTitleTimeVH, position);
        }
        else if(holder instanceof W_Item_Media_VH) {
            W_Item_Media_VH WItemMediaVH = (W_Item_Media_VH) holder;
            mediaData(WItemMediaVH, position);
        }
        else if(holder instanceof W_Item_Text_VH) {
            W_Item_Text_VH WItemMediaVH = (W_Item_Text_VH) holder;
            fillAppExclusiveData(WItemMediaVH, position);
        }
        else if(holder instanceof W_Item_Title_Text_VH) {
            W_Item_Title_Text_VH titleTextVh = (W_Item_Title_Text_VH) holder;
            fillTitleText(titleTextVh, position);
        }
        else if(holder instanceof W_Item_Media_Text) {
            W_Item_Media_Text mediaTitleVh = (W_Item_Media_Text) holder;
            fillMedia_Text(mediaTitleVh, position);
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

    private void fillMedia_Text(final W_Item_Media_Text holder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
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

        if (bean != null) {

            String imageUrl = bean.getIm_thumbnail_v2();
            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = bean.getIm_thumbnail();
            }
            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = "http://";
            }
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                PicassoUtil.loadImageWithFilePH(holder.itemView.getContext(), holder.mWidgetImageView, ContentUtil.getWidgetUrl(imageUrl));
            }

            holder.mWidgetTextView.setText(bean.getTi());

            // Dims Read article given view
            dimReadArticle(holder.cardView.getContext(), bean.getArticleId(), holder.cardView);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    */

                    //Firebase event
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", "Widget: Article Clicked", "Home Fragment");

                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.cardView);

                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }
    }

    private void fillTitleText(W_Item_Title_Text_VH holder, final int position) {
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
            holder.mWidgetDescripitionTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
            holder.mWidgetDescripitionTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
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


        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {
            List<ArticleSection> mSections = bean.getSections();
            if (mSections != null && mSections.size() > 0) {
                holder.mWidgetTextView.setText(mSections.get(0).getSection_name());
            } else {
                holder.mWidgetTextView.setText("Opinion");
            }
            String description = bean.getTi();
            if (description != null) {
                holder.mWidgetDescripitionTextView.setText(ResUtil.htmlText(description));
            } else {
                holder.mWidgetDescripitionTextView.setText("");
            }

            // Dims Read article given view
            dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.cardView);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget Openion", "Widget Openion: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget Openion: " + " Article Clicked");
                    */

                    //Firebase event
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", "Widget Openion: Article Clicked", "Home Fragment");

                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.cardView);
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }
    }

    private void mediaTitleTime(W_Item_MediaTitleTime_VH holder, final int position) {

        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
            holder.mWidgetTime.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
            holder.mWidgetTime.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
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


        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {
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

            articleTypeImage(bean.getArticleType(), bean, holder.mPlayButton);
            holder.mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    */

                    //Firebase event
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", "Widget : Article Clicked", "Home Fragment");

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

                    //Firebase event
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", "Widget: Article Clicked", "Home Fragment");

                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.mPlayButton);
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }

        // Dims Read article given view
        dimReadArticle(holder.mParentView.getContext(), bean.getArticleId(), holder.innerParent);
    }

    private void mediaData(W_Item_Media_VH holder, final int position) {

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

        // Item Outer Line Check
        if(widgetIndex.isItemOuterLineRequired()) {
            holder.innerParent.setBackground(ResUtil.getBackgroundDrawable(holder.itemView.getResources(), R.drawable.cartoon_border));
        } else {
            holder.innerParent.setBackground(null);
        }

        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
        }
        else {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
        }

        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {
            if (bean.getHi().equals("1")) {
                String imageUrl = bean.getMe().get(0).getIm_v2();
                if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                    imageUrl = bean.getMe().get(0).getIm();
                }
                if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                    PicassoUtil.loadImage(holder.itemView.getContext(), holder.mWidgetImageView, ContentUtil.getCartoonUrl(imageUrl));
                }
            }

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget Cartoon", "Widget Cartoon: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget Cartoon: " + " Article Clicked");
                    */

                    //Firebase event
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", "Widget Cartoon: Article Clicked", "Home Fragment");

                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.cardView);
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }

        // Dims Read article given view
        dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.innerParent);
    }

    private void fillAppExclusiveData(W_Item_Text_VH holder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getLight()));
            holder.mTitleTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.cardView.setCardBackgroundColor(Color.parseColor(widgetIndex.getItemBackground().getDark()));
            holder.mTitleTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
        }

        // Item Our Line Check
        if(widgetIndex.isItemOuterLineRequired()) {
            holder.innerParent.setBackground(ResUtil.getBackgroundDrawable(holder.itemView.getResources(), R.drawable.cartoon_border));
        } else {
            holder.innerParent.setBackground(null);
        }

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
        if (bean != null) {
            holder.mTitleTextView.setText(ResUtil.htmlText("<i>" + "\"" + bean.getTi() + "\"" + "</i>"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
//                    FlurryAgent.logEvent("Widget: " + " Article Clicked");

                    //Firebase event
                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", "Widget: Article Clicked", "Home Fragment");

                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.cardView);
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });

            // Dims Read article given view
            dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.innerParent);
        }
    }

}
