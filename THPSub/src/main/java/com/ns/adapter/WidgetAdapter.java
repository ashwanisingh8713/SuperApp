package com.ns.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.ArticleBean;
import com.netoperation.util.AppDateUtil;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.alerts.Alerts;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.GlideUtil;
import com.ns.utils.THPConstants;

import java.util.ArrayList;
import java.util.Locale;


public class WidgetAdapter extends BaseRecyclerViewAdapter {

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
    private Context mContext;
    private ArrayList<ArticleBean> mWidgetList;
    private int sectionId;

    public WidgetAdapter(Context ctxParam, ArrayList<ArticleBean> mWidgetListParam, int sectionId) {
        mContext = ctxParam;
        mWidgetList = mWidgetListParam;
        this.sectionId = sectionId;
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
        if (mWidgetList.size() > 5) {
            return 5;
        } else {
            return mWidgetList.size();
        }
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

    private void fillAppExclusiveData(AppExclusiveViewHolder appExclusiveViewHolder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {
            appExclusiveViewHolder.mTitleTextView.setText(Html.fromHtml("<i>" + "\"" + bean.getTi() + "\"" + "</i>"));
            appExclusiveViewHolder.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
//                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
//                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
//                            position, bean.getSid(), false);
//                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();
                    Alerts.showToast(mContext, "fillAppExclusiveData");
                }
            });

