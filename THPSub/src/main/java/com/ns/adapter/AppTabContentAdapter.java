package com.ns.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.material.snackbar.Snackbar;
import com.netoperation.model.AdData;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.MeBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.AppDateUtil;
import com.netoperation.util.NetConstants;
import com.netoperation.util.PremiumPref;
import com.netoperation.util.DefaultPref;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.OnEditionBtnClickListener;
import com.ns.clevertap.CleverTapUtil;
import com.ns.loginfragment.BaseFragmentTHP;
import com.ns.model.AppTabContentModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.AppAudioManager;
import com.ns.utils.CommonUtil;
import com.ns.utils.ContentUtil;
import com.ns.utils.PicassoUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.SharingArticleUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.WebViewLinkClick;
import com.ns.view.THP_AutoResizeWebview;
import com.ns.viewholder.ArticlesViewHolder;
import com.ns.viewholder.BookmarkPremiumViewHolder;
import com.ns.viewholder.BookmarkViewHolder;
import com.ns.viewholder.BriefcaseViewHolder;
import com.ns.viewholder.BriefingHeaderViewHolder;
import com.ns.viewholder.DG_DetailAudioViewHolder;
import com.ns.viewholder.DG_DetailPhotoViewHolder;
import com.ns.viewholder.DG_DetailVideoViewHolder;
import com.ns.viewholder.DG_Restricted_DetailDescriptionWebViewHolder;
import com.ns.viewholder.DashboardViewHolder;
import com.ns.viewholder.DG_DetailBannerViewHolder;
import com.ns.viewholder.DG_DetailDescriptionWebViewHolder;
import com.ns.viewholder.InlineAdViewHolder;
import com.ns.viewholder.PREMIUM_DetailBannerViewHolder;
import com.ns.viewholder.PREMIUM_DetailDescriptionWebViewHolder;
import com.ns.viewholder.DG_PostCommentBtnViewHolder;
import com.ns.viewholder.ViewHolderTaboola;
import com.taboola.android.TaboolaWidget;
import com.taboola.android.listeners.TaboolaEventListener;
import com.taboola.android.utils.SdkDetailsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AppTabContentAdapter extends BaseRecyclerViewAdapter {
    private List<AppTabContentModel> mContent;
    private String mFrom;
    private String mUserId;

    /**
     * This holds description webview position in recyclerview.
     * This is being used to notify recyclerview on TextSize change.
     */
    private int mDescriptionItemPosition;
    private int mDescriptionTextSize;


    private Snackbar snackbar;

    public Snackbar getSnackbar() {
        return snackbar;
    }

    private RecyclerView mRecyclerView;

    private String mGroupType;

    private TaboolaWidget mInfiniteTaboolaView;

    public void setFrom(String from) {
        mFrom = from;
    }

    public String getGroupType() {
        return mGroupType;
    }

    public void setGroupType(String groupType) {
        this.mGroupType = groupType;
    }

    public AppTabContentAdapter(List<AppTabContentModel> content, String from, String userId, RecyclerView recyclerView) {
        this.mContent = content;
        this.mFrom = from;
        this.mUserId = userId;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mContent.get(position).getViewType();
    }

    public int indexOf(AppTabContentModel item) {
        return mContent.indexOf(item);
    }

    public int insertItem(AppTabContentModel item, int index) {
        int updateIndex = 0;
        if (index >= mContent.size()) {
            mContent.add(item);
            updateIndex = mContent.size() - 1;
        } else if (index < mContent.size()) {
            mContent.add(index, item);
            updateIndex = index;
        }

        return updateIndex;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == VT_HEADER) {
            return new BriefingHeaderViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.premium_briefing_header, viewGroup, false));
        } else if (viewType == VT_DASHBOARD) {
            return new DashboardViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.premium_item_dashboard, viewGroup, false));
        } else if (viewType == VT_TRENDING) {
            return new DashboardViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.premium_item_dashboard, viewGroup, false));
        } else if (viewType == VT_BOOKMARK_PREMIUM) {
            return new BookmarkPremiumViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.premium_item_bookmark, viewGroup, false));
        } else if (viewType == VT_BRIEFCASE) {
            return new BriefcaseViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.premium_apptab_item_briefcase, viewGroup, false));
        } else if (viewType == VT_LOADMORE) {
            return new BookmarkViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loadmore, viewGroup, false));
        } else if (viewType == VT_PREMIUM_DETAIL_IMAGE_BANNER) {
            return new PREMIUM_DetailBannerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.premium_detail_banner, viewGroup, false));
        } else if (viewType == VT_PREMIUM_DETAIL_DESCRIPTION_WEBVIEW) {
            return new PREMIUM_DetailDescriptionWebViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.premium_detail_description, viewGroup, false));
        } else if (viewType == VT_GROUP_DEFAULT_DETAIL_IMAGE_BANNER) {
            return new DG_DetailBannerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dg_detail_banner, viewGroup, false));
        } else if (viewType == VT_GROUP_DEFAULT_DETAIL_DESCRIPTION_WEBVIEW) {
            return new DG_DetailDescriptionWebViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dg_detail_description, viewGroup, false));
        } else if (viewType == VT_GROUP_DEFAULT_DETAIL_RESTRICTED_DESCRIPTION_WEBVIEW) {
            return new DG_Restricted_DetailDescriptionWebViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dg_detail_restricted_description, viewGroup, false));
        } else if (viewType == VT_TABOOLA_WIDGET) {
            if (mInfiniteTaboolaView == null) {
                mInfiniteTaboolaView = createTaboolaWidget(viewGroup.getContext(), false);
            }
            return new ViewHolderTaboola(mInfiniteTaboolaView);
        } else if (viewType == VT_THD_300X250_ADS) {
            return new InlineAdViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.inline_ads_container, viewGroup, false));
        } else if (viewType == VT_THD_PHOTO_VIEW) {
            return new DG_DetailPhotoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dg_detail_banner, viewGroup, false));
        } else if (viewType == VT_DETAIL_VIDEO_PLAYER) {
            return new DG_DetailVideoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dg_detail_banner, viewGroup, false));
        } else if (viewType == VT_DETAIL_AUDIO_PLAYER) {
            return new DG_DetailAudioViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dg_detail_banner, viewGroup, false));
        } else if (viewType == VT_POST_COMMENT_BTN_VIEW) {
            return new DG_PostCommentBtnViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dg_detail_post_comment_btn, viewGroup, false));
        } else if (viewType == VT_THD_DEFAULT_ROW) {
            return new ArticlesViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.th_cardview_article_list, viewGroup, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        final ArticleBean bean = mContent.get(position).getBean();

        if (viewHolder instanceof DashboardViewHolder) {
            premium_ui_Dash_Tren_Row(viewHolder, bean, position);
        } else if (viewHolder instanceof BriefcaseViewHolder) {
            premium_ui_Briefing_Row(viewHolder, bean, position);
        } else if (viewHolder instanceof PREMIUM_DetailBannerViewHolder) {
            premium_ui_detail_banner(viewHolder, bean);
        } else if (viewHolder instanceof PREMIUM_DetailDescriptionWebViewHolder) {
            premium_ui_detail_description(viewHolder, bean, position);
        } else if (viewHolder instanceof BriefingHeaderViewHolder) {
            premium_ui_BriefingHeader(viewHolder, bean);
        } else if (viewHolder instanceof DG_DetailBannerViewHolder) {
            dg_ui_detail_banner(viewHolder, bean);
        } else if (viewHolder instanceof DG_DetailDescriptionWebViewHolder) {
            dg_ui_detail_description(viewHolder, mContent.get(position));
        } else if (viewHolder instanceof ViewHolderTaboola) {
            if (mInfiniteTaboolaView != null && mInfiniteTaboolaView.getTag() == null) {
                mInfiniteTaboolaView.setTag(bean.getArticleLink());
                buildBelowArticleWidget(mInfiniteTaboolaView, bean.getArticleLink());
            }
        } else if (viewHolder instanceof DG_Restricted_DetailDescriptionWebViewHolder) {
            dg_ui_restricted_detail_description(viewHolder, bean);
        } else if (viewHolder instanceof InlineAdViewHolder) {
            fillInlineAdView(viewHolder, mContent.get(position));
        } else if (viewHolder instanceof DG_DetailPhotoViewHolder) {
            dg_ui_detail_photoview_banner(viewHolder, bean);
        } else if (viewHolder instanceof DG_DetailVideoViewHolder) {
            dg_ui_detail_video_type_banner(viewHolder, bean);
        } else if (viewHolder instanceof DG_PostCommentBtnViewHolder) {
            dg_ui_post_comment_btn(viewHolder, mContent.get(position));
        } else if (viewHolder instanceof DG_DetailAudioViewHolder) {
            dg_ui_detail_audio_type_banner(viewHolder, bean);
        } else if (viewHolder instanceof BookmarkPremiumViewHolder) {
            premium_ui_Bookmark_Row(viewHolder, bean, position);
        } else if (viewHolder instanceof ArticlesViewHolder) {
            defaultBookmark_ArticleData(viewHolder, position);
        }
    }


    private void defaultBookmark_ArticleData(final RecyclerView.ViewHolder articlesViewHolder, final int position) {
        ArticlesViewHolder holder = (ArticlesViewHolder) articlesViewHolder;
        final ArticleBean bean = mContent.get(position).getBean();
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
                PicassoUtil.loadImage(holder.itemView.getContext(), holder.mArticleImageView, imageUrl, R.drawable.ph_newsfeed_th);

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
                    IntentUtil.openDetailActivity(holder.itemView.getContext(), NetConstants.GROUP_DEFAULT_BOOKMARK,
                            bean.getArticleUrl(), position, bean.getArticleId());
                }
            });

            holder.mMultimediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //GoogleAnalyticsTracker.setGoogleAnalyticsEvent(view.getContext(), "Home", "Home: Article Clicked", "Home Fragment");
                    //FlurryAgent.logEvent("Home: " + "Article Clicked");
                    IntentUtil.openDetailActivity(holder.itemView.getContext(), NetConstants.GROUP_DEFAULT_BOOKMARK,
                            bean.getArticleUrl(), position, bean.getArticleId());
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

    private void dg_ui_post_comment_btn(final RecyclerView.ViewHolder holder, AppTabContentModel item) {
        DG_PostCommentBtnViewHolder viewHolder = (DG_PostCommentBtnViewHolder) holder;
        viewHolder.post_comment_detail.setOnClickListener(v -> {
            IntentUtil.openCommentActivity(v.getContext(), item.getBean());
        });
    }

    private void fillInlineAdView(final RecyclerView.ViewHolder holder, AppTabContentModel item) {
        AdData adData = item.getAdData();
        InlineAdViewHolder inlineAdViewHolder = (InlineAdViewHolder) holder;
        inlineAdViewHolder.frameLayout.removeAllViews();
        inlineAdViewHolder.frameLayout.setBackgroundResource(R.drawable.interstetial_ads_bg);
        final PublisherAdView adView = adData.getAdView();

        if (adData.isReloadOnScroll()) {
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

    /**
     * Shows content on UI of Listing
     *
     * @param viewHolder
     * @param bean
     * @param position
     */
    private void premium_ui_Dash_Tren_Row(RecyclerView.ViewHolder viewHolder, ArticleBean bean, int position) {
        DashboardViewHolder holder = (DashboardViewHolder) viewHolder;

        holder.trendingIcon_Img.setVisibility(View.GONE);

        PicassoUtil.loadImage(holder.image.getContext(), holder.image, ContentUtil.getThumbUrl(bean.getThumbnailUrl()), R.drawable.th_ph_01);
        holder.authorName_Txt.setText(ContentUtil.getAuthor(bean.getAuthor()));
        holder.title.setText(bean.getArticletitle());
        // Section Name
        String sectionName = bean.getArticleSection();
        if (sectionName == null || TextUtils.isEmpty(sectionName)) {
            sectionName = bean.getSectionName();
        }

        sectionName = ResUtil.capitalizeFirstLetter(sectionName);
        holder.sectionName.setText(sectionName);
        // Publish Date
        String formatedPubDt = CommonUtil.fomatedDate(bean.getPubDateTime(), mFrom);
        holder.time_Txt.setText(formatedPubDt);


        holder.likeProgressBar.setVisibility(View.GONE);
        holder.bookmarkProgressBar.setVisibility(View.GONE);
        holder.toggleBtnProgressBar.setVisibility(View.GONE);

        premium_isFavOrLike(holder.like_Img.getContext(), bean, holder.like_Img, holder.toggleBtn_Img);
        // Checks whether article is bookmarked or not, If yes then it updates UI
        isExistInBookmark(holder.bookmark_Img.getContext(), bean, holder.bookmark_Img);

        holder.bookmark_Img.setOnClickListener(v -> {
                    if (NetUtils.isConnected(v.getContext())) {
                        premium_updateBookmarkFavLike(holder.bookmarkProgressBar, holder.bookmark_Img, holder.bookmark_Img.getContext(), position, bean, "bookmark");
                    } else {
                        Alerts.noConnectionSnackBar(v, (AppCompatActivity) v.getContext());
                    }
                }
        );

        holder.like_Img.setOnClickListener(v -> {
                    if (NetUtils.isConnected(v.getContext())) {
                        premium_updateBookmarkFavLike(holder.likeProgressBar, holder.like_Img, holder.like_Img.getContext(), position, bean, "favourite");
                    } else {
                        Alerts.noConnectionSnackBar(v, (AppCompatActivity) v.getContext());
                    }
                }
        );

        holder.toggleBtn_Img.setOnClickListener(v -> {
                    if (NetUtils.isConnected(v.getContext())) {
                        premium_updateBookmarkFavLike(holder.toggleBtnProgressBar, holder.toggleBtn_Img, holder.toggleBtn_Img.getContext(), position, bean, "dislike");
                    } else {
                        Alerts.noConnectionSnackBar(v, (AppCompatActivity) v.getContext());
                    }
                }
        );

        holder.itemView.setOnClickListener(v -> {
                    if (PremiumPref.getInstance(holder.itemView.getContext()).isUserAdsFree()) {
                        IntentUtil.openDetailActivity(holder.itemView.getContext(), mFrom,
                                bean.getArticleUrl(), position, bean.getArticleId());
                    } else {
                        IntentUtil.openSubscriptionActivity(holder.itemView.getContext(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    }

                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", ResUtil.capitalizeFirstLetter(mFrom) + " clicked : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");

                    if (getSnackbar() != null && getSnackbar().isShown()) {
                        getSnackbar().dismiss();
                    }
                }
        );
    }

    /**
     * Shows content on UI of Listing
     *
     * @param viewHolder
     * @param bean
     * @param position
     */
    private void premium_ui_Bookmark_Row(RecyclerView.ViewHolder viewHolder, ArticleBean bean, int position) {
        BookmarkPremiumViewHolder holder = (BookmarkPremiumViewHolder) viewHolder;
        PicassoUtil.loadImage(holder.image.getContext(), holder.image, ContentUtil.getThumbUrl(bean.getThumbnailUrl()), R.drawable.th_ph_01);
        holder.authorName_Txt.setText(ContentUtil.getAuthor(bean.getAuthor()));
        holder.title.setText(bean.getArticletitle());
        // Section Name
        String sectionName = bean.getArticleSection();
        if (sectionName == null || TextUtils.isEmpty(sectionName)) {
            sectionName = bean.getSectionName();
        }

        sectionName = ResUtil.capitalizeFirstLetter(sectionName);
        holder.sectionName.setText(sectionName);
        // Publish Date
        String formatedPubDt = CommonUtil.fomatedDate(bean.getPubDateTime(), mFrom);
        holder.time_Txt.setText(formatedPubDt);

        holder.bookmarkProgressBar.setVisibility(View.GONE);

        if (bean.getGroupType() == null || bean.getGroupType().equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
            holder.share_Img.setVisibility(View.VISIBLE);
        } else {
            holder.share_Img.setVisibility(View.GONE);
        }

        isExistInBookmark(holder.bookmark_Img.getContext(), bean, holder.bookmark_Img);

        holder.bookmark_Img.setOnClickListener(v -> {
                    if (bean.getGroupType() == null || bean.getGroupType().equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
                        local_removeBookmarkFromApp(v.getContext(), bean.getArticleId(), bean, holder.bookmarkProgressBar, holder.bookmark_Img, position, mFrom);
                    } else if (NetUtils.isConnected(v.getContext()) && bean.getGroupType().equals(NetConstants.GROUP_PREMIUM_BOOKMARK)) {
                        premium_updateBookmarkFavLike(holder.bookmarkProgressBar, holder.bookmark_Img, holder.bookmark_Img.getContext(), position, bean, "bookmark");
                    } else {
                        Alerts.noConnectionSnackBar(v, (AppCompatActivity) v.getContext());
                    }
                }
        );

        holder.share_Img.setOnClickListener(v -> {
                    SharingArticleUtil.shareArticle(v.getContext(), bean);
                }
        );

        holder.itemView.setOnClickListener(v -> {
                    if (bean.getGroupType() == null || bean.getGroupType().equals(NetConstants.GROUP_DEFAULT_BOOKMARK)) {
                        IntentUtil.openDetailActivity(holder.itemView.getContext(), NetConstants.GROUP_DEFAULT_BOOKMARK,
                                bean.getArticleUrl(), position, bean.getArticleId());
                    } else if (PremiumPref.getInstance(holder.itemView.getContext()).isUserAdsFree()) {
                        IntentUtil.openDetailActivity(holder.itemView.getContext(), mFrom,
                                bean.getArticleUrl(), position, bean.getArticleId());
                    } else {
                        IntentUtil.openSubscriptionActivity(holder.itemView.getContext(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    }

                    THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", ResUtil.capitalizeFirstLetter(mFrom) + " clicked : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");

                    if (getSnackbar() != null && getSnackbar().isShown()) {
                        getSnackbar().dismiss();
                    }
                }
        );
    }

    /**
     * Briefing Listing Row UI
     *
     * @param viewHolder
     * @param bean
     * @param position
     */
    private void premium_ui_Briefing_Row(RecyclerView.ViewHolder viewHolder, ArticleBean bean, int position) {
        BriefcaseViewHolder holder = (BriefcaseViewHolder) viewHolder;
        PicassoUtil.loadImage(holder.image.getContext(), holder.image, ContentUtil.getBreifingImgUrl(bean.getThumbnailUrl()), R.drawable.th_ph_02);
        holder.authorName_Txt.setText(ContentUtil.getAuthor(bean.getAuthor()));
        holder.title.setText(ResUtil.htmlText(bean.getArticletitle()));

        // Section name
        String sectionName = bean.getArticleSection();
        if (sectionName == null || TextUtils.isEmpty(sectionName)) {
            sectionName = bean.getSectionName();
        }

        sectionName = ResUtil.capitalizeFirstLetter(sectionName);
        holder.sectionName.setText(sectionName);
        // Publish Date
        String formatedPubDt = CommonUtil.fomatedDate(bean.getPubDateTime(), mFrom);
        holder.time_Txt.setText(formatedPubDt);
        holder.description_Txt.setText(ResUtil.htmlText(bean.getDescription()));

        holder.itemView.setOnClickListener(v -> {
            IntentUtil.openDetailActivity(holder.itemView.getContext(), mFrom,
                    bean.getArticleUrl(), position, bean.getArticleId());
            THPFirebaseAnalytics.setFirbaseAnalyticsEvent(holder.itemView.getContext(), "Action", "Briefing clicked : " + bean.getArticleId() + " : " + bean.getTitle(), "Briefing List Screen");

        });
    }

    /**
     * Premium Detail Page Banner UI
     *
     * @param viewHolder
     * @param bean
     */
    private void premium_ui_detail_banner(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        PREMIUM_DetailBannerViewHolder holder = (PREMIUM_DetailBannerViewHolder) viewHolder;
        final String articleType = bean.getArticletype();
        // To shows Article Type Image
        articleTypeImage(articleType, bean, holder.articleTypeimageView);

        String sectionName = bean.getArticleSection();
        if (sectionName == null || TextUtils.isEmpty(sectionName)) {
            sectionName = bean.getSectionName();
        }

        sectionName = ResUtil.capitalizeFirstLetter(sectionName);

        holder.tv_section.setText(sectionName);

        String authors = CommonUtil.getAutors(bean.getAuthor());
        if (authors == null) {
            holder.tv_author_name.setVisibility(View.GONE);
        } else {
            holder.tv_author_name.setText(authors);
            holder.tv_author_name.setVisibility(View.VISIBLE);
        }

        String location = bean.getLocation();
        if (location == null || TextUtils.isEmpty(location)) {
            holder.tv_city_name.setVisibility(View.GONE);
        } else {
            holder.tv_city_name.setVisibility(View.VISIBLE);
            holder.tv_city_name.setText(location);

        }

        String timeToRead = bean.getTimeToRead();
        if (timeToRead == null || timeToRead.equalsIgnoreCase("0") || TextUtils.isEmpty(timeToRead)) {
            holder.tv_time.setVisibility(View.GONE);
        } else {
            holder.tv_time.setText(timeToRead);
            holder.tv_time.setVisibility(View.VISIBLE);
        }

        // Publish Date
        String formatedPubDt = CommonUtil.fomatedDate(bean.getPubDateTime(), mFrom);
        if (formatedPubDt == null || TextUtils.isEmpty(formatedPubDt)) {
            holder.tv_updated_time.setVisibility(View.GONE);
        } else {
            holder.tv_updated_time.setText(formatedPubDt);
            holder.tv_updated_time.setVisibility(View.VISIBLE);
        }

        holder.tv_title.setText(bean.getArticletitle());

        if (ContentUtil.getBannerUrl(bean.getThumbnailUrl()).equalsIgnoreCase("")) {
            holder.imageView.setVisibility(View.GONE);
            holder.tv_caption.setVisibility(View.GONE);
            holder.shadowOverlay.setVisibility(View.GONE);
        } else {
            holder.imageView.setVisibility(View.VISIBLE);
            PicassoUtil.loadImage(holder.itemView.getContext(), holder.imageView, ContentUtil.getBannerUrl(bean.getThumbnailUrl()), R.drawable.th_ph_02);

            String caption = null;
            if (bean.getIMAGES() != null && bean.getIMAGES().size() > 0) {
                caption = bean.getIMAGES().get(0).getCa();
            }

            if (caption != null && !TextUtils.isEmpty(caption.trim())) {
                holder.shadowOverlay.setVisibility(View.VISIBLE);
                holder.tv_caption.setVisibility(View.VISIBLE);
                holder.tv_caption.setText(ResUtil.htmlText(caption));
            } else {
                holder.shadowOverlay.setVisibility(View.GONE);
                holder.tv_caption.setVisibility(View.GONE);
            }


        }

        // Banner Image Click Listener
        holder.imageView.setOnClickListener(v -> {
            if (isVideo(articleType, bean)) {
                if (isYoutubeVideo(articleType)) {
                    if (bean.getYoutubeVideoId() == null || ResUtil.isEmpty(bean.getYoutubeVideoId())) {
                        Alerts.showSnackbar((Activity) holder.imageView.getContext(), "Video link not found.");
                        return;
                    }
                    IntentUtil.openYoutubeActivity(holder.itemView.getContext(), bean.getYoutubeVideoId());
                    return;
                }
            }

            // Opens Gallery
            //IntentUtils.openVerticleGalleryActivity(holder.itemView.getContext(), articleBean.getIMAGES(), articleBean.getTITLE());
            if (bean.getIMAGES() != null && bean.getIMAGES().size() > 0) {
                IntentUtil.openHorizontalGalleryActivity(holder.itemView.getContext(), null, bean.getIMAGES(), 0);
            } else {
                IntentUtil.openHorizontalGalleryActivity(holder.itemView.getContext(), null, bean.getMedia(), 0);
            }
        });

        long animDuration = 500;

        holder.tv_title.animate()
                .alpha(1.0f)
                .setDuration(animDuration);
        holder.imageView.animate()
                .alpha(1.0f)
                .setDuration(animDuration);
        holder.tv_author_name.animate()
                .alpha(0.6f)
                .setDuration(animDuration);
        holder.tv_caption.animate()
                .alpha(1.0f)
                .setDuration(animDuration);
        holder.tv_updated_time.animate()
                .alpha(1.0f)
                .setDuration(animDuration);
        holder.tv_city_name.animate()
                .alpha(1.0f)
                .setDuration(animDuration);
        holder.tv_time.animate()
                .alpha(1.0f)
                .setDuration(animDuration);

    }

    /**
     * Premium Detail Page Description UI
     *
     * @param viewHolder
     * @param bean
     */
    private void premium_ui_detail_description(RecyclerView.ViewHolder viewHolder, ArticleBean bean, int position) {
        PREMIUM_DetailDescriptionWebViewHolder holder = (PREMIUM_DetailDescriptionWebViewHolder) viewHolder;
        mDescriptionTextSize = DefaultPref.getInstance(holder.mLeadTxt.getContext()).getDescriptionSize();

        // Enabling Weblink click on Lead Text
        new WebViewLinkClick(true).linkClick(holder.mLeadTxt, holder.itemView.getContext(), null);

        holder.mLeadTxt.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.premium_WebTextDescription(holder.webview.getContext(), bean.getArticletitle(), true),
                "text/html", "UTF-8", null);
        holder.mLeadTxt.setSize(mDescriptionTextSize);

        holder.webview.setSize(mDescriptionTextSize);

        // Enabling Weblink click on Description
        new WebViewLinkClick(true).linkClick(holder.webview, holder.itemView.getContext(), null);

        holder.webview.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.premium_WebTextDescription(holder.webview.getContext(), bean.getDescription(), false),
                "text/html", "UTF-8", null);

        mDescriptionItemPosition = position;

    }

    /**
     * Premium Briefing Header UI
     *
     * @param viewHolder
     * @param bean
     */
    private void premium_ui_BriefingHeader(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        BriefingHeaderViewHolder holder = (BriefingHeaderViewHolder) viewHolder;
        holder.userName_Txt.setText(bean.getTitle());
        if (mFrom.equalsIgnoreCase(NetConstants.API_Mystories)) {
            holder.yourEditionFor_Txt.setText(bean.getSectionName());
            holder.yourEditionFor_Txt.setVisibility(View.VISIBLE);
            holder.editionBtn_Txt.setVisibility(View.GONE);
        } else if (mFrom.equalsIgnoreCase(NetConstants.BREIFING_ALL) || mFrom.equalsIgnoreCase(NetConstants.BREIFING_MORNING)
                || mFrom.equalsIgnoreCase(NetConstants.BREIFING_NOON) || mFrom.equalsIgnoreCase(NetConstants.BREIFING_EVENING)) {
            holder.editionBtn_Txt.setText(bean.getSectionName());
            holder.editionBtn_Txt.setVisibility(View.VISIBLE);
            holder.yourEditionFor_Txt.setVisibility(View.VISIBLE);
            holder.yourEditionFor_Txt.setText("Today's Briefing");
        } else if (mFrom.equalsIgnoreCase(NetConstants.API_suggested)) {
            holder.yourEditionFor_Txt.setText(bean.getSectionName());
            holder.yourEditionFor_Txt.setVisibility(View.VISIBLE);
            holder.editionBtn_Txt.setVisibility(View.GONE);
        }

        boolean isDayTheme = DefaultPref.getInstance(holder.editionBtn_Txt.getContext()).isUserThemeDay();
        if (isDayTheme) {
            holder.editionBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ResUtil.getBackgroundDrawable(holder.editionBtn_Txt.getContext().getResources(), R.drawable.ic_edition_dropdown), null);
        } else {
            holder.editionBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ResUtil.getBackgroundDrawable(holder.editionBtn_Txt.getContext().getResources(), R.drawable.ic_edition_dropdown_white), null);
        }

        holder.editionBtn_Txt.setOnClickListener(v -> {
            if (mOnEditionBtnClickListener != null) {
                mOnEditionBtnClickListener.OnEditionBtnClickListener();
            }
        });

    }


    /**
     * Group Default Detail Page Banner
     *
     * @param viewHolder
     * @param bean
     */
    private void dg_ui_detail_banner(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        DG_DetailBannerViewHolder dg_banner_vh = (DG_DetailBannerViewHolder) viewHolder;
        dg_banner_vh.mMultiMediaButton.setVisibility(View.GONE);
        boolean isAppExclusive = bean.getSid() != null && bean.getSid().equals("" + THPConstants.APP_EXCLUSIVE_SECTION_ID);
        if (isAppExclusive) {
            dg_banner_vh.mTitleTextView.setText(Html.fromHtml("<i>" + "\"" + bean.getTi() + "\"" + "</i>"));
            dg_banner_vh.mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            dg_banner_vh.mAuthorTextView.setVisibility(View.GONE);
            dg_banner_vh.mUpdatedTextView.setVisibility(View.GONE);
            dg_banner_vh.mCreatedDateTextView.setVisibility(View.GONE);
            dg_banner_vh.mHeaderImageView.setVisibility(View.GONE);
            dg_banner_vh.mCaptionTextView.setVisibility(View.GONE);
            dg_banner_vh.mCaptionDevider.setVisibility(View.GONE);
        } else {
            dg_banner_vh.mTitleTextView.setText(bean.getTi());
            dg_banner_vh.mArticleLocationView.setText(bean.getLocation());
            String author = bean.getAu();
            if (author != null && !TextUtils.isEmpty(author)) {
                dg_banner_vh.mAuthorTextView.setVisibility(View.VISIBLE);
                dg_banner_vh.mAuthorTextView.setText(author.replace(",\n", " | ").replace(",", " | "));
            } else {
                dg_banner_vh.mAuthorTextView.setVisibility(View.GONE);
            }

            dg_banner_vh.mUpdatedTextView.setText(AppDateUtil.getTopNewsFormattedDate(AppDateUtil.changeStringToMillisGMT(bean.getPd())));
            dg_banner_vh.mCreatedDateTextView.setText(AppDateUtil.getPlaneTopNewsFormattedDate(AppDateUtil.changeStringToMillis(bean.getOd())));

            final ArrayList<MeBean> mImageList = bean.getMe();

            if (mImageList != null && mImageList.size() > 0) {
                String imageUrl = mImageList.get(0).getIm_v2();
                if (!ResUtil.isEmpty(imageUrl)) {
                    PicassoUtil.loadImage(dg_banner_vh.itemView.getContext(), dg_banner_vh.mHeaderImageView, imageUrl, R.drawable.ph_topnews_th);
                } else {
                    dg_banner_vh.mHeaderImageView.setVisibility(View.GONE);
                }

                String caption = mImageList.get(0).getCa();
                if (caption != null && !TextUtils.isEmpty(caption)) {
                    dg_banner_vh.mCaptionTextView.setText(Html.fromHtml(caption));
                } else {
                    dg_banner_vh.mCaptionTextView.setVisibility(View.GONE);
                }
            }
            dg_banner_vh.mHeaderImageView.setOnClickListener(v -> IntentUtil.openVerticleGalleryActivity(v.getContext(), mImageList, mFrom));
        }
    }

    /**
     * Photo view type banner
     *
     * @param viewHolder view holder for Photo type views
     * @param bean       for Article
     */
    private void dg_ui_detail_photoview_banner(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        DG_DetailPhotoViewHolder db_photo_banner_holder = (DG_DetailPhotoViewHolder) viewHolder;
        boolean isAppExclusive = bean.getSid() != null && bean.getSid().equals("" + THPConstants.APP_EXCLUSIVE_SECTION_ID);
        if (isAppExclusive) {
            db_photo_banner_holder.mTitleTextView.setText(Html.fromHtml("<i>" + "\"" + bean.getTi() + "\"" + "</i>"));
            db_photo_banner_holder.mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            db_photo_banner_holder.mAuthorTextView.setVisibility(View.GONE);
            db_photo_banner_holder.mUpdatedTextView.setVisibility(View.GONE);
            db_photo_banner_holder.mCreatedDateTextView.setVisibility(View.GONE);
            db_photo_banner_holder.mHeaderImageView.setVisibility(View.GONE);
            db_photo_banner_holder.mMultiMediaButton.setVisibility(View.GONE);
            db_photo_banner_holder.mCaptionTextView.setVisibility(View.GONE);
            db_photo_banner_holder.mCaptionDevider.setVisibility(View.GONE);
        } else {
            db_photo_banner_holder.mTitleTextView.setText(bean.getTi());
            db_photo_banner_holder.mArticleLocationView.setText(bean.getLocation());
            String author = bean.getAu();
            if (author != null && !TextUtils.isEmpty(author)) {
                db_photo_banner_holder.mAuthorTextView.setVisibility(View.VISIBLE);
                db_photo_banner_holder.mAuthorTextView.setText(author.replace(",\n", " | ").replace(",", " | "));
            } else {
                db_photo_banner_holder.mAuthorTextView.setVisibility(View.GONE);
            }

            db_photo_banner_holder.mUpdatedTextView.setText(AppDateUtil.getTopNewsFormattedDate(AppDateUtil.changeStringToMillisGMT(bean.getPd())));
            db_photo_banner_holder.mCreatedDateTextView.setText(AppDateUtil.getPlaneTopNewsFormattedDate(AppDateUtil.changeStringToMillis(bean.getOd())));

            final ArrayList<MeBean> mImageList = bean.getMe();
            if (mImageList != null && mImageList.size() > 0) {
                String imageUrl = mImageList.get(0).getIm_v2();
                if (!ResUtil.isEmpty(imageUrl)) {
                    PicassoUtil.loadImage(db_photo_banner_holder.itemView.getContext(), db_photo_banner_holder.mHeaderImageView, imageUrl, R.drawable.ph_topnews_th);
                }
                String caption = mImageList.get(0).getCa();
                if (caption != null && !TextUtils.isEmpty(caption)) {
                    db_photo_banner_holder.mCaptionTextView.setText(Html.fromHtml(caption));
                } else {
                    db_photo_banner_holder.mCaptionTextView.setVisibility(View.GONE);
                }
            }
            db_photo_banner_holder.mHeaderImageView.setOnClickListener(v -> {
                IntentUtil.openVerticleGalleryActivity(v.getContext(), mImageList, mFrom);
            });
            // To shows Article Type Image
            articleTypeImage(bean.getArticleType(), bean, db_photo_banner_holder.mMultiMediaButton);
            db_photo_banner_holder.mMultiMediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IntentUtil.openVerticleGalleryActivity(v.getContext(), mImageList, mFrom);
                }
            });
        }
    }

    /**
     * Video view type banner
     *
     * @param viewHolder view holder for Photo type views
     * @param bean       for Article
     */
    private void dg_ui_detail_video_type_banner(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        DG_DetailVideoViewHolder db_video_banner_holder = (DG_DetailVideoViewHolder) viewHolder;
        boolean isAppExclusive = bean.getSid() != null && bean.getSid().equals("" + THPConstants.APP_EXCLUSIVE_SECTION_ID);
        if (isAppExclusive) {
            db_video_banner_holder.mTitleTextView.setText(Html.fromHtml("<i>" + "\"" + bean.getTi() + "\"" + "</i>"));
            db_video_banner_holder.mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            db_video_banner_holder.mAuthorTextView.setVisibility(View.GONE);
            db_video_banner_holder.mUpdatedTextView.setVisibility(View.GONE);
            db_video_banner_holder.mCreatedDateTextView.setVisibility(View.GONE);
            db_video_banner_holder.mHeaderImageView.setVisibility(View.GONE);
            db_video_banner_holder.mMultiMediaButton.setVisibility(View.GONE);
            db_video_banner_holder.mCaptionTextView.setVisibility(View.GONE);
            db_video_banner_holder.mCaptionDevider.setVisibility(View.GONE);
        } else {
            db_video_banner_holder.mTitleTextView.setText(bean.getTi());
            db_video_banner_holder.mArticleLocationView.setText(bean.getLocation());
            String author = bean.getAu();
            if (author != null && !TextUtils.isEmpty(author)) {
                db_video_banner_holder.mAuthorTextView.setVisibility(View.VISIBLE);
                db_video_banner_holder.mAuthorTextView.setText(author.replace(",\n", " | ").replace(",", " | "));
            } else {
                db_video_banner_holder.mAuthorTextView.setVisibility(View.GONE);
            }

            db_video_banner_holder.mUpdatedTextView.setText(AppDateUtil.getTopNewsFormattedDate(AppDateUtil.changeStringToMillisGMT(bean.getPd())));
            db_video_banner_holder.mCreatedDateTextView.setText(AppDateUtil.getPlaneTopNewsFormattedDate(AppDateUtil.changeStringToMillis(bean.getOd())));

            final ArrayList<MeBean> mImageList = bean.getMe();
            if (mImageList != null && mImageList.size() > 0) {
                String imageUrl = mImageList.get(0).getIm_v2();
                if (!ResUtil.isEmpty(imageUrl)) {
                    PicassoUtil.loadImage(db_video_banner_holder.itemView.getContext(), db_video_banner_holder.mHeaderImageView, imageUrl, R.drawable.ph_topnews_th);
                }
                String caption = mImageList.get(0).getCa();
                if (caption != null && !TextUtils.isEmpty(caption)) {
                    db_video_banner_holder.mCaptionTextView.setText(Html.fromHtml(caption));
                } else {
                    db_video_banner_holder.mCaptionTextView.setVisibility(View.GONE);
                }
            }
            db_video_banner_holder.mHeaderImageView.setOnClickListener(v -> {
                if (bean.getVid() != null && !TextUtils.isEmpty(bean.getVid())) {
                    IntentUtil.openJWVideoPayerActivity(v.getContext(), bean.getVid());
                } else {
                    IntentUtil.openYoutubeActivity(v.getContext(), bean.getYoutube_video_id());
                }
            });
            // To shows Article Type Image
            articleTypeImage(bean.getArticleType(), bean, db_video_banner_holder.mMultiMediaButton);
            db_video_banner_holder.mMultiMediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getVid() != null && !TextUtils.isEmpty(bean.getVid())) {
                        IntentUtil.openJWVideoPayerActivity(v.getContext(), bean.getVid());
                    } else {
                        IntentUtil.openYoutubeActivity(v.getContext(), bean.getYoutube_video_id());
                    }
                }
            });
        }
    }

    /**
     * Audio view type banner
     *
     * @param viewHolder view holder for Photo type views
     * @param bean       for Article
     */
    private void dg_ui_detail_audio_type_banner(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        DG_DetailAudioViewHolder dg_detailAudioViewHolder = (DG_DetailAudioViewHolder) viewHolder;
        boolean isAppExclusive = bean.getSid() != null && bean.getSid().equals("" + THPConstants.APP_EXCLUSIVE_SECTION_ID);
        if (isAppExclusive) {
            dg_detailAudioViewHolder.mTitleTextView.setText(Html.fromHtml("<i>" + "\"" + bean.getTi() + "\"" + "</i>"));
            dg_detailAudioViewHolder.mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            dg_detailAudioViewHolder.mAuthorTextView.setVisibility(View.GONE);
            dg_detailAudioViewHolder.mUpdatedTextView.setVisibility(View.GONE);
            dg_detailAudioViewHolder.mCreatedDateTextView.setVisibility(View.GONE);
            dg_detailAudioViewHolder.mHeaderImageView.setVisibility(View.GONE);
            dg_detailAudioViewHolder.mMultiMediaButton.setVisibility(View.GONE);
            dg_detailAudioViewHolder.mCaptionTextView.setVisibility(View.GONE);
            dg_detailAudioViewHolder.mCaptionDevider.setVisibility(View.GONE);
        } else {
            dg_detailAudioViewHolder.mTitleTextView.setText(bean.getTi());
            dg_detailAudioViewHolder.mArticleLocationView.setText(bean.getLocation());
            String author = bean.getAu();
            if (author != null && !TextUtils.isEmpty(author)) {
                dg_detailAudioViewHolder.mAuthorTextView.setVisibility(View.VISIBLE);
                dg_detailAudioViewHolder.mAuthorTextView.setText(author.replace(",\n", " | ").replace(",", " | "));
            } else {
                dg_detailAudioViewHolder.mAuthorTextView.setVisibility(View.GONE);
            }

            dg_detailAudioViewHolder.mUpdatedTextView.setText(AppDateUtil.getTopNewsFormattedDate(AppDateUtil.changeStringToMillisGMT(bean.getPd())));
            dg_detailAudioViewHolder.mCreatedDateTextView.setText(AppDateUtil.getPlaneTopNewsFormattedDate(AppDateUtil.changeStringToMillis(bean.getOd())));

            final ArrayList<MeBean> mImageList = bean.getMe();
            if (mImageList != null && mImageList.size() > 0) {
                String imageUrl = mImageList.get(0).getIm_v2();
                if (!ResUtil.isEmpty(imageUrl)) {
                    PicassoUtil.loadImage(dg_detailAudioViewHolder.itemView.getContext(), dg_detailAudioViewHolder.mHeaderImageView, imageUrl, R.drawable.ph_topnews_th);
                }
                String caption = mImageList.get(0).getCa();
                if (caption != null && !TextUtils.isEmpty(caption)) {
                    dg_detailAudioViewHolder.mCaptionTextView.setText(Html.fromHtml(caption));
                } else {
                    dg_detailAudioViewHolder.mCaptionTextView.setVisibility(View.GONE);
                }
            }
            dg_detailAudioViewHolder.mHeaderImageView.setOnClickListener(v -> {
                //Play or Pause Audio
                AppAudioManager.getInstance().changeMediaFile(bean.getAudioLink());
            });
            // To shows Article Type Image
            articleTypeImage(bean.getArticleType(), bean, dg_detailAudioViewHolder.mMultiMediaButton);
            dg_detailAudioViewHolder.mMultiMediaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Play or Pause Audio
                    AppAudioManager.getInstance().changeMediaFile(bean.getAudioLink());
                }
            });
        }
    }

    /**
     * Default Group Detail Page Description UI
     *
     * @param viewHolder
     * @param item
     */
    private void dg_ui_detail_description(RecyclerView.ViewHolder viewHolder, AppTabContentModel item) {
        ArticleBean bean = item.getBean();
        DG_DetailDescriptionWebViewHolder holder = (DG_DetailDescriptionWebViewHolder) viewHolder;
        mDescriptionTextSize = DefaultPref.getInstance(holder.itemView.getContext()).getDescriptionSize();

        String fullDescription = bean.getDescription();

        if (bean.getAdd_pos() > 1) {
            if (item.getUniqueIdForView().equals("description_1Model")) {
                holder.webview.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.defaultgroup_showDescription(holder.itemView.getContext(), bean.getLe(), fullDescription.substring(0, bean.getAdd_pos() - 1)),
                        "text/html", "UTF-8", null);
            } else if (item.getUniqueIdForView().equals("description_2Model")) {
                holder.webview.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.defaultgroup_showDescription(holder.itemView.getContext(), "", fullDescription.substring(bean.getAdd_pos() - 1)),
                        "text/html", "UTF-8", null);
            }
        } else {
            holder.webview.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.defaultgroup_showDescription(holder.itemView.getContext(), bean.getLe(), fullDescription),
                    "text/html", "UTF-8", null);
        }

        holder.webview.setSize(mDescriptionTextSize);

        // Enabling Weblink click on Description
        new WebViewLinkClick(true).linkClick(holder.webview, holder.itemView.getContext(), null);


    }

    /**
     * Default Group Restricted Detail Page Description UI
     *
     * @param viewHolder
     * @param bean
     */
    private void dg_ui_restricted_detail_description(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        DG_Restricted_DetailDescriptionWebViewHolder holder = (DG_Restricted_DetailDescriptionWebViewHolder) viewHolder;
        mDescriptionTextSize = DefaultPref.getInstance(holder.itemView.getContext()).getDescriptionSize();

        holder.webview.setSize(mDescriptionTextSize);

        holder.webview.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.defaultgroup_showDescription(holder.itemView.getContext(), bean.getLe(), bean.getDescription()),
                "text/html", "UTF-8", null);

        RelativeLayout.LayoutParams part1WebviewParam = (RelativeLayout.LayoutParams) holder.webview.getLayoutParams();
        mDescriptionTextSize = DefaultPref.getInstance(holder.itemView.getContext()).getDescriptionSize();
        if (part1WebviewParam != null) {
            switch (mDescriptionTextSize) {
                case 0:
                    part1WebviewParam.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                    break;
                case 1:
                    part1WebviewParam.height = holder.itemView.getResources().getDimensionPixelSize(R.dimen.webview_shadow_SMALLER);
                    break;
                case 2:
                    part1WebviewParam.height = holder.itemView.getResources().getDimensionPixelSize(R.dimen.webview_shadow_NORMAL);
                    break;
                case 3:
                    part1WebviewParam.height = holder.itemView.getResources().getDimensionPixelSize(R.dimen.webview_shadow_LARGER);
                    break;
                case 4:
                    part1WebviewParam.height = holder.itemView.getResources().getDimensionPixelSize(R.dimen.webview_shadow_LARGEST);
                    break;
            }

            holder.webview.setLayoutParams(part1WebviewParam);

            if (holder.shadowView_Mp != null) {
                holder.shadowView_Mp.setLayoutParams(part1WebviewParam);
                boolean mIsDayTheme = DefaultPref.getInstance(holder.shadowView_Mp.getContext()).isUserThemeDay();
                if (mIsDayTheme) {
                    holder.shadowView_Mp.setBackground(ResUtil.getBackgroundDrawable(holder.shadowView_Mp.getContext().getResources(), R.drawable.top_shadow_gradient_light));
                } else {
                    holder.shadowView_Mp.setBackground(ResUtil.getBackgroundDrawable(holder.shadowView_Mp.getContext().getResources(), R.drawable.top_shadow_gradient_dark));
                }
            }
            boolean isUserLoggedIn = PremiumPref.getInstance(holder.itemView.getContext()).isUserLoggedIn();
            boolean isHasSubscription = PremiumPref.getInstance(holder.itemView.getContext()).isUserAdsFree();
            String mpBlockerTitleText = BaseFragmentTHP.getMpNonSignInTitleBlocker();
            String mpBlockerDescText = BaseFragmentTHP.getMpNonSignInDescBlocker();
            if (isUserLoggedIn && !isHasSubscription) {
                mpBlockerTitleText = BaseFragmentTHP.getMpExpiredUserTitleBlocker();
                mpBlockerDescText = BaseFragmentTHP.getMpExpiredUserDescBlocker();
            }
            holder.textMpBlockerTitle.setText(mpBlockerTitleText);
            holder.textMPBlockerDescription.setText(mpBlockerDescText);

            if (isUserLoggedIn) {
                holder.signIn_Txt.setVisibility(View.GONE);
                holder.signUp_Txt.setVisibility(View.GONE);
            } else {
                holder.signIn_Txt.setVisibility(View.VISIBLE);
                holder.signUp_Txt.setVisibility(View.VISIBLE);
            }
            ResUtil.doClickSpanForString(holder.signIn_Txt.getContext(), BaseFragmentTHP.getMpSignInButtonName(), "Sign In",
                    holder.signIn_Txt,
                    R.color.color_1b528e,
                    () -> {
                        THPConstants.IS_FROM_MP_BLOCKER = true;
                        IntentUtil.openSignInOrUpActivity(holder.signIn_Txt.getContext(), "signIn");
                        //CleverTap and Firebase Events
                        THPFirebaseAnalytics.firebaseMP_SignIn(holder.signIn_Txt.getContext(), BaseFragmentTHP.getCycleName());
                        CleverTapUtil.cleverTapMP_SignIn(holder.signIn_Txt.getContext(), BaseFragmentTHP.getCycleName());

                    });
            ResUtil.doClickSpanForString(holder.signUp_Txt.getContext(), BaseFragmentTHP.getMpSignUpButtonName(), "Sign Up",
                    holder.signUp_Txt, R.color.color_1b528e, () -> {
                        THPConstants.IS_FROM_MP_BLOCKER = true;
                        IntentUtil.openSignInOrUpActivity(holder.signUp_Txt.getContext(), "signUp");
                        //CleverTap and Firebase Events
                        THPFirebaseAnalytics.firebaseMP_SignUp(holder.signUp_Txt.getContext(), BaseFragmentTHP.getCycleName());
                        CleverTapUtil.cleverTapMP_SignUp(holder.signUp_Txt.getContext(), BaseFragmentTHP.getCycleName());
                    });
            holder.getFullAccess_Txt.setText(BaseFragmentTHP.getMpGetFullAccessButtonName());
            // Get Full Access Click Listener
            holder.getFullAccess_Txt.setOnClickListener(v -> {
                if (!NetUtils.isConnected(holder.getFullAccess_Txt.getContext())) {
                    Alerts.noConnectionSnackBar(holder.getFullAccess_Txt, (AppCompatActivity) holder.getFullAccess_Txt.getContext());
                } else {
                    IntentUtil.openSubscriptionActivity(holder.getFullAccess_Txt.getContext(), THPConstants.FROM_SUBSCRIPTION_EXPLORE);
                    THPConstants.IS_FROM_MP_BLOCKER = true;
                    //MP Firebase & CleverTap events
                    THPFirebaseAnalytics.firebaseGetFullAccessButtonClick(holder.getFullAccess_Txt.getContext(), BaseFragmentTHP.getCycleName());
                    CleverTapUtil.cleverTapGetFullAccessButtonClick(holder.getFullAccess_Txt.getContext(), BaseFragmentTHP.getCycleName());
                }
            });
        }

    }

    private static TaboolaWidget createTaboolaWidget(Context context, boolean infiniteWidget) {
        TaboolaWidget taboolaWidget = new TaboolaWidget(context);
        int height = infiniteWidget ? SdkDetailsHelper.getDisplayHeight(context) * 2 : ViewGroup.LayoutParams.WRAP_CONTENT;
        taboolaWidget.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        return taboolaWidget;
    }

    private void buildBelowArticleWidget(TaboolaWidget taboolaWidget, String articleLink) {
        if (PremiumPref.getInstance(taboolaWidget.getContext()).isUserAdsFree()) {
            return;
        }
        Resources res = taboolaWidget.getResources();
        taboolaWidget.setPublisher(res.getString(R.string.taboola_pub));
        taboolaWidget.setPageType(res.getString(R.string.taboola_pagetype));
        taboolaWidget.setMode(res.getString(R.string.taboola_mode));
        boolean mIsDayTheme = DefaultPref.getInstance(taboolaWidget.getContext()).isUserThemeDay();
        if (mIsDayTheme) {
            taboolaWidget.setPlacement(res.getString(R.string.taboola_placement));
        } else {
            taboolaWidget.setPlacement(res.getString(R.string.dark_taboola_placement));
        }
        taboolaWidget.setTargetType(res.getString(R.string.taboola_targetype));
        taboolaWidget.setPageUrl(articleLink);
        taboolaWidget.setInterceptScroll(true);

        //used for enable horizontal scroll
        HashMap<String, String> optionalPageCommands = new HashMap<>();
        optionalPageCommands.put("enableHorizontalScroll", "true");
        optionalPageCommands.put("useOnlineTemplate", "true");
        taboolaWidget.setExtraProperties(optionalPageCommands);
        mInfiniteTaboolaView.setExtraProperties(optionalPageCommands);

        mInfiniteTaboolaView.setTaboolaEventListener(new TaboolaEventListener() {
            @Override
            public boolean taboolaViewItemClickHandler(String url, boolean isOrganic) {

                if (isOrganic) {
                    int articleId = CommonUtil.getArticleIdFromArticleUrl(url);
                    IntentUtil.openDetailAfterSearchInActivity(mInfiniteTaboolaView.getContext(), "" + articleId, url);

                    /*FlurryAgent.logEvent(mInfiniteTaboolaView.getContext().getResources().getString(R.string.ga_article_taboola_organic_clicked));
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mInfiniteTaboolaView.getContext(), "Taboola Item Click",
                            getString(R.string.ga_article_taboola_organic_clicked),
                            getString(R.string.ga_article_detail_lebel));*/
                    return false;
                } else {
                    /*FlurryAgent.logEvent(mInfiniteTaboolaView.getContext().getResources().getString(R.string.ga_article_taboola_nonorganic_clicked));
                    GoogleAnalyticsTracker.setGoogleAnalyticsEvent(mInfiniteTaboolaView.getContext(), "Taboola Item Click",
                            getString(R.string.ga_article_taboola_nonorganic_clicked),
                            getString(R.string.ga_article_detail_lebel));*/
                }

                return true;
            }

            @Override
            public void taboolaViewResizeHandler(TaboolaWidget taboolaWidget, int i) {

            }
        });

        taboolaWidget.fetchContent();
    }


    public void addData(List<AppTabContentModel> content) {
        mContent.addAll(content);
        notifyDataSetChanged();
    }

    public void setData(List<AppTabContentModel> content) {
        mContent = content;
        notifyDataSetChanged();
    }

    public void addData(AppTabContentModel content) {
        mContent.add(content);
        notifyItemChanged(mContent.size() - 1);
    }

    public void replaceData(ArticleBean articleBean, int position) {
        if (position < mContent.size()) {
            mContent.get(position).setBean(articleBean);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        if (mInfiniteTaboolaView != null) {
            mInfiniteTaboolaView.clearCache(true);
            mInfiniteTaboolaView = null;
        }
        mContent.clear();
    }


    /**
     * Checks, Whether article is Favorite or not
     *
     * @param context
     * @param articleBean
     * @param favStartImg
     * @param toggleLikeDisLikeImg
     */
    private void premium_isFavOrLike(Context context, ArticleBean articleBean, final ImageView favStartImg, final ImageView toggleLikeDisLikeImg) {
        ApiManager.isExistFavNdLike(context, articleBean.getArticleId())
                .subscribe(likeVal -> {
                    int like = (int) likeVal;
                    if (articleBean != null) {
                        articleBean.setIsFavourite(like);
                    }
                    favStartImg.setVisibility(View.VISIBLE);
                    toggleLikeDisLikeImg.setVisibility(View.VISIBLE);
                    favStartImg.setEnabled(true);
                    toggleLikeDisLikeImg.setEnabled(true);
                    if (like == NetConstants.LIKE_NEUTRAL) {
                        favStartImg.setImageResource(R.drawable.ic_like_unselected);
                        toggleLikeDisLikeImg.setImageResource(R.drawable.ic_switch_off_copy);
                    } else if (like == NetConstants.LIKE_YES) {
                        favStartImg.setImageResource(R.drawable.ic_like_selected);
                        toggleLikeDisLikeImg.setImageResource(R.drawable.ic_switch_off_copy);
                    } else if (like == NetConstants.LIKE_NO) {
                        favStartImg.setImageResource(R.drawable.ic_like_unselected);
                        toggleLikeDisLikeImg.setImageResource(R.drawable.ic_switch_on_copy);
                    }

                }, val -> {
                    Log.i("", "");
                });
    }

    private void premium_updateBookmarkFavLike(ProgressBar bar, ImageView imageView, final Context context, int position, ArticleBean bean, String from) {
        if (bar != null) {
            bar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            imageView.setEnabled(false);
        }
        int bookmark = bean.getIsBookmark();
        int favourite = bean.getIsFavourite();
        if (from.equals("bookmark")) {
            if (bean.getIsBookmark() == NetConstants.BOOKMARK_YES) {
                bookmark = NetConstants.BOOKMARK_NO;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Removed Read Lated : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            } else {
                bookmark = NetConstants.BOOKMARK_YES;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Added Read Later : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            }
        } else if (from.equals("favourite")) {
            if (bean.getIsFavourite() == NetConstants.LIKE_NEUTRAL) {
                favourite = NetConstants.LIKE_YES;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Favourite Added : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            } else if (bean.getIsFavourite() == NetConstants.LIKE_NO) {
                favourite = NetConstants.LIKE_YES;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Favourite Added : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            } else {
                favourite = NetConstants.LIKE_NEUTRAL;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Favourite Removed : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            }
        } else if (from.equals("dislike")) {
            if (bean.getIsFavourite() == NetConstants.LIKE_NO) {
                favourite = NetConstants.LIKE_NEUTRAL;
            } else if (bean.getIsFavourite() == NetConstants.LIKE_NEUTRAL) {
                favourite = NetConstants.LIKE_NO;
            } else if (bean.getIsFavourite() == NetConstants.LIKE_YES) {
                favourite = NetConstants.LIKE_NO;
            }

        }

        final int book = bookmark;
        final int fav = favourite;
        final String articleId = bean.getArticleId();

        ApiManager.getUserProfile(imageView.getContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userProfile -> {
                    // To Create and Remove at server end
                    ApiManager.createBookmarkFavLike(userProfile.getAuthorization(), mUserId, BuildConfig.SITEID, articleId, book, fav)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(val -> {
                                        if (val) {
                                            bean.setIsFavourite(fav);
                                            bean.setIsBookmark(book);
                                            if (from.equals("bookmark")) {
                                                if (book == NetConstants.BOOKMARK_YES) {
                                                    bean.setGroupType(NetConstants.GROUP_PREMIUM_SECTIONS);
                                                    // To Create at App end
                                                    ApiManager.createBookmark(context, bean).subscribe(boole -> {
                                                        if (bar != null) {
                                                            bar.setVisibility(View.GONE);
                                                            imageView.setVisibility(View.VISIBLE);
                                                            imageView.setEnabled(true);
                                                        }
                                                        notifyItemChanged(position);
                                                        CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "NetConstants.BOOKMARK_YES");
                                                    });

                                                    Alerts.showToastAtCenter(context, "Added to Read Later");
                                                } else if (book == NetConstants.BOOKMARK_NO) {
                                                    // To Remove at App end
                                                    ApiManager.createUnBookmark(context, bean.getArticleId()).subscribe(boole -> {
                                                        if (bar != null) {
                                                            bar.setVisibility(View.GONE);
                                                            imageView.setVisibility(View.VISIBLE);
                                                            imageView.setEnabled(true);
                                                        }
                                                        // If user is in bookmark then item should be removed.
                                                        if (mFrom.equals(NetConstants.API_bookmarks)) {
                                                            deletedContentModel = mContent.remove(position);
                                                            deletedPosition = position;
                                                            notifyItemRemoved(position);
                                                            notifyItemRangeChanged(position, mContent.size());
                                                            // Empty Check Call back
                                                            checkPageEmptyCallback();
                                                        } else {
                                                            notifyItemChanged(position);
                                                        }
                                                        CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "NetConstants.BOOKMARK_NO");
                                                    });

                                                }
                                            } else if (from.equals("dislike") || from.equals("favourite")) {
                                                if (book == NetConstants.BOOKMARK_YES) {
                                                    // To Update at App end
                                                    ApiManager.updateBookmark(context, bean.getArticleId(), fav).subscribe(boole ->
                                                            Log.i("updateBookmark", "true")
                                                    );
                                                }
                                                // To Update at App end
                                                ApiManager.updateLike(context, bean.getArticleId(), fav).subscribe(boole -> {

                                                    if (fav == NetConstants.LIKE_YES) {
                                                        Alerts.showToastAtCenter(context, "You will see more stories like this.");
                                                        notifyItemChanged(position);
                                                        CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "NetConstants.LIKE_YES");
//                                            notifyDataSetChanged();
                                                    } else if (fav == NetConstants.LIKE_NO) {
//                                    Alerts.showToastAtCenter(context, "Show fewer stories like this.");
                                                        if (mFrom.equals(NetConstants.API_bookmarks)) {
                                                            notifyItemChanged(position);
//                                                notifyDataSetChanged();
                                                        } else {
                                                            // This is for UNDO functionality
                                                            if (bar != null) {
                                                                // SnackBar Initializing
                                                                snackbar = Snackbar.make(mRecyclerView, R.string.item_removed_message, 3000)
                                                                        .setAction(R.string.undo, new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                //This is for UNDO functionality
                                                                                if (deletedContentModel != null && deletedContentModel.getBean() != null) {
                                                                                    premium_updateBookmarkFavLike(null, null, context,
                                                                                            deletedPosition, deletedContentModel.getBean(), "dislike");
                                                                                    CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "UNDO");
                                                                                }
                                                                            }
                                                                        });
                                                                // SnackBar Displaying and managing bottom margin
                                                                displaySnackBarWithBottomMargin(getSnackbar());
                                                            }
                                                            deletedContentModel = mContent.remove(position);
                                                            deletedPosition = position;
                                                            //notifyDataSetChanged();
                                                            notifyItemRemoved(position);
                                                            notifyItemRangeChanged(position, mContent.size());
                                                            // Empty Check Call back
                                                            checkPageEmptyCallback();
                                                        }
                                                        CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "NetConstants.LIKE_NO");

                                                    } else {
                                                        //This is for UNDO functionality, from SnackBar
                                                        if (deletedContentModel != null) {
                                                            boolean contains = mContent.contains(deletedContentModel);
                                                            if (!contains) {
                                                                deletedContentModel.setBean(bean);
                                                                AppTabContentModel model = new AppTabContentModel(deletedContentModel.getViewType());
                                                                model.setBean(bean);
                                                                mContent.add(deletedPosition, model);
                                                                deletedContentModel = null;
                                                                notifyItemInserted(deletedPosition);
                                                                notifyItemRangeChanged(deletedPosition, mContent.size());
                                                            }
                                                        } else {
                                                            notifyItemChanged(position);
                                                        }
                                                    }

                                                });
                                            }

                                        } else {
                                            notifyItemChanged(position);
                                        }
                                    },
                                    val -> {
                                        if (imageView != null && imageView.getContext() != null) {
                                            notifyItemChanged(position);
                                            Alerts.showSnackbar((Activity) imageView.getContext(), imageView.getContext().getResources().getString(R.string.something_went_wrong));
//                            Alerts.showAlertDialogOKBtn(imageView.getContext(), imageView.getContext().getResources().getString(R.string.failed_to_connect), imageView.getContext().getResources().getString(R.string.please_check_ur_connectivity));
                                        }
                                    }
                            );
                });


    }

    public int getDescriptionItemPosition() {
        return mDescriptionItemPosition;
    }

    public int getLastDescriptionTextSize() {
        return mDescriptionTextSize;
    }

    private OnEditionBtnClickListener mOnEditionBtnClickListener;

    public void setOnEditionBtnClickListener(OnEditionBtnClickListener onEditionBtnClickListener) {
        mOnEditionBtnClickListener = onEditionBtnClickListener;
    }

    private AppTabContentModel deletedContentModel;
    private int deletedPosition = -1;

    public static void displaySnackBarWithBottomMargin(Snackbar snackbar) {
        final View snackBarView = snackbar.getView();
        int sideMargin = 0;
        int marginBottom = snackBarView.getContext().getResources().getDimensionPixelSize(R.dimen.snackbarBottomMargin);

        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();

        params.setMargins(params.leftMargin + sideMargin,
                params.topMargin,
                params.rightMargin + sideMargin,
                params.bottomMargin + marginBottom);

        snackBarView.setLayoutParams(params);
        snackbar.show();
    }


    public void deleteIndex(int index) {
        mContent.remove(index);
        notifyDataSetChanged();
    }

    public List<AppTabContentModel> getAllContent() {
        return mContent;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

}
