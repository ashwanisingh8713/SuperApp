package com.netoperation.model;

import java.util.ArrayList;
import java.util.List;

public class SearchedArticleModel {


    /**
     * status : 1
     * da : 2019-05-20 15:04:29
     * yd : 2019-05-19 15:04:29
     * s : 1
     * sid : 44
     * sname : Pushnotification
     * data : [{"aid":27178918,"au":"PTI","sid":"44","sname":"Pushnotification","parentId":"0","parentName":"","pid":"0","opid":1000,"pd":"2019-05-19 21:20:20","od":"2019-05-19 21:14:01","location":"Amaravati, ","ti":"Since 1999, most exit polls have gone wrong, says Vice-President Venkaiah Naidu","al":"https://www.thehindu.com/elections/lok-sabha-2019/exit-polls-are-not-exact-polls-vice-president-venkaiah-naidu/article27178918.ece","bk":"false","gmt":"2019-05-19 15:50:20","me":[{"im":"https://www.thehindu.com/elections/lok-sabha-2019/hsur5y/article27178917.ece/ALTERNATES/FREE_660/VICE-PRESIDENT","im_v2":"https://www.thehindu.com/elections/lok-sabha-2019/hsur5y/article27178917.ece/ALTERNATES/FREE_660/VICE-PRESIDENT","ca":"Vice President M. Venkaiah Naidu addresses a meeting at the Guntur Club in Guntur on May 19, 2019."}],"hi":1,"youtube_video_id":"","audioLink":"","weblink":"","de":"<p> Vice-President M. Venkaiah Naidu has mocked at the exit polls&#44; saying they were not exact polls. \u201cExit polls do not mean exact polls. We have to understand that. Since 1999&#44; most of the exit polls have gone wrong&#44;\u201d the Vice-President pointed out.<\/p><p>Mr. Naidu addressed an informal meeting of well-wishers on May 19&#44; who felicitated him in Guntur. <\/p><p>Referring to the ongoing general elections&#44; he said every party exuded confidence (over victory). \u201cEveryone exhibits his own confidence till the 23rd (day of counting). There will be no base for it. So we have to wait for 23rd&#44;\u201d he remarked.<\/p><p>\u201cCountry and the State need an able leader and stable government&#44; whoever it be. That\u2019s what is required. That's all&#44;\u201d Mr. Naidu observed.<\/p><p>The Vice-President also said change in society should start with political parties.<\/p><p>  <\/p><p>\u201cIf democracy has to strengthen and something good has to happen to people elections&#44; selections&#44; candidates&#44; parties all should discharge their duties responsibly and properly&#44;\u201d he noted.<\/p><p>The Vice-President lamented that civility has become a casualty in the present political discourse. \u201cThere is a lot of degeneration in the speeches of political leaders. They are resorting to personal abuses. One is not an enemy to the other in politics&#44; they are only rivals... They are forgetting this basic fact&#44;\u201d he said.<\/p><p>Expressing anguish over the behaviour of elected representatives in Parliament and state legislatures&#44; he said&#44; \u201cSee how MPs are behaving in Parliament and MLAs in Assembly&#44; irrespective of the parties. Panchayat and civic bodies\u2019 members follow them.\u201d<\/p><p>The Vice-President also found fault with political parties announcing freebies to win over the electorate.<\/p><p>\u201cThe way parties are behaving.. you have been given a mandate for five years. You have to work. Without doing that&#44; you announce freebies at the last minute. I am always opposed to it. Free power means&#44; no power&#44;\u201d Mr. Naidu observed.<\/p>","short_de":"\n \n Vice-President M. Venkaiah Naidu has mocked at the exit polls&#44; saying they were not exact polls. \u201cExit polls do not mean exact polls. We have to understand that. Since 1999&#44; most of the ","le":"\u201cExit polls do not mean exact polls. We have to understand that. Since 1999, most of the exit polls have gone wrong,\u201d the Vice-President pointed out.","add_pos":376,"vid":"","parent_section_id":"152","sub_section_id":"160","sub_section_name":"Lok Sabha Election 2019","articleType":"article","im_thumbnail":"https://www.thehindu.com/elections/lok-sabha-2019/hsur5y/article27178917.ece/ALTERNATES/FREE_660/VICE-PRESIDENT","im_thumbnail_v2":"https://www.thehindu.com/elections/lok-sabha-2019/hsur5y/article27178917.ece/ALTERNATES/FREE_660/VICE-PRESIDENT","comm_count":"0","sections":[],"rn":[]}]
     */

    private int status;
    private String da;
    private String yd;
    private int s;
    private String sid;
    private String sname;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDa() {
        return da;
    }

    public void setDa(String da) {
        this.da = da;
    }

    public String getYd() {
        return yd;
    }

