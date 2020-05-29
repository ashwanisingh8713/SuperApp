package com.ns.adapter;

import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.config.model.WidgetIndex;
import com.netoperation.model.ArticleBean;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseAcitivityTHP;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.callbacks.WidgetItemClickListener;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.PicassoUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.view.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Locale;


public class TH_WidgetAdapter extends BaseRecyclerViewAdapter {

    private static final String TAG = "WidgetAdapter";
    private final int VIEW_WIDGET = 1;
    private final int VIEW_CARTOON = 2;
    private final int VIEW_MULTIMEDIA = 3;
    private final int VIEW_OPINION = 4;
    private final int VIEW_APPEXCLUSIVE = 5;
    private final int SECTION_OPINION = 5;
    private final int SECTION_MULTIMEDIA = 138;
    private final int SECTION_CARTOON = 69;
    private final int SECTION_APP_EXCLUSIVE = THPConstants.APP_EXCLUSIVE_SECTION_ID;
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

    public TH_WidgetAdapter(List<ArticleBean> mWidgetListParam, int sectionId, String sectionName) {
        mWidgetList = mWidgetListParam;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_WIDGET:
                View widgetView = inflater.inflate(R.layout.layout_widget_recycler_column, parent, false);
                mViewHolder = new WidgetViewHolder(widgetView);
                break;
            case VIEW_OPINION:
                View opinionView = inflater.inflate(R.layout.layout_widget_opinion, parent, false);
                mViewHolder = new OpinionViewHolder(opinionView);
                break;
            case VIEW_MULTIMEDIA:
                View multiMedia = inflater.inflate(R.layout.layout_widget_multimedia, parent, false);
                mViewHolder = new MultiMediaViewHolder(multiMedia);
                break;
            case VIEW_CARTOON:
                View cartoonView = inflater.inflate(R.layout.layout_widget_cartoon, parent, false);
                mViewHolder = new CartoonViewHolder(cartoonView);
                break;
            case VIEW_APPEXCLUSIVE:
                View appExclusive = inflater.inflate(R.layout.layout_widget_app_exclusive, parent, false);
                mViewHolder = new AppExclusiveViewHolder(appExclusive);
                break;
        }
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (holder.getItemViewType()) {
            case VIEW_WIDGET:
                WidgetViewHolder widgetViewHolder = (WidgetViewHolder) holder;
                fillWidgetData(widgetViewHolder, position);
                break;
            case VIEW_CARTOON:
                CartoonViewHolder cartoonViewHolder = (CartoonViewHolder) holder;
                fillCartoonData(cartoonViewHolder, position);
                break;
            case VIEW_MULTIMEDIA:
                MultiMediaViewHolder multiMediaViewHolder = (MultiMediaViewHolder) holder;
                fillMultiMediaData(multiMediaViewHolder, position);
                break;
            case VIEW_OPINION:
                OpinionViewHolder opinionViewHolder = (OpinionViewHolder) holder;
                fillOpinionData(opinionViewHolder, position);
                break;
            case VIEW_APPEXCLUSIVE:
                AppExclusiveViewHolder appExclusiveViewHolder = (AppExclusiveViewHolder) holder;
                fillAppExclusiveData(appExclusiveViewHolder, position);
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
            case SECTION_APP_EXCLUSIVE:
                return VIEW_APPEXCLUSIVE;
            case SECTION_CARTOON:
                return VIEW_CARTOON;
            case SECTION_MULTIMEDIA:
                return VIEW_MULTIMEDIA;
            case SECTION_OPINION:
                return VIEW_OPINION;
            default:
                return VIEW_WIDGET;
        }
    }

    private void fillAppExclusiveData(AppExclusiveViewHolder holder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getLight()));
            holder.mTitleTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getDark()));
            holder.mTitleTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
        }
        if (bean != null) {
            holder.mTitleTextView.setText(ResUtil.htmlText("<i>" + "\"" + bean.getTi() + "\"" + "</i>"));
            holder.widgetParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
//                    FlurryAgent.logEvent("Widget: " + " Article Clicked");

                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.widgetParentLayout);
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });

            // Dims Read article given view
            dimReadArticle(holder.widgetParentLayout.getContext(), bean.getArticleId(), holder.widgetParentLayout);
        }
    }

    private void fillWidgetData(final WidgetViewHolder holder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getDark()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
        }
        if (bean != null) {

            String imageUrl = bean.getIm_thumbnail_v2();
            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = bean.getIm_thumbnail();
            }
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                PicassoUtil.loadImageWithFilePH(holder.itemView.getContext(), holder.mWidgetImageView, ContentUtil.getWidgetUrl(imageUrl));
            }

            holder.mWidgetTextView.setText(bean.getTi());

            // Dims Read article given view
            dimReadArticle(holder.widgetParentLayout.getContext(), bean.getArticleId(), holder.widgetParentLayout);
            holder.widgetParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    */
                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.widgetParentLayout);

                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }
    }

    private void fillCartoonData(CartoonViewHolder holder, final int position) {

        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getLight()));
        }
        else {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getDark()));
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

            // Dims Read article given view
            dimReadArticle(holder.widgetParentLayout.getContext(), bean.getArticleId(), holder.widgetParentLayout);

            holder.widgetParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget Cartoon", "Widget Cartoon: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget Cartoon: " + " Article Clicked");
                    */
                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.widgetParentLayout);
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }
    }

    private void fillOpinionData(OpinionViewHolder holder, final int position) {
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
            holder.mWidgetDescripitionTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getDark()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
            holder.mWidgetDescripitionTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getDark()));
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

            // Dims Read article given view
            dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.widgetParentLayout);

            holder.widgetParentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget Openion", "Widget Openion: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget Openion: " + " Article Clicked");
                    */
                    IntentUtil.openSectionOrSubSectionDetailActivity(view.getContext(), bean.getSid(),
                            bean.getArticleId(), NetConstants.G_DEFAULT_SECTIONS, holder.widgetParentLayout);
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }
    }

    private void fillMultiMediaData(MultiMediaViewHolder holder, final int position) {
        if(BaseAcitivityTHP.sIsDayTheme) {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getLight()));
            holder.mWidgetTextView.setTextColor(Color.parseColor(widgetIndex.getDescription().getLight()));
        }
        else {
            holder.widgetParentLayout.setBackgroundColor(Color.parseColor(widgetIndex.getBackground().getDark()));
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
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                PicassoUtil.loadImageWithFilePH(holder.itemView.getContext(), holder.mWidgetImageView, ContentUtil.getMultimediaUrl(imageUrl));
            }
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
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
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
                    if(mWidgetItemClickListener != null) {
                        mWidgetItemClickListener.onWidgetItemClickListener(position, bean.getSid());
                    }
                }
            });
        }
    }

    public String getSectionName() {
        return sectionName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public List<ArticleBean> getArticleList() {
        return mWidgetList;
    }

    public void updateArticleList(List<ArticleBean> articleBeans) {
        this.mWidgetList = articleBeans;
    }

    public class WidgetViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView mWidgetImageView;
        private TextView mWidgetTextView;
        private LinearLayout widgetParentLayout;

        public WidgetViewHolder(View itemView) {
            super(itemView);
            widgetParentLayout = itemView.findViewById(R.id.widgetParentLayout);
            mWidgetImageView = itemView.findViewById(R.id.imageview_widget_image);
            mWidgetTextView = itemView.findViewById(R.id.textview_widget_text);
        }
    }

    public class CartoonViewHolder extends RecyclerView.ViewHolder {

        private ImageView mWidgetImageView;
        private LinearLayout widgetParentLayout;

        public CartoonViewHolder(View itemView) {
            super(itemView);
            mWidgetImageView = itemView.findViewById(R.id.imageview_widget_cartoon);
            widgetParentLayout = itemView.findViewById(R.id.widgetParentLayout);

        }
    }

    public class AppExclusiveViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout widgetParentLayout;
        private TextView mTitleTextView;

        public AppExclusiveViewHolder(View itemView) {
            super(itemView);
            widgetParentLayout = itemView.findViewById(R.id.widgetParentLayout);
            mTitleTextView = itemView.findViewById(R.id.title);
        }
    }

    public class MultiMediaViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView mWidgetImageView;
        private TextView mWidgetTextView;
        private TextView mWidgetTime;
        private ImageView mPlayButton;
        private View mParentView;
        private LinearLayout widgetParentLayout;

        public MultiMediaViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            widgetParentLayout = itemView.findViewById(R.id.widgetParentLayout);
            mWidgetImageView = itemView.findViewById(R.id.imageview_multimedia_thumbnail);
            mWidgetTextView = itemView.findViewById(R.id.textview_multimedia_title);
            mWidgetTime = itemView.findViewById(R.id.textview_multimedia_time);
            mPlayButton = itemView.findViewById(R.id.button_multimedia_play);
        }
    }

    public class OpinionViewHolder extends RecyclerView.ViewHolder {

        private TextView mWidgetTextView, mWidgetDescripitionTextView;
        private LinearLayout widgetParentLayout;

        public OpinionViewHolder(View itemView) {
            super(itemView);

            mWidgetTextView = itemView.findViewById(R.id.textview_opinion_title);
            mWidgetDescripitionTextView = itemView.findViewById(R.id.textview_opinion_content);
            widgetParentLayout = itemView.findViewById(R.id.widgetParentLayout);
        }
    }

    private WidgetItemClickListener mWidgetItemClickListener;

    public void setWidgetItemClickListener(WidgetItemClickListener widgetItemClickListener) {
        mWidgetItemClickListener = widgetItemClickListener;
    }


}
