package com.netoperation.retrofit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.netoperation.default_db.TableOptional;
import com.netoperation.model.BreifingModelNew;
import com.netoperation.model.ConfigurationData;
import com.netoperation.model.HomeData;
import com.netoperation.model.KeyValueModel;
import com.netoperation.model.MPConfigurationModel;
import com.netoperation.model.MPCycleDurationModel;
import com.netoperation.model.NSE_BSE_Data;
import com.netoperation.model.PlanRecoModel;
import com.netoperation.model.PrefListModel;
import com.netoperation.model.RecomendationData;
import com.netoperation.model.SearchedArticleModel;
import com.netoperation.model.SectionAndWidget;
import com.netoperation.model.SectionContentFromServer;
import com.netoperation.model.USPData;
import com.netoperation.model.SelectedPrefModel;
import com.netoperation.model.UpdateModel;
import com.netoperation.model.UserChoice;
import com.netoperation.model.UserPlanList;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by ashwanisingh on 04/23/19.
 */

public interface ServiceAPIs {


    @POST("/userreco")
    Observable<JsonElement> recommendation(@Body JsonObject recommendationBody);

    @POST(UrlPath.login)
    Observable<JsonElement> login(@Header("Origin") String origin, @Body JsonObject loginBody);

    @POST(UrlPath.socialLogin)
    Observable<JsonElement> socialLogin(@Header("Origin") String origin, @Body JsonObject loginDetails);

    @POST(UrlPath.signup)
    Observable<JsonElement> signup(@Header("Origin") String origin, @Body JsonObject logoutBody);

    @POST(UrlPath.logout)
    Observable<JsonElement> logout(@Header("Authorization") String authorization, @Header("Origin") String origin,  @Body JsonObject loginBody);

    @POST(UrlPath.userVerification)
    Observable<JsonElement> userVerification(@Header("Origin") String origin, @Body JsonObject userVerificationBody);

    @POST(UrlPath.resetPassword)
    Observable<JsonElement> resetPassword(@Header("Origin") String origin, @Body JsonObject resetPasswordBody);

    @POST(UrlPath.userInfo)
    Observable<JsonElement> userInfo(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject userInfoBody);

    @POST(UrlPath.editProfile)
    Observable<JsonElement> editProfile(@Header("Authorization") String authorization, @Header("Origin") String origin,@Body JsonObject editProfileBody);

    @POST(UrlPath.validateOtp)
    Observable<JsonElement> validateOtp(@Header("Origin") String origin, @Body JsonObject validateOtpBody);

    @POST(UrlPath.updatePassword)
    Observable<JsonElement> updatePassword(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject updatePasswordBody);

    @POST(UrlPath.suspendAccount)
    Observable<JsonElement> suspendAccount(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject suspendAccountBody);

    @POST(UrlPath.deleteAccount)
    Observable<JsonElement> deleteAccount(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject deleteAccountBody);

    @GET(UrlPath.getRecommendation)
    Observable<RecomendationData> getRecommendation(@Header("Authorization") String authorization, @Header("Origin") String origin, @Query("userid") String userid, @Query("recotype") String recotype,
                                                    @Query("size") String size, @Query("siteid") String siteid, @Query("requestSource") String requestSource);

    @POST(UrlPath.createBookmarkFavLike)
    Observable<JsonElement> createBookmarkFavLike(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject bookmarkFavLikeBody);

    @GET(UrlPath.getBookmarkFavLike)
    Observable<List<UserChoice>> getBookmarkFavLike(@Header("Authorization") String authorization, @Header("Origin") String origin, @Query("userid") String userid, @Query("siteid") String siteid);

    @GET("")
    Observable<SearchedArticleModel> searchArticleByIDFromServer(@Url String url);


    @GET("")
    Observable<BreifingModelNew> getBriefing(@Url String url);

    @GET("")
    Observable<PrefListModel> getAllPreferences(@Url String url);

    @GET(UrlPath.getCountry)
    Observable<ArrayList<KeyValueModel>> getCountry(@Query("type") String type);

    @GET(UrlPath.getState)
    Observable<ArrayList<KeyValueModel>> getState(@Query("type") String type, @Query("country") String country);

    @POST(UrlPath.updateProfile)
    Observable<JsonElement> updateProfile(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject updateProfile);

    @POST(UrlPath.updateAddress)
    Observable<JsonElement> updateAddress(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject updateProfile);

    @POST(UrlPath.setPersonalise)
    Observable<JsonElement> setPersonalise(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject updateProfile);

    @POST(UrlPath.getPersonalise)
    Observable<SelectedPrefModel> getPersonalise(@Header("Authorization") String authorization, @Header("Origin") String origin,@Body JsonObject updateProfile);

    @GET(UrlPath.getTxnHistory)
    Observable<JsonElement> getTxnHistory(@Header("Authorization") String authorization,  @Header("Origin") String origin, @Query("userid") String userid, @Query("pageno") String pageno, @Query("siteId") String siteId, @Query("requestSource") String requestSource);

    @GET(UrlPath.getUserPlanInfo)
    Observable<UserPlanList> getUserPlanInfo(@Header("Authorization") String authorization, @Header("Origin") String origin, @Query("userid") String userid, @Query("siteid") String siteid, @Query("requestSource") String requestSource);

    @GET(UrlPath.getRecommendedPlan)
    Observable<PlanRecoModel> getRecommendedPlan(@Header("Origin") String origin, @Query("siteid") String siteid, @Query("tagid") String tagid,
                                                 @Query("isInd") String isInd, @Query("isPlt") String isPlt);
    @POST(UrlPath.createSubscription)
    Observable<JsonElement> createSubscription(@Header("Authorization") String authorization, @Body JsonObject subscriptionBody);

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

    @POST(UrlPath.eventCapture)
    Observable<Void> eventCapture(@Body JsonObject captureBody);


    @POST(UrlPath.freePlan)
    Observable<JsonElement> freePlan(@Header("Authorization") String authorization, @Header("Origin") String origin, @Body JsonObject recommendationBody);


    // ######################################################################################################## //
    // ##################################### DEFAULT TH APIS ################################################## //
    // ######################################################################################################## //

    @GET("")
    Observable<MPConfigurationModel> mpConfigurationAPI(@Url String url);

    @GET("")
    Observable<MPCycleDurationModel> mpCycleDurationAPI(@Url String url);

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
    Observable<SectionContentFromServer> newsDigest(@Url String url);

    @POST("") // configuration api
    Observable<ConfigurationData> config(@Url String url, @Body JsonObject jsonObject);

    @POST("") // configuration api
    Observable<JsonElement> configUpdateCheck(@Url String url, @Body JsonObject jsonObject);

    @POST("") // Force Update.php
    Observable<UpdateModel> forceUpdate(@Url String url, @Body JsonObject jsonObject);

    @GET("") // BL SENSEX Widget api
    Observable<NSE_BSE_Data> bl_sensexWidget(@Url String url);

    @GET("") //Menu Sequence API
    Single<JsonElement> getMenuSequence(@Url String url);

    @POST("") //USP API
    Observable<USPData> getUSP(@Url String url, @Body JsonObject jsonObject);


}
