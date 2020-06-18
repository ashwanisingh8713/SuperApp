package com.netoperation.db;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netoperation.config.model.ContentUrl;
import com.netoperation.config.model.ImportantMsg;
import com.netoperation.config.model.OtherIconsDownloadUrls;
import com.netoperation.config.model.PlaceHolder;
import com.netoperation.config.model.WidgetIndex;
import com.netoperation.default_db.TableOptional;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionBean;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.model.UserProfile;
import com.netoperation.config.model.AdsBean;
import com.netoperation.config.model.AppThemeBean;
import com.netoperation.config.model.SearchOptionBean;
import com.netoperation.config.model.TaboolaBean;
import com.netoperation.config.model.TabsBean;
import com.netoperation.config.model.UrlBean;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class Converters {

    @TypeConverter
    public static List<ArticleBean> stringToBeanList(String value) {
        Type listType = new TypeToken<List<ArticleBean>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String beanListToString(List<ArticleBean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    /////////////////////////// Start List<SectionBean>
    @TypeConverter
    public static List<SectionBean> stringToSectionList(String value) {
        Type listType = new TypeToken<List<SectionBean>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String sectionListToString(List<SectionBean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    //////////////////////////// End List<SectionBean>

    //////////////////////////// Start ArticleBean
    @TypeConverter
    public static String recobeanToString(ArticleBean articleBean) {
        Gson gson = new Gson();
        String json = gson.toJson(articleBean);
        return json;
    }

    @TypeConverter
    public static ArticleBean stringToRecobean(String value) {
        Type listType = new TypeToken<ArticleBean>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    //////////////////////////// End ArticleBean

    //////////////////////////// Start SectionBean
    @TypeConverter
    public static String sectionBeanToString(SectionBean sectionBean) {
        Gson gson = new Gson();
        String json = gson.toJson(sectionBean);
        return json;
    }

    @TypeConverter
    public static SectionBean stringToSectionBean(String value) {
        Type listType = new TypeToken<SectionBean>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    //////////////////////////// End SectionBean


    //////////////////////////// Start StaticPageUrlBean
    @TypeConverter
    public static String staticPageUrlBeanToString(StaticPageUrlBean sectionBean) {
        Gson gson = new Gson();
        String json = gson.toJson(sectionBean);
        return json;
    }

    @TypeConverter
    public static StaticPageUrlBean stringToStaticPageUrlBean(String value) {
        Type listType = new TypeToken<StaticPageUrlBean>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    //////////////////////////// End StaticPageUrlBean

    @TypeConverter
    public static String userProfileToString(UserProfile userProfile) {
        Gson gson = new Gson();
        String json = gson.toJson(userProfile);
        return json;
    }

    @TypeConverter
    public static UserProfile stringToUserProfile(String value) {
        Type listType = new TypeToken<UserProfile>() {}.getType();
        return new Gson().fromJson(value, listType);
    }


    @TypeConverter
    public static Set<String> stringToSet(String value) {
        Type listType = new TypeToken<Set<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String setToString(Set<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }


    //////////////////////////// Start Configuration's Bean

    @TypeConverter
    public static String appThemeBeanToString(AppThemeBean userProfile) {
        Gson gson = new Gson();
        String json = gson.toJson(userProfile);
        return json;
    }

    @TypeConverter
    public static AppThemeBean stringToAppThemeBean(String value) {
        Type listType = new TypeToken<AppThemeBean>() {}.getType();
        return new Gson().fromJson(value, listType);
    }


    @TypeConverter
    public static String adsBeanToString(AdsBean adsBean) {
        Gson gson = new Gson();
        String json = gson.toJson(adsBean);
        return json;
    }

    @TypeConverter
    public static AdsBean stringToAdsBean(String value) {
        Type listType = new TypeToken<AdsBean>() {}.getType();
        return new Gson().fromJson(value, listType);
    }


    @TypeConverter
    public static String taboolaBeanToString(TaboolaBean taboolaBean) {
        Gson gson = new Gson();
        String json = gson.toJson(taboolaBean);
        return json;
    }

    @TypeConverter
    public static TaboolaBean stringToTaboolaBean(String value) {
        Type listType = new TypeToken<TaboolaBean>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String searchOptionBeanToString(SearchOptionBean userProfile) {
        Gson gson = new Gson();
        String json = gson.toJson(userProfile);
        return json;
    }

    @TypeConverter
    public static SearchOptionBean stringToSearchOptionBean(String value) {
        Type listType = new TypeToken<SearchOptionBean>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String PlaceHolderToString(PlaceHolder widgetIndex) {
        Gson gson = new Gson();
        String json = gson.toJson(widgetIndex);
        return json;
    }

    @TypeConverter
    public static PlaceHolder stringToPlaceHolder(String value) {
        Type listType = new TypeToken<PlaceHolder>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String OtherIconsDownloadUrlsBeanToString(OtherIconsDownloadUrls otherIconsDownloadUrls) {
        Gson gson = new Gson();
        String json = gson.toJson(otherIconsDownloadUrls);
        return json;
    }

    @TypeConverter
    public static OtherIconsDownloadUrls stringToOtherIconsDownloadUrlsBean(String value) {
        Type listType = new TypeToken<OtherIconsDownloadUrls>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String ContentUrlToString(ContentUrl contentUrl) {
        Gson gson = new Gson();
        String json = gson.toJson(contentUrl);
        return json;
    }

    @TypeConverter
    public static ContentUrl stringToContentUrl(String value) {
        Type listType = new TypeToken<ContentUrl>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static List<UrlBean> stringToUrlBeanList(String value) {
        Type listType = new TypeToken<List<UrlBean>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String urlBeanListToString(List<UrlBean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<WidgetIndex> stringToWidgetIndexList(String value) {
        Type listType = new TypeToken<List<WidgetIndex>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String widgetIndexListToString(List<WidgetIndex> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static List<TabsBean> stringToTabsBeanList(String value) {
        Type listType = new TypeToken<List<TabsBean>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String tabsBeanListToString(List<TabsBean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static String ImportantMsgToString(ImportantMsg importantMsg) {
        Gson gson = new Gson();
        String json = gson.toJson(importantMsg);
        return json;
    }

    @TypeConverter
    public static ImportantMsg stringToImportantMsg(String value) {
        Type listType = new TypeToken<ImportantMsg>() {}.getType();
        return new Gson().fromJson(value, listType);
    }


    @TypeConverter
    public static String widgetIndexToString(WidgetIndex widgetIndex) {
        Gson gson = new Gson();
        String json = gson.toJson(widgetIndex);
        return json;
    }

    @TypeConverter
    public static WidgetIndex stringToWidgetIndex(String value) {
        Type listType = new TypeToken<WidgetIndex>() {}.getType();
        return new Gson().fromJson(value, listType);
    }


    /////////////////////////// Start List<OptionsBean>
    @TypeConverter
    public static List<TableOptional.OptionsBean> stringToOptionsBeanList(String value) {
        Type listType = new TypeToken<List<TableOptional.OptionsBean>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String OptionsBeanListToString(List<TableOptional.OptionsBean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
    //////////////////////////// End List<OptionsBean>

}
