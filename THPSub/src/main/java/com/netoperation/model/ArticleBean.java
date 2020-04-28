package com.netoperation.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.ns.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;

public class ArticleBean implements Parcelable {



    /**
     * articleId : 27130581
     * articletitle : In 2018, 207 Indians gave up citizenship
     * articleSection : national
     * articleUrl : https://www.thehindu.com/news/national/in-2018-207-indians-gave-up-citizenship/article27130581.ece
     * thumbnailUrl : ["https://www.thehindu.com/news/national/65tzh9/article25705034.ece/BINARY/thumbnail/TH10PASSPORT"]
     * pubDate : 2019-05-14
     * pubDateTime : May 14, 2019 10:45:39 PM
     * author : ["Vijaita Singh"]
     * rank : 1
     * recotype : trending
     * articletype : story
     */

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // TH Default Response Fields
    private String aid;
    private String au;
    private String sid;
    private String sname;
    private String parentId;
    private String parentName;
    private String pid;
    private String opid;
    private String pd;
    private String od;
    private String ti;
    private String al;
    private String bk;
    private String hi;
    private String youtube_video_id;
    private String audioLink;
    private String weblink;
    private String short_de;
    private String le;
    private int add_pos;
    private int p4_pos;
    private String vid;
    private String parent_section_id;
    private String sub_section_id;
    private String sub_section_name;
    private String im_thumbnail;
    private String im_thumbnail_v2;
    private String comm_count;
    private String groupType;
    private boolean isArticleRestricted;
    /** This is for Article Description (Default, Article search by id) */
    private String de;

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Common Fields
    private String location;
    private String gmt;
    private String articleType;
    private boolean isRead;

    ////////////////////////////////////////////////////////////////////////////////////////////////


    /** This is for splitted description*/
    private String description2;
    /** This is for breifing*/
    private String description;
    private String articleId;
    private String leadText;

    private String commentCount;
    private String AUDIO_URL;
    private String VIDEO_URL;
    private ArrayList<MeBean> IMAGES;

    private String articletitle;
    private String articleSection;
    private String articleUrl;
    private String pubDate;
    private String pubDateTime;
    private int rank;
    private String recotype;
    private String articletype;
    private List<String> thumbnailUrl;
    private List<String> author;
    private int isFavourite;
    private int isBookmark;
    private int hasDescription;

    private int isPremium;
    private String sectionName;
    private String publishedDate;
    private String originalDate;
    private String title;
    private String articleLink;
    private String youtubeVideoId;
    private String shortDescription;
    private String videoId;
    private String timeToRead;
    private ArrayList<MeBean> media;

    private String timeForBriefing;

    public boolean isArticleRestricted() {
        return isArticleRestricted;
    }