    public void setYd(String yd) {
        this.yd = yd;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public String getSid() {
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * aid : 27178918
         * au : PTI
         * sid : 44
         * sname : Pushnotification
         * parentId : 0
         * parentName :
         * pid : 0
         * opid : 1000
         * pd : 2019-05-19 21:20:20
         * od : 2019-05-19 21:14:01
         * location : Amaravati,
         * ti : Since 1999, most exit polls have gone wrong, says Vice-President Venkaiah Naidu
         * al : https://www.thehindu.com/elections/lok-sabha-2019/exit-polls-are-not-exact-polls-vice-president-venkaiah-naidu/article27178918.ece
         * bk : false
         * gmt : 2019-05-19 15:50:20
         * me : [{"im":"https://www.thehindu.com/elections/lok-sabha-2019/hsur5y/article27178917.ece/ALTERNATES/FREE_660/VICE-PRESIDENT","im_v2":"https://www.thehindu.com/elections/lok-sabha-2019/hsur5y/article27178917.ece/ALTERNATES/FREE_660/VICE-PRESIDENT","ca":"Vice President M. Venkaiah Naidu addresses a meeting at the Guntur Club in Guntur on May 19, 2019."}]
         * hi : 1
         * youtube_video_id :
         * audioLink :
         * weblink :
         * de : <p> Vice-President M. Venkaiah Naidu has mocked at the exit polls&#44; saying they were not exact polls. “Exit polls do not mean exact polls. We have to understand that. Since 1999&#44; most of the exit polls have gone wrong&#44;” the Vice-President pointed out.</p><p>Mr. Naidu addressed an informal meeting of well-wishers on May 19&#44; who felicitated him in Guntur. </p><p>Referring to the ongoing general elections&#44; he said every party exuded confidence (over victory). “Everyone exhibits his own confidence till the 23rd (day of counting). There will be no base for it. So we have to wait for 23rd&#44;” he remarked.</p><p>“Country and the State need an able leader and stable government&#44; whoever it be. That’s what is required. That's all&#44;” Mr. Naidu observed.</p><p>The Vice-President also said change in society should start with political parties.</p><p>  </p><p>“If democracy has to strengthen and something good has to happen to people elections&#44; selections&#44; candidates&#44; parties all should discharge their duties responsibly and properly&#44;” he noted.</p><p>The Vice-President lamented that civility has become a casualty in the present political discourse. “There is a lot of degeneration in the speeches of political leaders. They are resorting to personal abuses. One is not an enemy to the other in politics&#44; they are only rivals... They are forgetting this basic fact&#44;” he said.</p><p>Expressing anguish over the behaviour of elected representatives in Parliament and state legislatures&#44; he said&#44; “See how MPs are behaving in Parliament and MLAs in Assembly&#44; irrespective of the parties. Panchayat and civic bodies’ members follow them.”</p><p>The Vice-President also found fault with political parties announcing freebies to win over the electorate.</p><p>“The way parties are behaving.. you have been given a mandate for five years. You have to work. Without doing that&#44; you announce freebies at the last minute. I am always opposed to it. Free power means&#44; no power&#44;” Mr. Naidu observed.</p>
         * short_de :

         Vice-President M. Venkaiah Naidu has mocked at the exit polls&#44; saying they were not exact polls. “Exit polls do not mean exact polls. We have to understand that. Since 1999&#44; most of the
         * le : “Exit polls do not mean exact polls. We have to understand that. Since 1999, most of the exit polls have gone wrong,” the Vice-President pointed out.
         * add_pos : 376
         * vid :
         * parent_section_id : 152
         * sub_section_id : 160
         * sub_section_name : Lok Sabha Election 2019
         * articleType : article
         * im_thumbnail : https://www.thehindu.com/elections/lok-sabha-2019/hsur5y/article27178917.ece/ALTERNATES/FREE_660/VICE-PRESIDENT
         * im_thumbnail_v2 : https://www.thehindu.com/elections/lok-sabha-2019/hsur5y/article27178917.ece/ALTERNATES/FREE_660/VICE-PRESIDENT
         * comm_count : 0
         * sections : []
         * rn : []
         */

        private int aid;
        private String au;
        private String sid;
        private String sname;
        private String parentId;
        private String parentName;
        private String pid;
        private String opid;
        private String pd;
        private String od;
        private String location;
        private String ti;
        private String al;
        private String bk;
        private String gmt;
        private String hi;
        private String youtube_video_id;
        private String audioLink;
        private String weblink;
        private String de;
        private String short_de;
        private String le;
        private String add_pos;
        private String vid;
        private String parent_section_id;
        private String sub_section_id;
        private String sub_section_name;
        private String articleType;
        private String im_thumbnail;
        private String im_thumbnail_v2;
        private String comm_count;
        private ArrayList<MeBean> me;
        private List<?> sections;
        private List<?> rn;

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getAu() {
            return au;
        }

        public void setAu(String au) {
            this.au = au;
        }

        public String getSid() {
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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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

        public String getGmt() {
            return gmt;
        }

        public void setGmt(String gmt) {
            this.gmt = gmt;
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
            return weblink;
        }

        public void setWeblink(String weblink) {
            this.weblink = weblink;
        }

        public String getDe() {
            return de;
        }

        public void setDe(String de) {
            this.de = de;
        }

        public String getShort_de() {
            return short_de;
        }

        public void setShort_de(String short_de) {
            this.short_de = short_de;
        }

        public String getLe() {
            return le;
        }

        public void setLe(String le) {
            this.le = le;
        }

        public String getAdd_pos() {
            return add_pos;
        }

        public void setAdd_pos(String add_pos) {
            this.add_pos = add_pos;
        }

        public String getVid() {
            return vid;
        }

        public void setVid(String vid) {
            this.vid = vid;
        }

        public String getParent_section_id() {
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

        public String getArticleType() {
            return articleType;
        }

        public void setArticleType(String articleType) {
            this.articleType = articleType;
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

        public List<?> getSections() {
            return sections;
        }

        public void setSections(List<?> sections) {
            this.sections = sections;
        }


    }
}
