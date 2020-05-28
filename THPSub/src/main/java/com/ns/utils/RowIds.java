package com.ns.utils;

public class RowIds {

    public static String rowId_widget(String secId) {
        return "widget_" + secId;
    }


    public static String rowId_staticWebPage(String secId, int position) {
        return "staticWebpage_" + secId + "_" + position;
    }

    public static String rowId_banner(String secId) {
        return "banner_" + secId ;
    }

    public static String adDataUiqueId(int index, String adId) {
        return index + "_" + "_" + adId;
    }

    public static String rowId_defaultArticle(String articleId) {
        return "defaultRow_" + articleId;
    }

    public static String rowId_subSection(String sectionId) {
        return "subsection_" + sectionId;
    }

    public static String rowId_loadMore(String sectionId) {
        return "loadMore_" + sectionId;
    }

    public static String rowId_photoModel() {
        return "photoModel";
    }

    public static String rowId_videoModel() {
        return "videoModel";
    }

    public static String rowId_audioModel() {
        return "audioModel";
    }

    public static String rowId_bannerModel() {
        return "bannerModel";
    }

    public static String rowId_description_1() {
        return "description_1_Model";
    }

    public static String rowId_description_2() {
        return "description_2_Model";
    }

    public static String rowId_postCommentBtn() {
        return "postCommentBtn";
    }

    public static String rowId_taboolaModel() {
        return "taboolaModel";
    }


}