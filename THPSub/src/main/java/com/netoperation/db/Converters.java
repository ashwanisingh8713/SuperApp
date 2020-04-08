package com.netoperation.db;


import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netoperation.model.RecoBean;
import com.netoperation.model.UserProfile;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class Converters {

    @TypeConverter
    public static List<RecoBean> stringToBeanList(String value) {
        Type listType = new TypeToken<List<RecoBean>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String beanListToString(List<RecoBean> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static String recobeanToString(RecoBean articleBean) {
        Gson gson = new Gson();
        String json = gson.toJson(articleBean);
        return json;
    }

    @TypeConverter
    public static RecoBean stringToRecobean(String value) {
        Type listType = new TypeToken<RecoBean>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

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
