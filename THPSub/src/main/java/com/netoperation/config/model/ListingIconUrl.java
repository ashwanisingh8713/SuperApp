package com.netoperation.config.model;

import android.webkit.URLUtil;

public class ListingIconUrl {
    /**
     * bookmark : url
     * unbookmark : url
     * like : url
     * dislike : url
     * favorite : url
     * unfavorite : url
     */

    private String bookmark;
    private String unbookmark;
    private String like;
    private String dislike;
    private String favorite;
    private String unfavorite;

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getUnbookmark() {
        return unbookmark;
    }

    public void setUnbookmark(String unbookmark) {
        this.unbookmark = unbookmark;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getDislike() {
        return dislike;
    }

    public void setDislike(String dislike) {
        this.dislike = dislike;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getUnfavorite() {
        return unfavorite;
    }

    public void setUnfavorite(String unfavorite) {
        this.unfavorite = unfavorite;
    }
}