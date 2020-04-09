package com.ns.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.netoperation.model.ArticleBean;
import com.ns.thpremium.R;
import com.ns.utils.THPConstants;

public abstract class BaseRecyclerViewAdapter extends RecyclerView.Adapter {

    public static final int VT_NORMAL = 1;
    public static final int VT_DASHBOARD = 2;
    public static final int VT_BRIEFCASE = 3;
    public static final int VT_TRENDING = 4;
    public static final int VT_BOOKMARK = 5;
    public static final int VT_ADD_NEW_ADDRESS = 6;
    public static final int VT_LOADMORE = 7;

    public static final int VT_DETAIL_DESCRIPTION_WEBVIEW = 8;
    public static final int VT_DETAIL_IMAGE_BANNER= 9;
    public static final int VT_DETAIL_VIDEO_PLAYER= 10;
    public static final int VT_DETAIL_AUDIO_PLAYER= 11;
    public static final int VT_HEADER = 12;


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
                }
                else {
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

}
