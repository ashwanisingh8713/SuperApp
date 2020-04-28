package com.netoperation.retrofit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.netoperation.model.BreifingModelNew;
import com.netoperation.model.HomeData;
import com.netoperation.model.KeyValueModel;
import com.netoperation.model.MPConfigurationModel;
import com.netoperation.model.MPCycleDurationModel;
import com.netoperation.model.PlanRecoModel;
import com.netoperation.model.PrefListModel;
import com.netoperation.model.RecomendationData;
import com.netoperation.model.SearchedArticleModel;
import com.netoperation.model.SectionAndWidget;
import com.netoperation.model.SectionContentFromServer;
import com.netoperation.model.UserChoice;
import com.netoperation.model.UserPlanList;
import com.netoperation.model.SelectedPrefModel;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ashwanisingh on 04/23/19.
 */

public interface ServiceAPIs {


    @POST("/userreco")
    Observable<JsonElement> recommendation(@Body JsonObject recommendationBody);

    @POST("/taiauth/login/HINDU")
    Observable<JsonElement> login(@Body JsonObject loginBody);

    @POST("/taiauth/regSubmit/HINDU")
    Observable<JsonElement> signup(@Body JsonObject logoutBody);

    @POST("/taiauth/logout/hindu")
    Observable<JsonElement> logout(@Body JsonObject loginBody);

    @POST("/taiauth/userVerify/HINDU")
    Observable<JsonElement> userVerification(@Body JsonObject userVerificationBody);

    @POST("/taiauth/resetPassword/HINDU")
    Observable<JsonElement> resetPassword(@Body JsonObject resetPasswordBody);

    @POST("/taiauth/userInfo/HINDU")
    Observable<JsonElement> userInfo(@Body JsonObject userInfoBody);

    @POST("/taiauth/updateUserInfo/HINDU")
    Observable<JsonElement> editProfile(@Body JsonObject editProfileBody);

    @POST("/taiauth/validateOtp/HINDU")
    Observable<JsonElement> validateOtp(@Body JsonObject validateOtpBody);

    @POST("/taiauth/updatePassword/HINDU")
    Observable<JsonElement> updatePassword(@Body JsonObject updatePasswordBody);

    @POST("/taiauth/updateAccountStatus/HINDU")
    Observable<JsonElement> suspendAccount(@Body JsonObject suspendAccountBody);

    @POST("/taiauth/updateAccountStatus/HINDU")
    Observable<JsonElement> deleteAccount(@Body JsonObject deleteAccountBody);

    @GET("/mydashboard/userreco/hindu")
    Observable<RecomendationData> getRecommendation(@Query("userid") String userid, @Query("recotype") String recotype,
                                                    @Query("size") String size, @Query("siteid") String siteid, @Query("requestSource") String requestSource);

    @POST("/mydashboard/userchoice/HINDU")
    Observable<JsonElement> createBookmarkFavLike(@Body JsonObject bookmarkFavLikeBody);

    @GET("/mydashboard/userchoicelist/HINDU")
    Observable<List<UserChoice>> getBookmarkFavLike(@Query("userid") String userid, @Query("siteid") String siteid);

    @GET("")
    Observable<SearchedArticleModel> searchArticleByIDFromServer(@Url String url);


    @GET("")
    Observable<BreifingModelNew> getBriefing(@Url String url);

    @GET("")
    Observable<PrefListModel> getAllPreferences(@Url String url);

    @GET("taiauth/list/HINDU")
    Observable<ArrayList<KeyValueModel>> getCountry(@Query("type") String type);

    @GET("taiauth/list/HINDU")
    Observable<ArrayList<KeyValueModel>> getState(@Query("type") String type, @Query("country") String country);

    @POST("taiauth/updateUserInfo/HINDU")
    Observable<JsonElement> updateProfile(@Body JsonObject updateProfile);

    @POST("taiauth/updateUserInfo/HINDU")
    Observable<JsonElement> updateAddress(@Body JsonObject updateProfile);

