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
    private List<ArticleBean> data;

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

    public List<ArticleBean> getData() {
        return data;
    }

    public void setData(List<ArticleBean> data) {
        this.data = data;
    }


}
