package com.netoperation.model;

public class UserChoice {


    /**
     * contentId : 3866
     * bookmark : 1
     * favourite : 0
     * bookmarkDate : 2019-01-24
     */

    private String contentId;
    private int bookmark;
    private int favourite;
    private String bookmarkDate;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public String getBookmarkDate() {
        return bookmarkDate;
    }

    public void setBookmarkDate(String bookmarkDate) {
        this.bookmarkDate = bookmarkDate;
    }
}