    @POST("taiauth/userPreference/hindu")
    Observable<JsonElement> setPersonalise(@Body JsonObject updateProfile);

    @POST("taiauth/userPreference/hindu")
    Observable<SelectedPrefModel> getPersonalise(@Body JsonObject updateProfile);

    @GET("charging/transaction/detail/HINDU")
    Observable<JsonElement> getTxnHistory(@Query("userid") String userid, @Query("pageno") String pageno, @Query("siteId") String siteId, @Query("requestSource") String requestSource);

    @GET("subscription/getuserplaninfo/HINDU")
    Observable<UserPlanList> getUserPlanInfo(@Query("userid") String userid, @Query("siteid") String siteid, @Query("requestSource") String requestSource);

    @GET("subscription/getplaninfo/HINDU")
    Observable<PlanRecoModel> getRecommendedPlan(@Query("siteid") String siteid, @Query("tagid") String tagid,
                                                 @Query("isInd") String isInd, @Query("isPlt") String isPlt);
    @POST("subscription/createsub/HINDU")
    Observable<JsonElement> createSubscription(@Body JsonObject subscriptionBody);



    @POST("taiauth/socialLogin/HINDU")
    Observable<JsonElement> socialLogin(@Body JsonObject loginDetails);

    @POST("")
    Observable<JsonElement> getChecksumHash(@Url String url, @Body JsonObject checksumHashAPIBody);

    @FormUrlEncoded
    @POST("")
    Observable<JsonElement> verifyPaytmTransactionStatusAPI(@Url String url,
                                                            @Field("ORDERID") String ORDERID,
                                                            @Field("MID") String MID,
                                                            @Field("TXNID") String TXNID,
                                                            @Field("TXNAMOUNT") String TXNAMOUNT,
                                                            @Field("PAYMENTMODE") String PAYMENTMODE,
                                                            @Field("CURRENCY") String CURRENCY,
                                                            @Field("TXNDATE") String TXNDATE,
                                                            @Field("STATUS") String STATUS,
                                                            @Field("RESPCODE") String RESPCODE,
                                                            @Field("RESPMSG") String RESPMSG,
                                                            @Field("GATEWAYNAME") String GATEWAYNAME,
                                                            @Field("BANKTXNID") String BANKTXNID,
                                                            @Field("BANKNAME") String BANKNAME,
                                                            @Field("CHECKSUMHASH") String CHECKSUMHASH);


    @GET
    Observable<JsonElement> getSubsWebviewUrl(@Url String s);

    @POST("/tait/appaudience/HINDU")
    Observable<Void> eventCapture(@Body JsonObject captureBody);


    @POST("/subscription/createfreesub/HINDU")
    Observable<JsonElement> freePlan(@Body JsonObject recommendationBody);


    @GET("")
    Observable<MPConfigurationModel> mpConfigurationAPI(@Url String url);

    @GET("")
    Observable<MPCycleDurationModel> mpCycleDurationAPI(@Url String url);


    // ######################################################################################################## //
    // ##################################### DEFAULT TH APIS ################################################## //
    // ######################################################################################################## //

    @POST("") // sectionList_v4.php
    Observable<SectionAndWidget> sectionList(@Url String url, @Body JsonObject jsonObject);

    @POST("") // sectionList_v4.php
    Observable<JsonElement> sectionListForJson(@Url String url, @Body JsonObject jsonObject);

    @POST("") // newsFeed.php
    Observable<HomeData> homeContent(@Url String url, @Body JsonObject jsonObject);

    @POST("") // section-content.php
    Observable<SectionContentFromServer> sectionContent(@Url String url, @Body JsonObject jsonObject);

    @GET("") // url = "https://api.vuukle.com/api/v1/Comments/getCommentCountListByHost?host=thehindu.com&articleIds="+articleId;
    Observable<JsonElement> getCommentCount(@Url String url);

    @GET("") // newsLetter.php
    Observable<JsonElement> DailyDigestApi(@Url String url);


}
