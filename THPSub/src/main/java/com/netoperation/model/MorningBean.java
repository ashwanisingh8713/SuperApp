package com.netoperation.model;

import java.util.ArrayList;
import java.util.List;

public class MorningBean {
    /**
     * articleId : 10397693
     * author : ["Baskaran"]
     * sectionName : News
     * publishedDate : Thu, 6 Jun 2019 12:17:30 +0530
     * originalDate : Mon, 18 Mar 2019 15:34:14 +0530
     * location :
     * title : BL CHANGEMAKERS 2019: AWARD WINNERS
     * articleLink : https://alphath.thehindu.co.in/news/bl-changemakers-2019-award-winners/article10397693.ece
     * gmt : Thu, 6 Jun 2019 12:17:30 +0530
     * media : [{"image":"https://alphath.thehindu.co.in/todays-paper/tp-features/tp-weekend/rpk5h1/article10397555.ece/ALTERNATES/FREE_660/newMPTemplateGOA5HF84J4jpgjpg","caption":"Caption for picture"}]
     * youtubeVideoId : ZyrkqybFL2Y
     * description :
     * shortDescription :
     * leadText : Lead text : BL CHANGEMAKERS 2019: AWARD WINNERS
     * videoId :
     * articleType : embedYoutube
     * thumbnailUrl : https://alphath.thehindu.co.in/todays-paper/tp-features/tp-weekend/rpk5h1/article10397555.ece/ALTERNATES/FREE_660/newMPTemplateGOA5HF84J4jpgjpg
     * timeToRead :
     */

    private String articleId;
    private String sectionName;
    private String publishedDate;
    private String originalDate;
    private String location;
    private String title;
    private String articleLink;
    private String gmt;
    private String youtubeVideoId;
    private String description;
    private String shortDescription;
    private String leadText;
    private String videoId;
    private String articleType;
    private String thumbnailUrl;
    private String timeToRead;
    private List<String> author;
    private ArrayList<MeBean> media;

    public String getArticleId() {
        return articleId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getOriginalDate() {
        return originalDate;
    }

    public void setOriginalDate(String originalDate) {
        this.originalDate = originalDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getGmt() {
        return gmt;
    }

    public void setGmt(String gmt) {
        this.gmt = gmt;
    }

    public String getYoutubeVideoId() {
        return youtubeVideoId;
    }

    public void setYoutubeVideoId(String youtubeVideoId) {
        this.youtubeVideoId = youtubeVideoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLeadText() {
        return leadText;
    }

    public void setLeadText(String leadText) {
        this.leadText = leadText;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTimeToRead() {
        return timeToRead;
    }

    public void setTimeToRead(String timeToRead) {
        this.timeToRead = timeToRead;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public ArrayList<MeBean> getMedia() {
        return media;
    }


}