            if (bean.isRead()) {
                appExclusiveViewHolder.mRootLayout.setAlpha(.8f);
            } else {
                appExclusiveViewHolder.mRootLayout.setAlpha(1f);
            }
        }
    }

    private void fillWidgetData(final WidgetViewHolder mWidgetViewHolder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {

            String imageUrl = bean.getIm_thumbnail_v2();
            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = bean.getIm_thumbnail();
            }
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                GlideUtil.loadImage(mWidgetViewHolder.itemView.getContext(), mWidgetViewHolder.mWidgetImageView, ContentUtil.getWidgetUrl(imageUrl), R.drawable.ph_toppicks_th);
            } else {
                mWidgetViewHolder.mWidgetImageView.setImageResource(R.drawable.ph_toppicks_th);
            }
            mWidgetViewHolder.mWidgetTextView.setText(bean.getTi());


            if (bean.isRead()) {
                mWidgetViewHolder.mWidgetLayout.setAlpha(.8f);
            } else {
                mWidgetViewHolder.mWidgetLayout.setAlpha(1f);
            }

            mWidgetViewHolder.mWidgetLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            position, bean.getSid(), false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();*/
                    Alerts.showToast(mContext, "fillWidgetData");
                }
            });
        }
    }

    private void fillCartoonData(CartoonViewHolder mCartoonViewHolder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {
            if (bean.getHi().equals("1")) {
                String imageUrl = bean.getMe().get(0).getIm_v2();
                if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                    imageUrl = bean.getMe().get(0).getIm();
                }
                if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                    GlideUtil.loadImage(mCartoonViewHolder.itemView.getContext(), mCartoonViewHolder.mWidgetImageView, ContentUtil.getCartoonUrl(imageUrl), R.drawable.ph_topnews_th);
                } else {
                    mCartoonViewHolder.mWidgetImageView.setImageResource(R.drawable.ph_topnews_th);
                }
            }

            if (bean.isRead()) {
                mCartoonViewHolder.mRootLayout.setAlpha(.8f);
            } else {
                mCartoonViewHolder.mRootLayout.setAlpha(1f);
            }

            mCartoonViewHolder.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget Cartoon", "Widget Cartoon: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget Cartoon: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            position, bean.getSid(), false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();*/
                    Alerts.showToast(mContext, "fillCartoonData");
                }
            });
        }
    }

    private void fillOpinionData(OpinionViewHolder mOpinionViewHolder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);
        if (bean != null) {
            mOpinionViewHolder.mWidgetTextView.setText("OPINION");
            String description = bean.getTi();
            if (description != null) {
                mOpinionViewHolder.mWidgetDescripitionTextView.setText(
                        Html.fromHtml(description)
                );
            } else {
                mOpinionViewHolder.mWidgetDescripitionTextView.setText("");
            }

            if (bean.isRead()) {
                mOpinionViewHolder.mRootLayout.setAlpha(.8f);
            } else {
                mOpinionViewHolder.mRootLayout.setAlpha(1f);
            }

            mOpinionViewHolder.mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget Openion", "Widget Openion: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget Openion: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            position, bean.getSid(), false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();*/
                    Alerts.showToast(mContext, "fillOpinionData");
                }
            });
        }
    }

    private void fillMultiMediaData(MultiMediaViewHolder mMultiMediaViewHolder, final int position) {
        final ArticleBean bean = mWidgetList.get(position);

        if (bean != null) {
            mMultiMediaViewHolder.mWidgetTitleTextView.setText(bean.getTi());
            String publishTime = bean.getGmt();
            String timeDiff = AppDateUtil.getDurationFormattedDate(AppDateUtil.strToMlsForSearchedArticle(publishTime), Locale.ENGLISH);
            mMultiMediaViewHolder.mWidgetTime.setText(timeDiff);

            String imageUrl = bean.getIm_thumbnail_v2();
            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = bean.getIm_thumbnail();
            }

            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                GlideUtil.loadImage(mMultiMediaViewHolder.itemView.getContext(), mMultiMediaViewHolder.mWidgetImageView, ContentUtil.getMultimediaUrl(imageUrl), R.drawable.ph_exploresections_th);
            } else {
                mMultiMediaViewHolder.mWidgetImageView.setImageResource(R.drawable.ph_exploresections_th);
            }


            if (bean.isRead()) {
                mMultiMediaViewHolder.mParentView.setAlpha(.8f);
            } else {
                mMultiMediaViewHolder.mParentView.setAlpha(1f);
            }

            articleTypeImage(bean.getArticleType(), bean, mMultiMediaViewHolder.mPlayButton);


            mMultiMediaViewHolder.mParentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            position, bean.getSid(), false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();*/
                    Alerts.showToast(mContext, "fillMultiMediaData 1");
                }
            });

            mMultiMediaViewHolder.mPlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mContext, "Widget", "Widget: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Widget: " + " Article Clicked");
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(
                            position, bean.getSid(), false);
                    ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.FRAME_CONTENT, fragment).addToBackStack(null).commit();*/
                    Alerts.showToast(mContext, "fillMultiMediaData 2");
                }
            });
        }
    }

    public ArrayList<ArticleBean> getArticleList() {
        return mWidgetList;
    }

    public class WidgetViewHolder extends RecyclerView.ViewHolder {
        private ImageView mWidgetImageView;
        private TextView mWidgetTextView;
        private LinearLayout mWidgetLayout;

        public WidgetViewHolder(View itemView) {
            super(itemView);
            mWidgetLayout = itemView.findViewById(R.id.layout_widget);
            mWidgetImageView = itemView.findViewById(R.id.imageview_widget_image);
            mWidgetTextView = itemView.findViewById(R.id.textview_widget_text);
        }
    }

    public class CartoonViewHolder extends RecyclerView.ViewHolder {

        private ImageView mWidgetImageView;
        private LinearLayout mRootLayout;

        public CartoonViewHolder(View itemView) {
            super(itemView);
            mWidgetImageView = itemView.findViewById(R.id.imageview_widget_cartoon);
            mRootLayout = itemView.findViewById(R.id.layout_root_cartoon);

        }
    }

    public class AppExclusiveViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mRootLayout;
        private TextView mTitleTextView;

        public AppExclusiveViewHolder(View itemView) {
            super(itemView);
            mRootLayout = itemView.findViewById(R.id.layout_root_opinion);
            mTitleTextView = itemView.findViewById(R.id.title);
        }
    }

    public class MultiMediaViewHolder extends RecyclerView.ViewHolder {

        private ImageView mWidgetImageView;
        private TextView mWidgetTitleTextView;
        private TextView mWidgetTime;
        private ImageView mPlayButton;
        private View mParentView;



        public MultiMediaViewHolder(View itemView) {
            super(itemView);
            mParentView = itemView;
            mWidgetImageView = itemView.findViewById(R.id.imageview_multimedia_thumbnail);
            mWidgetTitleTextView = itemView.findViewById(R.id.textview_multimedia_title);
            mWidgetTime = itemView.findViewById(R.id.textview_multimedia_time);
            mPlayButton = itemView.findViewById(R.id.button_multimedia_play);
        }
    }

    public class OpinionViewHolder extends RecyclerView.ViewHolder {

        private TextView mWidgetTextView, mWidgetDescripitionTextView;
        private LinearLayout mRootLayout;

        public OpinionViewHolder(View itemView) {
            super(itemView);

            mWidgetTextView = itemView.findViewById(R.id.textview_opinion_title);
            mWidgetDescripitionTextView = itemView.findViewById(R.id.textview_opinion_content);
            mRootLayout = itemView.findViewById(R.id.layout_root_opinion);
        }
    }
}
