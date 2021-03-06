package com.ns.activity;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.ArticleBean;
import com.netoperation.net.ApiManager;
import com.netoperation.net.DefaultTHApiManager;
import com.netoperation.util.NetConstants;
import com.ns.alerts.Alerts;
import com.ns.callbacks.THP_AppEmptyPageListener;
import com.ns.clevertap.CleverTapUtil;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;
import com.ns.view.img.BaseImgView;

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter {

    public static final int VT_NORMAL = 1;
    public static final int VT_DASHBOARD = 2;
    public static final int VT_BRIEFCASE = 3;
    public static final int VT_TRENDING = 4;
    public static final int VT_BOOKMARK_PREMIUM = 5;
    public static final int VT_ADD_NEW_ADDRESS = 6;
    public static final int VT_LOADMORE = 7;

    public static final int VT_PREMIUM_DETAIL_DESCRIPTION_WEBVIEW = 8;
    public static final int VT_PREMIUM_DETAIL_IMAGE_BANNER = 9;
    public static final int VT_DETAIL_VIDEO_PLAYER= 10;
    public static final int VT_DETAIL_AUDIO_PLAYER= 11;
    public static final int VT_HEADER = 12;

    public static final int VT_THD_BANNER = 13;
    public static final int VT_THD_WIDGET_News_In_Quotes = 14;
    public static final int VT_THD_WIDGET_Top_Picks = 15;
    public static final int VT_THD_WIDGET_Editorials_Opinion = 16;
    public static final int VT_THD_WIDGET_Multimedia = 17;
    public static final int VT_THD_WIDGET_Cartoons = 18;
    public static final int VT_THD_WIDGET_DEFAULT = 19;
    public static final int VT_THD_300X250_ADS = 20;
    public static final int VT_TABOOLA_LISTING_ADS = 218;
    public static final int VT_THD_DEFAULT_ROW = 22;

    public static final int VT_BLD_BANNER = 23;
    public static final int VT_BLD_WIDGET_DEFAULT = 24;
    public static final int VT_BLD_DEFAULT_ROW = 25;

    public static final int VT_WEB_WIDGET = 26;
    public static final int VT_THD_HORIZONTAL_LIST = 27;

    public static final int VT_GROUP_DEFAULT_DETAIL_IMAGE_BANNER = 28;
    public static final int VT_GROUP_DEFAULT_DETAIL_DESCRIPTION_WEBVIEW = 29;
    public static final int VT_TABOOLA_WIDGET = 30;
    public static final int VT_GROUP_DEFAULT_DETAIL_RESTRICTED_DESCRIPTION_WEBVIEW = 31;
    public static final int VT_THD_SEARCH_ROW = 32;
    public static final int VT_THD_PHOTO_VIEW = 33;
    public static final int VT_POST_COMMENT_BTN_VIEW = 34;
    public static final int VT_RELATED_ARTICLE_HEADER = 35;
    public static final int VT_BL_SENSEX = 36;

    private final int SECTION_APP_EXCLUSIVE = THPConstants.APP_EXCLUSIVE_SECTION_ID;

    public static final int WIDGET_LAYOUT_H_LIST = 301;
    public static final int WIDGET_LAYOUT_GRID = 302;
    public static final int WIDGET_LAYOUT_PAGER = 303;

    public static final int GroupHeader_MEDIA_ActionAtLeft = 201;
    public static final int GroupHeader_MEDIA_ActionAtCenter = 202;

    public static final int GroupHeader_TEXT_ActionAtLeft = 203;
    public static final int GroupHeader_TEXT_ActionAtCenter = 204;

    public static final int GroupHeader_MEDIA_TEXT_ActionAtLeft = 205;
    public static final int GroupHeader_MEDIA_TEXT_ActionAtCenter = 206;

    public static final int GroupHeader_TITLE_TEXT_ActionAtLeft = 207;
    public static final int GroupHeader_TITLE_TEXT_ActionAtCenter = 208;

    public static final int GroupHeader_MEDIA_TITLE_TIME_ActionAtLeft = 209;
    public static final int GroupHeader_MEDIA_TITLE_TIME_ActionAtCenter = 210;

    public static final int GroupHeader_MEDIA = 211;

    public static final int GroupHeader_MEDIA_TextOverlay = 212;



    private THP_AppEmptyPageListener appEmptyPageListener;

    public void setAppEmptyPageListener(THP_AppEmptyPageListener appEmptyPageListener) {
        this.appEmptyPageListener = appEmptyPageListener;
    }



    /**
     * Shows Article Type Image
     * @param articleType
     * @param articleTypeimageView
     */
    protected void articleTypeImage(String articleType, ArticleBean articleBean, ImageView articleTypeimageView) {
        if(articleType != null) {
            if(articleType.equalsIgnoreCase(THPConstants.ARTICLE_TYPE_ARTICLE)) {
                articleTypeimageView.setVisibility(View.GONE);
            }
            else if(articleType.equalsIgnoreCase(THPConstants.ARTICLE_TYPE_AUDIO)) {
                articleTypeimageView.setVisibility(View.VISIBLE);
                articleTypeimageView.setImageResource(R.drawable.podcasts);
            }
            else if(articleType.equalsIgnoreCase(THPConstants.ARTICLE_TYPE_PHOTO)) {
                articleTypeimageView.setVisibility(View.VISIBLE);
                articleTypeimageView.setImageResource(R.drawable.photos);
            }
            else if(articleType.equalsIgnoreCase(THPConstants.ARTICLE_TYPE_VIDEO)) {
                articleTypeimageView.setVisibility(View.VISIBLE);
                if(articleBean.getVIDEO_URL() != null && !TextUtils.isEmpty(articleBean.getVIDEO_URL())) {
                    articleTypeimageView.setImageResource(R.drawable.video);
                } else if (articleBean.getVid() != null && !TextUtils.isEmpty(articleBean.getVid())) {
                    articleTypeimageView.setImageResource(R.drawable.video);
                } else {
                    articleTypeimageView.setImageResource(R.drawable.yt_play_big);
                }
            }
            else if(articleType.equalsIgnoreCase(THPConstants.ARTICLE_TYPE_YOUTUBE_VIDEO)) {
                articleTypeimageView.setImageResource(R.drawable.yt_play_big);
                articleTypeimageView.setVisibility(View.VISIBLE);
            }
            else {
                articleTypeimageView.setVisibility(View.GONE);
            }
        }
        else {
            articleTypeimageView.setVisibility(View.GONE);
        }
    }

    protected boolean isVideo(String articleType, ArticleBean articleBean) {
        if(articleType != null && articleType.equalsIgnoreCase(THPConstants.ARTICLE_TYPE_VIDEO)) {
            return articleBean.getVIDEO_URL() != null && !TextUtils.isEmpty(articleBean.getVIDEO_URL());

        }
        else return articleType != null && articleType.equalsIgnoreCase(THPConstants.ARTICLE_TYPE_YOUTUBE_VIDEO);
    }

    protected boolean isYoutubeVideo(String articleType) {
        return articleType != null && articleType.equalsIgnoreCase(THPConstants.ARTICLE_TYPE_YOUTUBE_VIDEO);
    }


    /**
     * Checks, Visible Article is bookmarked or not.
     * @param context
     * @param articleBean
     * @param imageView1
     */
    protected void isExistInBookmark(Context context, ArticleBean articleBean, final BaseImgView imageView1) {
        ApiManager.isExistInBookmark(context, articleBean.getArticleId())
                .subscribe(bean -> {
                    ArticleBean bean1 = bean;
                    if (articleBean != null) {
                        articleBean.setIsBookmark(bean1.getIsBookmark());
                    }
                    imageView1.setVisibility(View.VISIBLE);
                    imageView1.setEnabled(true);
                    if(THPConstants.IS_USE_SEVER_THEME) {
                        if (bean1.getArticleId() != null && bean1.getArticleId().equals(articleBean.getArticleId())) {
                            // 3 = app:iconType="bookmarked"
                            imageView1.updateIcon(3);
                        } else {
                            // 4 = app:iconType="unbookmark"
                            imageView1.updateIcon(4);
                        }
                    }
                    else {
                        if (bean1.getArticleId() != null && bean1.getArticleId().equals(articleBean.getArticleId())) {
                            imageView1.setImageResource(R.drawable.ic_bookmark_selected);
                        } else {
                            imageView1.setImageResource(R.drawable.ic_bookmark_unselected);
                        }
                    }


                }, val -> {
                    Log.i("", "");
                });
    }

    /**
     * Makes dim to read article's row
     * @param context
     * @param articleId
     * @param view
     */
    protected void dimReadArticle(Context context, String articleId, View view) {
        DefaultTHApiManager.isReadArticleId(context, articleId)
                .subscribe(tableRead -> {
                    Log.i("", "");
                    if (tableRead != null) {
                        view.setAlpha(.7f);
                    } else {
                        view.setAlpha(1f);
                    }
                }, throwable -> {
                    Log.i("", "");
                });
    }

    /**
     * Removes Article from App, BookmarkTable in local DB
     * @param context
     * @param articleId
     * @param bean
     * @param bar
     * @param imageView
     * @param position
     */
    protected void local_removeBookmarkFromApp(Context context, String articleId, ArticleBean bean, ProgressBar bar, ImageView imageView, int position, String mFrom) {
        // To Remove at App end
        ApiManager.createUnBookmark(context, bean.getArticleId()).subscribe(boole -> {
            if (bar != null) {
                bar.setVisibility(View.GONE);
            }
            imageView.setVisibility(View.VISIBLE);
            imageView.setEnabled(true);

            notifyItemChanged(position);
            // Empty Check Call back
            checkPageEmptyCallback();
            CleverTapUtil.cleverTapBookmarkFavLike(context, articleId, mFrom, "NetConstants.BOOKMARK_NO");
        });
    }


    protected void local_createBookmarkFromApp(Context context, ArticleBean bean, ProgressBar bar, ImageView imageView, int position, String mFrom) {
        // To Create at App end
        ApiManager.createBookmark(context, bean).subscribe(boole -> {
            if (bar != null) {
                bar.setVisibility(View.GONE);
            }
            imageView.setVisibility(View.VISIBLE);
            imageView.setEnabled(true);
            notifyItemChanged(position);
            CleverTapUtil.cleverTapBookmarkFavLike(context, bean.getArticleId(), mFrom, "NetConstants.BOOKMARK_YES");
        });
    }


    protected void local_bookmarkOperation(Context context, ArticleBean bean, ImageView imageView, int position) {
        ApiManager.isExistInBookmark(context, bean.getArticleId())
                .subscribe(articleBean->{
                    bean.setGroupType(NetConstants.G_BOOKMARK_DEFAULT);
                    if(bean.getArticleId().equals(articleBean.getArticleId())) {
                        local_removeBookmarkFromApp(context, bean.getArticleId(), bean, null, imageView, position, NetConstants.G_BOOKMARK_DEFAULT);
                    } else {
                        local_createBookmarkFromApp(context, bean, null, imageView, position, NetConstants.G_BOOKMARK_DEFAULT);
                        Alerts.showToastAtCenter(context, context.getResources().getString(R.string.added_to_read_later));
                    }
                });
    }

    protected void checkPageEmptyCallback() {
        if (appEmptyPageListener != null) {
            appEmptyPageListener.checkPageEmpty();
        }
    }
}
