package com.nect.friendlymony.Retrofit;

import com.nect.friendlymony.Model.BaseResponse;
import com.nect.friendlymony.Model.Block.BlockResponse;
import com.nect.friendlymony.Model.Chat.ConversationResponse;
import com.nect.friendlymony.Model.CheckLogin.LoginCheckResponse;
import com.nect.friendlymony.Model.CommonResponse.CommonResponse;
import com.nect.friendlymony.Model.Conversation.ConversationListResponse;
import com.nect.friendlymony.Model.Crused.CrushesResponse;
import com.nect.friendlymony.Model.Feeddetail.FeedDetailResponse;
import com.nect.friendlymony.Model.Feeds.FeedResponse;
import com.nect.friendlymony.Model.GetOnlineUserStatus.ResponseGetOnlineUserStatus;
import com.nect.friendlymony.Model.GetSubscriptions.ResponseGetSubscriptions;
import com.nect.friendlymony.Model.Images.ImageResponse;
import com.nect.friendlymony.Model.Login.LoginResponse;
import com.nect.friendlymony.Model.Login.UserResponse;
import com.nect.friendlymony.Model.Questions.QuestionResponse;
import com.nect.friendlymony.Model.Razorpay.OrderIdResponse;
import com.nect.friendlymony.Model.Referral.ReferralUserResponse;
import com.nect.friendlymony.Model.ReportUser.ResponseReportUserReasons;
import com.nect.friendlymony.Model.Subsciption.SubcriptionResponse;
import com.nect.friendlymony.Model.buy.CountCrushResponse;
import com.nect.friendlymony.Model.payment.ResponseStripePayment;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by nectabits on 1/16/2018.
 * An Interface which defines the HTTP operations (Functions or methods)
 */
@SuppressWarnings("unused")
public interface ApiEndpointInterface {

    @GET("getQuestions")
    Call<QuestionResponse> getQuestions();

