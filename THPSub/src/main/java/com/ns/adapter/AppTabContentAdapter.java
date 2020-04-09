package com.ns.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.util.NetConstants;
import com.netoperation.util.THPPreferences;
import com.netoperation.util.UserPref;
import com.ns.activity.BaseRecyclerViewAdapter;
import com.ns.alerts.Alerts;
import com.ns.callbacks.OnEditionBtnClickListener;
import com.ns.callbacks.THP_AppEmptyPageListener;
import com.ns.clevertap.CleverTapUtil;
import com.ns.model.AppTabContentModel;
import com.ns.thpremium.BuildConfig;
import com.ns.thpremium.R;
import com.ns.utils.CommonUtil;
import com.ns.utils.ContentUtil;
import com.ns.utils.GlideUtil;
import com.ns.utils.IntentUtil;
import com.ns.utils.NetUtils;
import com.ns.utils.ResUtil;
import com.ns.utils.THPConstants;
import com.ns.utils.THPFirebaseAnalytics;
import com.ns.utils.WebViewLinkClick;
import com.ns.view.THP_AutoResizeWebview;
import com.ns.viewholder.BookmarkViewHolder;
import com.ns.viewholder.BriefcaseViewHolder;
import com.ns.viewholder.BriefingHeaderViewHolder;
import com.ns.viewholder.DashboardViewHolder;
import com.ns.viewholder.DetailBannerViewHolder;
import com.ns.viewholder.DetailDescriptionWebViewHolder;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AppTabContentAdapter extends BaseRecyclerViewAdapter {
    private List<AppTabContentModel> mContent;
    private String mFrom;
    private String mUserId;

    /** This holds description webview position in recyclerview.
     * This is being used to notify recyclerview on TextSize change. */
    private int mDescriptionItemPosition;
    private int mLastDescriptionTextSize;

    private THP_AppEmptyPageListener appEmptyPageListener;

    private Snackbar snackbar;

    public Snackbar getSnackbar() {
        return snackbar;
    }

    private RecyclerView mRecyclerView;

    public void setAppEmptyPageListener(THP_AppEmptyPageListener appEmptyPageListener) {
        this.appEmptyPageListener = appEmptyPageListener;
    }

    public void setFrom(String from) {
        mFrom = from;
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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if(viewType == VT_HEADER) {
            return new BriefingHeaderViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_briefing_header, viewGroup, false));
        }
        else if(viewType == VT_DASHBOARD) {
            return new DashboardViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.apptab_item_dashboard, viewGroup, false));
        }
        else if(viewType == VT_TRENDING) {
            return new DashboardViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.apptab_item_dashboard, viewGroup, false));
        }
        else if(viewType == VT_BOOKMARK) {
            return new DashboardViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.apptab_item_bookmark, viewGroup, false));
        }
        else if(viewType == VT_BRIEFCASE) {
            return new BriefcaseViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.apptab_item_briefcase, viewGroup, false));
        }
        else if(viewType == VT_LOADMORE) {
            return new BookmarkViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loadmore, viewGroup, false));
        }
        else if(viewType == VT_DETAIL_DESCRIPTION_WEBVIEW) {
            return new DetailDescriptionWebViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_detail_description_webview, viewGroup, false));
        }

        else if(viewType == VT_DETAIL_IMAGE_BANNER) {
            return new DetailBannerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_detail_banner_item, viewGroup, false));
        }
        else if(viewType == VT_DETAIL_VIDEO_PLAYER) {

        }
        else if(viewType == VT_DETAIL_AUDIO_PLAYER) {

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        final ArticleBean bean = mContent.get(position).getBean();

        if(viewHolder instanceof DashboardViewHolder) {
            ui_Dash_Tren_Book_Populate(viewHolder, bean, position);
        }
        else if(viewHolder instanceof BriefcaseViewHolder) {
            ui_Briefing_Populate(viewHolder, bean, position);
        }
        else if (viewHolder instanceof DetailBannerViewHolder) {
            ui_detail_banner(viewHolder, bean);
        }
        else if(viewHolder instanceof DetailDescriptionWebViewHolder) {
            ui_detail_description(viewHolder, bean, position);
        }
        else if(viewHolder instanceof BriefingHeaderViewHolder) {
            BriefingHeader(viewHolder, bean);
        }

    }


    private void ui_Dash_Tren_Book_Populate(RecyclerView.ViewHolder viewHolder, ArticleBean bean, int position) {
        DashboardViewHolder holder = (DashboardViewHolder) viewHolder;
        if(mFrom.equalsIgnoreCase(NetConstants.RECO_trending)) {
            holder.trendingIcon_Img.setVisibility(View.VISIBLE);
        } else {
            holder.trendingIcon_Img.setVisibility(View.GONE);
        }

//        GlideUtil.loadImage(holder.image.getContext(), holder.image, ContentUtil.getThumbUrl(bean.getThumbnailUrl()), R.drawable.th_ph_01);
        GlideUtil.loadImage(holder.image.getContext(), holder.image, ContentUtil.getThumbUrl(bean.getThumbnailUrl()), R.drawable.th_ph_01);
        holder.authorName_Txt.setText(ContentUtil.getAuthor(bean.getAuthor()));
        holder.title.setText(bean.getArticletitle());
        // Section Name
        String sectionName = bean.getArticleSection();
        if(sectionName == null || TextUtils.isEmpty(sectionName)) {
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

        // In Bookmark page we have to hide like-Dislike and toggle Imgs icon
        if(mFrom.equalsIgnoreCase(NetConstants.RECO_bookmarks)) {
            holder.like_Img.setVisibility(View.GONE);
            holder.toggleBtn_Img.setVisibility(View.GONE);
        } else {
            isFavOrLike(holder.like_Img.getContext(), bean, holder.like_Img, holder.toggleBtn_Img);
        }

        isExistInBookmark(holder.bookmark_Img.getContext(), bean, holder.bookmark_Img);

        holder.bookmark_Img.setOnClickListener(v-> {
                    if (NetUtils.isConnected(v.getContext())) {
                        updateBookmarkFavLike(holder.bookmarkProgressBar, holder.bookmark_Img, holder.bookmark_Img.getContext(), position, bean, "bookmark");
                    } else {
                        Alerts.noConnectionSnackBar(v, (AppCompatActivity) v.getContext());
                    }
                }
        );

        holder.like_Img.setOnClickListener(v->{
                    if (NetUtils.isConnected(v.getContext())) {
                        updateBookmarkFavLike(holder.likeProgressBar, holder.like_Img, holder.like_Img.getContext(), position, bean, "favourite");
                    } else {
                        Alerts.noConnectionSnackBar(v, (AppCompatActivity) v.getContext());
                    }
                }
        );

        holder.toggleBtn_Img.setOnClickListener(v-> {
                    if (NetUtils.isConnected(v.getContext())) {
                        updateBookmarkFavLike(holder.toggleBtnProgressBar, holder.toggleBtn_Img, holder.toggleBtn_Img.getContext(), position, bean, "dislike");
                    } else {
                        Alerts.noConnectionSnackBar(v, (AppCompatActivity) v.getContext());
                    }
                }
        );

        holder.itemView.setOnClickListener(v-> {
            if(THPPreferences.getInstance(holder.itemView.getContext()).isUserAdsFree()){
                IntentUtil.openDetailActivity(holder.itemView.getContext(), mFrom,
                        bean.getArticleUrl(), position, bean.getArticleId());
            }else{
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
     * @param viewHolder
     * @param bean
     * @param position
     */
    private void ui_Briefing_Populate(RecyclerView.ViewHolder viewHolder, ArticleBean bean, int position) {
        BriefcaseViewHolder holder = (BriefcaseViewHolder) viewHolder;
        GlideUtil.loadImage(holder.image.getContext(), holder.image, ContentUtil.getBreifingImgUrl(bean.getThumbnailUrl()), R.drawable.th_ph_02);
        holder.authorName_Txt.setText(ContentUtil.getAuthor(bean.getAuthor()));
        holder.title.setText(ResUtil.htmlText(bean.getArticletitle()));

        // Section name
        String sectionName = bean.getArticleSection();
        if(sectionName == null || TextUtils.isEmpty(sectionName)) {
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
     * Detail Page Banner UI
     * @param viewHolder
     * @param bean
     */
    private void ui_detail_banner(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        DetailBannerViewHolder holder = (DetailBannerViewHolder) viewHolder;
        final String articleType = bean.getArticletype();
        // To shows Article Type Image
        articleTypeImage(articleType, bean, holder.articleTypeimageView);

        String sectionName = bean.getArticleSection();
        if(sectionName == null || TextUtils.isEmpty(sectionName)) {
            sectionName = bean.getSectionName();
        }

        sectionName = ResUtil.capitalizeFirstLetter(sectionName);

        holder.tv_section.setText(sectionName);

        String authors = CommonUtil.getAutors(bean.getAuthor());
        if(authors == null) {
            holder.tv_author_name.setVisibility(View.GONE);
        } else {
            holder.tv_author_name.setText(authors);
            holder.tv_author_name.setVisibility(View.VISIBLE);
        }

        String location = bean.getLocation();
        if(location == null || TextUtils.isEmpty(location)) {
            holder.tv_city_name.setVisibility(View.GONE);
        }
        else {
            holder.tv_city_name.setVisibility(View.VISIBLE);
            holder.tv_city_name.setText(location);

        }

        String timeToRead = bean.getTimeToRead();
        if(timeToRead == null || timeToRead.equalsIgnoreCase("0") || TextUtils.isEmpty(timeToRead)) {
            holder.tv_time.setVisibility(View.GONE);
        }else {
            holder.tv_time.setText(timeToRead);
            holder.tv_time.setVisibility(View.VISIBLE);
        }

        // Publish Date
        String formatedPubDt = CommonUtil.fomatedDate(bean.getPubDateTime(), mFrom);
        if(formatedPubDt == null || TextUtils.isEmpty(formatedPubDt)) {
            holder.tv_updated_time.setVisibility(View.GONE);
        }
        else {
            holder.tv_updated_time.setText(formatedPubDt);
            holder.tv_updated_time.setVisibility(View.VISIBLE);
        }

        holder.tv_title.setText(bean.getArticletitle());

        if(ContentUtil.getBannerUrl(bean.getThumbnailUrl()).equalsIgnoreCase("") ) {
            holder.imageView.setVisibility(View.GONE);
            holder.tv_caption.setVisibility(View.GONE);
            holder.shadowOverlay.setVisibility(View.GONE);
        }
        else {
            holder.imageView.setVisibility(View.VISIBLE);
            GlideUtil.loadImage(holder.itemView.getContext(), holder.imageView, ContentUtil.getBannerUrl(bean.getThumbnailUrl()), R.drawable.th_ph_02);

            String caption = null;
            if(bean.getIMAGES() != null && bean.getIMAGES().size() > 0) {
                caption = bean.getIMAGES().get(0).getCa();
            }

            if(caption != null && !TextUtils.isEmpty(caption.trim())) {
                holder.shadowOverlay.setVisibility(View.VISIBLE);
                holder.tv_caption.setVisibility(View.VISIBLE);
                holder.tv_caption.setText(ResUtil.htmlText(caption));
            } else {
                holder.shadowOverlay.setVisibility(View.GONE);
                holder.tv_caption.setVisibility(View.GONE);
            }


        }

        // Banner Image Click Listener
        holder.imageView.setOnClickListener(v-> {
            if (isVideo(articleType, bean)) {
                if (isYoutubeVideo(articleType)) {
                    if(bean.getYoutubeVideoId() == null || ResUtil.isEmpty(bean.getYoutubeVideoId())) {
                        Alerts.showSnackbar((Activity)holder.imageView.getContext(), "Video link not found.");
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

    private void ui_detail_description(RecyclerView.ViewHolder viewHolder, ArticleBean bean, int position) {
        DetailDescriptionWebViewHolder holder = (DetailDescriptionWebViewHolder) viewHolder;
        mLastDescriptionTextSize = UserPref.getInstance(holder.mLeadTxt.getContext()).getDescriptionSize();

        // Enabling Weblink click on Lead Text
        new WebViewLinkClick().linkClick(holder.mLeadTxt, holder.itemView.getContext(), bean.getArticleId());

        holder.mLeadTxt.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.shoWebTextDescription(holder.webview.getContext(), bean.getArticletitle(), true),
                "text/html", "UTF-8", null);
        holder.mLeadTxt.setSize(mLastDescriptionTextSize);

        holder.webview.setSize(mLastDescriptionTextSize);

        // Enabling Weblink click on Description
        new WebViewLinkClick().linkClick(holder.webview, holder.itemView.getContext(), bean.getArticleId());

        holder.webview.loadDataWithBaseURL("https:/", THP_AutoResizeWebview.shoWebTextDescription(holder.webview.getContext(), bean.getDescription(), false),
                "text/html", "UTF-8", null);

        mDescriptionItemPosition = position;

        holder.mLeadTxt.animate()
                //.translationY(holder.mLeadTxt.getHeight())
                .alpha(1.0f)
                .setDuration(1000);

        holder.webview.animate()
                .translationY(holder.webview.getHeight())
                .alpha(1.0f)
                .setDuration(1000);


    }

    private void BriefingHeader(RecyclerView.ViewHolder viewHolder, ArticleBean bean) {
        BriefingHeaderViewHolder holder = (BriefingHeaderViewHolder) viewHolder;
        holder.userName_Txt.setText(bean.getTitle());
        if(mFrom.equalsIgnoreCase(NetConstants.RECO_Mystories)) {
            holder.yourEditionFor_Txt.setText(bean.getSectionName());
            holder.yourEditionFor_Txt.setVisibility(View.VISIBLE);
            holder.editionBtn_Txt.setVisibility(View.GONE);
        }
        else if(mFrom.equalsIgnoreCase(NetConstants.BREIFING_ALL) || mFrom.equalsIgnoreCase(NetConstants.BREIFING_MORNING)
                || mFrom.equalsIgnoreCase(NetConstants.BREIFING_NOON) || mFrom.equalsIgnoreCase(NetConstants.BREIFING_EVENING)) {
            holder.editionBtn_Txt.setText(bean.getSectionName());
            holder.editionBtn_Txt.setVisibility(View.VISIBLE);
            holder.yourEditionFor_Txt.setVisibility(View.VISIBLE);
            holder.yourEditionFor_Txt.setText("Today's Briefing");
        }
        else if(mFrom.equalsIgnoreCase(NetConstants.RECO_suggested) || mFrom.equalsIgnoreCase(NetConstants.RECO_trending)) {
            holder.yourEditionFor_Txt.setText(bean.getSectionName());
            holder.yourEditionFor_Txt.setVisibility(View.VISIBLE);
            holder.editionBtn_Txt.setVisibility(View.GONE);
        }

        boolean isDayTheme = UserPref.getInstance(holder.editionBtn_Txt.getContext()).isUserThemeDay();
        if(isDayTheme) {
            holder.editionBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ResUtil.getBackgroundDrawable(holder.editionBtn_Txt.getContext().getResources(), R.drawable.ic_edition_dropdown), null);
        } else {
            holder.editionBtn_Txt.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    ResUtil.getBackgroundDrawable(holder.editionBtn_Txt.getContext().getResources(), R.drawable.ic_edition_dropdown_white), null);
        }


        holder.editionBtn_Txt.setOnClickListener(v->{
            if(mOnEditionBtnClickListener != null) {
                mOnEditionBtnClickListener.OnEditionBtnClickListener();
            }
        });

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
        notifyDataSetChanged();
    }

    public void replaceData(ArticleBean articleBean, int position) {
        if(position < mContent.size()) {
            mContent.get(position).setBean(articleBean);
        }
        notifyDataSetChanged();
    }

    public void clearData() {
        mContent.clear();
    }


    /**
     * Checks, Visible Article is bookmarked or not.
     * @param context
     * @param articleBean
     * @param imageView1
     */
    private void isExistInBookmark(Context context, ArticleBean articleBean, final ImageView imageView1) {
        ApiManager.isExistInBookmark(context, articleBean.getArticleId())
                .subscribe(bean-> {
                    ArticleBean bean1 = bean;
                    if(articleBean != null) {
                        articleBean.setIsBookmark(bean1.getIsBookmark());
                    }
                    imageView1.setVisibility(View.VISIBLE);
                    imageView1.setEnabled(true);
                    if (bean1.getArticleId() != null && bean1.getArticleId().equals(articleBean.getArticleId())) {
                        imageView1.setImageResource(R.drawable.ic_bookmark_selected);
                    } else {
                        imageView1.setImageResource(R.drawable.ic_bookmark_unselected);

                    }

                }, val->{
                    Log.i("", "");
                });
    }

    private void isFavOrLike(Context context, ArticleBean articleBean, final ImageView favStartImg, final ImageView toggleLikeDisLikeImg) {
        ApiManager.isExistFavNdLike(context, articleBean.getArticleId())
                .subscribe(likeVal-> {
                    int like = (int)likeVal;
                    if(articleBean != null) {
                        articleBean.setIsFavourite(like);
                    }
                    favStartImg.setVisibility(View.VISIBLE);
                    toggleLikeDisLikeImg.setVisibility(View.VISIBLE);
                    favStartImg.setEnabled(true);
                    toggleLikeDisLikeImg.setEnabled(true);
                    if(like == NetConstants.LIKE_NEUTRAL) {
                        favStartImg.setImageResource(R.drawable.ic_like_unselected);
                        toggleLikeDisLikeImg.setImageResource(R.drawable.ic_switch_off_copy);
                    }
                    else if(like == NetConstants.LIKE_YES) {
                        favStartImg.setImageResource(R.drawable.ic_like_selected);
                        toggleLikeDisLikeImg.setImageResource(R.drawable.ic_switch_off_copy);
                    }
                    else if(like == NetConstants.LIKE_NO) {
                        favStartImg.setImageResource(R.drawable.ic_like_unselected);
                        toggleLikeDisLikeImg.setImageResource(R.drawable.ic_switch_on_copy);
                    }

                }, val->{
                    Log.i("", "");
                });
    }


    private void updateBookmarkFavLike(ProgressBar bar, ImageView imageView, final Context context, int position, ArticleBean bean
            , String from) {
        if(bar != null) {
            bar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.INVISIBLE);
            imageView.setEnabled(false);
        }
        int bookmark = bean.getIsBookmark();
        int favourite = bean.getIsFavourite();
        if(from.equals("bookmark")) {
            if(bean.getIsBookmark() == NetConstants.BOOKMARK_YES) {
                bookmark = NetConstants.BOOKMARK_NO;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Removed Read Lated : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            }
            else {
                bookmark = NetConstants.BOOKMARK_YES;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Added Read Later : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            }
        }
        else if(from.equals("favourite")) {
            if(bean.getIsFavourite() == NetConstants.LIKE_NEUTRAL) {
                favourite = NetConstants.LIKE_YES;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Favourite Added : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            }
            else if(bean.getIsFavourite() == NetConstants.LIKE_NO) {
                favourite = NetConstants.LIKE_YES;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Favourite Added : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            }
            else {
                favourite = NetConstants.LIKE_NEUTRAL;
                THPFirebaseAnalytics.setFirbaseAnalyticsEvent(context, "Action", ResUtil.capitalizeFirstLetter(mFrom) + " : Favourite Removed : " + bean.getArticleId() + " : " + bean.getTitle(), ResUtil.capitalizeFirstLetter(mFrom) + " List Screen");
            }
        }
        else if(from.equals("dislike")) {
            if(bean.getIsFavourite() == NetConstants.LIKE_NO) {
                favourite = NetConstants.LIKE_NEUTRAL;
            }
            else if(bean.getIsFavourite() == NetConstants.LIKE_NEUTRAL) {
                favourite = NetConstants.LIKE_NO;
            } else if(bean.getIsFavourite() == NetConstants.LIKE_YES) {
                favourite = NetConstants.LIKE_NO;
            }

        }

        final int book = bookmark;
        final int fav = favourite;
        final String articleId = bean.getArticleId();

        // To Create and Remove at server end
        ApiManager.createBookmarkFavLike(mUserId, BuildConfig.SITEID, articleId, bookmark, favourite)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(val-> {
                            if (val) {
                                bean.setIsFavourite(fav);
                                bean.setIsBookmark(book);
                                if(from.equals("bookmark")) {
                                    if(book == NetConstants.BOOKMARK_YES) {
                                        // To Create at App end
                                        ApiManager.createBookmark(context, bean).subscribe(boole -> {
                                            if(bar != null) {
                                                bar.setVisibility(View.GONE);
                                                imageView.setVisibility(View.VISIBLE);
                                                imageView.setEnabled(true);
                                            }
                                            notifyItemChanged(position);
//                                            notifyDataSetChanged();
                                            CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "NetConstants.BOOKMARK_YES");
                                        });

                                        Alerts.showToastAtCenter(context, "Added to Read Later");
                                    }
                                    else if(book == NetConstants.BOOKMARK_NO) {
                                            // To Remove at App end
                                            ApiManager.createUnBookmark(context, bean.getArticleId()).subscribe(boole -> {
                                                if (bar != null) {
                                                    bar.setVisibility(View.GONE);
                                                    imageView.setVisibility(View.VISIBLE);
                                                    imageView.setEnabled(true);
                                                }
                                                // If user is in bookmark then item should be removed.
                                                if(mFrom.equals(NetConstants.RECO_bookmarks)) {
                                                    deletedContentModel = mContent.remove(position);
                                                    deletedPosition = position;
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, mContent.size());

                                                    // Empty Check Call back
                                                    checkPageEmptyCallback();

                                                } else {
//                                                    notifyDataSetChanged();
                                                    notifyItemChanged(position);
                                                }
                                                CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "NetConstants.BOOKMARK_NO");
                                            });

                                    }
                                }
                                else if(from.equals("dislike") || from.equals("favourite")) {
                                    if(book == NetConstants.BOOKMARK_YES) {
                                        // To Update at App end
                                        ApiManager.updateBookmark(context, bean.getArticleId(), fav).subscribe(boole ->
                                                Log.i("updateBookmark", "true")
                                        );
                                    }
                                    // To Update at App end
                                    ApiManager.updateLike(context, bean.getArticleId(), fav).subscribe(boole -> {

                                        if(fav == NetConstants.LIKE_YES) {
                                            Alerts.showToastAtCenter(context, "You will see more stories like this.");
                                            notifyItemChanged(position);
                                            CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "NetConstants.LIKE_YES");
//                                            notifyDataSetChanged();
                                        }
                                        else if(fav == NetConstants.LIKE_NO) {
//                                    Alerts.showToastAtCenter(context, "Show fewer stories like this.");
                                            if(mFrom.equals(NetConstants.RECO_bookmarks)) {
                                                notifyItemChanged(position);
//                                                notifyDataSetChanged();
                                            }
                                            else {
                                                // This is for UNDO functionality
                                                if (bar != null) {
                                                    // SnackBar Initializing
                                                    snackbar = Snackbar.make(mRecyclerView, R.string.item_removed_message, 3000)
                                                            .setAction(R.string.undo, new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    //This is for UNDO functionality
                                                                    if(deletedContentModel != null && deletedContentModel.getBean() != null) {
                                                                        updateBookmarkFavLike(null, null, context,
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
                                            if(deletedContentModel != null) {
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

                            }
                            else {
                                notifyItemChanged(position);
                            }
                        },
                        val-> {
                            if(imageView != null && imageView.getContext() != null) {
                                notifyItemChanged(position);
                                Alerts.showSnackbar((Activity) imageView.getContext(), imageView.getContext().getResources().getString(R.string.something_went_wrong));
//                            Alerts.showAlertDialogOKBtn(imageView.getContext(), imageView.getContext().getResources().getString(R.string.failed_to_connect), imageView.getContext().getResources().getString(R.string.please_check_ur_connectivity));
                            }
                        }
                );
    }

    public int getDescriptionItemPosition() {
        return mDescriptionItemPosition;
    }

    public int getLastDescriptionTextSize() {
        return mLastDescriptionTextSize;
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

    private void checkPageEmptyCallback() {
        if(appEmptyPageListener != null) {
            appEmptyPageListener.checkPageEmpty();
        }
    }

    public void deleteIndex(int index) {
        mContent.remove(index);
        notifyDataSetChanged();
    }

    public List<AppTabContentModel> getAllContent() {
        return mContent;
    }

}
