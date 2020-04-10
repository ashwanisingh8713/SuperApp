package com.netoperation.db;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netoperation.model.ArticleBean;
import com.netoperation.model.SectionBean;
import com.netoperation.model.StaticPageUrlBean;
import com.netoperation.model.UserProfile;

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


}