    @FormUrlEncoded
    @POST("UserRegisteredV1")
    Call<LoginCheckResponse> UserRegisteredV1(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("signupV3")
    Call<LoginResponse> signup(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("checkUserMail")
    Call<BaseResponse> checkUserMail(@FieldMap Map<String, Object> queryMap);


    @FormUrlEncoded
    @POST("userData")
    Call<UserResponse> editProfile(@FieldMap Map<String, Object> queryMap);

    @GET("userProfileImage")
    Call<ImageResponse> userProfileImage();

    @FormUrlEncoded
    @POST("deleteProfileImage")
    Call<BaseResponse> deleteProfileImage(@FieldMap Map<String, Object> queryMap);


    @FormUrlEncoded
    @POST("userLogout")
    Call<BaseResponse> userLogout(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("userLocation")
    Call<BaseResponse> userLocation(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("userLike")
    Call<BaseResponse> userLike(@FieldMap Map<String, Object> queryMap);


    @FormUrlEncoded
    @POST("feeds")
    Call<FeedResponse> feeds(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("feeddetail")
    Call<FeedDetailResponse> feeddetail(@FieldMap Map<String, Object> queryMap);

    @GET("userData")
    Call<UserResponse> userData();

    @GET("CountCrush")
    Call<CountCrushResponse> getCrucshCount();


    @FormUrlEncoded
    @POST("addTransaction")
    Call<BaseResponse> addTransaction(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("subscriptionLowerToUper")
    Call<BaseResponse> subscriptionLowerToUper(@FieldMap Map<String, Object> queryMap);

    @POST("UsedBoost")
    Call<BaseResponse> UsedBoost();

    @Multipart
    @POST("userProfileImage")
    Call<BaseResponse> userProfileImage(@Header("vphotoid") String vphotoid, @Header("Authorization") String Authorization, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("checkReferCode")
    Call<BaseResponse> checkReferCode(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("getReferalUserData")
    Call<ReferralUserResponse> getReferalUserData(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("userLikeDislikeSuperlike")
    Call<CrushesResponse> userLikeDislikeSuperlike(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("MatchResult")
    Call<CrushesResponse> MatchResult(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("usermatch")
    Call<BaseResponse> usermatch(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("notificationCrushInterseted")
    Call<BaseResponse> notificationCrushInterseted(@FieldMap Map<String, Object> queryMap);


    @FormUrlEncoded
    @POST("GetSubscriptionDetails")
    Call<SubcriptionResponse> GetSubscriptionDetails(@FieldMap Map<String, Object> queryMap);

    //setting

    @FormUrlEncoded
    @POST("hideShowProfile")
    Call<BaseResponse> hideShowProfile(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("cancelMyAccount")
    Call<BaseResponse> cancelMyAccount(@FieldMap Map<String, Object> queryMap);

    @POST("chatSubscriptionValidate")
    Call<BaseResponse> chatSubscriptionValidate();


    //chat
    @FormUrlEncoded
    @POST("StartConversation")
    Call<ConversationResponse> StartConversation(@FieldMap Map<String, Object> queryMap);

    @POST("getConversation")
    Call<ConversationListResponse> getConversation();

    @POST("send")
    Call<BaseResponse> sendFCM(@Header("Content-Type") String cType, @Header("Authorization") String auth, @Body RequestBody params);


    @FormUrlEncoded
    @POST("orders")
    Call<OrderIdResponse> getOrderId(@Field("amount") int amount, @Field("currency") String currency);


    @FormUrlEncoded
    @POST("isblockChat")
    Call<BlockResponse> isblockChat(@Field("conversation_id") String conversation_id);

    @FormUrlEncoded
    @POST("lastConversation")
    Call<BaseResponse> lastConversation(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("blockChat")
    Call<BaseResponse> blockChat(@Field("sender") String sender, @Field("recipient") String recipient, @Field("isBlock") String isBlock);



   /* @FormUrlEncoded
    @POST("signupV3")
    Call<RegisterDataResponse> signupV3(@Field("facebook_id") String facebook_id, @Field("is_sign_up") String is_sign_up,
                                        @Field("country_code") String country_code, @Field("mobile_no") String mobile_no,
                                        @Field("name") String name, @Field("email") String email,
                                        @Field("device_token") String device_token, @Field("device_type") String device_type,
                                        @Field("vAge") String vAge, @Field("vGender") String vGender,
                                        @Field("vRadius") String vRadius, @Field("vAbout") String vAbout,
                                        @Field("is_qualification") String is_qualification, @Field("is_smoke") String is_smoke,
                                        @Field("is_drink") String is_drink, @Field("is_politics") String is_politics,
                                        @Field("is_employee") String is_employee, @Field("is_earn") String is_earn,
                                        @Field("last_name") String last_name, @Field("vBirthdate") String vBirthdate,
                                        @Field("vShow_me") String vShow_me, @Field("vAge_min") String vAge_min,
                                        @Field("vAge_max") String vAge_max, @Field("distance_type") String distance_type
    );

    @FormUrlEncoded
    @POST("signupV3")
    Call<RegisterDataResponse> signupV3Google(@Field("google_id") String google_id, @Field("is_sign_up") String is_sign_up,
                                              @Field("country_code") String country_code, @Field("mobile_no") String mobile_no,
                                              @Field("name") String name, @Field("email") String email,
                                              @Field("device_token") String device_token, @Field("device_type") String device_type,
                                              @Field("vAge") String vAge, @Field("vGender") String vGender,
                                              @Field("vRadius") String vRadius, @Field("vAbout") String vAbout,
                                              @Field("is_qualification") String is_qualification, @Field("is_smoke") String is_smoke,
                                              @Field("is_drink") String is_drink, @Field("is_politics") String is_politics,
                                              @Field("is_employee") String is_employee, @Field("is_earn") String is_earn,
                                              @Field("last_name") String last_name, @Field("vBirthdate") String vBirthdate,
                                              @Field("vShow_me") String vShow_me, @Field("vAge_min") String vAge_min,
                                              @Field("vAge_max") String vAge_max, @Field("distance_type") String distance_type
    );

    @FormUrlEncoded
    @POST("signupV3")
    Call<RegisterDataResponse> signupV3Phone(@Field("is_sign_up") String is_sign_up,
                                             @Field("country_code") String country_code, @Field("mobile_no") String mobile_no,
                                             @Field("name") String name, @Field("email") String email,
                                             @Field("device_token") String device_token, @Field("device_type") String device_type,
                                             @Field("vAge") String vAge, @Field("vGender") String vGender,
                                             @Field("vRadius") String vRadius, @Field("vAbout") String vAbout,
                                             @Field("is_qualification") String is_qualification, @Field("is_smoke") String is_smoke,
                                             @Field("is_drink") String is_drink, @Field("is_politics") String is_politics,
                                             @Field("is_employee") String is_employee, @Field("is_earn") String is_earn,
                                             @Field("last_name") String last_name, @Field("vBirthdate") String vBirthdate,
                                             @Field("vShow_me") String vShow_me, @Field("vAge_min") String vAge_min,
                                             @Field("vAge_max") String vAge_max, @Field("distance_type") String distance_type
    );
*/

    @GET("getReportTypes")
    Call<ResponseReportUserReasons> GetReportReasons();

    /*@FormUrlEncoded
    @POST("getReportTypes")
    Call<ResponseReportUserReasons> GetReportReasons(@FieldMap Map<String, Object> queryMap);*/
    @FormUrlEncoded
    @POST("reportUser")
    Call<CommonResponse> getReportUser(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("blockUser")
    Call<CommonResponse> getBlockUser(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("stripePaymentProcess")
    Call<ResponseStripePayment> getStripePaymentProcess(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("getSubscriptions")
    Call<ResponseGetSubscriptions> getSubscriptions(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("getBoostRecords")
    Call<ResponseGetSubscriptions> getBoostRecords(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("getCrushRecords")
    Call<ResponseGetSubscriptions> getCrushRecords(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("userCancelSubscription")
    Call<CommonResponse> getUserCancelSubscription(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("updateUserOnlineStatus")
    Call<CommonResponse> getUpdateUserOnlineStatus(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("signupValidationFreeTrial")
    Call<CommonResponse> getSignupValidationFreeTrial(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("getUserOnlineStatus")
    Call<ResponseGetOnlineUserStatus> getUserOnlineStatus(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("token")
    Call<String> getAppleLoginInfo(@FieldMap Map<String, Object> queryMap);

    @FormUrlEncoded
    @POST("readConversation")
    Call<CommonResponse> readConversation(@FieldMap Map<String, Object> queryMap);
}
/*
"id": 5,
        "name": "Boost Plan 1",
        "price": "300",
        "qty": "100",
        "converted_price": "22893.14",
        "status": "Yes",
        "createdAt": "2020-04-02T00:00:00.000Z",
        "updatedAt": "2020-04-02T00:00:00.000Z"*/
