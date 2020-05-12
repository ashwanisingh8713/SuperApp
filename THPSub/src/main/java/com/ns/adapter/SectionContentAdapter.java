package com.ns.adapter;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.netoperation.model.AdData;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionAdapterItem;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.DefaultPref;
import com.netoperation.util.NetConstants;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.ContentUtil;
import com.ns.utils.FragmentUtil;
import com.ns.utils.GlideUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.ResUtil;
import com.ns.utils.SharingArticleUtil;
import com.ns.utils.WebViewLinkClick;
import com.ns.view.IconImgView;
import com.ns.viewholder.ArticlesViewHolder;
import com.ns.viewholder.InlineAdViewHolder;
import com.ns.viewholder.LoadMoreViewHolder;
import com.ns.viewholder.SearchRecyclerHolder;
import com.ns.viewholder.StaticItemWebViewHolder;
import com.ns.viewholder.TaboolaNativeAdViewHolder;
import com.taboola.android.api.TBImageView;
import com.taboola.android.api.TBRecommendationItem;
import com.taboola.android.api.TBTextView;
import com.taboola.android.api.TaboolaApi;

import java.util.ArrayList;
import java.util.Locale;


public class SectionContentAdapter extends BaseRecyclerViewAdapter {

    private ArrayList<SectionAdapterItem> adapterItems;
    private String mPageSource;
    private boolean mIsSubSection;
    private String mSectionId;
    private String mSectionType;