    public void setArticleRestricted(boolean articleRestricted) {
        isArticleRestricted = articleRestricted;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    private ArrayList<MeBean> me;

    private List<ArticleBean> rn;

    public List<ArticleBean> getRn() {
        return rn;
    }

    public void setRn(List<ArticleBean> rn) {
        this.rn = rn;
    }


    public String getAid() {
        if(aid == null) {
            return articleId;
        }
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAu() {
        return au;
    }

    public void setAu(String au) {
        this.au = au;
    }

    public String getSid() {
        if(sid == null) {
            return parent_section_id;
        }
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOpid() {
        return opid;
    }

    public void setOpid(String opid) {
        this.opid = opid;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getOd() {
        return od;
    }

    public void setOd(String od) {
        this.od = od;
    }

    public String getTi() {
        return ti;
    }

    public void setTi(String ti) {
        this.ti = ti;
    }

    public String getAl() {
        return al;
    }

    public void setAl(String al) {
        this.al = al;
    }

    public String getBk() {
        return bk;
    }

    public void setBk(String bk) {
        this.bk = bk;
    }

    public String getHi() {
        return hi;
    }

    public void setHi(String hi) {
        this.hi = hi;
    }

    public String getYoutube_video_id() {
        return youtube_video_id;
    }

    public void setYoutube_video_id(String youtube_video_id) {
        this.youtube_video_id = youtube_video_id;
    }

    public String getAudioLink() {
        return audioLink;
    }

    public void setAudioLink(String audioLink) {
        this.audioLink = audioLink;
    }

    public String getWeblink() {
        if(weblink == null) {
            return articleLink;
        }
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    public String getDe() {
        if(de == null) {
            return description;
        }
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getShort_de() {
        if(short_de == null) {
            return shortDescription;
        }
        return short_de;
    }

    public void setShort_de(String short_de) {
        this.short_de = short_de;
    }

    public String getLe() {
        if(le == null) {
            return leadText;
        }
        return le;
    }

    public void setLe(String le) {
        this.le = le;
    }

    public int getAdd_pos() {
        return add_pos;
    }

    public void setAdd_pos(int add_pos) {
        this.add_pos = add_pos;
    }

    public int getP4_pos() {
        return p4_pos;
    }

    public void setP4_pos(int p4_pos) {
        this.p4_pos = p4_pos;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getParent_section_id() {
        if(parent_section_id == null) {
            return sid;
        }
        return parent_section_id;
    }

    public void setParent_section_id(String parent_section_id) {
        this.parent_section_id = parent_section_id;
    }

    public String getSub_section_id() {
        return sub_section_id;
    }

    public void setSub_section_id(String sub_section_id) {
        this.sub_section_id = sub_section_id;
    }

    public String getSub_section_name() {
        return sub_section_name;
    }

    public void setSub_section_name(String sub_section_name) {
        this.sub_section_name = sub_section_name;
    }

    public String getIm_thumbnail() {
        return im_thumbnail;
    }

    public void setIm_thumbnail(String im_thumbnail) {
        this.im_thumbnail = im_thumbnail;
    }

    public String getIm_thumbnail_v2() {
        return im_thumbnail_v2;
    }

    public void setIm_thumbnail_v2(String im_thumbnail_v2) {
        this.im_thumbnail_v2 = im_thumbnail_v2;
    }

    public String getComm_count() {
        return comm_count;
    }

    public void setComm_count(String comm_count) {
        this.comm_count = comm_count;
    }

    public ArrayList<MeBean> getMe() {
        return me;
    }

    public void setMe(ArrayList<MeBean> me) {
        this.me = me;
    }




    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(int isPremium) {
        this.isPremium = isPremium;
    }


    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getTimeForBriefing() {
        return timeForBriefing;
    }

    public void setTimeForBriefing(String timeForBriefing) {
        this.timeForBriefing = timeForBriefing;
    }

    public ArrayList<MeBean> getIMAGES() {
        return IMAGES;
    }

    public void setIMAGES(ArrayList<MeBean> IMAGES) {
        this.IMAGES = IMAGES;
    }

    public ArrayList<MeBean> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<MeBean> media) {
        this.media = media;
    }

    public String getAUDIO_URL() {
        return AUDIO_URL;
    }

    public void setAUDIO_URL(String AUDIO_URL) {
        this.AUDIO_URL = AUDIO_URL;
    }

    public String getVIDEO_URL() {
        return VIDEO_URL;
    }

    public void setVIDEO_URL(String VIDEO_URL) {
        this.VIDEO_URL = VIDEO_URL;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getLeadText() {
        if(leadText == null) {
            return le;
        }
        return leadText;
    }

    public void setLeadText(String leadText) {
        this.leadText = leadText;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public int getIsBookmark() {
        return isBookmark;
    }

    public void setIsBookmark(int isBookmark) {
        this.isBookmark = isBookmark;
    }

    public int getHasDescription() {
        if((!ResUtil.isEmpty(description)) || (!ResUtil.isEmpty(description2)) || (!ResUtil.isEmpty(de))){
            return 1;
        }
        return hasDescription;
    }

    public void setHasDescription(int hasDescription) {
        this.hasDescription = hasDescription;
    }

    public String getArticleId() {
        if(articleId == null) {
            return aid;
        }
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticletitle() {
        if(articletitle == null || TextUtils.isEmpty(articletitle)) {
            return title;
        }
        return articletitle;
    }

    public String getTitle() {
        if(title == null || TextUtils.isEmpty(title)) {
            if(articletitle == null || TextUtils.isEmpty(articletitle)) {
                return ti;
            }
            return articletitle;
        }
        return title;
    }

    public void setArticletitle(String articletitle) {
        this.articletitle = articletitle;
    }

    public String getArticleSection() {
        if(articleSection == null) {
            return sectionName;
        }
        return articleSection;
    }

    public void setArticleSection(String articleSection) {
        this.articleSection = articleSection;
    }

    public String getArticleUrl() {
        if(articleUrl == null) {
            if(articleLink == null) {
                return al;
            }
            return articleLink;
        }
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getPubDate() {
        if(pubDate == null) {
            return pd;
        }
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getPubDateTime() {
        return pubDateTime;
    }

    public void setPubDateTime(String pubDateTime) {
        this.pubDateTime = pubDateTime;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getRecotype() {
        return recotype;
    }

    public void setRecotype(String recotype) {
        this.recotype = recotype;
    }

    public String getArticletype() {
        if(articletype == null) {
            return articleType;
        }
        return articletype;
    }

    public void setArticletype(String articletype) {
        this.articletype = articletype;
    }

    public List<String> getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(List<String> thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public String getDescription() {
        if(description == null) {
            return de;
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public ArticleBean() {
    }

    public String getSectionName() {
        if(sectionName == null) {
            return articleSection;
        }
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



    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticleLink() {
        if(articleLink == null) {
            if(articleUrl == null) {
                if(weblink == null) {
                    return al;
                }
                return weblink;
            }
            return articleUrl;
        }
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

    public String getShortDescription() {
        if(shortDescription == null) {
            return short_de;
        }
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getArticleType() {
        if(articleType == null || TextUtils.isEmpty(articleType)) {
            return articletype;
        }
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getTimeToRead() {
        return timeToRead;
    }

    public void setTimeToRead(String timeToRead) {
        this.timeToRead = timeToRead;
    }



    @Override
    public boolean equals(Object obj) {
        super.equals(obj);

        if(obj instanceof ArticleBean) {
            ArticleBean bean = (ArticleBean) obj;
            return bean.getArticleId().equals(getArticleId());
        }

        return false;
    }

    @Override
    public int hashCode() {
        super.hashCode();
        return getArticleId().hashCode();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.aid);
        dest.writeString(this.au);
        dest.writeString(this.sid);
        dest.writeString(this.sname);
        dest.writeString(this.parentId);
        dest.writeString(this.parentName);
        dest.writeString(this.pid);
        dest.writeString(this.opid);
        dest.writeString(this.pd);
        dest.writeString(this.od);
        dest.writeString(this.ti);
        dest.writeString(this.al);
        dest.writeString(this.bk);
        dest.writeString(this.hi);
        dest.writeString(this.youtube_video_id);
        dest.writeString(this.audioLink);
        dest.writeString(this.weblink);
        dest.writeString(this.short_de);
        dest.writeString(this.le);
        dest.writeInt(this.add_pos);
        dest.writeInt(this.p4_pos);
        dest.writeString(this.vid);
        dest.writeString(this.parent_section_id);
        dest.writeString(this.sub_section_id);
        dest.writeString(this.sub_section_name);
        dest.writeString(this.im_thumbnail);
        dest.writeString(this.im_thumbnail_v2);
        dest.writeString(this.comm_count);
        dest.writeString(this.groupType);
        dest.writeByte(this.isArticleRestricted ? (byte) 1 : (byte) 0);
        dest.writeString(this.de);
        dest.writeString(this.location);
        dest.writeString(this.gmt);
        dest.writeString(this.articleType);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeString(this.description2);
        dest.writeString(this.description);
        dest.writeString(this.articleId);
        dest.writeString(this.leadText);
        dest.writeString(this.commentCount);
        dest.writeString(this.AUDIO_URL);
        dest.writeString(this.VIDEO_URL);
        dest.writeTypedList(this.IMAGES);
        dest.writeString(this.articletitle);
        dest.writeString(this.articleSection);
        dest.writeString(this.articleUrl);
        dest.writeString(this.pubDate);
        dest.writeString(this.pubDateTime);
        dest.writeInt(this.rank);
        dest.writeString(this.recotype);
        dest.writeString(this.articletype);
        dest.writeStringList(this.thumbnailUrl);
        dest.writeStringList(this.author);
        dest.writeInt(this.isFavourite);
        dest.writeInt(this.isBookmark);
        dest.writeInt(this.hasDescription);
        dest.writeInt(this.isPremium);
        dest.writeString(this.sectionName);
        dest.writeString(this.publishedDate);
        dest.writeString(this.originalDate);
        dest.writeString(this.title);
        dest.writeString(this.articleLink);
        dest.writeString(this.youtubeVideoId);
        dest.writeString(this.shortDescription);
        dest.writeString(this.videoId);
        dest.writeString(this.timeToRead);
        dest.writeTypedList(this.media);
        dest.writeString(this.timeForBriefing);
        dest.writeTypedList(this.me);
        dest.writeList(this.rn);
    }

    protected ArticleBean(Parcel in) {
        this.aid = in.readString();
        this.au = in.readString();
        this.sid = in.readString();
        this.sname = in.readString();
        this.parentId = in.readString();
        this.parentName = in.readString();
        this.pid = in.readString();
        this.opid = in.readString();
        this.pd = in.readString();
        this.od = in.readString();
        this.ti = in.readString();
        this.al = in.readString();
        this.bk = in.readString();
        this.hi = in.readString();
        this.youtube_video_id = in.readString();
        this.audioLink = in.readString();
        this.weblink = in.readString();
        this.short_de = in.readString();
        this.le = in.readString();
        this.add_pos = in.readInt();
        this.p4_pos = in.readInt();
        this.vid = in.readString();
        this.parent_section_id = in.readString();
        this.sub_section_id = in.readString();
        this.sub_section_name = in.readString();
        this.im_thumbnail = in.readString();
        this.im_thumbnail_v2 = in.readString();
        this.comm_count = in.readString();
        this.groupType = in.readString();
        this.isArticleRestricted = in.readByte() != 0;
        this.de = in.readString();
        this.location = in.readString();
        this.gmt = in.readString();
        this.articleType = in.readString();
        this.isRead = in.readByte() != 0;
        this.description2 = in.readString();
        this.description = in.readString();
        this.articleId = in.readString();
        this.leadText = in.readString();
        this.commentCount = in.readString();
        this.AUDIO_URL = in.readString();
        this.VIDEO_URL = in.readString();
        this.IMAGES = in.createTypedArrayList(MeBean.CREATOR);
        this.articletitle = in.readString();
        this.articleSection = in.readString();
        this.articleUrl = in.readString();
        this.pubDate = in.readString();
        this.pubDateTime = in.readString();
        this.rank = in.readInt();
        this.recotype = in.readString();
        this.articletype = in.readString();
        this.thumbnailUrl = in.createStringArrayList();
        this.author = in.createStringArrayList();
        this.isFavourite = in.readInt();
        this.isBookmark = in.readInt();
        this.hasDescription = in.readInt();
        this.isPremium = in.readInt();
        this.sectionName = in.readString();
        this.publishedDate = in.readString();
        this.originalDate = in.readString();
        this.title = in.readString();
        this.articleLink = in.readString();
        this.youtubeVideoId = in.readString();
        this.shortDescription = in.readString();
        this.videoId = in.readString();
        this.timeToRead = in.readString();
        this.media = in.createTypedArrayList(MeBean.CREATOR);
        this.timeForBriefing = in.readString();
        this.me = in.createTypedArrayList(MeBean.CREATOR);
        this.rn = new ArrayList<ArticleBean>();
        in.readList(this.rn, ArticleBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ArticleBean> CREATOR = new Parcelable.Creator<ArticleBean>() {
        @Override
        public ArticleBean createFromParcel(Parcel source) {
            return new ArticleBean(source);
        }

        @Override
        public ArticleBean[] newArray(int size) {
            return new ArticleBean[size];
        }
    };
}
