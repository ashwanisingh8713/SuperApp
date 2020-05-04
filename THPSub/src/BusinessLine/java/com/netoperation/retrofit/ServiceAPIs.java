package com.netoperation.retrofit;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.netoperation.model.BreifingModelNew;
import com.netoperation.model.KeyValueModel;
import com.netoperation.model.MPConfigurationModel;
import com.netoperation.model.MPCycleDurationModel;
import com.netoperation.model.PlanRecoModel;
import com.netoperation.model.PrefListModel;
import com.netoperation.model.RecomendationData;
import com.netoperation.model.SearchedArticleModel;
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

    @POST("/v1/auth/login/HINDU") // BL
    Observable<JsonElement> login(@Body JsonObject loginBody);

    @POST("/v1/auth/regSubmit/HINDU") // BL
    Observable<JsonElement> signup(@Body JsonObject logoutBody);

    @POST("/v1/auth/logout/hindu") // BL
    Observable<JsonElement> logout(@Header("Authorization") String authorization, @Body JsonObject loginBody);

    @POST("/v1/auth/userVerify/HINDU") // BL
    Observable<JsonElement> userVerification(@Body JsonObject userVerificationBody);

    @POST("/v1/auth/resetPassword/HINDU") // BL
    Observable<JsonElement> resetPassword(@Body JsonObject resetPasswordBody);

    @POST("/v1/auth/userInfo/HINDU") // BL
    Observable<JsonElement> userInfo(@Header("Authorization") String authorization, @Body JsonObject userInfoBody);

    @POST("/v1/auth/updateUserInfo/HINDU") // BL
    Observable<JsonElement> editProfile(@Body JsonObject editProfileBody);

    @POST("/v1/auth/validateOtp/HINDU") // BL
    Observable<JsonElement> validateOtp(@Body JsonObject validateOtpBody);

    @POST("/v1/auth/updatePassword/HINDU") // BL
    Observable<JsonElement> updatePassword(@Body JsonObject updatePasswordBody);

    @POST("/v1/auth/updateAccountStatus/HINDU") // BL
    Observable<JsonElement> suspendAccount(@Body JsonObject suspendAccountBody);

    @POST("/v1/auth/updateAccountStatus/HINDU") // BL
    Observable<JsonElement> deleteAccount(@Body JsonObject deleteAccountBody);

    @GET("/v1/mydashboard/userreco/hindu") // BL
    Observable<RecomendationData> getRecommendation(@Query("userid") String userid, @Query("recotype") String recotype,
                                                    @Query("size") String size, @Query("siteid") String siteid, @Query("requestSource") String requestSource );

    @POST("/v1/mydashboard/userchoice/HINDU") // BL
    Observable<JsonElement> createBookmarkFavLike(@Body JsonObject bookmarkFavLikeBody);

    @GET("/v1/mydashboard/userchoicelist/HINDU") // BL
    Observable<List<UserChoice>> getBookmarkFavLike(@Query("userid") String userid, @Query("siteid") String siteid );

    @GET("")
    Observable<SearchedArticleModel> searchArticleByIDFromServer(@Url String url);


    @GET("")
    Observable<BreifingModelNew> getBriefing(@Url String url);

    @GET("")
    Observable<PrefListModel> getAllPreferences(@Url String url);

    @GET("/v1/auth/list/HINDU") // BL
    Observable<ArrayList<KeyValueModel>> getCountry(@Query("type") String type);

    @GET("/v1/auth/list/HINDU") // BL
    Observable<ArrayList<KeyValueModel>> getState(@Query("type") String type, @Query("country") String country);

    @POST("/v1/auth/updateUserInfo/HINDU") // BL
    Observable<JsonElement> updateProfile(@Body JsonObject updateProfile);

    @POST("/v1/auth/updateUserInfo/HINDU") // BL
    Observable<JsonElement> updateAddress(@Body JsonObject updateProfile);

    @POST("/v1/auth/userPreference/hindu") // BL
    Observable<JsonElement> setPersonalise(@Header("Authorization") String authorization, @Body JsonObject updateProfile);

    @POST("/v1/auth/userPreference/hindu") // BL
    Observable<SelectedPrefModel> getPersonalise(@Body JsonObject updateProfile);

    @GET("/v1/charging/transaction/detail/HINDU") // BL
    Observable<JsonElement> getTxnHistory(@Query("userid") String userid, @Query("pageno") String pageno, @Query("siteId") String siteId, @Query("requestSource") String requestSource);

    @GET("/v1/subscription/getuserplaninfo/HINDU")
    Observable<UserPlanList> getUserPlanInfo(@Query("userid") String userid, @Query("siteid") String siteid, @Query("requestSource") String requestSource);

    @GET("/v1/subscription/getplaninfo/HINDU")
    Observable<PlanRecoModel> getRecommendedPlan(@Query("siteid") String siteid, @Query("tagid") String tagid,
                                                 @Query("isInd") String isInd, @Query("isPlt") String isPlt);
    @POST("/v1/subscription/createsub/HINDU")
    Observable<JsonElement> createSubscription(@Body JsonObject subscriptionBody);



    @POST("/v1/auth/socialLogin/HINDU") // BL
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


    @POST("/v1/subscription/createfreesub/HINDU") // BL
    Observable<JsonElement> freePlan(@Header("Authorization") String authorization, @Body JsonObject recommendationBody);


    @GET("")
    Observable<MPConfigurationModel> mpConfigurationAPI(@Url String url);

    @GET("")
    Observable<MPCycleDurationModel> mpCycleDurationAPI(@Url String url);
}
