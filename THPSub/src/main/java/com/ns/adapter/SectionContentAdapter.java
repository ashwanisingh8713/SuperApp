package com.ns.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.AppDateUtil;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.utils.ContentUtil;
import com.ns.utils.GlideUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.SharingArticleUtil;
import com.ns.utils.WebViewLinkClick;
import com.ns.viewholder.LoadMoreViewHolder;
import com.ns.viewholder.StaticItemWebViewHolder;

import java.util.ArrayList;
import java.util.Locale;


public class SectionContentAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<SectionAdapterItem> adapterItems;
    private String mFrom;
    private boolean mIsSubSection;
    private String mSectionId;
    private String mSectionType;

    public SectionContentAdapter(String from, ArrayList<SectionAdapterItem> adapterItems, boolean isSubsection, String sectionId, String sectionType) {
        this.mFrom = from;
        this.adapterItems = adapterItems;
        this.mIsSubSection = isSubsection;
        this.mSectionId = sectionId;
        this.mSectionType = sectionType;
    }

    @Override
    public int getItemViewType(int position) {
        return adapterItems.get(position).getViewType();
    }


    @Override
    public int getItemCount() {
        return adapterItems.size();
    }

    /*@Override
    public void onViewAttachedToWindow(final RecyclerView.ViewHolder holder) {
        if (holder instanceof StaticItemWebViewHolder) {
            holder.setIsRecyclable(false);
        } else if (holder instanceof WidgetsViewHolder) {
            holder.setIsRecyclable(false);
        }
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
        if (holder instanceof StaticItemWebViewHolder){
            holder.setIsRecyclable(true);
        } else if (holder instanceof WidgetsViewHolder){
            holder.setIsRecyclable(true);
        }
        super.onViewDetachedFromWindow(holder);
    }*/

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == VT_LOADMORE) {
            return new LoadMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loadmore, viewGroup, false));
        }
        else if(viewType == VT_THD_BANNER) {
            return new BannerViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_banner, viewGroup, false));
        }
        else if(viewType == VT_THD_DEFAULT_ROW) {
            return new ArticlesViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_article_list, viewGroup, false));
        }
        else if(viewType == VT_THD_WIDGET_DEFAULT) {
            return new WidgetsViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_widgets, viewGroup, false));
        }
        else if(viewType == VT_WEB_WIDGET) {
            return new StaticItemWebViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_webview, viewGroup, false));
        }
        else if(viewType == VT_THD_SUB_SECTION) {
            return new StaticItemWebViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_explore, viewGroup, false));
        }
        return new LoadMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_loadmore, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SectionAdapterItem item = adapterItems.get(position);
        if(holder instanceof BannerViewHolder) {
            fillBannerData((BannerViewHolder) holder, position);
            /*bannerViewHolder.mArticleSectionName.setText("pos : "+position+"--"+item.getItemRowId());
            ArticleBean bean = item.getArticleBean();
            bannerViewHolder.itemView.setOnClickListener(v->{
                IntentUtil.openNonPremiumDetailActivity(holder.itemView.getContext(), mFrom, bean.getArticleId(), mSectionId, mSectionType, bean.getSectionName(), mIsSubSection);
            });*/

        }
        else if(holder instanceof WidgetsViewHolder) {
            fillWidgetData((WidgetsViewHolder)holder, position);
        }
        else if(holder instanceof ArticlesViewHolder) {
            ArticlesViewHolder articlesViewHolder = (ArticlesViewHolder) holder;
            articlesViewHolder.mArticleSectionName.setText("pos : "+position+"--"+item.getItemRowId());

            ArticleBean bean = item.getArticleBean();

            articlesViewHolder.itemView.setOnClickListener(v->{
                IntentUtil.openNonPremiumDetailActivity(holder.itemView.getContext(), mFrom, bean.getArticleId(), mSectionId, mSectionType, bean.getSectionName(), mIsSubSection);
            });
        }
        else if(holder instanceof StaticItemWebViewHolder) {
            StaticItemWebViewHolder staticItemHolder = (StaticItemWebViewHolder) holder;
            StaticPageUrlBean pageUrlBean = item.getStaticPageUrlBean();
            staticItemHolder.webView.loadUrl(pageUrlBean.getUrl());
            if(!pageUrlBean.getSectionId().equals("0")) {
                staticItemHolder.mDummyView.setVisibility(View.VISIBLE);
                // DummyView Click Listener
                // TODO, redirect on particular section or sub-section
//                staticItemHolder.mDummyView.setOnClickListener(new StaticItemDummyViewClick(url, mContext, null, false, -1));
            } else {
                staticItemHolder.mDummyView.setVisibility(View.GONE);
                // Enabling Weblink click on Lead Text
                new WebViewLinkClick().linkClick(staticItemHolder.webView, staticItemHolder.itemView.getContext());
            }
        }

    }

    /**
     * Makes dim to read article's row
     * @param context
     * @param articleId
     * @param view
     */
    private void dimReadArticle(Context context, String articleId, View view) {
        DefaultTHApiManager.isReadArticleId(context, articleId)
                .subscribe(tableRead -> {
                    Log.i("", "");
                    if (tableRead != null) {
                        view.setAlpha(.4f);
                    } else {
                        view.setAlpha(1f);
                    }
                }, throwable -> {
                    Log.i("", "");
                });
    }

    /**
     * Fill Banner
     * @param holder
     * @param position
     */
    private void fillBannerData(final BannerViewHolder holder, final int position) {
        final ArticleBean bean = adapterItems.get(position).getArticleBean();

        dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.mBannerTextView);

            String imageUrl = null;
            if (bean.getHi().equals("1")) {
                imageUrl = bean.getMe().get(0).getIm_v2();
            }
            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                imageUrl = ContentUtil.getBannerUrl(imageUrl);
                GlideUtil.loadImage(holder.itemView.getContext(), holder.mBannerImageView, imageUrl, R.drawable.ph_topnews_th);
            } else {
                holder.mBannerImageView.setImageResource(R.drawable.ph_topnews_th);
            }

            articleTypeImage(bean.getArticleType(), bean, holder.mMultimediaButton);

            holder.mBannerTextView.setText(bean.getTi());

            String publishTime = bean.getGmt();
            String timeDiff = AppDateUtil.getDurationFormattedDate(AppDateUtil.strToMlsForSearchedArticle(publishTime), Locale.ENGLISH);
            holder.mArticleUpdateTime.setText(timeDiff);
            holder.mArticleSectionName.setText(bean.getSname());

            holder.mBannerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Banner", "Banner: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Banner: " + "Article Clicked");*/

                    IntentUtil.openNonPremiumDetailActivity(holder.itemView.getContext(), mFrom,
                            bean.getArticleId(), mSectionId, mSectionType, bean.getSectionName(), mIsSubSection);

                }
            });

            holder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Banner", "Banner: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Banner: " + "Article Clicked");*/

                }
            });
            holder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Home", "Home: Bookmark button Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Home: " + "Bookmark button Clicked");*/


                }
            });

            holder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingArticleUtil.shareArticle(v.getContext(), bean);
                }
            });

        //Enabling sliding for view pager
        //new UniversalTouchListener(mBannerViewHolder.mBannerLayout, true);

    }


    /**
     * Banner View Holder
     */
    public class BannerViewHolder extends RecyclerView.ViewHolder {

        public ImageView mBannerImageView;
        public TextView mBannerTextView;
        public LinearLayout mBannerLayout;
        public TextView mArticleUpdateTime;
        public TextView mArticleSectionName;
        public Button mBookmarkButton;
        public Button mShareArticleButton;
        public ImageButton mMultimediaButton;

        public BannerViewHolder(View itemView) {
            super(itemView);
            mBookmarkButton = itemView.findViewById(R.id.button_bookmark);
            mBannerLayout = itemView.findViewById(R.id.layout_banner);
            mBannerImageView = itemView.findViewById(R.id.imageview_banner);
            mBannerTextView = itemView.findViewById(R.id.textview_banner);
            mArticleSectionName = itemView.findViewById(R.id.section_name);
            mArticleUpdateTime = itemView.findViewById(R.id.textview_time);
            mShareArticleButton = itemView.findViewById(R.id.button_article_share);
            mMultimediaButton = itemView.findViewById(R.id.multimedia_button);
            mArticleSectionName.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Normal Row Item View Holder
     */
    public class ArticlesViewHolder extends RecyclerView.ViewHolder {

        public ImageView mArticleImageView;
        public TextView mArticleTextView;
        public TextView mArticleTimeTextView;
        public TextView mArticleSectionName;
        public LinearLayout mArticlesLayout;
        public Button mBookmarkButton;
        public Button mShareArticleButton;
        public ImageButton mMultimediaButton;
        public FrameLayout mImageParentLayout;

        public ArticlesViewHolder(View itemView) {
            super(itemView);
            mMultimediaButton = itemView.findViewById(R.id.multimedia_button);
            mImageParentLayout = itemView.findViewById(R.id.imageParentLayout);
            mArticlesLayout = itemView.findViewById(R.id.layout_articles_root);
            mArticleImageView = itemView.findViewById(R.id.imageview_article_list_image);
            mArticleTextView = itemView.findViewById(R.id.textview_article_list_header);
            mShareArticleButton = itemView.findViewById(R.id.button_article_share);
            mArticleTimeTextView = itemView.findViewById(R.id.textview_time);
            mBookmarkButton = itemView.findViewById(R.id.button_bookmark);
            mArticleSectionName = itemView.findViewById(R.id.section_name);
            mArticleSectionName.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Default Widget View Holder
     */
    public class WidgetsViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView mWidgetsRecyclerView;
        public TextView mWidgetTitleTextView, mWidgetFooterTextView;

        public WidgetsViewHolder(View itemView) {
            super(itemView);

            mWidgetsRecyclerView = itemView.findViewById(R.id.recyclerview_widgets);
            mWidgetTitleTextView = itemView.findViewById(R.id.textview_widget_title);
            mWidgetFooterTextView = itemView.findViewById(R.id.textview_widget_viewAll);

        }
    }

    public void deleteItem(SectionAdapterItem item) {
        adapterItems.remove(item);
    }

    public void addSingleItem(SectionAdapterItem item) {
        if(adapterItems == null) {
            adapterItems = new ArrayList<>();
        }
        adapterItems.add(item);
        notifyItemChanged(adapterItems.size()-1);
    }

    public void addMultiItems(ArrayList<SectionAdapterItem> items) {
        if(adapterItems == null) {
            adapterItems = new ArrayList<>();
        }
        int fromIndex = adapterItems.size();
        adapterItems.addAll(items);
        notifyItemRangeChanged(fromIndex, items.size());
    }

    public void insertItem(SectionAdapterItem item, int index) {
        if(index >= adapterItems.size()) {
            adapterItems.add(item);
        } else if(index < adapterItems.size()){
            adapterItems.add(index, item);
        }
    }

    public int indexOf(SectionAdapterItem item) {
        return adapterItems.indexOf(item);
    }

    public SectionAdapterItem getItem(int index) {
        return adapterItems.get(index);
    }

    public void updateItem(SectionAdapterItem item) {
        int index = adapterItems.indexOf(item);
        if(index != -1) {
            final SectionAdapterItem oldItem = adapterItems.get(index);
            oldItem.setADID_300X250(item.getADID_300X250());
            oldItem.setArticleBean(item.getArticleBean());
            oldItem.setItemRowId(item.getItemRowId());
            oldItem.setStaticPageUrlBean(item.getStaticPageUrlBean());
            oldItem.setExploreAdapter(item.getExploreAdapter());
            oldItem.setWidgetAdapter(item.getWidgetAdapter());
        }

    }

    public void deleteAllItems() {
        if(adapterItems != null) {
            adapterItems.clear();
            adapterItems = new ArrayList<>();
        }
    }


    ////////////////////////////////////
    private void fillWidgetData(WidgetsViewHolder mWidgetsViewHolder, final int position) {

        final SectionAdapterItem dataBean = adapterItems.get(position);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mWidgetsViewHolder.mWidgetsRecyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);

        /*mWidgetsViewHolder.mWidgetsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        if (SlidingSectionFragment.mViewPager != null) {
                            SlidingSectionFragment.mViewPager.setPagingEnabled(true);
                        }
                        break;

                }
                return false;
            }
        });*/

        mWidgetsViewHolder.mWidgetsRecyclerView.setAdapter(dataBean.getWidgetAdapter());
        mWidgetsViewHolder.mWidgetsRecyclerView.setLayoutManager(mLayoutManager);
//        mWidgetsViewHolder.mWidgetsRecyclerView.setHasFixedSize(true);
        mWidgetsViewHolder.mWidgetTitleTextView.setText(dataBean.getWidgetAdapter().getSectionName());
        //for top-picks we are desableing the visiblity the view all textview
        if (dataBean.getWidgetAdapter().getSectionId()== 88) {
            mWidgetsViewHolder.mWidgetFooterTextView.setVisibility(View.GONE);
        } else {
            mWidgetsViewHolder.mWidgetFooterTextView.setVisibility(View.VISIBLE);
            mWidgetsViewHolder.mWidgetFooterTextView.setText("View All " + dataBean.getWidgetAdapter().getSectionName());
        }

        /*mWidgetsViewHolder.mWidgetFooterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int parentId = dataBean.getWidgetParentSecId();
                int position;
                boolean isSubsection;
                String parentName;
                if (parentId == 0) {
                    position = DatabaseJanitor.getSectionPosition(dataBean.getWidgetSectionId());
                    parentName = dataBean.getWidgetName();
                    isSubsection = false;
                } else {
                    position = DatabaseJanitor.getSubsectionPostion(parentId, dataBean.getWidgetSectionId());
                    parentName = DatabaseJanitor.getParentSectionName(parentId);
                    isSubsection = true;
                }
                if (position >= 0) {
                    SlidingSectionFragment fragment = SlidingSectionFragment.newInstance(SlidingSectionFragment.FROM_VIEW_ALL, position, isSubsection, parentId, parentName);
                    pushFragmentToBackStack(fragment);

                } else if (position == -1) {
                    SlidingArticleFragment fragment = SlidingArticleFragment.newInstance(0, String.valueOf(dataBean.getWidgetSectionId()),
                            false);
                    pushFragmentToBackStack(fragment);
                }
            }
        });*/

    }

}