    public SectionContentAdapter(String pageSource, ArrayList<SectionAdapterItem> adapterItems, boolean isSubsection, String sectionId, String sectionType) {
        this.mPageSource = pageSource;
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
        else if(viewType == VT_THD_SEARCH_ROW) {
            return new SearchRecyclerHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.search_recycler_item, viewGroup, false));
        }
        else if(viewType == VT_THD_WIDGET_DEFAULT) {
            return new WidgetsViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_widgets, viewGroup, false));
        }
        else if(viewType == VT_WEB_WIDGET) {
            return new StaticItemWebViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_webview, viewGroup, false));
        }
        else if(viewType == VT_THD_HORIZONTAL_LIST) {
            return new ExploreViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_home_explore, viewGroup, false));
        }
        else if(viewType == VT_THD_300X250_ADS) {
            return new InlineAdViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.inline_ads_container, viewGroup, false));
        }
        else if(viewType == VT_TABOOLA_LISTING_ADS) {
            return new TaboolaNativeAdViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.cardview_taboola_native_ad_item, viewGroup, false));
        }
        return new LoadMoreViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_loadmore, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SectionAdapterItem item = adapterItems.get(position);
        if(holder instanceof BannerViewHolder) {
            fillBannerData((BannerViewHolder) holder, position);
        }
        else if(holder instanceof WidgetsViewHolder) {
            fillWidgetData((WidgetsViewHolder)holder, position);
        }
        else if(holder instanceof ArticlesViewHolder) {
            fillArticleData(holder, position);
        }
        else if(holder instanceof SearchRecyclerHolder) {
            fillSearchedArticleData(holder, position);
        }
        else if(holder instanceof StaticItemWebViewHolder) {
            fillStaticWebview(holder, item);
        }
        else if(holder instanceof InlineAdViewHolder) {
            fillInlineAdView(holder, item);
        }
        else if(holder instanceof TaboolaNativeAdViewHolder) {
            fillTaboolaAds(holder, item);
        }
        else if(holder instanceof ExploreViewHolder) {
            fillExploreData(holder, item);
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof TaboolaNativeAdViewHolder) {
            ((TaboolaNativeAdViewHolder) holder).mAdContainer.removeAllViews();
            ((TaboolaNativeAdViewHolder) holder).thumbNailContainer.removeAllViews();
        }
    }

    private void fillExploreData(final RecyclerView.ViewHolder holder, SectionAdapterItem item) {
        ExploreViewHolder exploreHolder = (ExploreViewHolder) holder;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(exploreHolder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        exploreHolder.mExploreRecyclerView.setLayoutManager(mLayoutManager);
        exploreHolder.mExploreRecyclerView.setHasFixedSize(true);
        exploreHolder.mExploreRecyclerView.setAdapter(item.getExploreAdapter());

    }

    private void fillTaboolaAds(final RecyclerView.ViewHolder holder, SectionAdapterItem item) {
        TBRecommendationItem tbRecommendationItem = item.getAdData().getTaboolaNativeAdItem();
        TaboolaNativeAdViewHolder taboolaNativeAdViewHolder = (TaboolaNativeAdViewHolder) holder;
        taboolaNativeAdViewHolder.mAttributionView.setOnClickListener(view -> TaboolaApi.getInstance().handleAttributionClick(holder.itemView.getContext()));
        TBImageView thumbnailView = tbRecommendationItem.getThumbnailView(taboolaNativeAdViewHolder.itemView.getContext());
        thumbnailView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ViewGroup parent = (ViewGroup) thumbnailView.getParent();
        if (parent != null) {
            parent.removeView(thumbnailView);
        }

        TBTextView tbTextView = tbRecommendationItem.getTitleView(taboolaNativeAdViewHolder.itemView.getContext());

        ViewGroup parent2 = (ViewGroup) tbTextView.getParent();
        if (parent2 != null) {
            parent2.removeView(tbTextView);
        }

        boolean isUserThemeDay = DefaultPref.getInstance(holder.itemView.getContext()).isUserThemeDay();
        if(isUserThemeDay) {
            tbTextView.setTextColor(ResUtil.getColor(holder.itemView.getContext().getResources(), R.color.article_list_text));
        } else {
            tbTextView.setTextColor(ResUtil.getColor(holder.itemView.getContext().getResources(), R.color.dark_article_list_text));
        }
        tbTextView.setMaxLines(2);
        tbTextView.setEllipsize(TextUtils.TruncateAt.END);
        tbTextView.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(), holder.itemView.getContext().getResources().getString(R.string.THP_TundraOffc)));
        tbTextView.setTextSize(17);

        taboolaNativeAdViewHolder.thumbNailContainer.addView(thumbnailView);
        taboolaNativeAdViewHolder.mAdContainer.addView(tbTextView);

        if (tbRecommendationItem.getBrandingView(taboolaNativeAdViewHolder.itemView.getContext()) != null) {
            TBTextView tbBrandTextView = tbRecommendationItem.getBrandingView(taboolaNativeAdViewHolder.itemView.getContext());
            tbBrandTextView.setTypeface(Typeface.createFromAsset(holder.itemView.getContext().getAssets(),
                    holder.itemView.getContext().getResources().getString(R.string.THP_FiraSans_Regular)));
            ViewGroup parent3 = (ViewGroup) tbBrandTextView.getParent();
            if (parent3 != null) {
                parent3.removeView(tbBrandTextView);
            }
            taboolaNativeAdViewHolder.mAdContainer.addView(tbBrandTextView);
        }

        TaboolaApi.getInstance().setOnClickListener((placementName, itemId, url, isOrganic) -> {
            Log.i("", "");

            if (isOrganic) {
                int articleId = CommonUtil.getArticleIdFromArticleUrl(url);
                IntentUtil.openDetailAfterSearchInActivity(holder.itemView.getContext(), ""+articleId, url);

                /*FlurryAgent.logEvent(holder.itemView.getContext().getString(R.string.ga_article_taboola_home_organic_clicked));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(holder.itemView.getContext(), "Taboola Item Click",
                        holder.itemView.getContext().getString(R.string.ga_article_taboola_home_organic_clicked),
                        holder.itemView.getContext().getString(R.string.ga_home));*/
                return false;
            } else {
                /*FlurryAgent.logEvent(holder.itemView.getContext().getString(R.string.ga_article_taboola_home_nonorganic_clicked));
                GoogleAnalyticsTracker.setGoogleAnalyticsEvent(holder.itemView.getContext(), "Taboola Item Click",
                        holder.itemView.getContext().getString(R.string.ga_article_taboola_home_nonorganic_clicked),
                        holder.itemView.getContext().getString(R.string.ga_home));*/
            }

            return true;
        });
    }

    private void fillInlineAdView(final RecyclerView.ViewHolder holder, SectionAdapterItem item) {
        AdData adData = item.getAdData();
        InlineAdViewHolder inlineAdViewHolder = (InlineAdViewHolder) holder;
        inlineAdViewHolder.frameLayout.removeAllViews();
        inlineAdViewHolder.frameLayout.setBackgroundResource(R.drawable.interstetial_ads_bg);
        final PublisherAdView adView = adData.getAdView();

        if(adData.isReloadOnScroll()) {
            // Create an ad request.
            PublisherAdRequest.Builder publisherAdRequestBuilder = new PublisherAdRequest.Builder();
            // Start loading the ad.
            adView.loadAd(publisherAdRequestBuilder.build());
        }

        inlineAdViewHolder.frameLayout.setBackground(null);
        adView.removeView(inlineAdViewHolder.frameLayout);
        // Just below is fix of Crashlytics #6509
        if (adView != null && adView.getParent() != null) {
            ((ViewGroup) adView.getParent()).removeView(adView);
        }
        inlineAdViewHolder.frameLayout.addView(adView);
    }

    private void fillStaticWebview(final RecyclerView.ViewHolder holder, SectionAdapterItem item) {
        StaticItemWebViewHolder staticItemHolder = (StaticItemWebViewHolder) holder;
        StaticPageUrlBean pageUrlBean = item.getStaticPageUrlBean();
        if(pageUrlBean == null) {
            return;
        }
        staticItemHolder.webView.loadUrl(pageUrlBean.getUrl());
        if(!pageUrlBean.getSectionId().equals("0")) {
            staticItemHolder.mDummyView.setVisibility(View.VISIBLE);
            // DummyView Click Listener
            // TODO, redirect on particular section or sub-section
            // staticItemHolder.mDummyView.setOnClickListener(new StaticItemDummyViewClick(url, mContext, null, false, -1));
        } else {
            staticItemHolder.mDummyView.setVisibility(View.GONE);
            // Enabling Weblink click on Lead Text
            new WebViewLinkClick(true).linkClick(staticItemHolder.webView, staticItemHolder.itemView.getContext(), null);
        }
    }

    private void fillSearchedArticleData(final RecyclerView.ViewHolder holder, final int position) {
        SearchRecyclerHolder searchHolder = (SearchRecyclerHolder)holder;
        final ArticleBean bean = adapterItems.get(position).getArticleBean();
        searchHolder.title.setText(bean.getTi());
        searchHolder.sname.setText(bean.getSname());
        String publishDate = bean.getPd();
        String formatedDate = AppDateUtil.getDurationFormattedDate(AppDateUtil.changeStringToMillis(publishDate), Locale.ENGLISH);
        searchHolder.publishDate.setText(formatedDate);
        searchHolder.mParentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Searched ", "Searched: Article Clicked", "Search Fragment");
                    FlurryAgent.logEvent("Searched: " + "Article Clicked");*/
                    IntentUtil.openSingleDetailActivity(v.getContext(), NetConstants.RECO_TEMP_NOT_EXIST, bean, bean.getArticleLink());
            }
        });

    }

    private void fillArticleData(final RecyclerView.ViewHolder articlesViewHolder, final int position) {
        ArticlesViewHolder holder = (ArticlesViewHolder) articlesViewHolder;
        final ArticleBean bean = adapterItems.get(position).getArticleBean();
        if (bean != null) {

            // Dims Read article given view
            dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.mArticlesLayout);

            // Checks article's type
            articleTypeImage(bean.getArticleType(), bean, holder.mMultimediaButton);

            // Checks whether article is bookmarked or not, If yes then it updates UI
            isExistInBookmark(holder.itemView.getContext(), bean, holder.mBookmarkButton);

            String imageUrl = bean.getIm_thumbnail_v2();

            if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
                imageUrl = bean.getIm_thumbnail();
            }

            if (imageUrl != null && !TextUtils.isEmpty(imageUrl)) {
                holder.mImageParentLayout.setVisibility(View.VISIBLE);
                holder.mArticleImageView.setVisibility(View.VISIBLE);
                imageUrl = ContentUtil.getThumbUrl(imageUrl);
                GlideUtil.loadImage(holder.itemView.getContext(), holder.mArticleImageView, imageUrl, R.drawable.ph_newsfeed_th);

            } else {
                holder.mArticleImageView.setVisibility(View.GONE);
                holder.mImageParentLayout.setVisibility(View.GONE);
            }



            holder.mArticleTextView.setText(bean.getTi());
            String publishTime = bean.getGmt();
            String timeDiff = AppDateUtil.getDurationFormattedDate(AppDateUtil.changeStringToMillisGMT(publishTime), Locale.ENGLISH);

            holder.mArticleTimeTextView.setText(timeDiff);
            holder.mArticleSectionName.setText(bean.getSname());

            holder.mBookmarkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Home", "Home: Bookmark button Clicked", "Home Fragment");
                    //FlurryAgent.logEvent("Home: " + "Bookmark button Clicked");

                    local_bookmarkOperation(v.getContext(), bean, holder.mBookmarkButton, position);

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //GoogleAnalyticsTracker.setGoogleAnalyticsEvent(view.getContext(), "Home", "Home: Article Clicked", "Home Fragment");
                    //FlurryAgent.logEvent("Home: " + "Article Clicked");
                    IntentUtil.openDetailActivity(view.getContext(), mPageSource, bean.getArticleId(), mSectionId, mSectionType, bean.getSectionName(), mIsSubSection);
                }
            });

            holder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //GoogleAnalyticsTracker.setGoogleAnalyticsEvent(view.getContext(), "Home", "Home: Article Clicked", "Home Fragment");
                    //FlurryAgent.logEvent("Home: " + "Article Clicked");
                    IntentUtil.openDetailActivity(view.getContext(), mPageSource, bean.getArticleId(), mSectionId, mSectionType, bean.getSectionName(), mIsSubSection);
                }
            });

            holder.mShareArticleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharingArticleUtil.shareArticle(v.getContext(), bean);
                }
            });

            //Enabling sliding for view pager
            //new UniversalTouchListener(mArticleViewHolder.mArticlesLayout, true);
        }
    }


    /**
     * Fill Banner
     * @param holder
     * @param position
     */
    private void fillBannerData(final BannerViewHolder holder, final int position) {
        final ArticleBean bean = adapterItems.get(position).getArticleBean();

        // Dims Read article given view
        dimReadArticle(holder.itemView.getContext(), bean.getArticleId(), holder.mBannerTextView);

        // Checks article's type
        articleTypeImage(bean.getArticleType(), bean, holder.mMultimediaButton);

        // Checks whether article is bookmarked or not, If yes then it updates UI
        isExistInBookmark(holder.itemView.getContext(), bean, holder.mBookmarkButton);

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
            String timeDiff = AppDateUtil.getDurationFormattedDate(AppDateUtil.changeStringToMillisGMT(publishTime), Locale.ENGLISH);
            holder.mArticleUpdateTime.setText(timeDiff);
            holder.mArticleSectionName.setText(bean.getSname());

            holder.mBannerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*GoogleAnalyticsTracker.setGoogleAnalyticsEvent(v.getContext(), "Banner", "Banner: Article Clicked", "Home Fragment");
                    FlurryAgent.logEvent("Banner: " + "Article Clicked");*/

                    IntentUtil.openDetailActivity(holder.itemView.getContext(), mPageSource,
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

                    local_bookmarkOperation(v.getContext(), bean, holder.mBookmarkButton, position);
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
        public IconImgView mBookmarkButton;
        public IconImgView mShareArticleButton;
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
     * Default Widget View Holder
     */
    private static class WidgetsViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView mWidgetsRecyclerView;
        public TextView mWidgetTitleTextView, mWidgetFooterTextView;

        public WidgetsViewHolder(View itemView) {
            super(itemView);

            mWidgetsRecyclerView = itemView.findViewById(R.id.recyclerview_widgets);
            mWidgetTitleTextView = itemView.findViewById(R.id.textview_widget_title);
            mWidgetFooterTextView = itemView.findViewById(R.id.textview_widget_viewAll);

        }
    }

    /**
     * Horizontal RecyclerView for Sub-sections
     */
    private static class ExploreViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView mExploreRecyclerView;

        public ExploreViewHolder(View itemView) {
            super(itemView);

            mExploreRecyclerView = itemView.findViewById(R.id.recyclerview_explore);
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

    public int insertItem(SectionAdapterItem item, int index) {
        int updateIndex = 0;
        if(index >= adapterItems.size()) {
            adapterItems.add(item);
            notifyItemChanged(adapterItems.size());
            updateIndex = adapterItems.size()-1;
        } else if(index < adapterItems.size()) {
            adapterItems.add(index, item);
            updateIndex = index;
        }

        return updateIndex;
    }

    public int indexOf(SectionAdapterItem item) {
        return adapterItems.indexOf(item);
    }

    public SectionAdapterItem getItem(int index) {
        return adapterItems.get(index);
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

        mWidgetsViewHolder.mWidgetFooterTextView.setOnClickListener(v->{
            FragmentUtil.redirectionOnSectionAndSubSection(v.getContext(), ""+dataBean.getWidgetAdapter().getSectionId());
        });

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
